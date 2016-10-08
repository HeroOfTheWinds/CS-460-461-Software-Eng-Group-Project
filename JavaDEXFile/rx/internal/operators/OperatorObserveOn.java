package rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable.Operator;
import rx.Producer;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.exceptions.MissingBackpressureException;
import rx.functions.Action0;
import rx.internal.util.RxRingBuffer;
import rx.internal.util.atomic.SpscAtomicArrayQueue;
import rx.internal.util.unsafe.SpscArrayQueue;
import rx.internal.util.unsafe.UnsafeAccess;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.ImmediateScheduler;
import rx.schedulers.TrampolineScheduler;

public final class OperatorObserveOn<T> implements Operator<T, T> {
    private final int bufferSize;
    private final boolean delayError;
    private final Scheduler scheduler;

    private static final class ObserveOnSubscriber<T> extends Subscriber<T> implements Action0 {
        final Subscriber<? super T> child;
        final AtomicLong counter;
        final boolean delayError;
        long emitted;
        Throwable error;
        volatile boolean finished;
        final int limit;
        final NotificationLite<T> on;
        final Queue<Object> queue;
        final Worker recursiveScheduler;
        final AtomicLong requested;

        /* renamed from: rx.internal.operators.OperatorObserveOn.ObserveOnSubscriber.1 */
        class C13471 implements Producer {
            C13471() {
            }

            public void request(long j) {
                if (j > 0) {
                    BackpressureUtils.getAndAddRequest(ObserveOnSubscriber.this.requested, j);
                    ObserveOnSubscriber.this.schedule();
                }
            }
        }

        public ObserveOnSubscriber(Scheduler scheduler, Subscriber<? super T> subscriber, boolean z, int i) {
            this.requested = new AtomicLong();
            this.counter = new AtomicLong();
            this.child = subscriber;
            this.recursiveScheduler = scheduler.createWorker();
            this.delayError = z;
            this.on = NotificationLite.instance();
            if (i <= 0) {
                i = RxRingBuffer.SIZE;
            }
            this.limit = i - (i >> 2);
            if (UnsafeAccess.isUnsafeAvailable()) {
                this.queue = new SpscArrayQueue(i);
            } else {
                this.queue = new SpscAtomicArrayQueue(i);
            }
            request((long) i);
        }

        public void call() {
            long j = 1;
            long j2 = this.emitted;
            Queue queue = this.queue;
            Subscriber subscriber = this.child;
            NotificationLite notificationLite = this.on;
            do {
                long j3 = this.requested.get();
                while (j3 != j2) {
                    boolean z = this.finished;
                    Object poll = queue.poll();
                    boolean z2 = poll == null;
                    if (!checkTerminated(z, z2, subscriber, queue)) {
                        if (z2) {
                            break;
                        }
                        subscriber.onNext(notificationLite.getValue(poll));
                        long j4 = 1 + j2;
                        if (j4 == ((long) this.limit)) {
                            j2 = BackpressureUtils.produced(this.requested, j4);
                            request(j4);
                            j3 = j2;
                            j2 = 0;
                        } else {
                            j2 = j4;
                        }
                    } else {
                        return;
                    }
                }
                if (j3 != j2 || !checkTerminated(this.finished, queue.isEmpty(), subscriber, queue)) {
                    this.emitted = j2;
                    j = this.counter.addAndGet(-j);
                } else {
                    return;
                }
            } while (j != 0);
        }

        boolean checkTerminated(boolean z, boolean z2, Subscriber<? super T> subscriber, Queue<Object> queue) {
            if (subscriber.isUnsubscribed()) {
                queue.clear();
                return true;
            }
            if (z) {
                if (!this.delayError) {
                    Throwable th = this.error;
                    if (th != null) {
                        queue.clear();
                        try {
                            subscriber.onError(th);
                            return true;
                        } finally {
                            this.recursiveScheduler.unsubscribe();
                        }
                    } else if (z2) {
                        try {
                            subscriber.onCompleted();
                            return true;
                        } finally {
                            this.recursiveScheduler.unsubscribe();
                        }
                    }
                } else if (z2) {
                    Throwable th2 = this.error;
                    if (th2 != null) {
                        try {
                            subscriber.onError(th2);
                        } catch (Throwable th3) {
                            this.recursiveScheduler.unsubscribe();
                        }
                    } else {
                        subscriber.onCompleted();
                    }
                    this.recursiveScheduler.unsubscribe();
                }
            }
            return false;
        }

        void init() {
            Subscriber subscriber = this.child;
            subscriber.setProducer(new C13471());
            subscriber.add(this.recursiveScheduler);
            subscriber.add(this);
        }

        public void onCompleted() {
            if (!isUnsubscribed() && !this.finished) {
                this.finished = true;
                schedule();
            }
        }

        public void onError(Throwable th) {
            if (isUnsubscribed() || this.finished) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
                return;
            }
            this.error = th;
            this.finished = true;
            schedule();
        }

        public void onNext(T t) {
            if (!isUnsubscribed() && !this.finished) {
                if (this.queue.offer(this.on.next(t))) {
                    schedule();
                } else {
                    onError(new MissingBackpressureException());
                }
            }
        }

        protected void schedule() {
            if (this.counter.getAndIncrement() == 0) {
                this.recursiveScheduler.schedule(this);
            }
        }
    }

    public OperatorObserveOn(Scheduler scheduler, boolean z) {
        this(scheduler, z, RxRingBuffer.SIZE);
    }

    public OperatorObserveOn(Scheduler scheduler, boolean z, int i) {
        this.scheduler = scheduler;
        this.delayError = z;
        if (i <= 0) {
            i = RxRingBuffer.SIZE;
        }
        this.bufferSize = i;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        if ((this.scheduler instanceof ImmediateScheduler) || (this.scheduler instanceof TrampolineScheduler)) {
            return subscriber;
        }
        Subscriber observeOnSubscriber = new ObserveOnSubscriber(this.scheduler, subscriber, this.delayError, this.bufferSize);
        observeOnSubscriber.init();
        return observeOnSubscriber;
    }
}

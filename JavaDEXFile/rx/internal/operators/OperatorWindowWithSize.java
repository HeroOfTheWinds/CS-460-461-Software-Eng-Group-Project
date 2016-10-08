package rx.internal.operators;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.internal.util.atomic.SpscLinkedArrayQueue;
import rx.subjects.Subject;
import rx.subscriptions.Subscriptions;

public final class OperatorWindowWithSize<T> implements Operator<Observable<T>, T> {
    final int size;
    final int skip;

    static final class WindowExact<T> extends Subscriber<T> implements Action0 {
        final Subscriber<? super Observable<T>> actual;
        final Subscription cancel;
        int index;
        final int size;
        Subject<T, T> window;
        final AtomicInteger wip;

        /* renamed from: rx.internal.operators.OperatorWindowWithSize.WindowExact.1 */
        class C14251 implements Producer {
            C14251() {
            }

            public void request(long j) {
                if (j < 0) {
                    throw new IllegalArgumentException("n >= 0 required but it was " + j);
                } else if (j != 0) {
                    WindowExact.this.request(BackpressureUtils.multiplyCap((long) WindowExact.this.size, j));
                }
            }
        }

        public WindowExact(Subscriber<? super Observable<T>> subscriber, int i) {
            this.actual = subscriber;
            this.size = i;
            this.wip = new AtomicInteger(1);
            this.cancel = Subscriptions.create(this);
            add(this.cancel);
            request(0);
        }

        public void call() {
            if (this.wip.decrementAndGet() == 0) {
                unsubscribe();
            }
        }

        Producer createProducer() {
            return new C14251();
        }

        public void onCompleted() {
            Subject subject = this.window;
            if (subject != null) {
                this.window = null;
                subject.onCompleted();
            }
            this.actual.onCompleted();
        }

        public void onError(Throwable th) {
            Subject subject = this.window;
            if (subject != null) {
                this.window = null;
                subject.onError(th);
            }
            this.actual.onError(th);
        }

        public void onNext(T t) {
            int i = this.index;
            Subject subject = this.window;
            if (i == 0) {
                this.wip.getAndIncrement();
                subject = UnicastSubject.create(this.size, this);
                this.window = subject;
                this.actual.onNext(subject);
            }
            i++;
            subject.onNext(t);
            if (i == this.size) {
                this.index = 0;
                this.window = null;
                subject.onCompleted();
                return;
            }
            this.index = i;
        }
    }

    static final class WindowOverlap<T> extends Subscriber<T> implements Action0 {
        final Subscriber<? super Observable<T>> actual;
        final Subscription cancel;
        volatile boolean done;
        final AtomicInteger drainWip;
        Throwable error;
        int index;
        int produced;
        final Queue<Subject<T, T>> queue;
        final AtomicLong requested;
        final int size;
        final int skip;
        final ArrayDeque<Subject<T, T>> windows;
        final AtomicInteger wip;

        final class WindowOverlapProducer extends AtomicBoolean implements Producer {
            private static final long serialVersionUID = 4625807964358024108L;

            WindowOverlapProducer() {
            }

            public void request(long j) {
                if (j < 0) {
                    throw new IllegalArgumentException("n >= 0 required but it was " + j);
                } else if (j != 0) {
                    WindowOverlap windowOverlap = WindowOverlap.this;
                    if (get() || !compareAndSet(false, true)) {
                        WindowOverlap.this.request(BackpressureUtils.multiplyCap((long) windowOverlap.skip, j));
                    } else {
                        windowOverlap.request(BackpressureUtils.addCap(BackpressureUtils.multiplyCap((long) windowOverlap.skip, j - 1), (long) windowOverlap.size));
                    }
                    BackpressureUtils.getAndAddRequest(windowOverlap.requested, j);
                    windowOverlap.drain();
                }
            }
        }

        public WindowOverlap(Subscriber<? super Observable<T>> subscriber, int i, int i2) {
            this.actual = subscriber;
            this.size = i;
            this.skip = i2;
            this.wip = new AtomicInteger(1);
            this.windows = new ArrayDeque();
            this.drainWip = new AtomicInteger();
            this.requested = new AtomicLong();
            this.cancel = Subscriptions.create(this);
            add(this.cancel);
            request(0);
            this.queue = new SpscLinkedArrayQueue(((i2 - 1) + i) / i2);
        }

        public void call() {
            if (this.wip.decrementAndGet() == 0) {
                unsubscribe();
            }
        }

        boolean checkTerminated(boolean z, boolean z2, Subscriber<? super Subject<T, T>> subscriber, Queue<Subject<T, T>> queue) {
            if (subscriber.isUnsubscribed()) {
                queue.clear();
                return true;
            }
            if (z) {
                Throwable th = this.error;
                if (th != null) {
                    queue.clear();
                    subscriber.onError(th);
                    return true;
                } else if (z2) {
                    subscriber.onCompleted();
                    return true;
                }
            }
            return false;
        }

        Producer createProducer() {
            return new WindowOverlapProducer();
        }

        void drain() {
            AtomicInteger atomicInteger = this.drainWip;
            if (atomicInteger.getAndIncrement() == 0) {
                Subscriber subscriber = this.actual;
                Queue queue = this.queue;
                int i = 1;
                while (true) {
                    long j = this.requested.get();
                    long j2 = 0;
                    while (j2 != j) {
                        boolean z = this.done;
                        Subject subject = (Subject) queue.poll();
                        boolean z2 = subject == null;
                        if (!checkTerminated(z, z2, subscriber, queue)) {
                            if (z2) {
                                break;
                            }
                            subscriber.onNext(subject);
                            j2 = 1 + j2;
                        } else {
                            return;
                        }
                    }
                    if (j2 != j || !checkTerminated(this.done, queue.isEmpty(), subscriber, queue)) {
                        if (!(j2 == 0 || j == Long.MAX_VALUE)) {
                            this.requested.addAndGet(-j2);
                        }
                        int addAndGet = atomicInteger.addAndGet(-i);
                        if (addAndGet != 0) {
                            i = addAndGet;
                        } else {
                            return;
                        }
                    }
                    return;
                }
            }
        }

        public void onCompleted() {
            Iterator it = this.windows.iterator();
            while (it.hasNext()) {
                ((Subject) it.next()).onCompleted();
            }
            this.windows.clear();
            this.done = true;
            drain();
        }

        public void onError(Throwable th) {
            Iterator it = this.windows.iterator();
            while (it.hasNext()) {
                ((Subject) it.next()).onError(th);
            }
            this.windows.clear();
            this.error = th;
            this.done = true;
            drain();
        }

        public void onNext(T t) {
            int i = this.index;
            ArrayDeque arrayDeque = this.windows;
            if (i == 0 && !this.actual.isUnsubscribed()) {
                this.wip.getAndIncrement();
                UnicastSubject create = UnicastSubject.create(16, this);
                arrayDeque.offer(create);
                this.queue.offer(create);
                drain();
            }
            Iterator it = this.windows.iterator();
            while (it.hasNext()) {
                ((Subject) it.next()).onNext(t);
            }
            int i2 = this.produced + 1;
            if (i2 == this.size) {
                this.produced = i2 - this.skip;
                Subject subject = (Subject) arrayDeque.poll();
                if (subject != null) {
                    subject.onCompleted();
                }
            } else {
                this.produced = i2;
            }
            i2 = i + 1;
            if (i2 == this.skip) {
                this.index = 0;
            } else {
                this.index = i2;
            }
        }
    }

    static final class WindowSkip<T> extends Subscriber<T> implements Action0 {
        final Subscriber<? super Observable<T>> actual;
        final Subscription cancel;
        int index;
        final int size;
        final int skip;
        Subject<T, T> window;
        final AtomicInteger wip;

        final class WindowSkipProducer extends AtomicBoolean implements Producer {
            private static final long serialVersionUID = 4625807964358024108L;

            WindowSkipProducer() {
            }

            public void request(long j) {
                if (j < 0) {
                    throw new IllegalArgumentException("n >= 0 required but it was " + j);
                } else if (j != 0) {
                    WindowSkip windowSkip = WindowSkip.this;
                    if (get() || !compareAndSet(false, true)) {
                        windowSkip.request(BackpressureUtils.multiplyCap(j, (long) windowSkip.skip));
                    } else {
                        windowSkip.request(BackpressureUtils.addCap(BackpressureUtils.multiplyCap(j, (long) windowSkip.size), BackpressureUtils.multiplyCap((long) (windowSkip.skip - windowSkip.size), j - 1)));
                    }
                }
            }
        }

        public WindowSkip(Subscriber<? super Observable<T>> subscriber, int i, int i2) {
            this.actual = subscriber;
            this.size = i;
            this.skip = i2;
            this.wip = new AtomicInteger(1);
            this.cancel = Subscriptions.create(this);
            add(this.cancel);
            request(0);
        }

        public void call() {
            if (this.wip.decrementAndGet() == 0) {
                unsubscribe();
            }
        }

        Producer createProducer() {
            return new WindowSkipProducer();
        }

        public void onCompleted() {
            Subject subject = this.window;
            if (subject != null) {
                this.window = null;
                subject.onCompleted();
            }
            this.actual.onCompleted();
        }

        public void onError(Throwable th) {
            Subject subject = this.window;
            if (subject != null) {
                this.window = null;
                subject.onError(th);
            }
            this.actual.onError(th);
        }

        public void onNext(T t) {
            int i = this.index;
            Subject subject = this.window;
            if (i == 0) {
                this.wip.getAndIncrement();
                subject = UnicastSubject.create(this.size, this);
                this.window = subject;
                this.actual.onNext(subject);
            }
            i++;
            if (subject != null) {
                subject.onNext(t);
            }
            if (i == this.size) {
                this.index = i;
                this.window = null;
                subject.onCompleted();
            } else if (i == this.skip) {
                this.index = 0;
            } else {
                this.index = i;
            }
        }
    }

    public OperatorWindowWithSize(int i, int i2) {
        this.size = i;
        this.skip = i2;
    }

    public Subscriber<? super T> call(Subscriber<? super Observable<T>> subscriber) {
        Subscriber windowExact;
        if (this.skip == this.size) {
            windowExact = new WindowExact(subscriber, this.size);
            subscriber.add(windowExact.cancel);
            subscriber.setProducer(windowExact.createProducer());
            return windowExact;
        } else if (this.skip > this.size) {
            windowExact = new WindowSkip(subscriber, this.size, this.skip);
            subscriber.add(windowExact.cancel);
            subscriber.setProducer(windowExact.createProducer());
            return windowExact;
        } else {
            windowExact = new WindowOverlap(subscriber, this.size, this.skip);
            subscriber.add(windowExact.cancel);
            subscriber.setProducer(windowExact.createProducer());
            return windowExact;
        }
    }
}

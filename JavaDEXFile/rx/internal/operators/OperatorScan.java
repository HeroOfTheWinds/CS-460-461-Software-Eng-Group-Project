package rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable.Operator;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func0;
import rx.functions.Func2;
import rx.internal.util.atomic.SpscLinkedAtomicQueue;
import rx.internal.util.unsafe.SpscLinkedQueue;
import rx.internal.util.unsafe.UnsafeAccess;

public final class OperatorScan<R, T> implements Operator<R, T> {
    private static final Object NO_INITIAL_VALUE;
    final Func2<R, ? super T, R> accumulator;
    private final Func0<R> initialValueFactory;

    /* renamed from: rx.internal.operators.OperatorScan.1 */
    class C13731 implements Func0<R> {
        final /* synthetic */ Object val$initialValue;

        C13731(Object obj) {
            this.val$initialValue = obj;
        }

        public R call() {
            return this.val$initialValue;
        }
    }

    /* renamed from: rx.internal.operators.OperatorScan.2 */
    class C13742 extends Subscriber<T> {
        boolean once;
        final /* synthetic */ Subscriber val$child;
        R value;

        C13742(Subscriber subscriber, Subscriber subscriber2) {
            this.val$child = subscriber2;
            super(subscriber);
        }

        public void onCompleted() {
            this.val$child.onCompleted();
        }

        public void onError(Throwable th) {
            this.val$child.onError(th);
        }

        public void onNext(T t) {
            Object call;
            if (this.once) {
                try {
                    call = OperatorScan.this.accumulator.call(this.value, t);
                } catch (Throwable th) {
                    Exceptions.throwOrReport(th, this.val$child, t);
                    return;
                }
            }
            this.once = true;
            this.value = call;
            this.val$child.onNext(call);
        }
    }

    /* renamed from: rx.internal.operators.OperatorScan.3 */
    class C13753 extends Subscriber<T> {
        final /* synthetic */ Object val$initialValue;
        final /* synthetic */ InitialProducer val$ip;
        private R value;

        C13753(Object obj, InitialProducer initialProducer) {
            this.val$initialValue = obj;
            this.val$ip = initialProducer;
            this.value = this.val$initialValue;
        }

        public void onCompleted() {
            this.val$ip.onCompleted();
        }

        public void onError(Throwable th) {
            this.val$ip.onError(th);
        }

        public void onNext(T t) {
            try {
                Object call = OperatorScan.this.accumulator.call(this.value, t);
                this.value = call;
                this.val$ip.onNext(call);
            } catch (Throwable th) {
                Exceptions.throwOrReport(th, this, t);
            }
        }

        public void setProducer(Producer producer) {
            this.val$ip.setProducer(producer);
        }
    }

    static final class InitialProducer<R> implements Producer, Observer<R> {
        final Subscriber<? super R> child;
        volatile boolean done;
        boolean emitting;
        Throwable error;
        boolean missed;
        long missedRequested;
        volatile Producer producer;
        final Queue<Object> queue;
        final AtomicLong requested;

        public InitialProducer(R r, Subscriber<? super R> subscriber) {
            this.child = subscriber;
            Queue spscLinkedQueue = UnsafeAccess.isUnsafeAvailable() ? new SpscLinkedQueue() : new SpscLinkedAtomicQueue();
            this.queue = spscLinkedQueue;
            spscLinkedQueue.offer(NotificationLite.instance().next(r));
            this.requested = new AtomicLong();
        }

        boolean checkTerminated(boolean z, boolean z2, Subscriber<? super R> subscriber) {
            if (subscriber.isUnsubscribed()) {
                return true;
            }
            if (z) {
                Throwable th = this.error;
                if (th != null) {
                    subscriber.onError(th);
                    return true;
                } else if (z2) {
                    subscriber.onCompleted();
                    return true;
                }
            }
            return false;
        }

        void emit() {
            synchronized (this) {
                if (this.emitting) {
                    this.missed = true;
                    return;
                }
                this.emitting = true;
                emitLoop();
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        void emitLoop() {
            /*
            r14 = this;
            r8 = r14.child;
            r9 = r14.queue;
            r10 = rx.internal.operators.NotificationLite.instance();
            r11 = r14.requested;
            r4 = r11.get();
        L_0x000e:
            r0 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1));
            if (r0 != 0) goto L_0x0025;
        L_0x0017:
            r0 = 1;
        L_0x0018:
            r1 = r14.done;
            r2 = r9.isEmpty();
            r1 = r14.checkTerminated(r1, r2, r8);
            if (r1 == 0) goto L_0x0027;
        L_0x0024:
            return;
        L_0x0025:
            r0 = 0;
            goto L_0x0018;
        L_0x0027:
            r2 = 0;
            r12 = r2;
            r2 = r4;
            r4 = r12;
        L_0x002c:
            r6 = 0;
            r1 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
            if (r1 == 0) goto L_0x0043;
        L_0x0032:
            r6 = r14.done;
            r7 = r9.poll();
            if (r7 != 0) goto L_0x005c;
        L_0x003a:
            r1 = 1;
        L_0x003b:
            r6 = r14.checkTerminated(r6, r1, r8);
            if (r6 != 0) goto L_0x0024;
        L_0x0041:
            if (r1 == 0) goto L_0x005e;
        L_0x0043:
            r6 = 0;
            r1 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
            if (r1 == 0) goto L_0x007b;
        L_0x0049:
            if (r0 != 0) goto L_0x007b;
        L_0x004b:
            r0 = r11.addAndGet(r4);
        L_0x004f:
            monitor-enter(r14);
            r2 = r14.missed;	 Catch:{ all -> 0x0059 }
            if (r2 != 0) goto L_0x0075;
        L_0x0054:
            r0 = 0;
            r14.emitting = r0;	 Catch:{ all -> 0x0059 }
            monitor-exit(r14);	 Catch:{ all -> 0x0059 }
            goto L_0x0024;
        L_0x0059:
            r0 = move-exception;
            monitor-exit(r14);	 Catch:{ all -> 0x0059 }
            throw r0;
        L_0x005c:
            r1 = 0;
            goto L_0x003b;
        L_0x005e:
            r1 = r10.getValue(r7);
            r8.onNext(r1);	 Catch:{ Throwable -> 0x0070 }
            r6 = 1;
            r6 = r2 - r6;
            r2 = 1;
            r2 = r4 - r2;
            r4 = r2;
            r2 = r6;
            goto L_0x002c;
        L_0x0070:
            r0 = move-exception;
            rx.exceptions.Exceptions.throwOrReport(r0, r8, r1);
            goto L_0x0024;
        L_0x0075:
            r2 = 0;
            r14.missed = r2;	 Catch:{ all -> 0x0059 }
            monitor-exit(r14);	 Catch:{ all -> 0x0059 }
            r4 = r0;
            goto L_0x000e;
        L_0x007b:
            r0 = r2;
            goto L_0x004f;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorScan.InitialProducer.emitLoop():void");
        }

        public void onCompleted() {
            this.done = true;
            emit();
        }

        public void onError(Throwable th) {
            this.error = th;
            this.done = true;
            emit();
        }

        public void onNext(R r) {
            this.queue.offer(NotificationLite.instance().next(r));
            emit();
        }

        public void request(long j) {
            if (j < 0) {
                throw new IllegalArgumentException("n >= required but it was " + j);
            } else if (j != 0) {
                BackpressureUtils.getAndAddRequest(this.requested, j);
                Producer producer = this.producer;
                if (producer == null) {
                    synchronized (this.requested) {
                        producer = this.producer;
                        if (producer == null) {
                            this.missedRequested = BackpressureUtils.addCap(this.missedRequested, j);
                        }
                    }
                }
                if (producer != null) {
                    producer.request(j);
                }
                emit();
            }
        }

        public void setProducer(Producer producer) {
            if (producer == null) {
                throw new NullPointerException();
            }
            synchronized (this.requested) {
                if (this.producer != null) {
                    throw new IllegalStateException("Can't set more than one Producer!");
                }
                long j = this.missedRequested;
                if (j != Long.MAX_VALUE) {
                    j--;
                }
                this.missedRequested = 0;
                this.producer = producer;
            }
            if (j > 0) {
                producer.request(j);
            }
            emit();
        }
    }

    static {
        NO_INITIAL_VALUE = new Object();
    }

    public OperatorScan(R r, Func2<R, ? super T, R> func2) {
        this(new C13731(r), (Func2) func2);
    }

    public OperatorScan(Func0<R> func0, Func2<R, ? super T, R> func2) {
        this.initialValueFactory = func0;
        this.accumulator = func2;
    }

    public OperatorScan(Func2<R, ? super T, R> func2) {
        this(NO_INITIAL_VALUE, (Func2) func2);
    }

    public Subscriber<? super T> call(Subscriber<? super R> subscriber) {
        Object call = this.initialValueFactory.call();
        if (call == NO_INITIAL_VALUE) {
            return new C13742(subscriber, subscriber);
        }
        Producer initialProducer = new InitialProducer(call, subscriber);
        Object c13753 = new C13753(call, initialProducer);
        subscriber.add(c13753);
        subscriber.setProducer(initialProducer);
        return c13753;
    }
}

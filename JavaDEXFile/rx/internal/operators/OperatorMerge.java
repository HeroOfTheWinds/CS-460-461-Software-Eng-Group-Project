package rx.internal.operators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.CompositeException;
import rx.exceptions.MissingBackpressureException;
import rx.exceptions.OnErrorThrowable;
import rx.internal.util.RxRingBuffer;
import rx.internal.util.ScalarSynchronousObservable;
import rx.internal.util.atomic.SpscAtomicArrayQueue;
import rx.internal.util.atomic.SpscExactAtomicArrayQueue;
import rx.internal.util.atomic.SpscUnboundedAtomicArrayQueue;
import rx.internal.util.unsafe.Pow2;
import rx.internal.util.unsafe.SpscArrayQueue;
import rx.internal.util.unsafe.UnsafeAccess;
import rx.subscriptions.CompositeSubscription;

public final class OperatorMerge<T> implements Operator<T, Observable<? extends T>> {
    final boolean delayErrors;
    final int maxConcurrent;

    private static final class HolderDelayErrors {
        static final OperatorMerge<Object> INSTANCE;

        static {
            INSTANCE = new OperatorMerge(true, Integer.MAX_VALUE);
        }

        private HolderDelayErrors() {
        }
    }

    private static final class HolderNoDelay {
        static final OperatorMerge<Object> INSTANCE;

        static {
            INSTANCE = new OperatorMerge(false, Integer.MAX_VALUE);
        }

        private HolderNoDelay() {
        }
    }

    static final class InnerSubscriber<T> extends Subscriber<T> {
        static final int limit;
        volatile boolean done;
        final long id;
        int outstanding;
        final MergeSubscriber<T> parent;
        volatile RxRingBuffer queue;

        static {
            limit = RxRingBuffer.SIZE / 4;
        }

        public InnerSubscriber(MergeSubscriber<T> mergeSubscriber, long j) {
            this.parent = mergeSubscriber;
            this.id = j;
        }

        public void onCompleted() {
            this.done = true;
            this.parent.emit();
        }

        public void onError(Throwable th) {
            this.done = true;
            this.parent.getOrCreateErrorQueue().offer(th);
            this.parent.emit();
        }

        public void onNext(T t) {
            this.parent.tryEmit(this, t);
        }

        public void onStart() {
            this.outstanding = RxRingBuffer.SIZE;
            request((long) RxRingBuffer.SIZE);
        }

        public void requestMore(long j) {
            int i = this.outstanding - ((int) j);
            if (i > limit) {
                this.outstanding = i;
                return;
            }
            this.outstanding = RxRingBuffer.SIZE;
            i = RxRingBuffer.SIZE - i;
            if (i > 0) {
                request((long) i);
            }
        }
    }

    static final class MergeProducer<T> extends AtomicLong implements Producer {
        private static final long serialVersionUID = -1214379189873595503L;
        final MergeSubscriber<T> subscriber;

        public MergeProducer(MergeSubscriber<T> mergeSubscriber) {
            this.subscriber = mergeSubscriber;
        }

        public long produced(int i) {
            return addAndGet((long) (-i));
        }

        public void request(long j) {
            if (j > 0) {
                if (get() != Long.MAX_VALUE) {
                    BackpressureUtils.getAndAddRequest(this, j);
                    this.subscriber.emit();
                }
            } else if (j < 0) {
                throw new IllegalArgumentException("n >= 0 required");
            }
        }
    }

    static final class MergeSubscriber<T> extends Subscriber<Observable<? extends T>> {
        static final InnerSubscriber<?>[] EMPTY;
        final Subscriber<? super T> child;
        final boolean delayErrors;
        volatile boolean done;
        boolean emitting;
        volatile ConcurrentLinkedQueue<Throwable> errors;
        final Object innerGuard;
        volatile InnerSubscriber<?>[] innerSubscribers;
        long lastId;
        int lastIndex;
        final int maxConcurrent;
        boolean missed;
        final NotificationLite<T> nl;
        MergeProducer<T> producer;
        volatile Queue<Object> queue;
        int scalarEmissionCount;
        final int scalarEmissionLimit;
        volatile CompositeSubscription subscriptions;
        long uniqueId;

        static {
            EMPTY = new InnerSubscriber[0];
        }

        public MergeSubscriber(Subscriber<? super T> subscriber, boolean z, int i) {
            this.child = subscriber;
            this.delayErrors = z;
            this.maxConcurrent = i;
            this.nl = NotificationLite.instance();
            this.innerGuard = new Object();
            this.innerSubscribers = EMPTY;
            if (i == Integer.MAX_VALUE) {
                this.scalarEmissionLimit = Integer.MAX_VALUE;
                request(Long.MAX_VALUE);
                return;
            }
            this.scalarEmissionLimit = Math.max(1, i >> 1);
            request((long) i);
        }

        private void reportError() {
            Collection arrayList = new ArrayList(this.errors);
            if (arrayList.size() == 1) {
                this.child.onError((Throwable) arrayList.get(0));
            } else {
                this.child.onError(new CompositeException(arrayList));
            }
        }

        void addInner(InnerSubscriber<T> innerSubscriber) {
            getOrCreateComposite().add(innerSubscriber);
            synchronized (this.innerGuard) {
                Object obj = this.innerSubscribers;
                int length = obj.length;
                Object obj2 = new InnerSubscriber[(length + 1)];
                System.arraycopy(obj, 0, obj2, 0, length);
                obj2[length] = innerSubscriber;
                this.innerSubscribers = obj2;
            }
        }

        boolean checkTerminate() {
            if (this.child.isUnsubscribed()) {
                return true;
            }
            Queue queue = this.errors;
            if (this.delayErrors || queue == null || queue.isEmpty()) {
                return false;
            }
            try {
                reportError();
                return true;
            } finally {
                unsubscribe();
            }
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
            r25 = this;
            r11 = 0;
            r10 = 0;
            r0 = r25;
            r15 = r0.child;	 Catch:{ all -> 0x0084 }
        L_0x0006:
            r4 = r25.checkTerminate();	 Catch:{ all -> 0x0084 }
            if (r4 == 0) goto L_0x000d;
        L_0x000c:
            return;
        L_0x000d:
            r0 = r25;
            r13 = r0.queue;	 Catch:{ all -> 0x0084 }
            r0 = r25;
            r4 = r0.producer;	 Catch:{ all -> 0x0084 }
            r4 = r4.get();	 Catch:{ all -> 0x0084 }
            r6 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
            if (r6 != 0) goto L_0x0090;
        L_0x0022:
            r6 = 1;
            r14 = r6;
        L_0x0024:
            r7 = 0;
            r6 = 0;
            if (r13 == 0) goto L_0x0055;
        L_0x0028:
            r6 = r7;
        L_0x0029:
            r8 = 0;
            r7 = 0;
            r22 = r7;
            r7 = r8;
            r8 = r4;
            r4 = r22;
        L_0x0031:
            r16 = 0;
            r5 = (r8 > r16 ? 1 : (r8 == r16 ? 0 : -1));
            if (r5 <= 0) goto L_0x0043;
        L_0x0037:
            r4 = r13.poll();	 Catch:{ all -> 0x0084 }
            r5 = r25.checkTerminate();	 Catch:{ all -> 0x0084 }
            if (r5 != 0) goto L_0x000c;
        L_0x0041:
            if (r4 != 0) goto L_0x0093;
        L_0x0043:
            r12 = r4;
            if (r7 <= 0) goto L_0x0226;
        L_0x0046:
            if (r14 == 0) goto L_0x00c7;
        L_0x0048:
            r4 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        L_0x004d:
            r8 = 0;
            r7 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
            if (r7 == 0) goto L_0x0055;
        L_0x0053:
            if (r12 != 0) goto L_0x0029;
        L_0x0055:
            r0 = r25;
            r7 = r0.done;	 Catch:{ all -> 0x0084 }
            r0 = r25;
            r8 = r0.queue;	 Catch:{ all -> 0x0084 }
            r0 = r25;
            r0 = r0.innerSubscribers;	 Catch:{ all -> 0x0084 }
            r16 = r0;
            r0 = r16;
            r0 = r0.length;	 Catch:{ all -> 0x0084 }
            r17 = r0;
            if (r7 == 0) goto L_0x00d6;
        L_0x006a:
            if (r8 == 0) goto L_0x0072;
        L_0x006c:
            r7 = r8.isEmpty();	 Catch:{ all -> 0x0084 }
            if (r7 == 0) goto L_0x00d6;
        L_0x0072:
            if (r17 != 0) goto L_0x00d6;
        L_0x0074:
            r0 = r25;
            r4 = r0.errors;	 Catch:{ all -> 0x0084 }
            if (r4 == 0) goto L_0x0080;
        L_0x007a:
            r4 = r4.isEmpty();	 Catch:{ all -> 0x0084 }
            if (r4 == 0) goto L_0x00d1;
        L_0x0080:
            r15.onCompleted();	 Catch:{ all -> 0x0084 }
            goto L_0x000c;
        L_0x0084:
            r4 = move-exception;
            r5 = r10;
        L_0x0086:
            if (r5 != 0) goto L_0x008f;
        L_0x0088:
            monitor-enter(r25);
            r5 = 0;
            r0 = r25;
            r0.emitting = r5;	 Catch:{ all -> 0x020d }
            monitor-exit(r25);	 Catch:{ all -> 0x020d }
        L_0x008f:
            throw r4;
        L_0x0090:
            r6 = 0;
            r14 = r6;
            goto L_0x0024;
        L_0x0093:
            r0 = r25;
            r5 = r0.nl;	 Catch:{ all -> 0x0084 }
            r5 = r5.getValue(r4);	 Catch:{ all -> 0x0084 }
            r15.onNext(r5);	 Catch:{ Throwable -> 0x00aa }
        L_0x009e:
            r5 = r6 + 1;
            r12 = r7 + 1;
            r6 = 1;
            r6 = r8 - r6;
            r8 = r6;
            r6 = r5;
            r7 = r12;
            goto L_0x0031;
        L_0x00aa:
            r12 = move-exception;
            r0 = r25;
            r5 = r0.delayErrors;	 Catch:{ all -> 0x0084 }
            if (r5 != 0) goto L_0x00bf;
        L_0x00b1:
            rx.exceptions.Exceptions.throwIfFatal(r12);	 Catch:{ all -> 0x0084 }
            r5 = 1;
            r25.unsubscribe();	 Catch:{ all -> 0x00bd }
            r15.onError(r12);	 Catch:{ all -> 0x00bd }
            goto L_0x000c;
        L_0x00bd:
            r4 = move-exception;
            goto L_0x0086;
        L_0x00bf:
            r5 = r25.getOrCreateErrorQueue();	 Catch:{ all -> 0x0084 }
            r5.offer(r12);	 Catch:{ all -> 0x0084 }
            goto L_0x009e;
        L_0x00c7:
            r0 = r25;
            r4 = r0.producer;	 Catch:{ all -> 0x0084 }
            r4 = r4.produced(r7);	 Catch:{ all -> 0x0084 }
            goto L_0x004d;
        L_0x00d1:
            r25.reportError();	 Catch:{ all -> 0x0084 }
            goto L_0x000c;
        L_0x00d6:
            r7 = 0;
            r9 = 0;
            if (r17 <= 0) goto L_0x0229;
        L_0x00da:
            r0 = r25;
            r12 = r0.lastId;	 Catch:{ all -> 0x0084 }
            r0 = r25;
            r7 = r0.lastIndex;	 Catch:{ all -> 0x0084 }
            r0 = r17;
            if (r0 <= r7) goto L_0x00f0;
        L_0x00e6:
            r8 = r16[r7];	 Catch:{ all -> 0x0084 }
            r0 = r8.id;	 Catch:{ all -> 0x0084 }
            r18 = r0;
            r8 = (r18 > r12 ? 1 : (r18 == r12 ? 0 : -1));
            if (r8 == 0) goto L_0x0112;
        L_0x00f0:
            r0 = r17;
            if (r0 > r7) goto L_0x00f5;
        L_0x00f4:
            r7 = 0;
        L_0x00f5:
            r8 = 0;
        L_0x00f6:
            r0 = r17;
            if (r8 >= r0) goto L_0x0106;
        L_0x00fa:
            r18 = r16[r7];	 Catch:{ all -> 0x0084 }
            r0 = r18;
            r0 = r0.id;	 Catch:{ all -> 0x0084 }
            r18 = r0;
            r18 = (r18 > r12 ? 1 : (r18 == r12 ? 0 : -1));
            if (r18 != 0) goto L_0x01b6;
        L_0x0106:
            r0 = r25;
            r0.lastIndex = r7;	 Catch:{ all -> 0x0084 }
            r8 = r16[r7];	 Catch:{ all -> 0x0084 }
            r12 = r8.id;	 Catch:{ all -> 0x0084 }
            r0 = r25;
            r0.lastId = r12;	 Catch:{ all -> 0x0084 }
        L_0x0112:
            r8 = 0;
            r12 = r7;
            r13 = r8;
            r8 = r9;
            r9 = r6;
            r6 = r4;
        L_0x0118:
            r0 = r17;
            if (r13 >= r0) goto L_0x0217;
        L_0x011c:
            r4 = r25.checkTerminate();	 Catch:{ all -> 0x0084 }
            if (r4 != 0) goto L_0x000c;
        L_0x0122:
            r18 = r16[r12];
            r4 = 0;
        L_0x0125:
            r5 = 0;
            r22 = r4;
            r23 = r6;
            r6 = r22;
            r7 = r5;
            r4 = r23;
        L_0x012f:
            r20 = 0;
            r19 = (r4 > r20 ? 1 : (r4 == r20 ? 0 : -1));
            if (r19 <= 0) goto L_0x0143;
        L_0x0135:
            r19 = r25.checkTerminate();	 Catch:{ all -> 0x0084 }
            if (r19 != 0) goto L_0x000c;
        L_0x013b:
            r0 = r18;
            r0 = r0.queue;	 Catch:{ all -> 0x0084 }
            r19 = r0;
            if (r19 != 0) goto L_0x01c1;
        L_0x0143:
            if (r7 <= 0) goto L_0x0159;
        L_0x0145:
            if (r14 != 0) goto L_0x0210;
        L_0x0147:
            r0 = r25;
            r4 = r0.producer;	 Catch:{ all -> 0x0084 }
            r4 = r4.produced(r7);	 Catch:{ all -> 0x0084 }
        L_0x014f:
            r0 = (long) r7;	 Catch:{ all -> 0x0084 }
            r20 = r0;
            r0 = r18;
            r1 = r20;
            r0.requestMore(r1);	 Catch:{ all -> 0x0084 }
        L_0x0159:
            r20 = 0;
            r7 = (r4 > r20 ? 1 : (r4 == r20 ? 0 : -1));
            if (r7 == 0) goto L_0x0161;
        L_0x015f:
            if (r6 != 0) goto L_0x021b;
        L_0x0161:
            r0 = r18;
            r6 = r0.done;	 Catch:{ all -> 0x0084 }
            r0 = r18;
            r7 = r0.queue;	 Catch:{ all -> 0x0084 }
            if (r6 == 0) goto L_0x0222;
        L_0x016b:
            if (r7 == 0) goto L_0x0173;
        L_0x016d:
            r6 = r7.isEmpty();	 Catch:{ all -> 0x0084 }
            if (r6 == 0) goto L_0x0222;
        L_0x0173:
            r0 = r25;
            r1 = r18;
            r0.removeInner(r1);	 Catch:{ all -> 0x0084 }
            r6 = r25.checkTerminate();	 Catch:{ all -> 0x0084 }
            if (r6 != 0) goto L_0x000c;
        L_0x0180:
            r9 = r9 + 1;
            r8 = 1;
            r7 = r8;
            r8 = r9;
        L_0x0185:
            r18 = 0;
            r6 = (r4 > r18 ? 1 : (r4 == r18 ? 0 : -1));
            if (r6 != 0) goto L_0x01f2;
        L_0x018b:
            r4 = r7;
            r5 = r8;
        L_0x018d:
            r0 = r25;
            r0.lastIndex = r12;	 Catch:{ all -> 0x0084 }
            r6 = r16[r12];	 Catch:{ all -> 0x0084 }
            r6 = r6.id;	 Catch:{ all -> 0x0084 }
            r0 = r25;
            r0.lastId = r6;	 Catch:{ all -> 0x0084 }
        L_0x0199:
            if (r5 <= 0) goto L_0x01a1;
        L_0x019b:
            r6 = (long) r5;	 Catch:{ all -> 0x0084 }
            r0 = r25;
            r0.request(r6);	 Catch:{ all -> 0x0084 }
        L_0x01a1:
            if (r4 != 0) goto L_0x0006;
        L_0x01a3:
            monitor-enter(r25);	 Catch:{ all -> 0x0084 }
            r0 = r25;
            r4 = r0.missed;	 Catch:{ all -> 0x020a }
            if (r4 != 0) goto L_0x0202;
        L_0x01aa:
            r5 = 1;
            r4 = 0;
            r0 = r25;
            r0.emitting = r4;	 Catch:{ all -> 0x01b3 }
            monitor-exit(r25);	 Catch:{ all -> 0x01b3 }
            goto L_0x000c;
        L_0x01b3:
            r4 = move-exception;
        L_0x01b4:
            monitor-exit(r25);	 Catch:{ all -> 0x01b3 }
            throw r4;	 Catch:{ all -> 0x00bd }
        L_0x01b6:
            r7 = r7 + 1;
            r0 = r17;
            if (r7 != r0) goto L_0x01bd;
        L_0x01bc:
            r7 = 0;
        L_0x01bd:
            r8 = r8 + 1;
            goto L_0x00f6;
        L_0x01c1:
            r6 = r19.poll();	 Catch:{ all -> 0x0084 }
            if (r6 == 0) goto L_0x0143;
        L_0x01c7:
            r0 = r25;
            r0 = r0.nl;	 Catch:{ all -> 0x0084 }
            r19 = r0;
            r0 = r19;
            r19 = r0.getValue(r6);	 Catch:{ all -> 0x0084 }
            r0 = r19;
            r15.onNext(r0);	 Catch:{ Throwable -> 0x01e0 }
            r20 = 1;
            r4 = r4 - r20;
            r7 = r7 + 1;
            goto L_0x012f;
        L_0x01e0:
            r4 = move-exception;
            r5 = 1;
            rx.exceptions.Exceptions.throwIfFatal(r4);	 Catch:{ all -> 0x00bd }
            r15.onError(r4);	 Catch:{ all -> 0x01ed }
            r25.unsubscribe();	 Catch:{ all -> 0x00bd }
            goto L_0x000c;
        L_0x01ed:
            r4 = move-exception;
            r25.unsubscribe();	 Catch:{ all -> 0x00bd }
            throw r4;	 Catch:{ all -> 0x00bd }
        L_0x01f2:
            r6 = r12 + 1;
            r0 = r17;
            if (r6 != r0) goto L_0x01f9;
        L_0x01f8:
            r6 = 0;
        L_0x01f9:
            r9 = r13 + 1;
            r12 = r6;
            r13 = r9;
            r9 = r8;
            r8 = r7;
            r6 = r4;
            goto L_0x0118;
        L_0x0202:
            r4 = 0;
            r0 = r25;
            r0.missed = r4;	 Catch:{ all -> 0x020a }
            monitor-exit(r25);	 Catch:{ all -> 0x020a }
            goto L_0x0006;
        L_0x020a:
            r4 = move-exception;
            r5 = r11;
            goto L_0x01b4;
        L_0x020d:
            r4 = move-exception;
            monitor-exit(r25);	 Catch:{ all -> 0x020d }
            throw r4;
        L_0x0210:
            r4 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            goto L_0x014f;
        L_0x0217:
            r4 = r8;
            r5 = r9;
            goto L_0x018d;
        L_0x021b:
            r22 = r6;
            r6 = r4;
            r4 = r22;
            goto L_0x0125;
        L_0x0222:
            r7 = r8;
            r8 = r9;
            goto L_0x0185;
        L_0x0226:
            r4 = r8;
            goto L_0x004d;
        L_0x0229:
            r4 = r7;
            r5 = r6;
            goto L_0x0199;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorMerge.MergeSubscriber.emitLoop():void");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected void emitScalar(T r7, long r8) {
            /*
            r6 = this;
            r2 = 1;
            r1 = 0;
            r0 = r6.child;	 Catch:{ Throwable -> 0x002f }
            r0.onNext(r7);	 Catch:{ Throwable -> 0x002f }
        L_0x0007:
            r4 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            r0 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1));
            if (r0 == 0) goto L_0x0016;
        L_0x0010:
            r0 = r6.producer;	 Catch:{ all -> 0x0050 }
            r3 = 1;
            r0.produced(r3);	 Catch:{ all -> 0x0050 }
        L_0x0016:
            r0 = r6.scalarEmissionCount;	 Catch:{ all -> 0x0050 }
            r0 = r0 + 1;
            r3 = r6.scalarEmissionLimit;	 Catch:{ all -> 0x0050 }
            if (r0 != r3) goto L_0x0052;
        L_0x001e:
            r3 = 0;
            r6.scalarEmissionCount = r3;	 Catch:{ all -> 0x0050 }
            r4 = (long) r0;	 Catch:{ all -> 0x0050 }
            r6.requestMore(r4);	 Catch:{ all -> 0x0050 }
        L_0x0025:
            monitor-enter(r6);	 Catch:{ all -> 0x0050 }
            r0 = r6.missed;	 Catch:{ all -> 0x005d }
            if (r0 != 0) goto L_0x0055;
        L_0x002a:
            r0 = 0;
            r6.emitting = r0;	 Catch:{ all -> 0x005d }
            monitor-exit(r6);	 Catch:{ all -> 0x005d }
        L_0x002e:
            return;
        L_0x002f:
            r0 = move-exception;
            r3 = r6.delayErrors;	 Catch:{ all -> 0x0050 }
            if (r3 != 0) goto L_0x0048;
        L_0x0034:
            rx.exceptions.Exceptions.throwIfFatal(r0);	 Catch:{ all -> 0x0050 }
            r6.unsubscribe();	 Catch:{ all -> 0x003e }
            r6.onError(r0);	 Catch:{ all -> 0x003e }
            goto L_0x002e;
        L_0x003e:
            r0 = move-exception;
            r1 = r2;
        L_0x0040:
            if (r1 != 0) goto L_0x0047;
        L_0x0042:
            monitor-enter(r6);
            r1 = 0;
            r6.emitting = r1;	 Catch:{ all -> 0x0060 }
            monitor-exit(r6);	 Catch:{ all -> 0x0060 }
        L_0x0047:
            throw r0;
        L_0x0048:
            r3 = r6.getOrCreateErrorQueue();	 Catch:{ all -> 0x0050 }
            r3.offer(r0);	 Catch:{ all -> 0x0050 }
            goto L_0x0007;
        L_0x0050:
            r0 = move-exception;
            goto L_0x0040;
        L_0x0052:
            r6.scalarEmissionCount = r0;	 Catch:{ all -> 0x0050 }
            goto L_0x0025;
        L_0x0055:
            r0 = 0;
            r6.missed = r0;	 Catch:{ all -> 0x005d }
            monitor-exit(r6);	 Catch:{ all -> 0x005d }
            r6.emitLoop();
            goto L_0x002e;
        L_0x005d:
            r0 = move-exception;
            monitor-exit(r6);	 Catch:{ all -> 0x005d }
            throw r0;	 Catch:{ all -> 0x003e }
        L_0x0060:
            r0 = move-exception;
            monitor-exit(r6);	 Catch:{ all -> 0x0060 }
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorMerge.MergeSubscriber.emitScalar(java.lang.Object, long):void");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected void emitScalar(rx.internal.operators.OperatorMerge.InnerSubscriber<T> r8, T r9, long r10) {
            /*
            r7 = this;
            r2 = 1;
            r1 = 0;
            r0 = r7.child;	 Catch:{ Throwable -> 0x0025 }
            r0.onNext(r9);	 Catch:{ Throwable -> 0x0025 }
        L_0x0007:
            r4 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            r0 = (r10 > r4 ? 1 : (r10 == r4 ? 0 : -1));
            if (r0 == 0) goto L_0x0016;
        L_0x0010:
            r0 = r7.producer;	 Catch:{ all -> 0x0046 }
            r3 = 1;
            r0.produced(r3);	 Catch:{ all -> 0x0046 }
        L_0x0016:
            r4 = 1;
            r8.requestMore(r4);	 Catch:{ all -> 0x0046 }
            monitor-enter(r7);	 Catch:{ all -> 0x0046 }
            r0 = r7.missed;	 Catch:{ all -> 0x0050 }
            if (r0 != 0) goto L_0x0048;
        L_0x0020:
            r0 = 0;
            r7.emitting = r0;	 Catch:{ all -> 0x0050 }
            monitor-exit(r7);	 Catch:{ all -> 0x0050 }
        L_0x0024:
            return;
        L_0x0025:
            r0 = move-exception;
            r3 = r7.delayErrors;	 Catch:{ all -> 0x0046 }
            if (r3 != 0) goto L_0x003e;
        L_0x002a:
            rx.exceptions.Exceptions.throwIfFatal(r0);	 Catch:{ all -> 0x0046 }
            r8.unsubscribe();	 Catch:{ all -> 0x0034 }
            r8.onError(r0);	 Catch:{ all -> 0x0034 }
            goto L_0x0024;
        L_0x0034:
            r0 = move-exception;
            r1 = r2;
        L_0x0036:
            if (r1 != 0) goto L_0x003d;
        L_0x0038:
            monitor-enter(r7);
            r1 = 0;
            r7.emitting = r1;	 Catch:{ all -> 0x0053 }
            monitor-exit(r7);	 Catch:{ all -> 0x0053 }
        L_0x003d:
            throw r0;
        L_0x003e:
            r3 = r7.getOrCreateErrorQueue();	 Catch:{ all -> 0x0046 }
            r3.offer(r0);	 Catch:{ all -> 0x0046 }
            goto L_0x0007;
        L_0x0046:
            r0 = move-exception;
            goto L_0x0036;
        L_0x0048:
            r0 = 0;
            r7.missed = r0;	 Catch:{ all -> 0x0050 }
            monitor-exit(r7);	 Catch:{ all -> 0x0050 }
            r7.emitLoop();
            goto L_0x0024;
        L_0x0050:
            r0 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x0050 }
            throw r0;	 Catch:{ all -> 0x0034 }
        L_0x0053:
            r0 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x0053 }
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorMerge.MergeSubscriber.emitScalar(rx.internal.operators.OperatorMerge$InnerSubscriber, java.lang.Object, long):void");
        }

        CompositeSubscription getOrCreateComposite() {
            Throwable th;
            CompositeSubscription compositeSubscription = this.subscriptions;
            if (compositeSubscription == null) {
                Object obj = null;
                synchronized (this) {
                    compositeSubscription = this.subscriptions;
                    if (compositeSubscription == null) {
                        compositeSubscription = new CompositeSubscription();
                        try {
                            this.subscriptions = compositeSubscription;
                            obj = 1;
                        } catch (Throwable th2) {
                            th = th2;
                            throw th;
                        }
                    }
                    try {
                        if (obj != null) {
                            add(compositeSubscription);
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        throw th;
                    }
                }
            }
            return compositeSubscription;
        }

        Queue<Throwable> getOrCreateErrorQueue() {
            Queue<Throwable> queue = this.errors;
            if (queue == null) {
                synchronized (this) {
                    try {
                        queue = this.errors;
                        if (queue == null) {
                            queue = new ConcurrentLinkedQueue();
                            this.errors = queue;
                        }
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                }
            }
            return queue;
        }

        public void onCompleted() {
            this.done = true;
            emit();
        }

        public void onError(Throwable th) {
            getOrCreateErrorQueue().offer(th);
            this.done = true;
            emit();
        }

        public void onNext(Observable<? extends T> observable) {
            if (observable != null) {
                if (observable instanceof ScalarSynchronousObservable) {
                    tryEmit(((ScalarSynchronousObservable) observable).get());
                    return;
                }
                long j = this.uniqueId;
                this.uniqueId = 1 + j;
                Subscriber innerSubscriber = new InnerSubscriber(this, j);
                addInner(innerSubscriber);
                observable.unsafeSubscribe(innerSubscriber);
                emit();
            }
        }

        protected void queueScalar(T t) {
            Queue queue = this.queue;
            if (queue == null) {
                int i = this.maxConcurrent;
                queue = i == Integer.MAX_VALUE ? new SpscUnboundedAtomicArrayQueue(RxRingBuffer.SIZE) : Pow2.isPowerOfTwo(i) ? UnsafeAccess.isUnsafeAvailable() ? new SpscArrayQueue(i) : new SpscAtomicArrayQueue(i) : new SpscExactAtomicArrayQueue(i);
                this.queue = queue;
            }
            if (queue.offer(this.nl.next(t))) {
                emit();
                return;
            }
            unsubscribe();
            onError(OnErrorThrowable.addValueAsLastCause(new MissingBackpressureException(), t));
        }

        protected void queueScalar(InnerSubscriber<T> innerSubscriber, T t) {
            RxRingBuffer rxRingBuffer = innerSubscriber.queue;
            if (rxRingBuffer == null) {
                rxRingBuffer = RxRingBuffer.getSpscInstance();
                innerSubscriber.add(rxRingBuffer);
                innerSubscriber.queue = rxRingBuffer;
            }
            try {
                rxRingBuffer.onNext(this.nl.next(t));
                emit();
            } catch (Throwable e) {
                innerSubscriber.unsubscribe();
                innerSubscriber.onError(e);
            } catch (Throwable e2) {
                if (!innerSubscriber.isUnsubscribed()) {
                    innerSubscriber.unsubscribe();
                    innerSubscriber.onError(e2);
                }
            }
        }

        void removeInner(InnerSubscriber<T> innerSubscriber) {
            int i = 0;
            RxRingBuffer rxRingBuffer = innerSubscriber.queue;
            if (rxRingBuffer != null) {
                rxRingBuffer.release();
            }
            this.subscriptions.remove(innerSubscriber);
            synchronized (this.innerGuard) {
                Object obj = this.innerSubscribers;
                int length = obj.length;
                while (i < length) {
                    if (innerSubscriber.equals(obj[i])) {
                        break;
                    }
                    i++;
                }
                i = -1;
                if (i < 0) {
                } else if (length == 1) {
                    this.innerSubscribers = EMPTY;
                } else {
                    Object obj2 = new InnerSubscriber[(length - 1)];
                    System.arraycopy(obj, 0, obj2, 0, i);
                    System.arraycopy(obj, i + 1, obj2, i, (length - i) - 1);
                    this.innerSubscribers = obj2;
                }
            }
        }

        public void requestMore(long j) {
            request(j);
        }

        void tryEmit(T t) {
            Object obj = null;
            long j = this.producer.get();
            if (j != 0) {
                synchronized (this) {
                    j = this.producer.get();
                    if (!(this.emitting || j == 0)) {
                        this.emitting = true;
                        obj = 1;
                    }
                }
            }
            if (obj != null) {
                emitScalar(t, j);
            } else {
                queueScalar(t);
            }
        }

        void tryEmit(InnerSubscriber<T> innerSubscriber, T t) {
            Object obj = null;
            long j = this.producer.get();
            if (j != 0) {
                synchronized (this) {
                    j = this.producer.get();
                    if (!(this.emitting || j == 0)) {
                        this.emitting = true;
                        obj = 1;
                    }
                }
            }
            if (obj != null) {
                emitScalar(innerSubscriber, t, j);
            } else {
                queueScalar(innerSubscriber, t);
            }
        }
    }

    OperatorMerge(boolean z, int i) {
        this.delayErrors = z;
        this.maxConcurrent = i;
    }

    public static <T> OperatorMerge<T> instance(boolean z) {
        return z ? HolderDelayErrors.INSTANCE : HolderNoDelay.INSTANCE;
    }

    public static <T> OperatorMerge<T> instance(boolean z, int i) {
        if (i > 0) {
            return i == Integer.MAX_VALUE ? instance(z) : new OperatorMerge(z, i);
        } else {
            throw new IllegalArgumentException("maxConcurrent > 0 required but it was " + i);
        }
    }

    public Subscriber<Observable<? extends T>> call(Subscriber<? super T> subscriber) {
        Object mergeSubscriber = new MergeSubscriber(subscriber, this.delayErrors, this.maxConcurrent);
        Producer mergeProducer = new MergeProducer(mergeSubscriber);
        mergeSubscriber.producer = mergeProducer;
        subscriber.add(mergeSubscriber);
        subscriber.setProducer(mergeProducer);
        return mergeSubscriber;
    }
}

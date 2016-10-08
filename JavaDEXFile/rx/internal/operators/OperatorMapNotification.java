package rx.internal.operators;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func0;
import rx.functions.Func1;

public final class OperatorMapNotification<T, R> implements Operator<R, T> {
    final Func0<? extends R> onCompleted;
    final Func1<? super Throwable, ? extends R> onError;
    final Func1<? super T, ? extends R> onNext;

    /* renamed from: rx.internal.operators.OperatorMapNotification.1 */
    class C13391 implements Producer {
        final /* synthetic */ MapNotificationSubscriber val$parent;

        C13391(MapNotificationSubscriber mapNotificationSubscriber) {
            this.val$parent = mapNotificationSubscriber;
        }

        public void request(long j) {
            this.val$parent.requestInner(j);
        }
    }

    static final class MapNotificationSubscriber<T, R> extends Subscriber<T> {
        static final long COMPLETED_FLAG = Long.MIN_VALUE;
        static final long REQUESTED_MASK = Long.MAX_VALUE;
        final Subscriber<? super R> actual;
        final AtomicLong missedRequested;
        final Func0<? extends R> onCompleted;
        final Func1<? super Throwable, ? extends R> onError;
        final Func1<? super T, ? extends R> onNext;
        long produced;
        final AtomicReference<Producer> producer;
        final AtomicLong requested;
        R value;

        public MapNotificationSubscriber(Subscriber<? super R> subscriber, Func1<? super T, ? extends R> func1, Func1<? super Throwable, ? extends R> func12, Func0<? extends R> func0) {
            this.actual = subscriber;
            this.onNext = func1;
            this.onError = func12;
            this.onCompleted = func0;
            this.requested = new AtomicLong();
            this.missedRequested = new AtomicLong();
            this.producer = new AtomicReference();
        }

        void accountProduced() {
            long j = this.produced;
            if (j != 0 && this.producer.get() != null) {
                BackpressureUtils.produced(this.requested, j);
            }
        }

        public void onCompleted() {
            accountProduced();
            try {
                this.value = this.onCompleted.call();
            } catch (Throwable th) {
                Exceptions.throwOrReport(th, this.actual);
            }
            tryEmit();
        }

        public void onError(Throwable th) {
            accountProduced();
            try {
                this.value = this.onError.call(th);
            } catch (Throwable th2) {
                Exceptions.throwOrReport(th2, this.actual, th);
            }
            tryEmit();
        }

        public void onNext(T t) {
            try {
                this.produced++;
                this.actual.onNext(this.onNext.call(t));
            } catch (Throwable th) {
                Exceptions.throwOrReport(th, this.actual, t);
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        void requestInner(long r14) {
            /*
            r13 = this;
            r10 = -9223372036854775808;
            r8 = 0;
            r0 = (r14 > r8 ? 1 : (r14 == r8 ? 0 : -1));
            if (r0 >= 0) goto L_0x0021;
        L_0x0008:
            r0 = new java.lang.IllegalArgumentException;
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "n >= 0 required but it was ";
            r1 = r1.append(r2);
            r1 = r1.append(r14);
            r1 = r1.toString();
            r0.<init>(r1);
            throw r0;
        L_0x0021:
            r0 = (r14 > r8 ? 1 : (r14 == r8 ? 0 : -1));
            if (r0 != 0) goto L_0x0026;
        L_0x0025:
            return;
        L_0x0026:
            r0 = r13.requested;
            r0 = r0.get();
            r2 = r10 & r0;
            r2 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1));
            if (r2 == 0) goto L_0x0066;
        L_0x0032:
            r2 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            r2 = r2 & r0;
            r4 = rx.internal.operators.BackpressureUtils.addCap(r2, r14);
            r6 = r13.requested;
            r4 = r4 | r10;
            r0 = r6.compareAndSet(r0, r4);
            if (r0 == 0) goto L_0x0026;
        L_0x0045:
            r0 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1));
            if (r0 != 0) goto L_0x0025;
        L_0x0049:
            r0 = r13.actual;
            r0 = r0.isUnsubscribed();
            if (r0 != 0) goto L_0x0058;
        L_0x0051:
            r0 = r13.actual;
            r1 = r13.value;
            r0.onNext(r1);
        L_0x0058:
            r0 = r13.actual;
            r0 = r0.isUnsubscribed();
            if (r0 != 0) goto L_0x0025;
        L_0x0060:
            r0 = r13.actual;
            r0.onCompleted();
            goto L_0x0025;
        L_0x0066:
            r2 = rx.internal.operators.BackpressureUtils.addCap(r0, r14);
            r4 = r13.requested;
            r0 = r4.compareAndSet(r0, r2);
            if (r0 == 0) goto L_0x0026;
        L_0x0072:
            r1 = r13.producer;
            r0 = r1.get();
            r0 = (rx.Producer) r0;
            if (r0 == 0) goto L_0x0080;
        L_0x007c:
            r0.request(r14);
            goto L_0x0025;
        L_0x0080:
            r0 = r13.missedRequested;
            rx.internal.operators.BackpressureUtils.getAndAddRequest(r0, r14);
            r0 = r1.get();
            r0 = (rx.Producer) r0;
            if (r0 == 0) goto L_0x0025;
        L_0x008d:
            r1 = r13.missedRequested;
            r2 = r1.getAndSet(r8);
            r1 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1));
            if (r1 == 0) goto L_0x0025;
        L_0x0097:
            r0.request(r2);
            goto L_0x0025;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorMapNotification.MapNotificationSubscriber.requestInner(long):void");
        }

        public void setProducer(Producer producer) {
            if (this.producer.compareAndSet(null, producer)) {
                long andSet = this.missedRequested.getAndSet(0);
                if (andSet != 0) {
                    producer.request(andSet);
                    return;
                }
                return;
            }
            throw new IllegalStateException("Producer already set!");
        }

        void tryEmit() {
            long j;
            do {
                j = this.requested.get();
                if ((j & COMPLETED_FLAG) != 0) {
                    return;
                }
            } while (!this.requested.compareAndSet(j, j | COMPLETED_FLAG));
            if (j != 0 || this.producer.get() == null) {
                if (!this.actual.isUnsubscribed()) {
                    this.actual.onNext(this.value);
                }
                if (!this.actual.isUnsubscribed()) {
                    this.actual.onCompleted();
                }
            }
        }
    }

    public OperatorMapNotification(Func1<? super T, ? extends R> func1, Func1<? super Throwable, ? extends R> func12, Func0<? extends R> func0) {
        this.onNext = func1;
        this.onError = func12;
        this.onCompleted = func0;
    }

    public Subscriber<? super T> call(Subscriber<? super R> subscriber) {
        Subscriber<? super T> mapNotificationSubscriber = new MapNotificationSubscriber(subscriber, this.onNext, this.onError, this.onCompleted);
        subscriber.add(mapNotificationSubscriber);
        subscriber.setProducer(new C13391(mapNotificationSubscriber));
        return mapNotificationSubscriber;
    }
}

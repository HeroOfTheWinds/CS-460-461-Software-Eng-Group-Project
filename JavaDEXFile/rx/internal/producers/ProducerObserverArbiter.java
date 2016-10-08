package rx.internal.producers;

import java.util.ArrayList;
import java.util.List;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;

public final class ProducerObserverArbiter<T> implements Producer, Observer<T> {
    static final Producer NULL_PRODUCER;
    final Subscriber<? super T> child;
    Producer currentProducer;
    boolean emitting;
    volatile boolean hasError;
    Producer missedProducer;
    long missedRequested;
    Object missedTerminal;
    List<T> queue;
    long requested;

    /* renamed from: rx.internal.producers.ProducerObserverArbiter.1 */
    static final class C14431 implements Producer {
        C14431() {
        }

        public void request(long j) {
        }
    }

    static {
        NULL_PRODUCER = new C14431();
    }

    public ProducerObserverArbiter(Subscriber<? super T> subscriber) {
        this.child = subscriber;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void emitLoop() {
        /*
        r18 = this;
        r0 = r18;
        r12 = r0.child;
        r4 = 0;
        r2 = 0;
        r3 = r2;
        r6 = r4;
    L_0x0009:
        r5 = 0;
        monitor-enter(r18);
        r0 = r18;
        r14 = r0.missedRequested;	 Catch:{ all -> 0x0052 }
        r0 = r18;
        r4 = r0.missedProducer;	 Catch:{ all -> 0x0052 }
        r0 = r18;
        r2 = r0.missedTerminal;	 Catch:{ all -> 0x0052 }
        r0 = r18;
        r10 = r0.queue;	 Catch:{ all -> 0x0052 }
        r8 = 0;
        r8 = (r14 > r8 ? 1 : (r14 == r8 ? 0 : -1));
        if (r8 != 0) goto L_0x003c;
    L_0x0021:
        if (r4 != 0) goto L_0x003c;
    L_0x0023:
        if (r10 != 0) goto L_0x003c;
    L_0x0025:
        if (r2 != 0) goto L_0x003c;
    L_0x0027:
        r5 = 0;
        r0 = r18;
        r0.emitting = r5;	 Catch:{ all -> 0x0052 }
        r5 = 1;
    L_0x002d:
        monitor-exit(r18);	 Catch:{ all -> 0x0052 }
        if (r5 == 0) goto L_0x0055;
    L_0x0030:
        r4 = 0;
        r2 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1));
        if (r2 == 0) goto L_0x003b;
    L_0x0036:
        if (r3 == 0) goto L_0x003b;
    L_0x0038:
        r3.request(r6);
    L_0x003b:
        return;
    L_0x003c:
        r8 = 0;
        r0 = r18;
        r0.missedRequested = r8;	 Catch:{ all -> 0x0052 }
        r8 = 0;
        r0 = r18;
        r0.missedProducer = r8;	 Catch:{ all -> 0x0052 }
        r8 = 0;
        r0 = r18;
        r0.queue = r8;	 Catch:{ all -> 0x0052 }
        r8 = 0;
        r0 = r18;
        r0.missedTerminal = r8;	 Catch:{ all -> 0x0052 }
        goto L_0x002d;
    L_0x0052:
        r2 = move-exception;
        monitor-exit(r18);	 Catch:{ all -> 0x0052 }
        throw r2;
    L_0x0055:
        if (r10 == 0) goto L_0x005d;
    L_0x0057:
        r5 = r10.isEmpty();
        if (r5 == 0) goto L_0x006a;
    L_0x005d:
        r5 = 1;
    L_0x005e:
        if (r2 == 0) goto L_0x0072;
    L_0x0060:
        r8 = java.lang.Boolean.TRUE;
        if (r2 == r8) goto L_0x006c;
    L_0x0064:
        r2 = (java.lang.Throwable) r2;
        r12.onError(r2);
        goto L_0x003b;
    L_0x006a:
        r5 = 0;
        goto L_0x005e;
    L_0x006c:
        if (r5 == 0) goto L_0x0072;
    L_0x006e:
        r12.onCompleted();
        goto L_0x003b;
    L_0x0072:
        r8 = 0;
        if (r10 == 0) goto L_0x00a1;
    L_0x0076:
        r2 = r10.iterator();
    L_0x007a:
        r5 = r2.hasNext();
        if (r5 == 0) goto L_0x0099;
    L_0x0080:
        r5 = r2.next();
        r8 = r12.isUnsubscribed();
        if (r8 != 0) goto L_0x003b;
    L_0x008a:
        r0 = r18;
        r8 = r0.hasError;
        if (r8 != 0) goto L_0x0009;
    L_0x0090:
        r12.onNext(r5);	 Catch:{ Throwable -> 0x0094 }
        goto L_0x007a;
    L_0x0094:
        r2 = move-exception;
        rx.exceptions.Exceptions.throwOrReport(r2, r12, r5);
        goto L_0x003b;
    L_0x0099:
        r8 = 0;
        r2 = r10.size();
        r10 = (long) r2;
        r8 = r8 + r10;
    L_0x00a1:
        r0 = r18;
        r10 = r0.requested;
        r16 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r2 = (r10 > r16 ? 1 : (r10 == r16 ? 0 : -1));
        if (r2 == 0) goto L_0x0117;
    L_0x00ae:
        r16 = 0;
        r2 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1));
        if (r2 == 0) goto L_0x00c0;
    L_0x00b4:
        r10 = r10 + r14;
        r16 = 0;
        r2 = (r10 > r16 ? 1 : (r10 == r16 ? 0 : -1));
        if (r2 >= 0) goto L_0x00c0;
    L_0x00bb:
        r10 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
    L_0x00c0:
        r16 = 0;
        r2 = (r8 > r16 ? 1 : (r8 == r16 ? 0 : -1));
        if (r2 == 0) goto L_0x00df;
    L_0x00c6:
        r16 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r2 = (r10 > r16 ? 1 : (r10 == r16 ? 0 : -1));
        if (r2 == 0) goto L_0x00df;
    L_0x00cf:
        r8 = r10 - r8;
        r10 = 0;
        r2 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r2 >= 0) goto L_0x00e0;
    L_0x00d7:
        r2 = new java.lang.IllegalStateException;
        r3 = "More produced than requested";
        r2.<init>(r3);
        throw r2;
    L_0x00df:
        r8 = r10;
    L_0x00e0:
        r0 = r18;
        r0.requested = r8;
    L_0x00e4:
        if (r4 == 0) goto L_0x0103;
    L_0x00e6:
        r2 = NULL_PRODUCER;
        if (r4 != r2) goto L_0x00f1;
    L_0x00ea:
        r2 = 0;
        r0 = r18;
        r0.currentProducer = r2;
        goto L_0x0009;
    L_0x00f1:
        r0 = r18;
        r0.currentProducer = r4;
        r10 = 0;
        r2 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r2 == 0) goto L_0x0009;
    L_0x00fb:
        r2 = rx.internal.operators.BackpressureUtils.addCap(r6, r8);
        r6 = r2;
        r3 = r4;
        goto L_0x0009;
    L_0x0103:
        r0 = r18;
        r2 = r0.currentProducer;
        if (r2 == 0) goto L_0x0009;
    L_0x0109:
        r4 = 0;
        r4 = (r14 > r4 ? 1 : (r14 == r4 ? 0 : -1));
        if (r4 == 0) goto L_0x0009;
    L_0x010f:
        r4 = rx.internal.operators.BackpressureUtils.addCap(r6, r14);
        r3 = r2;
        r6 = r4;
        goto L_0x0009;
    L_0x0117:
        r8 = r10;
        goto L_0x00e4;
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.producers.ProducerObserverArbiter.emitLoop():void");
    }

    public void onCompleted() {
        synchronized (this) {
            if (this.emitting) {
                this.missedTerminal = Boolean.valueOf(true);
                return;
            }
            this.emitting = true;
            this.child.onCompleted();
        }
    }

    public void onError(Throwable th) {
        boolean z;
        synchronized (this) {
            if (this.emitting) {
                this.missedTerminal = th;
                z = false;
            } else {
                this.emitting = true;
                z = true;
            }
        }
        if (z) {
            this.child.onError(th);
        } else {
            this.hasError = true;
        }
    }

    public void onNext(T t) {
        synchronized (this) {
            if (this.emitting) {
                List list = this.queue;
                if (list == null) {
                    list = new ArrayList(4);
                    this.queue = list;
                }
                list.add(t);
                return;
            }
            try {
                this.child.onNext(t);
                long j = this.requested;
                if (j != Long.MAX_VALUE) {
                    this.requested = j - 1;
                }
                emitLoop();
            } catch (Throwable th) {
                synchronized (this) {
                }
                this.emitting = false;
            }
        }
    }

    public void request(long j) {
        if (j < 0) {
            throw new IllegalArgumentException("n >= 0 required");
        } else if (j != 0) {
            synchronized (this) {
                if (this.emitting) {
                    this.missedRequested += j;
                    return;
                }
                this.emitting = true;
                Producer producer = this.currentProducer;
                try {
                    long j2 = this.requested + j;
                    if (j2 < 0) {
                        j2 = Long.MAX_VALUE;
                    }
                    this.requested = j2;
                    emitLoop();
                    if (producer != null) {
                        producer.request(j);
                    }
                } catch (Throwable th) {
                    synchronized (this) {
                    }
                    this.emitting = false;
                }
            }
        }
    }

    public void setProducer(Producer producer) {
        synchronized (this) {
            if (this.emitting) {
                if (producer == null) {
                    producer = NULL_PRODUCER;
                }
                this.missedProducer = producer;
                return;
            }
            this.emitting = true;
            this.currentProducer = producer;
            long j = this.requested;
            try {
                emitLoop();
                if (producer != null && j != 0) {
                    producer.request(j);
                }
            } catch (Throwable th) {
                synchronized (this) {
                }
                this.emitting = false;
            }
        }
    }
}

package rx.internal.operators;

import java.util.concurrent.atomic.AtomicLong;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;

public final class OperatorTake<T> implements Operator<T, T> {
    final int limit;

    /* renamed from: rx.internal.operators.OperatorTake.1 */
    class C13961 extends Subscriber<T> {
        boolean completed;
        int count;
        final /* synthetic */ Subscriber val$child;

        /* renamed from: rx.internal.operators.OperatorTake.1.1 */
        class C13951 implements Producer {
            final AtomicLong requested;
            final /* synthetic */ Producer val$producer;

            C13951(Producer producer) {
                this.val$producer = producer;
                this.requested = new AtomicLong(0);
            }

            public void request(long j) {
                if (j > 0 && !C13961.this.completed) {
                    long min;
                    long j2;
                    do {
                        j2 = this.requested.get();
                        min = Math.min(j, ((long) OperatorTake.this.limit) - j2);
                        if (min == 0) {
                            return;
                        }
                    } while (!this.requested.compareAndSet(j2, j2 + min));
                    this.val$producer.request(min);
                }
            }
        }

        C13961(Subscriber subscriber) {
            this.val$child = subscriber;
        }

        public void onCompleted() {
            if (!this.completed) {
                this.completed = true;
                this.val$child.onCompleted();
            }
        }

        public void onError(Throwable th) {
            if (!this.completed) {
                this.completed = true;
                try {
                    this.val$child.onError(th);
                } finally {
                    unsubscribe();
                }
            }
        }

        public void onNext(T t) {
            if (!isUnsubscribed()) {
                int i = this.count;
                this.count = i + 1;
                if (i < OperatorTake.this.limit) {
                    boolean z = this.count == OperatorTake.this.limit;
                    this.val$child.onNext(t);
                    if (z && !this.completed) {
                        this.completed = true;
                        try {
                            this.val$child.onCompleted();
                        } finally {
                            unsubscribe();
                        }
                    }
                }
            }
        }

        public void setProducer(Producer producer) {
            this.val$child.setProducer(new C13951(producer));
        }
    }

    public OperatorTake(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("limit >= 0 required but it was " + i);
        }
        this.limit = i;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        Object c13961 = new C13961(subscriber);
        if (this.limit == 0) {
            subscriber.onCompleted();
            c13961.unsubscribe();
        }
        subscriber.add(c13961);
        return c13961;
    }
}

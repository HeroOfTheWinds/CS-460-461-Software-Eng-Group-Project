package rx.internal.operators;

import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.internal.producers.SingleDelayedProducer;

public final class OperatorAll<T> implements Operator<Boolean, T> {
    final Func1<? super T, Boolean> predicate;

    /* renamed from: rx.internal.operators.OperatorAll.1 */
    class C13051 extends Subscriber<T> {
        boolean done;
        final /* synthetic */ Subscriber val$child;
        final /* synthetic */ SingleDelayedProducer val$producer;

        C13051(SingleDelayedProducer singleDelayedProducer, Subscriber subscriber) {
            this.val$producer = singleDelayedProducer;
            this.val$child = subscriber;
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                this.val$producer.setValue(Boolean.valueOf(true));
            }
        }

        public void onError(Throwable th) {
            this.val$child.onError(th);
        }

        public void onNext(T t) {
            try {
                if (!((Boolean) OperatorAll.this.predicate.call(t)).booleanValue() && !this.done) {
                    this.done = true;
                    this.val$producer.setValue(Boolean.valueOf(false));
                    unsubscribe();
                }
            } catch (Throwable th) {
                Exceptions.throwOrReport(th, this, t);
            }
        }
    }

    public OperatorAll(Func1<? super T, Boolean> func1) {
        this.predicate = func1;
    }

    public Subscriber<? super T> call(Subscriber<? super Boolean> subscriber) {
        Producer singleDelayedProducer = new SingleDelayedProducer(subscriber);
        Object c13051 = new C13051(singleDelayedProducer, subscriber);
        subscriber.add(c13051);
        subscriber.setProducer(singleDelayedProducer);
        return c13051;
    }
}

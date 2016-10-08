package rx.internal.operators;

import rx.Observable.Operator;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

public final class OperatorMap<T, R> implements Operator<R, T> {
    final Func1<? super T, ? extends R> transformer;

    /* renamed from: rx.internal.operators.OperatorMap.1 */
    class C13381 extends Subscriber<T> {
        final /* synthetic */ Subscriber val$o;

        C13381(Subscriber subscriber, Subscriber subscriber2) {
            this.val$o = subscriber2;
            super(subscriber);
        }

        public void onCompleted() {
            this.val$o.onCompleted();
        }

        public void onError(Throwable th) {
            this.val$o.onError(th);
        }

        public void onNext(T t) {
            try {
                this.val$o.onNext(OperatorMap.this.transformer.call(t));
            } catch (Throwable th) {
                Exceptions.throwOrReport(th, this, t);
            }
        }
    }

    public OperatorMap(Func1<? super T, ? extends R> func1) {
        this.transformer = func1;
    }

    public Subscriber<? super T> call(Subscriber<? super R> subscriber) {
        return new C13381(subscriber, subscriber);
    }
}

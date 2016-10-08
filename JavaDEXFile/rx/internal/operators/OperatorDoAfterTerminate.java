package rx.internal.operators;

import rx.Observable.Operator;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.plugins.RxJavaPlugins;

public final class OperatorDoAfterTerminate<T> implements Operator<T, T> {
    final Action0 action;

    /* renamed from: rx.internal.operators.OperatorDoAfterTerminate.1 */
    class C13301 extends Subscriber<T> {
        final /* synthetic */ Subscriber val$child;

        C13301(Subscriber subscriber, Subscriber subscriber2) {
            this.val$child = subscriber2;
            super(subscriber);
        }

        void callAction() {
            try {
                OperatorDoAfterTerminate.this.action.call();
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
            }
        }

        public void onCompleted() {
            try {
                this.val$child.onCompleted();
            } finally {
                callAction();
            }
        }

        public void onError(Throwable th) {
            try {
                this.val$child.onError(th);
            } finally {
                callAction();
            }
        }

        public void onNext(T t) {
            this.val$child.onNext(t);
        }
    }

    public OperatorDoAfterTerminate(Action0 action0) {
        if (action0 == null) {
            throw new NullPointerException("Action can not be null");
        }
        this.action = action0;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        return new C13301(subscriber, subscriber);
    }
}

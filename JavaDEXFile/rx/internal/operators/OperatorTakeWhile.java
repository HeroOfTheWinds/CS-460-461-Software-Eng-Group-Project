package rx.internal.operators;

import rx.Observable.Operator;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.functions.Func2;

public final class OperatorTakeWhile<T> implements Operator<T, T> {
    final Func2<? super T, ? super Integer, Boolean> predicate;

    /* renamed from: rx.internal.operators.OperatorTakeWhile.1 */
    class C14031 implements Func2<T, Integer, Boolean> {
        final /* synthetic */ Func1 val$underlying;

        C14031(Func1 func1) {
            this.val$underlying = func1;
        }

        public Boolean call(T t, Integer num) {
            return (Boolean) this.val$underlying.call(t);
        }
    }

    /* renamed from: rx.internal.operators.OperatorTakeWhile.2 */
    class C14042 extends Subscriber<T> {
        private int counter;
        private boolean done;
        final /* synthetic */ Subscriber val$subscriber;

        C14042(Subscriber subscriber, boolean z, Subscriber subscriber2) {
            this.val$subscriber = subscriber2;
            super(subscriber, z);
            this.counter = 0;
            this.done = false;
        }

        public void onCompleted() {
            if (!this.done) {
                this.val$subscriber.onCompleted();
            }
        }

        public void onError(Throwable th) {
            if (!this.done) {
                this.val$subscriber.onError(th);
            }
        }

        public void onNext(T t) {
            try {
                Func2 func2 = OperatorTakeWhile.this.predicate;
                int i = this.counter;
                this.counter = i + 1;
                if (((Boolean) func2.call(t, Integer.valueOf(i))).booleanValue()) {
                    this.val$subscriber.onNext(t);
                    return;
                }
                this.done = true;
                this.val$subscriber.onCompleted();
                unsubscribe();
            } catch (Throwable th) {
                this.done = true;
                Exceptions.throwOrReport(th, this.val$subscriber, t);
                unsubscribe();
            }
        }
    }

    public OperatorTakeWhile(Func1<? super T, Boolean> func1) {
        this(new C14031(func1));
    }

    public OperatorTakeWhile(Func2<? super T, ? super Integer, Boolean> func2) {
        this.predicate = func2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        Object c14042 = new C14042(subscriber, false, subscriber);
        subscriber.add(c14042);
        return c14042;
    }
}

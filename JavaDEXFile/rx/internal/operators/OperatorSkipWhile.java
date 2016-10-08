package rx.internal.operators;

import rx.Observable.Operator;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.functions.Func2;

public final class OperatorSkipWhile<T> implements Operator<T, T> {
    final Func2<? super T, Integer, Boolean> predicate;

    /* renamed from: rx.internal.operators.OperatorSkipWhile.1 */
    class C13871 extends Subscriber<T> {
        int index;
        boolean skipping;
        final /* synthetic */ Subscriber val$child;

        C13871(Subscriber subscriber, Subscriber subscriber2) {
            this.val$child = subscriber2;
            super(subscriber);
            this.skipping = true;
        }

        public void onCompleted() {
            this.val$child.onCompleted();
        }

        public void onError(Throwable th) {
            this.val$child.onError(th);
        }

        public void onNext(T t) {
            if (this.skipping) {
                try {
                    Func2 func2 = OperatorSkipWhile.this.predicate;
                    int i = this.index;
                    this.index = i + 1;
                    if (((Boolean) func2.call(t, Integer.valueOf(i))).booleanValue()) {
                        request(1);
                        return;
                    }
                    this.skipping = false;
                    this.val$child.onNext(t);
                    return;
                } catch (Throwable th) {
                    Exceptions.throwOrReport(th, this.val$child, t);
                    return;
                }
            }
            this.val$child.onNext(t);
        }
    }

    /* renamed from: rx.internal.operators.OperatorSkipWhile.2 */
    static final class C13882 implements Func2<T, Integer, Boolean> {
        final /* synthetic */ Func1 val$predicate;

        C13882(Func1 func1) {
            this.val$predicate = func1;
        }

        public Boolean call(T t, Integer num) {
            return (Boolean) this.val$predicate.call(t);
        }
    }

    public OperatorSkipWhile(Func2<? super T, Integer, Boolean> func2) {
        this.predicate = func2;
    }

    public static <T> Func2<T, Integer, Boolean> toPredicate2(Func1<? super T, Boolean> func1) {
        return new C13882(func1);
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        return new C13871(subscriber, subscriber);
    }
}

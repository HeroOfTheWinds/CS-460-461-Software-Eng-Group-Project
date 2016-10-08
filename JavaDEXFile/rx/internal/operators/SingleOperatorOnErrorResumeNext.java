package rx.internal.operators;

import rx.Single;
import rx.Single.OnSubscribe;
import rx.SingleSubscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

public class SingleOperatorOnErrorResumeNext<T> implements OnSubscribe<T> {
    private final Single<? extends T> originalSingle;
    private final Func1<Throwable, ? extends Single<? extends T>> resumeFunctionInCaseOfError;

    /* renamed from: rx.internal.operators.SingleOperatorOnErrorResumeNext.1 */
    static final class C14381 implements Func1<Throwable, Single<? extends T>> {
        final /* synthetic */ Single val$resumeSingleInCaseOfError;

        C14381(Single single) {
            this.val$resumeSingleInCaseOfError = single;
        }

        public Single<? extends T> call(Throwable th) {
            return this.val$resumeSingleInCaseOfError;
        }
    }

    /* renamed from: rx.internal.operators.SingleOperatorOnErrorResumeNext.2 */
    class C14392 extends SingleSubscriber<T> {
        final /* synthetic */ SingleSubscriber val$child;

        C14392(SingleSubscriber singleSubscriber) {
            this.val$child = singleSubscriber;
        }

        public void onError(Throwable th) {
            try {
                ((Single) SingleOperatorOnErrorResumeNext.this.resumeFunctionInCaseOfError.call(th)).subscribe(this.val$child);
            } catch (Throwable th2) {
                Exceptions.throwOrReport(th2, this.val$child);
            }
        }

        public void onSuccess(T t) {
            this.val$child.onSuccess(t);
        }
    }

    private SingleOperatorOnErrorResumeNext(Single<? extends T> single, Func1<Throwable, ? extends Single<? extends T>> func1) {
        if (single == null) {
            throw new NullPointerException("originalSingle must not be null");
        } else if (func1 == null) {
            throw new NullPointerException("resumeFunctionInCaseOfError must not be null");
        } else {
            this.originalSingle = single;
            this.resumeFunctionInCaseOfError = func1;
        }
    }

    public static <T> SingleOperatorOnErrorResumeNext<T> withFunction(Single<? extends T> single, Func1<Throwable, ? extends Single<? extends T>> func1) {
        return new SingleOperatorOnErrorResumeNext(single, func1);
    }

    public static <T> SingleOperatorOnErrorResumeNext<T> withOther(Single<? extends T> single, Single<? extends T> single2) {
        if (single2 != null) {
            return new SingleOperatorOnErrorResumeNext(single, new C14381(single2));
        }
        throw new NullPointerException("resumeSingleInCaseOfError must not be null");
    }

    public void call(SingleSubscriber<? super T> singleSubscriber) {
        SingleSubscriber c14392 = new C14392(singleSubscriber);
        singleSubscriber.add(c14392);
        this.originalSingle.subscribe(c14392);
    }
}

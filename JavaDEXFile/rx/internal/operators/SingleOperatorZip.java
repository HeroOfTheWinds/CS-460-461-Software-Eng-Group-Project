package rx.internal.operators;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import rx.Single;
import rx.Single.OnSubscribe;
import rx.SingleSubscriber;
import rx.exceptions.Exceptions;
import rx.functions.FuncN;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.CompositeSubscription;

public class SingleOperatorZip {

    /* renamed from: rx.internal.operators.SingleOperatorZip.1 */
    static final class C14411 implements OnSubscribe<R> {
        final /* synthetic */ Single[] val$singles;
        final /* synthetic */ FuncN val$zipper;

        /* renamed from: rx.internal.operators.SingleOperatorZip.1.1 */
        class C14401 extends SingleSubscriber<T> {
            final /* synthetic */ int val$j;
            final /* synthetic */ AtomicBoolean val$once;
            final /* synthetic */ SingleSubscriber val$subscriber;
            final /* synthetic */ Object[] val$values;
            final /* synthetic */ AtomicInteger val$wip;

            C14401(Object[] objArr, int i, AtomicInteger atomicInteger, SingleSubscriber singleSubscriber, AtomicBoolean atomicBoolean) {
                this.val$values = objArr;
                this.val$j = i;
                this.val$wip = atomicInteger;
                this.val$subscriber = singleSubscriber;
                this.val$once = atomicBoolean;
            }

            public void onError(Throwable th) {
                if (this.val$once.compareAndSet(false, true)) {
                    this.val$subscriber.onError(th);
                } else {
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
                }
            }

            public void onSuccess(T t) {
                this.val$values[this.val$j] = t;
                if (this.val$wip.decrementAndGet() == 0) {
                    try {
                        this.val$subscriber.onSuccess(C14411.this.val$zipper.call(this.val$values));
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        onError(th);
                    }
                }
            }
        }

        C14411(Single[] singleArr, FuncN funcN) {
            this.val$singles = singleArr;
            this.val$zipper = funcN;
        }

        public void call(SingleSubscriber<? super R> singleSubscriber) {
            if (this.val$singles.length == 0) {
                singleSubscriber.onError(new NoSuchElementException("Can't zip 0 Singles."));
                return;
            }
            AtomicInteger atomicInteger = new AtomicInteger(this.val$singles.length);
            AtomicBoolean atomicBoolean = new AtomicBoolean();
            Object[] objArr = new Object[this.val$singles.length];
            CompositeSubscription compositeSubscription = new CompositeSubscription();
            singleSubscriber.add(compositeSubscription);
            int i = 0;
            while (i < this.val$singles.length && !compositeSubscription.isUnsubscribed() && !atomicBoolean.get()) {
                SingleSubscriber c14401 = new C14401(objArr, i, atomicInteger, singleSubscriber, atomicBoolean);
                compositeSubscription.add(c14401);
                if (!compositeSubscription.isUnsubscribed() && !atomicBoolean.get()) {
                    this.val$singles[i].subscribe(c14401);
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public static <T, R> Single<R> zip(Single<? extends T>[] singleArr, FuncN<? extends R> funcN) {
        return Single.create(new C14411(singleArr, funcN));
    }
}

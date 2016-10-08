package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import rx.Completable;
import rx.Completable.CompletableOnSubscribe;
import rx.Completable.CompletableSubscriber;
import rx.Subscription;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.CompositeSubscription;

public final class CompletableOnSubscribeMergeArray implements CompletableOnSubscribe {
    final Completable[] sources;

    /* renamed from: rx.internal.operators.CompletableOnSubscribeMergeArray.1 */
    class C12721 implements CompletableSubscriber {
        final /* synthetic */ AtomicBoolean val$once;
        final /* synthetic */ CompletableSubscriber val$s;
        final /* synthetic */ CompositeSubscription val$set;
        final /* synthetic */ AtomicInteger val$wip;

        C12721(CompositeSubscription compositeSubscription, AtomicBoolean atomicBoolean, CompletableSubscriber completableSubscriber, AtomicInteger atomicInteger) {
            this.val$set = compositeSubscription;
            this.val$once = atomicBoolean;
            this.val$s = completableSubscriber;
            this.val$wip = atomicInteger;
        }

        public void onCompleted() {
            if (this.val$wip.decrementAndGet() == 0 && this.val$once.compareAndSet(false, true)) {
                this.val$s.onCompleted();
            }
        }

        public void onError(Throwable th) {
            this.val$set.unsubscribe();
            if (this.val$once.compareAndSet(false, true)) {
                this.val$s.onError(th);
            } else {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
            }
        }

        public void onSubscribe(Subscription subscription) {
            this.val$set.add(subscription);
        }
    }

    public CompletableOnSubscribeMergeArray(Completable[] completableArr) {
        this.sources = completableArr;
    }

    public void call(CompletableSubscriber completableSubscriber) {
        CompositeSubscription compositeSubscription = new CompositeSubscription();
        AtomicInteger atomicInteger = new AtomicInteger(this.sources.length + 1);
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        completableSubscriber.onSubscribe(compositeSubscription);
        Completable[] completableArr = this.sources;
        int length = completableArr.length;
        int i = 0;
        while (i < length) {
            Completable completable = completableArr[i];
            if (!compositeSubscription.isUnsubscribed()) {
                if (completable == null) {
                    compositeSubscription.unsubscribe();
                    Throwable nullPointerException = new NullPointerException("A completable source is null");
                    if (atomicBoolean.compareAndSet(false, true)) {
                        completableSubscriber.onError(nullPointerException);
                        return;
                    }
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(nullPointerException);
                }
                completable.subscribe(new C12721(compositeSubscription, atomicBoolean, completableSubscriber, atomicInteger));
                i++;
            } else {
                return;
            }
        }
        if (atomicInteger.decrementAndGet() == 0 && atomicBoolean.compareAndSet(false, true)) {
            completableSubscriber.onCompleted();
        }
    }
}

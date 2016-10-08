package rx.internal.operators;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import rx.Completable;
import rx.Completable.CompletableOnSubscribe;
import rx.Completable.CompletableSubscriber;
import rx.Subscription;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.CompositeSubscription;

public final class CompletableOnSubscribeMergeIterable implements CompletableOnSubscribe {
    final Iterable<? extends Completable> sources;

    /* renamed from: rx.internal.operators.CompletableOnSubscribeMergeIterable.1 */
    class C12751 implements CompletableSubscriber {
        final /* synthetic */ AtomicBoolean val$once;
        final /* synthetic */ CompletableSubscriber val$s;
        final /* synthetic */ CompositeSubscription val$set;
        final /* synthetic */ AtomicInteger val$wip;

        C12751(CompositeSubscription compositeSubscription, AtomicBoolean atomicBoolean, CompletableSubscriber completableSubscriber, AtomicInteger atomicInteger) {
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

    public CompletableOnSubscribeMergeIterable(Iterable<? extends Completable> iterable) {
        this.sources = iterable;
    }

    public void call(CompletableSubscriber completableSubscriber) {
        CompositeSubscription compositeSubscription = new CompositeSubscription();
        AtomicInteger atomicInteger = new AtomicInteger(1);
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        completableSubscriber.onSubscribe(compositeSubscription);
        Throwable nullPointerException;
        try {
            Iterator it = this.sources.iterator();
            if (it == null) {
                completableSubscriber.onError(new NullPointerException("The source iterator returned is null"));
                return;
            }
            while (!compositeSubscription.isUnsubscribed()) {
                try {
                    if (it.hasNext()) {
                        if (!compositeSubscription.isUnsubscribed()) {
                            try {
                                Completable completable = (Completable) it.next();
                                if (!compositeSubscription.isUnsubscribed()) {
                                    if (completable == null) {
                                        compositeSubscription.unsubscribe();
                                        nullPointerException = new NullPointerException("A completable source is null");
                                        if (atomicBoolean.compareAndSet(false, true)) {
                                            completableSubscriber.onError(nullPointerException);
                                            return;
                                        } else {
                                            RxJavaPlugins.getInstance().getErrorHandler().handleError(nullPointerException);
                                            return;
                                        }
                                    }
                                    atomicInteger.getAndIncrement();
                                    completable.subscribe(new C12751(compositeSubscription, atomicBoolean, completableSubscriber, atomicInteger));
                                } else {
                                    return;
                                }
                            } catch (Throwable nullPointerException2) {
                                compositeSubscription.unsubscribe();
                                if (atomicBoolean.compareAndSet(false, true)) {
                                    completableSubscriber.onError(nullPointerException2);
                                    return;
                                } else {
                                    RxJavaPlugins.getInstance().getErrorHandler().handleError(nullPointerException2);
                                    return;
                                }
                            }
                        }
                        return;
                    } else if (atomicInteger.decrementAndGet() == 0 && atomicBoolean.compareAndSet(false, true)) {
                        completableSubscriber.onCompleted();
                        return;
                    } else {
                        return;
                    }
                } catch (Throwable nullPointerException22) {
                    compositeSubscription.unsubscribe();
                    if (atomicBoolean.compareAndSet(false, true)) {
                        completableSubscriber.onError(nullPointerException22);
                        return;
                    } else {
                        RxJavaPlugins.getInstance().getErrorHandler().handleError(nullPointerException22);
                        return;
                    }
                }
            }
        } catch (Throwable nullPointerException222) {
            completableSubscriber.onError(nullPointerException222);
        }
    }
}

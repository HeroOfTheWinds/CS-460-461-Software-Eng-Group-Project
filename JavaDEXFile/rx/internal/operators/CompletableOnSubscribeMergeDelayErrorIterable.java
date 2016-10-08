package rx.internal.operators;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import rx.Completable;
import rx.Completable.CompletableOnSubscribe;
import rx.Completable.CompletableSubscriber;
import rx.Subscription;
import rx.internal.util.unsafe.MpscLinkedQueue;
import rx.subscriptions.CompositeSubscription;

public final class CompletableOnSubscribeMergeDelayErrorIterable implements CompletableOnSubscribe {
    final Iterable<? extends Completable> sources;

    /* renamed from: rx.internal.operators.CompletableOnSubscribeMergeDelayErrorIterable.1 */
    class C12741 implements CompletableSubscriber {
        final /* synthetic */ Queue val$queue;
        final /* synthetic */ CompletableSubscriber val$s;
        final /* synthetic */ CompositeSubscription val$set;
        final /* synthetic */ AtomicInteger val$wip;

        C12741(CompositeSubscription compositeSubscription, Queue queue, AtomicInteger atomicInteger, CompletableSubscriber completableSubscriber) {
            this.val$set = compositeSubscription;
            this.val$queue = queue;
            this.val$wip = atomicInteger;
            this.val$s = completableSubscriber;
        }

        public void onCompleted() {
            tryTerminate();
        }

        public void onError(Throwable th) {
            this.val$queue.offer(th);
            tryTerminate();
        }

        public void onSubscribe(Subscription subscription) {
            this.val$set.add(subscription);
        }

        void tryTerminate() {
            if (this.val$wip.decrementAndGet() != 0) {
                return;
            }
            if (this.val$queue.isEmpty()) {
                this.val$s.onCompleted();
            } else {
                this.val$s.onError(CompletableOnSubscribeMerge.collectErrors(this.val$queue));
            }
        }
    }

    public CompletableOnSubscribeMergeDelayErrorIterable(Iterable<? extends Completable> iterable) {
        this.sources = iterable;
    }

    public void call(CompletableSubscriber completableSubscriber) {
        CompositeSubscription compositeSubscription = new CompositeSubscription();
        AtomicInteger atomicInteger = new AtomicInteger(1);
        Queue mpscLinkedQueue = new MpscLinkedQueue();
        completableSubscriber.onSubscribe(compositeSubscription);
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
                                        mpscLinkedQueue.offer(new NullPointerException("A completable source is null"));
                                        if (atomicInteger.decrementAndGet() != 0) {
                                            return;
                                        }
                                        if (mpscLinkedQueue.isEmpty()) {
                                            completableSubscriber.onCompleted();
                                            return;
                                        } else {
                                            completableSubscriber.onError(CompletableOnSubscribeMerge.collectErrors(mpscLinkedQueue));
                                            return;
                                        }
                                    }
                                    atomicInteger.getAndIncrement();
                                    completable.subscribe(new C12741(compositeSubscription, mpscLinkedQueue, atomicInteger, completableSubscriber));
                                } else {
                                    return;
                                }
                            } catch (Throwable th) {
                                mpscLinkedQueue.offer(th);
                                if (atomicInteger.decrementAndGet() != 0) {
                                    return;
                                }
                                if (mpscLinkedQueue.isEmpty()) {
                                    completableSubscriber.onCompleted();
                                    return;
                                } else {
                                    completableSubscriber.onError(CompletableOnSubscribeMerge.collectErrors(mpscLinkedQueue));
                                    return;
                                }
                            }
                        }
                        return;
                    } else if (atomicInteger.decrementAndGet() != 0) {
                        return;
                    } else {
                        if (mpscLinkedQueue.isEmpty()) {
                            completableSubscriber.onCompleted();
                            return;
                        } else {
                            completableSubscriber.onError(CompletableOnSubscribeMerge.collectErrors(mpscLinkedQueue));
                            return;
                        }
                    }
                } catch (Throwable th2) {
                    mpscLinkedQueue.offer(th2);
                    if (atomicInteger.decrementAndGet() != 0) {
                        return;
                    }
                    if (mpscLinkedQueue.isEmpty()) {
                        completableSubscriber.onCompleted();
                        return;
                    } else {
                        completableSubscriber.onError(CompletableOnSubscribeMerge.collectErrors(mpscLinkedQueue));
                        return;
                    }
                }
            }
        } catch (Throwable th22) {
            completableSubscriber.onError(th22);
        }
    }
}

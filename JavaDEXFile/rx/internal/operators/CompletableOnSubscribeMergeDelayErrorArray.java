package rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import rx.Completable;
import rx.Completable.CompletableOnSubscribe;
import rx.Completable.CompletableSubscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public final class CompletableOnSubscribeMergeDelayErrorArray implements CompletableOnSubscribe {
    final Completable[] sources;

    /* renamed from: rx.internal.operators.CompletableOnSubscribeMergeDelayErrorArray.1 */
    class C12731 implements CompletableSubscriber {
        final /* synthetic */ Queue val$q;
        final /* synthetic */ CompletableSubscriber val$s;
        final /* synthetic */ CompositeSubscription val$set;
        final /* synthetic */ AtomicInteger val$wip;

        C12731(CompositeSubscription compositeSubscription, Queue queue, AtomicInteger atomicInteger, CompletableSubscriber completableSubscriber) {
            this.val$set = compositeSubscription;
            this.val$q = queue;
            this.val$wip = atomicInteger;
            this.val$s = completableSubscriber;
        }

        public void onCompleted() {
            tryTerminate();
        }

        public void onError(Throwable th) {
            this.val$q.offer(th);
            tryTerminate();
        }

        public void onSubscribe(Subscription subscription) {
            this.val$set.add(subscription);
        }

        void tryTerminate() {
            if (this.val$wip.decrementAndGet() != 0) {
                return;
            }
            if (this.val$q.isEmpty()) {
                this.val$s.onCompleted();
            } else {
                this.val$s.onError(CompletableOnSubscribeMerge.collectErrors(this.val$q));
            }
        }
    }

    public CompletableOnSubscribeMergeDelayErrorArray(Completable[] completableArr) {
        this.sources = completableArr;
    }

    public void call(CompletableSubscriber completableSubscriber) {
        CompositeSubscription compositeSubscription = new CompositeSubscription();
        AtomicInteger atomicInteger = new AtomicInteger(this.sources.length + 1);
        Queue concurrentLinkedQueue = new ConcurrentLinkedQueue();
        completableSubscriber.onSubscribe(compositeSubscription);
        Completable[] completableArr = this.sources;
        int length = completableArr.length;
        int i = 0;
        while (i < length) {
            Completable completable = completableArr[i];
            if (!compositeSubscription.isUnsubscribed()) {
                if (completable == null) {
                    concurrentLinkedQueue.offer(new NullPointerException("A completable source is null"));
                    atomicInteger.decrementAndGet();
                } else {
                    completable.subscribe(new C12731(compositeSubscription, concurrentLinkedQueue, atomicInteger, completableSubscriber));
                }
                i++;
            } else {
                return;
            }
        }
        if (atomicInteger.decrementAndGet() != 0) {
            return;
        }
        if (concurrentLinkedQueue.isEmpty()) {
            completableSubscriber.onCompleted();
        } else {
            completableSubscriber.onError(CompletableOnSubscribeMerge.collectErrors(concurrentLinkedQueue));
        }
    }
}

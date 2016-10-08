package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import rx.Completable;
import rx.Completable.CompletableOnSubscribe;
import rx.Completable.CompletableSubscriber;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.CompositeSubscription;

public final class CompletableOnSubscribeTimeout implements CompletableOnSubscribe {
    final Completable other;
    final Scheduler scheduler;
    final Completable source;
    final long timeout;
    final TimeUnit unit;

    /* renamed from: rx.internal.operators.CompletableOnSubscribeTimeout.1 */
    class C12771 implements Action0 {
        final /* synthetic */ AtomicBoolean val$once;
        final /* synthetic */ CompletableSubscriber val$s;
        final /* synthetic */ CompositeSubscription val$set;

        /* renamed from: rx.internal.operators.CompletableOnSubscribeTimeout.1.1 */
        class C12761 implements CompletableSubscriber {
            C12761() {
            }

            public void onCompleted() {
                C12771.this.val$set.unsubscribe();
                C12771.this.val$s.onCompleted();
            }

            public void onError(Throwable th) {
                C12771.this.val$set.unsubscribe();
                C12771.this.val$s.onError(th);
            }

            public void onSubscribe(Subscription subscription) {
                C12771.this.val$set.add(subscription);
            }
        }

        C12771(AtomicBoolean atomicBoolean, CompositeSubscription compositeSubscription, CompletableSubscriber completableSubscriber) {
            this.val$once = atomicBoolean;
            this.val$set = compositeSubscription;
            this.val$s = completableSubscriber;
        }

        public void call() {
            if (this.val$once.compareAndSet(false, true)) {
                this.val$set.clear();
                if (CompletableOnSubscribeTimeout.this.other == null) {
                    this.val$s.onError(new TimeoutException());
                } else {
                    CompletableOnSubscribeTimeout.this.other.subscribe(new C12761());
                }
            }
        }
    }

    /* renamed from: rx.internal.operators.CompletableOnSubscribeTimeout.2 */
    class C12782 implements CompletableSubscriber {
        final /* synthetic */ AtomicBoolean val$once;
        final /* synthetic */ CompletableSubscriber val$s;
        final /* synthetic */ CompositeSubscription val$set;

        C12782(CompositeSubscription compositeSubscription, AtomicBoolean atomicBoolean, CompletableSubscriber completableSubscriber) {
            this.val$set = compositeSubscription;
            this.val$once = atomicBoolean;
            this.val$s = completableSubscriber;
        }

        public void onCompleted() {
            if (this.val$once.compareAndSet(false, true)) {
                this.val$set.unsubscribe();
                this.val$s.onCompleted();
            }
        }

        public void onError(Throwable th) {
            if (this.val$once.compareAndSet(false, true)) {
                this.val$set.unsubscribe();
                this.val$s.onError(th);
                return;
            }
            RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
        }

        public void onSubscribe(Subscription subscription) {
            this.val$set.add(subscription);
        }
    }

    public CompletableOnSubscribeTimeout(Completable completable, long j, TimeUnit timeUnit, Scheduler scheduler, Completable completable2) {
        this.source = completable;
        this.timeout = j;
        this.unit = timeUnit;
        this.scheduler = scheduler;
        this.other = completable2;
    }

    public void call(CompletableSubscriber completableSubscriber) {
        CompositeSubscription compositeSubscription = new CompositeSubscription();
        completableSubscriber.onSubscribe(compositeSubscription);
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        Object createWorker = this.scheduler.createWorker();
        compositeSubscription.add(createWorker);
        createWorker.schedule(new C12771(atomicBoolean, compositeSubscription, completableSubscriber), this.timeout, this.unit);
        this.source.subscribe(new C12782(compositeSubscription, atomicBoolean, completableSubscriber));
    }
}

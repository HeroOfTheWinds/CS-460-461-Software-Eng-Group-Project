package rx.internal.operators;

import rx.Observable;
import rx.Single;
import rx.Single.OnSubscribe;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.SerialSubscription;

public final class SingleOnSubscribeDelaySubscriptionOther<T> implements OnSubscribe<T> {
    final Single<? extends T> main;
    final Observable<?> other;

    /* renamed from: rx.internal.operators.SingleOnSubscribeDelaySubscriptionOther.1 */
    class C14351 extends SingleSubscriber<T> {
        final /* synthetic */ SingleSubscriber val$subscriber;

        C14351(SingleSubscriber singleSubscriber) {
            this.val$subscriber = singleSubscriber;
        }

        public void onError(Throwable th) {
            this.val$subscriber.onError(th);
        }

        public void onSuccess(T t) {
            this.val$subscriber.onSuccess(t);
        }
    }

    /* renamed from: rx.internal.operators.SingleOnSubscribeDelaySubscriptionOther.2 */
    class C14362 extends Subscriber<Object> {
        boolean done;
        final /* synthetic */ SingleSubscriber val$child;
        final /* synthetic */ SerialSubscription val$serial;

        C14362(SingleSubscriber singleSubscriber, SerialSubscription serialSubscription) {
            this.val$child = singleSubscriber;
            this.val$serial = serialSubscription;
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                this.val$serial.set(this.val$child);
                SingleOnSubscribeDelaySubscriptionOther.this.main.subscribe(this.val$child);
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
                return;
            }
            this.done = true;
            this.val$child.onError(th);
        }

        public void onNext(Object obj) {
            onCompleted();
        }
    }

    public SingleOnSubscribeDelaySubscriptionOther(Single<? extends T> single, Observable<?> observable) {
        this.main = single;
        this.other = observable;
    }

    public void call(SingleSubscriber<? super T> singleSubscriber) {
        SingleSubscriber c14351 = new C14351(singleSubscriber);
        SerialSubscription serialSubscription = new SerialSubscription();
        singleSubscriber.add(serialSubscription);
        Subscriber c14362 = new C14362(c14351, serialSubscription);
        serialSubscription.set(c14362);
        this.other.subscribe(c14362);
    }
}

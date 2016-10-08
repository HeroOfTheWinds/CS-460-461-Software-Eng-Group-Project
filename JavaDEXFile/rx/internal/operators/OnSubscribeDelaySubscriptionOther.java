package rx.internal.operators;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.observers.Subscribers;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.SerialSubscription;
import rx.subscriptions.Subscriptions;

public final class OnSubscribeDelaySubscriptionOther<T, U> implements OnSubscribe<T> {
    final Observable<? extends T> main;
    final Observable<U> other;

    /* renamed from: rx.internal.operators.OnSubscribeDelaySubscriptionOther.1 */
    class C12851 extends Subscriber<U> {
        boolean done;
        final /* synthetic */ Subscriber val$child;
        final /* synthetic */ SerialSubscription val$serial;

        C12851(Subscriber subscriber, SerialSubscription serialSubscription) {
            this.val$child = subscriber;
            this.val$serial = serialSubscription;
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                this.val$serial.set(Subscriptions.unsubscribed());
                OnSubscribeDelaySubscriptionOther.this.main.unsafeSubscribe(this.val$child);
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

        public void onNext(U u) {
            onCompleted();
        }
    }

    public OnSubscribeDelaySubscriptionOther(Observable<? extends T> observable, Observable<U> observable2) {
        this.main = observable;
        this.other = observable2;
    }

    public void call(Subscriber<? super T> subscriber) {
        SerialSubscription serialSubscription = new SerialSubscription();
        subscriber.add(serialSubscription);
        Object c12851 = new C12851(Subscribers.wrap(subscriber), serialSubscription);
        serialSubscription.set(c12851);
        this.other.unsafeSubscribe(c12851);
    }
}

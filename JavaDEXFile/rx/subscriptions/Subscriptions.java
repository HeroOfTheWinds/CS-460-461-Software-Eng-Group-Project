package rx.subscriptions;

import java.util.concurrent.Future;
import rx.Subscription;
import rx.functions.Action0;

public final class Subscriptions {
    private static final Unsubscribed UNSUBSCRIBED;

    private static final class FutureSubscription implements Subscription {
        final Future<?> f920f;

        public FutureSubscription(Future<?> future) {
            this.f920f = future;
        }

        public boolean isUnsubscribed() {
            return this.f920f.isCancelled();
        }

        public void unsubscribe() {
            this.f920f.cancel(true);
        }
    }

    static final class Unsubscribed implements Subscription {
        Unsubscribed() {
        }

        public boolean isUnsubscribed() {
            return true;
        }

        public void unsubscribe() {
        }
    }

    static {
        UNSUBSCRIBED = new Unsubscribed();
    }

    private Subscriptions() {
        throw new IllegalStateException("No instances!");
    }

    public static Subscription create(Action0 action0) {
        return BooleanSubscription.create(action0);
    }

    public static Subscription empty() {
        return BooleanSubscription.create();
    }

    public static Subscription from(Future<?> future) {
        return new FutureSubscription(future);
    }

    public static CompositeSubscription from(Subscription... subscriptionArr) {
        return new CompositeSubscription(subscriptionArr);
    }

    public static Subscription unsubscribed() {
        return UNSUBSCRIBED;
    }
}

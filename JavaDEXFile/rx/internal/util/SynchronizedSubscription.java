package rx.internal.util;

import rx.Subscription;

public class SynchronizedSubscription implements Subscription {
    private final Subscription f916s;

    public SynchronizedSubscription(Subscription subscription) {
        this.f916s = subscription;
    }

    public boolean isUnsubscribed() {
        boolean isUnsubscribed;
        synchronized (this) {
            isUnsubscribed = this.f916s.isUnsubscribed();
        }
        return isUnsubscribed;
    }

    public void unsubscribe() {
        synchronized (this) {
            this.f916s.unsubscribe();
        }
    }
}

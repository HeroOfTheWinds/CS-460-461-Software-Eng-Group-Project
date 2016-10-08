package rx.subscriptions;

import java.util.concurrent.atomic.AtomicReference;
import rx.Subscription;

public final class MultipleAssignmentSubscription implements Subscription {
    final AtomicReference<State> state;

    private static final class State {
        final boolean isUnsubscribed;
        final Subscription subscription;

        State(boolean z, Subscription subscription) {
            this.isUnsubscribed = z;
            this.subscription = subscription;
        }

        State set(Subscription subscription) {
            return new State(this.isUnsubscribed, subscription);
        }

        State unsubscribe() {
            return new State(true, this.subscription);
        }
    }

    public MultipleAssignmentSubscription() {
        this.state = new AtomicReference(new State(false, Subscriptions.empty()));
    }

    public Subscription get() {
        return ((State) this.state.get()).subscription;
    }

    public boolean isUnsubscribed() {
        return ((State) this.state.get()).isUnsubscribed;
    }

    public void set(Subscription subscription) {
        if (subscription == null) {
            throw new IllegalArgumentException("Subscription can not be null");
        }
        AtomicReference atomicReference = this.state;
        State state;
        do {
            state = (State) atomicReference.get();
            if (state.isUnsubscribed) {
                subscription.unsubscribe();
                return;
            }
        } while (!atomicReference.compareAndSet(state, state.set(subscription)));
    }

    public void unsubscribe() {
        State state;
        AtomicReference atomicReference = this.state;
        do {
            state = (State) atomicReference.get();
            if (state.isUnsubscribed) {
                return;
            }
        } while (!atomicReference.compareAndSet(state, state.unsubscribe()));
        state.subscription.unsubscribe();
    }
}

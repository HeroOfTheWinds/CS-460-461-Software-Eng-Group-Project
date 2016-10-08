package com.upsight.android.internal.persistence.subscription;

import com.squareup.otto.Bus;
import com.upsight.android.internal.persistence.subscription.DataStoreEvent.Action;
import com.upsight.android.persistence.UpsightSubscription;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

public class Subscriptions {

    /* renamed from: com.upsight.android.internal.persistence.subscription.Subscriptions.1 */
    static final class C09171 implements Action1<T> {
        final /* synthetic */ Bus val$bus;
        final /* synthetic */ String val$type;

        C09171(Bus bus, String str) {
            this.val$bus = bus;
            this.val$type = str;
        }

        public void call(T t) {
            this.val$bus.post(new DataStoreEvent(Action.Created, this.val$type, t));
        }
    }

    /* renamed from: com.upsight.android.internal.persistence.subscription.Subscriptions.2 */
    static final class C09182 implements Action1<T> {
        final /* synthetic */ Bus val$bus;
        final /* synthetic */ String val$type;

        C09182(Bus bus, String str) {
            this.val$bus = bus;
            this.val$type = str;
        }

        public void call(T t) {
            this.val$bus.post(new DataStoreEvent(Action.Updated, this.val$type, t));
        }
    }

    /* renamed from: com.upsight.android.internal.persistence.subscription.Subscriptions.3 */
    static final class C09193 implements Action1<T> {
        final /* synthetic */ Bus val$bus;
        final /* synthetic */ String val$type;

        C09193(Bus bus, String str) {
            this.val$bus = bus;
            this.val$type = str;
        }

        public void call(T t) {
            this.val$bus.post(new DataStoreEvent(Action.Removed, this.val$type, t));
        }
    }

    private Subscriptions() {
    }

    public static AnnotatedSubscriber create(Object obj) {
        Object subscriptionHandlerVisitor = new SubscriptionHandlerVisitor(obj);
        new ClassSubscriptionReader(obj.getClass()).accept(subscriptionHandlerVisitor);
        return new AnnotatedSubscriber(subscriptionHandlerVisitor.getHandlers());
    }

    public static UpsightSubscription from(Subscription subscription) {
        return new SubscriptionAdapter(subscription);
    }

    public static <T> Action1<T> publishCreated(Bus bus, String str) {
        return new C09171(bus, str);
    }

    public static <T> Action1<T> publishRemoved(Bus bus, String str) {
        return new C09193(bus, str);
    }

    public static <T> Action1<T> publishUpdated(Bus bus, String str) {
        return new C09182(bus, str);
    }

    public static Observable<DataStoreEvent> toObservable(Bus bus) {
        return Observable.create(new OnSubscribeBus(bus)).onBackpressureBuffer();
    }
}

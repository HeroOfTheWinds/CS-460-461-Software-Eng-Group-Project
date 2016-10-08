package rx.observers;

import rx.Observer;
import rx.Subscriber;

public class SerializedSubscriber<T> extends Subscriber<T> {
    private final Observer<T> f918s;

    public SerializedSubscriber(Subscriber<? super T> subscriber) {
        this(subscriber, true);
    }

    public SerializedSubscriber(Subscriber<? super T> subscriber, boolean z) {
        super(subscriber, z);
        this.f918s = new SerializedObserver(subscriber);
    }

    public void onCompleted() {
        this.f918s.onCompleted();
    }

    public void onError(Throwable th) {
        this.f918s.onError(th);
    }

    public void onNext(T t) {
        this.f918s.onNext(t);
    }
}

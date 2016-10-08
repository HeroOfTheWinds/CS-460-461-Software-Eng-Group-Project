package rx.internal.operators;

import rx.Observable;
import rx.Observable.Operator;
import rx.Subscriber;
import rx.Subscription;
import rx.observers.SerializedSubscriber;

public final class OperatorTakeUntil<T, E> implements Operator<T, T> {
    private final Observable<? extends E> other;

    /* renamed from: rx.internal.operators.OperatorTakeUntil.1 */
    class C14001 extends Subscriber<T> {
        final /* synthetic */ Subscriber val$serial;

        C14001(Subscriber subscriber, boolean z, Subscriber subscriber2) {
            this.val$serial = subscriber2;
            super(subscriber, z);
        }

        public void onCompleted() {
            try {
                this.val$serial.onCompleted();
            } finally {
                this.val$serial.unsubscribe();
            }
        }

        public void onError(Throwable th) {
            try {
                this.val$serial.onError(th);
            } finally {
                this.val$serial.unsubscribe();
            }
        }

        public void onNext(T t) {
            this.val$serial.onNext(t);
        }
    }

    /* renamed from: rx.internal.operators.OperatorTakeUntil.2 */
    class C14012 extends Subscriber<E> {
        final /* synthetic */ Subscriber val$main;

        C14012(Subscriber subscriber) {
            this.val$main = subscriber;
        }

        public void onCompleted() {
            this.val$main.onCompleted();
        }

        public void onError(Throwable th) {
            this.val$main.onError(th);
        }

        public void onNext(E e) {
            onCompleted();
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }
    }

    public OperatorTakeUntil(Observable<? extends E> observable) {
        this.other = observable;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        Subscription serializedSubscriber = new SerializedSubscriber(subscriber, false);
        Object c14001 = new C14001(serializedSubscriber, false, serializedSubscriber);
        Object c14012 = new C14012(c14001);
        serializedSubscriber.add(c14001);
        serializedSubscriber.add(c14012);
        subscriber.add(serializedSubscriber);
        this.other.unsafeSubscribe(c14012);
        return c14001;
    }
}

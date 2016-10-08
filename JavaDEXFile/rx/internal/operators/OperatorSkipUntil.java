package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable;
import rx.Observable.Operator;
import rx.Subscriber;
import rx.observers.SerializedSubscriber;

public final class OperatorSkipUntil<T, U> implements Operator<T, T> {
    final Observable<U> other;

    /* renamed from: rx.internal.operators.OperatorSkipUntil.1 */
    class C13851 extends Subscriber<U> {
        final /* synthetic */ AtomicBoolean val$gate;
        final /* synthetic */ SerializedSubscriber val$s;

        C13851(AtomicBoolean atomicBoolean, SerializedSubscriber serializedSubscriber) {
            this.val$gate = atomicBoolean;
            this.val$s = serializedSubscriber;
        }

        public void onCompleted() {
            unsubscribe();
        }

        public void onError(Throwable th) {
            this.val$s.onError(th);
            this.val$s.unsubscribe();
        }

        public void onNext(U u) {
            this.val$gate.set(true);
            unsubscribe();
        }
    }

    /* renamed from: rx.internal.operators.OperatorSkipUntil.2 */
    class C13862 extends Subscriber<T> {
        final /* synthetic */ AtomicBoolean val$gate;
        final /* synthetic */ SerializedSubscriber val$s;

        C13862(Subscriber subscriber, AtomicBoolean atomicBoolean, SerializedSubscriber serializedSubscriber) {
            this.val$gate = atomicBoolean;
            this.val$s = serializedSubscriber;
            super(subscriber);
        }

        public void onCompleted() {
            this.val$s.onCompleted();
            unsubscribe();
        }

        public void onError(Throwable th) {
            this.val$s.onError(th);
            unsubscribe();
        }

        public void onNext(T t) {
            if (this.val$gate.get()) {
                this.val$s.onNext(t);
            } else {
                request(1);
            }
        }
    }

    public OperatorSkipUntil(Observable<U> observable) {
        this.other = observable;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        SerializedSubscriber serializedSubscriber = new SerializedSubscriber(subscriber);
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        Object c13851 = new C13851(atomicBoolean, serializedSubscriber);
        subscriber.add(c13851);
        this.other.unsafeSubscribe(c13851);
        return new C13862(subscriber, atomicBoolean, serializedSubscriber);
    }
}

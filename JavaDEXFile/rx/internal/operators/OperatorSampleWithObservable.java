package rx.internal.operators;

import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Observable.Operator;
import rx.Subscriber;
import rx.Subscription;
import rx.observers.SerializedSubscriber;

public final class OperatorSampleWithObservable<T, U> implements Operator<T, T> {
    static final Object EMPTY_TOKEN;
    final Observable<U> sampler;

    /* renamed from: rx.internal.operators.OperatorSampleWithObservable.1 */
    class C13711 extends Subscriber<U> {
        final /* synthetic */ AtomicReference val$main;
        final /* synthetic */ SerializedSubscriber val$s;
        final /* synthetic */ AtomicReference val$value;

        C13711(AtomicReference atomicReference, SerializedSubscriber serializedSubscriber, AtomicReference atomicReference2) {
            this.val$value = atomicReference;
            this.val$s = serializedSubscriber;
            this.val$main = atomicReference2;
        }

        public void onCompleted() {
            onNext(null);
            this.val$s.onCompleted();
            ((Subscription) this.val$main.get()).unsubscribe();
        }

        public void onError(Throwable th) {
            this.val$s.onError(th);
            ((Subscription) this.val$main.get()).unsubscribe();
        }

        public void onNext(U u) {
            Object andSet = this.val$value.getAndSet(OperatorSampleWithObservable.EMPTY_TOKEN);
            if (andSet != OperatorSampleWithObservable.EMPTY_TOKEN) {
                this.val$s.onNext(andSet);
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorSampleWithObservable.2 */
    class C13722 extends Subscriber<T> {
        final /* synthetic */ SerializedSubscriber val$s;
        final /* synthetic */ Subscriber val$samplerSub;
        final /* synthetic */ AtomicReference val$value;

        C13722(AtomicReference atomicReference, SerializedSubscriber serializedSubscriber, Subscriber subscriber) {
            this.val$value = atomicReference;
            this.val$s = serializedSubscriber;
            this.val$samplerSub = subscriber;
        }

        public void onCompleted() {
            this.val$samplerSub.onNext(null);
            this.val$s.onCompleted();
            this.val$samplerSub.unsubscribe();
        }

        public void onError(Throwable th) {
            this.val$s.onError(th);
            this.val$samplerSub.unsubscribe();
        }

        public void onNext(T t) {
            this.val$value.set(t);
        }
    }

    static {
        EMPTY_TOKEN = new Object();
    }

    public OperatorSampleWithObservable(Observable<U> observable) {
        this.sampler = observable;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        SerializedSubscriber serializedSubscriber = new SerializedSubscriber(subscriber);
        AtomicReference atomicReference = new AtomicReference(EMPTY_TOKEN);
        AtomicReference atomicReference2 = new AtomicReference();
        Object c13711 = new C13711(atomicReference, serializedSubscriber, atomicReference2);
        Object c13722 = new C13722(atomicReference, serializedSubscriber, c13711);
        atomicReference2.lazySet(c13722);
        subscriber.add(c13722);
        subscriber.add(c13711);
        this.sampler.unsafeSubscribe(c13711);
        return c13722;
    }
}

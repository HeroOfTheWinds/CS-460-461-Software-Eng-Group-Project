package rx.internal.operators;

import rx.Observable;
import rx.Observable.Operator;
import rx.Observer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.observers.SerializedSubscriber;
import rx.observers.Subscribers;
import rx.subjects.PublishSubject;

public final class OperatorDelayWithSelector<T, V> implements Operator<T, T> {
    final Func1<? super T, ? extends Observable<V>> itemDelay;
    final Observable<? extends T> source;

    /* renamed from: rx.internal.operators.OperatorDelayWithSelector.1 */
    class C13251 extends Subscriber<T> {
        final /* synthetic */ SerializedSubscriber val$child;
        final /* synthetic */ PublishSubject val$delayedEmissions;

        /* renamed from: rx.internal.operators.OperatorDelayWithSelector.1.1 */
        class C13241 implements Func1<V, T> {
            final /* synthetic */ Object val$t;

            C13241(Object obj) {
                this.val$t = obj;
            }

            public T call(V v) {
                return this.val$t;
            }
        }

        C13251(Subscriber subscriber, PublishSubject publishSubject, SerializedSubscriber serializedSubscriber) {
            this.val$delayedEmissions = publishSubject;
            this.val$child = serializedSubscriber;
            super(subscriber);
        }

        public void onCompleted() {
            this.val$delayedEmissions.onCompleted();
        }

        public void onError(Throwable th) {
            this.val$child.onError(th);
        }

        public void onNext(T t) {
            try {
                this.val$delayedEmissions.onNext(((Observable) OperatorDelayWithSelector.this.itemDelay.call(t)).take(1).defaultIfEmpty(null).map(new C13241(t)));
            } catch (Throwable th) {
                Exceptions.throwOrReport(th, (Observer) this);
            }
        }
    }

    public OperatorDelayWithSelector(Observable<? extends T> observable, Func1<? super T, ? extends Observable<V>> func1) {
        this.source = observable;
        this.itemDelay = func1;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        Object serializedSubscriber = new SerializedSubscriber(subscriber);
        Observable create = PublishSubject.create();
        subscriber.add(Observable.merge(create).unsafeSubscribe(Subscribers.from(serializedSubscriber)));
        return new C13251(subscriber, create, serializedSubscriber);
    }
}

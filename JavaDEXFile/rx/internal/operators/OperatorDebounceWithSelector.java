package rx.internal.operators;

import rx.Observable;
import rx.Observable.Operator;
import rx.Observer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.SerialSubscription;

public final class OperatorDebounceWithSelector<T, U> implements Operator<T, T> {
    final Func1<? super T, ? extends Observable<U>> selector;

    /* renamed from: rx.internal.operators.OperatorDebounceWithSelector.1 */
    class C13171 extends Subscriber<T> {
        final Subscriber<?> self;
        final DebounceState<T> state;
        final /* synthetic */ SerializedSubscriber val$s;
        final /* synthetic */ SerialSubscription val$ssub;

        /* renamed from: rx.internal.operators.OperatorDebounceWithSelector.1.1 */
        class C13161 extends Subscriber<U> {
            final /* synthetic */ int val$index;

            C13161(int i) {
                this.val$index = i;
            }

            public void onCompleted() {
                C13171.this.state.emit(this.val$index, C13171.this.val$s, C13171.this.self);
                unsubscribe();
            }

            public void onError(Throwable th) {
                C13171.this.self.onError(th);
            }

            public void onNext(U u) {
                onCompleted();
            }
        }

        C13171(Subscriber subscriber, SerializedSubscriber serializedSubscriber, SerialSubscription serialSubscription) {
            this.val$s = serializedSubscriber;
            this.val$ssub = serialSubscription;
            super(subscriber);
            this.state = new DebounceState();
            this.self = this;
        }

        public void onCompleted() {
            this.state.emitAndComplete(this.val$s, this);
        }

        public void onError(Throwable th) {
            this.val$s.onError(th);
            unsubscribe();
            this.state.clear();
        }

        public void onNext(T t) {
            try {
                Observable observable = (Observable) OperatorDebounceWithSelector.this.selector.call(t);
                Object c13161 = new C13161(this.state.next(t));
                this.val$ssub.set(c13161);
                observable.unsafeSubscribe(c13161);
            } catch (Throwable th) {
                Exceptions.throwOrReport(th, (Observer) this);
            }
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }
    }

    public OperatorDebounceWithSelector(Func1<? super T, ? extends Observable<U>> func1) {
        this.selector = func1;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        SerializedSubscriber serializedSubscriber = new SerializedSubscriber(subscriber);
        Object serialSubscription = new SerialSubscription();
        subscriber.add(serialSubscription);
        return new C13171(subscriber, serializedSubscriber, serialSubscription);
    }
}

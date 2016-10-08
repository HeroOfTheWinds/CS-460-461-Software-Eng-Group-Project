package rx.internal.operators;

import rx.Observable;
import rx.Observer;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public class OperatorTimeoutWithSelector<T, U, V> extends OperatorTimeoutBase<T> {

    /* renamed from: rx.internal.operators.OperatorTimeoutWithSelector.1 */
    class C14131 implements FirstTimeoutStub<T> {
        final /* synthetic */ Func0 val$firstTimeoutSelector;

        /* renamed from: rx.internal.operators.OperatorTimeoutWithSelector.1.1 */
        class C14121 extends Subscriber<U> {
            final /* synthetic */ Long val$seqId;
            final /* synthetic */ TimeoutSubscriber val$timeoutSubscriber;

            C14121(TimeoutSubscriber timeoutSubscriber, Long l) {
                this.val$timeoutSubscriber = timeoutSubscriber;
                this.val$seqId = l;
            }

            public void onCompleted() {
                this.val$timeoutSubscriber.onTimeout(this.val$seqId.longValue());
            }

            public void onError(Throwable th) {
                this.val$timeoutSubscriber.onError(th);
            }

            public void onNext(U u) {
                this.val$timeoutSubscriber.onTimeout(this.val$seqId.longValue());
            }
        }

        C14131(Func0 func0) {
            this.val$firstTimeoutSelector = func0;
        }

        public Subscription call(TimeoutSubscriber<T> timeoutSubscriber, Long l, Worker worker) {
            if (this.val$firstTimeoutSelector == null) {
                return Subscriptions.unsubscribed();
            }
            try {
                return ((Observable) this.val$firstTimeoutSelector.call()).unsafeSubscribe(new C14121(timeoutSubscriber, l));
            } catch (Throwable th) {
                Exceptions.throwOrReport(th, (Observer) timeoutSubscriber);
                return Subscriptions.unsubscribed();
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorTimeoutWithSelector.2 */
    class C14152 implements TimeoutStub<T> {
        final /* synthetic */ Func1 val$timeoutSelector;

        /* renamed from: rx.internal.operators.OperatorTimeoutWithSelector.2.1 */
        class C14141 extends Subscriber<V> {
            final /* synthetic */ Long val$seqId;
            final /* synthetic */ TimeoutSubscriber val$timeoutSubscriber;

            C14141(TimeoutSubscriber timeoutSubscriber, Long l) {
                this.val$timeoutSubscriber = timeoutSubscriber;
                this.val$seqId = l;
            }

            public void onCompleted() {
                this.val$timeoutSubscriber.onTimeout(this.val$seqId.longValue());
            }

            public void onError(Throwable th) {
                this.val$timeoutSubscriber.onError(th);
            }

            public void onNext(V v) {
                this.val$timeoutSubscriber.onTimeout(this.val$seqId.longValue());
            }
        }

        C14152(Func1 func1) {
            this.val$timeoutSelector = func1;
        }

        public Subscription call(TimeoutSubscriber<T> timeoutSubscriber, Long l, T t, Worker worker) {
            try {
                return ((Observable) this.val$timeoutSelector.call(t)).unsafeSubscribe(new C14141(timeoutSubscriber, l));
            } catch (Throwable th) {
                Exceptions.throwOrReport(th, (Observer) timeoutSubscriber);
                return Subscriptions.unsubscribed();
            }
        }
    }

    public OperatorTimeoutWithSelector(Func0<? extends Observable<U>> func0, Func1<? super T, ? extends Observable<V>> func1, Observable<? extends T> observable) {
        super(new C14131(func0), new C14152(func1), observable, Schedulers.immediate());
    }

    public /* bridge */ /* synthetic */ Subscriber call(Subscriber subscriber) {
        return super.call(subscriber);
    }
}

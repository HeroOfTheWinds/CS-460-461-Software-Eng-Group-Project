package rx.internal.operators;

import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.functions.Action1;

public class OperatorDoOnRequest<T> implements Operator<T, T> {
    final Action1<Long> request;

    /* renamed from: rx.internal.operators.OperatorDoOnRequest.1 */
    class C13321 implements Producer {
        final /* synthetic */ ParentSubscriber val$parent;

        C13321(ParentSubscriber parentSubscriber) {
            this.val$parent = parentSubscriber;
        }

        public void request(long j) {
            OperatorDoOnRequest.this.request.call(Long.valueOf(j));
            this.val$parent.requestMore(j);
        }
    }

    private static final class ParentSubscriber<T> extends Subscriber<T> {
        private final Subscriber<? super T> child;

        ParentSubscriber(Subscriber<? super T> subscriber) {
            this.child = subscriber;
            request(0);
        }

        private void requestMore(long j) {
            request(j);
        }

        public void onCompleted() {
            this.child.onCompleted();
        }

        public void onError(Throwable th) {
            this.child.onError(th);
        }

        public void onNext(T t) {
            this.child.onNext(t);
        }
    }

    public OperatorDoOnRequest(Action1<Long> action1) {
        this.request = action1;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        Object parentSubscriber = new ParentSubscriber(subscriber);
        subscriber.setProducer(new C13321(parentSubscriber));
        subscriber.add(parentSubscriber);
        return parentSubscriber;
    }
}

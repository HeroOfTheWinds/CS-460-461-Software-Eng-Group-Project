package rx.internal.operators;

import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.functions.Func1;

public final class OperatorTakeLast<T> implements Operator<T, T> {
    final int count;

    /* renamed from: rx.internal.operators.OperatorTakeLast.1 */
    class C13971 implements Producer {
        final /* synthetic */ TakeLastSubscriber val$parent;

        C13971(TakeLastSubscriber takeLastSubscriber) {
            this.val$parent = takeLastSubscriber;
        }

        public void request(long j) {
            this.val$parent.requestMore(j);
        }
    }

    static final class TakeLastSubscriber<T> extends Subscriber<T> implements Func1<Object, T> {
        final Subscriber<? super T> actual;
        final int count;
        final NotificationLite<T> nl;
        final ArrayDeque<Object> queue;
        final AtomicLong requested;

        public TakeLastSubscriber(Subscriber<? super T> subscriber, int i) {
            this.actual = subscriber;
            this.count = i;
            this.requested = new AtomicLong();
            this.queue = new ArrayDeque();
            this.nl = NotificationLite.instance();
        }

        public T call(Object obj) {
            return this.nl.getValue(obj);
        }

        public void onCompleted() {
            BackpressureUtils.postCompleteDone(this.requested, this.queue, this.actual, this);
        }

        public void onError(Throwable th) {
            this.queue.clear();
            this.actual.onError(th);
        }

        public void onNext(T t) {
            if (this.queue.size() == this.count) {
                this.queue.poll();
            }
            this.queue.offer(this.nl.next(t));
        }

        void requestMore(long j) {
            if (j > 0) {
                BackpressureUtils.postCompleteRequest(this.requested, j, this.queue, this.actual, this);
            }
        }
    }

    public OperatorTakeLast(int i) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("count cannot be negative");
        }
        this.count = i;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        Subscriber<? super T> takeLastSubscriber = new TakeLastSubscriber(subscriber, this.count);
        subscriber.add(takeLastSubscriber);
        subscriber.setProducer(new C13971(takeLastSubscriber));
        return takeLastSubscriber;
    }
}

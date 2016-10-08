package rx.internal.operators;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import rx.Observable.Operator;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Timestamped;

public class OperatorSkipLastTimed<T> implements Operator<T, T> {
    final Scheduler scheduler;
    final long timeInMillis;

    /* renamed from: rx.internal.operators.OperatorSkipLastTimed.1 */
    class C13821 extends Subscriber<T> {
        private Deque<Timestamped<T>> buffer;
        final /* synthetic */ Subscriber val$subscriber;

        C13821(Subscriber subscriber, Subscriber subscriber2) {
            this.val$subscriber = subscriber2;
            super(subscriber);
            this.buffer = new ArrayDeque();
        }

        private void emitItemsOutOfWindow(long j) {
            long j2 = OperatorSkipLastTimed.this.timeInMillis;
            while (!this.buffer.isEmpty()) {
                Timestamped timestamped = (Timestamped) this.buffer.getFirst();
                if (timestamped.getTimestampMillis() < j - j2) {
                    this.buffer.removeFirst();
                    this.val$subscriber.onNext(timestamped.getValue());
                } else {
                    return;
                }
            }
        }

        public void onCompleted() {
            emitItemsOutOfWindow(OperatorSkipLastTimed.this.scheduler.now());
            this.val$subscriber.onCompleted();
        }

        public void onError(Throwable th) {
            this.val$subscriber.onError(th);
        }

        public void onNext(T t) {
            long now = OperatorSkipLastTimed.this.scheduler.now();
            emitItemsOutOfWindow(now);
            this.buffer.offerLast(new Timestamped(now, t));
        }
    }

    public OperatorSkipLastTimed(long j, TimeUnit timeUnit, Scheduler scheduler) {
        this.timeInMillis = timeUnit.toMillis(j);
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        return new C13821(subscriber, subscriber);
    }
}

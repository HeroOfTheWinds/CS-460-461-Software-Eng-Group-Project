package rx.internal.operators;

import java.util.ArrayDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable.Operator;
import rx.Producer;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Func1;

public final class OperatorTakeLastTimed<T> implements Operator<T, T> {
    final long ageMillis;
    final int count;
    final Scheduler scheduler;

    /* renamed from: rx.internal.operators.OperatorTakeLastTimed.1 */
    class C13991 implements Producer {
        final /* synthetic */ TakeLastTimedSubscriber val$parent;

        C13991(TakeLastTimedSubscriber takeLastTimedSubscriber) {
            this.val$parent = takeLastTimedSubscriber;
        }

        public void request(long j) {
            this.val$parent.requestMore(j);
        }
    }

    static final class TakeLastTimedSubscriber<T> extends Subscriber<T> implements Func1<Object, T> {
        final Subscriber<? super T> actual;
        final long ageMillis;
        final int count;
        final NotificationLite<T> nl;
        final ArrayDeque<Object> queue;
        final ArrayDeque<Long> queueTimes;
        final AtomicLong requested;
        final Scheduler scheduler;

        public TakeLastTimedSubscriber(Subscriber<? super T> subscriber, int i, long j, Scheduler scheduler) {
            this.actual = subscriber;
            this.count = i;
            this.ageMillis = j;
            this.scheduler = scheduler;
            this.requested = new AtomicLong();
            this.queue = new ArrayDeque();
            this.queueTimes = new ArrayDeque();
            this.nl = NotificationLite.instance();
        }

        public T call(Object obj) {
            return this.nl.getValue(obj);
        }

        protected void evictOld(long j) {
            long j2 = this.ageMillis;
            while (true) {
                Long l = (Long) this.queueTimes.peek();
                if (l != null && l.longValue() < j - j2) {
                    this.queue.poll();
                    this.queueTimes.poll();
                } else {
                    return;
                }
            }
        }

        public void onCompleted() {
            evictOld(this.scheduler.now());
            this.queueTimes.clear();
            BackpressureUtils.postCompleteDone(this.requested, this.queue, this.actual, this);
        }

        public void onError(Throwable th) {
            this.queue.clear();
            this.queueTimes.clear();
            this.actual.onError(th);
        }

        public void onNext(T t) {
            if (this.count != 0) {
                long now = this.scheduler.now();
                if (this.queue.size() == this.count) {
                    this.queue.poll();
                    this.queueTimes.poll();
                }
                evictOld(now);
                this.queue.offer(this.nl.next(t));
                this.queueTimes.offer(Long.valueOf(now));
            }
        }

        void requestMore(long j) {
            BackpressureUtils.postCompleteRequest(this.requested, j, this.queue, this.actual, this);
        }
    }

    public OperatorTakeLastTimed(int i, long j, TimeUnit timeUnit, Scheduler scheduler) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("count could not be negative");
        }
        this.ageMillis = timeUnit.toMillis(j);
        this.scheduler = scheduler;
        this.count = i;
    }

    public OperatorTakeLastTimed(long j, TimeUnit timeUnit, Scheduler scheduler) {
        this.ageMillis = timeUnit.toMillis(j);
        this.scheduler = scheduler;
        this.count = -1;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        Subscriber<? super T> takeLastTimedSubscriber = new TakeLastTimedSubscriber(subscriber, this.count, this.ageMillis, this.scheduler);
        subscriber.add(takeLastTimedSubscriber);
        subscriber.setProducer(new C13991(takeLastTimedSubscriber));
        return takeLastTimedSubscriber;
    }
}

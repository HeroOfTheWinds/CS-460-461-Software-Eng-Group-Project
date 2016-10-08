package rx.schedulers;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.BooleanSubscription;
import rx.subscriptions.Subscriptions;

public final class TrampolineScheduler extends Scheduler {
    private static final TrampolineScheduler INSTANCE;

    private static class InnerCurrentThreadScheduler extends Worker implements Subscription {
        final AtomicInteger counter;
        private final BooleanSubscription innerSubscription;
        final PriorityBlockingQueue<TimedAction> queue;
        private final AtomicInteger wip;

        /* renamed from: rx.schedulers.TrampolineScheduler.InnerCurrentThreadScheduler.1 */
        class C15011 implements Action0 {
            final /* synthetic */ TimedAction val$timedAction;

            C15011(TimedAction timedAction) {
                this.val$timedAction = timedAction;
            }

            public void call() {
                InnerCurrentThreadScheduler.this.queue.remove(this.val$timedAction);
            }
        }

        InnerCurrentThreadScheduler() {
            this.counter = new AtomicInteger();
            this.queue = new PriorityBlockingQueue();
            this.innerSubscription = new BooleanSubscription();
            this.wip = new AtomicInteger();
        }

        private Subscription enqueue(Action0 action0, long j) {
            if (this.innerSubscription.isUnsubscribed()) {
                return Subscriptions.unsubscribed();
            }
            TimedAction timedAction = new TimedAction(action0, Long.valueOf(j), this.counter.incrementAndGet());
            this.queue.add(timedAction);
            if (this.wip.getAndIncrement() != 0) {
                return Subscriptions.create(new C15011(timedAction));
            }
            do {
                timedAction = (TimedAction) this.queue.poll();
                if (timedAction != null) {
                    timedAction.action.call();
                }
            } while (this.wip.decrementAndGet() > 0);
            return Subscriptions.unsubscribed();
        }

        public boolean isUnsubscribed() {
            return this.innerSubscription.isUnsubscribed();
        }

        public Subscription schedule(Action0 action0) {
            return enqueue(action0, now());
        }

        public Subscription schedule(Action0 action0, long j, TimeUnit timeUnit) {
            long now = now() + timeUnit.toMillis(j);
            return enqueue(new SleepingAction(action0, this, now), now);
        }

        public void unsubscribe() {
            this.innerSubscription.unsubscribe();
        }
    }

    private static final class TimedAction implements Comparable<TimedAction> {
        final Action0 action;
        final int count;
        final Long execTime;

        TimedAction(Action0 action0, Long l, int i) {
            this.action = action0;
            this.execTime = l;
            this.count = i;
        }

        public int compareTo(TimedAction timedAction) {
            int compareTo = this.execTime.compareTo(timedAction.execTime);
            return compareTo == 0 ? TrampolineScheduler.compare(this.count, timedAction.count) : compareTo;
        }
    }

    static {
        INSTANCE = new TrampolineScheduler();
    }

    TrampolineScheduler() {
    }

    static int compare(int i, int i2) {
        return i < i2 ? -1 : i == i2 ? 0 : 1;
    }

    static TrampolineScheduler instance() {
        return INSTANCE;
    }

    public Worker createWorker() {
        return new InnerCurrentThreadScheduler();
    }
}

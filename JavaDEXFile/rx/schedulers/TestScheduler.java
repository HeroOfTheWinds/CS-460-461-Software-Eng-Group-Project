package rx.schedulers;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.BooleanSubscription;
import rx.subscriptions.Subscriptions;

public class TestScheduler extends Scheduler {
    static long counter;
    final Queue<TimedAction> queue;
    long time;

    private static class CompareActionsByTime implements Comparator<TimedAction> {
        CompareActionsByTime() {
        }

        public int compare(TimedAction timedAction, TimedAction timedAction2) {
            if (timedAction.time == timedAction2.time) {
                if (timedAction.count >= timedAction2.count) {
                    return timedAction.count <= timedAction2.count ? 0 : 1;
                }
            } else if (timedAction.time >= timedAction2.time) {
                return timedAction.time <= timedAction2.time ? 0 : 1;
            }
            return -1;
        }
    }

    private final class InnerTestScheduler extends Worker {
        private final BooleanSubscription f919s;

        /* renamed from: rx.schedulers.TestScheduler.InnerTestScheduler.1 */
        class C14991 implements Action0 {
            final /* synthetic */ TimedAction val$timedAction;

            C14991(TimedAction timedAction) {
                this.val$timedAction = timedAction;
            }

            public void call() {
                TestScheduler.this.queue.remove(this.val$timedAction);
            }
        }

        /* renamed from: rx.schedulers.TestScheduler.InnerTestScheduler.2 */
        class C15002 implements Action0 {
            final /* synthetic */ TimedAction val$timedAction;

            C15002(TimedAction timedAction) {
                this.val$timedAction = timedAction;
            }

            public void call() {
                TestScheduler.this.queue.remove(this.val$timedAction);
            }
        }

        InnerTestScheduler() {
            this.f919s = new BooleanSubscription();
        }

        public boolean isUnsubscribed() {
            return this.f919s.isUnsubscribed();
        }

        public long now() {
            return TestScheduler.this.now();
        }

        public Subscription schedule(Action0 action0) {
            TimedAction timedAction = new TimedAction(this, 0, action0);
            TestScheduler.this.queue.add(timedAction);
            return Subscriptions.create(new C15002(timedAction));
        }

        public Subscription schedule(Action0 action0, long j, TimeUnit timeUnit) {
            TimedAction timedAction = new TimedAction(this, TestScheduler.this.time + timeUnit.toNanos(j), action0);
            TestScheduler.this.queue.add(timedAction);
            return Subscriptions.create(new C14991(timedAction));
        }

        public void unsubscribe() {
            this.f919s.unsubscribe();
        }
    }

    private static final class TimedAction {
        final Action0 action;
        private final long count;
        final Worker scheduler;
        final long time;

        TimedAction(Worker worker, long j, Action0 action0) {
            long j2 = TestScheduler.counter;
            TestScheduler.counter = 1 + j2;
            this.count = j2;
            this.time = j;
            this.action = action0;
            this.scheduler = worker;
        }

        public String toString() {
            return String.format("TimedAction(time = %d, action = %s)", new Object[]{Long.valueOf(this.time), this.action.toString()});
        }
    }

    static {
        counter = 0;
    }

    public TestScheduler() {
        this.queue = new PriorityQueue(11, new CompareActionsByTime());
    }

    private void triggerActions(long j) {
        while (!this.queue.isEmpty()) {
            TimedAction timedAction = (TimedAction) this.queue.peek();
            if (timedAction.time > j) {
                break;
            }
            this.time = timedAction.time == 0 ? this.time : timedAction.time;
            this.queue.remove();
            if (!timedAction.scheduler.isUnsubscribed()) {
                timedAction.action.call();
            }
        }
        this.time = j;
    }

    public void advanceTimeBy(long j, TimeUnit timeUnit) {
        advanceTimeTo(this.time + timeUnit.toNanos(j), TimeUnit.NANOSECONDS);
    }

    public void advanceTimeTo(long j, TimeUnit timeUnit) {
        triggerActions(timeUnit.toNanos(j));
    }

    public Worker createWorker() {
        return new InnerTestScheduler();
    }

    public long now() {
        return TimeUnit.NANOSECONDS.toMillis(this.time);
    }

    public void triggerActions() {
        triggerActions(this.time);
    }
}

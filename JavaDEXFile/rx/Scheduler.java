package rx;

import java.util.concurrent.TimeUnit;
import rx.functions.Action0;
import rx.subscriptions.MultipleAssignmentSubscription;

public abstract class Scheduler {
    static final long CLOCK_DRIFT_TOLERANCE_NANOS;

    public static abstract class Worker implements Subscription {

        /* renamed from: rx.Scheduler.Worker.1 */
        class C12201 implements Action0 {
            long count;
            long lastNowNanos;
            long startInNanos;
            final /* synthetic */ Action0 val$action;
            final /* synthetic */ long val$firstNowNanos;
            final /* synthetic */ long val$firstStartInNanos;
            final /* synthetic */ MultipleAssignmentSubscription val$mas;
            final /* synthetic */ long val$periodInNanos;

            C12201(long j, long j2, MultipleAssignmentSubscription multipleAssignmentSubscription, Action0 action0, long j3) {
                this.val$firstNowNanos = j;
                this.val$firstStartInNanos = j2;
                this.val$mas = multipleAssignmentSubscription;
                this.val$action = action0;
                this.val$periodInNanos = j3;
                this.lastNowNanos = this.val$firstNowNanos;
                this.startInNanos = this.val$firstStartInNanos;
            }

            public void call() {
                if (!this.val$mas.isUnsubscribed()) {
                    long j;
                    this.val$action.call();
                    long toNanos = TimeUnit.MILLISECONDS.toNanos(Worker.this.now());
                    long j2;
                    if (Scheduler.CLOCK_DRIFT_TOLERANCE_NANOS + toNanos < this.lastNowNanos || toNanos >= (this.lastNowNanos + this.val$periodInNanos) + Scheduler.CLOCK_DRIFT_TOLERANCE_NANOS) {
                        j = this.val$periodInNanos + toNanos;
                        j2 = this.val$periodInNanos;
                        long j3 = this.count + 1;
                        this.count = j3;
                        this.startInNanos = j - (j2 * j3);
                    } else {
                        j = this.startInNanos;
                        j2 = this.count + 1;
                        this.count = j2;
                        j += j2 * this.val$periodInNanos;
                    }
                    this.lastNowNanos = toNanos;
                    this.val$mas.set(Worker.this.schedule(this, j - toNanos, TimeUnit.NANOSECONDS));
                }
            }
        }

        public long now() {
            return System.currentTimeMillis();
        }

        public abstract Subscription schedule(Action0 action0);

        public abstract Subscription schedule(Action0 action0, long j, TimeUnit timeUnit);

        public Subscription schedulePeriodically(Action0 action0, long j, long j2, TimeUnit timeUnit) {
            long toNanos = timeUnit.toNanos(j2);
            long toNanos2 = TimeUnit.MILLISECONDS.toNanos(now());
            long toNanos3 = timeUnit.toNanos(j);
            Subscription multipleAssignmentSubscription = new MultipleAssignmentSubscription();
            Action0 c12201 = new C12201(toNanos2, toNanos3 + toNanos2, multipleAssignmentSubscription, action0, toNanos);
            Object multipleAssignmentSubscription2 = new MultipleAssignmentSubscription();
            multipleAssignmentSubscription.set(multipleAssignmentSubscription2);
            multipleAssignmentSubscription2.set(schedule(c12201, j, timeUnit));
            return multipleAssignmentSubscription;
        }
    }

    static {
        CLOCK_DRIFT_TOLERANCE_NANOS = TimeUnit.MINUTES.toNanos(Long.getLong("rx.scheduler.drift-tolerance", 15).longValue());
    }

    public abstract Worker createWorker();

    public long now() {
        return System.currentTimeMillis();
    }
}

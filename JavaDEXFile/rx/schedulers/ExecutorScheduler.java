package rx.schedulers;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscription;
import rx.functions.Action0;
import rx.internal.schedulers.GenericScheduledExecutorService;
import rx.internal.schedulers.ScheduledAction;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.MultipleAssignmentSubscription;
import rx.subscriptions.Subscriptions;

final class ExecutorScheduler extends Scheduler {
    final Executor executor;

    static final class ExecutorSchedulerWorker extends Worker implements Runnable {
        final Executor executor;
        final ConcurrentLinkedQueue<ScheduledAction> queue;
        final ScheduledExecutorService service;
        final CompositeSubscription tasks;
        final AtomicInteger wip;

        /* renamed from: rx.schedulers.ExecutorScheduler.ExecutorSchedulerWorker.1 */
        class C14971 implements Action0 {
            final /* synthetic */ MultipleAssignmentSubscription val$mas;

            C14971(MultipleAssignmentSubscription multipleAssignmentSubscription) {
                this.val$mas = multipleAssignmentSubscription;
            }

            public void call() {
                ExecutorSchedulerWorker.this.tasks.remove(this.val$mas);
            }
        }

        /* renamed from: rx.schedulers.ExecutorScheduler.ExecutorSchedulerWorker.2 */
        class C14982 implements Action0 {
            final /* synthetic */ Action0 val$action;
            final /* synthetic */ MultipleAssignmentSubscription val$mas;
            final /* synthetic */ Subscription val$removeMas;

            C14982(MultipleAssignmentSubscription multipleAssignmentSubscription, Action0 action0, Subscription subscription) {
                this.val$mas = multipleAssignmentSubscription;
                this.val$action = action0;
                this.val$removeMas = subscription;
            }

            public void call() {
                if (!this.val$mas.isUnsubscribed()) {
                    Subscription schedule = ExecutorSchedulerWorker.this.schedule(this.val$action);
                    this.val$mas.set(schedule);
                    if (schedule.getClass() == ScheduledAction.class) {
                        ((ScheduledAction) schedule).add(this.val$removeMas);
                    }
                }
            }
        }

        public ExecutorSchedulerWorker(Executor executor) {
            this.executor = executor;
            this.queue = new ConcurrentLinkedQueue();
            this.wip = new AtomicInteger();
            this.tasks = new CompositeSubscription();
            this.service = GenericScheduledExecutorService.getInstance();
        }

        public boolean isUnsubscribed() {
            return this.tasks.isUnsubscribed();
        }

        public void run() {
            while (!this.tasks.isUnsubscribed()) {
                ScheduledAction scheduledAction = (ScheduledAction) this.queue.poll();
                if (scheduledAction != null) {
                    if (!scheduledAction.isUnsubscribed()) {
                        scheduledAction.run();
                    }
                    if (this.wip.decrementAndGet() == 0) {
                        return;
                    }
                }
                return;
            }
            this.queue.clear();
        }

        public Subscription schedule(Action0 action0) {
            if (isUnsubscribed()) {
                return Subscriptions.unsubscribed();
            }
            Subscription scheduledAction = new ScheduledAction(action0, this.tasks);
            this.tasks.add(scheduledAction);
            this.queue.offer(scheduledAction);
            if (this.wip.getAndIncrement() != 0) {
                return scheduledAction;
            }
            try {
                this.executor.execute(this);
                return scheduledAction;
            } catch (Throwable e) {
                this.tasks.remove(scheduledAction);
                this.wip.decrementAndGet();
                RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                throw e;
            }
        }

        public Subscription schedule(Action0 action0, long j, TimeUnit timeUnit) {
            if (j <= 0) {
                return schedule(action0);
            }
            if (isUnsubscribed()) {
                return Subscriptions.unsubscribed();
            }
            Object multipleAssignmentSubscription = new MultipleAssignmentSubscription();
            MultipleAssignmentSubscription multipleAssignmentSubscription2 = new MultipleAssignmentSubscription();
            multipleAssignmentSubscription2.set(multipleAssignmentSubscription);
            this.tasks.add(multipleAssignmentSubscription2);
            Subscription create = Subscriptions.create(new C14971(multipleAssignmentSubscription2));
            ScheduledAction scheduledAction = new ScheduledAction(new C14982(multipleAssignmentSubscription2, action0, create));
            multipleAssignmentSubscription.set(scheduledAction);
            try {
                scheduledAction.add(this.service.schedule(scheduledAction, j, timeUnit));
                return create;
            } catch (Throwable e) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                throw e;
            }
        }

        public void unsubscribe() {
            this.tasks.unsubscribe();
            this.queue.clear();
        }
    }

    public ExecutorScheduler(Executor executor) {
        this.executor = executor;
    }

    public Worker createWorker() {
        return new ExecutorSchedulerWorker(this.executor);
    }
}

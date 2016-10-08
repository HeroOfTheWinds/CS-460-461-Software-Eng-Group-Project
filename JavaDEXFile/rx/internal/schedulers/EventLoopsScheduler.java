package rx.internal.schedulers;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscription;
import rx.functions.Action0;
import rx.internal.util.RxThreadFactory;
import rx.internal.util.SubscriptionList;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

public class EventLoopsScheduler extends Scheduler implements SchedulerLifecycle {
    static final String KEY_MAX_THREADS = "rx.scheduler.max-computation-threads";
    static final int MAX_THREADS;
    static final FixedSchedulerPool NONE;
    static final PoolWorker SHUTDOWN_WORKER;
    static final RxThreadFactory THREAD_FACTORY;
    private static final String THREAD_NAME_PREFIX = "RxComputationThreadPool-";
    final AtomicReference<FixedSchedulerPool> pool;

    private static class EventLoopWorker extends Worker {
        private final SubscriptionList both;
        private final PoolWorker poolWorker;
        private final SubscriptionList serial;
        private final CompositeSubscription timed;

        EventLoopWorker(PoolWorker poolWorker) {
            this.serial = new SubscriptionList();
            this.timed = new CompositeSubscription();
            this.both = new SubscriptionList(this.serial, this.timed);
            this.poolWorker = poolWorker;
        }

        public boolean isUnsubscribed() {
            return this.both.isUnsubscribed();
        }

        public Subscription schedule(Action0 action0) {
            if (isUnsubscribed()) {
                return Subscriptions.unsubscribed();
            }
            return this.poolWorker.scheduleActual(action0, 0, null, this.serial);
        }

        public Subscription schedule(Action0 action0, long j, TimeUnit timeUnit) {
            if (isUnsubscribed()) {
                return Subscriptions.unsubscribed();
            }
            return this.poolWorker.scheduleActual(action0, j, timeUnit, this.timed);
        }

        public void unsubscribe() {
            this.both.unsubscribe();
        }
    }

    static final class FixedSchedulerPool {
        final int cores;
        final PoolWorker[] eventLoops;
        long f911n;

        FixedSchedulerPool(int i) {
            this.cores = i;
            this.eventLoops = new PoolWorker[i];
            for (int i2 = EventLoopsScheduler.MAX_THREADS; i2 < i; i2++) {
                this.eventLoops[i2] = new PoolWorker(EventLoopsScheduler.THREAD_FACTORY);
            }
        }

        public PoolWorker getEventLoop() {
            int i = this.cores;
            if (i == 0) {
                return EventLoopsScheduler.SHUTDOWN_WORKER;
            }
            PoolWorker[] poolWorkerArr = this.eventLoops;
            long j = this.f911n;
            this.f911n = 1 + j;
            return poolWorkerArr[(int) (j % ((long) i))];
        }

        public void shutdown() {
            PoolWorker[] poolWorkerArr = this.eventLoops;
            int length = poolWorkerArr.length;
            for (int i = EventLoopsScheduler.MAX_THREADS; i < length; i++) {
                poolWorkerArr[i].unsubscribe();
            }
        }
    }

    static final class PoolWorker extends NewThreadWorker {
        PoolWorker(ThreadFactory threadFactory) {
            super(threadFactory);
        }
    }

    static {
        THREAD_FACTORY = new RxThreadFactory(THREAD_NAME_PREFIX);
        int intValue = Integer.getInteger(KEY_MAX_THREADS, MAX_THREADS).intValue();
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        if (intValue <= 0 || intValue > availableProcessors) {
            intValue = availableProcessors;
        }
        MAX_THREADS = intValue;
        SHUTDOWN_WORKER = new PoolWorker(new RxThreadFactory("RxComputationShutdown-"));
        SHUTDOWN_WORKER.unsubscribe();
        NONE = new FixedSchedulerPool(MAX_THREADS);
    }

    public EventLoopsScheduler() {
        this.pool = new AtomicReference(NONE);
        start();
    }

    public Worker createWorker() {
        return new EventLoopWorker(((FixedSchedulerPool) this.pool.get()).getEventLoop());
    }

    public Subscription scheduleDirect(Action0 action0) {
        return ((FixedSchedulerPool) this.pool.get()).getEventLoop().scheduleActual(action0, -1, TimeUnit.NANOSECONDS);
    }

    public void shutdown() {
        FixedSchedulerPool fixedSchedulerPool;
        do {
            fixedSchedulerPool = (FixedSchedulerPool) this.pool.get();
            if (fixedSchedulerPool == NONE) {
                return;
            }
        } while (!this.pool.compareAndSet(fixedSchedulerPool, NONE));
        fixedSchedulerPool.shutdown();
    }

    public void start() {
        FixedSchedulerPool fixedSchedulerPool = new FixedSchedulerPool(MAX_THREADS);
        if (!this.pool.compareAndSet(NONE, fixedSchedulerPool)) {
            fixedSchedulerPool.shutdown();
        }
    }
}

package rx.schedulers;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReference;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscription;
import rx.functions.Action0;
import rx.internal.schedulers.NewThreadWorker;
import rx.internal.schedulers.SchedulerLifecycle;
import rx.internal.util.RxThreadFactory;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

final class CachedThreadScheduler extends Scheduler implements SchedulerLifecycle {
    static final RxThreadFactory EVICTOR_THREAD_FACTORY;
    private static final String EVICTOR_THREAD_NAME_PREFIX = "RxCachedWorkerPoolEvictor-";
    private static final long KEEP_ALIVE_TIME = 60;
    private static final TimeUnit KEEP_ALIVE_UNIT;
    static final CachedWorkerPool NONE;
    static final ThreadWorker SHUTDOWN_THREADWORKER;
    static final RxThreadFactory WORKER_THREAD_FACTORY;
    private static final String WORKER_THREAD_NAME_PREFIX = "RxCachedThreadScheduler-";
    final AtomicReference<CachedWorkerPool> pool;

    private static final class CachedWorkerPool {
        private final CompositeSubscription allWorkers;
        private final ScheduledExecutorService evictorService;
        private final Future<?> evictorTask;
        private final ConcurrentLinkedQueue<ThreadWorker> expiringWorkerQueue;
        private final long keepAliveTime;

        /* renamed from: rx.schedulers.CachedThreadScheduler.CachedWorkerPool.1 */
        class C14961 implements Runnable {
            C14961() {
            }

            public void run() {
                CachedWorkerPool.this.evictExpiredWorkers();
            }
        }

        CachedWorkerPool(long j, TimeUnit timeUnit) {
            Future scheduleWithFixedDelay;
            ScheduledExecutorService scheduledExecutorService = null;
            this.keepAliveTime = timeUnit != null ? timeUnit.toNanos(j) : 0;
            this.expiringWorkerQueue = new ConcurrentLinkedQueue();
            this.allWorkers = new CompositeSubscription();
            if (timeUnit != null) {
                ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(1, CachedThreadScheduler.EVICTOR_THREAD_FACTORY);
                NewThreadWorker.tryEnableCancelPolicy(newScheduledThreadPool);
                scheduledExecutorService = newScheduledThreadPool;
                scheduleWithFixedDelay = newScheduledThreadPool.scheduleWithFixedDelay(new C14961(), this.keepAliveTime, this.keepAliveTime, TimeUnit.NANOSECONDS);
            } else {
                scheduleWithFixedDelay = null;
            }
            this.evictorService = scheduledExecutorService;
            this.evictorTask = scheduleWithFixedDelay;
        }

        void evictExpiredWorkers() {
            if (!this.expiringWorkerQueue.isEmpty()) {
                long now = now();
                Iterator it = this.expiringWorkerQueue.iterator();
                while (it.hasNext()) {
                    ThreadWorker threadWorker = (ThreadWorker) it.next();
                    if (threadWorker.getExpirationTime() > now) {
                        return;
                    }
                    if (this.expiringWorkerQueue.remove(threadWorker)) {
                        this.allWorkers.remove(threadWorker);
                    }
                }
            }
        }

        ThreadWorker get() {
            if (this.allWorkers.isUnsubscribed()) {
                return CachedThreadScheduler.SHUTDOWN_THREADWORKER;
            }
            while (!this.expiringWorkerQueue.isEmpty()) {
                ThreadWorker threadWorker = (ThreadWorker) this.expiringWorkerQueue.poll();
                if (threadWorker != null) {
                    return threadWorker;
                }
            }
            Object threadWorker2 = new ThreadWorker(CachedThreadScheduler.WORKER_THREAD_FACTORY);
            this.allWorkers.add(threadWorker2);
            return threadWorker2;
        }

        long now() {
            return System.nanoTime();
        }

        void release(ThreadWorker threadWorker) {
            threadWorker.setExpirationTime(now() + this.keepAliveTime);
            this.expiringWorkerQueue.offer(threadWorker);
        }

        void shutdown() {
            try {
                if (this.evictorTask != null) {
                    this.evictorTask.cancel(true);
                }
                if (this.evictorService != null) {
                    this.evictorService.shutdownNow();
                }
                this.allWorkers.unsubscribe();
            } catch (Throwable th) {
                this.allWorkers.unsubscribe();
            }
        }
    }

    private static final class EventLoopWorker extends Worker {
        static final AtomicIntegerFieldUpdater<EventLoopWorker> ONCE_UPDATER;
        private final CompositeSubscription innerSubscription;
        volatile int once;
        private final CachedWorkerPool pool;
        private final ThreadWorker threadWorker;

        static {
            ONCE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(EventLoopWorker.class, "once");
        }

        EventLoopWorker(CachedWorkerPool cachedWorkerPool) {
            this.innerSubscription = new CompositeSubscription();
            this.pool = cachedWorkerPool;
            this.threadWorker = cachedWorkerPool.get();
        }

        public boolean isUnsubscribed() {
            return this.innerSubscription.isUnsubscribed();
        }

        public Subscription schedule(Action0 action0) {
            return schedule(action0, 0, null);
        }

        public Subscription schedule(Action0 action0, long j, TimeUnit timeUnit) {
            if (this.innerSubscription.isUnsubscribed()) {
                return Subscriptions.unsubscribed();
            }
            Subscription scheduleActual = this.threadWorker.scheduleActual(action0, j, timeUnit);
            this.innerSubscription.add(scheduleActual);
            scheduleActual.addParent(this.innerSubscription);
            return scheduleActual;
        }

        public void unsubscribe() {
            if (ONCE_UPDATER.compareAndSet(this, 0, 1)) {
                this.pool.release(this.threadWorker);
            }
            this.innerSubscription.unsubscribe();
        }
    }

    private static final class ThreadWorker extends NewThreadWorker {
        private long expirationTime;

        ThreadWorker(ThreadFactory threadFactory) {
            super(threadFactory);
            this.expirationTime = 0;
        }

        public long getExpirationTime() {
            return this.expirationTime;
        }

        public void setExpirationTime(long j) {
            this.expirationTime = j;
        }
    }

    static {
        WORKER_THREAD_FACTORY = new RxThreadFactory(WORKER_THREAD_NAME_PREFIX);
        EVICTOR_THREAD_FACTORY = new RxThreadFactory(EVICTOR_THREAD_NAME_PREFIX);
        KEEP_ALIVE_UNIT = TimeUnit.SECONDS;
        SHUTDOWN_THREADWORKER = new ThreadWorker(new RxThreadFactory("RxCachedThreadSchedulerShutdown-"));
        SHUTDOWN_THREADWORKER.unsubscribe();
        NONE = new CachedWorkerPool(0, null);
        NONE.shutdown();
    }

    public CachedThreadScheduler() {
        this.pool = new AtomicReference(NONE);
        start();
    }

    public Worker createWorker() {
        return new EventLoopWorker((CachedWorkerPool) this.pool.get());
    }

    public void shutdown() {
        CachedWorkerPool cachedWorkerPool;
        do {
            cachedWorkerPool = (CachedWorkerPool) this.pool.get();
            if (cachedWorkerPool == NONE) {
                return;
            }
        } while (!this.pool.compareAndSet(cachedWorkerPool, NONE));
        cachedWorkerPool.shutdown();
    }

    public void start() {
        CachedWorkerPool cachedWorkerPool = new CachedWorkerPool(KEEP_ALIVE_TIME, KEEP_ALIVE_UNIT);
        if (!this.pool.compareAndSet(NONE, cachedWorkerPool)) {
            cachedWorkerPool.shutdown();
        }
    }
}

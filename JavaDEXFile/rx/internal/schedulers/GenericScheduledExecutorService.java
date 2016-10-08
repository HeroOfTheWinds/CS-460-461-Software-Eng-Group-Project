package rx.internal.schedulers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;
import rx.internal.util.RxThreadFactory;

public final class GenericScheduledExecutorService implements SchedulerLifecycle {
    public static final GenericScheduledExecutorService INSTANCE;
    private static final ScheduledExecutorService[] NONE;
    private static final ScheduledExecutorService SHUTDOWN;
    private static final RxThreadFactory THREAD_FACTORY;
    private static final String THREAD_NAME_PREFIX = "RxScheduledExecutorPool-";
    private static int roundRobin;
    private final AtomicReference<ScheduledExecutorService[]> executor;

    static {
        THREAD_FACTORY = new RxThreadFactory(THREAD_NAME_PREFIX);
        NONE = new ScheduledExecutorService[0];
        SHUTDOWN = Executors.newScheduledThreadPool(0);
        SHUTDOWN.shutdown();
        INSTANCE = new GenericScheduledExecutorService();
    }

    private GenericScheduledExecutorService() {
        this.executor = new AtomicReference(NONE);
        start();
    }

    public static ScheduledExecutorService getInstance() {
        ScheduledExecutorService[] scheduledExecutorServiceArr = (ScheduledExecutorService[]) INSTANCE.executor.get();
        if (scheduledExecutorServiceArr == NONE) {
            return SHUTDOWN;
        }
        int i = roundRobin + 1;
        if (i >= scheduledExecutorServiceArr.length) {
            i = 0;
        }
        roundRobin = i;
        return scheduledExecutorServiceArr[i];
    }

    public void shutdown() {
        ScheduledExecutorService[] scheduledExecutorServiceArr;
        do {
            scheduledExecutorServiceArr = (ScheduledExecutorService[]) this.executor.get();
            if (scheduledExecutorServiceArr == NONE) {
                return;
            }
        } while (!this.executor.compareAndSet(scheduledExecutorServiceArr, NONE));
        for (ScheduledExecutorService scheduledExecutorService : scheduledExecutorServiceArr) {
            NewThreadWorker.deregisterExecutor(scheduledExecutorService);
            scheduledExecutorService.shutdownNow();
        }
    }

    public void start() {
        int i = 8;
        int i2 = 0;
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        if (availableProcessors > 4) {
            availableProcessors /= 2;
        }
        if (availableProcessors <= 8) {
            i = availableProcessors;
        }
        Object obj = new ScheduledExecutorService[i];
        for (availableProcessors = 0; availableProcessors < i; availableProcessors++) {
            obj[availableProcessors] = Executors.newScheduledThreadPool(1, THREAD_FACTORY);
        }
        if (this.executor.compareAndSet(NONE, obj)) {
            availableProcessors = obj.length;
            while (i2 < availableProcessors) {
                ScheduledExecutorService scheduledExecutorService = obj[i2];
                if (!NewThreadWorker.tryEnableCancelPolicy(scheduledExecutorService) && (scheduledExecutorService instanceof ScheduledThreadPoolExecutor)) {
                    NewThreadWorker.registerExecutor((ScheduledThreadPoolExecutor) scheduledExecutorService);
                }
                i2++;
            }
            return;
        }
        for (ScheduledExecutorService shutdownNow : obj) {
            shutdownNow.shutdownNow();
        }
    }
}

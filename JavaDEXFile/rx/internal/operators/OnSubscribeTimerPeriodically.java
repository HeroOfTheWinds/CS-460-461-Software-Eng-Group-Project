package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import rx.Observable.OnSubscribe;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Action0;

public final class OnSubscribeTimerPeriodically implements OnSubscribe<Long> {
    final long initialDelay;
    final long period;
    final Scheduler scheduler;
    final TimeUnit unit;

    /* renamed from: rx.internal.operators.OnSubscribeTimerPeriodically.1 */
    class C13031 implements Action0 {
        long counter;
        final /* synthetic */ Subscriber val$child;
        final /* synthetic */ Worker val$worker;

        C13031(Subscriber subscriber, Worker worker) {
            this.val$child = subscriber;
            this.val$worker = worker;
        }

        public void call() {
            try {
                Subscriber subscriber = this.val$child;
                long j = this.counter;
                this.counter = 1 + j;
                subscriber.onNext(Long.valueOf(j));
            } catch (Throwable th) {
                this.val$worker.unsubscribe();
            } finally {
                Exceptions.throwOrReport(th, this.val$child);
            }
        }
    }

    public OnSubscribeTimerPeriodically(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        this.initialDelay = j;
        this.period = j2;
        this.unit = timeUnit;
        this.scheduler = scheduler;
    }

    public void call(Subscriber<? super Long> subscriber) {
        Worker createWorker = this.scheduler.createWorker();
        subscriber.add(createWorker);
        createWorker.schedulePeriodically(new C13031(subscriber, createWorker), this.initialDelay, this.period, this.unit);
    }
}

package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import rx.Observable.OnSubscribe;
import rx.Scheduler;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Action0;

public final class OnSubscribeTimerOnce implements OnSubscribe<Long> {
    final Scheduler scheduler;
    final long time;
    final TimeUnit unit;

    /* renamed from: rx.internal.operators.OnSubscribeTimerOnce.1 */
    class C13021 implements Action0 {
        final /* synthetic */ Subscriber val$child;

        C13021(Subscriber subscriber) {
            this.val$child = subscriber;
        }

        public void call() {
            try {
                this.val$child.onNext(Long.valueOf(0));
                this.val$child.onCompleted();
            } catch (Throwable th) {
                Exceptions.throwOrReport(th, this.val$child);
            }
        }
    }

    public OnSubscribeTimerOnce(long j, TimeUnit timeUnit, Scheduler scheduler) {
        this.time = j;
        this.unit = timeUnit;
        this.scheduler = scheduler;
    }

    public void call(Subscriber<? super Long> subscriber) {
        Object createWorker = this.scheduler.createWorker();
        subscriber.add(createWorker);
        createWorker.schedule(new C13021(subscriber), this.time, this.unit);
    }
}

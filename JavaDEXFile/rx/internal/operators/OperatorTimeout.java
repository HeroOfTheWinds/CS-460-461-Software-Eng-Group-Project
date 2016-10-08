package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;

public final class OperatorTimeout<T> extends OperatorTimeoutBase<T> {

    /* renamed from: rx.internal.operators.OperatorTimeout.1 */
    class C14081 implements FirstTimeoutStub<T> {
        final /* synthetic */ TimeUnit val$timeUnit;
        final /* synthetic */ long val$timeout;

        /* renamed from: rx.internal.operators.OperatorTimeout.1.1 */
        class C14071 implements Action0 {
            final /* synthetic */ Long val$seqId;
            final /* synthetic */ TimeoutSubscriber val$timeoutSubscriber;

            C14071(TimeoutSubscriber timeoutSubscriber, Long l) {
                this.val$timeoutSubscriber = timeoutSubscriber;
                this.val$seqId = l;
            }

            public void call() {
                this.val$timeoutSubscriber.onTimeout(this.val$seqId.longValue());
            }
        }

        C14081(long j, TimeUnit timeUnit) {
            this.val$timeout = j;
            this.val$timeUnit = timeUnit;
        }

        public Subscription call(TimeoutSubscriber<T> timeoutSubscriber, Long l, Worker worker) {
            return worker.schedule(new C14071(timeoutSubscriber, l), this.val$timeout, this.val$timeUnit);
        }
    }

    /* renamed from: rx.internal.operators.OperatorTimeout.2 */
    class C14102 implements TimeoutStub<T> {
        final /* synthetic */ TimeUnit val$timeUnit;
        final /* synthetic */ long val$timeout;

        /* renamed from: rx.internal.operators.OperatorTimeout.2.1 */
        class C14091 implements Action0 {
            final /* synthetic */ Long val$seqId;
            final /* synthetic */ TimeoutSubscriber val$timeoutSubscriber;

            C14091(TimeoutSubscriber timeoutSubscriber, Long l) {
                this.val$timeoutSubscriber = timeoutSubscriber;
                this.val$seqId = l;
            }

            public void call() {
                this.val$timeoutSubscriber.onTimeout(this.val$seqId.longValue());
            }
        }

        C14102(long j, TimeUnit timeUnit) {
            this.val$timeout = j;
            this.val$timeUnit = timeUnit;
        }

        public Subscription call(TimeoutSubscriber<T> timeoutSubscriber, Long l, T t, Worker worker) {
            return worker.schedule(new C14091(timeoutSubscriber, l), this.val$timeout, this.val$timeUnit);
        }
    }

    public OperatorTimeout(long j, TimeUnit timeUnit, Observable<? extends T> observable, Scheduler scheduler) {
        super(new C14081(j, timeUnit), new C14102(j, timeUnit), observable, scheduler);
    }

    public /* bridge */ /* synthetic */ Subscriber call(Subscriber subscriber) {
        return super.call(subscriber);
    }
}

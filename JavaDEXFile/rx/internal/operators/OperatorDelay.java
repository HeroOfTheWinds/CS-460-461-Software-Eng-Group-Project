package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import rx.Observable.Operator;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;

public final class OperatorDelay<T> implements Operator<T, T> {
    final long delay;
    final Scheduler scheduler;
    final TimeUnit unit;

    /* renamed from: rx.internal.operators.OperatorDelay.1 */
    class C13231 extends Subscriber<T> {
        boolean done;
        final /* synthetic */ Subscriber val$child;
        final /* synthetic */ Worker val$worker;

        /* renamed from: rx.internal.operators.OperatorDelay.1.1 */
        class C13201 implements Action0 {
            C13201() {
            }

            public void call() {
                if (!C13231.this.done) {
                    C13231.this.done = true;
                    C13231.this.val$child.onCompleted();
                }
            }
        }

        /* renamed from: rx.internal.operators.OperatorDelay.1.2 */
        class C13212 implements Action0 {
            final /* synthetic */ Throwable val$e;

            C13212(Throwable th) {
                this.val$e = th;
            }

            public void call() {
                if (!C13231.this.done) {
                    C13231.this.done = true;
                    C13231.this.val$child.onError(this.val$e);
                    C13231.this.val$worker.unsubscribe();
                }
            }
        }

        /* renamed from: rx.internal.operators.OperatorDelay.1.3 */
        class C13223 implements Action0 {
            final /* synthetic */ Object val$t;

            C13223(Object obj) {
                this.val$t = obj;
            }

            public void call() {
                if (!C13231.this.done) {
                    C13231.this.val$child.onNext(this.val$t);
                }
            }
        }

        C13231(Subscriber subscriber, Worker worker, Subscriber subscriber2) {
            this.val$worker = worker;
            this.val$child = subscriber2;
            super(subscriber);
        }

        public void onCompleted() {
            this.val$worker.schedule(new C13201(), OperatorDelay.this.delay, OperatorDelay.this.unit);
        }

        public void onError(Throwable th) {
            this.val$worker.schedule(new C13212(th));
        }

        public void onNext(T t) {
            this.val$worker.schedule(new C13223(t), OperatorDelay.this.delay, OperatorDelay.this.unit);
        }
    }

    public OperatorDelay(long j, TimeUnit timeUnit, Scheduler scheduler) {
        this.delay = j;
        this.unit = timeUnit;
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        Object createWorker = this.scheduler.createWorker();
        subscriber.add(createWorker);
        return new C13231(subscriber, createWorker, subscriber);
    }
}

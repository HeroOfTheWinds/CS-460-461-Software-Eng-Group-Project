package rx.internal.operators;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Producer;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;

public final class OperatorSubscribeOn<T> implements OnSubscribe<T> {
    final Scheduler scheduler;
    final Observable<T> source;

    /* renamed from: rx.internal.operators.OperatorSubscribeOn.1 */
    class C13921 implements Action0 {
        final /* synthetic */ Worker val$inner;
        final /* synthetic */ Subscriber val$subscriber;

        /* renamed from: rx.internal.operators.OperatorSubscribeOn.1.1 */
        class C13911 extends Subscriber<T> {
            final /* synthetic */ Thread val$t;

            /* renamed from: rx.internal.operators.OperatorSubscribeOn.1.1.1 */
            class C13901 implements Producer {
                final /* synthetic */ Producer val$p;

                /* renamed from: rx.internal.operators.OperatorSubscribeOn.1.1.1.1 */
                class C13891 implements Action0 {
                    final /* synthetic */ long val$n;

                    C13891(long j) {
                        this.val$n = j;
                    }

                    public void call() {
                        C13901.this.val$p.request(this.val$n);
                    }
                }

                C13901(Producer producer) {
                    this.val$p = producer;
                }

                public void request(long j) {
                    if (C13911.this.val$t == Thread.currentThread()) {
                        this.val$p.request(j);
                    } else {
                        C13921.this.val$inner.schedule(new C13891(j));
                    }
                }
            }

            C13911(Subscriber subscriber, Thread thread) {
                this.val$t = thread;
                super(subscriber);
            }

            public void onCompleted() {
                try {
                    C13921.this.val$subscriber.onCompleted();
                } finally {
                    C13921.this.val$inner.unsubscribe();
                }
            }

            public void onError(Throwable th) {
                try {
                    C13921.this.val$subscriber.onError(th);
                } finally {
                    C13921.this.val$inner.unsubscribe();
                }
            }

            public void onNext(T t) {
                C13921.this.val$subscriber.onNext(t);
            }

            public void setProducer(Producer producer) {
                C13921.this.val$subscriber.setProducer(new C13901(producer));
            }
        }

        C13921(Subscriber subscriber, Worker worker) {
            this.val$subscriber = subscriber;
            this.val$inner = worker;
        }

        public void call() {
            OperatorSubscribeOn.this.source.unsafeSubscribe(new C13911(this.val$subscriber, Thread.currentThread()));
        }
    }

    public OperatorSubscribeOn(Observable<T> observable, Scheduler scheduler) {
        this.scheduler = scheduler;
        this.source = observable;
    }

    public void call(Subscriber<? super T> subscriber) {
        Worker createWorker = this.scheduler.createWorker();
        subscriber.add(createWorker);
        createWorker.schedule(new C13921(subscriber, createWorker));
    }
}

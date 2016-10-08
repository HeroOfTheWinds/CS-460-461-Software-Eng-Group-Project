package rx.internal.operators;

import java.util.concurrent.atomic.AtomicInteger;
import rx.Observable;
import rx.Observable.Operator;
import rx.Producer;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func2;
import rx.internal.producers.ProducerArbiter;
import rx.schedulers.Schedulers;
import rx.subscriptions.SerialSubscription;

public final class OperatorRetryWithPredicate<T> implements Operator<T, Observable<T>> {
    final Func2<Integer, Throwable, Boolean> predicate;

    static final class SourceSubscriber<T> extends Subscriber<Observable<T>> {
        final AtomicInteger attempts;
        final Subscriber<? super T> child;
        final Worker inner;
        final ProducerArbiter pa;
        final Func2<Integer, Throwable, Boolean> predicate;
        final SerialSubscription serialSubscription;

        /* renamed from: rx.internal.operators.OperatorRetryWithPredicate.SourceSubscriber.1 */
        class C13701 implements Action0 {
            final /* synthetic */ Observable val$o;

            /* renamed from: rx.internal.operators.OperatorRetryWithPredicate.SourceSubscriber.1.1 */
            class C13691 extends Subscriber<T> {
                boolean done;
                final /* synthetic */ Action0 val$_self;

                C13691(Action0 action0) {
                    this.val$_self = action0;
                }

                public void onCompleted() {
                    if (!this.done) {
                        this.done = true;
                        SourceSubscriber.this.child.onCompleted();
                    }
                }

                public void onError(Throwable th) {
                    if (!this.done) {
                        this.done = true;
                        if (!((Boolean) SourceSubscriber.this.predicate.call(Integer.valueOf(SourceSubscriber.this.attempts.get()), th)).booleanValue() || SourceSubscriber.this.inner.isUnsubscribed()) {
                            SourceSubscriber.this.child.onError(th);
                        } else {
                            SourceSubscriber.this.inner.schedule(this.val$_self);
                        }
                    }
                }

                public void onNext(T t) {
                    if (!this.done) {
                        SourceSubscriber.this.child.onNext(t);
                        SourceSubscriber.this.pa.produced(1);
                    }
                }

                public void setProducer(Producer producer) {
                    SourceSubscriber.this.pa.setProducer(producer);
                }
            }

            C13701(Observable observable) {
                this.val$o = observable;
            }

            public void call() {
                SourceSubscriber.this.attempts.incrementAndGet();
                Object c13691 = new C13691(this);
                SourceSubscriber.this.serialSubscription.set(c13691);
                this.val$o.unsafeSubscribe(c13691);
            }
        }

        public SourceSubscriber(Subscriber<? super T> subscriber, Func2<Integer, Throwable, Boolean> func2, Worker worker, SerialSubscription serialSubscription, ProducerArbiter producerArbiter) {
            this.attempts = new AtomicInteger();
            this.child = subscriber;
            this.predicate = func2;
            this.inner = worker;
            this.serialSubscription = serialSubscription;
            this.pa = producerArbiter;
        }

        public void onCompleted() {
        }

        public void onError(Throwable th) {
            this.child.onError(th);
        }

        public void onNext(Observable<T> observable) {
            this.inner.schedule(new C13701(observable));
        }
    }

    public OperatorRetryWithPredicate(Func2<Integer, Throwable, Boolean> func2) {
        this.predicate = func2;
    }

    public Subscriber<? super Observable<T>> call(Subscriber<? super T> subscriber) {
        Object createWorker = Schedulers.trampoline().createWorker();
        subscriber.add(createWorker);
        Object serialSubscription = new SerialSubscription();
        subscriber.add(serialSubscription);
        Object producerArbiter = new ProducerArbiter();
        subscriber.setProducer(producerArbiter);
        return new SourceSubscriber(subscriber, this.predicate, createWorker, serialSubscription, producerArbiter);
    }
}

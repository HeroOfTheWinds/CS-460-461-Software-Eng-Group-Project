package rx.internal.operators;

import rx.Observable;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.internal.producers.ProducerArbiter;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.SerialSubscription;

public final class OperatorOnErrorResumeNextViaFunction<T> implements Operator<T, T> {
    final Func1<Throwable, ? extends Observable<? extends T>> resumeFunction;

    /* renamed from: rx.internal.operators.OperatorOnErrorResumeNextViaFunction.1 */
    static final class C13501 implements Func1<Throwable, Observable<? extends T>> {
        final /* synthetic */ Func1 val$resumeFunction;

        C13501(Func1 func1) {
            this.val$resumeFunction = func1;
        }

        public Observable<? extends T> call(Throwable th) {
            return Observable.just(this.val$resumeFunction.call(th));
        }
    }

    /* renamed from: rx.internal.operators.OperatorOnErrorResumeNextViaFunction.2 */
    static final class C13512 implements Func1<Throwable, Observable<? extends T>> {
        final /* synthetic */ Observable val$other;

        C13512(Observable observable) {
            this.val$other = observable;
        }

        public Observable<? extends T> call(Throwable th) {
            return this.val$other;
        }
    }

    /* renamed from: rx.internal.operators.OperatorOnErrorResumeNextViaFunction.3 */
    static final class C13523 implements Func1<Throwable, Observable<? extends T>> {
        final /* synthetic */ Observable val$other;

        C13523(Observable observable) {
            this.val$other = observable;
        }

        public Observable<? extends T> call(Throwable th) {
            return th instanceof Exception ? this.val$other : Observable.error(th);
        }
    }

    /* renamed from: rx.internal.operators.OperatorOnErrorResumeNextViaFunction.4 */
    class C13544 extends Subscriber<T> {
        private boolean done;
        long produced;
        final /* synthetic */ Subscriber val$child;
        final /* synthetic */ ProducerArbiter val$pa;
        final /* synthetic */ SerialSubscription val$ssub;

        /* renamed from: rx.internal.operators.OperatorOnErrorResumeNextViaFunction.4.1 */
        class C13531 extends Subscriber<T> {
            C13531() {
            }

            public void onCompleted() {
                C13544.this.val$child.onCompleted();
            }

            public void onError(Throwable th) {
                C13544.this.val$child.onError(th);
            }

            public void onNext(T t) {
                C13544.this.val$child.onNext(t);
            }

            public void setProducer(Producer producer) {
                C13544.this.val$pa.setProducer(producer);
            }
        }

        C13544(Subscriber subscriber, ProducerArbiter producerArbiter, SerialSubscription serialSubscription) {
            this.val$child = subscriber;
            this.val$pa = producerArbiter;
            this.val$ssub = serialSubscription;
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                this.val$child.onCompleted();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
                return;
            }
            this.done = true;
            try {
                unsubscribe();
                Object c13531 = new C13531();
                this.val$ssub.set(c13531);
                long j = this.produced;
                if (j != 0) {
                    this.val$pa.produced(j);
                }
                ((Observable) OperatorOnErrorResumeNextViaFunction.this.resumeFunction.call(th)).unsafeSubscribe(c13531);
            } catch (Throwable th2) {
                Exceptions.throwOrReport(th2, this.val$child);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                this.produced++;
                this.val$child.onNext(t);
            }
        }

        public void setProducer(Producer producer) {
            this.val$pa.setProducer(producer);
        }
    }

    public OperatorOnErrorResumeNextViaFunction(Func1<Throwable, ? extends Observable<? extends T>> func1) {
        this.resumeFunction = func1;
    }

    public static <T> OperatorOnErrorResumeNextViaFunction<T> withException(Observable<? extends T> observable) {
        return new OperatorOnErrorResumeNextViaFunction(new C13523(observable));
    }

    public static <T> OperatorOnErrorResumeNextViaFunction<T> withOther(Observable<? extends T> observable) {
        return new OperatorOnErrorResumeNextViaFunction(new C13512(observable));
    }

    public static <T> OperatorOnErrorResumeNextViaFunction<T> withSingle(Func1<Throwable, ? extends T> func1) {
        return new OperatorOnErrorResumeNextViaFunction(new C13501(func1));
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        Producer producerArbiter = new ProducerArbiter();
        Subscription serialSubscription = new SerialSubscription();
        Object c13544 = new C13544(subscriber, producerArbiter, serialSubscription);
        serialSubscription.set(c13544);
        subscriber.add(serialSubscription);
        subscriber.setProducer(producerArbiter);
        return c13544;
    }
}

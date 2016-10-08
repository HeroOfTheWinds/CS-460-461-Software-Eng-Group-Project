package rx.internal.util;

import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Producer;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.internal.producers.SingleProducer;
import rx.internal.schedulers.EventLoopsScheduler;
import rx.observers.Subscribers;

public final class ScalarSynchronousObservable<T> extends Observable<T> {
    static final boolean STRONG_MODE;
    final T f915t;

    /* renamed from: rx.internal.util.ScalarSynchronousObservable.1 */
    class C14501 implements OnSubscribe<T> {
        final /* synthetic */ Object val$t;

        C14501(Object obj) {
            this.val$t = obj;
        }

        public void call(Subscriber<? super T> subscriber) {
            subscriber.setProducer(ScalarSynchronousObservable.createProducer(subscriber, this.val$t));
        }
    }

    /* renamed from: rx.internal.util.ScalarSynchronousObservable.2 */
    class C14512 implements Func1<Action0, Subscription> {
        final /* synthetic */ EventLoopsScheduler val$els;

        C14512(EventLoopsScheduler eventLoopsScheduler) {
            this.val$els = eventLoopsScheduler;
        }

        public Subscription call(Action0 action0) {
            return this.val$els.scheduleDirect(action0);
        }
    }

    /* renamed from: rx.internal.util.ScalarSynchronousObservable.3 */
    class C14533 implements Func1<Action0, Subscription> {
        final /* synthetic */ Scheduler val$scheduler;

        /* renamed from: rx.internal.util.ScalarSynchronousObservable.3.1 */
        class C14521 implements Action0 {
            final /* synthetic */ Action0 val$a;
            final /* synthetic */ Worker val$w;

            C14521(Action0 action0, Worker worker) {
                this.val$a = action0;
                this.val$w = worker;
            }

            public void call() {
                try {
                    this.val$a.call();
                } finally {
                    this.val$w.unsubscribe();
                }
            }
        }

        C14533(Scheduler scheduler) {
            this.val$scheduler = scheduler;
        }

        public Subscription call(Action0 action0) {
            Subscription createWorker = this.val$scheduler.createWorker();
            createWorker.schedule(new C14521(action0, createWorker));
            return createWorker;
        }
    }

    /* renamed from: rx.internal.util.ScalarSynchronousObservable.4 */
    class C14544 implements OnSubscribe<R> {
        final /* synthetic */ Func1 val$func;

        C14544(Func1 func1) {
            this.val$func = func1;
        }

        public void call(Subscriber<? super R> subscriber) {
            Observable observable = (Observable) this.val$func.call(ScalarSynchronousObservable.this.f915t);
            if (observable instanceof ScalarSynchronousObservable) {
                subscriber.setProducer(ScalarSynchronousObservable.createProducer(subscriber, ((ScalarSynchronousObservable) observable).f915t));
            } else {
                observable.unsafeSubscribe(Subscribers.wrap(subscriber));
            }
        }
    }

    static final class ScalarAsyncOnSubscribe<T> implements OnSubscribe<T> {
        final Func1<Action0, Subscription> onSchedule;
        final T value;

        ScalarAsyncOnSubscribe(T t, Func1<Action0, Subscription> func1) {
            this.value = t;
            this.onSchedule = func1;
        }

        public void call(Subscriber<? super T> subscriber) {
            subscriber.setProducer(new ScalarAsyncProducer(subscriber, this.value, this.onSchedule));
        }
    }

    static final class ScalarAsyncProducer<T> extends AtomicBoolean implements Producer, Action0 {
        private static final long serialVersionUID = -2466317989629281651L;
        final Subscriber<? super T> actual;
        final Func1<Action0, Subscription> onSchedule;
        final T value;

        public ScalarAsyncProducer(Subscriber<? super T> subscriber, T t, Func1<Action0, Subscription> func1) {
            this.actual = subscriber;
            this.value = t;
            this.onSchedule = func1;
        }

        public void call() {
            Observer observer = this.actual;
            if (!observer.isUnsubscribed()) {
                Object obj = this.value;
                try {
                    observer.onNext(obj);
                    if (!observer.isUnsubscribed()) {
                        observer.onCompleted();
                    }
                } catch (Throwable th) {
                    Exceptions.throwOrReport(th, observer, obj);
                }
            }
        }

        public void request(long j) {
            if (j < 0) {
                throw new IllegalArgumentException("n >= 0 required but it was " + j);
            } else if (j != 0 && compareAndSet(false, true)) {
                this.actual.add((Subscription) this.onSchedule.call(this));
            }
        }

        public String toString() {
            return "ScalarAsyncProducer[" + this.value + ", " + get() + "]";
        }
    }

    static final class WeakSingleProducer<T> implements Producer {
        final Subscriber<? super T> actual;
        boolean once;
        final T value;

        public WeakSingleProducer(Subscriber<? super T> subscriber, T t) {
            this.actual = subscriber;
            this.value = t;
        }

        public void request(long j) {
            if (!this.once) {
                if (j < 0) {
                    throw new IllegalStateException("n >= required but it was " + j);
                } else if (j != 0) {
                    this.once = true;
                    Observer observer = this.actual;
                    if (!observer.isUnsubscribed()) {
                        Object obj = this.value;
                        try {
                            observer.onNext(obj);
                            if (!observer.isUnsubscribed()) {
                                observer.onCompleted();
                            }
                        } catch (Throwable th) {
                            Exceptions.throwOrReport(th, observer, obj);
                        }
                    }
                }
            }
        }
    }

    static {
        STRONG_MODE = Boolean.valueOf(System.getProperty("rx.just.strong-mode", "false")).booleanValue();
    }

    protected ScalarSynchronousObservable(T t) {
        super(new C14501(t));
        this.f915t = t;
    }

    public static <T> ScalarSynchronousObservable<T> create(T t) {
        return new ScalarSynchronousObservable(t);
    }

    static <T> Producer createProducer(Subscriber<? super T> subscriber, T t) {
        return STRONG_MODE ? new SingleProducer(subscriber, t) : new WeakSingleProducer(subscriber, t);
    }

    public T get() {
        return this.f915t;
    }

    public <R> Observable<R> scalarFlatMap(Func1<? super T, ? extends Observable<? extends R>> func1) {
        return Observable.create(new C14544(func1));
    }

    public Observable<T> scalarScheduleOn(Scheduler scheduler) {
        return Observable.create(new ScalarAsyncOnSubscribe(this.f915t, scheduler instanceof EventLoopsScheduler ? new C14512((EventLoopsScheduler) scheduler) : new C14533(scheduler)));
    }
}

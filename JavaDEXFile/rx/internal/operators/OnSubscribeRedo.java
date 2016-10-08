package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import rx.Notification;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observable.Operator;
import rx.Producer;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.internal.producers.ProducerArbiter;
import rx.observers.Subscribers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.SerialSubscription;

public final class OnSubscribeRedo<T> implements OnSubscribe<T> {
    static final Func1<Observable<? extends Notification<?>>, Observable<?>> REDO_INFINITE;
    private final Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> controlHandlerFunction;
    private final Scheduler scheduler;
    final Observable<T> source;
    final boolean stopOnComplete;
    final boolean stopOnError;

    /* renamed from: rx.internal.operators.OnSubscribeRedo.1 */
    static final class C12881 implements Func1<Observable<? extends Notification<?>>, Observable<?>> {

        /* renamed from: rx.internal.operators.OnSubscribeRedo.1.1 */
        class C12871 implements Func1<Notification<?>, Notification<?>> {
            C12871() {
            }

            public Notification<?> call(Notification<?> notification) {
                return Notification.createOnNext(null);
            }
        }

        C12881() {
        }

        public Observable<?> call(Observable<? extends Notification<?>> observable) {
            return observable.map(new C12871());
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeRedo.2 */
    class C12902 implements Action0 {
        final /* synthetic */ ProducerArbiter val$arbiter;
        final /* synthetic */ Subscriber val$child;
        final /* synthetic */ AtomicLong val$consumerCapacity;
        final /* synthetic */ SerialSubscription val$sourceSubscriptions;
        final /* synthetic */ BehaviorSubject val$terminals;

        /* renamed from: rx.internal.operators.OnSubscribeRedo.2.1 */
        class C12891 extends Subscriber<T> {
            boolean done;

            C12891() {
            }

            private void decrementConsumerCapacity() {
                long j;
                do {
                    j = C12902.this.val$consumerCapacity.get();
                    if (j == Long.MAX_VALUE) {
                        return;
                    }
                } while (!C12902.this.val$consumerCapacity.compareAndSet(j, j - 1));
            }

            public void onCompleted() {
                if (!this.done) {
                    this.done = true;
                    unsubscribe();
                    C12902.this.val$terminals.onNext(Notification.createOnCompleted());
                }
            }

            public void onError(Throwable th) {
                if (!this.done) {
                    this.done = true;
                    unsubscribe();
                    C12902.this.val$terminals.onNext(Notification.createOnError(th));
                }
            }

            public void onNext(T t) {
                if (!this.done) {
                    C12902.this.val$child.onNext(t);
                    decrementConsumerCapacity();
                    C12902.this.val$arbiter.produced(1);
                }
            }

            public void setProducer(Producer producer) {
                C12902.this.val$arbiter.setProducer(producer);
            }
        }

        C12902(Subscriber subscriber, BehaviorSubject behaviorSubject, ProducerArbiter producerArbiter, AtomicLong atomicLong, SerialSubscription serialSubscription) {
            this.val$child = subscriber;
            this.val$terminals = behaviorSubject;
            this.val$arbiter = producerArbiter;
            this.val$consumerCapacity = atomicLong;
            this.val$sourceSubscriptions = serialSubscription;
        }

        public void call() {
            if (!this.val$child.isUnsubscribed()) {
                Object c12891 = new C12891();
                this.val$sourceSubscriptions.set(c12891);
                OnSubscribeRedo.this.source.unsafeSubscribe(c12891);
            }
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeRedo.3 */
    class C12923 implements Operator<Notification<?>, Notification<?>> {

        /* renamed from: rx.internal.operators.OnSubscribeRedo.3.1 */
        class C12911 extends Subscriber<Notification<?>> {
            final /* synthetic */ Subscriber val$filteredTerminals;

            C12911(Subscriber subscriber, Subscriber subscriber2) {
                this.val$filteredTerminals = subscriber2;
                super(subscriber);
            }

            public void onCompleted() {
                this.val$filteredTerminals.onCompleted();
            }

            public void onError(Throwable th) {
                this.val$filteredTerminals.onError(th);
            }

            public void onNext(Notification<?> notification) {
                if (notification.isOnCompleted() && OnSubscribeRedo.this.stopOnComplete) {
                    this.val$filteredTerminals.onCompleted();
                } else if (notification.isOnError() && OnSubscribeRedo.this.stopOnError) {
                    this.val$filteredTerminals.onError(notification.getThrowable());
                } else {
                    this.val$filteredTerminals.onNext(notification);
                }
            }

            public void setProducer(Producer producer) {
                producer.request(Long.MAX_VALUE);
            }
        }

        C12923() {
        }

        public Subscriber<? super Notification<?>> call(Subscriber<? super Notification<?>> subscriber) {
            return new C12911(subscriber, subscriber);
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeRedo.4 */
    class C12944 implements Action0 {
        final /* synthetic */ Subscriber val$child;
        final /* synthetic */ AtomicLong val$consumerCapacity;
        final /* synthetic */ Observable val$restarts;
        final /* synthetic */ AtomicBoolean val$resumeBoundary;
        final /* synthetic */ Action0 val$subscribeToSource;
        final /* synthetic */ Worker val$worker;

        /* renamed from: rx.internal.operators.OnSubscribeRedo.4.1 */
        class C12931 extends Subscriber<Object> {
            C12931(Subscriber subscriber) {
                super(subscriber);
            }

            public void onCompleted() {
                C12944.this.val$child.onCompleted();
            }

            public void onError(Throwable th) {
                C12944.this.val$child.onError(th);
            }

            public void onNext(Object obj) {
                if (!C12944.this.val$child.isUnsubscribed()) {
                    if (C12944.this.val$consumerCapacity.get() > 0) {
                        C12944.this.val$worker.schedule(C12944.this.val$subscribeToSource);
                    } else {
                        C12944.this.val$resumeBoundary.compareAndSet(false, true);
                    }
                }
            }

            public void setProducer(Producer producer) {
                producer.request(Long.MAX_VALUE);
            }
        }

        C12944(Observable observable, Subscriber subscriber, AtomicLong atomicLong, Worker worker, Action0 action0, AtomicBoolean atomicBoolean) {
            this.val$restarts = observable;
            this.val$child = subscriber;
            this.val$consumerCapacity = atomicLong;
            this.val$worker = worker;
            this.val$subscribeToSource = action0;
            this.val$resumeBoundary = atomicBoolean;
        }

        public void call() {
            this.val$restarts.unsafeSubscribe(new C12931(this.val$child));
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeRedo.5 */
    class C12955 implements Producer {
        final /* synthetic */ ProducerArbiter val$arbiter;
        final /* synthetic */ AtomicLong val$consumerCapacity;
        final /* synthetic */ AtomicBoolean val$resumeBoundary;
        final /* synthetic */ Action0 val$subscribeToSource;
        final /* synthetic */ Worker val$worker;

        C12955(AtomicLong atomicLong, ProducerArbiter producerArbiter, AtomicBoolean atomicBoolean, Worker worker, Action0 action0) {
            this.val$consumerCapacity = atomicLong;
            this.val$arbiter = producerArbiter;
            this.val$resumeBoundary = atomicBoolean;
            this.val$worker = worker;
            this.val$subscribeToSource = action0;
        }

        public void request(long j) {
            if (j > 0) {
                BackpressureUtils.getAndAddRequest(this.val$consumerCapacity, j);
                this.val$arbiter.request(j);
                if (this.val$resumeBoundary.compareAndSet(true, false)) {
                    this.val$worker.schedule(this.val$subscribeToSource);
                }
            }
        }
    }

    public static final class RedoFinite implements Func1<Observable<? extends Notification<?>>, Observable<?>> {
        final long count;

        /* renamed from: rx.internal.operators.OnSubscribeRedo.RedoFinite.1 */
        class C12961 implements Func1<Notification<?>, Notification<?>> {
            int num;

            C12961() {
                this.num = 0;
            }

            public Notification<?> call(Notification<?> notification) {
                if (RedoFinite.this.count == 0) {
                    return notification;
                }
                this.num++;
                return ((long) this.num) <= RedoFinite.this.count ? Notification.createOnNext(Integer.valueOf(this.num)) : notification;
            }
        }

        public RedoFinite(long j) {
            this.count = j;
        }

        public Observable<?> call(Observable<? extends Notification<?>> observable) {
            return observable.map(new C12961()).dematerialize();
        }
    }

    public static final class RetryWithPredicate implements Func1<Observable<? extends Notification<?>>, Observable<? extends Notification<?>>> {
        final Func2<Integer, Throwable, Boolean> predicate;

        /* renamed from: rx.internal.operators.OnSubscribeRedo.RetryWithPredicate.1 */
        class C12971 implements Func2<Notification<Integer>, Notification<?>, Notification<Integer>> {
            C12971() {
            }

            public Notification<Integer> call(Notification<Integer> notification, Notification<?> notification2) {
                int intValue = ((Integer) notification.getValue()).intValue();
                return ((Boolean) RetryWithPredicate.this.predicate.call(Integer.valueOf(intValue), notification2.getThrowable())).booleanValue() ? Notification.createOnNext(Integer.valueOf(intValue + 1)) : notification2;
            }
        }

        public RetryWithPredicate(Func2<Integer, Throwable, Boolean> func2) {
            this.predicate = func2;
        }

        public Observable<? extends Notification<?>> call(Observable<? extends Notification<?>> observable) {
            return observable.scan(Notification.createOnNext(Integer.valueOf(0)), new C12971());
        }
    }

    static {
        REDO_INFINITE = new C12881();
    }

    private OnSubscribeRedo(Observable<T> observable, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> func1, boolean z, boolean z2, Scheduler scheduler) {
        this.source = observable;
        this.controlHandlerFunction = func1;
        this.stopOnComplete = z;
        this.stopOnError = z2;
        this.scheduler = scheduler;
    }

    public static <T> Observable<T> redo(Observable<T> observable, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> func1, Scheduler scheduler) {
        return Observable.create(new OnSubscribeRedo(observable, func1, false, false, scheduler));
    }

    public static <T> Observable<T> repeat(Observable<T> observable) {
        return repeat((Observable) observable, Schedulers.trampoline());
    }

    public static <T> Observable<T> repeat(Observable<T> observable, long j) {
        return repeat((Observable) observable, j, Schedulers.trampoline());
    }

    public static <T> Observable<T> repeat(Observable<T> observable, long j, Scheduler scheduler) {
        if (j == 0) {
            return Observable.empty();
        }
        if (j >= 0) {
            return repeat((Observable) observable, new RedoFinite(j - 1), scheduler);
        }
        throw new IllegalArgumentException("count >= 0 expected");
    }

    public static <T> Observable<T> repeat(Observable<T> observable, Scheduler scheduler) {
        return repeat((Observable) observable, REDO_INFINITE, scheduler);
    }

    public static <T> Observable<T> repeat(Observable<T> observable, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> func1) {
        return Observable.create(new OnSubscribeRedo(observable, func1, false, true, Schedulers.trampoline()));
    }

    public static <T> Observable<T> repeat(Observable<T> observable, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> func1, Scheduler scheduler) {
        return Observable.create(new OnSubscribeRedo(observable, func1, false, true, scheduler));
    }

    public static <T> Observable<T> retry(Observable<T> observable) {
        return retry((Observable) observable, REDO_INFINITE);
    }

    public static <T> Observable<T> retry(Observable<T> observable, long j) {
        if (j >= 0) {
            return j == 0 ? observable : retry((Observable) observable, new RedoFinite(j));
        } else {
            throw new IllegalArgumentException("count >= 0 expected");
        }
    }

    public static <T> Observable<T> retry(Observable<T> observable, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> func1) {
        return Observable.create(new OnSubscribeRedo(observable, func1, true, false, Schedulers.trampoline()));
    }

    public static <T> Observable<T> retry(Observable<T> observable, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> func1, Scheduler scheduler) {
        return Observable.create(new OnSubscribeRedo(observable, func1, true, false, scheduler));
    }

    public void call(Subscriber<? super T> subscriber) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        AtomicLong atomicLong = new AtomicLong();
        Worker createWorker = this.scheduler.createWorker();
        subscriber.add(createWorker);
        Object serialSubscription = new SerialSubscription();
        subscriber.add(serialSubscription);
        BehaviorSubject create = BehaviorSubject.create();
        create.subscribe(Subscribers.empty());
        ProducerArbiter producerArbiter = new ProducerArbiter();
        C12902 c12902 = new C12902(subscriber, create, producerArbiter, atomicLong, serialSubscription);
        createWorker.schedule(new C12944((Observable) this.controlHandlerFunction.call(create.lift(new C12923())), subscriber, atomicLong, createWorker, c12902, atomicBoolean));
        subscriber.setProducer(new C12955(atomicLong, producerArbiter, atomicBoolean, createWorker, c12902));
    }
}

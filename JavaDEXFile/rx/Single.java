package rx;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import rx.Completable.CompletableSubscriber;
import rx.Observable.Operator;
import rx.Scheduler.Worker;
import rx.annotations.Beta;
import rx.annotations.Experimental;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.functions.Func4;
import rx.functions.Func5;
import rx.functions.Func6;
import rx.functions.Func7;
import rx.functions.Func8;
import rx.functions.Func9;
import rx.functions.FuncN;
import rx.internal.operators.OnSubscribeToObservableFuture;
import rx.internal.operators.OperatorDelay;
import rx.internal.operators.OperatorDoAfterTerminate;
import rx.internal.operators.OperatorDoOnEach;
import rx.internal.operators.OperatorDoOnSubscribe;
import rx.internal.operators.OperatorDoOnUnsubscribe;
import rx.internal.operators.OperatorMap;
import rx.internal.operators.OperatorObserveOn;
import rx.internal.operators.OperatorOnErrorResumeNextViaFunction;
import rx.internal.operators.OperatorTimeout;
import rx.internal.operators.SingleOnSubscribeDelaySubscriptionOther;
import rx.internal.operators.SingleOnSubscribeUsing;
import rx.internal.operators.SingleOperatorOnErrorResumeNext;
import rx.internal.operators.SingleOperatorZip;
import rx.internal.producers.SingleDelayedProducer;
import rx.internal.util.ScalarSynchronousSingle;
import rx.internal.util.UtilityFunctions;
import rx.observers.SafeSubscriber;
import rx.observers.SerializedSubscriber;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSingleExecutionHook;
import rx.schedulers.Schedulers;
import rx.singles.BlockingSingle;

@Beta
public class Single<T> {
    static RxJavaSingleExecutionHook hook;
    final rx.Observable.OnSubscribe<T> onSubscribe;

    public interface OnSubscribe<T> extends Action1<SingleSubscriber<? super T>> {
    }

    /* renamed from: rx.Single.10 */
    static final class AnonymousClass10 implements FuncN<R> {
        final /* synthetic */ Func6 val$zipFunction;

        AnonymousClass10(Func6 func6) {
            this.val$zipFunction = func6;
        }

        public R call(Object... objArr) {
            return this.val$zipFunction.call(objArr[0], objArr[1], objArr[2], objArr[3], objArr[4], objArr[5]);
        }
    }

    /* renamed from: rx.Single.11 */
    static final class AnonymousClass11 implements FuncN<R> {
        final /* synthetic */ Func7 val$zipFunction;

        AnonymousClass11(Func7 func7) {
            this.val$zipFunction = func7;
        }

        public R call(Object... objArr) {
            return this.val$zipFunction.call(objArr[0], objArr[1], objArr[2], objArr[3], objArr[4], objArr[5], objArr[6]);
        }
    }

    /* renamed from: rx.Single.12 */
    static final class AnonymousClass12 implements FuncN<R> {
        final /* synthetic */ Func8 val$zipFunction;

        AnonymousClass12(Func8 func8) {
            this.val$zipFunction = func8;
        }

        public R call(Object... objArr) {
            return this.val$zipFunction.call(objArr[0], objArr[1], objArr[2], objArr[3], objArr[4], objArr[5], objArr[6], objArr[7]);
        }
    }

    /* renamed from: rx.Single.13 */
    static final class AnonymousClass13 implements FuncN<R> {
        final /* synthetic */ Func9 val$zipFunction;

        AnonymousClass13(Func9 func9) {
            this.val$zipFunction = func9;
        }

        public R call(Object... objArr) {
            return this.val$zipFunction.call(objArr[0], objArr[1], objArr[2], objArr[3], objArr[4], objArr[5], objArr[6], objArr[7], objArr[8]);
        }
    }

    /* renamed from: rx.Single.15 */
    class AnonymousClass15 extends Subscriber<T> {
        final /* synthetic */ Action1 val$onSuccess;

        AnonymousClass15(Action1 action1) {
            this.val$onSuccess = action1;
        }

        public final void onCompleted() {
        }

        public final void onError(Throwable th) {
            throw new OnErrorNotImplementedException(th);
        }

        public final void onNext(T t) {
            this.val$onSuccess.call(t);
        }
    }

    /* renamed from: rx.Single.16 */
    class AnonymousClass16 extends Subscriber<T> {
        final /* synthetic */ Action1 val$onError;
        final /* synthetic */ Action1 val$onSuccess;

        AnonymousClass16(Action1 action1, Action1 action12) {
            this.val$onError = action1;
            this.val$onSuccess = action12;
        }

        public final void onCompleted() {
        }

        public final void onError(Throwable th) {
            this.val$onError.call(th);
        }

        public final void onNext(T t) {
            this.val$onSuccess.call(t);
        }
    }

    /* renamed from: rx.Single.17 */
    class AnonymousClass17 extends SingleSubscriber<T> {
        final /* synthetic */ Observer val$observer;

        AnonymousClass17(Observer observer) {
            this.val$observer = observer;
        }

        public void onError(Throwable th) {
            this.val$observer.onError(th);
        }

        public void onSuccess(T t) {
            this.val$observer.onNext(t);
            this.val$observer.onCompleted();
        }
    }

    /* renamed from: rx.Single.18 */
    class AnonymousClass18 extends Subscriber<T> {
        final /* synthetic */ SingleSubscriber val$te;

        AnonymousClass18(SingleSubscriber singleSubscriber) {
            this.val$te = singleSubscriber;
        }

        public void onCompleted() {
        }

        public void onError(Throwable th) {
            this.val$te.onError(th);
        }

        public void onNext(T t) {
            this.val$te.onSuccess(t);
        }
    }

    /* renamed from: rx.Single.19 */
    class AnonymousClass19 implements OnSubscribe<T> {
        final /* synthetic */ Scheduler val$scheduler;

        /* renamed from: rx.Single.19.1 */
        class C12231 implements Action0 {
            final /* synthetic */ SingleSubscriber val$t;
            final /* synthetic */ Worker val$w;

            /* renamed from: rx.Single.19.1.1 */
            class C12221 extends SingleSubscriber<T> {
                C12221() {
                }

                public void onError(Throwable th) {
                    try {
                        C12231.this.val$t.onError(th);
                    } finally {
                        C12231.this.val$w.unsubscribe();
                    }
                }

                public void onSuccess(T t) {
                    try {
                        C12231.this.val$t.onSuccess(t);
                    } finally {
                        C12231.this.val$w.unsubscribe();
                    }
                }
            }

            C12231(SingleSubscriber singleSubscriber, Worker worker) {
                this.val$t = singleSubscriber;
                this.val$w = worker;
            }

            public void call() {
                SingleSubscriber c12221 = new C12221();
                this.val$t.add(c12221);
                Single.this.subscribe(c12221);
            }
        }

        AnonymousClass19(Scheduler scheduler) {
            this.val$scheduler = scheduler;
        }

        public void call(SingleSubscriber<? super T> singleSubscriber) {
            Worker createWorker = this.val$scheduler.createWorker();
            singleSubscriber.add(createWorker);
            createWorker.schedule(new C12231(singleSubscriber, createWorker));
        }
    }

    /* renamed from: rx.Single.1 */
    class C12241 implements rx.Observable.OnSubscribe<T> {
        final /* synthetic */ OnSubscribe val$f;

        /* renamed from: rx.Single.1.1 */
        class C12211 extends SingleSubscriber<T> {
            final /* synthetic */ Subscriber val$child;
            final /* synthetic */ SingleDelayedProducer val$producer;

            C12211(SingleDelayedProducer singleDelayedProducer, Subscriber subscriber) {
                this.val$producer = singleDelayedProducer;
                this.val$child = subscriber;
            }

            public void onError(Throwable th) {
                this.val$child.onError(th);
            }

            public void onSuccess(T t) {
                this.val$producer.setValue(t);
            }
        }

        C12241(OnSubscribe onSubscribe) {
            this.val$f = onSubscribe;
        }

        public void call(Subscriber<? super T> subscriber) {
            Object singleDelayedProducer = new SingleDelayedProducer(subscriber);
            subscriber.setProducer(singleDelayedProducer);
            Subscription c12211 = new C12211(singleDelayedProducer, subscriber);
            subscriber.add(c12211);
            this.val$f.call(c12211);
        }
    }

    /* renamed from: rx.Single.20 */
    class AnonymousClass20 implements Operator<T, T> {
        final /* synthetic */ Completable val$other;

        /* renamed from: rx.Single.20.1 */
        class C12251 extends Subscriber<T> {
            final /* synthetic */ Subscriber val$serial;

            C12251(Subscriber subscriber, boolean z, Subscriber subscriber2) {
                this.val$serial = subscriber2;
                super(subscriber, z);
            }

            public void onCompleted() {
                try {
                    this.val$serial.onCompleted();
                } finally {
                    this.val$serial.unsubscribe();
                }
            }

            public void onError(Throwable th) {
                try {
                    this.val$serial.onError(th);
                } finally {
                    this.val$serial.unsubscribe();
                }
            }

            public void onNext(T t) {
                this.val$serial.onNext(t);
            }
        }

        /* renamed from: rx.Single.20.2 */
        class C12262 implements CompletableSubscriber {
            final /* synthetic */ Subscriber val$main;
            final /* synthetic */ Subscriber val$serial;

            C12262(Subscriber subscriber, Subscriber subscriber2) {
                this.val$main = subscriber;
                this.val$serial = subscriber2;
            }

            public void onCompleted() {
                onError(new CancellationException("Stream was canceled before emitting a terminal event."));
            }

            public void onError(Throwable th) {
                this.val$main.onError(th);
            }

            public void onSubscribe(Subscription subscription) {
                this.val$serial.add(subscription);
            }
        }

        AnonymousClass20(Completable completable) {
            this.val$other = completable;
        }

        public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
            Subscription serializedSubscriber = new SerializedSubscriber(subscriber, false);
            Object c12251 = new C12251(serializedSubscriber, false, serializedSubscriber);
            CompletableSubscriber c12262 = new C12262(c12251, serializedSubscriber);
            serializedSubscriber.add(c12251);
            subscriber.add(serializedSubscriber);
            this.val$other.subscribe(c12262);
            return c12251;
        }
    }

    /* renamed from: rx.Single.21 */
    class AnonymousClass21 implements Operator<T, T> {
        final /* synthetic */ Observable val$other;

        /* renamed from: rx.Single.21.1 */
        class C12271 extends Subscriber<T> {
            final /* synthetic */ Subscriber val$serial;

            C12271(Subscriber subscriber, boolean z, Subscriber subscriber2) {
                this.val$serial = subscriber2;
                super(subscriber, z);
            }

            public void onCompleted() {
                try {
                    this.val$serial.onCompleted();
                } finally {
                    this.val$serial.unsubscribe();
                }
            }

            public void onError(Throwable th) {
                try {
                    this.val$serial.onError(th);
                } finally {
                    this.val$serial.unsubscribe();
                }
            }

            public void onNext(T t) {
                this.val$serial.onNext(t);
            }
        }

        /* renamed from: rx.Single.21.2 */
        class C12282 extends Subscriber<E> {
            final /* synthetic */ Subscriber val$main;

            C12282(Subscriber subscriber) {
                this.val$main = subscriber;
            }

            public void onCompleted() {
                onError(new CancellationException("Stream was canceled before emitting a terminal event."));
            }

            public void onError(Throwable th) {
                this.val$main.onError(th);
            }

            public void onNext(E e) {
                onError(new CancellationException("Stream was canceled before emitting a terminal event."));
            }
        }

        AnonymousClass21(Observable observable) {
            this.val$other = observable;
        }

        public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
            Subscription serializedSubscriber = new SerializedSubscriber(subscriber, false);
            Object c12271 = new C12271(serializedSubscriber, false, serializedSubscriber);
            Object c12282 = new C12282(c12271);
            serializedSubscriber.add(c12271);
            serializedSubscriber.add(c12282);
            subscriber.add(serializedSubscriber);
            this.val$other.unsafeSubscribe(c12282);
            return c12271;
        }
    }

    /* renamed from: rx.Single.23 */
    class AnonymousClass23 implements Observer<T> {
        final /* synthetic */ Action1 val$onError;

        AnonymousClass23(Action1 action1) {
            this.val$onError = action1;
        }

        public void onCompleted() {
        }

        public void onError(Throwable th) {
            this.val$onError.call(th);
        }

        public void onNext(T t) {
        }
    }

    /* renamed from: rx.Single.24 */
    class AnonymousClass24 implements Observer<T> {
        final /* synthetic */ Action1 val$onSuccess;

        AnonymousClass24(Action1 action1) {
            this.val$onSuccess = action1;
        }

        public void onCompleted() {
        }

        public void onError(Throwable th) {
        }

        public void onNext(T t) {
            this.val$onSuccess.call(t);
        }
    }

    /* renamed from: rx.Single.25 */
    static final class AnonymousClass25 implements OnSubscribe<T> {
        final /* synthetic */ Callable val$singleFactory;

        AnonymousClass25(Callable callable) {
            this.val$singleFactory = callable;
        }

        public void call(SingleSubscriber<? super T> singleSubscriber) {
            try {
                ((Single) this.val$singleFactory.call()).subscribe((SingleSubscriber) singleSubscriber);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                singleSubscriber.onError(th);
            }
        }
    }

    /* renamed from: rx.Single.2 */
    class C12312 implements rx.Observable.OnSubscribe<R> {
        final /* synthetic */ Operator val$lift;

        C12312(Operator operator) {
            this.val$lift = operator;
        }

        public void call(Subscriber<? super R> subscriber) {
            Observer observer;
            try {
                observer = (Subscriber) Single.hook.onLift(this.val$lift).call(subscriber);
                observer.onStart();
                Single.this.onSubscribe.call(observer);
            } catch (Throwable th) {
                Exceptions.throwOrReport(th, (Observer) subscriber);
            }
        }
    }

    /* renamed from: rx.Single.3 */
    static final class C12323 implements OnSubscribe<T> {
        final /* synthetic */ Throwable val$exception;

        C12323(Throwable th) {
            this.val$exception = th;
        }

        public void call(SingleSubscriber<? super T> singleSubscriber) {
            singleSubscriber.onError(this.val$exception);
        }
    }

    /* renamed from: rx.Single.4 */
    static final class C12334 implements OnSubscribe<T> {
        final /* synthetic */ Callable val$func;

        C12334(Callable callable) {
            this.val$func = callable;
        }

        public void call(SingleSubscriber<? super T> singleSubscriber) {
            try {
                singleSubscriber.onSuccess(this.val$func.call());
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                singleSubscriber.onError(th);
            }
        }
    }

    /* renamed from: rx.Single.5 */
    static final class C12355 implements OnSubscribe<T> {
        final /* synthetic */ Single val$source;

        /* renamed from: rx.Single.5.1 */
        class C12341 extends SingleSubscriber<Single<? extends T>> {
            final /* synthetic */ SingleSubscriber val$child;

            C12341(SingleSubscriber singleSubscriber) {
                this.val$child = singleSubscriber;
            }

            public void onError(Throwable th) {
                this.val$child.onError(th);
            }

            public void onSuccess(Single<? extends T> single) {
                single.subscribe(this.val$child);
            }
        }

        C12355(Single single) {
            this.val$source = single;
        }

        public void call(SingleSubscriber<? super T> singleSubscriber) {
            this.val$source.subscribe(new C12341(singleSubscriber));
        }
    }

    /* renamed from: rx.Single.6 */
    static final class C12366 implements FuncN<R> {
        final /* synthetic */ Func2 val$zipFunction;

        C12366(Func2 func2) {
            this.val$zipFunction = func2;
        }

        public R call(Object... objArr) {
            return this.val$zipFunction.call(objArr[0], objArr[1]);
        }
    }

    /* renamed from: rx.Single.7 */
    static final class C12377 implements FuncN<R> {
        final /* synthetic */ Func3 val$zipFunction;

        C12377(Func3 func3) {
            this.val$zipFunction = func3;
        }

        public R call(Object... objArr) {
            return this.val$zipFunction.call(objArr[0], objArr[1], objArr[2]);
        }
    }

    /* renamed from: rx.Single.8 */
    static final class C12388 implements FuncN<R> {
        final /* synthetic */ Func4 val$zipFunction;

        C12388(Func4 func4) {
            this.val$zipFunction = func4;
        }

        public R call(Object... objArr) {
            return this.val$zipFunction.call(objArr[0], objArr[1], objArr[2], objArr[3]);
        }
    }

    /* renamed from: rx.Single.9 */
    static final class C12399 implements FuncN<R> {
        final /* synthetic */ Func5 val$zipFunction;

        C12399(Func5 func5) {
            this.val$zipFunction = func5;
        }

        public R call(Object... objArr) {
            return this.val$zipFunction.call(objArr[0], objArr[1], objArr[2], objArr[3], objArr[4]);
        }
    }

    public interface Transformer<T, R> extends Func1<Single<T>, Single<R>> {
    }

    static {
        hook = RxJavaPlugins.getInstance().getSingleExecutionHook();
    }

    private Single(rx.Observable.OnSubscribe<T> onSubscribe) {
        this.onSubscribe = onSubscribe;
    }

    protected Single(OnSubscribe<T> onSubscribe) {
        this.onSubscribe = new C12241(onSubscribe);
    }

    private static <T> Observable<T> asObservable(Single<T> single) {
        return Observable.create(single.onSubscribe);
    }

    public static <T> Observable<T> concat(Single<? extends T> single, Single<? extends T> single2) {
        return Observable.concat(asObservable(single), asObservable(single2));
    }

    public static <T> Observable<T> concat(Single<? extends T> single, Single<? extends T> single2, Single<? extends T> single3) {
        return Observable.concat(asObservable(single), asObservable(single2), asObservable(single3));
    }

    public static <T> Observable<T> concat(Single<? extends T> single, Single<? extends T> single2, Single<? extends T> single3, Single<? extends T> single4) {
        return Observable.concat(asObservable(single), asObservable(single2), asObservable(single3), asObservable(single4));
    }

    public static <T> Observable<T> concat(Single<? extends T> single, Single<? extends T> single2, Single<? extends T> single3, Single<? extends T> single4, Single<? extends T> single5) {
        return Observable.concat(asObservable(single), asObservable(single2), asObservable(single3), asObservable(single4), asObservable(single5));
    }

    public static <T> Observable<T> concat(Single<? extends T> single, Single<? extends T> single2, Single<? extends T> single3, Single<? extends T> single4, Single<? extends T> single5, Single<? extends T> single6) {
        return Observable.concat(asObservable(single), asObservable(single2), asObservable(single3), asObservable(single4), asObservable(single5), asObservable(single6));
    }

    public static <T> Observable<T> concat(Single<? extends T> single, Single<? extends T> single2, Single<? extends T> single3, Single<? extends T> single4, Single<? extends T> single5, Single<? extends T> single6, Single<? extends T> single7) {
        return Observable.concat(asObservable(single), asObservable(single2), asObservable(single3), asObservable(single4), asObservable(single5), asObservable(single6), asObservable(single7));
    }

    public static <T> Observable<T> concat(Single<? extends T> single, Single<? extends T> single2, Single<? extends T> single3, Single<? extends T> single4, Single<? extends T> single5, Single<? extends T> single6, Single<? extends T> single7, Single<? extends T> single8) {
        return Observable.concat(asObservable(single), asObservable(single2), asObservable(single3), asObservable(single4), asObservable(single5), asObservable(single6), asObservable(single7), asObservable(single8));
    }

    public static <T> Observable<T> concat(Single<? extends T> single, Single<? extends T> single2, Single<? extends T> single3, Single<? extends T> single4, Single<? extends T> single5, Single<? extends T> single6, Single<? extends T> single7, Single<? extends T> single8, Single<? extends T> single9) {
        return Observable.concat(asObservable(single), asObservable(single2), asObservable(single3), asObservable(single4), asObservable(single5), asObservable(single6), asObservable(single7), asObservable(single8), asObservable(single9));
    }

    public static <T> Single<T> create(OnSubscribe<T> onSubscribe) {
        return new Single(hook.onCreate(onSubscribe));
    }

    @Experimental
    public static <T> Single<T> defer(Callable<Single<T>> callable) {
        return create(new AnonymousClass25(callable));
    }

    public static <T> Single<T> error(Throwable th) {
        return create(new C12323(th));
    }

    public static <T> Single<T> from(Future<? extends T> future) {
        return new Single(OnSubscribeToObservableFuture.toObservableFuture(future));
    }

    public static <T> Single<T> from(Future<? extends T> future, long j, TimeUnit timeUnit) {
        return new Single(OnSubscribeToObservableFuture.toObservableFuture(future, j, timeUnit));
    }

    public static <T> Single<T> from(Future<? extends T> future, Scheduler scheduler) {
        return new Single(OnSubscribeToObservableFuture.toObservableFuture(future)).subscribeOn(scheduler);
    }

    @Beta
    public static <T> Single<T> fromCallable(Callable<? extends T> callable) {
        return create(new C12334(callable));
    }

    static <T> Single<? extends T>[] iterableToArray(Iterable<? extends Single<? extends T>> iterable) {
        if (iterable instanceof Collection) {
            Collection collection = (Collection) iterable;
            return (Single[]) collection.toArray(new Single[collection.size()]);
        }
        Object obj = new Single[8];
        int i = 0;
        Object obj2 = obj;
        for (Single single : iterable) {
            if (i == obj2.length) {
                Object obj3 = new Single[((i >> 2) + i)];
                System.arraycopy(obj2, 0, obj3, 0, i);
                obj2 = obj3;
            }
            obj2[i] = single;
            i++;
        }
        if (obj2.length == i) {
            return obj2;
        }
        obj = new Single[i];
        System.arraycopy(obj2, 0, obj, 0, i);
        return obj;
    }

    public static <T> Single<T> just(T t) {
        return ScalarSynchronousSingle.create(t);
    }

    public static <T> Observable<T> merge(Single<? extends T> single, Single<? extends T> single2) {
        return Observable.merge(asObservable(single), asObservable(single2));
    }

    public static <T> Observable<T> merge(Single<? extends T> single, Single<? extends T> single2, Single<? extends T> single3) {
        return Observable.merge(asObservable(single), asObservable(single2), asObservable(single3));
    }

    public static <T> Observable<T> merge(Single<? extends T> single, Single<? extends T> single2, Single<? extends T> single3, Single<? extends T> single4) {
        return Observable.merge(asObservable(single), asObservable(single2), asObservable(single3), asObservable(single4));
    }

    public static <T> Observable<T> merge(Single<? extends T> single, Single<? extends T> single2, Single<? extends T> single3, Single<? extends T> single4, Single<? extends T> single5) {
        return Observable.merge(asObservable(single), asObservable(single2), asObservable(single3), asObservable(single4), asObservable(single5));
    }

    public static <T> Observable<T> merge(Single<? extends T> single, Single<? extends T> single2, Single<? extends T> single3, Single<? extends T> single4, Single<? extends T> single5, Single<? extends T> single6) {
        return Observable.merge(asObservable(single), asObservable(single2), asObservable(single3), asObservable(single4), asObservable(single5), asObservable(single6));
    }

    public static <T> Observable<T> merge(Single<? extends T> single, Single<? extends T> single2, Single<? extends T> single3, Single<? extends T> single4, Single<? extends T> single5, Single<? extends T> single6, Single<? extends T> single7) {
        return Observable.merge(asObservable(single), asObservable(single2), asObservable(single3), asObservable(single4), asObservable(single5), asObservable(single6), asObservable(single7));
    }

    public static <T> Observable<T> merge(Single<? extends T> single, Single<? extends T> single2, Single<? extends T> single3, Single<? extends T> single4, Single<? extends T> single5, Single<? extends T> single6, Single<? extends T> single7, Single<? extends T> single8) {
        return Observable.merge(asObservable(single), asObservable(single2), asObservable(single3), asObservable(single4), asObservable(single5), asObservable(single6), asObservable(single7), asObservable(single8));
    }

    public static <T> Observable<T> merge(Single<? extends T> single, Single<? extends T> single2, Single<? extends T> single3, Single<? extends T> single4, Single<? extends T> single5, Single<? extends T> single6, Single<? extends T> single7, Single<? extends T> single8, Single<? extends T> single9) {
        return Observable.merge(asObservable(single), asObservable(single2), asObservable(single3), asObservable(single4), asObservable(single5), asObservable(single6), asObservable(single7), asObservable(single8), asObservable(single9));
    }

    public static <T> Single<T> merge(Single<? extends Single<? extends T>> single) {
        return single instanceof ScalarSynchronousSingle ? ((ScalarSynchronousSingle) single).scalarFlatMap(UtilityFunctions.identity()) : create(new C12355(single));
    }

    private Single<Observable<T>> nest() {
        return just(asObservable(this));
    }

    @Experimental
    public static <T, Resource> Single<T> using(Func0<Resource> func0, Func1<? super Resource, ? extends Single<? extends T>> func1, Action1<? super Resource> action1) {
        return using(func0, func1, action1, false);
    }

    @Experimental
    public static <T, Resource> Single<T> using(Func0<Resource> func0, Func1<? super Resource, ? extends Single<? extends T>> func1, Action1<? super Resource> action1, boolean z) {
        if (func0 == null) {
            throw new NullPointerException("resourceFactory is null");
        } else if (func1 == null) {
            throw new NullPointerException("singleFactory is null");
        } else if (action1 != null) {
            return create(new SingleOnSubscribeUsing(func0, func1, action1, z));
        } else {
            throw new NullPointerException("disposeAction is null");
        }
    }

    public static <R> Single<R> zip(Iterable<? extends Single<?>> iterable, FuncN<? extends R> funcN) {
        return SingleOperatorZip.zip(iterableToArray(iterable), funcN);
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Single<R> zip(Single<? extends T1> single, Single<? extends T2> single2, Single<? extends T3> single3, Single<? extends T4> single4, Single<? extends T5> single5, Single<? extends T6> single6, Single<? extends T7> single7, Single<? extends T8> single8, Single<? extends T9> single9, Func9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> func9) {
        return SingleOperatorZip.zip(new Single[]{single, single2, single3, single4, single5, single6, single7, single8, single9}, new AnonymousClass13(func9));
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Single<R> zip(Single<? extends T1> single, Single<? extends T2> single2, Single<? extends T3> single3, Single<? extends T4> single4, Single<? extends T5> single5, Single<? extends T6> single6, Single<? extends T7> single7, Single<? extends T8> single8, Func8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> func8) {
        return SingleOperatorZip.zip(new Single[]{single, single2, single3, single4, single5, single6, single7, single8}, new AnonymousClass12(func8));
    }

    public static <T1, T2, T3, T4, T5, T6, T7, R> Single<R> zip(Single<? extends T1> single, Single<? extends T2> single2, Single<? extends T3> single3, Single<? extends T4> single4, Single<? extends T5> single5, Single<? extends T6> single6, Single<? extends T7> single7, Func7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> func7) {
        return SingleOperatorZip.zip(new Single[]{single, single2, single3, single4, single5, single6, single7}, new AnonymousClass11(func7));
    }

    public static <T1, T2, T3, T4, T5, T6, R> Single<R> zip(Single<? extends T1> single, Single<? extends T2> single2, Single<? extends T3> single3, Single<? extends T4> single4, Single<? extends T5> single5, Single<? extends T6> single6, Func6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> func6) {
        return SingleOperatorZip.zip(new Single[]{single, single2, single3, single4, single5, single6}, new AnonymousClass10(func6));
    }

    public static <T1, T2, T3, T4, T5, R> Single<R> zip(Single<? extends T1> single, Single<? extends T2> single2, Single<? extends T3> single3, Single<? extends T4> single4, Single<? extends T5> single5, Func5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> func5) {
        return SingleOperatorZip.zip(new Single[]{single, single2, single3, single4, single5}, new C12399(func5));
    }

    public static <T1, T2, T3, T4, R> Single<R> zip(Single<? extends T1> single, Single<? extends T2> single2, Single<? extends T3> single3, Single<? extends T4> single4, Func4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> func4) {
        return SingleOperatorZip.zip(new Single[]{single, single2, single3, single4}, new C12388(func4));
    }

    public static <T1, T2, T3, R> Single<R> zip(Single<? extends T1> single, Single<? extends T2> single2, Single<? extends T3> single3, Func3<? super T1, ? super T2, ? super T3, ? extends R> func3) {
        return SingleOperatorZip.zip(new Single[]{single, single2, single3}, new C12377(func3));
    }

    public static <T1, T2, R> Single<R> zip(Single<? extends T1> single, Single<? extends T2> single2, Func2<? super T1, ? super T2, ? extends R> func2) {
        return SingleOperatorZip.zip(new Single[]{single, single2}, new C12366(func2));
    }

    public <R> Single<R> compose(Transformer<? super T, ? extends R> transformer) {
        return (Single) transformer.call(this);
    }

    public final Observable<T> concatWith(Single<? extends T> single) {
        return concat(this, single);
    }

    @Experimental
    public final Single<T> delay(long j, TimeUnit timeUnit) {
        return delay(j, timeUnit, Schedulers.computation());
    }

    @Experimental
    public final Single<T> delay(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return lift(new OperatorDelay(j, timeUnit, scheduler));
    }

    @Experimental
    public final Single<T> delaySubscription(Observable<?> observable) {
        if (observable != null) {
            return create(new SingleOnSubscribeDelaySubscriptionOther(this, observable));
        }
        throw new NullPointerException();
    }

    @Experimental
    public final Single<T> doAfterTerminate(Action0 action0) {
        return lift(new OperatorDoAfterTerminate(action0));
    }

    @Experimental
    public final Single<T> doOnError(Action1<Throwable> action1) {
        return lift(new OperatorDoOnEach(new AnonymousClass23(action1)));
    }

    @Experimental
    public final Single<T> doOnSubscribe(Action0 action0) {
        return lift(new OperatorDoOnSubscribe(action0));
    }

    @Experimental
    public final Single<T> doOnSuccess(Action1<? super T> action1) {
        return lift(new OperatorDoOnEach(new AnonymousClass24(action1)));
    }

    @Experimental
    public final Single<T> doOnUnsubscribe(Action0 action0) {
        return lift(new OperatorDoOnUnsubscribe(action0));
    }

    public final <R> Single<R> flatMap(Func1<? super T, ? extends Single<? extends R>> func1) {
        return this instanceof ScalarSynchronousSingle ? ((ScalarSynchronousSingle) this).scalarFlatMap(func1) : merge(map(func1));
    }

    public final <R> Observable<R> flatMapObservable(Func1<? super T, ? extends Observable<? extends R>> func1) {
        return Observable.merge(asObservable(map(func1)));
    }

    @Experimental
    public final <R> Single<R> lift(Operator<? extends R, ? super T> operator) {
        return new Single(new C12312(operator));
    }

    public final <R> Single<R> map(Func1<? super T, ? extends R> func1) {
        return lift(new OperatorMap(func1));
    }

    public final Observable<T> mergeWith(Single<? extends T> single) {
        return merge(this, single);
    }

    public final Single<T> observeOn(Scheduler scheduler) {
        return this instanceof ScalarSynchronousSingle ? ((ScalarSynchronousSingle) this).scalarScheduleOn(scheduler) : lift(new OperatorObserveOn(scheduler, false));
    }

    @Experimental
    public final Single<T> onErrorResumeNext(Single<? extends T> single) {
        return new Single(SingleOperatorOnErrorResumeNext.withOther(this, single));
    }

    @Experimental
    public final Single<T> onErrorResumeNext(Func1<Throwable, ? extends Single<? extends T>> func1) {
        return new Single(SingleOperatorOnErrorResumeNext.withFunction(this, func1));
    }

    public final Single<T> onErrorReturn(Func1<Throwable, ? extends T> func1) {
        return lift(OperatorOnErrorResumeNextViaFunction.withSingle(func1));
    }

    public final Single<T> retry() {
        return toObservable().retry().toSingle();
    }

    public final Single<T> retry(long j) {
        return toObservable().retry(j).toSingle();
    }

    public final Single<T> retry(Func2<Integer, Throwable, Boolean> func2) {
        return toObservable().retry((Func2) func2).toSingle();
    }

    public final Single<T> retryWhen(Func1<Observable<? extends Throwable>, ? extends Observable<?>> func1) {
        return toObservable().retryWhen(func1).toSingle();
    }

    public final Subscription subscribe() {
        return subscribe(new Subscriber<T>() {
            public final void onCompleted() {
            }

            public final void onError(Throwable th) {
                throw new OnErrorNotImplementedException(th);
            }

            public final void onNext(T t) {
            }
        });
    }

    public final Subscription subscribe(Observer<? super T> observer) {
        if (observer != null) {
            return subscribe(new AnonymousClass17(observer));
        }
        throw new NullPointerException("observer is null");
    }

    public final Subscription subscribe(SingleSubscriber<? super T> singleSubscriber) {
        Subscriber anonymousClass18 = new AnonymousClass18(singleSubscriber);
        singleSubscriber.add(anonymousClass18);
        subscribe(anonymousClass18);
        return anonymousClass18;
    }

    public final Subscription subscribe(Subscriber<? super T> subscriber) {
        if (subscriber == null) {
            throw new IllegalArgumentException("observer can not be null");
        } else if (this.onSubscribe == null) {
            throw new IllegalStateException("onSubscribe function can not be null.");
        } else {
            Object safeSubscriber;
            subscriber.onStart();
            if (!(subscriber instanceof SafeSubscriber)) {
                safeSubscriber = new SafeSubscriber(subscriber);
            }
            try {
                hook.onSubscribeStart(this, this.onSubscribe).call(safeSubscriber);
                return hook.onSubscribeReturn(safeSubscriber);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                hook.onSubscribeError(new RuntimeException("Error occurred attempting to subscribe [" + th.getMessage() + "] and then again while trying to pass to onError.", th));
            }
        }
    }

    public final Subscription subscribe(Action1<? super T> action1) {
        if (action1 != null) {
            return subscribe(new AnonymousClass15(action1));
        }
        throw new IllegalArgumentException("onSuccess can not be null");
    }

    public final Subscription subscribe(Action1<? super T> action1, Action1<Throwable> action12) {
        if (action1 == null) {
            throw new IllegalArgumentException("onSuccess can not be null");
        } else if (action12 != null) {
            return subscribe(new AnonymousClass16(action12, action1));
        } else {
            throw new IllegalArgumentException("onError can not be null");
        }
    }

    public final Single<T> subscribeOn(Scheduler scheduler) {
        return this instanceof ScalarSynchronousSingle ? ((ScalarSynchronousSingle) this).scalarScheduleOn(scheduler) : create(new AnonymousClass19(scheduler));
    }

    public final Single<T> takeUntil(Completable completable) {
        return lift(new AnonymousClass20(completable));
    }

    public final <E> Single<T> takeUntil(Observable<? extends E> observable) {
        return lift(new AnonymousClass21(observable));
    }

    public final <E> Single<T> takeUntil(Single<? extends E> single) {
        return lift(new Operator<T, T>() {
            final /* synthetic */ Single val$other;

            /* renamed from: rx.Single.22.1 */
            class C12291 extends Subscriber<T> {
                final /* synthetic */ Subscriber val$serial;

                C12291(Subscriber subscriber, boolean z, Subscriber subscriber2) {
                    this.val$serial = subscriber2;
                    super(subscriber, z);
                }

                public void onCompleted() {
                    try {
                        this.val$serial.onCompleted();
                    } finally {
                        this.val$serial.unsubscribe();
                    }
                }

                public void onError(Throwable th) {
                    try {
                        this.val$serial.onError(th);
                    } finally {
                        this.val$serial.unsubscribe();
                    }
                }

                public void onNext(T t) {
                    this.val$serial.onNext(t);
                }
            }

            /* renamed from: rx.Single.22.2 */
            class C12302 extends SingleSubscriber<E> {
                final /* synthetic */ Subscriber val$main;

                C12302(Subscriber subscriber) {
                    this.val$main = subscriber;
                }

                public void onError(Throwable th) {
                    this.val$main.onError(th);
                }

                public void onSuccess(E e) {
                    onError(new CancellationException("Stream was canceled before emitting a terminal event."));
                }
            }

            {
                this.val$other = r2;
            }

            public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
                Subscription serializedSubscriber = new SerializedSubscriber(subscriber, false);
                Object c12291 = new C12291(serializedSubscriber, false, serializedSubscriber);
                SingleSubscriber c12302 = new C12302(c12291);
                serializedSubscriber.add(c12291);
                serializedSubscriber.add(c12302);
                subscriber.add(serializedSubscriber);
                this.val$other.subscribe(c12302);
                return c12291;
            }
        });
    }

    public final Single<T> timeout(long j, TimeUnit timeUnit) {
        return timeout(j, timeUnit, null, Schedulers.computation());
    }

    public final Single<T> timeout(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout(j, timeUnit, null, scheduler);
    }

    public final Single<T> timeout(long j, TimeUnit timeUnit, Single<? extends T> single) {
        return timeout(j, timeUnit, single, Schedulers.computation());
    }

    public final Single<T> timeout(long j, TimeUnit timeUnit, Single<? extends T> single, Scheduler scheduler) {
        Single error;
        if (single == null) {
            error = error(new TimeoutException());
        }
        return lift(new OperatorTimeout(j, timeUnit, asObservable(error), scheduler));
    }

    @Experimental
    public final BlockingSingle<T> toBlocking() {
        return BlockingSingle.from(this);
    }

    public final Observable<T> toObservable() {
        return asObservable(this);
    }

    public final Subscription unsafeSubscribe(Subscriber<? super T> subscriber) {
        try {
            subscriber.onStart();
            hook.onSubscribeStart(this, this.onSubscribe).call(subscriber);
            return hook.onSubscribeReturn(subscriber);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            hook.onSubscribeError(new RuntimeException("Error occurred attempting to subscribe [" + th.getMessage() + "] and then again while trying to pass to onError.", th));
        }
    }

    public final <T2, R> Single<R> zipWith(Single<? extends T2> single, Func2<? super T, ? super T2, ? extends R> func2) {
        return zip(this, single, func2);
    }
}

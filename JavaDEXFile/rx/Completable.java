package rx;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import rx.Scheduler.Worker;
import rx.Single.OnSubscribe;
import rx.annotations.Experimental;
import rx.exceptions.CompositeException;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.internal.operators.CompletableOnSubscribeConcat;
import rx.internal.operators.CompletableOnSubscribeConcatArray;
import rx.internal.operators.CompletableOnSubscribeConcatIterable;
import rx.internal.operators.CompletableOnSubscribeMerge;
import rx.internal.operators.CompletableOnSubscribeMergeArray;
import rx.internal.operators.CompletableOnSubscribeMergeDelayErrorArray;
import rx.internal.operators.CompletableOnSubscribeMergeDelayErrorIterable;
import rx.internal.operators.CompletableOnSubscribeMergeIterable;
import rx.internal.operators.CompletableOnSubscribeTimeout;
import rx.internal.util.SubscriptionList;
import rx.internal.util.UtilityFunctions;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.Schedulers;
import rx.subscriptions.BooleanSubscription;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.MultipleAssignmentSubscription;
import rx.subscriptions.SerialSubscription;
import rx.subscriptions.Subscriptions;

@Experimental
public class Completable {
    static final Completable COMPLETE;
    static final RxJavaErrorHandler ERROR_HANDLER;
    static final Completable NEVER;
    private final CompletableOnSubscribe onSubscribe;

    public interface CompletableOnSubscribe extends Action1<CompletableSubscriber> {
    }

    /* renamed from: rx.Completable.10 */
    static final class AnonymousClass10 implements CompletableOnSubscribe {
        final /* synthetic */ Observable val$flowable;

        /* renamed from: rx.Completable.10.1 */
        class C11701 extends Subscriber<Object> {
            final /* synthetic */ CompletableSubscriber val$cs;

            C11701(CompletableSubscriber completableSubscriber) {
                this.val$cs = completableSubscriber;
            }

            public void onCompleted() {
                this.val$cs.onCompleted();
            }

            public void onError(Throwable th) {
                this.val$cs.onError(th);
            }

            public void onNext(Object obj) {
            }
        }

        AnonymousClass10(Observable observable) {
            this.val$flowable = observable;
        }

        public void call(CompletableSubscriber completableSubscriber) {
            Object c11701 = new C11701(completableSubscriber);
            completableSubscriber.onSubscribe(c11701);
            this.val$flowable.unsafeSubscribe(c11701);
        }
    }

    /* renamed from: rx.Completable.11 */
    static final class AnonymousClass11 implements CompletableOnSubscribe {
        final /* synthetic */ Single val$single;

        /* renamed from: rx.Completable.11.1 */
        class C11711 extends SingleSubscriber<Object> {
            final /* synthetic */ CompletableSubscriber val$s;

            C11711(CompletableSubscriber completableSubscriber) {
                this.val$s = completableSubscriber;
            }

            public void onError(Throwable th) {
                this.val$s.onError(th);
            }

            public void onSuccess(Object obj) {
                this.val$s.onCompleted();
            }
        }

        AnonymousClass11(Single single) {
            this.val$single = single;
        }

        public void call(CompletableSubscriber completableSubscriber) {
            SingleSubscriber c11711 = new C11711(completableSubscriber);
            completableSubscriber.onSubscribe(c11711);
            this.val$single.subscribe(c11711);
        }
    }

    /* renamed from: rx.Completable.12 */
    static final class AnonymousClass12 implements CompletableOnSubscribe {
        final /* synthetic */ long val$delay;
        final /* synthetic */ Scheduler val$scheduler;
        final /* synthetic */ TimeUnit val$unit;

        /* renamed from: rx.Completable.12.1 */
        class C11721 implements Action0 {
            final /* synthetic */ CompletableSubscriber val$s;
            final /* synthetic */ Worker val$w;

            C11721(CompletableSubscriber completableSubscriber, Worker worker) {
                this.val$s = completableSubscriber;
                this.val$w = worker;
            }

            public void call() {
                try {
                    this.val$s.onCompleted();
                } finally {
                    this.val$w.unsubscribe();
                }
            }
        }

        AnonymousClass12(Scheduler scheduler, long j, TimeUnit timeUnit) {
            this.val$scheduler = scheduler;
            this.val$delay = j;
            this.val$unit = timeUnit;
        }

        public void call(CompletableSubscriber completableSubscriber) {
            MultipleAssignmentSubscription multipleAssignmentSubscription = new MultipleAssignmentSubscription();
            completableSubscriber.onSubscribe(multipleAssignmentSubscription);
            if (!multipleAssignmentSubscription.isUnsubscribed()) {
                Worker createWorker = this.val$scheduler.createWorker();
                multipleAssignmentSubscription.set(createWorker);
                createWorker.schedule(new C11721(completableSubscriber, createWorker), this.val$delay, this.val$unit);
            }
        }
    }

    public interface CompletableSubscriber {
        void onCompleted();

        void onError(Throwable th);

        void onSubscribe(Subscription subscription);
    }

    /* renamed from: rx.Completable.13 */
    static final class AnonymousClass13 implements CompletableOnSubscribe {
        final /* synthetic */ Func1 val$completableFunc1;
        final /* synthetic */ Action1 val$disposer;
        final /* synthetic */ boolean val$eager;
        final /* synthetic */ Func0 val$resourceFunc0;

        /* renamed from: rx.Completable.13.1 */
        class C11741 implements CompletableSubscriber {
            Subscription f906d;
            final /* synthetic */ AtomicBoolean val$once;
            final /* synthetic */ Object val$resource;
            final /* synthetic */ CompletableSubscriber val$s;

            /* renamed from: rx.Completable.13.1.1 */
            class C11731 implements Action0 {
                C11731() {
                }

                public void call() {
                    C11741.this.dispose();
                }
            }

            C11741(AtomicBoolean atomicBoolean, Object obj, CompletableSubscriber completableSubscriber) {
                this.val$once = atomicBoolean;
                this.val$resource = obj;
                this.val$s = completableSubscriber;
            }

            void dispose() {
                this.f906d.unsubscribe();
                if (this.val$once.compareAndSet(false, true)) {
                    try {
                        AnonymousClass13.this.val$disposer.call(this.val$resource);
                    } catch (Throwable th) {
                        Completable.ERROR_HANDLER.handleError(th);
                    }
                }
            }

            public void onCompleted() {
                if (AnonymousClass13.this.val$eager && this.val$once.compareAndSet(false, true)) {
                    try {
                        AnonymousClass13.this.val$disposer.call(this.val$resource);
                    } catch (Throwable th) {
                        this.val$s.onError(th);
                        return;
                    }
                }
                this.val$s.onCompleted();
                if (!AnonymousClass13.this.val$eager) {
                    dispose();
                }
            }

            public void onError(Throwable th) {
                if (AnonymousClass13.this.val$eager && this.val$once.compareAndSet(false, true)) {
                    try {
                        AnonymousClass13.this.val$disposer.call(this.val$resource);
                    } catch (Throwable th2) {
                        Throwable th3 = th2;
                        th = new CompositeException(Arrays.asList(new Throwable[]{th, th3}));
                    }
                }
                this.val$s.onError(th);
                if (!AnonymousClass13.this.val$eager) {
                    dispose();
                }
            }

            public void onSubscribe(Subscription subscription) {
                this.f906d = subscription;
                this.val$s.onSubscribe(Subscriptions.create(new C11731()));
            }
        }

        AnonymousClass13(Func0 func0, Func1 func1, Action1 action1, boolean z) {
            this.val$resourceFunc0 = func0;
            this.val$completableFunc1 = func1;
            this.val$disposer = action1;
            this.val$eager = z;
        }

        public void call(CompletableSubscriber completableSubscriber) {
            try {
                Object call = this.val$resourceFunc0.call();
                try {
                    Completable completable = (Completable) this.val$completableFunc1.call(call);
                    if (completable == null) {
                        try {
                            this.val$disposer.call(call);
                            completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
                            completableSubscriber.onError(new NullPointerException("The completable supplied is null"));
                            return;
                        } catch (Throwable th) {
                            Exceptions.throwIfFatal(th);
                            completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
                            completableSubscriber.onError(new CompositeException(Arrays.asList(new Throwable[]{new NullPointerException("The completable supplied is null"), th})));
                            return;
                        }
                    }
                    completable.subscribe(new C11741(new AtomicBoolean(), call, completableSubscriber));
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th);
                    Exceptions.throwIfFatal(th2);
                    completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
                    completableSubscriber.onError(new CompositeException(Arrays.asList(new Throwable[]{th, th2})));
                }
            } catch (Throwable th3) {
                completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
                completableSubscriber.onError(th3);
            }
        }
    }

    /* renamed from: rx.Completable.14 */
    class AnonymousClass14 implements CompletableSubscriber {
        final /* synthetic */ CountDownLatch val$cdl;
        final /* synthetic */ Throwable[] val$err;

        AnonymousClass14(CountDownLatch countDownLatch, Throwable[] thArr) {
            this.val$cdl = countDownLatch;
            this.val$err = thArr;
        }

        public void onCompleted() {
            this.val$cdl.countDown();
        }

        public void onError(Throwable th) {
            this.val$err[0] = th;
            this.val$cdl.countDown();
        }

        public void onSubscribe(Subscription subscription) {
        }
    }

    /* renamed from: rx.Completable.15 */
    class AnonymousClass15 implements CompletableSubscriber {
        final /* synthetic */ CountDownLatch val$cdl;
        final /* synthetic */ Throwable[] val$err;

        AnonymousClass15(CountDownLatch countDownLatch, Throwable[] thArr) {
            this.val$cdl = countDownLatch;
            this.val$err = thArr;
        }

        public void onCompleted() {
            this.val$cdl.countDown();
        }

        public void onError(Throwable th) {
            this.val$err[0] = th;
            this.val$cdl.countDown();
        }

        public void onSubscribe(Subscription subscription) {
        }
    }

    /* renamed from: rx.Completable.16 */
    class AnonymousClass16 implements CompletableOnSubscribe {
        final /* synthetic */ long val$delay;
        final /* synthetic */ boolean val$delayError;
        final /* synthetic */ Scheduler val$scheduler;
        final /* synthetic */ TimeUnit val$unit;

        /* renamed from: rx.Completable.16.1 */
        class C11771 implements CompletableSubscriber {
            final /* synthetic */ CompletableSubscriber val$s;
            final /* synthetic */ CompositeSubscription val$set;
            final /* synthetic */ Worker val$w;

            /* renamed from: rx.Completable.16.1.1 */
            class C11751 implements Action0 {
                C11751() {
                }

                public void call() {
                    try {
                        C11771.this.val$s.onCompleted();
                    } finally {
                        C11771.this.val$w.unsubscribe();
                    }
                }
            }

            /* renamed from: rx.Completable.16.1.2 */
            class C11762 implements Action0 {
                final /* synthetic */ Throwable val$e;

                C11762(Throwable th) {
                    this.val$e = th;
                }

                public void call() {
                    try {
                        C11771.this.val$s.onError(this.val$e);
                    } finally {
                        C11771.this.val$w.unsubscribe();
                    }
                }
            }

            C11771(CompositeSubscription compositeSubscription, Worker worker, CompletableSubscriber completableSubscriber) {
                this.val$set = compositeSubscription;
                this.val$w = worker;
                this.val$s = completableSubscriber;
            }

            public void onCompleted() {
                this.val$set.add(this.val$w.schedule(new C11751(), AnonymousClass16.this.val$delay, AnonymousClass16.this.val$unit));
            }

            public void onError(Throwable th) {
                if (AnonymousClass16.this.val$delayError) {
                    this.val$set.add(this.val$w.schedule(new C11762(th), AnonymousClass16.this.val$delay, AnonymousClass16.this.val$unit));
                } else {
                    this.val$s.onError(th);
                }
            }

            public void onSubscribe(Subscription subscription) {
                this.val$set.add(subscription);
                this.val$s.onSubscribe(this.val$set);
            }
        }

        AnonymousClass16(Scheduler scheduler, long j, TimeUnit timeUnit, boolean z) {
            this.val$scheduler = scheduler;
            this.val$delay = j;
            this.val$unit = timeUnit;
            this.val$delayError = z;
        }

        public void call(CompletableSubscriber completableSubscriber) {
            CompositeSubscription compositeSubscription = new CompositeSubscription();
            Object createWorker = this.val$scheduler.createWorker();
            compositeSubscription.add(createWorker);
            Completable.this.subscribe(new C11771(compositeSubscription, createWorker, completableSubscriber));
        }
    }

    /* renamed from: rx.Completable.17 */
    class AnonymousClass17 implements CompletableOnSubscribe {
        final /* synthetic */ Action0 val$onAfterComplete;
        final /* synthetic */ Action0 val$onComplete;
        final /* synthetic */ Action1 val$onError;
        final /* synthetic */ Action1 val$onSubscribe;
        final /* synthetic */ Action0 val$onUnsubscribe;

        /* renamed from: rx.Completable.17.1 */
        class C11791 implements CompletableSubscriber {
            final /* synthetic */ CompletableSubscriber val$s;

            /* renamed from: rx.Completable.17.1.1 */
            class C11781 implements Action0 {
                final /* synthetic */ Subscription val$d;

                C11781(Subscription subscription) {
                    this.val$d = subscription;
                }

                public void call() {
                    try {
                        AnonymousClass17.this.val$onUnsubscribe.call();
                    } catch (Throwable th) {
                        Completable.ERROR_HANDLER.handleError(th);
                    }
                    this.val$d.unsubscribe();
                }
            }

            C11791(CompletableSubscriber completableSubscriber) {
                this.val$s = completableSubscriber;
            }

            public void onCompleted() {
                try {
                    AnonymousClass17.this.val$onComplete.call();
                    this.val$s.onCompleted();
                    try {
                        AnonymousClass17.this.val$onAfterComplete.call();
                    } catch (Throwable th) {
                        Completable.ERROR_HANDLER.handleError(th);
                    }
                } catch (Throwable th2) {
                    this.val$s.onError(th2);
                }
            }

            public void onError(Throwable th) {
                try {
                    AnonymousClass17.this.val$onError.call(th);
                } catch (Throwable th2) {
                    Throwable th3 = th2;
                    th = new CompositeException(Arrays.asList(new Throwable[]{th, th3}));
                }
                this.val$s.onError(th);
            }

            public void onSubscribe(Subscription subscription) {
                try {
                    AnonymousClass17.this.val$onSubscribe.call(subscription);
                    this.val$s.onSubscribe(Subscriptions.create(new C11781(subscription)));
                } catch (Throwable th) {
                    subscription.unsubscribe();
                    this.val$s.onSubscribe(Subscriptions.unsubscribed());
                    this.val$s.onError(th);
                }
            }
        }

        AnonymousClass17(Action0 action0, Action0 action02, Action1 action1, Action1 action12, Action0 action03) {
            this.val$onComplete = action0;
            this.val$onAfterComplete = action02;
            this.val$onError = action1;
            this.val$onSubscribe = action12;
            this.val$onUnsubscribe = action03;
        }

        public void call(CompletableSubscriber completableSubscriber) {
            Completable.this.subscribe(new C11791(completableSubscriber));
        }
    }

    /* renamed from: rx.Completable.18 */
    class AnonymousClass18 implements Action1<Throwable> {
        final /* synthetic */ Action0 val$onTerminate;

        AnonymousClass18(Action0 action0) {
            this.val$onTerminate = action0;
        }

        public void call(Throwable th) {
            this.val$onTerminate.call();
        }
    }

    /* renamed from: rx.Completable.19 */
    class AnonymousClass19 implements CompletableSubscriber {
        final /* synthetic */ CountDownLatch val$cdl;
        final /* synthetic */ Throwable[] val$err;

        AnonymousClass19(CountDownLatch countDownLatch, Throwable[] thArr) {
            this.val$cdl = countDownLatch;
            this.val$err = thArr;
        }

        public void onCompleted() {
            this.val$cdl.countDown();
        }

        public void onError(Throwable th) {
            this.val$err[0] = th;
            this.val$cdl.countDown();
        }

        public void onSubscribe(Subscription subscription) {
        }
    }

    /* renamed from: rx.Completable.1 */
    static final class C11801 implements CompletableOnSubscribe {
        C11801() {
        }

        public void call(CompletableSubscriber completableSubscriber) {
            completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
            completableSubscriber.onCompleted();
        }
    }

    /* renamed from: rx.Completable.20 */
    class AnonymousClass20 implements CompletableSubscriber {
        final /* synthetic */ CountDownLatch val$cdl;
        final /* synthetic */ Throwable[] val$err;

        AnonymousClass20(CountDownLatch countDownLatch, Throwable[] thArr) {
            this.val$cdl = countDownLatch;
            this.val$err = thArr;
        }

        public void onCompleted() {
            this.val$cdl.countDown();
        }

        public void onError(Throwable th) {
            this.val$err[0] = th;
            this.val$cdl.countDown();
        }

        public void onSubscribe(Subscription subscription) {
        }
    }

    /* renamed from: rx.Completable.21 */
    class AnonymousClass21 implements CompletableOnSubscribe {
        final /* synthetic */ CompletableOperator val$onLift;

        AnonymousClass21(CompletableOperator completableOperator) {
            this.val$onLift = completableOperator;
        }

        public void call(CompletableSubscriber completableSubscriber) {
            NullPointerException e;
            try {
                Completable.this.subscribe((CompletableSubscriber) this.val$onLift.call(completableSubscriber));
            } catch (NullPointerException e2) {
                throw e2;
            } catch (Throwable th) {
                e2 = Completable.toNpe(th);
            }
        }
    }

    /* renamed from: rx.Completable.22 */
    class AnonymousClass22 implements CompletableOnSubscribe {
        final /* synthetic */ Scheduler val$scheduler;

        /* renamed from: rx.Completable.22.1 */
        class C11831 implements CompletableSubscriber {
            final /* synthetic */ SubscriptionList val$ad;
            final /* synthetic */ CompletableSubscriber val$s;
            final /* synthetic */ Worker val$w;

            /* renamed from: rx.Completable.22.1.1 */
            class C11811 implements Action0 {
                C11811() {
                }

                public void call() {
                    try {
                        C11831.this.val$s.onCompleted();
                    } finally {
                        C11831.this.val$ad.unsubscribe();
                    }
                }
            }

            /* renamed from: rx.Completable.22.1.2 */
            class C11822 implements Action0 {
                final /* synthetic */ Throwable val$e;

                C11822(Throwable th) {
                    this.val$e = th;
                }

                public void call() {
                    try {
                        C11831.this.val$s.onError(this.val$e);
                    } finally {
                        C11831.this.val$ad.unsubscribe();
                    }
                }
            }

            C11831(Worker worker, CompletableSubscriber completableSubscriber, SubscriptionList subscriptionList) {
                this.val$w = worker;
                this.val$s = completableSubscriber;
                this.val$ad = subscriptionList;
            }

            public void onCompleted() {
                this.val$w.schedule(new C11811());
            }

            public void onError(Throwable th) {
                this.val$w.schedule(new C11822(th));
            }

            public void onSubscribe(Subscription subscription) {
                this.val$ad.add(subscription);
            }
        }

        AnonymousClass22(Scheduler scheduler) {
            this.val$scheduler = scheduler;
        }

        public void call(CompletableSubscriber completableSubscriber) {
            Object subscriptionList = new SubscriptionList();
            Object createWorker = this.val$scheduler.createWorker();
            subscriptionList.add(createWorker);
            completableSubscriber.onSubscribe(subscriptionList);
            Completable.this.subscribe(new C11831(createWorker, completableSubscriber, subscriptionList));
        }
    }

    /* renamed from: rx.Completable.23 */
    class AnonymousClass23 implements CompletableOnSubscribe {
        final /* synthetic */ Func1 val$predicate;

        /* renamed from: rx.Completable.23.1 */
        class C11841 implements CompletableSubscriber {
            final /* synthetic */ CompletableSubscriber val$s;

            C11841(CompletableSubscriber completableSubscriber) {
                this.val$s = completableSubscriber;
            }

            public void onCompleted() {
                this.val$s.onCompleted();
            }

            public void onError(Throwable th) {
                try {
                    if (((Boolean) AnonymousClass23.this.val$predicate.call(th)).booleanValue()) {
                        this.val$s.onCompleted();
                    } else {
                        this.val$s.onError(th);
                    }
                } catch (Throwable th2) {
                    CompositeException compositeException = new CompositeException(Arrays.asList(new Throwable[]{th, th2}));
                }
            }

            public void onSubscribe(Subscription subscription) {
                this.val$s.onSubscribe(subscription);
            }
        }

        AnonymousClass23(Func1 func1) {
            this.val$predicate = func1;
        }

        public void call(CompletableSubscriber completableSubscriber) {
            Completable.this.subscribe(new C11841(completableSubscriber));
        }
    }

    /* renamed from: rx.Completable.24 */
    class AnonymousClass24 implements CompletableOnSubscribe {
        final /* synthetic */ Func1 val$errorMapper;

        /* renamed from: rx.Completable.24.1 */
        class C11861 implements CompletableSubscriber {
            final /* synthetic */ CompletableSubscriber val$s;
            final /* synthetic */ SerialSubscription val$sd;

            /* renamed from: rx.Completable.24.1.1 */
            class C11851 implements CompletableSubscriber {
                C11851() {
                }

                public void onCompleted() {
                    C11861.this.val$s.onCompleted();
                }

                public void onError(Throwable th) {
                    C11861.this.val$s.onError(th);
                }

                public void onSubscribe(Subscription subscription) {
                    C11861.this.val$sd.set(subscription);
                }
            }

            C11861(CompletableSubscriber completableSubscriber, SerialSubscription serialSubscription) {
                this.val$s = completableSubscriber;
                this.val$sd = serialSubscription;
            }

            public void onCompleted() {
                this.val$s.onCompleted();
            }

            public void onError(Throwable th) {
                try {
                    Completable completable = (Completable) AnonymousClass24.this.val$errorMapper.call(th);
                    if (completable == null) {
                        this.val$s.onError(new CompositeException(Arrays.asList(new Throwable[]{th, new NullPointerException("The completable returned is null")})));
                        return;
                    }
                    completable.subscribe(new C11851());
                } catch (Throwable th2) {
                    this.val$s.onError(new CompositeException(Arrays.asList(new Throwable[]{th, th2})));
                }
            }

            public void onSubscribe(Subscription subscription) {
                this.val$sd.set(subscription);
            }
        }

        AnonymousClass24(Func1 func1) {
            this.val$errorMapper = func1;
        }

        public void call(CompletableSubscriber completableSubscriber) {
            Completable.this.subscribe(new C11861(completableSubscriber, new SerialSubscription()));
        }
    }

    /* renamed from: rx.Completable.25 */
    class AnonymousClass25 implements CompletableSubscriber {
        final /* synthetic */ MultipleAssignmentSubscription val$mad;

        AnonymousClass25(MultipleAssignmentSubscription multipleAssignmentSubscription) {
            this.val$mad = multipleAssignmentSubscription;
        }

        public void onCompleted() {
            this.val$mad.unsubscribe();
        }

        public void onError(Throwable th) {
            Completable.ERROR_HANDLER.handleError(th);
            this.val$mad.unsubscribe();
            Completable.deliverUncaughtException(th);
        }

        public void onSubscribe(Subscription subscription) {
            this.val$mad.set(subscription);
        }
    }

    /* renamed from: rx.Completable.26 */
    class AnonymousClass26 implements CompletableSubscriber {
        final /* synthetic */ MultipleAssignmentSubscription val$mad;
        final /* synthetic */ Action0 val$onComplete;

        AnonymousClass26(Action0 action0, MultipleAssignmentSubscription multipleAssignmentSubscription) {
            this.val$onComplete = action0;
            this.val$mad = multipleAssignmentSubscription;
        }

        public void onCompleted() {
            try {
                this.val$onComplete.call();
            } catch (Throwable th) {
                Completable.ERROR_HANDLER.handleError(th);
                Completable.deliverUncaughtException(th);
            } finally {
                this.val$mad.unsubscribe();
            }
        }

        public void onError(Throwable th) {
            Completable.ERROR_HANDLER.handleError(th);
            this.val$mad.unsubscribe();
            Completable.deliverUncaughtException(th);
        }

        public void onSubscribe(Subscription subscription) {
            this.val$mad.set(subscription);
        }
    }

    /* renamed from: rx.Completable.27 */
    class AnonymousClass27 implements CompletableSubscriber {
        final /* synthetic */ MultipleAssignmentSubscription val$mad;
        final /* synthetic */ Action0 val$onComplete;
        final /* synthetic */ Action1 val$onError;

        AnonymousClass27(Action0 action0, MultipleAssignmentSubscription multipleAssignmentSubscription, Action1 action1) {
            this.val$onComplete = action0;
            this.val$mad = multipleAssignmentSubscription;
            this.val$onError = action1;
        }

        public void onCompleted() {
            try {
                this.val$onComplete.call();
                this.val$mad.unsubscribe();
            } catch (Throwable th) {
                onError(th);
            }
        }

        public void onError(Throwable th) {
            Throwable th2;
            try {
                this.val$onError.call(th);
                this.val$mad.unsubscribe();
            } catch (Throwable th3) {
                th2 = th3;
                this.val$mad.unsubscribe();
                throw th2;
            }
        }

        public void onSubscribe(Subscription subscription) {
            this.val$mad.set(subscription);
        }
    }

    /* renamed from: rx.Completable.28 */
    class AnonymousClass28 implements CompletableSubscriber {
        final /* synthetic */ Subscriber val$sw;

        AnonymousClass28(Subscriber subscriber) {
            this.val$sw = subscriber;
        }

        public void onCompleted() {
            this.val$sw.onCompleted();
        }

        public void onError(Throwable th) {
            this.val$sw.onError(th);
        }

        public void onSubscribe(Subscription subscription) {
            this.val$sw.add(subscription);
        }
    }

    /* renamed from: rx.Completable.29 */
    class AnonymousClass29 implements CompletableOnSubscribe {
        final /* synthetic */ Scheduler val$scheduler;

        /* renamed from: rx.Completable.29.1 */
        class C11871 implements Action0 {
            final /* synthetic */ CompletableSubscriber val$s;
            final /* synthetic */ Worker val$w;

            C11871(CompletableSubscriber completableSubscriber, Worker worker) {
                this.val$s = completableSubscriber;
                this.val$w = worker;
            }

            public void call() {
                try {
                    Completable.this.subscribe(this.val$s);
                } finally {
                    this.val$w.unsubscribe();
                }
            }
        }

        AnonymousClass29(Scheduler scheduler) {
            this.val$scheduler = scheduler;
        }

        public void call(CompletableSubscriber completableSubscriber) {
            Worker createWorker = this.val$scheduler.createWorker();
            createWorker.schedule(new C11871(completableSubscriber, createWorker));
        }
    }

    /* renamed from: rx.Completable.2 */
    static final class C11882 implements CompletableOnSubscribe {
        C11882() {
        }

        public void call(CompletableSubscriber completableSubscriber) {
            completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
        }
    }

    /* renamed from: rx.Completable.31 */
    class AnonymousClass31 implements OnSubscribe<T> {
        final /* synthetic */ Func0 val$completionValueFunc0;

        /* renamed from: rx.Completable.31.1 */
        class C11901 implements CompletableSubscriber {
            final /* synthetic */ SingleSubscriber val$s;

            C11901(SingleSubscriber singleSubscriber) {
                this.val$s = singleSubscriber;
            }

            public void onCompleted() {
                try {
                    Object call = AnonymousClass31.this.val$completionValueFunc0.call();
                    if (call == null) {
                        this.val$s.onError(new NullPointerException("The value supplied is null"));
                    } else {
                        this.val$s.onSuccess(call);
                    }
                } catch (Throwable th) {
                    this.val$s.onError(th);
                }
            }

            public void onError(Throwable th) {
                this.val$s.onError(th);
            }

            public void onSubscribe(Subscription subscription) {
                this.val$s.add(subscription);
            }
        }

        AnonymousClass31(Func0 func0) {
            this.val$completionValueFunc0 = func0;
        }

        public void call(SingleSubscriber<? super T> singleSubscriber) {
            Completable.this.subscribe(new C11901(singleSubscriber));
        }
    }

    /* renamed from: rx.Completable.32 */
    class AnonymousClass32 implements Func0<T> {
        final /* synthetic */ Object val$completionValue;

        AnonymousClass32(Object obj) {
            this.val$completionValue = obj;
        }

        public T call() {
            return this.val$completionValue;
        }
    }

    /* renamed from: rx.Completable.33 */
    class AnonymousClass33 implements CompletableOnSubscribe {
        final /* synthetic */ Scheduler val$scheduler;

        /* renamed from: rx.Completable.33.1 */
        class C11931 implements CompletableSubscriber {
            final /* synthetic */ CompletableSubscriber val$s;

            /* renamed from: rx.Completable.33.1.1 */
            class C11921 implements Action0 {
                final /* synthetic */ Subscription val$d;

                /* renamed from: rx.Completable.33.1.1.1 */
                class C11911 implements Action0 {
                    final /* synthetic */ Worker val$w;

                    C11911(Worker worker) {
                        this.val$w = worker;
                    }

                    public void call() {
                        try {
                            C11921.this.val$d.unsubscribe();
                        } finally {
                            this.val$w.unsubscribe();
                        }
                    }
                }

                C11921(Subscription subscription) {
                    this.val$d = subscription;
                }

                public void call() {
                    Worker createWorker = AnonymousClass33.this.val$scheduler.createWorker();
                    createWorker.schedule(new C11911(createWorker));
                }
            }

            C11931(CompletableSubscriber completableSubscriber) {
                this.val$s = completableSubscriber;
            }

            public void onCompleted() {
                this.val$s.onCompleted();
            }

            public void onError(Throwable th) {
                this.val$s.onError(th);
            }

            public void onSubscribe(Subscription subscription) {
                this.val$s.onSubscribe(Subscriptions.create(new C11921(subscription)));
            }
        }

        AnonymousClass33(Scheduler scheduler) {
            this.val$scheduler = scheduler;
        }

        public void call(CompletableSubscriber completableSubscriber) {
            Completable.this.subscribe(new C11931(completableSubscriber));
        }
    }

    /* renamed from: rx.Completable.3 */
    static final class C11943 implements CompletableOnSubscribe {
        final /* synthetic */ Completable[] val$sources;

        /* renamed from: rx.Completable.3.1 */
        class C11891 implements CompletableSubscriber {
            final /* synthetic */ AtomicBoolean val$once;
            final /* synthetic */ CompletableSubscriber val$s;
            final /* synthetic */ CompositeSubscription val$set;

            C11891(AtomicBoolean atomicBoolean, CompositeSubscription compositeSubscription, CompletableSubscriber completableSubscriber) {
                this.val$once = atomicBoolean;
                this.val$set = compositeSubscription;
                this.val$s = completableSubscriber;
            }

            public void onCompleted() {
                if (this.val$once.compareAndSet(false, true)) {
                    this.val$set.unsubscribe();
                    this.val$s.onCompleted();
                }
            }

            public void onError(Throwable th) {
                if (this.val$once.compareAndSet(false, true)) {
                    this.val$set.unsubscribe();
                    this.val$s.onError(th);
                    return;
                }
                Completable.ERROR_HANDLER.handleError(th);
            }

            public void onSubscribe(Subscription subscription) {
                this.val$set.add(subscription);
            }
        }

        C11943(Completable[] completableArr) {
            this.val$sources = completableArr;
        }

        public void call(CompletableSubscriber completableSubscriber) {
            CompositeSubscription compositeSubscription = new CompositeSubscription();
            completableSubscriber.onSubscribe(compositeSubscription);
            AtomicBoolean atomicBoolean = new AtomicBoolean();
            CompletableSubscriber c11891 = new C11891(atomicBoolean, compositeSubscription, completableSubscriber);
            Completable[] completableArr = this.val$sources;
            int length = completableArr.length;
            int i = 0;
            while (i < length) {
                Completable completable = completableArr[i];
                if (!compositeSubscription.isUnsubscribed()) {
                    if (completable == null) {
                        Throwable nullPointerException = new NullPointerException("One of the sources is null");
                        if (atomicBoolean.compareAndSet(false, true)) {
                            compositeSubscription.unsubscribe();
                            completableSubscriber.onError(nullPointerException);
                            return;
                        }
                        Completable.ERROR_HANDLER.handleError(nullPointerException);
                        return;
                    } else if (!atomicBoolean.get() && !compositeSubscription.isUnsubscribed()) {
                        completable.subscribe(c11891);
                        i++;
                    } else {
                        return;
                    }
                }
                return;
            }
        }
    }

    /* renamed from: rx.Completable.4 */
    static final class C11964 implements CompletableOnSubscribe {
        final /* synthetic */ Iterable val$sources;

        /* renamed from: rx.Completable.4.1 */
        class C11951 implements CompletableSubscriber {
            final /* synthetic */ AtomicBoolean val$once;
            final /* synthetic */ CompletableSubscriber val$s;
            final /* synthetic */ CompositeSubscription val$set;

            C11951(AtomicBoolean atomicBoolean, CompositeSubscription compositeSubscription, CompletableSubscriber completableSubscriber) {
                this.val$once = atomicBoolean;
                this.val$set = compositeSubscription;
                this.val$s = completableSubscriber;
            }

            public void onCompleted() {
                if (this.val$once.compareAndSet(false, true)) {
                    this.val$set.unsubscribe();
                    this.val$s.onCompleted();
                }
            }

            public void onError(Throwable th) {
                if (this.val$once.compareAndSet(false, true)) {
                    this.val$set.unsubscribe();
                    this.val$s.onError(th);
                    return;
                }
                Completable.ERROR_HANDLER.handleError(th);
            }

            public void onSubscribe(Subscription subscription) {
                this.val$set.add(subscription);
            }
        }

        C11964(Iterable iterable) {
            this.val$sources = iterable;
        }

        public void call(CompletableSubscriber completableSubscriber) {
            CompositeSubscription compositeSubscription = new CompositeSubscription();
            completableSubscriber.onSubscribe(compositeSubscription);
            AtomicBoolean atomicBoolean = new AtomicBoolean();
            CompletableSubscriber c11951 = new C11951(atomicBoolean, compositeSubscription, completableSubscriber);
            Throwable nullPointerException;
            try {
                Iterator it = this.val$sources.iterator();
                if (it == null) {
                    completableSubscriber.onError(new NullPointerException("The iterator returned is null"));
                    return;
                }
                boolean z = true;
                while (!atomicBoolean.get() && !compositeSubscription.isUnsubscribed()) {
                    try {
                        if (it.hasNext()) {
                            if (!atomicBoolean.get() && !compositeSubscription.isUnsubscribed()) {
                                try {
                                    Completable completable = (Completable) it.next();
                                    if (completable == null) {
                                        nullPointerException = new NullPointerException("One of the sources is null");
                                        if (atomicBoolean.compareAndSet(false, true)) {
                                            compositeSubscription.unsubscribe();
                                            completableSubscriber.onError(nullPointerException);
                                            return;
                                        }
                                        Completable.ERROR_HANDLER.handleError(nullPointerException);
                                        return;
                                    } else if (!atomicBoolean.get() && !compositeSubscription.isUnsubscribed()) {
                                        completable.subscribe(c11951);
                                        z = false;
                                    } else {
                                        return;
                                    }
                                } catch (Throwable nullPointerException2) {
                                    if (atomicBoolean.compareAndSet(false, true)) {
                                        compositeSubscription.unsubscribe();
                                        completableSubscriber.onError(nullPointerException2);
                                        return;
                                    }
                                    Completable.ERROR_HANDLER.handleError(nullPointerException2);
                                    return;
                                }
                            }
                            return;
                        } else if (z) {
                            completableSubscriber.onCompleted();
                            return;
                        } else {
                            return;
                        }
                    } catch (Throwable nullPointerException22) {
                        if (atomicBoolean.compareAndSet(false, true)) {
                            compositeSubscription.unsubscribe();
                            completableSubscriber.onError(nullPointerException22);
                            return;
                        }
                        Completable.ERROR_HANDLER.handleError(nullPointerException22);
                        return;
                    }
                }
            } catch (Throwable nullPointerException222) {
                completableSubscriber.onError(nullPointerException222);
            }
        }
    }

    /* renamed from: rx.Completable.5 */
    static final class C11975 implements CompletableOnSubscribe {
        final /* synthetic */ Func0 val$completableFunc0;

        C11975(Func0 func0) {
            this.val$completableFunc0 = func0;
        }

        public void call(CompletableSubscriber completableSubscriber) {
            try {
                Completable completable = (Completable) this.val$completableFunc0.call();
                if (completable == null) {
                    completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
                    completableSubscriber.onError(new NullPointerException("The completable returned is null"));
                    return;
                }
                completable.subscribe(completableSubscriber);
            } catch (Throwable th) {
                completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
                completableSubscriber.onError(th);
            }
        }
    }

    /* renamed from: rx.Completable.6 */
    static final class C11986 implements CompletableOnSubscribe {
        final /* synthetic */ Func0 val$errorFunc0;

        C11986(Func0 func0) {
            this.val$errorFunc0 = func0;
        }

        public void call(CompletableSubscriber completableSubscriber) {
            Throwable th;
            completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
            try {
                th = (Throwable) this.val$errorFunc0.call();
            } catch (Throwable th2) {
                th = th2;
            }
            if (th == null) {
                th = new NullPointerException("The error supplied is null");
            }
            completableSubscriber.onError(th);
        }
    }

    /* renamed from: rx.Completable.7 */
    static final class C11997 implements CompletableOnSubscribe {
        final /* synthetic */ Throwable val$error;

        C11997(Throwable th) {
            this.val$error = th;
        }

        public void call(CompletableSubscriber completableSubscriber) {
            completableSubscriber.onSubscribe(Subscriptions.unsubscribed());
            completableSubscriber.onError(this.val$error);
        }
    }

    /* renamed from: rx.Completable.8 */
    static final class C12008 implements CompletableOnSubscribe {
        final /* synthetic */ Action0 val$action;

        C12008(Action0 action0) {
            this.val$action = action0;
        }

        public void call(CompletableSubscriber completableSubscriber) {
            BooleanSubscription booleanSubscription = new BooleanSubscription();
            completableSubscriber.onSubscribe(booleanSubscription);
            try {
                this.val$action.call();
                if (!booleanSubscription.isUnsubscribed()) {
                    completableSubscriber.onCompleted();
                }
            } catch (Throwable th) {
                if (!booleanSubscription.isUnsubscribed()) {
                    completableSubscriber.onError(th);
                }
            }
        }
    }

    /* renamed from: rx.Completable.9 */
    static final class C12019 implements CompletableOnSubscribe {
        final /* synthetic */ Callable val$callable;

        C12019(Callable callable) {
            this.val$callable = callable;
        }

        public void call(CompletableSubscriber completableSubscriber) {
            BooleanSubscription booleanSubscription = new BooleanSubscription();
            completableSubscriber.onSubscribe(booleanSubscription);
            try {
                this.val$callable.call();
                if (!booleanSubscription.isUnsubscribed()) {
                    completableSubscriber.onCompleted();
                }
            } catch (Throwable th) {
                if (!booleanSubscription.isUnsubscribed()) {
                    completableSubscriber.onError(th);
                }
            }
        }
    }

    public interface CompletableOperator extends Func1<CompletableSubscriber, CompletableSubscriber> {
    }

    public interface CompletableTransformer extends Func1<Completable, Completable> {
    }

    static {
        COMPLETE = create(new C11801());
        NEVER = create(new C11882());
        ERROR_HANDLER = RxJavaPlugins.getInstance().getErrorHandler();
    }

    protected Completable(CompletableOnSubscribe completableOnSubscribe) {
        this.onSubscribe = completableOnSubscribe;
    }

    public static Completable amb(Iterable<? extends Completable> iterable) {
        requireNonNull(iterable);
        return create(new C11964(iterable));
    }

    public static Completable amb(Completable... completableArr) {
        requireNonNull(completableArr);
        return completableArr.length == 0 ? complete() : completableArr.length == 1 ? completableArr[0] : create(new C11943(completableArr));
    }

    public static Completable complete() {
        return COMPLETE;
    }

    public static Completable concat(Iterable<? extends Completable> iterable) {
        requireNonNull(iterable);
        return create(new CompletableOnSubscribeConcatIterable(iterable));
    }

    public static Completable concat(Observable<? extends Completable> observable) {
        return concat(observable, 2);
    }

    public static Completable concat(Observable<? extends Completable> observable, int i) {
        requireNonNull(observable);
        if (i >= 1) {
            return create(new CompletableOnSubscribeConcat(observable, i));
        }
        throw new IllegalArgumentException("prefetch > 0 required but it was " + i);
    }

    public static Completable concat(Completable... completableArr) {
        requireNonNull(completableArr);
        return completableArr.length == 0 ? complete() : completableArr.length == 1 ? completableArr[0] : create(new CompletableOnSubscribeConcatArray(completableArr));
    }

    public static Completable create(CompletableOnSubscribe completableOnSubscribe) {
        NullPointerException e;
        requireNonNull(completableOnSubscribe);
        try {
            return new Completable(completableOnSubscribe);
        } catch (NullPointerException e2) {
            throw e2;
        } catch (Throwable th) {
            ERROR_HANDLER.handleError(th);
            e2 = toNpe(th);
        }
    }

    public static Completable defer(Func0<? extends Completable> func0) {
        requireNonNull(func0);
        return create(new C11975(func0));
    }

    private static void deliverUncaughtException(Throwable th) {
        Thread currentThread = Thread.currentThread();
        currentThread.getUncaughtExceptionHandler().uncaughtException(currentThread, th);
    }

    public static Completable error(Throwable th) {
        requireNonNull(th);
        return create(new C11997(th));
    }

    public static Completable error(Func0<? extends Throwable> func0) {
        requireNonNull(func0);
        return create(new C11986(func0));
    }

    public static Completable fromAction(Action0 action0) {
        requireNonNull(action0);
        return create(new C12008(action0));
    }

    public static Completable fromCallable(Callable<?> callable) {
        requireNonNull(callable);
        return create(new C12019(callable));
    }

    public static Completable fromFuture(Future<?> future) {
        requireNonNull(future);
        return fromObservable(Observable.from((Future) future));
    }

    public static Completable fromObservable(Observable<?> observable) {
        requireNonNull(observable);
        return create(new AnonymousClass10(observable));
    }

    public static Completable fromSingle(Single<?> single) {
        requireNonNull(single);
        return create(new AnonymousClass11(single));
    }

    public static Completable merge(Iterable<? extends Completable> iterable) {
        requireNonNull(iterable);
        return create(new CompletableOnSubscribeMergeIterable(iterable));
    }

    public static Completable merge(Observable<? extends Completable> observable) {
        return merge0(observable, Integer.MAX_VALUE, false);
    }

    public static Completable merge(Observable<? extends Completable> observable, int i) {
        return merge0(observable, i, false);
    }

    public static Completable merge(Completable... completableArr) {
        requireNonNull(completableArr);
        return completableArr.length == 0 ? complete() : completableArr.length == 1 ? completableArr[0] : create(new CompletableOnSubscribeMergeArray(completableArr));
    }

    protected static Completable merge0(Observable<? extends Completable> observable, int i, boolean z) {
        requireNonNull(observable);
        if (i >= 1) {
            return create(new CompletableOnSubscribeMerge(observable, i, z));
        }
        throw new IllegalArgumentException("maxConcurrency > 0 required but it was " + i);
    }

    public static Completable mergeDelayError(Iterable<? extends Completable> iterable) {
        requireNonNull(iterable);
        return create(new CompletableOnSubscribeMergeDelayErrorIterable(iterable));
    }

    public static Completable mergeDelayError(Observable<? extends Completable> observable) {
        return merge0(observable, Integer.MAX_VALUE, true);
    }

    public static Completable mergeDelayError(Observable<? extends Completable> observable, int i) {
        return merge0(observable, i, true);
    }

    public static Completable mergeDelayError(Completable... completableArr) {
        requireNonNull(completableArr);
        return create(new CompletableOnSubscribeMergeDelayErrorArray(completableArr));
    }

    public static Completable never() {
        return NEVER;
    }

    static <T> T requireNonNull(T t) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException();
    }

    public static Completable timer(long j, TimeUnit timeUnit) {
        return timer(j, timeUnit, Schedulers.computation());
    }

    public static Completable timer(long j, TimeUnit timeUnit, Scheduler scheduler) {
        requireNonNull(timeUnit);
        requireNonNull(scheduler);
        return create(new AnonymousClass12(scheduler, j, timeUnit));
    }

    static NullPointerException toNpe(Throwable th) {
        NullPointerException nullPointerException = new NullPointerException("Actually not, but can't pass out an exception otherwise...");
        nullPointerException.initCause(th);
        return nullPointerException;
    }

    public static <R> Completable using(Func0<R> func0, Func1<? super R, ? extends Completable> func1, Action1<? super R> action1) {
        return using(func0, func1, action1, true);
    }

    public static <R> Completable using(Func0<R> func0, Func1<? super R, ? extends Completable> func1, Action1<? super R> action1, boolean z) {
        requireNonNull(func0);
        requireNonNull(func1);
        requireNonNull(action1);
        return create(new AnonymousClass13(func0, func1, action1, z));
    }

    public final Completable ambWith(Completable completable) {
        requireNonNull(completable);
        return amb(this, completable);
    }

    public final <T> Observable<T> andThen(Observable<T> observable) {
        requireNonNull(observable);
        return observable.delaySubscription(toObservable());
    }

    public final <T> Single<T> andThen(Single<T> single) {
        requireNonNull(single);
        return single.delaySubscription(toObservable());
    }

    public final void await() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Throwable[] thArr = new Throwable[1];
        subscribe(new AnonymousClass14(countDownLatch, thArr));
        if (countDownLatch.getCount() != 0) {
            try {
                countDownLatch.await();
                if (thArr[0] != null) {
                    Exceptions.propagate(thArr[0]);
                }
            } catch (Throwable e) {
                throw Exceptions.propagate(e);
            }
        } else if (thArr[0] != null) {
            Exceptions.propagate(thArr[0]);
        }
    }

    public final boolean await(long j, TimeUnit timeUnit) {
        boolean z = true;
        requireNonNull(timeUnit);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Throwable[] thArr = new Throwable[1];
        subscribe(new AnonymousClass15(countDownLatch, thArr));
        if (countDownLatch.getCount() != 0) {
            try {
                z = countDownLatch.await(j, timeUnit);
                if (z && thArr[0] != null) {
                    Exceptions.propagate(thArr[0]);
                }
            } catch (Throwable e) {
                throw Exceptions.propagate(e);
            }
        } else if (thArr[0] != null) {
            Exceptions.propagate(thArr[0]);
        }
        return z;
    }

    public final Completable compose(CompletableTransformer completableTransformer) {
        return (Completable) to(completableTransformer);
    }

    public final Completable concatWith(Completable completable) {
        requireNonNull(completable);
        return concat(this, completable);
    }

    public final Completable delay(long j, TimeUnit timeUnit) {
        return delay(j, timeUnit, Schedulers.computation(), false);
    }

    public final Completable delay(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return delay(j, timeUnit, scheduler, false);
    }

    public final Completable delay(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        requireNonNull(timeUnit);
        requireNonNull(scheduler);
        return create(new AnonymousClass16(scheduler, j, timeUnit, z));
    }

    public final Completable doAfterTerminate(Action0 action0) {
        return doOnLifecycle(Actions.empty(), Actions.empty(), Actions.empty(), action0, Actions.empty());
    }

    @Deprecated
    public final Completable doOnComplete(Action0 action0) {
        return doOnCompleted(action0);
    }

    public final Completable doOnCompleted(Action0 action0) {
        return doOnLifecycle(Actions.empty(), Actions.empty(), action0, Actions.empty(), Actions.empty());
    }

    public final Completable doOnError(Action1<? super Throwable> action1) {
        return doOnLifecycle(Actions.empty(), action1, Actions.empty(), Actions.empty(), Actions.empty());
    }

    protected final Completable doOnLifecycle(Action1<? super Subscription> action1, Action1<? super Throwable> action12, Action0 action0, Action0 action02, Action0 action03) {
        requireNonNull(action1);
        requireNonNull(action12);
        requireNonNull(action0);
        requireNonNull(action02);
        requireNonNull(action03);
        return create(new AnonymousClass17(action0, action02, action12, action1, action03));
    }

    public final Completable doOnSubscribe(Action1<? super Subscription> action1) {
        return doOnLifecycle(action1, Actions.empty(), Actions.empty(), Actions.empty(), Actions.empty());
    }

    public final Completable doOnTerminate(Action0 action0) {
        return doOnLifecycle(Actions.empty(), new AnonymousClass18(action0), action0, Actions.empty(), Actions.empty());
    }

    public final Completable doOnUnsubscribe(Action0 action0) {
        return doOnLifecycle(Actions.empty(), Actions.empty(), Actions.empty(), Actions.empty(), action0);
    }

    public final Completable endWith(Completable completable) {
        return concatWith(completable);
    }

    public final <T> Observable<T> endWith(Observable<T> observable) {
        return observable.startWith(toObservable());
    }

    public final Throwable get() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Throwable[] thArr = new Throwable[1];
        subscribe(new AnonymousClass19(countDownLatch, thArr));
        if (countDownLatch.getCount() == 0) {
            return thArr[0];
        }
        try {
            countDownLatch.await();
            return thArr[0];
        } catch (Throwable e) {
            throw Exceptions.propagate(e);
        }
    }

    public final Throwable get(long j, TimeUnit timeUnit) {
        requireNonNull(timeUnit);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Throwable[] thArr = new Throwable[1];
        subscribe(new AnonymousClass20(countDownLatch, thArr));
        if (countDownLatch.getCount() == 0) {
            return thArr[0];
        }
        try {
            if (countDownLatch.await(j, timeUnit)) {
                return thArr[0];
            }
            Exceptions.propagate(new TimeoutException());
            return null;
        } catch (Throwable e) {
            throw Exceptions.propagate(e);
        }
    }

    public final Completable lift(CompletableOperator completableOperator) {
        requireNonNull(completableOperator);
        return create(new AnonymousClass21(completableOperator));
    }

    public final Completable mergeWith(Completable completable) {
        requireNonNull(completable);
        return merge(this, completable);
    }

    public final Completable observeOn(Scheduler scheduler) {
        requireNonNull(scheduler);
        return create(new AnonymousClass22(scheduler));
    }

    public final Completable onErrorComplete() {
        return onErrorComplete(UtilityFunctions.alwaysTrue());
    }

    public final Completable onErrorComplete(Func1<? super Throwable, Boolean> func1) {
        requireNonNull(func1);
        return create(new AnonymousClass23(func1));
    }

    public final Completable onErrorResumeNext(Func1<? super Throwable, ? extends Completable> func1) {
        requireNonNull(func1);
        return create(new AnonymousClass24(func1));
    }

    public final Completable repeat() {
        return fromObservable(toObservable().repeat());
    }

    public final Completable repeat(long j) {
        return fromObservable(toObservable().repeat(j));
    }

    public final Completable repeatWhen(Func1<? super Observable<? extends Void>, ? extends Observable<?>> func1) {
        requireNonNull(func1);
        return fromObservable(toObservable().repeatWhen(func1));
    }

    public final Completable retry() {
        return fromObservable(toObservable().retry());
    }

    public final Completable retry(long j) {
        return fromObservable(toObservable().retry(j));
    }

    public final Completable retry(Func2<Integer, Throwable, Boolean> func2) {
        return fromObservable(toObservable().retry((Func2) func2));
    }

    public final Completable retryWhen(Func1<? super Observable<? extends Throwable>, ? extends Observable<?>> func1) {
        return fromObservable(toObservable().retryWhen(func1));
    }

    public final Completable startWith(Completable completable) {
        requireNonNull(completable);
        return concat(completable, this);
    }

    public final <T> Observable<T> startWith(Observable<T> observable) {
        requireNonNull(observable);
        return toObservable().startWith((Observable) observable);
    }

    public final Subscription subscribe() {
        Subscription multipleAssignmentSubscription = new MultipleAssignmentSubscription();
        subscribe(new AnonymousClass25(multipleAssignmentSubscription));
        return multipleAssignmentSubscription;
    }

    public final Subscription subscribe(Action0 action0) {
        requireNonNull(action0);
        Subscription multipleAssignmentSubscription = new MultipleAssignmentSubscription();
        subscribe(new AnonymousClass26(action0, multipleAssignmentSubscription));
        return multipleAssignmentSubscription;
    }

    public final Subscription subscribe(Action1<? super Throwable> action1, Action0 action0) {
        requireNonNull(action1);
        requireNonNull(action0);
        Subscription multipleAssignmentSubscription = new MultipleAssignmentSubscription();
        subscribe(new AnonymousClass27(action0, multipleAssignmentSubscription, action1));
        return multipleAssignmentSubscription;
    }

    public final void subscribe(CompletableSubscriber completableSubscriber) {
        NullPointerException e;
        requireNonNull(completableSubscriber);
        try {
            this.onSubscribe.call(completableSubscriber);
        } catch (NullPointerException e2) {
            throw e2;
        } catch (Throwable th) {
            ERROR_HANDLER.handleError(th);
            e2 = toNpe(th);
        }
    }

    public final <T> void subscribe(Subscriber<T> subscriber) {
        NullPointerException e;
        requireNonNull(subscriber);
        if (subscriber == null) {
            try {
                throw new NullPointerException("The RxJavaPlugins.onSubscribe returned a null Subscriber");
            } catch (NullPointerException e2) {
                throw e2;
            } catch (Throwable th) {
                ERROR_HANDLER.handleError(th);
                e2 = toNpe(th);
            }
        } else {
            subscribe(new AnonymousClass28(subscriber));
        }
    }

    public final Completable subscribeOn(Scheduler scheduler) {
        requireNonNull(scheduler);
        return create(new AnonymousClass29(scheduler));
    }

    public final Completable timeout(long j, TimeUnit timeUnit) {
        return timeout0(j, timeUnit, Schedulers.computation(), null);
    }

    public final Completable timeout(long j, TimeUnit timeUnit, Completable completable) {
        requireNonNull(completable);
        return timeout0(j, timeUnit, Schedulers.computation(), completable);
    }

    public final Completable timeout(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout0(j, timeUnit, scheduler, null);
    }

    public final Completable timeout(long j, TimeUnit timeUnit, Scheduler scheduler, Completable completable) {
        requireNonNull(completable);
        return timeout0(j, timeUnit, scheduler, completable);
    }

    public final Completable timeout0(long j, TimeUnit timeUnit, Scheduler scheduler, Completable completable) {
        requireNonNull(timeUnit);
        requireNonNull(scheduler);
        return create(new CompletableOnSubscribeTimeout(this, j, timeUnit, scheduler, completable));
    }

    public final <U> U to(Func1<? super Completable, U> func1) {
        return func1.call(this);
    }

    public final <T> Observable<T> toObservable() {
        return Observable.create(new Observable.OnSubscribe<T>() {
            public void call(Subscriber<? super T> subscriber) {
                Completable.this.subscribe((Subscriber) subscriber);
            }
        });
    }

    public final <T> Single<T> toSingle(Func0<? extends T> func0) {
        requireNonNull(func0);
        return Single.create(new AnonymousClass31(func0));
    }

    public final <T> Single<T> toSingleDefault(T t) {
        requireNonNull(t);
        return toSingle(new AnonymousClass32(t));
    }

    public final Completable unsubscribeOn(Scheduler scheduler) {
        requireNonNull(scheduler);
        return create(new AnonymousClass33(scheduler));
    }
}

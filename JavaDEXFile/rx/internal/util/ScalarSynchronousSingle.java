package rx.internal.util;

import rx.Scheduler;
import rx.Single;
import rx.Single.OnSubscribe;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.internal.schedulers.EventLoopsScheduler;

public final class ScalarSynchronousSingle<T> extends Single<T> {
    final T value;

    /* renamed from: rx.internal.util.ScalarSynchronousSingle.1 */
    class C14551 implements OnSubscribe<T> {
        final /* synthetic */ Object val$t;

        C14551(Object obj) {
            this.val$t = obj;
        }

        public void call(SingleSubscriber<? super T> singleSubscriber) {
            singleSubscriber.onSuccess(this.val$t);
        }
    }

    /* renamed from: rx.internal.util.ScalarSynchronousSingle.2 */
    class C14572 implements OnSubscribe<R> {
        final /* synthetic */ Func1 val$func;

        /* renamed from: rx.internal.util.ScalarSynchronousSingle.2.1 */
        class C14561 extends Subscriber<R> {
            final /* synthetic */ SingleSubscriber val$child;

            C14561(SingleSubscriber singleSubscriber) {
                this.val$child = singleSubscriber;
            }

            public void onCompleted() {
            }

            public void onError(Throwable th) {
                this.val$child.onError(th);
            }

            public void onNext(R r) {
                this.val$child.onSuccess(r);
            }
        }

        C14572(Func1 func1) {
            this.val$func = func1;
        }

        public void call(SingleSubscriber<? super R> singleSubscriber) {
            Single single = (Single) this.val$func.call(ScalarSynchronousSingle.this.value);
            if (single instanceof ScalarSynchronousSingle) {
                singleSubscriber.onSuccess(((ScalarSynchronousSingle) single).value);
                return;
            }
            Object c14561 = new C14561(singleSubscriber);
            singleSubscriber.add(c14561);
            single.unsafeSubscribe(c14561);
        }
    }

    static final class DirectScheduledEmission<T> implements OnSubscribe<T> {
        private final EventLoopsScheduler es;
        private final T value;

        DirectScheduledEmission(EventLoopsScheduler eventLoopsScheduler, T t) {
            this.es = eventLoopsScheduler;
            this.value = t;
        }

        public void call(SingleSubscriber<? super T> singleSubscriber) {
            singleSubscriber.add(this.es.scheduleDirect(new ScalarSynchronousSingleAction(singleSubscriber, this.value)));
        }
    }

    static final class NormalScheduledEmission<T> implements OnSubscribe<T> {
        private final Scheduler scheduler;
        private final T value;

        NormalScheduledEmission(Scheduler scheduler, T t) {
            this.scheduler = scheduler;
            this.value = t;
        }

        public void call(SingleSubscriber<? super T> singleSubscriber) {
            Object createWorker = this.scheduler.createWorker();
            singleSubscriber.add(createWorker);
            createWorker.schedule(new ScalarSynchronousSingleAction(singleSubscriber, this.value));
        }
    }

    static final class ScalarSynchronousSingleAction<T> implements Action0 {
        private final SingleSubscriber<? super T> subscriber;
        private final T value;

        ScalarSynchronousSingleAction(SingleSubscriber<? super T> singleSubscriber, T t) {
            this.subscriber = singleSubscriber;
            this.value = t;
        }

        public void call() {
            try {
                this.subscriber.onSuccess(this.value);
            } catch (Throwable th) {
                this.subscriber.onError(th);
            }
        }
    }

    protected ScalarSynchronousSingle(T t) {
        super(new C14551(t));
        this.value = t;
    }

    public static final <T> ScalarSynchronousSingle<T> create(T t) {
        return new ScalarSynchronousSingle(t);
    }

    public T get() {
        return this.value;
    }

    public <R> Single<R> scalarFlatMap(Func1<? super T, ? extends Single<? extends R>> func1) {
        return Single.create(new C14572(func1));
    }

    public Single<T> scalarScheduleOn(Scheduler scheduler) {
        return scheduler instanceof EventLoopsScheduler ? Single.create(new DirectScheduledEmission((EventLoopsScheduler) scheduler, this.value)) : Single.create(new NormalScheduledEmission(scheduler, this.value));
    }
}

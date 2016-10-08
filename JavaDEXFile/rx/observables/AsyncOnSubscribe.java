package rx.observables;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.annotations.Experimental;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Action3;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func3;
import rx.internal.operators.BufferUntilSubscriber;
import rx.observers.SerializedObserver;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.CompositeSubscription;

@Experimental
public abstract class AsyncOnSubscribe<S, T> implements OnSubscribe<T> {

    /* renamed from: rx.observables.AsyncOnSubscribe.1 */
    static final class C14591 implements Func3<S, Long, Observer<Observable<? extends T>>, S> {
        final /* synthetic */ Action3 val$next;

        C14591(Action3 action3) {
            this.val$next = action3;
        }

        public S call(S s, Long l, Observer<Observable<? extends T>> observer) {
            this.val$next.call(s, l, observer);
            return s;
        }
    }

    /* renamed from: rx.observables.AsyncOnSubscribe.2 */
    static final class C14602 implements Func3<S, Long, Observer<Observable<? extends T>>, S> {
        final /* synthetic */ Action3 val$next;

        C14602(Action3 action3) {
            this.val$next = action3;
        }

        public S call(S s, Long l, Observer<Observable<? extends T>> observer) {
            this.val$next.call(s, l, observer);
            return s;
        }
    }

    /* renamed from: rx.observables.AsyncOnSubscribe.3 */
    static final class C14613 implements Func3<Void, Long, Observer<Observable<? extends T>>, Void> {
        final /* synthetic */ Action2 val$next;

        C14613(Action2 action2) {
            this.val$next = action2;
        }

        public Void call(Void voidR, Long l, Observer<Observable<? extends T>> observer) {
            this.val$next.call(l, observer);
            return voidR;
        }
    }

    /* renamed from: rx.observables.AsyncOnSubscribe.4 */
    static final class C14624 implements Func3<Void, Long, Observer<Observable<? extends T>>, Void> {
        final /* synthetic */ Action2 val$next;

        C14624(Action2 action2) {
            this.val$next = action2;
        }

        public Void call(Void voidR, Long l, Observer<Observable<? extends T>> observer) {
            this.val$next.call(l, observer);
            return null;
        }
    }

    /* renamed from: rx.observables.AsyncOnSubscribe.5 */
    static final class C14635 implements Action1<Void> {
        final /* synthetic */ Action0 val$onUnsubscribe;

        C14635(Action0 action0) {
            this.val$onUnsubscribe = action0;
        }

        public void call(Void voidR) {
            this.val$onUnsubscribe.call();
        }
    }

    /* renamed from: rx.observables.AsyncOnSubscribe.6 */
    class C14646 extends Subscriber<T> {
        final /* synthetic */ Subscriber val$actualSubscriber;
        final /* synthetic */ AsyncOuterManager val$outerProducer;

        C14646(Subscriber subscriber, AsyncOuterManager asyncOuterManager) {
            this.val$actualSubscriber = subscriber;
            this.val$outerProducer = asyncOuterManager;
        }

        public void onCompleted() {
            this.val$actualSubscriber.onCompleted();
        }

        public void onError(Throwable th) {
            this.val$actualSubscriber.onError(th);
        }

        public void onNext(T t) {
            this.val$actualSubscriber.onNext(t);
        }

        public void setProducer(Producer producer) {
            this.val$outerProducer.setConcatProducer(producer);
        }
    }

    /* renamed from: rx.observables.AsyncOnSubscribe.7 */
    class C14657 implements Func1<Observable<T>, Observable<T>> {
        C14657() {
        }

        public Observable<T> call(Observable<T> observable) {
            return observable.onBackpressureBuffer();
        }
    }

    private static final class AsyncOnSubscribeImpl<S, T> extends AsyncOnSubscribe<S, T> {
        private final Func0<? extends S> generator;
        private final Func3<? super S, Long, ? super Observer<Observable<? extends T>>, ? extends S> next;
        private final Action1<? super S> onUnsubscribe;

        public AsyncOnSubscribeImpl(Func0<? extends S> func0, Func3<? super S, Long, ? super Observer<Observable<? extends T>>, ? extends S> func3) {
            this(func0, func3, null);
        }

        AsyncOnSubscribeImpl(Func0<? extends S> func0, Func3<? super S, Long, ? super Observer<Observable<? extends T>>, ? extends S> func3, Action1<? super S> action1) {
            this.generator = func0;
            this.next = func3;
            this.onUnsubscribe = action1;
        }

        public AsyncOnSubscribeImpl(Func3<S, Long, Observer<Observable<? extends T>>, S> func3) {
            this(null, func3, null);
        }

        public AsyncOnSubscribeImpl(Func3<S, Long, Observer<Observable<? extends T>>, S> func3, Action1<? super S> action1) {
            this(null, func3, action1);
        }

        public /* bridge */ /* synthetic */ void call(Object obj) {
            super.call((Subscriber) obj);
        }

        protected S generateState() {
            return this.generator == null ? null : this.generator.call();
        }

        protected S next(S s, long j, Observer<Observable<? extends T>> observer) {
            return this.next.call(s, Long.valueOf(j), observer);
        }

        protected void onUnsubscribe(S s) {
            if (this.onUnsubscribe != null) {
                this.onUnsubscribe.call(s);
            }
        }
    }

    static final class AsyncOuterManager<S, T> implements Producer, Subscription, Observer<Observable<? extends T>> {
        private static final AtomicIntegerFieldUpdater<AsyncOuterManager> IS_UNSUBSCRIBED;
        Producer concatProducer;
        boolean emitting;
        long expectedDelivery;
        private boolean hasTerminated;
        private volatile int isUnsubscribed;
        private final UnicastSubject<Observable<T>> merger;
        private boolean onNextCalled;
        private final AsyncOnSubscribe<S, T> parent;
        List<Long> requests;
        private final SerializedObserver<Observable<? extends T>> serializedSubscriber;
        private S state;
        final CompositeSubscription subscriptions;

        /* renamed from: rx.observables.AsyncOnSubscribe.AsyncOuterManager.1 */
        class C14661 extends Subscriber<T> {
            long remaining;
            final /* synthetic */ BufferUntilSubscriber val$buffer;
            final /* synthetic */ long val$expected;

            C14661(long j, BufferUntilSubscriber bufferUntilSubscriber) {
                this.val$expected = j;
                this.val$buffer = bufferUntilSubscriber;
                this.remaining = this.val$expected;
            }

            public void onCompleted() {
                this.val$buffer.onCompleted();
                long j = this.remaining;
                if (j > 0) {
                    AsyncOuterManager.this.requestRemaining(j);
                }
            }

            public void onError(Throwable th) {
                this.val$buffer.onError(th);
            }

            public void onNext(T t) {
                this.remaining--;
                this.val$buffer.onNext(t);
            }
        }

        /* renamed from: rx.observables.AsyncOnSubscribe.AsyncOuterManager.2 */
        class C14672 implements Action0 {
            final /* synthetic */ Subscriber val$s;

            C14672(Subscriber subscriber) {
                this.val$s = subscriber;
            }

            public void call() {
                AsyncOuterManager.this.subscriptions.remove(this.val$s);
            }
        }

        static {
            IS_UNSUBSCRIBED = AtomicIntegerFieldUpdater.newUpdater(AsyncOuterManager.class, "isUnsubscribed");
        }

        public AsyncOuterManager(AsyncOnSubscribe<S, T> asyncOnSubscribe, S s, UnicastSubject<Observable<T>> unicastSubject) {
            this.subscriptions = new CompositeSubscription();
            this.parent = asyncOnSubscribe;
            this.serializedSubscriber = new SerializedObserver(this);
            this.state = s;
            this.merger = unicastSubject;
        }

        private void handleThrownError(Throwable th) {
            if (this.hasTerminated) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
                return;
            }
            this.hasTerminated = true;
            this.merger.onError(th);
            cleanup();
        }

        private void subscribeBufferToObservable(Observable<? extends T> observable) {
            BufferUntilSubscriber create = BufferUntilSubscriber.create();
            Subscriber c14661 = new C14661(this.expectedDelivery, create);
            this.subscriptions.add(c14661);
            observable.doOnTerminate(new C14672(c14661)).subscribe(c14661);
            this.merger.onNext(create);
        }

        void cleanup() {
            this.subscriptions.unsubscribe();
            try {
                this.parent.onUnsubscribe(this.state);
            } catch (Throwable th) {
                handleThrownError(th);
            }
        }

        public boolean isUnsubscribed() {
            return this.isUnsubscribed != 0;
        }

        public void nextIteration(long j) {
            this.state = this.parent.next(this.state, j, this.serializedSubscriber);
        }

        public void onCompleted() {
            if (this.hasTerminated) {
                throw new IllegalStateException("Terminal event already emitted.");
            }
            this.hasTerminated = true;
            this.merger.onCompleted();
        }

        public void onError(Throwable th) {
            if (this.hasTerminated) {
                throw new IllegalStateException("Terminal event already emitted.");
            }
            this.hasTerminated = true;
            this.merger.onError(th);
        }

        public void onNext(Observable<? extends T> observable) {
            if (this.onNextCalled) {
                throw new IllegalStateException("onNext called multiple times!");
            }
            this.onNextCalled = true;
            if (!this.hasTerminated) {
                subscribeBufferToObservable(observable);
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void request(long r8) {
            /*
            r7 = this;
            r4 = 0;
            r0 = 1;
            r1 = 0;
            r2 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1));
            if (r2 != 0) goto L_0x0009;
        L_0x0008:
            return;
        L_0x0009:
            r2 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1));
            if (r2 >= 0) goto L_0x0026;
        L_0x000d:
            r0 = new java.lang.IllegalStateException;
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "Request can't be negative! ";
            r1 = r1.append(r2);
            r1 = r1.append(r8);
            r1 = r1.toString();
            r0.<init>(r1);
            throw r0;
        L_0x0026:
            monitor-enter(r7);
            r2 = r7.emitting;	 Catch:{ all -> 0x005d }
            if (r2 == 0) goto L_0x0058;
        L_0x002b:
            r1 = r7.requests;	 Catch:{ all -> 0x005d }
            if (r1 != 0) goto L_0x0036;
        L_0x002f:
            r1 = new java.util.ArrayList;	 Catch:{ all -> 0x005d }
            r1.<init>();	 Catch:{ all -> 0x005d }
            r7.requests = r1;	 Catch:{ all -> 0x005d }
        L_0x0036:
            r2 = java.lang.Long.valueOf(r8);	 Catch:{ all -> 0x005d }
            r1.add(r2);	 Catch:{ all -> 0x005d }
        L_0x003d:
            monitor-exit(r7);	 Catch:{ all -> 0x005d }
            r1 = r7.concatProducer;
            r1.request(r8);
            if (r0 != 0) goto L_0x0008;
        L_0x0045:
            r0 = r7.tryEmit(r8);
            if (r0 != 0) goto L_0x0008;
        L_0x004b:
            monitor-enter(r7);
            r0 = r7.requests;	 Catch:{ all -> 0x0055 }
            if (r0 != 0) goto L_0x0060;
        L_0x0050:
            r0 = 0;
            r7.emitting = r0;	 Catch:{ all -> 0x0055 }
            monitor-exit(r7);	 Catch:{ all -> 0x0055 }
            goto L_0x0008;
        L_0x0055:
            r0 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x0055 }
            throw r0;
        L_0x0058:
            r0 = 1;
            r7.emitting = r0;	 Catch:{ all -> 0x005d }
            r0 = r1;
            goto L_0x003d;
        L_0x005d:
            r0 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x005d }
            throw r0;
        L_0x0060:
            r1 = 0;
            r7.requests = r1;	 Catch:{ all -> 0x0055 }
            monitor-exit(r7);	 Catch:{ all -> 0x0055 }
            r1 = r0.iterator();
        L_0x0068:
            r0 = r1.hasNext();
            if (r0 == 0) goto L_0x004b;
        L_0x006e:
            r0 = r1.next();
            r0 = (java.lang.Long) r0;
            r2 = r0.longValue();
            r0 = r7.tryEmit(r2);
            if (r0 == 0) goto L_0x0068;
        L_0x007e:
            goto L_0x0008;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.observables.AsyncOnSubscribe.AsyncOuterManager.request(long):void");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void requestRemaining(long r6) {
            /*
            r5 = this;
            r2 = 0;
            r0 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1));
            if (r0 != 0) goto L_0x0007;
        L_0x0006:
            return;
        L_0x0007:
            r0 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1));
            if (r0 >= 0) goto L_0x0024;
        L_0x000b:
            r0 = new java.lang.IllegalStateException;
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "Request can't be negative! ";
            r1 = r1.append(r2);
            r1 = r1.append(r6);
            r1 = r1.toString();
            r0.<init>(r1);
            throw r0;
        L_0x0024:
            monitor-enter(r5);
            r0 = r5.emitting;	 Catch:{ all -> 0x003d }
            if (r0 == 0) goto L_0x0040;
        L_0x0029:
            r0 = r5.requests;	 Catch:{ all -> 0x003d }
            if (r0 != 0) goto L_0x0034;
        L_0x002d:
            r0 = new java.util.ArrayList;	 Catch:{ all -> 0x003d }
            r0.<init>();	 Catch:{ all -> 0x003d }
            r5.requests = r0;	 Catch:{ all -> 0x003d }
        L_0x0034:
            r1 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x003d }
            r0.add(r1);	 Catch:{ all -> 0x003d }
            monitor-exit(r5);	 Catch:{ all -> 0x003d }
            goto L_0x0006;
        L_0x003d:
            r0 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x003d }
            throw r0;
        L_0x0040:
            r0 = 1;
            r5.emitting = r0;	 Catch:{ all -> 0x003d }
            monitor-exit(r5);	 Catch:{ all -> 0x003d }
            r0 = r5.tryEmit(r6);
            if (r0 != 0) goto L_0x0006;
        L_0x004a:
            monitor-enter(r5);
            r0 = r5.requests;	 Catch:{ all -> 0x0054 }
            if (r0 != 0) goto L_0x0057;
        L_0x004f:
            r0 = 0;
            r5.emitting = r0;	 Catch:{ all -> 0x0054 }
            monitor-exit(r5);	 Catch:{ all -> 0x0054 }
            goto L_0x0006;
        L_0x0054:
            r0 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x0054 }
            throw r0;
        L_0x0057:
            r1 = 0;
            r5.requests = r1;	 Catch:{ all -> 0x0054 }
            monitor-exit(r5);	 Catch:{ all -> 0x0054 }
            r1 = r0.iterator();
        L_0x005f:
            r0 = r1.hasNext();
            if (r0 == 0) goto L_0x004a;
        L_0x0065:
            r0 = r1.next();
            r0 = (java.lang.Long) r0;
            r2 = r0.longValue();
            r0 = r5.tryEmit(r2);
            if (r0 == 0) goto L_0x005f;
        L_0x0075:
            goto L_0x0006;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.observables.AsyncOnSubscribe.AsyncOuterManager.requestRemaining(long):void");
        }

        void setConcatProducer(Producer producer) {
            if (this.concatProducer != null) {
                throw new IllegalStateException("setConcatProducer may be called at most once!");
            }
            this.concatProducer = producer;
        }

        boolean tryEmit(long j) {
            if (isUnsubscribed()) {
                cleanup();
                return true;
            }
            try {
                this.onNextCalled = false;
                this.expectedDelivery = j;
                nextIteration(j);
                if (this.hasTerminated || isUnsubscribed()) {
                    cleanup();
                    return true;
                } else if (this.onNextCalled) {
                    return false;
                } else {
                    handleThrownError(new IllegalStateException("No events emitted!"));
                    return true;
                }
            } catch (Throwable th) {
                handleThrownError(th);
                return true;
            }
        }

        public void unsubscribe() {
            if (IS_UNSUBSCRIBED.compareAndSet(this, 0, 1)) {
                synchronized (this) {
                    if (this.emitting) {
                        this.requests = new ArrayList();
                        this.requests.add(Long.valueOf(0));
                        return;
                    }
                    this.emitting = true;
                    cleanup();
                }
            }
        }
    }

    static final class UnicastSubject<T> extends Observable<T> implements Observer<T> {
        private State<T> state;

        static final class State<T> implements OnSubscribe<T> {
            Subscriber<? super T> subscriber;

            State() {
            }

            public void call(Subscriber<? super T> subscriber) {
                synchronized (this) {
                    if (this.subscriber == null) {
                        this.subscriber = subscriber;
                        return;
                    }
                    subscriber.onError(new IllegalStateException("There can be only one subscriber"));
                }
            }
        }

        protected UnicastSubject(State<T> state) {
            super(state);
            this.state = state;
        }

        public static <T> UnicastSubject<T> create() {
            return new UnicastSubject(new State());
        }

        public void onCompleted() {
            this.state.subscriber.onCompleted();
        }

        public void onError(Throwable th) {
            this.state.subscriber.onError(th);
        }

        public void onNext(T t) {
            this.state.subscriber.onNext(t);
        }
    }

    @Experimental
    public static <S, T> AsyncOnSubscribe<S, T> createSingleState(Func0<? extends S> func0, Action3<? super S, Long, ? super Observer<Observable<? extends T>>> action3) {
        return new AsyncOnSubscribeImpl((Func0) func0, new C14591(action3));
    }

    @Experimental
    public static <S, T> AsyncOnSubscribe<S, T> createSingleState(Func0<? extends S> func0, Action3<? super S, Long, ? super Observer<Observable<? extends T>>> action3, Action1<? super S> action1) {
        return new AsyncOnSubscribeImpl(func0, new C14602(action3), action1);
    }

    @Experimental
    public static <S, T> AsyncOnSubscribe<S, T> createStateful(Func0<? extends S> func0, Func3<? super S, Long, ? super Observer<Observable<? extends T>>, ? extends S> func3) {
        return new AsyncOnSubscribeImpl((Func0) func0, (Func3) func3);
    }

    @Experimental
    public static <S, T> AsyncOnSubscribe<S, T> createStateful(Func0<? extends S> func0, Func3<? super S, Long, ? super Observer<Observable<? extends T>>, ? extends S> func3, Action1<? super S> action1) {
        return new AsyncOnSubscribeImpl(func0, func3, action1);
    }

    @Experimental
    public static <T> AsyncOnSubscribe<Void, T> createStateless(Action2<Long, ? super Observer<Observable<? extends T>>> action2) {
        return new AsyncOnSubscribeImpl(new C14613(action2));
    }

    @Experimental
    public static <T> AsyncOnSubscribe<Void, T> createStateless(Action2<Long, ? super Observer<Observable<? extends T>>> action2, Action0 action0) {
        return new AsyncOnSubscribeImpl(new C14624(action2), new C14635(action0));
    }

    public final void call(Subscriber<? super T> subscriber) {
        try {
            Object generateState = generateState();
            UnicastSubject create = UnicastSubject.create();
            Object asyncOuterManager = new AsyncOuterManager(this, generateState, create);
            Subscription c14646 = new C14646(subscriber, asyncOuterManager);
            create.onBackpressureBuffer().concatMap(new C14657()).unsafeSubscribe(c14646);
            subscriber.add(c14646);
            subscriber.add(asyncOuterManager);
            subscriber.setProducer(asyncOuterManager);
        } catch (Throwable th) {
            subscriber.onError(th);
        }
    }

    protected abstract S generateState();

    protected abstract S next(S s, long j, Observer<Observable<? extends T>> observer);

    protected void onUnsubscribe(S s) {
    }
}

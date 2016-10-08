package rx.observables;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.annotations.Experimental;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.functions.Func1;
import rx.internal.operators.BlockingOperatorLatest;
import rx.internal.operators.BlockingOperatorMostRecent;
import rx.internal.operators.BlockingOperatorNext;
import rx.internal.operators.BlockingOperatorToFuture;
import rx.internal.operators.BlockingOperatorToIterator;
import rx.internal.operators.NotificationLite;
import rx.internal.util.BlockingUtils;
import rx.internal.util.UtilityFunctions;

public final class BlockingObservable<T> {
    static final Object ON_START;
    static final Object SET_PRODUCER;
    static final Object UNSUBSCRIBE;
    private final Observable<? extends T> f917o;

    /* renamed from: rx.observables.BlockingObservable.1 */
    class C14681 extends Subscriber<T> {
        final /* synthetic */ AtomicReference val$exceptionFromOnError;
        final /* synthetic */ CountDownLatch val$latch;
        final /* synthetic */ Action1 val$onNext;

        C14681(CountDownLatch countDownLatch, AtomicReference atomicReference, Action1 action1) {
            this.val$latch = countDownLatch;
            this.val$exceptionFromOnError = atomicReference;
            this.val$onNext = action1;
        }

        public void onCompleted() {
            this.val$latch.countDown();
        }

        public void onError(Throwable th) {
            this.val$exceptionFromOnError.set(th);
            this.val$latch.countDown();
        }

        public void onNext(T t) {
            this.val$onNext.call(t);
        }
    }

    /* renamed from: rx.observables.BlockingObservable.2 */
    class C14692 implements Iterable<T> {
        C14692() {
        }

        public Iterator<T> iterator() {
            return BlockingObservable.this.getIterator();
        }
    }

    /* renamed from: rx.observables.BlockingObservable.3 */
    class C14703 extends Subscriber<T> {
        final /* synthetic */ CountDownLatch val$latch;
        final /* synthetic */ AtomicReference val$returnException;
        final /* synthetic */ AtomicReference val$returnItem;

        C14703(CountDownLatch countDownLatch, AtomicReference atomicReference, AtomicReference atomicReference2) {
            this.val$latch = countDownLatch;
            this.val$returnException = atomicReference;
            this.val$returnItem = atomicReference2;
        }

        public void onCompleted() {
            this.val$latch.countDown();
        }

        public void onError(Throwable th) {
            this.val$returnException.set(th);
            this.val$latch.countDown();
        }

        public void onNext(T t) {
            this.val$returnItem.set(t);
        }
    }

    /* renamed from: rx.observables.BlockingObservable.4 */
    class C14714 extends Subscriber<T> {
        final /* synthetic */ CountDownLatch val$cdl;
        final /* synthetic */ Throwable[] val$error;

        C14714(Throwable[] thArr, CountDownLatch countDownLatch) {
            this.val$error = thArr;
            this.val$cdl = countDownLatch;
        }

        public void onCompleted() {
            this.val$cdl.countDown();
        }

        public void onError(Throwable th) {
            this.val$error[0] = th;
            this.val$cdl.countDown();
        }

        public void onNext(T t) {
        }
    }

    /* renamed from: rx.observables.BlockingObservable.5 */
    class C14725 extends Subscriber<T> {
        final /* synthetic */ NotificationLite val$nl;
        final /* synthetic */ BlockingQueue val$queue;

        C14725(BlockingQueue blockingQueue, NotificationLite notificationLite) {
            this.val$queue = blockingQueue;
            this.val$nl = notificationLite;
        }

        public void onCompleted() {
            this.val$queue.offer(this.val$nl.completed());
        }

        public void onError(Throwable th) {
            this.val$queue.offer(this.val$nl.error(th));
        }

        public void onNext(T t) {
            this.val$queue.offer(this.val$nl.next(t));
        }
    }

    /* renamed from: rx.observables.BlockingObservable.6 */
    class C14736 extends Subscriber<T> {
        final /* synthetic */ NotificationLite val$nl;
        final /* synthetic */ BlockingQueue val$queue;
        final /* synthetic */ Producer[] val$theProducer;

        C14736(BlockingQueue blockingQueue, NotificationLite notificationLite, Producer[] producerArr) {
            this.val$queue = blockingQueue;
            this.val$nl = notificationLite;
            this.val$theProducer = producerArr;
        }

        public void onCompleted() {
            this.val$queue.offer(this.val$nl.completed());
        }

        public void onError(Throwable th) {
            this.val$queue.offer(this.val$nl.error(th));
        }

        public void onNext(T t) {
            this.val$queue.offer(this.val$nl.next(t));
        }

        public void onStart() {
            this.val$queue.offer(BlockingObservable.ON_START);
        }

        public void setProducer(Producer producer) {
            this.val$theProducer[0] = producer;
            this.val$queue.offer(BlockingObservable.SET_PRODUCER);
        }
    }

    /* renamed from: rx.observables.BlockingObservable.7 */
    class C14747 implements Action0 {
        final /* synthetic */ BlockingQueue val$queue;

        C14747(BlockingQueue blockingQueue) {
            this.val$queue = blockingQueue;
        }

        public void call() {
            this.val$queue.offer(BlockingObservable.UNSUBSCRIBE);
        }
    }

    /* renamed from: rx.observables.BlockingObservable.8 */
    class C14758 implements Action1<Throwable> {
        C14758() {
        }

        public void call(Throwable th) {
            throw new OnErrorNotImplementedException(th);
        }
    }

    /* renamed from: rx.observables.BlockingObservable.9 */
    class C14769 implements Observer<T> {
        final /* synthetic */ Action0 val$onCompleted;
        final /* synthetic */ Action1 val$onError;
        final /* synthetic */ Action1 val$onNext;

        C14769(Action1 action1, Action1 action12, Action0 action0) {
            this.val$onNext = action1;
            this.val$onError = action12;
            this.val$onCompleted = action0;
        }

        public void onCompleted() {
            this.val$onCompleted.call();
        }

        public void onError(Throwable th) {
            this.val$onError.call(th);
        }

        public void onNext(T t) {
            this.val$onNext.call(t);
        }
    }

    static {
        ON_START = new Object();
        SET_PRODUCER = new Object();
        UNSUBSCRIBE = new Object();
    }

    private BlockingObservable(Observable<? extends T> observable) {
        this.f917o = observable;
    }

    private T blockForSingle(Observable<? extends T> observable) {
        AtomicReference atomicReference = new AtomicReference();
        AtomicReference atomicReference2 = new AtomicReference();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        BlockingUtils.awaitForComplete(countDownLatch, observable.subscribe(new C14703(countDownLatch, atomicReference2, atomicReference)));
        if (atomicReference2.get() == null) {
            return atomicReference.get();
        }
        if (atomicReference2.get() instanceof RuntimeException) {
            throw ((RuntimeException) atomicReference2.get());
        }
        throw new RuntimeException((Throwable) atomicReference2.get());
    }

    public static <T> BlockingObservable<T> from(Observable<? extends T> observable) {
        return new BlockingObservable(observable);
    }

    public T first() {
        return blockForSingle(this.f917o.first());
    }

    public T first(Func1<? super T, Boolean> func1) {
        return blockForSingle(this.f917o.first(func1));
    }

    public T firstOrDefault(T t) {
        return blockForSingle(this.f917o.map(UtilityFunctions.identity()).firstOrDefault(t));
    }

    public T firstOrDefault(T t, Func1<? super T, Boolean> func1) {
        return blockForSingle(this.f917o.filter(func1).map(UtilityFunctions.identity()).firstOrDefault(t));
    }

    public void forEach(Action1<? super T> action1) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicReference atomicReference = new AtomicReference();
        BlockingUtils.awaitForComplete(countDownLatch, this.f917o.subscribe(new C14681(countDownLatch, atomicReference, action1)));
        if (atomicReference.get() == null) {
            return;
        }
        if (atomicReference.get() instanceof RuntimeException) {
            throw ((RuntimeException) atomicReference.get());
        }
        throw new RuntimeException((Throwable) atomicReference.get());
    }

    public Iterator<T> getIterator() {
        return BlockingOperatorToIterator.toIterator(this.f917o);
    }

    public T last() {
        return blockForSingle(this.f917o.last());
    }

    public T last(Func1<? super T, Boolean> func1) {
        return blockForSingle(this.f917o.last(func1));
    }

    public T lastOrDefault(T t) {
        return blockForSingle(this.f917o.map(UtilityFunctions.identity()).lastOrDefault(t));
    }

    public T lastOrDefault(T t, Func1<? super T, Boolean> func1) {
        return blockForSingle(this.f917o.filter(func1).map(UtilityFunctions.identity()).lastOrDefault(t));
    }

    public Iterable<T> latest() {
        return BlockingOperatorLatest.latest(this.f917o);
    }

    public Iterable<T> mostRecent(T t) {
        return BlockingOperatorMostRecent.mostRecent(this.f917o, t);
    }

    public Iterable<T> next() {
        return BlockingOperatorNext.next(this.f917o);
    }

    public T single() {
        return blockForSingle(this.f917o.single());
    }

    public T single(Func1<? super T, Boolean> func1) {
        return blockForSingle(this.f917o.single(func1));
    }

    public T singleOrDefault(T t) {
        return blockForSingle(this.f917o.map(UtilityFunctions.identity()).singleOrDefault(t));
    }

    public T singleOrDefault(T t, Func1<? super T, Boolean> func1) {
        return blockForSingle(this.f917o.filter(func1).map(UtilityFunctions.identity()).singleOrDefault(t));
    }

    @Experimental
    public void subscribe() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Throwable[] thArr = new Throwable[]{null};
        BlockingUtils.awaitForComplete(countDownLatch, this.f917o.subscribe(new C14714(thArr, countDownLatch)));
        Throwable th = thArr[0];
        if (th == null) {
            return;
        }
        if (th instanceof RuntimeException) {
            throw ((RuntimeException) th);
        }
        throw new RuntimeException(th);
    }

    @Experimental
    public void subscribe(Observer<? super T> observer) {
        NotificationLite instance = NotificationLite.instance();
        BlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
        Subscription subscribe = this.f917o.subscribe(new C14725(linkedBlockingQueue, instance));
        while (true) {
            try {
                Object poll = linkedBlockingQueue.poll();
                if (poll == null) {
                    poll = linkedBlockingQueue.take();
                }
                if (instance.accept(observer, poll)) {
                    break;
                }
            } catch (Throwable e) {
                Thread.currentThread().interrupt();
                observer.onError(e);
            } finally {
                subscribe.unsubscribe();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @rx.annotations.Experimental
    public void subscribe(rx.Subscriber<? super T> r7) {
        /*
        r6 = this;
        r4 = 0;
        r1 = rx.internal.operators.NotificationLite.instance();
        r2 = new java.util.concurrent.LinkedBlockingQueue;
        r2.<init>();
        r0 = 1;
        r3 = new rx.Producer[r0];
        r0 = 0;
        r3[r4] = r0;
        r4 = new rx.observables.BlockingObservable$6;
        r4.<init>(r2, r1, r3);
        r7.add(r4);
        r0 = new rx.observables.BlockingObservable$7;
        r0.<init>(r2);
        r0 = rx.subscriptions.Subscriptions.create(r0);
        r7.add(r0);
        r0 = r6.f917o;
        r0.subscribe(r4);
    L_0x0029:
        r0 = r7.isUnsubscribed();	 Catch:{ InterruptedException -> 0x004f }
        if (r0 == 0) goto L_0x0033;
    L_0x002f:
        r4.unsubscribe();
    L_0x0032:
        return;
    L_0x0033:
        r0 = r2.poll();	 Catch:{ InterruptedException -> 0x004f }
        if (r0 != 0) goto L_0x003d;
    L_0x0039:
        r0 = r2.take();	 Catch:{ InterruptedException -> 0x004f }
    L_0x003d:
        r5 = r7.isUnsubscribed();	 Catch:{ InterruptedException -> 0x004f }
        if (r5 != 0) goto L_0x002f;
    L_0x0043:
        r5 = UNSUBSCRIBE;	 Catch:{ InterruptedException -> 0x004f }
        if (r0 == r5) goto L_0x002f;
    L_0x0047:
        r5 = ON_START;	 Catch:{ InterruptedException -> 0x004f }
        if (r0 != r5) goto L_0x005e;
    L_0x004b:
        r7.onStart();	 Catch:{ InterruptedException -> 0x004f }
        goto L_0x0029;
    L_0x004f:
        r0 = move-exception;
        r1 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x0069 }
        r1.interrupt();	 Catch:{ all -> 0x0069 }
        r7.onError(r0);	 Catch:{ all -> 0x0069 }
        r4.unsubscribe();
        goto L_0x0032;
    L_0x005e:
        r5 = SET_PRODUCER;	 Catch:{ InterruptedException -> 0x004f }
        if (r0 != r5) goto L_0x006e;
    L_0x0062:
        r0 = 0;
        r0 = r3[r0];	 Catch:{ InterruptedException -> 0x004f }
        r7.setProducer(r0);	 Catch:{ InterruptedException -> 0x004f }
        goto L_0x0029;
    L_0x0069:
        r0 = move-exception;
        r4.unsubscribe();
        throw r0;
    L_0x006e:
        r0 = r1.accept(r7, r0);	 Catch:{ InterruptedException -> 0x004f }
        if (r0 == 0) goto L_0x0029;
    L_0x0074:
        r4.unsubscribe();
        goto L_0x0032;
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.observables.BlockingObservable.subscribe(rx.Subscriber):void");
    }

    @Experimental
    public void subscribe(Action1<? super T> action1) {
        subscribe(action1, new C14758(), Actions.empty());
    }

    @Experimental
    public void subscribe(Action1<? super T> action1, Action1<? super Throwable> action12) {
        subscribe(action1, action12, Actions.empty());
    }

    @Experimental
    public void subscribe(Action1<? super T> action1, Action1<? super Throwable> action12, Action0 action0) {
        subscribe(new C14769(action1, action12, action0));
    }

    public Future<T> toFuture() {
        return BlockingOperatorToFuture.toFuture(this.f917o);
    }

    public Iterable<T> toIterable() {
        return new C14692();
    }
}

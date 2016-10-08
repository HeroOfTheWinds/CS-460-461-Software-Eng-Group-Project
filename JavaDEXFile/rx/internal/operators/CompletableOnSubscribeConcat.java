package rx.internal.operators;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import rx.Completable;
import rx.Completable.CompletableOnSubscribe;
import rx.Completable.CompletableSubscriber;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.MissingBackpressureException;
import rx.internal.util.unsafe.SpscArrayQueue;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.SerialSubscription;

public final class CompletableOnSubscribeConcat implements CompletableOnSubscribe {
    final int prefetch;
    final Observable<Completable> sources;

    static final class CompletableConcatSubscriber extends Subscriber<Completable> {
        static final AtomicIntegerFieldUpdater<CompletableConcatSubscriber> ONCE;
        final CompletableSubscriber actual;
        volatile boolean done;
        final ConcatInnerSubscriber inner;
        volatile int once;
        final int prefetch;
        final SpscArrayQueue<Completable> queue;
        final SerialSubscription sr;
        final AtomicInteger wip;

        final class ConcatInnerSubscriber implements CompletableSubscriber {
            ConcatInnerSubscriber() {
            }

            public void onCompleted() {
                CompletableConcatSubscriber.this.innerComplete();
            }

            public void onError(Throwable th) {
                CompletableConcatSubscriber.this.innerError(th);
            }

            public void onSubscribe(Subscription subscription) {
                CompletableConcatSubscriber.this.sr.set(subscription);
            }
        }

        static {
            ONCE = AtomicIntegerFieldUpdater.newUpdater(CompletableConcatSubscriber.class, "once");
        }

        public CompletableConcatSubscriber(CompletableSubscriber completableSubscriber, int i) {
            this.actual = completableSubscriber;
            this.prefetch = i;
            this.queue = new SpscArrayQueue(i);
            this.sr = new SerialSubscription();
            this.inner = new ConcatInnerSubscriber();
            this.wip = new AtomicInteger();
            add(this.sr);
            request((long) i);
        }

        void innerComplete() {
            if (this.wip.decrementAndGet() != 0) {
                next();
            }
            if (!this.done) {
                request(1);
            }
        }

        void innerError(Throwable th) {
            unsubscribe();
            onError(th);
        }

        void next() {
            boolean z = this.done;
            Completable completable = (Completable) this.queue.poll();
            if (completable != null) {
                completable.subscribe(this.inner);
            } else if (!z) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(new IllegalStateException("Queue is empty?!"));
            } else if (ONCE.compareAndSet(this, 0, 1)) {
                this.actual.onCompleted();
            }
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                if (this.wip.getAndIncrement() == 0) {
                    next();
                }
            }
        }

        public void onError(Throwable th) {
            if (ONCE.compareAndSet(this, 0, 1)) {
                this.actual.onError(th);
            } else {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
            }
        }

        public void onNext(Completable completable) {
            if (!this.queue.offer(completable)) {
                onError(new MissingBackpressureException());
            } else if (this.wip.getAndIncrement() == 0) {
                next();
            }
        }
    }

    public CompletableOnSubscribeConcat(Observable<? extends Completable> observable, int i) {
        this.sources = observable;
        this.prefetch = i;
    }

    public void call(CompletableSubscriber completableSubscriber) {
        Subscriber completableConcatSubscriber = new CompletableConcatSubscriber(completableSubscriber, this.prefetch);
        completableSubscriber.onSubscribe(completableConcatSubscriber);
        this.sources.subscribe(completableConcatSubscriber);
    }
}

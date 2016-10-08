package rx.internal.operators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import rx.Completable;
import rx.Completable.CompletableOnSubscribe;
import rx.Completable.CompletableSubscriber;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.CompositeException;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.CompositeSubscription;

public final class CompletableOnSubscribeMerge implements CompletableOnSubscribe {
    final boolean delayErrors;
    final int maxConcurrency;
    final Observable<Completable> source;

    static final class CompletableMergeSubscriber extends Subscriber<Completable> {
        static final AtomicReferenceFieldUpdater<CompletableMergeSubscriber, Queue> ERRORS;
        static final AtomicIntegerFieldUpdater<CompletableMergeSubscriber> ONCE;
        final CompletableSubscriber actual;
        final boolean delayErrors;
        volatile boolean done;
        volatile Queue<Throwable> errors;
        final int maxConcurrency;
        volatile int once;
        final CompositeSubscription set;
        final AtomicInteger wip;

        /* renamed from: rx.internal.operators.CompletableOnSubscribeMerge.CompletableMergeSubscriber.1 */
        class C12711 implements CompletableSubscriber {
            Subscription f907d;
            boolean innerDone;

            C12711() {
            }

            public void onCompleted() {
                if (!this.innerDone) {
                    this.innerDone = true;
                    CompletableMergeSubscriber.this.set.remove(this.f907d);
                    CompletableMergeSubscriber.this.terminate();
                    if (!CompletableMergeSubscriber.this.done) {
                        CompletableMergeSubscriber.this.request(1);
                    }
                }
            }

            public void onError(Throwable th) {
                if (this.innerDone) {
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
                    return;
                }
                this.innerDone = true;
                CompletableMergeSubscriber.this.set.remove(this.f907d);
                CompletableMergeSubscriber.this.getOrCreateErrors().offer(th);
                CompletableMergeSubscriber.this.terminate();
                if (CompletableMergeSubscriber.this.delayErrors && !CompletableMergeSubscriber.this.done) {
                    CompletableMergeSubscriber.this.request(1);
                }
            }

            public void onSubscribe(Subscription subscription) {
                this.f907d = subscription;
                CompletableMergeSubscriber.this.set.add(subscription);
            }
        }

        static {
            ERRORS = AtomicReferenceFieldUpdater.newUpdater(CompletableMergeSubscriber.class, Queue.class, "errors");
            ONCE = AtomicIntegerFieldUpdater.newUpdater(CompletableMergeSubscriber.class, "once");
        }

        public CompletableMergeSubscriber(CompletableSubscriber completableSubscriber, int i, boolean z) {
            this.actual = completableSubscriber;
            this.maxConcurrency = i;
            this.delayErrors = z;
            this.set = new CompositeSubscription();
            this.wip = new AtomicInteger(1);
            if (i == Integer.MAX_VALUE) {
                request(Long.MAX_VALUE);
            } else {
                request((long) i);
            }
        }

        Queue<Throwable> getOrCreateErrors() {
            Queue<Throwable> queue = this.errors;
            if (queue != null) {
                return queue;
            }
            Queue concurrentLinkedQueue = new ConcurrentLinkedQueue();
            return !ERRORS.compareAndSet(this, null, concurrentLinkedQueue) ? this.errors : concurrentLinkedQueue;
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                terminate();
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
                return;
            }
            getOrCreateErrors().offer(th);
            this.done = true;
            terminate();
        }

        public void onNext(Completable completable) {
            if (!this.done) {
                this.wip.getAndIncrement();
                completable.subscribe(new C12711());
            }
        }

        void terminate() {
            Queue queue;
            Throwable collectErrors;
            if (this.wip.decrementAndGet() == 0) {
                queue = this.errors;
                if (queue == null || queue.isEmpty()) {
                    this.actual.onCompleted();
                    return;
                }
                collectErrors = CompletableOnSubscribeMerge.collectErrors(queue);
                if (ONCE.compareAndSet(this, 0, 1)) {
                    this.actual.onError(collectErrors);
                } else {
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(collectErrors);
                }
            } else if (!this.delayErrors) {
                queue = this.errors;
                if (queue != null && !queue.isEmpty()) {
                    collectErrors = CompletableOnSubscribeMerge.collectErrors(queue);
                    if (ONCE.compareAndSet(this, 0, 1)) {
                        this.actual.onError(collectErrors);
                    } else {
                        RxJavaPlugins.getInstance().getErrorHandler().handleError(collectErrors);
                    }
                }
            }
        }
    }

    public CompletableOnSubscribeMerge(Observable<? extends Completable> observable, int i, boolean z) {
        this.source = observable;
        this.maxConcurrency = i;
        this.delayErrors = z;
    }

    public static Throwable collectErrors(Queue<Throwable> queue) {
        Collection arrayList = new ArrayList();
        while (true) {
            Throwable th = (Throwable) queue.poll();
            if (th == null) {
                break;
            }
            arrayList.add(th);
        }
        return arrayList.isEmpty() ? null : arrayList.size() == 1 ? (Throwable) arrayList.get(0) : new CompositeException(arrayList);
    }

    public void call(CompletableSubscriber completableSubscriber) {
        Subscriber completableMergeSubscriber = new CompletableMergeSubscriber(completableSubscriber, this.maxConcurrency, this.delayErrors);
        completableSubscriber.onSubscribe(completableMergeSubscriber);
        this.source.subscribe(completableMergeSubscriber);
    }
}

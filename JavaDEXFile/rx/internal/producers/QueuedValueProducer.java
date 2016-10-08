package rx.internal.producers;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.internal.operators.BackpressureUtils;
import rx.internal.util.atomic.SpscLinkedAtomicQueue;
import rx.internal.util.unsafe.SpscLinkedQueue;
import rx.internal.util.unsafe.UnsafeAccess;

public final class QueuedValueProducer<T> extends AtomicLong implements Producer {
    static final Object NULL_SENTINEL;
    private static final long serialVersionUID = 7277121710709137047L;
    final Subscriber<? super T> child;
    final Queue<Object> queue;
    final AtomicInteger wip;

    static {
        NULL_SENTINEL = new Object();
    }

    public QueuedValueProducer(Subscriber<? super T> subscriber) {
        this(subscriber, UnsafeAccess.isUnsafeAvailable() ? new SpscLinkedQueue() : new SpscLinkedAtomicQueue());
    }

    public QueuedValueProducer(Subscriber<? super T> subscriber, Queue<Object> queue) {
        this.child = subscriber;
        this.queue = queue;
        this.wip = new AtomicInteger();
    }

    private void drain() {
        Object obj = null;
        if (this.wip.getAndIncrement() == 0) {
            Observer observer = this.child;
            Queue queue = this.queue;
            while (!observer.isUnsubscribed()) {
                this.wip.lazySet(1);
                long j = get();
                long j2 = 0;
                while (j != 0) {
                    Object poll = queue.poll();
                    if (poll == null) {
                        break;
                    }
                    try {
                        if (poll == NULL_SENTINEL) {
                            observer.onNext(null);
                        } else {
                            observer.onNext(poll);
                        }
                        if (!observer.isUnsubscribed()) {
                            j--;
                            j2++;
                        } else {
                            return;
                        }
                    } catch (Throwable th) {
                        if (poll != NULL_SENTINEL) {
                            obj = poll;
                        }
                        Exceptions.throwOrReport(th, observer, obj);
                        return;
                    }
                }
                if (!(j2 == 0 || get() == Long.MAX_VALUE)) {
                    addAndGet(-j2);
                }
                if (this.wip.decrementAndGet() == 0) {
                    return;
                }
            }
        }
    }

    public boolean offer(T t) {
        if (t != null ? !this.queue.offer(t) : !this.queue.offer(NULL_SENTINEL)) {
            return false;
        }
        drain();
        return true;
    }

    public void request(long j) {
        if (j < 0) {
            throw new IllegalArgumentException("n >= 0 required");
        } else if (j > 0) {
            BackpressureUtils.getAndAddRequest(this, j);
            drain();
        }
    }
}

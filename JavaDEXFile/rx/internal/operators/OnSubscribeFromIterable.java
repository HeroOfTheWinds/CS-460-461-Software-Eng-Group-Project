package rx.internal.operators;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable.OnSubscribe;
import rx.Producer;
import rx.Subscriber;

public final class OnSubscribeFromIterable<T> implements OnSubscribe<T> {
    final Iterable<? extends T> is;

    private static final class IterableProducer<T> extends AtomicLong implements Producer {
        private static final long serialVersionUID = -8730475647105475802L;
        private final Iterator<? extends T> it;
        private final Subscriber<? super T> f909o;

        IterableProducer(Subscriber<? super T> subscriber, Iterator<? extends T> it) {
            this.f909o = subscriber;
            this.it = it;
        }

        void fastpath() {
            Subscriber subscriber = this.f909o;
            Iterator it = this.it;
            while (!subscriber.isUnsubscribed()) {
                if (it.hasNext()) {
                    subscriber.onNext(it.next());
                } else if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                    return;
                } else {
                    return;
                }
            }
        }

        public void request(long j) {
            if (get() != Long.MAX_VALUE) {
                if (j == Long.MAX_VALUE && compareAndSet(0, Long.MAX_VALUE)) {
                    fastpath();
                } else if (j > 0 && BackpressureUtils.getAndAddRequest(this, j) == 0) {
                    slowpath(j);
                }
            }
        }

        void slowpath(long j) {
            Subscriber subscriber = this.f909o;
            Iterator it = this.it;
            do {
                long j2 = j;
                while (!subscriber.isUnsubscribed()) {
                    if (it.hasNext()) {
                        j2--;
                        if (j2 >= 0) {
                            subscriber.onNext(it.next());
                        } else {
                            j = addAndGet(-j);
                        }
                    } else if (!subscriber.isUnsubscribed()) {
                        subscriber.onCompleted();
                        return;
                    } else {
                        return;
                    }
                }
                return;
            } while (j != 0);
        }
    }

    public OnSubscribeFromIterable(Iterable<? extends T> iterable) {
        if (iterable == null) {
            throw new NullPointerException("iterable must not be null");
        }
        this.is = iterable;
    }

    public void call(Subscriber<? super T> subscriber) {
        Iterator it = this.is.iterator();
        if (it.hasNext() || subscriber.isUnsubscribed()) {
            subscriber.setProducer(new IterableProducer(subscriber, it));
        } else {
            subscriber.onCompleted();
        }
    }
}

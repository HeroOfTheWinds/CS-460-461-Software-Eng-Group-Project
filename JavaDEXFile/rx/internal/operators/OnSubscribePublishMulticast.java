package rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.MissingBackpressureException;
import rx.internal.util.atomic.SpscAtomicArrayQueue;
import rx.internal.util.unsafe.SpscArrayQueue;
import rx.internal.util.unsafe.UnsafeAccess;

public final class OnSubscribePublishMulticast<T> extends AtomicInteger implements OnSubscribe<T>, Observer<T>, Subscription {
    static final PublishProducer<?>[] EMPTY;
    static final PublishProducer<?>[] TERMINATED;
    private static final long serialVersionUID = -3741892510772238743L;
    final boolean delayError;
    volatile boolean done;
    Throwable error;
    final ParentSubscriber<T> parent;
    final int prefetch;
    volatile Producer producer;
    final Queue<T> queue;
    volatile PublishProducer<T>[] subscribers;

    static final class ParentSubscriber<T> extends Subscriber<T> {
        final OnSubscribePublishMulticast<T> state;

        public ParentSubscriber(OnSubscribePublishMulticast<T> onSubscribePublishMulticast) {
            this.state = onSubscribePublishMulticast;
        }

        public void onCompleted() {
            this.state.onCompleted();
        }

        public void onError(Throwable th) {
            this.state.onError(th);
        }

        public void onNext(T t) {
            this.state.onNext(t);
        }

        public void setProducer(Producer producer) {
            this.state.setProducer(producer);
        }
    }

    static final class PublishProducer<T> extends AtomicLong implements Producer, Subscription {
        private static final long serialVersionUID = 960704844171597367L;
        final Subscriber<? super T> actual;
        final AtomicBoolean once;
        final OnSubscribePublishMulticast<T> parent;

        public PublishProducer(Subscriber<? super T> subscriber, OnSubscribePublishMulticast<T> onSubscribePublishMulticast) {
            this.actual = subscriber;
            this.parent = onSubscribePublishMulticast;
            this.once = new AtomicBoolean();
        }

        public boolean isUnsubscribed() {
            return this.once.get();
        }

        public void request(long j) {
            if (j < 0) {
                throw new IllegalArgumentException("n >= 0 required but it was " + j);
            } else if (j != 0) {
                BackpressureUtils.getAndAddRequest(this, j);
                this.parent.drain();
            }
        }

        public void unsubscribe() {
            if (this.once.compareAndSet(false, true)) {
                this.parent.remove(this);
            }
        }
    }

    static {
        EMPTY = new PublishProducer[0];
        TERMINATED = new PublishProducer[0];
    }

    public OnSubscribePublishMulticast(int i, boolean z) {
        if (i <= 0) {
            throw new IllegalArgumentException("prefetch > 0 required but it was " + i);
        }
        this.prefetch = i;
        this.delayError = z;
        if (UnsafeAccess.isUnsafeAvailable()) {
            this.queue = new SpscArrayQueue(i);
        } else {
            this.queue = new SpscAtomicArrayQueue(i);
        }
        this.subscribers = EMPTY;
        this.parent = new ParentSubscriber(this);
    }

    boolean add(PublishProducer<T> publishProducer) {
        if (this.subscribers == TERMINATED) {
            return false;
        }
        synchronized (this) {
            Object obj = this.subscribers;
            if (obj == TERMINATED) {
                return false;
            }
            int length = obj.length;
            Object obj2 = new PublishProducer[(length + 1)];
            System.arraycopy(obj, 0, obj2, 0, length);
            obj2[length] = publishProducer;
            this.subscribers = obj2;
            return true;
        }
    }

    public void call(Subscriber<? super T> subscriber) {
        PublishProducer publishProducer = new PublishProducer(subscriber, this);
        subscriber.add(publishProducer);
        subscriber.setProducer(publishProducer);
        if (!add(publishProducer)) {
            Throwable th = this.error;
            if (th != null) {
                subscriber.onError(th);
            } else {
                subscriber.onCompleted();
            }
        } else if (publishProducer.isUnsubscribed()) {
            remove(publishProducer);
        } else {
            drain();
        }
    }

    boolean checkTerminated(boolean z, boolean z2) {
        int i = 0;
        if (z) {
            int length;
            PublishProducer[] terminate;
            int length2;
            if (!this.delayError) {
                Throwable th = this.error;
                if (th != null) {
                    this.queue.clear();
                    PublishProducer[] terminate2 = terminate();
                    length = terminate2.length;
                    while (i < length) {
                        terminate2[i].actual.onError(th);
                        i++;
                    }
                    return true;
                } else if (z2) {
                    terminate = terminate();
                    length2 = terminate.length;
                    while (i < length2) {
                        terminate[i].actual.onCompleted();
                        i++;
                    }
                    return true;
                }
            } else if (z2) {
                terminate = terminate();
                Throwable th2 = this.error;
                if (th2 != null) {
                    length = terminate.length;
                    while (i < length) {
                        terminate[i].actual.onError(th2);
                        i++;
                    }
                    return true;
                }
                length2 = terminate.length;
                while (i < length2) {
                    terminate[i].actual.onCompleted();
                    i++;
                }
                return true;
            }
        }
        return false;
    }

    void drain() {
        if (getAndIncrement() == 0) {
            Queue queue = this.queue;
            int i = 0;
            do {
                PublishProducer[] publishProducerArr = this.subscribers;
                int length = publishProducerArr.length;
                int length2 = publishProducerArr.length;
                long j = Long.MAX_VALUE;
                int i2 = 0;
                while (i2 < length2) {
                    i2++;
                    j = Math.min(j, publishProducerArr[i2].get());
                }
                if (length != 0) {
                    long j2 = 0;
                    while (j2 != j) {
                        boolean z = this.done;
                        Object poll = queue.poll();
                        boolean z2 = poll == null;
                        if (!checkTerminated(z, z2)) {
                            if (z2) {
                                break;
                            }
                            for (PublishProducer publishProducer : publishProducerArr) {
                                publishProducer.actual.onNext(poll);
                            }
                            j2 = 1 + j2;
                        } else {
                            return;
                        }
                    }
                    if (j2 != j || !checkTerminated(this.done, queue.isEmpty())) {
                        if (j2 != 0) {
                            Producer producer = this.producer;
                            if (producer != null) {
                                producer.request(j2);
                            }
                            for (AtomicLong produced : publishProducerArr) {
                                BackpressureUtils.produced(produced, j2);
                            }
                        }
                    } else {
                        return;
                    }
                }
                i = addAndGet(-i);
            } while (i != 0);
        }
    }

    public boolean isUnsubscribed() {
        return this.parent.isUnsubscribed();
    }

    public void onCompleted() {
        this.done = true;
        drain();
    }

    public void onError(Throwable th) {
        this.error = th;
        this.done = true;
        drain();
    }

    public void onNext(T t) {
        if (!this.queue.offer(t)) {
            this.parent.unsubscribe();
            this.error = new MissingBackpressureException("Queue full?!");
            this.done = true;
        }
        drain();
    }

    void remove(PublishProducer<T> publishProducer) {
        PublishProducer[] publishProducerArr = this.subscribers;
        if (publishProducerArr != TERMINATED && publishProducerArr != EMPTY) {
            synchronized (this) {
                Object obj = this.subscribers;
                if (obj == TERMINATED || obj == EMPTY) {
                    return;
                }
                int i = -1;
                int length = obj.length;
                for (int i2 = 0; i2 < length; i2++) {
                    if (obj[i2] == publishProducer) {
                        i = i2;
                        break;
                    }
                }
                if (i < 0) {
                    return;
                }
                PublishProducer[] publishProducerArr2;
                if (length == 1) {
                    publishProducerArr2 = EMPTY;
                } else {
                    publishProducerArr2 = new PublishProducer[(length - 1)];
                    System.arraycopy(obj, 0, publishProducerArr2, 0, i);
                    System.arraycopy(obj, i + 1, publishProducerArr2, i, (length - i) - 1);
                }
                this.subscribers = publishProducerArr2;
            }
        }
    }

    void setProducer(Producer producer) {
        this.producer = producer;
        producer.request((long) this.prefetch);
    }

    public Subscriber<T> subscriber() {
        return this.parent;
    }

    PublishProducer<T>[] terminate() {
        PublishProducer<T>[] publishProducerArr = this.subscribers;
        if (publishProducerArr == TERMINATED) {
            return publishProducerArr;
        }
        PublishProducer<T>[] publishProducerArr2;
        synchronized (this) {
            publishProducerArr2 = this.subscribers;
            if (publishProducerArr2 != TERMINATED) {
                this.subscribers = TERMINATED;
            }
        }
        return publishProducerArr2;
    }

    public void unsubscribe() {
        this.parent.unsubscribe();
    }
}

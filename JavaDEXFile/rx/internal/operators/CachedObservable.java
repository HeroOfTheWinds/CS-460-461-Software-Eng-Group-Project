package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.internal.util.LinkedArrayList;
import rx.subscriptions.SerialSubscription;

public final class CachedObservable<T> extends Observable<T> {
    private final CacheState<T> state;

    static final class CacheState<T> extends LinkedArrayList implements Observer<T> {
        static final ReplayProducer<?>[] EMPTY;
        final SerialSubscription connection;
        volatile boolean isConnected;
        final NotificationLite<T> nl;
        volatile ReplayProducer<?>[] producers;
        final Observable<? extends T> source;
        boolean sourceDone;

        /* renamed from: rx.internal.operators.CachedObservable.CacheState.1 */
        class C12701 extends Subscriber<T> {
            C12701() {
            }

            public void onCompleted() {
                CacheState.this.onCompleted();
            }

            public void onError(Throwable th) {
                CacheState.this.onError(th);
            }

            public void onNext(T t) {
                CacheState.this.onNext(t);
            }
        }

        static {
            EMPTY = new ReplayProducer[0];
        }

        public CacheState(Observable<? extends T> observable, int i) {
            super(i);
            this.source = observable;
            this.producers = EMPTY;
            this.nl = NotificationLite.instance();
            this.connection = new SerialSubscription();
        }

        public void addProducer(ReplayProducer<T> replayProducer) {
            synchronized (this.connection) {
                Object obj = this.producers;
                int length = obj.length;
                Object obj2 = new ReplayProducer[(length + 1)];
                System.arraycopy(obj, 0, obj2, 0, length);
                obj2[length] = replayProducer;
                this.producers = obj2;
            }
        }

        public void connect() {
            Object c12701 = new C12701();
            this.connection.set(c12701);
            this.source.unsafeSubscribe(c12701);
            this.isConnected = true;
        }

        void dispatch() {
            for (ReplayProducer replay : this.producers) {
                replay.replay();
            }
        }

        public void onCompleted() {
            if (!this.sourceDone) {
                this.sourceDone = true;
                add(this.nl.completed());
                this.connection.unsubscribe();
                dispatch();
            }
        }

        public void onError(Throwable th) {
            if (!this.sourceDone) {
                this.sourceDone = true;
                add(this.nl.error(th));
                this.connection.unsubscribe();
                dispatch();
            }
        }

        public void onNext(T t) {
            if (!this.sourceDone) {
                add(this.nl.next(t));
                dispatch();
            }
        }

        public void removeProducer(ReplayProducer<T> replayProducer) {
            int i = 0;
            synchronized (this.connection) {
                Object obj = this.producers;
                int length = obj.length;
                while (i < length) {
                    if (obj[i].equals(replayProducer)) {
                        break;
                    }
                    i++;
                }
                i = -1;
                if (i < 0) {
                } else if (length == 1) {
                    this.producers = EMPTY;
                } else {
                    Object obj2 = new ReplayProducer[(length - 1)];
                    System.arraycopy(obj, 0, obj2, 0, i);
                    System.arraycopy(obj, i + 1, obj2, i, (length - i) - 1);
                    this.producers = obj2;
                }
            }
        }
    }

    static final class CachedSubscribe<T> extends AtomicBoolean implements OnSubscribe<T> {
        private static final long serialVersionUID = -2817751667698696782L;
        final CacheState<T> state;

        public CachedSubscribe(CacheState<T> cacheState) {
            this.state = cacheState;
        }

        public void call(Subscriber<? super T> subscriber) {
            Object replayProducer = new ReplayProducer(subscriber, this.state);
            this.state.addProducer(replayProducer);
            subscriber.add(replayProducer);
            subscriber.setProducer(replayProducer);
            if (!get() && compareAndSet(false, true)) {
                this.state.connect();
            }
        }
    }

    static final class ReplayProducer<T> extends AtomicLong implements Producer, Subscription {
        private static final long serialVersionUID = -2557562030197141021L;
        final Subscriber<? super T> child;
        Object[] currentBuffer;
        int currentIndexInBuffer;
        boolean emitting;
        int index;
        boolean missed;
        final CacheState<T> state;

        public ReplayProducer(Subscriber<? super T> subscriber, CacheState<T> cacheState) {
            this.child = subscriber;
            this.state = cacheState;
        }

        public boolean isUnsubscribed() {
            return get() < 0;
        }

        public long produced(long j) {
            return addAndGet(-j);
        }

        public void replay() {
            Object obj;
            Throwable th;
            synchronized (this) {
                if (this.emitting) {
                    this.missed = true;
                    return;
                }
                this.emitting = true;
                NotificationLite notificationLite = this.state.nl;
                Subscriber subscriber = this.child;
                while (true) {
                    long j = get();
                    if (j >= 0) {
                        int size = this.state.size();
                        if (size != 0) {
                            Object[] objArr = this.currentBuffer;
                            if (objArr == null) {
                                objArr = this.state.head();
                                this.currentBuffer = objArr;
                            }
                            int length = objArr.length - 1;
                            int i = this.index;
                            int i2 = this.currentIndexInBuffer;
                            if (j == 0) {
                                Object obj2 = objArr[i2];
                                if (notificationLite.isCompleted(obj2)) {
                                    subscriber.onCompleted();
                                    obj = 1;
                                    try {
                                        unsubscribe();
                                        return;
                                    } catch (Throwable th2) {
                                        th = th2;
                                    }
                                } else {
                                    try {
                                        if (notificationLite.isError(obj2)) {
                                            subscriber.onError(notificationLite.getError(obj2));
                                            obj = 1;
                                            unsubscribe();
                                            return;
                                        }
                                    } catch (Throwable th3) {
                                        th = th3;
                                        obj = null;
                                    }
                                }
                            } else if (j > 0) {
                                int i3 = i;
                                i = i2;
                                i2 = 0;
                                int i4 = i3;
                                while (i4 < size && j > 0) {
                                    if (!subscriber.isUnsubscribed()) {
                                        if (i == length) {
                                            objArr = (Object[]) objArr[length];
                                            i = 0;
                                        }
                                        Object obj3 = objArr[i];
                                        try {
                                            if (notificationLite.accept(subscriber, obj3)) {
                                                obj = 1;
                                                try {
                                                    unsubscribe();
                                                    return;
                                                } catch (Throwable th4) {
                                                    th = th4;
                                                    i = 1;
                                                    if (obj == null) {
                                                        synchronized (this) {
                                                            this.emitting = false;
                                                        }
                                                    }
                                                    throw th;
                                                }
                                            }
                                            int i5 = i + 1;
                                            j--;
                                            i2++;
                                            i4++;
                                            i = i5;
                                        } catch (Throwable th5) {
                                            th = th5;
                                            obj = null;
                                        }
                                    } else {
                                        return;
                                    }
                                }
                                if (!subscriber.isUnsubscribed()) {
                                    this.index = i4;
                                    this.currentIndexInBuffer = i;
                                    this.currentBuffer = objArr;
                                    produced((long) i2);
                                } else {
                                    return;
                                }
                            }
                        }
                        synchronized (this) {
                            try {
                                if (this.missed) {
                                    this.missed = false;
                                } else {
                                    this.emitting = false;
                                    obj = 1;
                                    try {
                                        return;
                                    } catch (Throwable th6) {
                                        th = th6;
                                        throw th;
                                    }
                                }
                            } catch (Throwable th7) {
                                th = th7;
                                obj = null;
                            }
                        }
                    } else {
                        return;
                    }
                }
            }
        }

        public void request(long j) {
            long j2;
            long j3;
            do {
                j2 = get();
                if (j2 >= 0) {
                    j3 = j2 + j;
                    if (j3 < 0) {
                        j3 = Long.MAX_VALUE;
                    }
                } else {
                    return;
                }
            } while (!compareAndSet(j2, j3));
            replay();
        }

        public void unsubscribe() {
            if (get() >= 0 && getAndSet(-1) >= 0) {
                this.state.removeProducer(this);
            }
        }
    }

    private CachedObservable(OnSubscribe<T> onSubscribe, CacheState<T> cacheState) {
        super(onSubscribe);
        this.state = cacheState;
    }

    public static <T> CachedObservable<T> from(Observable<? extends T> observable) {
        return from(observable, 16);
    }

    public static <T> CachedObservable<T> from(Observable<? extends T> observable, int i) {
        if (i < 1) {
            throw new IllegalArgumentException("capacityHint > 0 required");
        }
        CacheState cacheState = new CacheState(observable, i);
        return new CachedObservable(new CachedSubscribe(cacheState), cacheState);
    }

    boolean hasObservers() {
        return this.state.producers.length != 0;
    }

    boolean isConnected() {
        return this.state.isConnected;
    }
}

package rx.internal.operators;

import java.util.concurrent.atomic.AtomicLong;
import rx.Notification;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.plugins.RxJavaPlugins;

public final class OperatorMaterialize<T> implements Operator<Notification<T>, T> {

    /* renamed from: rx.internal.operators.OperatorMaterialize.1 */
    class C13431 implements Producer {
        final /* synthetic */ ParentSubscriber val$parent;

        C13431(ParentSubscriber parentSubscriber) {
            this.val$parent = parentSubscriber;
        }

        public void request(long j) {
            if (j > 0) {
                this.val$parent.requestMore(j);
            }
        }
    }

    private static final class Holder {
        static final OperatorMaterialize<Object> INSTANCE;

        static {
            INSTANCE = new OperatorMaterialize();
        }

        private Holder() {
        }
    }

    private static class ParentSubscriber<T> extends Subscriber<T> {
        private boolean busy;
        private final Subscriber<? super Notification<T>> child;
        private boolean missed;
        private final AtomicLong requested;
        private volatile Notification<T> terminalNotification;

        ParentSubscriber(Subscriber<? super Notification<T>> subscriber) {
            this.busy = false;
            this.missed = false;
            this.requested = new AtomicLong();
            this.child = subscriber;
        }

        private void decrementRequested() {
            AtomicLong atomicLong = this.requested;
            long j;
            do {
                j = atomicLong.get();
                if (j == Long.MAX_VALUE) {
                    return;
                }
            } while (!atomicLong.compareAndSet(j, j - 1));
        }

        private void drain() {
            synchronized (this) {
                if (this.busy) {
                    this.missed = true;
                    return;
                }
                AtomicLong atomicLong = this.requested;
                while (!this.child.isUnsubscribed()) {
                    Notification notification = this.terminalNotification;
                    if (notification == null || atomicLong.get() <= 0) {
                        synchronized (this) {
                            if (this.missed) {
                            } else {
                                this.busy = false;
                                return;
                            }
                        }
                    }
                    this.terminalNotification = null;
                    this.child.onNext(notification);
                    if (!this.child.isUnsubscribed()) {
                        this.child.onCompleted();
                        return;
                    }
                    return;
                }
            }
        }

        public void onCompleted() {
            this.terminalNotification = Notification.createOnCompleted();
            drain();
        }

        public void onError(Throwable th) {
            this.terminalNotification = Notification.createOnError(th);
            RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
            drain();
        }

        public void onNext(T t) {
            this.child.onNext(Notification.createOnNext(t));
            decrementRequested();
        }

        public void onStart() {
            request(0);
        }

        void requestMore(long j) {
            BackpressureUtils.getAndAddRequest(this.requested, j);
            request(j);
            drain();
        }
    }

    OperatorMaterialize() {
    }

    public static <T> OperatorMaterialize<T> instance() {
        return Holder.INSTANCE;
    }

    public Subscriber<? super T> call(Subscriber<? super Notification<T>> subscriber) {
        Subscriber<? super T> parentSubscriber = new ParentSubscriber(subscriber);
        subscriber.add(parentSubscriber);
        subscriber.setProducer(new C13431(parentSubscriber));
        return parentSubscriber;
    }
}

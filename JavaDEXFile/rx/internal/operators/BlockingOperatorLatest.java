package rx.internal.operators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;

public final class BlockingOperatorLatest {

    /* renamed from: rx.internal.operators.BlockingOperatorLatest.1 */
    static final class C12621 implements Iterable<T> {
        final /* synthetic */ Observable val$source;

        C12621(Observable observable) {
            this.val$source = observable;
        }

        public Iterator<T> iterator() {
            Subscriber latestObserverIterator = new LatestObserverIterator();
            this.val$source.materialize().subscribe(latestObserverIterator);
            return latestObserverIterator;
        }
    }

    static final class LatestObserverIterator<T> extends Subscriber<Notification<? extends T>> implements Iterator<T> {
        Notification<? extends T> iNotif;
        final Semaphore notify;
        final AtomicReference<Notification<? extends T>> value;

        LatestObserverIterator() {
            this.notify = new Semaphore(0);
            this.value = new AtomicReference();
        }

        public boolean hasNext() {
            if (this.iNotif == null || !this.iNotif.isOnError()) {
                if ((this.iNotif == null || !this.iNotif.isOnCompleted()) && this.iNotif == null) {
                    try {
                        this.notify.acquire();
                        this.iNotif = (Notification) this.value.getAndSet(null);
                        if (this.iNotif.isOnError()) {
                            throw Exceptions.propagate(this.iNotif.getThrowable());
                        }
                    } catch (Throwable e) {
                        unsubscribe();
                        Thread.currentThread().interrupt();
                        this.iNotif = Notification.createOnError(e);
                        throw Exceptions.propagate(e);
                    }
                }
                return !this.iNotif.isOnCompleted();
            } else {
                throw Exceptions.propagate(this.iNotif.getThrowable());
            }
        }

        public T next() {
            if (hasNext() && this.iNotif.isOnNext()) {
                T value = this.iNotif.getValue();
                this.iNotif = null;
                return value;
            }
            throw new NoSuchElementException();
        }

        public void onCompleted() {
        }

        public void onError(Throwable th) {
        }

        public void onNext(Notification<? extends T> notification) {
            if ((this.value.getAndSet(notification) == null ? 1 : null) != null) {
                this.notify.release();
            }
        }

        public void remove() {
            throw new UnsupportedOperationException("Read-only iterator.");
        }
    }

    private BlockingOperatorLatest() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Iterable<T> latest(Observable<? extends T> observable) {
        return new C12621(observable);
    }
}

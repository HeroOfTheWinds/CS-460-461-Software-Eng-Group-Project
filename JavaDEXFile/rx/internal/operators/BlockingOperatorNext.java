package rx.internal.operators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;

public final class BlockingOperatorNext {

    /* renamed from: rx.internal.operators.BlockingOperatorNext.1 */
    static final class C12651 implements Iterable<T> {
        final /* synthetic */ Observable val$items;

        C12651(Observable observable) {
            this.val$items = observable;
        }

        public Iterator<T> iterator() {
            return new NextIterator(this.val$items, new NextObserver());
        }
    }

    static final class NextIterator<T> implements Iterator<T> {
        private Throwable error;
        private boolean hasNext;
        private boolean isNextConsumed;
        private final Observable<? extends T> items;
        private T next;
        private final NextObserver<T> observer;
        private boolean started;

        NextIterator(Observable<? extends T> observable, NextObserver<T> nextObserver) {
            this.hasNext = true;
            this.isNextConsumed = true;
            this.error = null;
            this.started = false;
            this.items = observable;
            this.observer = nextObserver;
        }

        private boolean moveToNext() {
            try {
                if (!this.started) {
                    this.started = true;
                    this.observer.setWaiting(1);
                    this.items.materialize().subscribe(this.observer);
                }
                Notification takeNext = this.observer.takeNext();
                if (takeNext.isOnNext()) {
                    this.isNextConsumed = false;
                    this.next = takeNext.getValue();
                    return true;
                }
                this.hasNext = false;
                if (takeNext.isOnCompleted()) {
                    return false;
                }
                if (takeNext.isOnError()) {
                    this.error = takeNext.getThrowable();
                    throw Exceptions.propagate(this.error);
                }
                throw new IllegalStateException("Should not reach here");
            } catch (Throwable e) {
                this.observer.unsubscribe();
                Thread.currentThread().interrupt();
                this.error = e;
                throw Exceptions.propagate(this.error);
            }
        }

        public boolean hasNext() {
            if (this.error == null) {
                return !this.hasNext ? false : !this.isNextConsumed ? true : moveToNext();
            } else {
                throw Exceptions.propagate(this.error);
            }
        }

        public T next() {
            if (this.error != null) {
                throw Exceptions.propagate(this.error);
            } else if (hasNext()) {
                this.isNextConsumed = true;
                return this.next;
            } else {
                throw new NoSuchElementException("No more elements");
            }
        }

        public void remove() {
            throw new UnsupportedOperationException("Read only iterator");
        }
    }

    private static class NextObserver<T> extends Subscriber<Notification<? extends T>> {
        private final BlockingQueue<Notification<? extends T>> buf;
        final AtomicInteger waiting;

        NextObserver() {
            this.buf = new ArrayBlockingQueue(1);
            this.waiting = new AtomicInteger();
        }

        public void onCompleted() {
        }

        public void onError(Throwable th) {
        }

        public void onNext(Notification<? extends T> notification) {
            if (this.waiting.getAndSet(0) == 1 || !notification.isOnNext()) {
                Object obj;
                while (!this.buf.offer(obj)) {
                    Notification notification2 = (Notification) this.buf.poll();
                    if (!(notification2 == null || notification2.isOnNext())) {
                        obj = notification2;
                    }
                }
            }
        }

        void setWaiting(int i) {
            this.waiting.set(i);
        }

        public Notification<? extends T> takeNext() throws InterruptedException {
            setWaiting(1);
            return (Notification) this.buf.take();
        }
    }

    private BlockingOperatorNext() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Iterable<T> next(Observable<? extends T> observable) {
        return new C12651(observable);
    }
}

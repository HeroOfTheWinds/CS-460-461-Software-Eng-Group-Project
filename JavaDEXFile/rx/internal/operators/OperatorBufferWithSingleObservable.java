package rx.internal.operators;

import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Observable.Operator;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Func0;
import rx.observers.SerializedSubscriber;
import rx.observers.Subscribers;

public final class OperatorBufferWithSingleObservable<T, TClosing> implements Operator<List<T>, T> {
    final Func0<? extends Observable<? extends TClosing>> bufferClosingSelector;
    final int initialCapacity;

    /* renamed from: rx.internal.operators.OperatorBufferWithSingleObservable.1 */
    class C13071 implements Func0<Observable<? extends TClosing>> {
        final /* synthetic */ Observable val$bufferClosing;

        C13071(Observable observable) {
            this.val$bufferClosing = observable;
        }

        public Observable<? extends TClosing> call() {
            return this.val$bufferClosing;
        }
    }

    /* renamed from: rx.internal.operators.OperatorBufferWithSingleObservable.2 */
    class C13082 extends Subscriber<TClosing> {
        final /* synthetic */ BufferingSubscriber val$bsub;

        C13082(BufferingSubscriber bufferingSubscriber) {
            this.val$bsub = bufferingSubscriber;
        }

        public void onCompleted() {
            this.val$bsub.onCompleted();
        }

        public void onError(Throwable th) {
            this.val$bsub.onError(th);
        }

        public void onNext(TClosing tClosing) {
            this.val$bsub.emit();
        }
    }

    final class BufferingSubscriber extends Subscriber<T> {
        final Subscriber<? super List<T>> child;
        List<T> chunk;
        boolean done;

        public BufferingSubscriber(Subscriber<? super List<T>> subscriber) {
            this.child = subscriber;
            this.chunk = new ArrayList(OperatorBufferWithSingleObservable.this.initialCapacity);
        }

        void emit() {
            synchronized (this) {
                if (this.done) {
                    return;
                }
                List list = this.chunk;
                this.chunk = new ArrayList(OperatorBufferWithSingleObservable.this.initialCapacity);
                try {
                    this.child.onNext(list);
                } catch (Throwable th) {
                    unsubscribe();
                    synchronized (this) {
                    }
                    if (!this.done) {
                        this.done = true;
                        Exceptions.throwOrReport(th, this.child);
                    }
                }
            }
        }

        public void onCompleted() {
            try {
                synchronized (this) {
                    if (this.done) {
                        return;
                    }
                    this.done = true;
                    List list = this.chunk;
                    this.chunk = null;
                    this.child.onNext(list);
                    this.child.onCompleted();
                    unsubscribe();
                }
            } catch (Throwable th) {
                Exceptions.throwOrReport(th, this.child);
            }
        }

        public void onError(Throwable th) {
            synchronized (this) {
                if (this.done) {
                    return;
                }
                this.done = true;
                this.chunk = null;
                this.child.onError(th);
                unsubscribe();
            }
        }

        public void onNext(T t) {
            synchronized (this) {
                if (this.done) {
                    return;
                }
                this.chunk.add(t);
            }
        }
    }

    public OperatorBufferWithSingleObservable(Observable<? extends TClosing> observable, int i) {
        this.bufferClosingSelector = new C13071(observable);
        this.initialCapacity = i;
    }

    public OperatorBufferWithSingleObservable(Func0<? extends Observable<? extends TClosing>> func0, int i) {
        this.bufferClosingSelector = func0;
        this.initialCapacity = i;
    }

    public Subscriber<? super T> call(Subscriber<? super List<T>> subscriber) {
        try {
            Observable observable = (Observable) this.bufferClosingSelector.call();
            Subscription bufferingSubscriber = new BufferingSubscriber(new SerializedSubscriber(subscriber));
            Object c13082 = new C13082(bufferingSubscriber);
            subscriber.add(c13082);
            subscriber.add(bufferingSubscriber);
            observable.unsafeSubscribe(c13082);
            return bufferingSubscriber;
        } catch (Throwable th) {
            Exceptions.throwOrReport(th, (Observer) subscriber);
            return Subscribers.empty();
        }
    }
}

package rx.internal.operators;

import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Observable.Operator;
import rx.Observer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func2;
import rx.observers.SerializedSubscriber;

public final class OperatorWithLatestFrom<T, U, R> implements Operator<R, T> {
    static final Object EMPTY;
    final Observable<? extends U> other;
    final Func2<? super T, ? super U, ? extends R> resultSelector;

    /* renamed from: rx.internal.operators.OperatorWithLatestFrom.1 */
    class C14321 extends Subscriber<T> {
        final /* synthetic */ AtomicReference val$current;
        final /* synthetic */ SerializedSubscriber val$s;

        C14321(Subscriber subscriber, boolean z, AtomicReference atomicReference, SerializedSubscriber serializedSubscriber) {
            this.val$current = atomicReference;
            this.val$s = serializedSubscriber;
            super(subscriber, z);
        }

        public void onCompleted() {
            this.val$s.onCompleted();
            this.val$s.unsubscribe();
        }

        public void onError(Throwable th) {
            this.val$s.onError(th);
            this.val$s.unsubscribe();
        }

        public void onNext(T t) {
            Object obj = this.val$current.get();
            if (obj != OperatorWithLatestFrom.EMPTY) {
                try {
                    this.val$s.onNext(OperatorWithLatestFrom.this.resultSelector.call(t, obj));
                } catch (Throwable th) {
                    Exceptions.throwOrReport(th, (Observer) this);
                }
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorWithLatestFrom.2 */
    class C14332 extends Subscriber<U> {
        final /* synthetic */ AtomicReference val$current;
        final /* synthetic */ SerializedSubscriber val$s;

        C14332(AtomicReference atomicReference, SerializedSubscriber serializedSubscriber) {
            this.val$current = atomicReference;
            this.val$s = serializedSubscriber;
        }

        public void onCompleted() {
            if (this.val$current.get() == OperatorWithLatestFrom.EMPTY) {
                this.val$s.onCompleted();
                this.val$s.unsubscribe();
            }
        }

        public void onError(Throwable th) {
            this.val$s.onError(th);
            this.val$s.unsubscribe();
        }

        public void onNext(U u) {
            this.val$current.set(u);
        }
    }

    static {
        EMPTY = new Object();
    }

    public OperatorWithLatestFrom(Observable<? extends U> observable, Func2<? super T, ? super U, ? extends R> func2) {
        this.other = observable;
        this.resultSelector = func2;
    }

    public Subscriber<? super T> call(Subscriber<? super R> subscriber) {
        SerializedSubscriber serializedSubscriber = new SerializedSubscriber(subscriber, false);
        subscriber.add(serializedSubscriber);
        AtomicReference atomicReference = new AtomicReference(EMPTY);
        Object c14321 = new C14321(serializedSubscriber, true, atomicReference, serializedSubscriber);
        Object c14332 = new C14332(atomicReference, serializedSubscriber);
        serializedSubscriber.add(c14321);
        serializedSubscriber.add(c14332);
        this.other.unsafeSubscribe(c14332);
        return c14321;
    }
}

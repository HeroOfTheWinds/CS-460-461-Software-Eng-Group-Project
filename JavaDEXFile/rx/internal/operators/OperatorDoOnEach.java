package rx.internal.operators;

import java.util.Arrays;
import rx.Observable.Operator;
import rx.Observer;
import rx.Subscriber;
import rx.exceptions.CompositeException;
import rx.exceptions.Exceptions;

public class OperatorDoOnEach<T> implements Operator<T, T> {
    final Observer<? super T> doOnEachObserver;

    /* renamed from: rx.internal.operators.OperatorDoOnEach.1 */
    class C13311 extends Subscriber<T> {
        private boolean done;
        final /* synthetic */ Subscriber val$observer;

        C13311(Subscriber subscriber, Subscriber subscriber2) {
            this.val$observer = subscriber2;
            super(subscriber);
            this.done = false;
        }

        public void onCompleted() {
            if (!this.done) {
                try {
                    OperatorDoOnEach.this.doOnEachObserver.onCompleted();
                    this.done = true;
                    this.val$observer.onCompleted();
                } catch (Throwable th) {
                    Exceptions.throwOrReport(th, (Observer) this);
                }
            }
        }

        public void onError(Throwable th) {
            Exceptions.throwIfFatal(th);
            if (!this.done) {
                this.done = true;
                try {
                    OperatorDoOnEach.this.doOnEachObserver.onError(th);
                    this.val$observer.onError(th);
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    this.val$observer.onError(new CompositeException(Arrays.asList(new Throwable[]{th, th2})));
                }
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    OperatorDoOnEach.this.doOnEachObserver.onNext(t);
                    this.val$observer.onNext(t);
                } catch (Throwable th) {
                    Exceptions.throwOrReport(th, this, t);
                }
            }
        }
    }

    public OperatorDoOnEach(Observer<? super T> observer) {
        this.doOnEachObserver = observer;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        return new C13311(subscriber, subscriber);
    }
}

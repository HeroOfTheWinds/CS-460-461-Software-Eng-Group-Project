package rx.internal.operators;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import rx.Observable.Operator;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.internal.producers.SingleDelayedProducer;

public final class OperatorToObservableList<T> implements Operator<List<T>, T> {

    /* renamed from: rx.internal.operators.OperatorToObservableList.1 */
    class C14191 extends Subscriber<T> {
        boolean completed;
        List<T> list;
        final /* synthetic */ Subscriber val$o;
        final /* synthetic */ SingleDelayedProducer val$producer;

        C14191(SingleDelayedProducer singleDelayedProducer, Subscriber subscriber) {
            this.val$producer = singleDelayedProducer;
            this.val$o = subscriber;
            this.completed = false;
            this.list = new LinkedList();
        }

        public void onCompleted() {
            if (!this.completed) {
                this.completed = true;
                try {
                    ArrayList arrayList = new ArrayList(this.list);
                    this.list = null;
                    this.val$producer.setValue(arrayList);
                } catch (Throwable th) {
                    Exceptions.throwOrReport(th, (Observer) this);
                }
            }
        }

        public void onError(Throwable th) {
            this.val$o.onError(th);
        }

        public void onNext(T t) {
            if (!this.completed) {
                this.list.add(t);
            }
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }
    }

    private static final class Holder {
        static final OperatorToObservableList<Object> INSTANCE;

        static {
            INSTANCE = new OperatorToObservableList();
        }

        private Holder() {
        }
    }

    OperatorToObservableList() {
    }

    public static <T> OperatorToObservableList<T> instance() {
        return Holder.INSTANCE;
    }

    public Subscriber<? super T> call(Subscriber<? super List<T>> subscriber) {
        Producer singleDelayedProducer = new SingleDelayedProducer(subscriber);
        Object c14191 = new C14191(singleDelayedProducer, subscriber);
        subscriber.add(c14191);
        subscriber.setProducer(singleDelayedProducer);
        return c14191;
    }
}

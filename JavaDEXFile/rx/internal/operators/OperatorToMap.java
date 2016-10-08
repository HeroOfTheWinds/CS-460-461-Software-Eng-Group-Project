package rx.internal.operators;

import java.util.HashMap;
import java.util.Map;
import rx.Observable.Operator;
import rx.Observer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.observers.Subscribers;

public final class OperatorToMap<T, K, V> implements Operator<Map<K, V>, T> {
    final Func1<? super T, ? extends K> keySelector;
    private final Func0<? extends Map<K, V>> mapFactory;
    final Func1<? super T, ? extends V> valueSelector;

    /* renamed from: rx.internal.operators.OperatorToMap.1 */
    class C14171 extends Subscriber<T> {
        private Map<K, V> map;
        final /* synthetic */ Map val$fLocalMap;
        final /* synthetic */ Subscriber val$subscriber;

        C14171(Subscriber subscriber, Map map, Subscriber subscriber2) {
            this.val$fLocalMap = map;
            this.val$subscriber = subscriber2;
            super(subscriber);
            this.map = this.val$fLocalMap;
        }

        public void onCompleted() {
            Map map = this.map;
            this.map = null;
            this.val$subscriber.onNext(map);
            this.val$subscriber.onCompleted();
        }

        public void onError(Throwable th) {
            this.map = null;
            this.val$subscriber.onError(th);
        }

        public void onNext(T t) {
            try {
                this.map.put(OperatorToMap.this.keySelector.call(t), OperatorToMap.this.valueSelector.call(t));
            } catch (Throwable th) {
                Exceptions.throwOrReport(th, this.val$subscriber);
            }
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }
    }

    public static final class DefaultToMapFactory<K, V> implements Func0<Map<K, V>> {
        public Map<K, V> call() {
            return new HashMap();
        }
    }

    public OperatorToMap(Func1<? super T, ? extends K> func1, Func1<? super T, ? extends V> func12) {
        this(func1, func12, new DefaultToMapFactory());
    }

    public OperatorToMap(Func1<? super T, ? extends K> func1, Func1<? super T, ? extends V> func12, Func0<? extends Map<K, V>> func0) {
        this.keySelector = func1;
        this.valueSelector = func12;
        this.mapFactory = func0;
    }

    public Subscriber<? super T> call(Subscriber<? super Map<K, V>> subscriber) {
        try {
            return new C14171(subscriber, (Map) this.mapFactory.call(), subscriber);
        } catch (Throwable th) {
            Exceptions.throwOrReport(th, (Observer) subscriber);
            Subscriber<? super T> empty = Subscribers.empty();
            empty.unsubscribe();
            return empty;
        }
    }
}

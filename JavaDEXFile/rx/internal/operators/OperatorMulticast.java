package rx.internal.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.observables.ConnectableObservable;
import rx.observers.Subscribers;
import rx.subjects.Subject;
import rx.subscriptions.Subscriptions;

public final class OperatorMulticast<T, R> extends ConnectableObservable<R> {
    final AtomicReference<Subject<? super T, ? extends R>> connectedSubject;
    final Object guard;
    Subscription guardedSubscription;
    final Observable<? extends T> source;
    final Func0<? extends Subject<? super T, ? extends R>> subjectFactory;
    Subscriber<T> subscription;
    final List<Subscriber<? super R>> waitingForConnect;

    /* renamed from: rx.internal.operators.OperatorMulticast.1 */
    class C13441 implements OnSubscribe<R> {
        final /* synthetic */ AtomicReference val$connectedSubject;
        final /* synthetic */ Object val$guard;
        final /* synthetic */ List val$waitingForConnect;

        C13441(Object obj, AtomicReference atomicReference, List list) {
            this.val$guard = obj;
            this.val$connectedSubject = atomicReference;
            this.val$waitingForConnect = list;
        }

        public void call(Subscriber<? super R> subscriber) {
            synchronized (this.val$guard) {
                if (this.val$connectedSubject.get() == null) {
                    this.val$waitingForConnect.add(subscriber);
                } else {
                    ((Subject) this.val$connectedSubject.get()).unsafeSubscribe(subscriber);
                }
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorMulticast.2 */
    class C13452 implements Action0 {
        final /* synthetic */ AtomicReference val$gs;

        C13452(AtomicReference atomicReference) {
            this.val$gs = atomicReference;
        }

        public void call() {
            synchronized (OperatorMulticast.this.guard) {
                if (OperatorMulticast.this.guardedSubscription == this.val$gs.get()) {
                    Subscription subscription = OperatorMulticast.this.subscription;
                    OperatorMulticast.this.subscription = null;
                    OperatorMulticast.this.guardedSubscription = null;
                    OperatorMulticast.this.connectedSubject.set(null);
                    if (subscription != null) {
                        subscription.unsubscribe();
                        return;
                    }
                    return;
                }
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorMulticast.3 */
    class C13463 extends Subscriber<R> {
        final /* synthetic */ Subscriber val$s;

        C13463(Subscriber subscriber, Subscriber subscriber2) {
            this.val$s = subscriber2;
            super(subscriber);
        }

        public void onCompleted() {
            this.val$s.onCompleted();
        }

        public void onError(Throwable th) {
            this.val$s.onError(th);
        }

        public void onNext(R r) {
            this.val$s.onNext(r);
        }
    }

    private OperatorMulticast(Object obj, AtomicReference<Subject<? super T, ? extends R>> atomicReference, List<Subscriber<? super R>> list, Observable<? extends T> observable, Func0<? extends Subject<? super T, ? extends R>> func0) {
        super(new C13441(obj, atomicReference, list));
        this.guard = obj;
        this.connectedSubject = atomicReference;
        this.waitingForConnect = list;
        this.source = observable;
        this.subjectFactory = func0;
    }

    public OperatorMulticast(Observable<? extends T> observable, Func0<? extends Subject<? super T, ? extends R>> func0) {
        this(new Object(), new AtomicReference(), new ArrayList(), observable, func0);
    }

    public void connect(Action1<? super Subscription> action1) {
        synchronized (this.guard) {
            if (this.subscription != null) {
                action1.call(this.guardedSubscription);
                return;
            }
            Subject subject = (Subject) this.subjectFactory.call();
            this.subscription = Subscribers.from(subject);
            AtomicReference atomicReference = new AtomicReference();
            atomicReference.set(Subscriptions.create(new C13452(atomicReference)));
            this.guardedSubscription = (Subscription) atomicReference.get();
            for (Subscriber subscriber : this.waitingForConnect) {
                subject.unsafeSubscribe(new C13463(subscriber, subscriber));
            }
            this.waitingForConnect.clear();
            this.connectedSubject.set(subject);
            action1.call(this.guardedSubscription);
            synchronized (this.guard) {
                Subscriber subscriber2 = this.subscription;
            }
            if (subscriber2 != null) {
                this.source.subscribe(subscriber2);
            }
        }
    }
}

package rx.subjects;

import java.util.ArrayList;
import java.util.List;
import rx.Observable.OnSubscribe;
import rx.annotations.Beta;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.internal.operators.NotificationLite;
import rx.internal.producers.SingleProducer;

public final class AsyncSubject<T> extends Subject<T, T> {
    volatile Object lastValue;
    private final NotificationLite<T> nl;
    final SubjectSubscriptionManager<T> state;

    /* renamed from: rx.subjects.AsyncSubject.1 */
    static final class C15031 implements Action1<SubjectObserver<T>> {
        final /* synthetic */ SubjectSubscriptionManager val$state;

        C15031(SubjectSubscriptionManager subjectSubscriptionManager) {
            this.val$state = subjectSubscriptionManager;
        }

        public void call(SubjectObserver<T> subjectObserver) {
            Object latest = this.val$state.getLatest();
            NotificationLite notificationLite = this.val$state.nl;
            if (latest == null || notificationLite.isCompleted(latest)) {
                subjectObserver.onCompleted();
            } else if (notificationLite.isError(latest)) {
                subjectObserver.onError(notificationLite.getError(latest));
            } else {
                subjectObserver.actual.setProducer(new SingleProducer(subjectObserver.actual, notificationLite.getValue(latest)));
            }
        }
    }

    protected AsyncSubject(OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> subjectSubscriptionManager) {
        super(onSubscribe);
        this.nl = NotificationLite.instance();
        this.state = subjectSubscriptionManager;
    }

    public static <T> AsyncSubject<T> create() {
        Object subjectSubscriptionManager = new SubjectSubscriptionManager();
        subjectSubscriptionManager.onTerminated = new C15031(subjectSubscriptionManager);
        return new AsyncSubject(subjectSubscriptionManager, subjectSubscriptionManager);
    }

    @Beta
    public Throwable getThrowable() {
        Object latest = this.state.getLatest();
        return this.nl.isError(latest) ? this.nl.getError(latest) : null;
    }

    @Beta
    public T getValue() {
        Object obj = this.lastValue;
        return (this.nl.isError(this.state.getLatest()) || !this.nl.isNext(obj)) ? null : this.nl.getValue(obj);
    }

    @Beta
    public boolean hasCompleted() {
        Object latest = this.state.getLatest();
        return (latest == null || this.nl.isError(latest)) ? false : true;
    }

    public boolean hasObservers() {
        return this.state.observers().length > 0;
    }

    @Beta
    public boolean hasThrowable() {
        return this.nl.isError(this.state.getLatest());
    }

    @Beta
    public boolean hasValue() {
        return !this.nl.isError(this.state.getLatest()) && this.nl.isNext(this.lastValue);
    }

    public void onCompleted() {
        if (this.state.active) {
            Object obj = this.lastValue;
            if (obj == null) {
                obj = this.nl.completed();
            }
            for (SubjectObserver subjectObserver : this.state.terminate(obj)) {
                if (obj == this.nl.completed()) {
                    subjectObserver.onCompleted();
                } else {
                    subjectObserver.actual.setProducer(new SingleProducer(subjectObserver.actual, this.nl.getValue(obj)));
                }
            }
        }
    }

    public void onError(Throwable th) {
        if (this.state.active) {
            List list = null;
            for (SubjectObserver onError : this.state.terminate(this.nl.error(th))) {
                try {
                    onError.onError(th);
                } catch (Throwable th2) {
                    if (list == null) {
                        list = new ArrayList();
                    }
                    list.add(th2);
                }
            }
            Exceptions.throwIfAny(list);
        }
    }

    public void onNext(T t) {
        this.lastValue = this.nl.next(t);
    }
}

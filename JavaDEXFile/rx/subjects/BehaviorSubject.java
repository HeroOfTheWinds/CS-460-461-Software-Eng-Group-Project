package rx.subjects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import rx.Observable.OnSubscribe;
import rx.annotations.Beta;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.internal.operators.NotificationLite;

public final class BehaviorSubject<T> extends Subject<T, T> {
    private static final Object[] EMPTY_ARRAY;
    private final NotificationLite<T> nl;
    private final SubjectSubscriptionManager<T> state;

    /* renamed from: rx.subjects.BehaviorSubject.1 */
    static final class C15041 implements Action1<SubjectObserver<T>> {
        final /* synthetic */ SubjectSubscriptionManager val$state;

        C15041(SubjectSubscriptionManager subjectSubscriptionManager) {
            this.val$state = subjectSubscriptionManager;
        }

        public void call(SubjectObserver<T> subjectObserver) {
            subjectObserver.emitFirst(this.val$state.getLatest(), this.val$state.nl);
        }
    }

    static {
        EMPTY_ARRAY = new Object[0];
    }

    protected BehaviorSubject(OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> subjectSubscriptionManager) {
        super(onSubscribe);
        this.nl = NotificationLite.instance();
        this.state = subjectSubscriptionManager;
    }

    public static <T> BehaviorSubject<T> create() {
        return create(null, false);
    }

    public static <T> BehaviorSubject<T> create(T t) {
        return create(t, true);
    }

    private static <T> BehaviorSubject<T> create(T t, boolean z) {
        Object subjectSubscriptionManager = new SubjectSubscriptionManager();
        if (z) {
            subjectSubscriptionManager.setLatest(NotificationLite.instance().next(t));
        }
        subjectSubscriptionManager.onAdded = new C15041(subjectSubscriptionManager);
        subjectSubscriptionManager.onTerminated = subjectSubscriptionManager.onAdded;
        return new BehaviorSubject(subjectSubscriptionManager, subjectSubscriptionManager);
    }

    @Beta
    public Throwable getThrowable() {
        Object latest = this.state.getLatest();
        return this.nl.isError(latest) ? this.nl.getError(latest) : null;
    }

    @Beta
    public T getValue() {
        Object latest = this.state.getLatest();
        return this.nl.isNext(latest) ? this.nl.getValue(latest) : null;
    }

    @Beta
    public Object[] getValues() {
        Object[] values = getValues(EMPTY_ARRAY);
        return values == EMPTY_ARRAY ? new Object[0] : values;
    }

    @Beta
    public T[] getValues(T[] tArr) {
        T[] tArr2;
        Object latest = this.state.getLatest();
        if (this.nl.isNext(latest)) {
            tArr2 = tArr.length == 0 ? (Object[]) Array.newInstance(tArr.getClass().getComponentType(), 1) : tArr;
            tArr2[0] = this.nl.getValue(latest);
            if (tArr2.length > 1) {
                tArr2[1] = null;
            }
        } else if (tArr.length > 0) {
            tArr[0] = null;
            return tArr;
        } else {
            tArr2 = tArr;
        }
        return tArr2;
    }

    @Beta
    public boolean hasCompleted() {
        return this.nl.isCompleted(this.state.getLatest());
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
        return this.nl.isNext(this.state.getLatest());
    }

    public void onCompleted() {
        if (this.state.getLatest() == null || this.state.active) {
            Object completed = this.nl.completed();
            for (SubjectObserver emitNext : this.state.terminate(completed)) {
                emitNext.emitNext(completed, this.state.nl);
            }
        }
    }

    public void onError(Throwable th) {
        if (this.state.getLatest() == null || this.state.active) {
            Object error = this.nl.error(th);
            List list = null;
            for (SubjectObserver emitNext : this.state.terminate(error)) {
                try {
                    emitNext.emitNext(error, this.state.nl);
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
        if (this.state.getLatest() == null || this.state.active) {
            Object next = this.nl.next(t);
            for (SubjectObserver emitNext : this.state.next(next)) {
                emitNext.emitNext(next, this.state.nl);
            }
        }
    }

    int subscriberCount() {
        return this.state.observers().length;
    }
}

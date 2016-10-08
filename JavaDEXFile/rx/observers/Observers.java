package rx.observers;

import rx.Observer;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action0;
import rx.functions.Action1;

public final class Observers {
    private static final Observer<Object> EMPTY;

    /* renamed from: rx.observers.Observers.1 */
    static final class C14841 implements Observer<Object> {
        C14841() {
        }

        public final void onCompleted() {
        }

        public final void onError(Throwable th) {
            throw new OnErrorNotImplementedException(th);
        }

        public final void onNext(Object obj) {
        }
    }

    /* renamed from: rx.observers.Observers.2 */
    static final class C14852 implements Observer<T> {
        final /* synthetic */ Action1 val$onNext;

        C14852(Action1 action1) {
            this.val$onNext = action1;
        }

        public final void onCompleted() {
        }

        public final void onError(Throwable th) {
            throw new OnErrorNotImplementedException(th);
        }

        public final void onNext(T t) {
            this.val$onNext.call(t);
        }
    }

    /* renamed from: rx.observers.Observers.3 */
    static final class C14863 implements Observer<T> {
        final /* synthetic */ Action1 val$onError;
        final /* synthetic */ Action1 val$onNext;

        C14863(Action1 action1, Action1 action12) {
            this.val$onError = action1;
            this.val$onNext = action12;
        }

        public final void onCompleted() {
        }

        public final void onError(Throwable th) {
            this.val$onError.call(th);
        }

        public final void onNext(T t) {
            this.val$onNext.call(t);
        }
    }

    /* renamed from: rx.observers.Observers.4 */
    static final class C14874 implements Observer<T> {
        final /* synthetic */ Action0 val$onComplete;
        final /* synthetic */ Action1 val$onError;
        final /* synthetic */ Action1 val$onNext;

        C14874(Action0 action0, Action1 action1, Action1 action12) {
            this.val$onComplete = action0;
            this.val$onError = action1;
            this.val$onNext = action12;
        }

        public final void onCompleted() {
            this.val$onComplete.call();
        }

        public final void onError(Throwable th) {
            this.val$onError.call(th);
        }

        public final void onNext(T t) {
            this.val$onNext.call(t);
        }
    }

    static {
        EMPTY = new C14841();
    }

    private Observers() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Observer<T> create(Action1<? super T> action1) {
        if (action1 != null) {
            return new C14852(action1);
        }
        throw new IllegalArgumentException("onNext can not be null");
    }

    public static <T> Observer<T> create(Action1<? super T> action1, Action1<Throwable> action12) {
        if (action1 == null) {
            throw new IllegalArgumentException("onNext can not be null");
        } else if (action12 != null) {
            return new C14863(action12, action1);
        } else {
            throw new IllegalArgumentException("onError can not be null");
        }
    }

    public static <T> Observer<T> create(Action1<? super T> action1, Action1<Throwable> action12, Action0 action0) {
        if (action1 == null) {
            throw new IllegalArgumentException("onNext can not be null");
        } else if (action12 == null) {
            throw new IllegalArgumentException("onError can not be null");
        } else if (action0 != null) {
            return new C14874(action0, action12, action1);
        } else {
            throw new IllegalArgumentException("onComplete can not be null");
        }
    }

    public static <T> Observer<T> empty() {
        return EMPTY;
    }
}

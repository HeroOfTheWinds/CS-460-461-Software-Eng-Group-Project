package rx.internal.operators;

import java.io.Serializable;
import rx.Notification.Kind;
import rx.Observer;

public final class NotificationLite<T> {
    private static final NotificationLite INSTANCE;
    private static final Object ON_COMPLETED_SENTINEL;
    private static final Object ON_NEXT_NULL_SENTINEL;

    /* renamed from: rx.internal.operators.NotificationLite.1 */
    static final class C12791 implements Serializable {
        private static final long serialVersionUID = 1;

        C12791() {
        }

        public String toString() {
            return "Notification=>Completed";
        }
    }

    /* renamed from: rx.internal.operators.NotificationLite.2 */
    static final class C12802 implements Serializable {
        private static final long serialVersionUID = 2;

        C12802() {
        }

        public String toString() {
            return "Notification=>NULL";
        }
    }

    private static class OnErrorSentinel implements Serializable {
        private static final long serialVersionUID = 3;
        final Throwable f908e;

        public OnErrorSentinel(Throwable th) {
            this.f908e = th;
        }

        public String toString() {
            return "Notification=>Error:" + this.f908e;
        }
    }

    static {
        INSTANCE = new NotificationLite();
        ON_COMPLETED_SENTINEL = new C12791();
        ON_NEXT_NULL_SENTINEL = new C12802();
    }

    private NotificationLite() {
    }

    public static <T> NotificationLite<T> instance() {
        return INSTANCE;
    }

    public boolean accept(Observer<? super T> observer, Object obj) {
        if (obj == ON_COMPLETED_SENTINEL) {
            observer.onCompleted();
            return true;
        } else if (obj == ON_NEXT_NULL_SENTINEL) {
            observer.onNext(null);
            return false;
        } else if (obj == null) {
            throw new IllegalArgumentException("The lite notification can not be null");
        } else if (obj.getClass() == OnErrorSentinel.class) {
            observer.onError(((OnErrorSentinel) obj).f908e);
            return true;
        } else {
            observer.onNext(obj);
            return false;
        }
    }

    public Object completed() {
        return ON_COMPLETED_SENTINEL;
    }

    public Object error(Throwable th) {
        return new OnErrorSentinel(th);
    }

    public Throwable getError(Object obj) {
        return ((OnErrorSentinel) obj).f908e;
    }

    public T getValue(Object obj) {
        return obj == ON_NEXT_NULL_SENTINEL ? null : obj;
    }

    public boolean isCompleted(Object obj) {
        return obj == ON_COMPLETED_SENTINEL;
    }

    public boolean isError(Object obj) {
        return obj instanceof OnErrorSentinel;
    }

    public boolean isNext(Object obj) {
        return (obj == null || isError(obj) || isCompleted(obj)) ? false : true;
    }

    public boolean isNull(Object obj) {
        return obj == ON_NEXT_NULL_SENTINEL;
    }

    public Kind kind(Object obj) {
        if (obj != null) {
            return obj == ON_COMPLETED_SENTINEL ? Kind.OnCompleted : obj instanceof OnErrorSentinel ? Kind.OnError : Kind.OnNext;
        } else {
            throw new IllegalArgumentException("The lite notification can not be null");
        }
    }

    public Object next(T t) {
        return t == null ? ON_NEXT_NULL_SENTINEL : t;
    }
}

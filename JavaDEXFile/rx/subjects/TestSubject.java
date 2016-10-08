package rx.subjects;

import java.util.concurrent.TimeUnit;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Scheduler.Worker;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.internal.operators.NotificationLite;
import rx.schedulers.TestScheduler;

public final class TestSubject<T> extends Subject<T, T> {
    private final Worker innerScheduler;
    private final SubjectSubscriptionManager<T> state;

    /* renamed from: rx.subjects.TestSubject.1 */
    static final class C15131 implements Action1<SubjectObserver<T>> {
        final /* synthetic */ SubjectSubscriptionManager val$state;

        C15131(SubjectSubscriptionManager subjectSubscriptionManager) {
            this.val$state = subjectSubscriptionManager;
        }

        public void call(SubjectObserver<T> subjectObserver) {
            subjectObserver.emitFirst(this.val$state.getLatest(), this.val$state.nl);
        }
    }

    /* renamed from: rx.subjects.TestSubject.2 */
    class C15142 implements Action0 {
        C15142() {
        }

        public void call() {
            TestSubject.this._onCompleted();
        }
    }

    /* renamed from: rx.subjects.TestSubject.3 */
    class C15153 implements Action0 {
        final /* synthetic */ Throwable val$e;

        C15153(Throwable th) {
            this.val$e = th;
        }

        public void call() {
            TestSubject.this._onError(this.val$e);
        }
    }

    /* renamed from: rx.subjects.TestSubject.4 */
    class C15164 implements Action0 {
        final /* synthetic */ Object val$v;

        C15164(Object obj) {
            this.val$v = obj;
        }

        public void call() {
            TestSubject.this._onNext(this.val$v);
        }
    }

    protected TestSubject(OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> subjectSubscriptionManager, TestScheduler testScheduler) {
        super(onSubscribe);
        this.state = subjectSubscriptionManager;
        this.innerScheduler = testScheduler.createWorker();
    }

    public static <T> TestSubject<T> create(TestScheduler testScheduler) {
        Object subjectSubscriptionManager = new SubjectSubscriptionManager();
        subjectSubscriptionManager.onAdded = new C15131(subjectSubscriptionManager);
        subjectSubscriptionManager.onTerminated = subjectSubscriptionManager.onAdded;
        return new TestSubject(subjectSubscriptionManager, subjectSubscriptionManager, testScheduler);
    }

    void _onCompleted() {
        if (this.state.active) {
            for (SubjectObserver onCompleted : this.state.terminate(NotificationLite.instance().completed())) {
                onCompleted.onCompleted();
            }
        }
    }

    void _onError(Throwable th) {
        if (this.state.active) {
            for (SubjectObserver onError : this.state.terminate(NotificationLite.instance().error(th))) {
                onError.onError(th);
            }
        }
    }

    void _onNext(T t) {
        for (Observer onNext : this.state.observers()) {
            onNext.onNext(t);
        }
    }

    public boolean hasObservers() {
        return this.state.observers().length > 0;
    }

    public void onCompleted() {
        onCompleted(0);
    }

    public void onCompleted(long j) {
        this.innerScheduler.schedule(new C15142(), j, TimeUnit.MILLISECONDS);
    }

    public void onError(Throwable th) {
        onError(th, 0);
    }

    public void onError(Throwable th, long j) {
        this.innerScheduler.schedule(new C15153(th), j, TimeUnit.MILLISECONDS);
    }

    public void onNext(T t) {
        onNext(t, 0);
    }

    public void onNext(T t, long j) {
        this.innerScheduler.schedule(new C15164(t), j, TimeUnit.MILLISECONDS);
    }
}

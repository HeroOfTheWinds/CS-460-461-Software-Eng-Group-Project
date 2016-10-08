package rx.internal.operators;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.functions.Action0;
import rx.subjects.Subject;

public final class BufferUntilSubscriber<T> extends Subject<T, T> {
    static final Observer EMPTY_OBSERVER;
    private boolean forward;
    final State<T> state;

    /* renamed from: rx.internal.operators.BufferUntilSubscriber.1 */
    static final class C12681 implements Observer {
        C12681() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable th) {
        }

        public void onNext(Object obj) {
        }
    }

    static final class OnSubscribeAction<T> implements OnSubscribe<T> {
        final State<T> state;

        /* renamed from: rx.internal.operators.BufferUntilSubscriber.OnSubscribeAction.1 */
        class C12691 implements Action0 {
            C12691() {
            }

            public void call() {
                OnSubscribeAction.this.state.set(BufferUntilSubscriber.EMPTY_OBSERVER);
            }
        }

        public OnSubscribeAction(State<T> state) {
            this.state = state;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void call(rx.Subscriber<? super T> r5) {
            /*
            r4 = this;
            r0 = 1;
            r1 = 0;
            r2 = r4.state;
            r3 = 0;
            r2 = r2.casObserverRef(r3, r5);
            if (r2 == 0) goto L_0x0062;
        L_0x000b:
            r2 = new rx.internal.operators.BufferUntilSubscriber$OnSubscribeAction$1;
            r2.<init>();
            r2 = rx.subscriptions.Subscriptions.create(r2);
            r5.add(r2);
            r2 = r4.state;
            r2 = r2.guard;
            monitor-enter(r2);
            r3 = r4.state;	 Catch:{ all -> 0x0044 }
            r3 = r3.emitting;	 Catch:{ all -> 0x0044 }
            if (r3 != 0) goto L_0x006d;
        L_0x0022:
            r1 = r4.state;	 Catch:{ all -> 0x0044 }
            r3 = 1;
            r1.emitting = r3;	 Catch:{ all -> 0x0044 }
        L_0x0027:
            monitor-exit(r2);	 Catch:{ all -> 0x0044 }
            if (r0 == 0) goto L_0x005c;
        L_0x002a:
            r1 = rx.internal.operators.NotificationLite.instance();
        L_0x002e:
            r0 = r4.state;
            r0 = r0.buffer;
            r2 = r0.poll();
            if (r2 == 0) goto L_0x0047;
        L_0x0038:
            r0 = r4.state;
            r0 = r0.get();
            r0 = (rx.Observer) r0;
            r1.accept(r0, r2);
            goto L_0x002e;
        L_0x0044:
            r0 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x0044 }
            throw r0;
        L_0x0047:
            r0 = r4.state;
            r2 = r0.guard;
            monitor-enter(r2);
            r0 = r4.state;	 Catch:{ all -> 0x005f }
            r0 = r0.buffer;	 Catch:{ all -> 0x005f }
            r0 = r0.isEmpty();	 Catch:{ all -> 0x005f }
            if (r0 == 0) goto L_0x005d;
        L_0x0056:
            r0 = r4.state;	 Catch:{ all -> 0x005f }
            r1 = 0;
            r0.emitting = r1;	 Catch:{ all -> 0x005f }
            monitor-exit(r2);	 Catch:{ all -> 0x005f }
        L_0x005c:
            return;
        L_0x005d:
            monitor-exit(r2);	 Catch:{ all -> 0x005f }
            goto L_0x002e;
        L_0x005f:
            r0 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x005f }
            throw r0;
        L_0x0062:
            r0 = new java.lang.IllegalStateException;
            r1 = "Only one subscriber allowed!";
            r0.<init>(r1);
            r5.onError(r0);
            goto L_0x005c;
        L_0x006d:
            r0 = r1;
            goto L_0x0027;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.BufferUntilSubscriber.OnSubscribeAction.call(rx.Subscriber):void");
        }
    }

    static final class State<T> extends AtomicReference<Observer<? super T>> {
        final ConcurrentLinkedQueue<Object> buffer;
        boolean emitting;
        final Object guard;
        final NotificationLite<T> nl;

        State() {
            this.guard = new Object();
            this.emitting = false;
            this.buffer = new ConcurrentLinkedQueue();
            this.nl = NotificationLite.instance();
        }

        boolean casObserverRef(Observer<? super T> observer, Observer<? super T> observer2) {
            return compareAndSet(observer, observer2);
        }
    }

    static {
        EMPTY_OBSERVER = new C12681();
    }

    private BufferUntilSubscriber(State<T> state) {
        super(new OnSubscribeAction(state));
        this.forward = false;
        this.state = state;
    }

    public static <T> BufferUntilSubscriber<T> create() {
        return new BufferUntilSubscriber(new State());
    }

    private void emit(Object obj) {
        synchronized (this.state.guard) {
            this.state.buffer.add(obj);
            if (!(this.state.get() == null || this.state.emitting)) {
                this.forward = true;
                this.state.emitting = true;
            }
        }
        if (this.forward) {
            while (true) {
                Object poll = this.state.buffer.poll();
                if (poll != null) {
                    this.state.nl.accept((Observer) this.state.get(), poll);
                } else {
                    return;
                }
            }
        }
    }

    public boolean hasObservers() {
        boolean z;
        synchronized (this.state.guard) {
            z = this.state.get() != null;
        }
        return z;
    }

    public void onCompleted() {
        if (this.forward) {
            ((Observer) this.state.get()).onCompleted();
        } else {
            emit(this.state.nl.completed());
        }
    }

    public void onError(Throwable th) {
        if (this.forward) {
            ((Observer) this.state.get()).onError(th);
        } else {
            emit(this.state.nl.error(th));
        }
    }

    public void onNext(T t) {
        if (this.forward) {
            ((Observer) this.state.get()).onNext(t);
        } else {
            emit(this.state.nl.next(t));
        }
    }
}

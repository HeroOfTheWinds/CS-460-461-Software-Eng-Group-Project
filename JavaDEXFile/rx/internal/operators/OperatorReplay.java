package rx.internal.operators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Producer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;
import rx.schedulers.Timestamped;
import rx.subscriptions.Subscriptions;

public final class OperatorReplay<T> extends ConnectableObservable<T> {
    static final Func0 DEFAULT_UNBOUNDED_FACTORY;
    final Func0<? extends ReplayBuffer<T>> bufferFactory;
    final AtomicReference<ReplaySubscriber<T>> current;
    final Observable<? extends T> source;

    /* renamed from: rx.internal.operators.OperatorReplay.1 */
    static final class C13591 implements Func0 {
        C13591() {
        }

        public Object call() {
            return new UnboundedReplayBuffer(16);
        }
    }

    /* renamed from: rx.internal.operators.OperatorReplay.2 */
    static final class C13612 implements OnSubscribe<R> {
        final /* synthetic */ Func0 val$connectableFactory;
        final /* synthetic */ Func1 val$selector;

        /* renamed from: rx.internal.operators.OperatorReplay.2.1 */
        class C13601 implements Action1<Subscription> {
            final /* synthetic */ Subscriber val$child;

            C13601(Subscriber subscriber) {
                this.val$child = subscriber;
            }

            public void call(Subscription subscription) {
                this.val$child.add(subscription);
            }
        }

        C13612(Func0 func0, Func1 func1) {
            this.val$connectableFactory = func0;
            this.val$selector = func1;
        }

        public void call(Subscriber<? super R> subscriber) {
            try {
                ConnectableObservable connectableObservable = (ConnectableObservable) this.val$connectableFactory.call();
                ((Observable) this.val$selector.call(connectableObservable)).subscribe((Subscriber) subscriber);
                connectableObservable.connect(new C13601(subscriber));
            } catch (Throwable th) {
                Exceptions.throwOrReport(th, (Observer) subscriber);
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorReplay.3 */
    static final class C13633 implements OnSubscribe<T> {
        final /* synthetic */ Observable val$observable;

        /* renamed from: rx.internal.operators.OperatorReplay.3.1 */
        class C13621 extends Subscriber<T> {
            final /* synthetic */ Subscriber val$child;

            C13621(Subscriber subscriber, Subscriber subscriber2) {
                this.val$child = subscriber2;
                super(subscriber);
            }

            public void onCompleted() {
                this.val$child.onCompleted();
            }

            public void onError(Throwable th) {
                this.val$child.onError(th);
            }

            public void onNext(T t) {
                this.val$child.onNext(t);
            }
        }

        C13633(Observable observable) {
            this.val$observable = observable;
        }

        public void call(Subscriber<? super T> subscriber) {
            this.val$observable.unsafeSubscribe(new C13621(subscriber, subscriber));
        }
    }

    /* renamed from: rx.internal.operators.OperatorReplay.4 */
    static final class C13644 extends ConnectableObservable<T> {
        final /* synthetic */ ConnectableObservable val$co;

        C13644(OnSubscribe onSubscribe, ConnectableObservable connectableObservable) {
            this.val$co = connectableObservable;
            super(onSubscribe);
        }

        public void connect(Action1<? super Subscription> action1) {
            this.val$co.connect(action1);
        }
    }

    /* renamed from: rx.internal.operators.OperatorReplay.5 */
    static final class C13655 implements Func0<ReplayBuffer<T>> {
        final /* synthetic */ int val$bufferSize;

        C13655(int i) {
            this.val$bufferSize = i;
        }

        public ReplayBuffer<T> call() {
            return new SizeBoundReplayBuffer(this.val$bufferSize);
        }
    }

    /* renamed from: rx.internal.operators.OperatorReplay.6 */
    static final class C13666 implements Func0<ReplayBuffer<T>> {
        final /* synthetic */ int val$bufferSize;
        final /* synthetic */ long val$maxAgeInMillis;
        final /* synthetic */ Scheduler val$scheduler;

        C13666(int i, long j, Scheduler scheduler) {
            this.val$bufferSize = i;
            this.val$maxAgeInMillis = j;
            this.val$scheduler = scheduler;
        }

        public ReplayBuffer<T> call() {
            return new SizeAndTimeBoundReplayBuffer(this.val$bufferSize, this.val$maxAgeInMillis, this.val$scheduler);
        }
    }

    /* renamed from: rx.internal.operators.OperatorReplay.7 */
    static final class C13677 implements OnSubscribe<T> {
        final /* synthetic */ Func0 val$bufferFactory;
        final /* synthetic */ AtomicReference val$curr;

        C13677(AtomicReference atomicReference, Func0 func0) {
            this.val$curr = atomicReference;
            this.val$bufferFactory = func0;
        }

        public void call(Subscriber<? super T> subscriber) {
            ReplaySubscriber replaySubscriber;
            ReplaySubscriber replaySubscriber2;
            do {
                replaySubscriber = (ReplaySubscriber) this.val$curr.get();
                if (replaySubscriber != null) {
                    break;
                }
                replaySubscriber2 = new ReplaySubscriber(this.val$curr, (ReplayBuffer) this.val$bufferFactory.call());
                replaySubscriber2.init();
            } while (!this.val$curr.compareAndSet(replaySubscriber, replaySubscriber2));
            replaySubscriber = replaySubscriber2;
            Producer innerProducer = new InnerProducer(replaySubscriber, subscriber);
            replaySubscriber.add(innerProducer);
            subscriber.add(innerProducer);
            replaySubscriber.buffer.replay(innerProducer);
            subscriber.setProducer(innerProducer);
        }
    }

    interface ReplayBuffer<T> {
        void complete();

        void error(Throwable th);

        void next(T t);

        void replay(InnerProducer<T> innerProducer);
    }

    static class BoundedReplayBuffer<T> extends AtomicReference<Node> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 2346567790059478686L;
        long index;
        final NotificationLite<T> nl;
        int size;
        Node tail;

        public BoundedReplayBuffer() {
            this.nl = NotificationLite.instance();
            Node node = new Node(null, 0);
            this.tail = node;
            set(node);
        }

        final void addLast(Node node) {
            this.tail.set(node);
            this.tail = node;
            this.size++;
        }

        final void collect(Collection<? super T> collection) {
            Node node = (Node) get();
            while (true) {
                node = (Node) node.get();
                if (node != null) {
                    Object leaveTransform = leaveTransform(node.value);
                    if (!this.nl.isCompleted(leaveTransform) && !this.nl.isError(leaveTransform)) {
                        collection.add(this.nl.getValue(leaveTransform));
                    } else {
                        return;
                    }
                }
                return;
            }
        }

        public final void complete() {
            Object enterTransform = enterTransform(this.nl.completed());
            long j = this.index + 1;
            this.index = j;
            addLast(new Node(enterTransform, j));
            truncateFinal();
        }

        Object enterTransform(Object obj) {
            return obj;
        }

        public final void error(Throwable th) {
            Object enterTransform = enterTransform(this.nl.error(th));
            long j = this.index + 1;
            this.index = j;
            addLast(new Node(enterTransform, j));
            truncateFinal();
        }

        boolean hasCompleted() {
            return this.tail.value != null && this.nl.isCompleted(leaveTransform(this.tail.value));
        }

        boolean hasError() {
            return this.tail.value != null && this.nl.isError(leaveTransform(this.tail.value));
        }

        Object leaveTransform(Object obj) {
            return obj;
        }

        public final void next(T t) {
            Object enterTransform = enterTransform(this.nl.next(t));
            long j = this.index + 1;
            this.index = j;
            addLast(new Node(enterTransform, j));
            truncate();
        }

        final void removeFirst() {
            Node node = (Node) ((Node) get()).get();
            if (node == null) {
                throw new IllegalStateException("Empty list!");
            }
            this.size--;
            setFirst(node);
        }

        final void removeSome(int i) {
            Node node = (Node) get();
            while (i > 0) {
                node = (Node) node.get();
                i--;
                this.size--;
            }
            setFirst(node);
        }

        public final void replay(InnerProducer<T> innerProducer) {
            synchronized (innerProducer) {
                if (innerProducer.emitting) {
                    innerProducer.missed = true;
                    return;
                }
                innerProducer.emitting = true;
                while (!innerProducer.isUnsubscribed()) {
                    long j = innerProducer.get();
                    Object obj = j == Long.MAX_VALUE ? 1 : null;
                    long j2 = 0;
                    Node node = (Node) innerProducer.index();
                    if (node == null) {
                        node = (Node) get();
                        innerProducer.index = node;
                        innerProducer.addTotalRequested(node.index);
                    }
                    if (!innerProducer.isUnsubscribed()) {
                        Node node2;
                        while (true) {
                            long j3 = j;
                            j = j2;
                            node2 = node;
                            if (j3 == 0) {
                                break;
                            }
                            node = (Node) node2.get();
                            if (node == null) {
                                break;
                            }
                            r2 = leaveTransform(node.value);
                            try {
                                if (this.nl.accept(innerProducer.child, r2)) {
                                    innerProducer.index = null;
                                    return;
                                }
                                j2 = 1 + j;
                                j = j3 - 1;
                                if (innerProducer.isUnsubscribed()) {
                                    return;
                                }
                            } catch (Throwable th) {
                                innerProducer.index = null;
                                Exceptions.throwIfFatal(th);
                                innerProducer.unsubscribe();
                                Object leaveTransform;
                                if (!this.nl.isError(leaveTransform) && !this.nl.isCompleted(leaveTransform)) {
                                    innerProducer.child.onError(OnErrorThrowable.addValueAsLastCause(th, this.nl.getValue(leaveTransform)));
                                    return;
                                }
                                return;
                            }
                        }
                        if (j != 0) {
                            innerProducer.index = node2;
                            if (obj == null) {
                                innerProducer.produced(j);
                            }
                        }
                        synchronized (innerProducer) {
                            if (innerProducer.missed) {
                                innerProducer.missed = false;
                            } else {
                                innerProducer.emitting = false;
                                return;
                            }
                        }
                    }
                    return;
                }
            }
        }

        final void setFirst(Node node) {
            set(node);
        }

        void truncate() {
        }

        void truncateFinal() {
        }
    }

    static final class InnerProducer<T> extends AtomicLong implements Producer, Subscription {
        static final long UNSUBSCRIBED = Long.MIN_VALUE;
        private static final long serialVersionUID = -4453897557930727610L;
        final Subscriber<? super T> child;
        boolean emitting;
        Object index;
        boolean missed;
        final ReplaySubscriber<T> parent;
        final AtomicLong totalRequested;

        public InnerProducer(ReplaySubscriber<T> replaySubscriber, Subscriber<? super T> subscriber) {
            this.parent = replaySubscriber;
            this.child = subscriber;
            this.totalRequested = new AtomicLong();
        }

        void addTotalRequested(long j) {
            long j2;
            long j3;
            do {
                j2 = this.totalRequested.get();
                j3 = j2 + j;
                if (j3 < 0) {
                    j3 = Long.MAX_VALUE;
                }
            } while (!this.totalRequested.compareAndSet(j2, j3));
        }

        <U> U index() {
            return this.index;
        }

        public boolean isUnsubscribed() {
            return get() == UNSUBSCRIBED;
        }

        public long produced(long j) {
            if (j <= 0) {
                throw new IllegalArgumentException("Cant produce zero or less");
            }
            long j2;
            long j3;
            do {
                j3 = get();
                if (j3 == UNSUBSCRIBED) {
                    return UNSUBSCRIBED;
                }
                j2 = j3 - j;
                if (j2 < 0) {
                    throw new IllegalStateException("More produced (" + j + ") than requested (" + j3 + ")");
                }
            } while (!compareAndSet(j3, j2));
            return j2;
        }

        public void request(long j) {
            if (j >= 0) {
                long j2;
                long j3;
                do {
                    j2 = get();
                    if (j2 == UNSUBSCRIBED) {
                        return;
                    }
                    if (j2 < 0 || j != 0) {
                        j3 = j2 + j;
                        if (j3 < 0) {
                            j3 = Long.MAX_VALUE;
                        }
                    } else {
                        return;
                    }
                } while (!compareAndSet(j2, j3));
                addTotalRequested(j);
                this.parent.manageRequests();
                this.parent.buffer.replay(this);
            }
        }

        public void unsubscribe() {
            if (get() != UNSUBSCRIBED && getAndSet(UNSUBSCRIBED) != UNSUBSCRIBED) {
                this.parent.remove(this);
                this.parent.manageRequests();
            }
        }
    }

    static final class Node extends AtomicReference<Node> {
        private static final long serialVersionUID = 245354315435971818L;
        final long index;
        final Object value;

        public Node(Object obj, long j) {
            this.value = obj;
            this.index = j;
        }
    }

    static final class ReplaySubscriber<T> extends Subscriber<T> implements Subscription {
        static final InnerProducer[] EMPTY;
        static final InnerProducer[] TERMINATED;
        final ReplayBuffer<T> buffer;
        boolean done;
        boolean emitting;
        long maxChildRequested;
        long maxUpstreamRequested;
        boolean missed;
        final NotificationLite<T> nl;
        volatile Producer producer;
        final AtomicReference<InnerProducer[]> producers;
        final AtomicBoolean shouldConnect;

        /* renamed from: rx.internal.operators.OperatorReplay.ReplaySubscriber.1 */
        class C13681 implements Action0 {
            C13681() {
            }

            public void call() {
                ReplaySubscriber.this.producers.getAndSet(ReplaySubscriber.TERMINATED);
            }
        }

        static {
            EMPTY = new InnerProducer[0];
            TERMINATED = new InnerProducer[0];
        }

        public ReplaySubscriber(AtomicReference<ReplaySubscriber<T>> atomicReference, ReplayBuffer<T> replayBuffer) {
            this.buffer = replayBuffer;
            this.nl = NotificationLite.instance();
            this.producers = new AtomicReference(EMPTY);
            this.shouldConnect = new AtomicBoolean();
            request(0);
        }

        boolean add(InnerProducer<T> innerProducer) {
            if (innerProducer == null) {
                throw new NullPointerException();
            }
            InnerProducer[] innerProducerArr;
            Object obj;
            do {
                innerProducerArr = (InnerProducer[]) this.producers.get();
                if (innerProducerArr == TERMINATED) {
                    return false;
                }
                int length = innerProducerArr.length;
                obj = new InnerProducer[(length + 1)];
                System.arraycopy(innerProducerArr, 0, obj, 0, length);
                obj[length] = innerProducer;
            } while (!this.producers.compareAndSet(innerProducerArr, obj));
            return true;
        }

        void init() {
            add(Subscriptions.create(new C13681()));
        }

        void manageRequests() {
            if (!isUnsubscribed()) {
                synchronized (this) {
                    if (this.emitting) {
                        this.missed = true;
                        return;
                    }
                    this.emitting = true;
                    while (!isUnsubscribed()) {
                        InnerProducer[] innerProducerArr = (InnerProducer[]) this.producers.get();
                        long j = this.maxChildRequested;
                        long j2 = j;
                        for (InnerProducer innerProducer : innerProducerArr) {
                            j2 = Math.max(j2, innerProducer.totalRequested.get());
                        }
                        long j3 = this.maxUpstreamRequested;
                        Producer producer = this.producer;
                        j = j2 - j;
                        if (j != 0) {
                            this.maxChildRequested = j2;
                            if (producer == null) {
                                j3 += j;
                                if (j3 < 0) {
                                    j3 = Long.MAX_VALUE;
                                }
                                this.maxUpstreamRequested = j3;
                            } else if (j3 != 0) {
                                this.maxUpstreamRequested = 0;
                                producer.request(j3 + j);
                            } else {
                                producer.request(j);
                            }
                        } else if (!(j3 == 0 || producer == null)) {
                            this.maxUpstreamRequested = 0;
                            producer.request(j3);
                        }
                        synchronized (this) {
                            if (this.missed) {
                                this.missed = false;
                            } else {
                                this.emitting = false;
                                return;
                            }
                        }
                    }
                }
            }
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                try {
                    this.buffer.complete();
                    replay();
                } finally {
                    unsubscribe();
                }
            }
        }

        public void onError(Throwable th) {
            if (!this.done) {
                this.done = true;
                try {
                    this.buffer.error(th);
                    replay();
                } finally {
                    unsubscribe();
                }
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                this.buffer.next(t);
                replay();
            }
        }

        void remove(InnerProducer<T> innerProducer) {
            InnerProducer[] innerProducerArr;
            Object obj;
            do {
                innerProducerArr = (InnerProducer[]) this.producers.get();
                if (innerProducerArr != EMPTY && innerProducerArr != TERMINATED) {
                    int i = -1;
                    int length = innerProducerArr.length;
                    for (int i2 = 0; i2 < length; i2++) {
                        if (innerProducerArr[i2].equals(innerProducer)) {
                            i = i2;
                            break;
                        }
                    }
                    if (i < 0) {
                        return;
                    }
                    if (length == 1) {
                        obj = EMPTY;
                    } else {
                        obj = new InnerProducer[(length - 1)];
                        System.arraycopy(innerProducerArr, 0, obj, 0, i);
                        System.arraycopy(innerProducerArr, i + 1, obj, i, (length - i) - 1);
                    }
                } else {
                    return;
                }
            } while (!this.producers.compareAndSet(innerProducerArr, obj));
        }

        void replay() {
            for (InnerProducer replay : (InnerProducer[]) this.producers.get()) {
                this.buffer.replay(replay);
            }
        }

        public void setProducer(Producer producer) {
            if (this.producer != null) {
                throw new IllegalStateException("Only a single producer can be set on a Subscriber.");
            }
            this.producer = producer;
            manageRequests();
            replay();
        }
    }

    static final class SizeAndTimeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
        private static final long serialVersionUID = 3457957419649567404L;
        final int limit;
        final long maxAgeInMillis;
        final Scheduler scheduler;

        public SizeAndTimeBoundReplayBuffer(int i, long j, Scheduler scheduler) {
            this.scheduler = scheduler;
            this.limit = i;
            this.maxAgeInMillis = j;
        }

        Object enterTransform(Object obj) {
            return new Timestamped(this.scheduler.now(), obj);
        }

        Object leaveTransform(Object obj) {
            return ((Timestamped) obj).getValue();
        }

        void truncate() {
            long now = this.scheduler.now();
            long j = this.maxAgeInMillis;
            Node node = (Node) get();
            int i = 0;
            Node node2 = (Node) node.get();
            Node node3 = node;
            while (node2 != null) {
                int i2;
                if (this.size <= this.limit) {
                    if (((Timestamped) node2.value).getTimestampMillis() > now - j) {
                        break;
                    }
                    i2 = i + 1;
                    this.size--;
                    i = i2;
                    node3 = node2;
                    node2 = (Node) node2.get();
                } else {
                    i2 = i + 1;
                    this.size--;
                    i = i2;
                    node3 = node2;
                    node2 = (Node) node2.get();
                }
            }
            if (i != 0) {
                setFirst(node3);
            }
        }

        void truncateFinal() {
            long now = this.scheduler.now();
            long j = this.maxAgeInMillis;
            Node node = (Node) get();
            int i = 0;
            Node node2 = (Node) node.get();
            Node node3 = node;
            while (node2 != null && this.size > 1 && ((Timestamped) node2.value).getTimestampMillis() <= now - j) {
                int i2 = i + 1;
                this.size--;
                i = i2;
                node3 = node2;
                node2 = (Node) node2.get();
            }
            if (i != 0) {
                setFirst(node3);
            }
        }
    }

    static final class SizeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
        private static final long serialVersionUID = -5898283885385201806L;
        final int limit;

        public SizeBoundReplayBuffer(int i) {
            this.limit = i;
        }

        void truncate() {
            if (this.size > this.limit) {
                removeFirst();
            }
        }
    }

    static final class UnboundedReplayBuffer<T> extends ArrayList<Object> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 7063189396499112664L;
        final NotificationLite<T> nl;
        volatile int size;

        public UnboundedReplayBuffer(int i) {
            super(i);
            this.nl = NotificationLite.instance();
        }

        public void complete() {
            add(this.nl.completed());
            this.size++;
        }

        public void error(Throwable th) {
            add(this.nl.error(th));
            this.size++;
        }

        public void next(T t) {
            add(this.nl.next(t));
            this.size++;
        }

        public void replay(InnerProducer<T> innerProducer) {
            synchronized (innerProducer) {
                if (innerProducer.emitting) {
                    innerProducer.missed = true;
                    return;
                }
                innerProducer.emitting = true;
                while (!innerProducer.isUnsubscribed()) {
                    int i = this.size;
                    Integer num = (Integer) innerProducer.index();
                    int intValue = num != null ? num.intValue() : 0;
                    long j = innerProducer.get();
                    int i2 = intValue;
                    long j2 = 0;
                    long j3 = j;
                    while (j3 != 0 && i2 < i) {
                        r8 = get(i2);
                        try {
                            if (!this.nl.accept(innerProducer.child, r8) && !innerProducer.isUnsubscribed()) {
                                i2++;
                                j3--;
                                j2++;
                            } else {
                                return;
                            }
                        } catch (Throwable th) {
                            Exceptions.throwIfFatal(th);
                            innerProducer.unsubscribe();
                            Object obj;
                            if (!this.nl.isError(obj) && !this.nl.isCompleted(obj)) {
                                innerProducer.child.onError(OnErrorThrowable.addValueAsLastCause(th, this.nl.getValue(obj)));
                                return;
                            }
                            return;
                        }
                    }
                    if (j2 != 0) {
                        innerProducer.index = Integer.valueOf(i2);
                        if (j != Long.MAX_VALUE) {
                            innerProducer.produced(j2);
                        }
                    }
                    synchronized (innerProducer) {
                        if (innerProducer.missed) {
                            innerProducer.missed = false;
                        } else {
                            innerProducer.emitting = false;
                            return;
                        }
                    }
                }
            }
        }
    }

    static {
        DEFAULT_UNBOUNDED_FACTORY = new C13591();
    }

    private OperatorReplay(OnSubscribe<T> onSubscribe, Observable<? extends T> observable, AtomicReference<ReplaySubscriber<T>> atomicReference, Func0<? extends ReplayBuffer<T>> func0) {
        super(onSubscribe);
        this.source = observable;
        this.current = atomicReference;
        this.bufferFactory = func0;
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> observable) {
        return create((Observable) observable, DEFAULT_UNBOUNDED_FACTORY);
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> observable, int i) {
        return i == Integer.MAX_VALUE ? create(observable) : create((Observable) observable, new C13655(i));
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> observable, long j, TimeUnit timeUnit, Scheduler scheduler) {
        return create(observable, j, timeUnit, scheduler, Integer.MAX_VALUE);
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> observable, long j, TimeUnit timeUnit, Scheduler scheduler, int i) {
        return create((Observable) observable, new C13666(i, timeUnit.toMillis(j), scheduler));
    }

    static <T> ConnectableObservable<T> create(Observable<? extends T> observable, Func0<? extends ReplayBuffer<T>> func0) {
        AtomicReference atomicReference = new AtomicReference();
        return new OperatorReplay(new C13677(atomicReference, func0), observable, atomicReference, func0);
    }

    public static <T, U, R> Observable<R> multicastSelector(Func0<? extends ConnectableObservable<U>> func0, Func1<? super Observable<U>, ? extends Observable<R>> func1) {
        return Observable.create(new C13612(func0, func1));
    }

    public static <T> ConnectableObservable<T> observeOn(ConnectableObservable<T> connectableObservable, Scheduler scheduler) {
        return new C13644(new C13633(connectableObservable.observeOn(scheduler)), connectableObservable);
    }

    public void connect(Action1<? super Subscription> action1) {
        Subscriber subscriber;
        Subscriber replaySubscriber;
        do {
            subscriber = (ReplaySubscriber) this.current.get();
            if (subscriber != null && !subscriber.isUnsubscribed()) {
                break;
            }
            replaySubscriber = new ReplaySubscriber(this.current, (ReplayBuffer) this.bufferFactory.call());
            replaySubscriber.init();
        } while (!this.current.compareAndSet(subscriber, replaySubscriber));
        subscriber = replaySubscriber;
        boolean z = !subscriber.shouldConnect.get() && subscriber.shouldConnect.compareAndSet(false, true);
        action1.call(subscriber);
        if (z) {
            this.source.unsafeSubscribe(subscriber);
        }
    }
}

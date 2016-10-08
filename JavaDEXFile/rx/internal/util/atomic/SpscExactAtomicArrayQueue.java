package rx.internal.util.atomic;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceArray;
import rx.internal.util.unsafe.Pow2;

public final class SpscExactAtomicArrayQueue<T> extends AtomicReferenceArray<T> implements Queue<T> {
    static final AtomicLongFieldUpdater<SpscExactAtomicArrayQueue> CONSUMER_INDEX;
    static final AtomicLongFieldUpdater<SpscExactAtomicArrayQueue> PRODUCER_INDEX;
    private static final long serialVersionUID = 6210984603741293445L;
    final int capacitySkip;
    volatile long consumerIndex;
    final int mask;
    volatile long producerIndex;

    static {
        PRODUCER_INDEX = AtomicLongFieldUpdater.newUpdater(SpscExactAtomicArrayQueue.class, "producerIndex");
        CONSUMER_INDEX = AtomicLongFieldUpdater.newUpdater(SpscExactAtomicArrayQueue.class, "consumerIndex");
    }

    public SpscExactAtomicArrayQueue(int i) {
        super(Pow2.roundToPowerOfTwo(i));
        int length = length();
        this.mask = length - 1;
        this.capacitySkip = length - i;
    }

    public boolean add(T t) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends T> collection) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        while (true) {
            if (poll() == null && isEmpty()) {
                return;
            }
        }
    }

    public boolean contains(Object obj) {
        throw new UnsupportedOperationException();
    }

    public boolean containsAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public T element() {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        return this.producerIndex == this.consumerIndex;
    }

    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    public boolean offer(T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        long j = this.producerIndex;
        int i = this.mask;
        if (get(((int) (((long) this.capacitySkip) + j)) & i) != null) {
            return false;
        }
        int i2 = (int) j;
        PRODUCER_INDEX.lazySet(this, j + 1);
        lazySet(i2 & i, t);
        return true;
    }

    public T peek() {
        return get(((int) this.consumerIndex) & this.mask);
    }

    public T poll() {
        long j = this.consumerIndex;
        int i = this.mask & ((int) j);
        T t = get(i);
        if (t == null) {
            return null;
        }
        CONSUMER_INDEX.lazySet(this, j + 1);
        lazySet(i, null);
        return t;
    }

    public T remove() {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object obj) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        long j = this.consumerIndex;
        while (true) {
            long j2 = this.producerIndex;
            long j3 = this.consumerIndex;
            if (j == j3) {
                return (int) (j2 - j3);
            }
            j = j3;
        }
    }

    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    public <E> E[] toArray(E[] eArr) {
        throw new UnsupportedOperationException();
    }
}

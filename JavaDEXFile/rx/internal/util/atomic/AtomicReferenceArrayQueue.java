package rx.internal.util.atomic;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReferenceArray;
import rx.internal.util.unsafe.Pow2;

abstract class AtomicReferenceArrayQueue<E> extends AbstractQueue<E> {
    protected final AtomicReferenceArray<E> buffer;
    protected final int mask;

    public AtomicReferenceArrayQueue(int i) {
        int roundToPowerOfTwo = Pow2.roundToPowerOfTwo(i);
        this.mask = roundToPowerOfTwo - 1;
        this.buffer = new AtomicReferenceArray(roundToPowerOfTwo);
    }

    protected final int calcElementOffset(long j) {
        return ((int) j) & this.mask;
    }

    protected final int calcElementOffset(long j, int i) {
        return ((int) j) & i;
    }

    public void clear() {
        while (true) {
            if (poll() == null && isEmpty()) {
                return;
            }
        }
    }

    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    protected final E lpElement(int i) {
        return this.buffer.get(i);
    }

    protected final E lpElement(AtomicReferenceArray<E> atomicReferenceArray, int i) {
        return atomicReferenceArray.get(i);
    }

    protected final E lvElement(int i) {
        return lvElement(this.buffer, i);
    }

    protected final E lvElement(AtomicReferenceArray<E> atomicReferenceArray, int i) {
        return atomicReferenceArray.get(i);
    }

    protected final void soElement(int i, E e) {
        this.buffer.lazySet(i, e);
    }

    protected final void soElement(AtomicReferenceArray<E> atomicReferenceArray, int i, E e) {
        atomicReferenceArray.lazySet(i, e);
    }

    protected final void spElement(int i, E e) {
        this.buffer.lazySet(i, e);
    }

    protected final void spElement(AtomicReferenceArray<E> atomicReferenceArray, int i, E e) {
        atomicReferenceArray.lazySet(i, e);
    }

    protected final void svElement(AtomicReferenceArray<E> atomicReferenceArray, int i, E e) {
        atomicReferenceArray.set(i, e);
    }
}

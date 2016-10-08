package rx.singles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import rx.Single;
import rx.SingleSubscriber;
import rx.annotations.Experimental;
import rx.internal.operators.BlockingOperatorToFuture;
import rx.internal.util.BlockingUtils;

@Experimental
public class BlockingSingle<T> {
    private final Single<? extends T> single;

    /* renamed from: rx.singles.BlockingSingle.1 */
    class C15021 extends SingleSubscriber<T> {
        final /* synthetic */ CountDownLatch val$latch;
        final /* synthetic */ AtomicReference val$returnException;
        final /* synthetic */ AtomicReference val$returnItem;

        C15021(AtomicReference atomicReference, CountDownLatch countDownLatch, AtomicReference atomicReference2) {
            this.val$returnItem = atomicReference;
            this.val$latch = countDownLatch;
            this.val$returnException = atomicReference2;
        }

        public void onError(Throwable th) {
            this.val$returnException.set(th);
            this.val$latch.countDown();
        }

        public void onSuccess(T t) {
            this.val$returnItem.set(t);
            this.val$latch.countDown();
        }
    }

    private BlockingSingle(Single<? extends T> single) {
        this.single = single;
    }

    @Experimental
    public static <T> BlockingSingle<T> from(Single<? extends T> single) {
        return new BlockingSingle(single);
    }

    @Experimental
    public Future<T> toFuture() {
        return BlockingOperatorToFuture.toFuture(this.single.toObservable());
    }

    @Experimental
    public T value() {
        AtomicReference atomicReference = new AtomicReference();
        AtomicReference atomicReference2 = new AtomicReference();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        BlockingUtils.awaitForComplete(countDownLatch, this.single.subscribe(new C15021(atomicReference, countDownLatch, atomicReference2)));
        Throwable th = (Throwable) atomicReference2.get();
        if (th == null) {
            return atomicReference.get();
        }
        if (th instanceof RuntimeException) {
            throw ((RuntimeException) th);
        }
        throw new RuntimeException(th);
    }
}

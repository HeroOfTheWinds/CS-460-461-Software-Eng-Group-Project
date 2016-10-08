package rx.internal.util.unsafe;

abstract class SpscArrayQueueConsumerField<E> extends SpscArrayQueueL2Pad<E> {
    protected static final long C_INDEX_OFFSET;
    protected long consumerIndex;

    static {
        C_INDEX_OFFSET = UnsafeAccess.addressOf(SpscArrayQueueConsumerField.class, "consumerIndex");
    }

    public SpscArrayQueueConsumerField(int i) {
        super(i);
    }
}

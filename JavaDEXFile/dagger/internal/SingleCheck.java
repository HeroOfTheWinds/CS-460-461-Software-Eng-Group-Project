package dagger.internal;

import dagger.Lazy;
import javax.inject.Provider;

public final class SingleCheck<T> implements Provider<T>, Lazy<T> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final Object UNINITIALIZED;
    private volatile Factory<T> factory;
    private volatile Object instance;

    static {
        $assertionsDisabled = !SingleCheck.class.desiredAssertionStatus();
        UNINITIALIZED = new Object();
    }

    private SingleCheck(Factory<T> factory) {
        this.instance = UNINITIALIZED;
        if ($assertionsDisabled || factory != null) {
            this.factory = factory;
            return;
        }
        throw new AssertionError();
    }

    public static <T> Provider<T> provider(Factory<T> factory) {
        return new SingleCheck((Factory) Preconditions.checkNotNull(factory));
    }

    public T get() {
        Factory factory = this.factory;
        if (this.instance == UNINITIALIZED) {
            this.instance = factory.get();
            this.factory = null;
        }
        return this.instance;
    }
}

package dagger.internal;

import dagger.Lazy;
import javax.inject.Provider;

public final class ProviderOfLazy<T> implements Provider<Lazy<T>> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<T> provider;

    static {
        $assertionsDisabled = !ProviderOfLazy.class.desiredAssertionStatus();
    }

    private ProviderOfLazy(Provider<T> provider) {
        if ($assertionsDisabled || provider != null) {
            this.provider = provider;
            return;
        }
        throw new AssertionError();
    }

    public static <T> Provider<Lazy<T>> create(Provider<T> provider) {
        return new ProviderOfLazy((Provider) Preconditions.checkNotNull(provider));
    }

    public Lazy<T> get() {
        return DoubleCheck.lazy(this.provider);
    }
}

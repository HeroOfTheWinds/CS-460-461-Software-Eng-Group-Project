package com.upsight.android.googlepushservices.internal;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class PushModule_ProvidePushConfigManagerFactory implements Factory<PushConfigManager> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final PushModule module;
    private final Provider<UpsightContext> upsightProvider;

    static {
        $assertionsDisabled = !PushModule_ProvidePushConfigManagerFactory.class.desiredAssertionStatus();
    }

    public PushModule_ProvidePushConfigManagerFactory(PushModule pushModule, Provider<UpsightContext> provider) {
        if ($assertionsDisabled || pushModule != null) {
            this.module = pushModule;
            if ($assertionsDisabled || provider != null) {
                this.upsightProvider = provider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static Factory<PushConfigManager> create(PushModule pushModule, Provider<UpsightContext> provider) {
        return new PushModule_ProvidePushConfigManagerFactory(pushModule, provider);
    }

    public PushConfigManager get() {
        return (PushConfigManager) Preconditions.checkNotNull(this.module.providePushConfigManager((UpsightContext) this.upsightProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}

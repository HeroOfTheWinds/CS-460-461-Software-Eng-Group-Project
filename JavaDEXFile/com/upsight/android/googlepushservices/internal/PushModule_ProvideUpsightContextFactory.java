package com.upsight.android.googlepushservices.internal;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class PushModule_ProvideUpsightContextFactory implements Factory<UpsightContext> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final PushModule module;

    static {
        $assertionsDisabled = !PushModule_ProvideUpsightContextFactory.class.desiredAssertionStatus();
    }

    public PushModule_ProvideUpsightContextFactory(PushModule pushModule) {
        if ($assertionsDisabled || pushModule != null) {
            this.module = pushModule;
            return;
        }
        throw new AssertionError();
    }

    public static Factory<UpsightContext> create(PushModule pushModule) {
        return new PushModule_ProvideUpsightContextFactory(pushModule);
    }

    public UpsightContext get() {
        return (UpsightContext) Preconditions.checkNotNull(this.module.provideUpsightContext(), "Cannot return null from a non-@Nullable @Provides method");
    }
}

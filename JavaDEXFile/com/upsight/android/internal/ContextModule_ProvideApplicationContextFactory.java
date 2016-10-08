package com.upsight.android.internal;

import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class ContextModule_ProvideApplicationContextFactory implements Factory<Context> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final ContextModule module;

    static {
        $assertionsDisabled = !ContextModule_ProvideApplicationContextFactory.class.desiredAssertionStatus();
    }

    public ContextModule_ProvideApplicationContextFactory(ContextModule contextModule) {
        if ($assertionsDisabled || contextModule != null) {
            this.module = contextModule;
            return;
        }
        throw new AssertionError();
    }

    public static Factory<Context> create(ContextModule contextModule) {
        return new ContextModule_ProvideApplicationContextFactory(contextModule);
    }

    public Context get() {
        return (Context) Preconditions.checkNotNull(this.module.provideApplicationContext(), "Cannot return null from a non-@Nullable @Provides method");
    }
}

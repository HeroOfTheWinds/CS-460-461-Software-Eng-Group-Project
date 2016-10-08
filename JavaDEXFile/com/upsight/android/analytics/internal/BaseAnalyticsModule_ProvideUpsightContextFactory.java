package com.upsight.android.analytics.internal;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class BaseAnalyticsModule_ProvideUpsightContextFactory implements Factory<UpsightContext> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final BaseAnalyticsModule module;

    static {
        $assertionsDisabled = !BaseAnalyticsModule_ProvideUpsightContextFactory.class.desiredAssertionStatus();
    }

    public BaseAnalyticsModule_ProvideUpsightContextFactory(BaseAnalyticsModule baseAnalyticsModule) {
        if ($assertionsDisabled || baseAnalyticsModule != null) {
            this.module = baseAnalyticsModule;
            return;
        }
        throw new AssertionError();
    }

    public static Factory<UpsightContext> create(BaseAnalyticsModule baseAnalyticsModule) {
        return new BaseAnalyticsModule_ProvideUpsightContextFactory(baseAnalyticsModule);
    }

    public UpsightContext get() {
        return (UpsightContext) Preconditions.checkNotNull(this.module.provideUpsightContext(), "Cannot return null from a non-@Nullable @Provides method");
    }
}

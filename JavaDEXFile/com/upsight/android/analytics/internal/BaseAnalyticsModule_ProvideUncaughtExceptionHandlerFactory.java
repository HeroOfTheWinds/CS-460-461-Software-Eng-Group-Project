package com.upsight.android.analytics.internal;

import com.upsight.android.internal.util.Opt;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.lang.Thread.UncaughtExceptionHandler;

public final class BaseAnalyticsModule_ProvideUncaughtExceptionHandlerFactory implements Factory<Opt<UncaughtExceptionHandler>> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final BaseAnalyticsModule module;

    static {
        $assertionsDisabled = !BaseAnalyticsModule_ProvideUncaughtExceptionHandlerFactory.class.desiredAssertionStatus();
    }

    public BaseAnalyticsModule_ProvideUncaughtExceptionHandlerFactory(BaseAnalyticsModule baseAnalyticsModule) {
        if ($assertionsDisabled || baseAnalyticsModule != null) {
            this.module = baseAnalyticsModule;
            return;
        }
        throw new AssertionError();
    }

    public static Factory<Opt<UncaughtExceptionHandler>> create(BaseAnalyticsModule baseAnalyticsModule) {
        return new BaseAnalyticsModule_ProvideUncaughtExceptionHandlerFactory(baseAnalyticsModule);
    }

    public Opt<UncaughtExceptionHandler> get() {
        return (Opt) Preconditions.checkNotNull(this.module.provideUncaughtExceptionHandler(), "Cannot return null from a non-@Nullable @Provides method");
    }
}

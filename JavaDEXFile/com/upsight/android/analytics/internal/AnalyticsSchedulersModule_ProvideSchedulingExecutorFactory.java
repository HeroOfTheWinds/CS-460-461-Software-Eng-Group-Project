package com.upsight.android.analytics.internal;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import rx.Scheduler;

public final class AnalyticsSchedulersModule_ProvideSchedulingExecutorFactory implements Factory<Scheduler> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final AnalyticsSchedulersModule module;

    static {
        $assertionsDisabled = !AnalyticsSchedulersModule_ProvideSchedulingExecutorFactory.class.desiredAssertionStatus();
    }

    public AnalyticsSchedulersModule_ProvideSchedulingExecutorFactory(AnalyticsSchedulersModule analyticsSchedulersModule) {
        if ($assertionsDisabled || analyticsSchedulersModule != null) {
            this.module = analyticsSchedulersModule;
            return;
        }
        throw new AssertionError();
    }

    public static Factory<Scheduler> create(AnalyticsSchedulersModule analyticsSchedulersModule) {
        return new AnalyticsSchedulersModule_ProvideSchedulingExecutorFactory(analyticsSchedulersModule);
    }

    public Scheduler get() {
        return (Scheduler) Preconditions.checkNotNull(this.module.provideSchedulingExecutor(), "Cannot return null from a non-@Nullable @Provides method");
    }
}

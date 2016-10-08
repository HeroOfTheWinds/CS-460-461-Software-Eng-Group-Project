package com.upsight.android.analytics.internal;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import rx.Scheduler;

public final class AnalyticsSchedulersModule_ProvideSendingExecutorFactory implements Factory<Scheduler> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final AnalyticsSchedulersModule module;

    static {
        $assertionsDisabled = !AnalyticsSchedulersModule_ProvideSendingExecutorFactory.class.desiredAssertionStatus();
    }

    public AnalyticsSchedulersModule_ProvideSendingExecutorFactory(AnalyticsSchedulersModule analyticsSchedulersModule) {
        if ($assertionsDisabled || analyticsSchedulersModule != null) {
            this.module = analyticsSchedulersModule;
            return;
        }
        throw new AssertionError();
    }

    public static Factory<Scheduler> create(AnalyticsSchedulersModule analyticsSchedulersModule) {
        return new AnalyticsSchedulersModule_ProvideSendingExecutorFactory(analyticsSchedulersModule);
    }

    public Scheduler get() {
        return (Scheduler) Preconditions.checkNotNull(this.module.provideSendingExecutor(), "Cannot return null from a non-@Nullable @Provides method");
    }
}

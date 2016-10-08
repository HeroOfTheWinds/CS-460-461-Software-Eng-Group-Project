package com.upsight.android.internal;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import rx.Scheduler;

public final class SchedulersModule_ProvideSubscribeOnSchedulerFactory implements Factory<Scheduler> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final SchedulersModule module;

    static {
        $assertionsDisabled = !SchedulersModule_ProvideSubscribeOnSchedulerFactory.class.desiredAssertionStatus();
    }

    public SchedulersModule_ProvideSubscribeOnSchedulerFactory(SchedulersModule schedulersModule) {
        if ($assertionsDisabled || schedulersModule != null) {
            this.module = schedulersModule;
            return;
        }
        throw new AssertionError();
    }

    public static Factory<Scheduler> create(SchedulersModule schedulersModule) {
        return new SchedulersModule_ProvideSubscribeOnSchedulerFactory(schedulersModule);
    }

    public Scheduler get() {
        return (Scheduler) Preconditions.checkNotNull(this.module.provideSubscribeOnScheduler(), "Cannot return null from a non-@Nullable @Provides method");
    }
}

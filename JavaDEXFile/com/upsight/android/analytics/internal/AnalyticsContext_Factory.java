package com.upsight.android.analytics.internal;

import com.upsight.android.UpsightContext;
import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.MembersInjectors;
import javax.inject.Provider;

public final class AnalyticsContext_Factory implements Factory<AnalyticsContext> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final MembersInjector<AnalyticsContext> analyticsContextMembersInjector;
    private final Provider<UpsightContext> upsightProvider;

    static {
        $assertionsDisabled = !AnalyticsContext_Factory.class.desiredAssertionStatus();
    }

    public AnalyticsContext_Factory(MembersInjector<AnalyticsContext> membersInjector, Provider<UpsightContext> provider) {
        if ($assertionsDisabled || membersInjector != null) {
            this.analyticsContextMembersInjector = membersInjector;
            if ($assertionsDisabled || provider != null) {
                this.upsightProvider = provider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static Factory<AnalyticsContext> create(MembersInjector<AnalyticsContext> membersInjector, Provider<UpsightContext> provider) {
        return new AnalyticsContext_Factory(membersInjector, provider);
    }

    public AnalyticsContext get() {
        return (AnalyticsContext) MembersInjectors.injectMembers(this.analyticsContextMembersInjector, new AnalyticsContext((UpsightContext) this.upsightProvider.get()));
    }
}

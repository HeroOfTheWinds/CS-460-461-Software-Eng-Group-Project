package com.upsight.android.analytics.internal.provider;

import com.upsight.android.UpsightContext;
import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.MembersInjectors;
import javax.inject.Provider;

public final class OptOutStatus_Factory implements Factory<OptOutStatus> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final MembersInjector<OptOutStatus> optOutStatusMembersInjector;
    private final Provider<UpsightContext> upsightProvider;

    static {
        $assertionsDisabled = !OptOutStatus_Factory.class.desiredAssertionStatus();
    }

    public OptOutStatus_Factory(MembersInjector<OptOutStatus> membersInjector, Provider<UpsightContext> provider) {
        if ($assertionsDisabled || membersInjector != null) {
            this.optOutStatusMembersInjector = membersInjector;
            if ($assertionsDisabled || provider != null) {
                this.upsightProvider = provider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static Factory<OptOutStatus> create(MembersInjector<OptOutStatus> membersInjector, Provider<UpsightContext> provider) {
        return new OptOutStatus_Factory(membersInjector, provider);
    }

    public OptOutStatus get() {
        return (OptOutStatus) MembersInjectors.injectMembers(this.optOutStatusMembersInjector, new OptOutStatus((UpsightContext) this.upsightProvider.get()));
    }
}

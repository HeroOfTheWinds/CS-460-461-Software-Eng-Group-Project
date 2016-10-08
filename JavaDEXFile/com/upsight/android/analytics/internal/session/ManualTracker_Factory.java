package com.upsight.android.analytics.internal.session;

import com.upsight.android.UpsightContext;
import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.MembersInjectors;
import javax.inject.Provider;

public final class ManualTracker_Factory implements Factory<ManualTracker> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final MembersInjector<ManualTracker> manualTrackerMembersInjector;
    private final Provider<SessionManager> sessionManagerProvider;
    private final Provider<UpsightContext> upsightProvider;

    static {
        $assertionsDisabled = !ManualTracker_Factory.class.desiredAssertionStatus();
    }

    public ManualTracker_Factory(MembersInjector<ManualTracker> membersInjector, Provider<SessionManager> provider, Provider<UpsightContext> provider2) {
        if ($assertionsDisabled || membersInjector != null) {
            this.manualTrackerMembersInjector = membersInjector;
            if ($assertionsDisabled || provider != null) {
                this.sessionManagerProvider = provider;
                if ($assertionsDisabled || provider2 != null) {
                    this.upsightProvider = provider2;
                    return;
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static Factory<ManualTracker> create(MembersInjector<ManualTracker> membersInjector, Provider<SessionManager> provider, Provider<UpsightContext> provider2) {
        return new ManualTracker_Factory(membersInjector, provider, provider2);
    }

    public ManualTracker get() {
        return (ManualTracker) MembersInjectors.injectMembers(this.manualTrackerMembersInjector, new ManualTracker((SessionManager) this.sessionManagerProvider.get(), (UpsightContext) this.upsightProvider.get()));
    }
}

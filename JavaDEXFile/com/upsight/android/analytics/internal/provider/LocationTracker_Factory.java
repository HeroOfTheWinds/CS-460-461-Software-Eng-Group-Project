package com.upsight.android.analytics.internal.provider;

import com.upsight.android.UpsightContext;
import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.MembersInjectors;
import javax.inject.Provider;

public final class LocationTracker_Factory implements Factory<LocationTracker> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final MembersInjector<LocationTracker> locationTrackerMembersInjector;
    private final Provider<UpsightContext> upsightProvider;

    static {
        $assertionsDisabled = !LocationTracker_Factory.class.desiredAssertionStatus();
    }

    public LocationTracker_Factory(MembersInjector<LocationTracker> membersInjector, Provider<UpsightContext> provider) {
        if ($assertionsDisabled || membersInjector != null) {
            this.locationTrackerMembersInjector = membersInjector;
            if ($assertionsDisabled || provider != null) {
                this.upsightProvider = provider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static Factory<LocationTracker> create(MembersInjector<LocationTracker> membersInjector, Provider<UpsightContext> provider) {
        return new LocationTracker_Factory(membersInjector, provider);
    }

    public LocationTracker get() {
        return (LocationTracker) MembersInjectors.injectMembers(this.locationTrackerMembersInjector, new LocationTracker((UpsightContext) this.upsightProvider.get()));
    }
}

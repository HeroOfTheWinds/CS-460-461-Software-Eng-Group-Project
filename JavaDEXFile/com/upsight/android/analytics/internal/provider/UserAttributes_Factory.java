package com.upsight.android.analytics.internal.provider;

import com.upsight.android.UpsightContext;
import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.MembersInjectors;
import javax.inject.Provider;

public final class UserAttributes_Factory implements Factory<UserAttributes> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<UpsightContext> upsightProvider;
    private final MembersInjector<UserAttributes> userAttributesMembersInjector;

    static {
        $assertionsDisabled = !UserAttributes_Factory.class.desiredAssertionStatus();
    }

    public UserAttributes_Factory(MembersInjector<UserAttributes> membersInjector, Provider<UpsightContext> provider) {
        if ($assertionsDisabled || membersInjector != null) {
            this.userAttributesMembersInjector = membersInjector;
            if ($assertionsDisabled || provider != null) {
                this.upsightProvider = provider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static Factory<UserAttributes> create(MembersInjector<UserAttributes> membersInjector, Provider<UpsightContext> provider) {
        return new UserAttributes_Factory(membersInjector, provider);
    }

    public UserAttributes get() {
        return (UserAttributes) MembersInjectors.injectMembers(this.userAttributesMembersInjector, new UserAttributes((UpsightContext) this.upsightProvider.get()));
    }
}

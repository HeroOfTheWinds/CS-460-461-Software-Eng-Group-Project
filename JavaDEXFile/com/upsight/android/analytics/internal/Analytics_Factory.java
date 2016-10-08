package com.upsight.android.analytics.internal;

import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.UpsightGooglePlayHelper;
import com.upsight.android.analytics.UpsightLifeCycleTracker;
import com.upsight.android.analytics.internal.association.AssociationManager;
import com.upsight.android.analytics.internal.dispatcher.schema.SchemaSelectorBuilder;
import com.upsight.android.analytics.internal.session.SessionManager;
import com.upsight.android.analytics.provider.UpsightLocationTracker;
import com.upsight.android.analytics.provider.UpsightOptOutStatus;
import com.upsight.android.analytics.provider.UpsightUserAttributes;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class Analytics_Factory implements Factory<Analytics> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<AssociationManager> associationManagerProvider;
    private final Provider<UpsightGooglePlayHelper> googlePlayHelperProvider;
    private final Provider<UpsightLifeCycleTracker> lifeCycleTrackerProvider;
    private final Provider<UpsightLocationTracker> locationTrackerProvider;
    private final Provider<UpsightOptOutStatus> optOutStatusProvider;
    private final Provider<SchemaSelectorBuilder> schemaSelectorProvider;
    private final Provider<SessionManager> sessionManagerProvider;
    private final Provider<UpsightContext> upsightProvider;
    private final Provider<UpsightUserAttributes> userAttributesProvider;

    static {
        $assertionsDisabled = !Analytics_Factory.class.desiredAssertionStatus();
    }

    public Analytics_Factory(Provider<UpsightContext> provider, Provider<UpsightLifeCycleTracker> provider2, Provider<SessionManager> provider3, Provider<SchemaSelectorBuilder> provider4, Provider<AssociationManager> provider5, Provider<UpsightOptOutStatus> provider6, Provider<UpsightLocationTracker> provider7, Provider<UpsightUserAttributes> provider8, Provider<UpsightGooglePlayHelper> provider9) {
        if ($assertionsDisabled || provider != null) {
            this.upsightProvider = provider;
            if ($assertionsDisabled || provider2 != null) {
                this.lifeCycleTrackerProvider = provider2;
                if ($assertionsDisabled || provider3 != null) {
                    this.sessionManagerProvider = provider3;
                    if ($assertionsDisabled || provider4 != null) {
                        this.schemaSelectorProvider = provider4;
                        if ($assertionsDisabled || provider5 != null) {
                            this.associationManagerProvider = provider5;
                            if ($assertionsDisabled || provider6 != null) {
                                this.optOutStatusProvider = provider6;
                                if ($assertionsDisabled || provider7 != null) {
                                    this.locationTrackerProvider = provider7;
                                    if ($assertionsDisabled || provider8 != null) {
                                        this.userAttributesProvider = provider8;
                                        if ($assertionsDisabled || provider9 != null) {
                                            this.googlePlayHelperProvider = provider9;
                                            return;
                                        }
                                        throw new AssertionError();
                                    }
                                    throw new AssertionError();
                                }
                                throw new AssertionError();
                            }
                            throw new AssertionError();
                        }
                        throw new AssertionError();
                    }
                    throw new AssertionError();
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static Factory<Analytics> create(Provider<UpsightContext> provider, Provider<UpsightLifeCycleTracker> provider2, Provider<SessionManager> provider3, Provider<SchemaSelectorBuilder> provider4, Provider<AssociationManager> provider5, Provider<UpsightOptOutStatus> provider6, Provider<UpsightLocationTracker> provider7, Provider<UpsightUserAttributes> provider8, Provider<UpsightGooglePlayHelper> provider9) {
        return new Analytics_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public Analytics get() {
        return new Analytics((UpsightContext) this.upsightProvider.get(), (UpsightLifeCycleTracker) this.lifeCycleTrackerProvider.get(), (SessionManager) this.sessionManagerProvider.get(), (SchemaSelectorBuilder) this.schemaSelectorProvider.get(), (AssociationManager) this.associationManagerProvider.get(), (UpsightOptOutStatus) this.optOutStatusProvider.get(), (UpsightLocationTracker) this.locationTrackerProvider.get(), (UpsightUserAttributes) this.userAttributesProvider.get(), (UpsightGooglePlayHelper) this.googlePlayHelperProvider.get());
    }
}

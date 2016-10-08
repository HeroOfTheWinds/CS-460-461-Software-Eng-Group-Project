package com.upsight.android.googlepushservices.internal;

import com.upsight.android.analytics.internal.session.SessionManager;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class PushClickIntentService_MembersInjector implements MembersInjector<PushClickIntentService> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<SessionManager> mSessionManagerProvider;

    static {
        $assertionsDisabled = !PushClickIntentService_MembersInjector.class.desiredAssertionStatus();
    }

    public PushClickIntentService_MembersInjector(Provider<SessionManager> provider) {
        if ($assertionsDisabled || provider != null) {
            this.mSessionManagerProvider = provider;
            return;
        }
        throw new AssertionError();
    }

    public static MembersInjector<PushClickIntentService> create(Provider<SessionManager> provider) {
        return new PushClickIntentService_MembersInjector(provider);
    }

    public static void injectMSessionManager(PushClickIntentService pushClickIntentService, Provider<SessionManager> provider) {
        pushClickIntentService.mSessionManager = (SessionManager) provider.get();
    }

    public void injectMembers(PushClickIntentService pushClickIntentService) {
        if (pushClickIntentService == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        pushClickIntentService.mSessionManager = (SessionManager) this.mSessionManagerProvider.get();
    }
}

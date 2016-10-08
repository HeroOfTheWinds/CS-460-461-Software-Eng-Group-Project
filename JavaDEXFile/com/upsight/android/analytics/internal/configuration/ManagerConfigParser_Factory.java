package com.upsight.android.analytics.internal.configuration;

import com.google.gson.Gson;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ManagerConfigParser_Factory implements Factory<ManagerConfigParser> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<Gson> gsonProvider;

    static {
        $assertionsDisabled = !ManagerConfigParser_Factory.class.desiredAssertionStatus();
    }

    public ManagerConfigParser_Factory(Provider<Gson> provider) {
        if ($assertionsDisabled || provider != null) {
            this.gsonProvider = provider;
            return;
        }
        throw new AssertionError();
    }

    public static Factory<ManagerConfigParser> create(Provider<Gson> provider) {
        return new ManagerConfigParser_Factory(provider);
    }

    public ManagerConfigParser get() {
        return new ManagerConfigParser((Gson) this.gsonProvider.get());
    }
}

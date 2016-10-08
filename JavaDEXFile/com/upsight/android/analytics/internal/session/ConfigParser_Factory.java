package com.upsight.android.analytics.internal.session;

import com.google.gson.Gson;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ConfigParser_Factory implements Factory<ConfigParser> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<Gson> gsonProvider;

    static {
        $assertionsDisabled = !ConfigParser_Factory.class.desiredAssertionStatus();
    }

    public ConfigParser_Factory(Provider<Gson> provider) {
        if ($assertionsDisabled || provider != null) {
            this.gsonProvider = provider;
            return;
        }
        throw new AssertionError();
    }

    public static Factory<ConfigParser> create(Provider<Gson> provider) {
        return new ConfigParser_Factory(provider);
    }

    public ConfigParser get() {
        return new ConfigParser((Gson) this.gsonProvider.get());
    }
}

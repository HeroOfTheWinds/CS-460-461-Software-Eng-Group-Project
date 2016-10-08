package com.upsight.android.analytics.internal.dispatcher;

import com.google.gson.Gson;
import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ConfigParser_Factory implements Factory<ConfigParser> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<Gson> gsonProvider;
    private final Provider<UpsightContext> upsightProvider;

    static {
        $assertionsDisabled = !ConfigParser_Factory.class.desiredAssertionStatus();
    }

    public ConfigParser_Factory(Provider<UpsightContext> provider, Provider<Gson> provider2) {
        if ($assertionsDisabled || provider != null) {
            this.upsightProvider = provider;
            if ($assertionsDisabled || provider2 != null) {
                this.gsonProvider = provider2;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static Factory<ConfigParser> create(Provider<UpsightContext> provider, Provider<Gson> provider2) {
        return new ConfigParser_Factory(provider, provider2);
    }

    public ConfigParser get() {
        return new ConfigParser((UpsightContext) this.upsightProvider.get(), (Gson) this.gsonProvider.get());
    }
}

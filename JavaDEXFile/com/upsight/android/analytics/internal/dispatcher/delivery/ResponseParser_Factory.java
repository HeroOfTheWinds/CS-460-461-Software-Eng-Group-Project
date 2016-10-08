package com.upsight.android.analytics.internal.dispatcher.delivery;

import com.google.gson.Gson;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ResponseParser_Factory implements Factory<ResponseParser> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<Gson> gsonProvider;

    static {
        $assertionsDisabled = !ResponseParser_Factory.class.desiredAssertionStatus();
    }

    public ResponseParser_Factory(Provider<Gson> provider) {
        if ($assertionsDisabled || provider != null) {
            this.gsonProvider = provider;
            return;
        }
        throw new AssertionError();
    }

    public static Factory<ResponseParser> create(Provider<Gson> provider) {
        return new ResponseParser_Factory(provider);
    }

    public ResponseParser get() {
        return new ResponseParser((Gson) this.gsonProvider.get());
    }
}

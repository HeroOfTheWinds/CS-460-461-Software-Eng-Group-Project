package com.upsight.android.internal;

import com.google.gson.JsonParser;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class JsonModule_ProvideJsonParserFactory implements Factory<JsonParser> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final JsonModule module;

    static {
        $assertionsDisabled = !JsonModule_ProvideJsonParserFactory.class.desiredAssertionStatus();
    }

    public JsonModule_ProvideJsonParserFactory(JsonModule jsonModule) {
        if ($assertionsDisabled || jsonModule != null) {
            this.module = jsonModule;
            return;
        }
        throw new AssertionError();
    }

    public static Factory<JsonParser> create(JsonModule jsonModule) {
        return new JsonModule_ProvideJsonParserFactory(jsonModule);
    }

    public JsonParser get() {
        return (JsonParser) Preconditions.checkNotNull(this.module.provideJsonParser(), "Cannot return null from a non-@Nullable @Provides method");
    }
}

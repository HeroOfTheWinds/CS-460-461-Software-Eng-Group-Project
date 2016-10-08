package com.upsight.android.managedvariables.internal.type;

import com.google.gson.JsonParser;
import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class UxmModule_ProvideUxmSchemaJsonParserFactory implements Factory<JsonParser> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final UxmModule module;
    private final Provider<UpsightContext> upsightProvider;

    static {
        $assertionsDisabled = !UxmModule_ProvideUxmSchemaJsonParserFactory.class.desiredAssertionStatus();
    }

    public UxmModule_ProvideUxmSchemaJsonParserFactory(UxmModule uxmModule, Provider<UpsightContext> provider) {
        if ($assertionsDisabled || uxmModule != null) {
            this.module = uxmModule;
            if ($assertionsDisabled || provider != null) {
                this.upsightProvider = provider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static Factory<JsonParser> create(UxmModule uxmModule, Provider<UpsightContext> provider) {
        return new UxmModule_ProvideUxmSchemaJsonParserFactory(uxmModule, provider);
    }

    public JsonParser get() {
        return (JsonParser) Preconditions.checkNotNull(this.module.provideUxmSchemaJsonParser((UpsightContext) this.upsightProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}

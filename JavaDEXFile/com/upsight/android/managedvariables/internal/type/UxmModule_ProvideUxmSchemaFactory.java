package com.upsight.android.managedvariables.internal.type;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class UxmModule_ProvideUxmSchemaFactory implements Factory<UxmSchema> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final UxmModule module;
    private final Provider<UpsightContext> upsightProvider;
    private final Provider<Gson> uxmSchemaGsonProvider;
    private final Provider<JsonParser> uxmSchemaJsonParserProvider;
    private final Provider<String> uxmSchemaStringProvider;

    static {
        $assertionsDisabled = !UxmModule_ProvideUxmSchemaFactory.class.desiredAssertionStatus();
    }

    public UxmModule_ProvideUxmSchemaFactory(UxmModule uxmModule, Provider<UpsightContext> provider, Provider<Gson> provider2, Provider<JsonParser> provider3, Provider<String> provider4) {
        if ($assertionsDisabled || uxmModule != null) {
            this.module = uxmModule;
            if ($assertionsDisabled || provider != null) {
                this.upsightProvider = provider;
                if ($assertionsDisabled || provider2 != null) {
                    this.uxmSchemaGsonProvider = provider2;
                    if ($assertionsDisabled || provider3 != null) {
                        this.uxmSchemaJsonParserProvider = provider3;
                        if ($assertionsDisabled || provider4 != null) {
                            this.uxmSchemaStringProvider = provider4;
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

    public static Factory<UxmSchema> create(UxmModule uxmModule, Provider<UpsightContext> provider, Provider<Gson> provider2, Provider<JsonParser> provider3, Provider<String> provider4) {
        return new UxmModule_ProvideUxmSchemaFactory(uxmModule, provider, provider2, provider3, provider4);
    }

    public UxmSchema get() {
        return (UxmSchema) Preconditions.checkNotNull(this.module.provideUxmSchema((UpsightContext) this.upsightProvider.get(), (Gson) this.uxmSchemaGsonProvider.get(), (JsonParser) this.uxmSchemaJsonParserProvider.get(), (String) this.uxmSchemaStringProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}

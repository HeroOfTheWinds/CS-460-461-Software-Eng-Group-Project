package com.upsight.android.managedvariables.internal.type;

import com.google.gson.Gson;
import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class UxmModule_ProvideUxmSchemaGsonFactory implements Factory<Gson> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final UxmModule module;
    private final Provider<UpsightContext> upsightProvider;

    static {
        $assertionsDisabled = !UxmModule_ProvideUxmSchemaGsonFactory.class.desiredAssertionStatus();
    }

    public UxmModule_ProvideUxmSchemaGsonFactory(UxmModule uxmModule, Provider<UpsightContext> provider) {
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

    public static Factory<Gson> create(UxmModule uxmModule, Provider<UpsightContext> provider) {
        return new UxmModule_ProvideUxmSchemaGsonFactory(uxmModule, provider);
    }

    public Gson get() {
        return (Gson) Preconditions.checkNotNull(this.module.provideUxmSchemaGson((UpsightContext) this.upsightProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}

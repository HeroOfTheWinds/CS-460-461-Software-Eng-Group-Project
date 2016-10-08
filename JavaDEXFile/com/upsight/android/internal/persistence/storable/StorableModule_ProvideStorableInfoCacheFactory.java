package com.upsight.android.internal.persistence.storable;

import com.google.gson.Gson;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StorableModule_ProvideStorableInfoCacheFactory implements Factory<StorableInfoCache> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<Gson> gsonProvider;
    private final StorableModule module;

    static {
        $assertionsDisabled = !StorableModule_ProvideStorableInfoCacheFactory.class.desiredAssertionStatus();
    }

    public StorableModule_ProvideStorableInfoCacheFactory(StorableModule storableModule, Provider<Gson> provider) {
        if ($assertionsDisabled || storableModule != null) {
            this.module = storableModule;
            if ($assertionsDisabled || provider != null) {
                this.gsonProvider = provider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static Factory<StorableInfoCache> create(StorableModule storableModule, Provider<Gson> provider) {
        return new StorableModule_ProvideStorableInfoCacheFactory(storableModule, provider);
    }

    public StorableInfoCache get() {
        return (StorableInfoCache) Preconditions.checkNotNull(this.module.provideStorableInfoCache((Gson) this.gsonProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}

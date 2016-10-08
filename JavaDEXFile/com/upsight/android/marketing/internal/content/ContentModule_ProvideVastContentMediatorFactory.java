package com.upsight.android.marketing.internal.content;

import com.upsight.android.UpsightContext;
import com.upsight.android.marketing.internal.vast.VastContentMediator;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class ContentModule_ProvideVastContentMediatorFactory implements Factory<VastContentMediator> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final ContentModule module;
    private final Provider<UpsightContext> upsightProvider;

    static {
        $assertionsDisabled = !ContentModule_ProvideVastContentMediatorFactory.class.desiredAssertionStatus();
    }

    public ContentModule_ProvideVastContentMediatorFactory(ContentModule contentModule, Provider<UpsightContext> provider) {
        if ($assertionsDisabled || contentModule != null) {
            this.module = contentModule;
            if ($assertionsDisabled || provider != null) {
                this.upsightProvider = provider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static Factory<VastContentMediator> create(ContentModule contentModule, Provider<UpsightContext> provider) {
        return new ContentModule_ProvideVastContentMediatorFactory(contentModule, provider);
    }

    public VastContentMediator get() {
        return (VastContentMediator) Preconditions.checkNotNull(this.module.provideVastContentMediator((UpsightContext) this.upsightProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}

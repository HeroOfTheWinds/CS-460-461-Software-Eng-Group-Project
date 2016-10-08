package com.upsight.android.marketing.internal.content;

import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class ContentModule_ProvideDefaultContentMediatorFactory implements Factory<DefaultContentMediator> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final ContentModule module;

    static {
        $assertionsDisabled = !ContentModule_ProvideDefaultContentMediatorFactory.class.desiredAssertionStatus();
    }

    public ContentModule_ProvideDefaultContentMediatorFactory(ContentModule contentModule) {
        if ($assertionsDisabled || contentModule != null) {
            this.module = contentModule;
            return;
        }
        throw new AssertionError();
    }

    public static Factory<DefaultContentMediator> create(ContentModule contentModule) {
        return new ContentModule_ProvideDefaultContentMediatorFactory(contentModule);
    }

    public DefaultContentMediator get() {
        return (DefaultContentMediator) Preconditions.checkNotNull(this.module.provideDefaultContentMediator(), "Cannot return null from a non-@Nullable @Provides method");
    }
}

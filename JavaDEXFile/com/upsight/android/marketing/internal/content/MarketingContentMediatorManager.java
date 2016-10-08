package com.upsight.android.marketing.internal.content;

import com.upsight.android.marketing.UpsightContentMediator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MarketingContentMediatorManager {
    private DefaultContentMediator mDefaultContentMediator;
    private Map<String, UpsightContentMediator> mMediators;

    MarketingContentMediatorManager(DefaultContentMediator defaultContentMediator) {
        this.mMediators = new HashMap();
        this.mDefaultContentMediator = defaultContentMediator;
    }

    UpsightContentMediator getContentMediator(String str) {
        return (UpsightContentMediator) this.mMediators.get(str);
    }

    Set<String> getContentProviders() {
        return new HashSet(this.mMediators.keySet());
    }

    UpsightContentMediator getDefaultContentMediator() {
        return this.mDefaultContentMediator;
    }

    public void register(UpsightContentMediator upsightContentMediator) {
        this.mMediators.put(upsightContentMediator.getContentProvider(), upsightContentMediator);
    }
}

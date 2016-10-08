package com.upsight.android.marketing.internal.billboard;

import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightMarketingExtension;
import com.upsight.android.marketing.UpsightBillboard;
import com.upsight.android.marketing.UpsightBillboard.Handler;
import com.upsight.android.marketing.UpsightBillboardManager;

public class Billboard extends UpsightBillboard {
    private UpsightBillboardManager mBillboardManager;
    protected final Handler mHandler;
    protected final String mScope;

    public Billboard(String str, Handler handler) {
        this.mScope = str;
        this.mHandler = handler;
    }

    public final void destroy() {
        UpsightBillboardManager upsightBillboardManager = this.mBillboardManager;
        if (upsightBillboardManager != null) {
            upsightBillboardManager.unregisterBillboard(this);
            this.mBillboardManager = null;
        }
    }

    Handler getHandler() {
        return this.mHandler;
    }

    String getScope() {
        return this.mScope;
    }

    public final UpsightBillboard setUp(UpsightContext upsightContext) throws IllegalStateException {
        UpsightBillboardManager api;
        UpsightMarketingExtension upsightMarketingExtension = (UpsightMarketingExtension) upsightContext.getUpsightExtension(UpsightMarketingExtension.EXTENSION_NAME);
        if (upsightMarketingExtension != null) {
            api = upsightMarketingExtension.getApi();
        } else {
            upsightContext.getLogger().m207e(Upsight.LOG_TAG, "com.upsight.extension.marketing must be registered in your Android Manifest", new Object[0]);
            api = null;
        }
        if (api != null) {
            this.mBillboardManager = api;
            if (!this.mBillboardManager.registerBillboard(this)) {
                String simpleName = UpsightBillboard.class.getSimpleName();
                throw new IllegalStateException("An active " + simpleName + " with the same scope already exists. A billboard remains active until either a content view is attached, or " + simpleName + "#destroy() is called.");
            }
        }
        return this;
    }
}

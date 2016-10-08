package com.upsight.android.unity;

import android.support.annotation.NonNull;
import android.util.Log;
import com.upsight.android.UpsightContext;
import com.upsight.android.marketing.UpsightBillboard;
import com.upsight.android.marketing.UpsightMarketingContentStore;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UpsightMarketingManager implements IUpsightExtensionManager {
    protected static final String TAG = "Upsight-UnityMarketing";
    @NonNull
    private Map<String, BillboardInfo> mBillboardMap;
    @NonNull
    private Set<String> mPreparedBillboards;
    private UpsightContext mUpsight;

    /* renamed from: com.upsight.android.unity.UpsightMarketingManager.1 */
    class C09801 implements Runnable {
        final /* synthetic */ String val$scope;

        C09801(String str) {
            this.val$scope = str;
        }

        public void run() {
            if (!UpsightMarketingManager.this.mBillboardMap.containsKey(this.val$scope) && !UpsightMarketingManager.this.mPreparedBillboards.contains(this.val$scope)) {
                Object billboardHandler = new BillboardHandler(UnityBridge.getActivity());
                UpsightMarketingManager.this.mBillboardMap.put(this.val$scope, new BillboardInfo(UpsightBillboard.create(UpsightMarketingManager.this.mUpsight, this.val$scope, billboardHandler), billboardHandler));
            }
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightMarketingManager.2 */
    class C09812 implements Runnable {
        final /* synthetic */ String val$scope;

        C09812(String str) {
            this.val$scope = str;
        }

        public void run() {
            Log.i(UpsightMarketingManager.TAG, "Destroying billboard for scope: " + this.val$scope);
            ((BillboardInfo) UpsightMarketingManager.this.mBillboardMap.remove(this.val$scope)).billboard.destroy();
            UpsightMarketingManager.this.mPreparedBillboards.remove(this.val$scope);
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightMarketingManager.3 */
    class C09823 implements Runnable {
        final /* synthetic */ String val$currentScope;

        C09823(String str) {
            this.val$currentScope = str;
        }

        public void run() {
            if (this.val$currentScope != null) {
                UpsightMarketingManager.this.mBillboardMap.remove(this.val$currentScope);
            }
            UpsightMarketingManager.this.mPreparedBillboards.addAll(UpsightMarketingManager.this.mBillboardMap.keySet());
            for (String str : UpsightMarketingManager.this.mBillboardMap.keySet()) {
                ((BillboardInfo) UpsightMarketingManager.this.mBillboardMap.get(str)).billboard.destroy();
            }
            UpsightMarketingManager.this.mBillboardMap.clear();
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightMarketingManager.4 */
    class C09834 implements Runnable {
        C09834() {
        }

        public void run() {
            for (String str : UpsightMarketingManager.this.mPreparedBillboards) {
                Object billboardHandler = new BillboardHandler(UnityBridge.getActivity());
                UpsightMarketingManager.this.mBillboardMap.put(str, new BillboardInfo(UpsightBillboard.create(UpsightMarketingManager.this.mUpsight, str, billboardHandler), billboardHandler));
            }
            UpsightMarketingManager.this.mPreparedBillboards.clear();
        }
    }

    private static class BillboardInfo {
        @NonNull
        public final UpsightBillboard billboard;
        @NonNull
        public final BillboardHandler handler;

        public BillboardInfo(@NonNull UpsightBillboard upsightBillboard, @NonNull BillboardHandler billboardHandler) {
            this.billboard = upsightBillboard;
            this.handler = billboardHandler;
        }
    }

    public UpsightMarketingManager() {
        this.mPreparedBillboards = new HashSet();
        this.mBillboardMap = new HashMap();
    }

    public void destroyBillboard(@NonNull String str) {
        if (this.mUpsight != null) {
            UnityBridge.runSafelyOnUiThread(new C09812(str));
        }
    }

    public void init(UpsightContext upsightContext) {
        this.mUpsight = upsightContext;
    }

    public boolean isContentReadyForBillboardWithScope(@NonNull String str) {
        boolean z = false;
        if (this.mUpsight != null) {
            try {
                z = UpsightMarketingContentStore.isContentReady(this.mUpsight, str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return z;
    }

    public void onApplicationPaused() {
        if (this.mUpsight != null) {
            UnityBridge.runSafelyOnUiThread(new C09823(BillboardHandler.getCurrentScope()));
        }
    }

    public void onApplicationResumed() {
        if (this.mUpsight != null) {
            UnityBridge.runSafelyOnUiThread(new C09834());
        }
    }

    public void prepareBillboard(@NonNull String str) {
        if (this.mUpsight != null) {
            UnityBridge.runSafelyOnUiThread(new C09801(str));
        }
    }
}

package com.upsight.android.unity;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.upsight.android.marketing.UpsightBillboard.AttachParameters;
import com.upsight.android.marketing.UpsightBillboardHandlers.DefaultHandler;
import com.upsight.android.marketing.UpsightPurchase;
import com.upsight.android.marketing.UpsightReward;
import java.util.List;
import org.json.JSONObject;

public class BillboardHandler extends DefaultHandler {
    protected static final String TAG = "UpsightBillboardHandler";
    @Nullable
    private static String mCurrentScope;

    public BillboardHandler(Activity activity) {
        super(activity);
    }

    @Nullable
    public static String getCurrentScope() {
        return mCurrentScope;
    }

    @Nullable
    public AttachParameters onAttach(@NonNull String str) {
        AttachParameters onAttach = super.onAttach(str);
        if (onAttach != null) {
            mCurrentScope = str;
            UnityBridge.UnitySendMessage("onBillboardAppear", str);
        }
        return onAttach;
    }

    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach");
        UnityBridge.UnitySendMessage("onBillboardDismiss", mCurrentScope);
        mCurrentScope = null;
    }

    public void onNextView() {
        super.onNextView();
        Log.i(TAG, "onNextView");
    }

    public void onPurchases(@NonNull List<UpsightPurchase> list) {
        super.onPurchases(list);
        Log.i(TAG, "onPurchases");
        for (UpsightPurchase upsightPurchase : list) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("productIdentifier", upsightPurchase.getProduct());
                jSONObject.put("quantity", upsightPurchase.getQuantity());
                jSONObject.put("billboardScope", mCurrentScope);
                UnityBridge.UnitySendMessage("billboardDidReceivePurchase", jSONObject.toString());
            } catch (Exception e) {
                Log.i(TAG, "Error creating JSON" + e.getMessage());
            }
        }
    }

    public void onRewards(@NonNull List<UpsightReward> list) {
        super.onRewards(list);
        Log.i(TAG, "onRewards");
        for (UpsightReward upsightReward : list) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("productIdentifier", upsightReward.getProduct());
                jSONObject.put("quantity", upsightReward.getQuantity());
                jSONObject.put("signatureData", upsightReward.getSignatureData());
                jSONObject.put("billboardScope", mCurrentScope);
                UnityBridge.UnitySendMessage("billboardDidReceiveReward", jSONObject.toString());
            } catch (Exception e) {
                Log.i(TAG, "Error creating JSON" + e.getMessage());
            }
        }
    }
}

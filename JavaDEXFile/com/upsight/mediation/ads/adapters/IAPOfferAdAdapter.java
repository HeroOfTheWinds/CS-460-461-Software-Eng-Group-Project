package com.upsight.mediation.ads.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.upsight.mediation.ads.model.AdapterLoadError;
import com.upsight.mediation.data.Offer;
import com.upsight.mediation.util.InAppBillingConnection;
import java.util.HashMap;

public class IAPOfferAdAdapter extends OfferAdAdapter implements LocalizedPrice {
    @Nullable
    private InAppBillingConnection mInAppBillingConnection;

    public void injectIAPBillingConnection(@NonNull InAppBillingConnection inAppBillingConnection) {
        this.mInAppBillingConnection = inAppBillingConnection;
    }

    public boolean isAdAvailable(@Nullable Offer offer, @Nullable Offer offer2) {
        return super.isAdAvailable(offer);
    }

    public void loadAd(@NonNull Activity activity, @NonNull HashMap<String, String> hashMap, @Nullable Offer offer, @Nullable Offer offer2) {
        if (this.mInAppBillingConnection == null || !this.mInAppBillingConnection.isConnected()) {
            onAdFailedToLoad(AdapterLoadError.PROVIDER_IAP_BILLING_FAILURE);
        } else if (offer == null) {
            onAdFailedToLoad(AdapterLoadError.PROVIDER_NO_FILL);
        } else {
            String localPriceForProductId = this.mInAppBillingConnection.getLocalPriceForProductId(offer.itemId);
            if (localPriceForProductId == null) {
                onAdFailedToLoad(AdapterLoadError.PROVIER_IAP_BILLING_NOT_FOUND);
                return;
            }
            this.javascriptInjection = "(function(){document.getElementById(\"bonus_price_or_quantity\").innerHTML=\"" + localPriceForProductId + "\"})()";
            super.loadAd(activity, hashMap, offer);
        }
    }
}

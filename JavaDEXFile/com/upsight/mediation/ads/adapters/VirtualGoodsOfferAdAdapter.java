package com.upsight.mediation.ads.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.upsight.mediation.data.Offer;
import java.util.HashMap;

public class VirtualGoodsOfferAdAdapter extends OfferAdAdapter {
    public boolean isAdAvailable(@Nullable Offer offer, @Nullable Offer offer2) {
        return super.isAdAvailable(offer2);
    }

    public void loadAd(@NonNull Activity activity, @NonNull HashMap<String, String> hashMap, Offer offer, Offer offer2) {
        super.loadAd(activity, hashMap, offer2);
    }
}

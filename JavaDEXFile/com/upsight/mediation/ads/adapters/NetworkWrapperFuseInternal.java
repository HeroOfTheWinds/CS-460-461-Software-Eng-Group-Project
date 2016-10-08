package com.upsight.mediation.ads.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.upsight.mediation.data.Offer;
import java.util.HashMap;

public abstract class NetworkWrapperFuseInternal extends NetworkWrapper {
    public final void onOfferAccepted(Offer offer) {
        this.listener.onOfferAccepted();
    }

    public final void onOfferDisplayed(Offer offer) {
        this.listener.onOfferDisplayed(offer);
    }

    public final void onOfferRejected(Offer offer) {
        this.listener.onOfferRejected();
    }

    protected void onOpenMRaidUrl(@NonNull String str) {
        this.listener.onOpenMRaidUrl(str);
    }

    protected final void onVastError(int i) {
        this.listener.onVastError(i);
    }

    protected final void onVastProgress(int i) {
        this.listener.onVastProgress(i);
    }

    protected final void onVastReplay() {
        this.listener.onVastReplay();
    }

    protected final void onVastSkip() {
        this.listener.onVastSkip();
    }

    public final void sendRequestToBeacon(@Nullable String str) {
        this.listener.sendRequestToBeacon(str);
    }

    public abstract boolean verifyParameters(HashMap<String, String> hashMap);
}

package com.upsight.mediation.ads.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import com.upsight.mediation.ads.model.AdapterLoadError;
import java.util.HashMap;

public class UnknownNetwork extends NetworkWrapper {
    private static final String UNKNOWN = "Unknown";

    public void displayAd() {
        onAdFailedToDisplay();
    }

    public String getName() {
        return UNKNOWN;
    }

    public void init() {
    }

    public boolean isAdAvailable() {
        return false;
    }

    public void loadAd(@NonNull Activity activity, @NonNull HashMap<String, String> hashMap) {
        onAdFailedToLoad(AdapterLoadError.PROVIDER_UNRECOGNIZED);
    }
}

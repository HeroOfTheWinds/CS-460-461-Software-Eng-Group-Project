package com.upsight.mediation.ads.adapters;

import com.upsight.mediation.log.FuseLog;
import java.util.ArrayList;

public class MRaidRegistry {
    private static final String TAG = "MRaidRegistry";
    public ArrayList<MRaidAdAdapter> registeredProviders;

    public MRaidRegistry() {
        this.registeredProviders = new ArrayList();
    }

    public MRaidAdAdapter getProvider(int i) {
        return (MRaidAdAdapter) this.registeredProviders.get(i);
    }

    public int register(MRaidAdAdapter mRaidAdAdapter) {
        if (this.registeredProviders.contains(mRaidAdAdapter)) {
            FuseLog.m240w(TAG, "Trying to register provider, already registered");
        }
        this.registeredProviders.add(mRaidAdAdapter);
        return this.registeredProviders.indexOf(mRaidAdAdapter);
    }
}

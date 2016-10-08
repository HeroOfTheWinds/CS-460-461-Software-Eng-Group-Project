package com.upsight.android.analytics.internal.dispatcher.schema;

class NetworkChangeEvent {
    public final String activeNetworkType;
    public final String networkOperatorName;

    public NetworkChangeEvent(String str, String str2) {
        this.activeNetworkType = str;
        this.networkOperatorName = str2;
    }
}

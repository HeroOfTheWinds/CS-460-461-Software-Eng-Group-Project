package com.upsight.android.internal.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public final class NetworkHelper {
    public static final String NETWORK_OPERATOR_NONE = "none";
    public static final String NETWORK_TYPE_NONE = "no_network";

    public static String getActiveNetworkType(Context context) {
        String str = NETWORK_TYPE_NONE;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    Object typeName = activeNetworkInfo.getTypeName();
                    if (!TextUtils.isEmpty(typeName)) {
                        return typeName;
                    }
                }
            }
            return str;
        } catch (SecurityException e) {
            return NETWORK_TYPE_NONE;
        }
    }

    public static String getNetworkOperatorName(Context context) {
        String str = NETWORK_OPERATOR_NONE;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager != null) {
                Object networkOperatorName = telephonyManager.getNetworkOperatorName();
                if (!TextUtils.isEmpty(networkOperatorName)) {
                    return networkOperatorName;
                }
            }
            return str;
        } catch (SecurityException e) {
            return NETWORK_OPERATOR_NONE;
        }
    }

    public static boolean isConnected(Context context) {
        try {
            boolean z;
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                    return false;
                }
                z = true;
            } else {
                z = false;
            }
            return z;
        } catch (SecurityException e) {
            return false;
        }
    }
}

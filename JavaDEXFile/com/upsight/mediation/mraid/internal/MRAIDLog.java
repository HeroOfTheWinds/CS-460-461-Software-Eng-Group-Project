package com.upsight.mediation.mraid.internal;

import com.upsight.mediation.log.FuseLog;

public class MRAIDLog {
    private static final String TAG = "MRAID";

    public static void m242d(String str) {
        FuseLog.m235d(TAG, str);
    }

    public static void m243d(String str, String str2) {
        FuseLog.m235d(TAG, "[" + str + "] " + str2);
    }

    public static void m244e(String str) {
        FuseLog.m236e(TAG, str);
    }

    public static void m245e(String str, String str2) {
        FuseLog.m236e(TAG, "[" + str + "] " + str2);
    }

    public static void m246i(String str) {
        FuseLog.m238i(TAG, str);
    }

    public static void m247i(String str, String str2) {
        FuseLog.m238i(TAG, "[" + str + "] " + str2);
    }

    public static void m248v(String str) {
        FuseLog.m239v(TAG, str);
    }

    public static void m249v(String str, String str2) {
        FuseLog.m239v(TAG, "[" + str + "] " + str2);
    }

    public static void m250w(String str) {
        FuseLog.m240w(TAG, str);
    }

    public static void m251w(String str, String str2) {
        FuseLog.m240w(TAG, "[" + str + "] " + str2);
    }
}

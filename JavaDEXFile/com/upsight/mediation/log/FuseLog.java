package com.upsight.mediation.log;

import android.os.Build.VERSION;
import android.util.Log;
import android.webkit.WebView;

public class FuseLog {
    private static boolean INTERNAL;
    public static boolean LOG;
    private static LogBuffer buffer;
    public static boolean debug;
    private static boolean testingMode;
    public static boolean veryDebug;

    static {
        buffer = new LogBuffer(100, 2000);
        debug = false;
        veryDebug = false;
        INTERNAL = false;
        testingMode = false;
        LOG = true;
    }

    public static void TOAST(String str) {
        if (!testingMode) {
        }
    }

    public static void clearBuffer() {
        if (INTERNAL) {
            buffer = new LogBuffer(200, 2000);
        }
    }

    public static void m235d(String str, String str2) {
        if (!testingMode) {
            buffer.append("d", str, str2);
            if (veryDebug) {
                Log.d(str, str2);
            }
        }
    }

    public static void disableForTests() {
        testingMode = true;
    }

    public static void m236e(String str, String str2) {
        if (!testingMode) {
            buffer.append("e", str, str2);
            if (debug) {
                Log.e(str, str2);
            }
        }
    }

    public static void m237e(String str, String str2, Throwable th) {
        if (!testingMode) {
            buffer.append("e", str, str2);
            if (debug) {
                Log.e(str, str2, th);
            }
        }
    }

    public static void enableInternalLogging() {
        buffer = new LogBuffer(200, 2000);
        debug = true;
        veryDebug = true;
        INTERNAL = true;
        if (VERSION.SDK_INT >= 19) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    public static String[] getLogHistory() {
        return buffer.getLog();
    }

    public static void m238i(String str, String str2) {
        if (!testingMode) {
            buffer.append("i", str, str2);
            if (debug) {
                Log.i(str, str2);
            }
        }
    }

    public static void internal(String str, String str2) {
        if (!testingMode) {
            buffer.append("INTERNAL", str, str2);
            if (INTERNAL) {
                Log.i(str, "INTERNAL | " + str2);
            }
        }
    }

    public static void public_e(String str, String str2) {
        if (!testingMode) {
            buffer.append("e", str, str2);
            Log.e(str, str2);
        }
    }

    public static void public_e(String str, String str2, Exception exception) {
        if (!testingMode) {
            buffer.append("e", str, str2);
            Log.e(str, str2);
        }
    }

    public static void public_w(String str, String str2) {
        if (!testingMode) {
            buffer.append("w", str, str2);
            Log.w(str, str2);
        }
    }

    public static void public_w(String str, String str2, Throwable th) {
        if (!testingMode) {
            buffer.append("w", str, str2);
            Log.w(str, str2, th);
        }
    }

    public static void m239v(String str, String str2) {
        if (!testingMode) {
            buffer.append("v", str, str2);
            if (veryDebug) {
                Log.v(str, str2);
            }
        }
    }

    public static void m240w(String str, String str2) {
        if (!testingMode) {
            buffer.append("w", str, str2);
            if (debug) {
                Log.w(str, str2);
            }
        }
    }

    public static void m241w(String str, String str2, Throwable th) {
        if (!testingMode) {
            buffer.append("w", str, str2);
            if (debug) {
                Log.w(str, str2, th);
            }
        }
    }
}

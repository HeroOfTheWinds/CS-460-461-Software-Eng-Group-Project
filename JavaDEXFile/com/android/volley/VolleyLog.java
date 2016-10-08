package com.android.volley;

import android.os.SystemClock;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VolleyLog {
    public static boolean DEBUG;
    public static String TAG;

    static class MarkerLog {
        public static final boolean ENABLED;
        private static final long MIN_DURATION_FOR_LOGGING_MS = 0;
        private boolean mFinished;
        private final List<Marker> mMarkers;

        private static class Marker {
            public final String name;
            public final long thread;
            public final long time;

            public Marker(String str, long j, long j2) {
                this.name = str;
                this.thread = j;
                this.time = j2;
            }
        }

        static {
            ENABLED = VolleyLog.DEBUG;
        }

        MarkerLog() {
            this.mMarkers = new ArrayList();
            this.mFinished = false;
        }

        private long getTotalDuration() {
            if (this.mMarkers.size() == 0) {
                return 0;
            }
            return ((Marker) this.mMarkers.get(this.mMarkers.size() - 1)).time - ((Marker) this.mMarkers.get(0)).time;
        }

        public void add(String str, long j) {
            synchronized (this) {
                if (this.mFinished) {
                    throw new IllegalStateException("Marker added to finished log");
                }
                this.mMarkers.add(new Marker(str, j, SystemClock.elapsedRealtime()));
            }
        }

        protected void finalize() throws Throwable {
            if (!this.mFinished) {
                finish("Request on the loose");
                VolleyLog.m17e("Marker log finalized without finish() - uncaught exit point for request", new Object[0]);
            }
        }

        public void finish(String str) {
            synchronized (this) {
                this.mFinished = true;
                if (getTotalDuration() > 0) {
                    long j = ((Marker) this.mMarkers.get(0)).time;
                    VolleyLog.m16d("(%-4d ms) %s", Long.valueOf(r2), str);
                    long j2 = j;
                    for (Marker marker : this.mMarkers) {
                        VolleyLog.m16d("(+%-4d) [%2d] %s", Long.valueOf(marker.time - j2), Long.valueOf(marker.thread), marker.name);
                        j2 = marker.time;
                    }
                }
            }
        }
    }

    static {
        TAG = "Volley";
        DEBUG = Log.isLoggable(TAG, 2);
    }

    private static String buildMessage(String str, Object... objArr) {
        if (objArr != null) {
            str = String.format(Locale.US, str, objArr);
        }
        StackTraceElement[] stackTrace = new Throwable().fillInStackTrace().getStackTrace();
        String str2 = "<unknown>";
        for (int i = 2; i < stackTrace.length; i++) {
            if (!stackTrace[i].getClass().equals(VolleyLog.class)) {
                str2 = stackTrace[i].getClassName();
                str2 = str2.substring(str2.lastIndexOf(46) + 1);
                str2 = String.valueOf(String.valueOf(str2.substring(str2.lastIndexOf(36) + 1)));
                String valueOf = String.valueOf(String.valueOf(stackTrace[i].getMethodName()));
                str2 = new StringBuilder((str2.length() + 1) + valueOf.length()).append(str2).append(".").append(valueOf).toString();
                break;
            }
        }
        return String.format(Locale.US, "[%d] %s: %s", new Object[]{Long.valueOf(Thread.currentThread().getId()), str2, str});
    }

    public static void m16d(String str, Object... objArr) {
        Log.d(TAG, buildMessage(str, objArr));
    }

    public static void m17e(String str, Object... objArr) {
        Log.e(TAG, buildMessage(str, objArr));
    }

    public static void m18e(Throwable th, String str, Object... objArr) {
        Log.e(TAG, buildMessage(str, objArr), th);
    }

    public static void setTag(String str) {
        m16d("Changing log tag to %s", str);
        TAG = str;
        DEBUG = Log.isLoggable(TAG, 2);
    }

    public static void m19v(String str, Object... objArr) {
        if (DEBUG) {
            Log.v(TAG, buildMessage(str, objArr));
        }
    }

    public static void wtf(String str, Object... objArr) {
        Log.wtf(TAG, buildMessage(str, objArr));
    }

    public static void wtf(Throwable th, String str, Object... objArr) {
        Log.wtf(TAG, buildMessage(str, objArr), th);
    }
}

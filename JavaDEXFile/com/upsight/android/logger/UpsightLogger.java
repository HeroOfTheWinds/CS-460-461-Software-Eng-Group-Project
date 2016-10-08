package com.upsight.android.logger;

import java.util.EnumSet;

public interface UpsightLogger {
    public static final int MAX_LENGTH = 4000;

    public enum Level {
        VERBOSE,
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    void m205d(String str, String str2, Object... objArr);

    void m206d(String str, Throwable th, String str2, Object... objArr);

    void m207e(String str, String str2, Object... objArr);

    void m208e(String str, Throwable th, String str2, Object... objArr);

    void m209i(String str, String str2, Object... objArr);

    void m210i(String str, Throwable th, String str2, Object... objArr);

    void setLogLevel(String str, EnumSet<Level> enumSet);

    void m211v(String str, String str2, Object... objArr);

    void m212v(String str, Throwable th, String str2, Object... objArr);

    void m213w(String str, String str2, Object... objArr);

    void m214w(String str, Throwable th, String str2, Object... objArr);
}

package spacemadness.com.lunarconsole.debug;

import spacemadness.com.lunarconsole.utils.StringUtils;

public class Log {
    private static final String TAG = "LunarConsole";
    private static final LogLevel logLevel;

    public enum LogLevel {
        Crit(6),
        Error(6),
        Warn(5),
        Info(4),
        Debug(3),
        None(-1);
        
        private int androidLogPriority;

        private LogLevel(int i) {
            this.androidLogPriority = i;
        }

        public int getAndroidLogPriority() {
            return this.androidLogPriority;
        }
    }

    static {
        logLevel = LogLevel.Info;
    }

    public static void m884d(String str, Object... objArr) {
        m885d(null, str, objArr);
    }

    public static void m885d(Tag tag, String str, Object... objArr) {
        log(LogLevel.Debug, tag, str, objArr);
    }

    public static void m886e(String str, Object... objArr) {
        m888e((Tag) null, str, objArr);
    }

    public static void m887e(Throwable th, String str, Object... objArr) {
        m886e(str, objArr);
        if (th != null) {
            th.printStackTrace();
        }
    }

    public static void m888e(Tag tag, String str, Object... objArr) {
        log(LogLevel.Error, tag, str, objArr);
    }

    public static void m889i(String str, Object... objArr) {
        m890i(null, str, objArr);
    }

    public static void m890i(Tag tag, String str, Object... objArr) {
        log(LogLevel.Info, tag, str, objArr);
    }

    private static void log(LogLevel logLevel, Tag tag, String str, Object... objArr) {
        if (!shouldLogLevel(logLevel) || !shouldLogTag(tag)) {
            return;
        }
        if (str != null) {
            logHelper(logLevel, str, objArr);
        } else {
            logHelper(logLevel, "null", new Object[0]);
        }
    }

    private static void logHelper(LogLevel logLevel, String str, Object... objArr) {
        android.util.Log.println(logLevel.getAndroidLogPriority(), "LunarConsole/" + Thread.currentThread().getName(), StringUtils.TryFormat(str, objArr));
    }

    private static boolean shouldLogLevel(LogLevel logLevel) {
        return logLevel.ordinal() <= logLevel.ordinal();
    }

    private static boolean shouldLogTag(Tag tag) {
        return tag == null || tag.enabled;
    }

    public static void m891w(String str, Object... objArr) {
        m892w(null, str, objArr);
    }

    public static void m892w(Tag tag, String str, Object... objArr) {
        log(LogLevel.Warn, tag, str, objArr);
    }
}

package rx.internal.util;

import java.security.AccessController;
import java.security.PrivilegedAction;

public final class PlatformDependent {
    private static final int ANDROID_API_VERSION;
    public static final int ANDROID_API_VERSION_IS_NOT_ANDROID = 0;
    private static final boolean IS_ANDROID;

    /* renamed from: rx.internal.util.PlatformDependent.1 */
    static final class C14471 implements PrivilegedAction<ClassLoader> {
        C14471() {
        }

        public ClassLoader run() {
            return ClassLoader.getSystemClassLoader();
        }
    }

    static {
        ANDROID_API_VERSION = resolveAndroidApiVersion();
        IS_ANDROID = ANDROID_API_VERSION != 0;
    }

    public static int getAndroidApiVersion() {
        return ANDROID_API_VERSION;
    }

    static ClassLoader getSystemClassLoader() {
        return System.getSecurityManager() == null ? ClassLoader.getSystemClassLoader() : (ClassLoader) AccessController.doPrivileged(new C14471());
    }

    public static boolean isAndroid() {
        return IS_ANDROID;
    }

    private static int resolveAndroidApiVersion() {
        try {
            return ((Integer) Class.forName("android.os.Build$VERSION", true, getSystemClassLoader()).getField("SDK_INT").get(null)).intValue();
        } catch (Exception e) {
            return 0;
        }
    }
}

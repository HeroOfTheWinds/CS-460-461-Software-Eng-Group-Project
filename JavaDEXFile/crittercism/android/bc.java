package crittercism.android;

import android.location.Location;

public final class bc {
    private static Location f397a;

    public static Location m473a() {
        synchronized (bc.class) {
            try {
            } finally {
                Object obj = bc.class;
            }
        }
        Location location = f397a;
        return location;
    }

    public static void m474a(Location location) {
        Class cls = bc.class;
        synchronized (cls) {
            if (location != null) {
                cls = new Location(location);
                Object obj = cls;
            }
        }
        try {
            f397a = location;
        } finally {
            Class cls2 = bc.class;
        }
    }

    public static boolean m475b() {
        boolean z;
        synchronized (bc.class) {
            try {
                z = f397a != null;
            } catch (Throwable th) {
                Class cls = bc.class;
            }
        }
        return z;
    }
}

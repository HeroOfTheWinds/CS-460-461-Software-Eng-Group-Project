package crittercism.android;

import android.util.Log;
import crittercism.android.ec.C11271;

public final class dx {
    public static C1124a f757a;
    private static ec f758b;

    /* renamed from: crittercism.android.dx.a */
    public enum C1124a {
        UNINITIALIZED,
        ON,
        OFF
    }

    static {
        f757a = C1124a.UNINITIALIZED;
    }

    public static void m773a() {
    }

    public static void m774a(ec ecVar) {
        f758b = ecVar;
    }

    public static void m775a(String str) {
        Log.i("Crittercism", str);
    }

    public static void m776a(String str, Throwable th) {
        Log.e("Crittercism", str, th);
    }

    public static void m777a(Throwable th) {
        if (!(th instanceof cp)) {
            try {
                ec ecVar = f758b;
                if (f758b != null && f757a == C1124a.ON) {
                    ecVar = f758b;
                    Runnable c11271 = new C11271(ecVar, th, Thread.currentThread().getId());
                    if (!ecVar.f781c.m733a(c11271)) {
                        ecVar.f780b.execute(c11271);
                    }
                }
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th2) {
            }
        }
    }

    public static void m778b() {
    }

    public static void m779b(String str) {
        Log.e("Crittercism", str);
    }

    public static void m780b(String str, Throwable th) {
        Log.w("Crittercism", str, th);
    }

    public static void m781c() {
    }

    public static void m782c(String str) {
        Log.w("Crittercism", str);
    }
}

package com.crittercism.app;

import android.app.AlertDialog;
import android.content.Context;
import android.location.Location;
import android.os.Build.VERSION;
import com.crittercism.integrations.PluginException;
import com.voxelbusters.nativeplugins.defines.Keys.Ui;
import crittercism.android.az;
import crittercism.android.az.C10551;
import crittercism.android.az.C10584;
import crittercism.android.az.C10617;
import crittercism.android.bc;
import crittercism.android.bg;
import crittercism.android.bn.C1078a;
import crittercism.android.cf;
import crittercism.android.cf.C1111a;
import crittercism.android.dk;
import crittercism.android.dq;
import crittercism.android.dt;
import crittercism.android.dx;
import java.lang.reflect.Array;
import java.net.URL;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.C1518R;

public class Crittercism {
    private Crittercism() {
    }

    public static void _logCrashException(String str, String str2) {
        if (str == null || str2 == null) {
            try {
                dx.m779b("Unable to handle application crash. Missing parameters");
                return;
            } catch (Throwable th) {
                dx.m777a(th);
                return;
            }
        }
        new StringBuilder("_logCrashException(msg, stack) called: ").append(str).append(" ").append(str2);
        dx.m778b();
        _logCrashException(new PluginException(str, str2));
    }

    public static void _logCrashException(String str, String str2, String str3) {
        if (str == null || str2 == null || str3 == null) {
            try {
                dx.m779b("Unable to handle application crash. Missing parameters");
                return;
            } catch (Throwable th) {
                dx.m777a(th);
                return;
            }
        }
        new StringBuilder("_logCrashException(name, msg, stack) called: ").append(str).append(" ").append(str2).append(" ").append(str3);
        dx.m778b();
        _logCrashException(new PluginException(str, str2, str3));
    }

    public static void _logCrashException(String str, String str2, String[] strArr, String[] strArr2, String[] strArr3, int[] iArr) {
        try {
            new StringBuilder("_logCrashException(String, String, String[], String[], String[], int[]) called: ").append(str).append(" ").append(str2);
            dx.m778b();
            if (str == null || str2 == null || strArr == null || strArr2 == null || strArr3 == null || iArr == null) {
                dx.m779b("Unable to handle application crash. Missing parameters");
                return;
            }
            if (m26a(strArr, strArr2, strArr3, iArr)) {
                _logCrashException(new PluginException(str, str2, strArr, strArr2, strArr3, iArr));
            } else {
                dx.m779b("Unable to handle application crash. Missing stack elements");
            }
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    @Deprecated
    public static void _logCrashException(Throwable th) {
        try {
            new StringBuilder("_logCrashException(Throwable) called with throwable: ").append(th.getMessage());
            dx.m778b();
            if (az.m400A().f361b) {
                az.m400A().m420a(th);
            } else {
                m27b("_logCrashException");
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th2) {
            dx.m777a(th2);
        }
    }

    public static void _logHandledException(String str, String str2, String str3) {
        try {
            new StringBuilder("_logHandledException(name, msg, stack) called: ").append(str).append(" ").append(str2).append(" ").append(str3);
            dx.m778b();
            if (str == null || str2 == null || str3 == null) {
                dx.m782c("Calling logHandledException with null parameter(s). Nothing will be reported to Crittercism");
            } else {
                logHandledException(new PluginException(str, str2, str3));
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public static void _logHandledException(String str, String str2, String[] strArr, String[] strArr2, String[] strArr3, int[] iArr) {
        try {
            new StringBuilder("_logHandledException(name, msg, classes, methods, files, lines) called: ").append(str);
            dx.m778b();
            if (str == null || str2 == null || strArr == null) {
                dx.m782c("Calling logHandledException with null parameter(s). Nothing will be reported to Crittercism");
            } else {
                logHandledException(new PluginException(str, str2, strArr, strArr2, strArr3, iArr));
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    private static void m25a(String str) {
        dx.m780b("Crittercism cannot be initialized", new NullPointerException(str + " was null"));
    }

    private static boolean m26a(Object... objArr) {
        boolean z = true;
        if (objArr.length <= 0 || objArr[0] == null) {
            z = false;
        } else {
            int length = Array.getLength(objArr[0]);
            int i = 1;
            while (i < objArr.length) {
                if (objArr[i] == null || Array.getLength(objArr[i]) != length) {
                    return false;
                }
                i++;
            }
        }
        return z;
    }

    private static void m27b(String str) {
        dx.m780b("Must initialize Crittercism before calling " + Crittercism.class.getName() + "." + str + "().  Request is being ignored...", new IllegalStateException());
    }

    public static void beginTransaction(String str) {
        try {
            az A = az.m400A();
            if (A.f379t) {
                dx.m782c("Transactions are not supported for services. Ignoring Crittercism.beginTransaction() call for " + str + ".");
                return;
            }
            Transaction a = Transaction.m34a(str);
            if (a instanceof bg) {
                synchronized (A.f385z) {
                    Transaction transaction = (Transaction) A.f385z.remove(str);
                    if (transaction != null) {
                        ((bg) transaction).m522h();
                    }
                    if (A.f385z.size() > 50) {
                        dx.m782c("Crittercism only supports a maximum of 50 concurrent transactions. Ignoring Crittercism.beginTransaction() call for " + str + ".");
                        return;
                    }
                    A.f385z.put(str, a);
                    a.m35a();
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public static boolean didCrashOnLastLoad() {
        boolean z = false;
        try {
            az A = az.m400A();
            if (!A.f361b) {
                m27b("didCrashOnLoad");
            } else if (!A.m404B()) {
                A.f364e.block();
                z = dq.f742a;
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
        return z;
    }

    public static void endTransaction(String str) {
        try {
            az A = az.m400A();
            if (A.f379t) {
                dx.m782c("Transactions are not supported for services. Ignoring Crittercism.endTransaction() call for " + str + ".");
                return;
            }
            Transaction transaction;
            synchronized (A.f385z) {
                transaction = (Transaction) A.f385z.remove(str);
            }
            if (transaction != null) {
                transaction.m37b();
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public static void failTransaction(String str) {
        try {
            az A = az.m400A();
            if (A.f379t) {
                dx.m782c("Transactions are not supported for services. Ignoring Crittercism.failTransaction() call for " + str + ".");
                return;
            }
            Transaction transaction;
            synchronized (A.f385z) {
                transaction = (Transaction) A.f385z.remove(str);
            }
            if (transaction != null) {
                transaction.m38c();
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public static AlertDialog generateRateMyAppAlertDialog(Context context) {
        AlertDialog alertDialog = null;
        try {
            String b;
            String c;
            az A = az.m400A();
            dt dtVar = A.f351A;
            if (A.f351A != null) {
                b = A.f351A.m760b();
                c = A.f351A.m761c();
            } else {
                b = null;
                c = null;
            }
            alertDialog = A.m408a(context, c, b);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
        return alertDialog;
    }

    public static AlertDialog generateRateMyAppAlertDialog(Context context, String str, String str2) {
        try {
            return az.m400A().m408a(context, str, str2);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
            return null;
        }
    }

    public static boolean getOptOutStatus() {
        boolean z = false;
        try {
            az A = az.m400A();
            if (A.f361b) {
                z = A.m404B();
            } else {
                m27b("getOptOutStatus");
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
        return z;
    }

    public static int getTransactionValue(String str) {
        try {
            return az.m400A().m422b(str);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
            return -1;
        }
    }

    public static void initialize(Context context, String str) {
        synchronized (Crittercism.class) {
            try {
            } finally {
                Class cls = Crittercism.class;
            }
        }
        Object crittercismConfig = new CrittercismConfig();
        initialize(context, str, crittercismConfig);
    }

    public static void initialize(Context context, String str, CrittercismConfig crittercismConfig) {
        synchronized (Crittercism.class) {
            if (str == null) {
                try {
                    m25a(String.class.getCanonicalName());
                } catch (C1078a e) {
                    throw new IllegalArgumentException("Crittercism cannot be initialized. " + e.getMessage());
                } catch (ThreadDeath e2) {
                    throw e2;
                } catch (Throwable th) {
                    Class cls = Crittercism.class;
                }
            } else if (context == null) {
                m25a(Context.class.getCanonicalName());
            } else if (crittercismConfig == null) {
                m25a(CrittercismConfig.class.getCanonicalName());
            } else if (!az.m400A().f361b) {
                long nanoTime = System.nanoTime();
                az.m400A().m411a(context, str, crittercismConfig);
                new StringBuilder("Crittercism finished initializing in ").append((System.nanoTime() - nanoTime) / 1000000).append("ms");
                dx.m778b();
            }
        }
    }

    public static void leaveBreadcrumb(String str) {
        try {
            if (!az.m400A().f361b) {
                m27b("leaveBreadcrumb");
            } else if (str == null) {
                dx.m780b("Cannot leave null breadcrumb", new NullPointerException());
            } else {
                az A = az.m400A();
                if (!A.f365f.m772b()) {
                    Runnable c10617 = new C10617(A, new cf(str, C1111a.NORMAL));
                    if (!A.f376q.m733a(c10617)) {
                        new StringBuilder("SENDING ").append(str).append(" TO EXECUTOR");
                        dx.m778b();
                        A.f378s.execute(c10617);
                    }
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public static void logHandledException(Throwable th) {
        try {
            if (!az.m400A().f361b) {
                m27b("logHandledException");
            } else if (!az.m400A().f365f.m772b()) {
                az.m400A().m425b(th);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th2) {
            dx.m777a(th2);
        }
    }

    public static void logNetworkRequest(String str, URL url, long j, long j2, long j3, int i, Exception exception) {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            if (!az.m400A().f361b) {
                m27b("logEndpoint");
            } else if (!az.m400A().f365f.m772b()) {
                az.m400A().m419a(str, url, j, j2, j3, i, exception, currentTimeMillis - j);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public static void performRateMyAppButtonAction(CritterRateMyAppButtons critterRateMyAppButtons) {
        try {
            if (az.m400A().f365f.m772b()) {
                dx.m782c("User has opted out of crittercism.  performRateMyAppButtonAction exiting.");
                return;
            }
            az A = az.m400A();
            if (VERSION.SDK_INT < 5) {
                dx.m782c("Rate my app not supported below api level 5");
                return;
            }
            String D = A.m406D();
            if (D == null) {
                dx.m779b("Cannot create proper URI to open app market.  Returning null.");
                return;
            }
            switch (C10584.f336a[critterRateMyAppButtons.ordinal()]) {
                case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    try {
                        A.m416a(D);
                    } catch (Exception e) {
                        dx.m782c("performRateMyAppButtonAction(CritterRateMyAppButtons.YES) failed.  Email support@crittercism.com.");
                        dx.m781c();
                    }
                case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
                    try {
                        A.m405C();
                    } catch (Exception e2) {
                        dx.m782c("performRateMyAppButtonAction(CritterRateMyAppButtons.NO) failed.  Email support@crittercism.com.");
                    }
                default:
            }
        } catch (ThreadDeath e3) {
            throw e3;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public static void sendAppLoadData() {
        try {
            CrittercismConfig crittercismConfig = az.m400A().f380u;
            if (crittercismConfig == null) {
                m27b("sendAppLoadData");
            } else if (!crittercismConfig.delaySendingAppLoad()) {
                dx.m775a("sendAppLoadData() will only send data to Crittercism if \"delaySendingAppLoad\" is set to true in the configuration settings you include in the init call.");
            } else if (!az.m400A().f365f.m772b()) {
                az A = az.m400A();
                if (!A.f380u.delaySendingAppLoad()) {
                    dx.m782c("CrittercismConfig instance not set to delay sending app loads.");
                } else if (!A.f379t && !A.f353C) {
                    A.f353C = true;
                    Runnable c10551 = new C10551(A);
                    if (!A.f376q.m733a(c10551)) {
                        A.f378s.execute(c10551);
                    }
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public static void setMetadata(JSONObject jSONObject) {
        try {
            if (az.m400A().f361b) {
                az.m400A().m421a(jSONObject);
            } else {
                m27b("setMetadata");
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public static void setOptOutStatus(boolean z) {
        try {
            if (az.m400A().f361b) {
                az A = az.m400A();
                Runnable dkVar = new dk(A.f362c, A, z);
                if (!A.f376q.m733a(dkVar)) {
                    A.f378s.execute(dkVar);
                    return;
                }
                return;
            }
            m27b("setOptOutStatus");
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public static void setTransactionValue(String str, int i) {
        try {
            az A = az.m400A();
            if (A.f379t) {
                dx.m782c("Transactions are not supported for services. Ignoring Crittercism.setTransactionValue() call for " + str + ".");
                return;
            }
            synchronized (A.f385z) {
                Transaction transaction = (Transaction) A.f385z.get(str);
                if (transaction != null) {
                    transaction.m36a(i);
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public static void setUsername(String str) {
        try {
            if (!az.m400A().f361b) {
                m27b("setUsername");
            } else if (str == null) {
                dx.m782c("Crittercism.setUsername() given invalid parameter: null");
            } else {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.putOpt(Ui.USER_NAME, str);
                    az.m400A().m421a(jSONObject);
                } catch (Throwable e) {
                    dx.m780b("Crittercism.setUsername()", e);
                }
            }
        } catch (ThreadDeath e2) {
            throw e2;
        } catch (Throwable e3) {
            dx.m777a(e3);
        }
    }

    public static void updateLocation(Location location) {
        if (!az.m400A().f361b) {
            m27b("updateLocation");
        } else if (location == null) {
            dx.m780b("Cannot leave null location", new NullPointerException());
        } else {
            bc.m474a(location);
        }
    }
}

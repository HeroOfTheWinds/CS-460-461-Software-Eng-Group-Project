package crittercism.android;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Provider.Service;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLContextSpi;

/* renamed from: crittercism.android.y */
public final class C1152y extends Service {
    public static final String[] f876a;
    private C1126e f877b;
    private C1116d f878c;
    private Service f879d;

    static {
        f876a = new String[]{"Default", "SSL", "TLSv1.1", "TLSv1.2", "SSLv3", "TLSv1", "TLS"};
    }

    private C1152y(Service service, C1126e c1126e, C1116d c1116d) {
        super(service.getProvider(), service.getType(), service.getAlgorithm(), service.getClassName(), null, null);
        this.f877b = c1126e;
        this.f878c = c1116d;
        this.f879d = service;
    }

    private static C1152y m871a(Service service, C1126e c1126e, C1116d c1116d) {
        C1152y c1152y = new C1152y(service, c1126e, c1116d);
        try {
            Field[] fields = Service.class.getFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                fields[i].set(c1152y, fields[i].get(service));
            }
            return c1152y;
        } catch (Exception e) {
            return null;
        }
    }

    private static Provider m872a() {
        try {
            SSLContext instance = SSLContext.getInstance("TLS");
            return instance != null ? instance.getProvider() : null;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static boolean m873a(C1126e c1126e, C1116d c1116d) {
        boolean z = false;
        if (C1153z.m878a()) {
            Provider a = C1152y.m872a();
            if (a != null) {
                for (String service : f876a) {
                    Service service2 = a.getService("SSLContext", service);
                    if (!(service2 == null || (service2 instanceof C1152y))) {
                        C1152y a2 = C1152y.m871a(service2, c1126e, c1116d);
                        if (a2 != null) {
                            z |= a2.m874b();
                        }
                    }
                }
            }
        }
        return z;
    }

    private boolean m874b() {
        Provider provider = getProvider();
        if (provider == null) {
            return false;
        }
        try {
            Method declaredMethod = Provider.class.getDeclaredMethod("putService", new Class[]{Service.class});
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(provider, new Object[]{this});
            provider.put("SSLContext.DummySSLAlgorithm", getClassName());
            provider.remove(getType() + "." + getAlgorithm());
            provider.remove("SSLContext.DummySSLAlgorithm");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public final Object newInstance(Object obj) {
        Object newInstance = super.newInstance(obj);
        try {
            if (!(newInstance instanceof SSLContextSpi)) {
                return newInstance;
            }
            C1153z a = C1153z.m875a((SSLContextSpi) newInstance, this.f877b, this.f878c);
            return a != null ? a : newInstance;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
            return newInstance;
        }
    }
}

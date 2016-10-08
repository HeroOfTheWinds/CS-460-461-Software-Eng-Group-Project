package crittercism.android;

import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContextSpi;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/* renamed from: crittercism.android.z */
public final class C1153z extends SSLContextSpi {
    private static Method[] f880a;
    private static boolean f881b;
    private SSLContextSpi f882c;
    private C1126e f883d;
    private C1116d f884e;

    static {
        f880a = new Method[7];
        f881b = false;
        try {
            f880a[0] = SSLContextSpi.class.getDeclaredMethod("engineCreateSSLEngine", new Class[0]);
            f880a[1] = SSLContextSpi.class.getDeclaredMethod("engineCreateSSLEngine", new Class[]{String.class, Integer.TYPE});
            f880a[2] = SSLContextSpi.class.getDeclaredMethod("engineGetClientSessionContext", new Class[0]);
            f880a[3] = SSLContextSpi.class.getDeclaredMethod("engineGetServerSessionContext", new Class[0]);
            f880a[4] = SSLContextSpi.class.getDeclaredMethod("engineGetServerSocketFactory", new Class[0]);
            f880a[5] = SSLContextSpi.class.getDeclaredMethod("engineGetSocketFactory", new Class[0]);
            f880a[6] = SSLContextSpi.class.getDeclaredMethod("engineInit", new Class[]{KeyManager[].class, TrustManager[].class, SecureRandom.class});
            C1134j.m821a(f880a);
            C1153z c1153z = new C1153z(new C1153z(), null, null);
            c1153z.engineCreateSSLEngine();
            c1153z.engineCreateSSLEngine(null, 0);
            c1153z.engineGetClientSessionContext();
            c1153z.engineGetServerSessionContext();
            c1153z.engineGetServerSocketFactory();
            c1153z.engineGetSocketFactory();
            c1153z.engineInit(null, null, null);
            f881b = true;
        } catch (Throwable th) {
            dx.m781c();
            f881b = false;
        }
    }

    private C1153z() {
    }

    private C1153z(SSLContextSpi sSLContextSpi, C1126e c1126e, C1116d c1116d) {
        this.f882c = sSLContextSpi;
        this.f883d = c1126e;
        this.f884e = c1116d;
    }

    public static C1153z m875a(SSLContextSpi sSLContextSpi, C1126e c1126e, C1116d c1116d) {
        return !f881b ? null : new C1153z(sSLContextSpi, c1126e, c1116d);
    }

    private Object m876a(int i, Object... objArr) {
        Throwable e;
        if (this.f882c == null) {
            return null;
        }
        try {
            return f880a[i].invoke(this.f882c, objArr);
        } catch (Throwable e2) {
            throw new ck(e2);
        } catch (Throwable e22) {
            throw new ck(e22);
        } catch (Throwable e222) {
            Throwable th = e222;
            e222 = th.getTargetException();
            if (e222 == null) {
                throw new ck(th);
            } else if (e222 instanceof Exception) {
                throw ((Exception) e222);
            } else if (e222 instanceof Error) {
                throw ((Error) e222);
            } else {
                throw new ck(th);
            }
        } catch (Throwable e2222) {
            throw new ck(e2222);
        }
    }

    private Object m877a(Object... objArr) {
        try {
            return m876a(6, objArr);
        } catch (RuntimeException e) {
            throw e;
        } catch (KeyManagementException e2) {
            throw e2;
        } catch (Throwable e3) {
            throw new ck(e3);
        }
    }

    public static boolean m878a() {
        return f881b;
    }

    private Object m879b(int i, Object... objArr) {
        try {
            return m876a(i, objArr);
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e2) {
            throw new ck(e2);
        }
    }

    protected final SSLEngine engineCreateSSLEngine() {
        return (SSLEngine) m879b(0, new Object[0]);
    }

    protected final SSLEngine engineCreateSSLEngine(String str, int i) {
        return (SSLEngine) m879b(1, str, Integer.valueOf(i));
    }

    protected final SSLSessionContext engineGetClientSessionContext() {
        return (SSLSessionContext) m879b(2, new Object[0]);
    }

    protected final SSLSessionContext engineGetServerSessionContext() {
        return (SSLSessionContext) m879b(3, new Object[0]);
    }

    protected final SSLServerSocketFactory engineGetServerSocketFactory() {
        return (SSLServerSocketFactory) m879b(4, new Object[0]);
    }

    protected final SSLSocketFactory engineGetSocketFactory() {
        SSLSocketFactory sSLSocketFactory = (SSLSocketFactory) m879b(5, new Object[0]);
        if (sSLSocketFactory == null) {
            return sSLSocketFactory;
        }
        try {
            return new ab(sSLSocketFactory, this.f883d, this.f884e);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
            return sSLSocketFactory;
        }
    }

    protected final void engineInit(KeyManager[] keyManagerArr, TrustManager[] trustManagerArr, SecureRandom secureRandom) {
        m877a(keyManagerArr, trustManagerArr, secureRandom);
    }

    public final boolean equals(Object obj) {
        SSLContextSpi sSLContextSpi = this.f882c;
        return this.f882c.equals(obj);
    }

    public final int hashCode() {
        SSLContextSpi sSLContextSpi = this.f882c;
        return this.f882c.hashCode();
    }

    public final String toString() {
        SSLContextSpi sSLContextSpi = this.f882c;
        return this.f882c.toString();
    }
}

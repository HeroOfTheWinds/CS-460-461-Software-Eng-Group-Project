package crittercism.android;

import android.os.Build.VERSION;
import crittercism.android.C1149v.C1148a;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.SocketImpl;
import java.net.SocketImplFactory;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.LinkedList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/* renamed from: crittercism.android.i */
public final class C1133i {
    public static final C1148a f808a;
    public static C1132b f809b;
    private static final List f810c;
    private ad f811d;
    private ab f812e;
    private ab f813f;
    private C1149v f814g;
    private C1126e f815h;
    private C1116d f816i;
    private C1132b f817j;
    private C1148a f818k;

    /* renamed from: crittercism.android.i.a */
    static final class C1131a implements Runnable {
        private boolean f801a;
        private boolean f802b;
        private C1133i f803c;

        public C1131a(C1133i c1133i) {
            this.f802b = false;
            this.f803c = c1133i;
            this.f801a = true;
        }

        public final boolean m807a() {
            return this.f802b;
        }

        public final void run() {
            if (this.f801a) {
                this.f802b = this.f803c.m818c();
            } else {
                this.f803c.m817b();
            }
        }
    }

    /* renamed from: crittercism.android.i.b */
    public enum C1132b {
        SOCKET_MONITOR,
        STREAM_MONITOR,
        NONE
    }

    static {
        f808a = C1148a.HTTPS_ONLY;
        f809b = C1132b.NONE;
        f810c = new LinkedList();
        try {
            if (!((URLStreamHandler) C1134j.m819a(C1134j.m820a(URL.class, URLStreamHandler.class), new URL("https://www.google.com"))).getClass().getName().contains("okhttp") || VERSION.SDK_INT < 19) {
                f809b = C1132b.STREAM_MONITOR;
            } else {
                f809b = C1132b.SOCKET_MONITOR;
            }
        } catch (Exception e) {
            f809b = C1132b.NONE;
        }
    }

    public C1133i(C1126e c1126e, C1116d c1116d) {
        this.f817j = f809b;
        this.f818k = f808a;
        this.f815h = c1126e;
        this.f816i = c1116d;
    }

    private static void m808a(String str, Throwable th) {
        synchronized (f810c) {
            f810c.add(th);
        }
        dx.m782c(str);
    }

    private static void m809a(SSLSocketFactory sSLSocketFactory) {
        C1134j.m820a(org.apache.http.conn.ssl.SSLSocketFactory.class, SSLSocketFactory.class).set(org.apache.http.conn.ssl.SSLSocketFactory.getSocketFactory(), sSLSocketFactory);
    }

    private static boolean m810a(SocketImplFactory socketImplFactory) {
        try {
            Field a = C1134j.m820a(Socket.class, SocketImplFactory.class);
            try {
                a.setAccessible(true);
                a.set(null, socketImplFactory);
                return true;
            } catch (Throwable e) {
                C1133i.m808a("Unable to install OPTIMZ for http connections", e);
                return true;
            } catch (Throwable e2) {
                C1133i.m808a("Unable to install OPTIMZ for http connections", e2);
                return false;
            } catch (Throwable e22) {
                C1133i.m808a("Unable to install OPTIMZ for http connections", e22);
                return false;
            }
        } catch (Throwable e222) {
            C1133i.m808a("Unable to install OPTIMZ for http connections", e222);
            return false;
        }
    }

    public static void m811d() {
        synchronized (f810c) {
            for (Throwable a : f810c) {
                dx.m777a(a);
            }
            f810c.clear();
        }
    }

    private boolean m812e() {
        Object c1131a = new C1131a(this);
        Thread thread = new Thread(c1131a);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
        }
        return c1131a.m807a();
    }

    private boolean m813f() {
        try {
            this.f814g = new C1149v(this.f818k, this.f815h, this.f816i);
            return this.f814g.m845b();
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static SSLSocketFactory m814g() {
        return (SSLSocketFactory) C1134j.m820a(org.apache.http.conn.ssl.SSLSocketFactory.class, SSLSocketFactory.class).get(org.apache.http.conn.ssl.SSLSocketFactory.getSocketFactory());
    }

    private boolean m815h() {
        Class cls = null;
        try {
            ad adVar;
            SocketImplFactory socketImplFactory = (SocketImplFactory) C1134j.m819a(C1134j.m820a(Socket.class, SocketImplFactory.class), null);
            if (socketImplFactory == null) {
                try {
                    SocketImpl socketImpl = (SocketImpl) C1134j.m819a(C1134j.m820a(Socket.class, SocketImpl.class), new Socket());
                    if (socketImpl == null) {
                        throw new cl("SocketImpl was null");
                    }
                    cls = socketImpl.getClass();
                } catch (Throwable e) {
                    C1133i.m808a("Unable to install OPTIMZ for http connections", e);
                    return false;
                }
            } else if (socketImplFactory instanceof ad) {
                return true;
            }
            if (socketImplFactory != null) {
                try {
                    SocketImplFactory adVar2 = new ad(socketImplFactory, this.f815h, this.f816i);
                    C1133i.m810a(adVar2);
                    adVar = adVar2;
                } catch (Throwable e2) {
                    C1133i.m808a("Unable to install OPTIMZ for http connections", e2);
                    return false;
                } catch (Throwable e22) {
                    C1133i.m808a("Unable to install OPTIMZ for http connections", e22);
                    return false;
                }
            } else if (cls != null) {
                adVar = new ad(cls, this.f815h, this.f816i);
                Socket.setSocketImplFactory(adVar);
            } else {
                C1133i.m808a("Unable to install OPTIMZ for http connections", new NullPointerException("Null SocketImpl"));
                return false;
            }
            this.f811d = adVar;
            return true;
        } catch (Throwable e222) {
            C1133i.m808a("Unable to install OPTIMZ for http connections", e222);
            return false;
        }
    }

    public final boolean m816a() {
        if (ac.m266c()) {
            try {
                ac.m268e();
                int h = m815h() | 0;
                boolean e = VERSION.SDK_INT >= 19 ? h | m812e() : h | m818c();
                boolean a = VERSION.SDK_INT >= 17 ? C1152y.m873a(this.f815h, this.f816i) | e : e;
                if (this.f817j != C1132b.SOCKET_MONITOR) {
                    return this.f817j == C1132b.STREAM_MONITOR ? m813f() | a : a;
                } else {
                    SSLSocketFactory defaultSSLSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
                    if (defaultSSLSocketFactory instanceof ab) {
                        this.f812e = (ab) defaultSSLSocketFactory;
                    } else {
                        this.f812e = new ab(defaultSSLSocketFactory, this.f815h, this.f816i);
                        HttpsURLConnection.setDefaultSSLSocketFactory(this.f812e);
                    }
                    return a | 1;
                }
            } catch (Throwable e2) {
                dx.m776a(e2.toString(), e2);
                return false;
            }
        }
        C1133i.m808a("Unable to install OPTMZ", ac.m267d());
        return false;
    }

    public final void m817b() {
        try {
            SSLSocketFactory g = C1133i.m814g();
            if (g instanceof ab) {
                C1133i.m809a(((ab) g).m261a());
            }
            this.f813f = null;
        } catch (Throwable e) {
            C1133i.m808a("Unable to install OPTIMZ for SSL HttpClient connections", e);
        } catch (Throwable e2) {
            C1133i.m808a("Unable to install OPTIMZ for SSL HttpClient connections", e2);
        } catch (Throwable e22) {
            C1133i.m808a("Unable to install OPTIMZ for SSL HttpClient connections", e22);
        }
    }

    public final boolean m818c() {
        try {
            SSLSocketFactory g = C1133i.m814g();
            if (g == null) {
                C1133i.m808a("Unable to install OPTIMZ for SSL HttpClient connections", new NullPointerException("Delegate factory was null"));
                return false;
            } else if (g instanceof ab) {
                return false;
            } else {
                SSLSocketFactory abVar = new ab(g, this.f815h, this.f816i);
                try {
                    C1133i.m809a(abVar);
                    this.f813f = abVar;
                    return true;
                } catch (Throwable e) {
                    C1133i.m808a("Unable to install OPTIMZ for SSL HttpClient connections", e);
                    return false;
                } catch (Throwable e2) {
                    C1133i.m808a("Unable to install OPTIMZ for SSL HttpClient connections", e2);
                    return false;
                } catch (Throwable e22) {
                    C1133i.m808a("Unable to install OPTIMZ for SSL HttpClient connections", e22);
                    return false;
                }
            }
        } catch (Throwable e222) {
            C1133i.m808a("Unable to install OPTIMZ for SSL HttpClient connections", e222);
            return false;
        } catch (Throwable e2222) {
            C1133i.m808a("Unable to install OPTIMZ for SSL HttpClient connections", e2222);
            return false;
        } catch (Throwable e22222) {
            C1133i.m808a("Unable to install OPTIMZ for SSL HttpClient connections", e22222);
            return false;
        } catch (Throwable e222222) {
            C1133i.m808a("Unable to install OPTIMZ for SSL HttpClient connections", e222222);
            return false;
        }
    }
}

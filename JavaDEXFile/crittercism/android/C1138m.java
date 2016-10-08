package crittercism.android;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/* renamed from: crittercism.android.m */
public abstract class C1138m extends URLStreamHandler {
    public static final String[] f830a;
    public static final String[] f831b;
    C1126e f832c;
    C1116d f833d;
    boolean f834e;
    private Constructor f835f;
    private Constructor f836g;

    static {
        f830a = new String[]{"java.net.URL", "int", "java.net.Proxy"};
        f831b = new String[]{"java.net.URL", "int"};
    }

    public C1138m(C1126e c1126e, C1116d c1116d, String[] strArr) {
        this(c1126e, c1116d, strArr, f830a, f831b);
    }

    private C1138m(C1126e c1126e, C1116d c1116d, String[] strArr, String[] strArr2, String[] strArr3) {
        this.f835f = null;
        this.f836g = null;
        this.f832c = c1126e;
        this.f833d = c1116d;
        this.f834e = true;
        int i = 0;
        while (i < strArr.length) {
            try {
                this.f835f = C1137l.m824a(strArr[i], strArr3);
                this.f836g = C1137l.m824a(strArr[i], strArr2);
                this.f835f.setAccessible(true);
                this.f836g.setAccessible(true);
                break;
            } catch (ClassNotFoundException e) {
                this.f835f = null;
                this.f835f = null;
                i++;
            }
        }
        if (this.f835f == null || this.f836g == null) {
            throw new ClassNotFoundException("Couldn't find suitable connection implementations");
        } else if (!m826b()) {
            throw new ClassNotFoundException("Unable to open test connections");
        }
    }

    private URLConnection m825a(URL url, Proxy proxy) {
        IOException iOException;
        URLConnection uRLConnection = null;
        String str = "Unable to setup network statistics on a " + m827a() + " connection due to ";
        try {
            ea eaVar = ea.GENERIC_HANDLER_DO_OPEN_CONNECTION_FAULT;
            if (proxy == null) {
                iOException = null;
                uRLConnection = (URLConnection) this.f835f.newInstance(new Object[]{url, Integer.valueOf(getDefaultPort())});
            } else {
                iOException = null;
                uRLConnection = (URLConnection) this.f836g.newInstance(new Object[]{url, Integer.valueOf(getDefaultPort()), proxy});
            }
        } catch (IllegalArgumentException e) {
            new StringBuilder().append(str).append("bad arguments");
            dx.m778b();
            iOException = new IOException(e.getMessage());
        } catch (InstantiationException e2) {
            new StringBuilder().append(str).append("an instantiation problem");
            dx.m778b();
            iOException = new IOException(e2.getMessage());
        } catch (IllegalAccessException e3) {
            new StringBuilder().append(str).append("security restrictions");
            dx.m778b();
            iOException = new IOException(e3.getMessage());
        } catch (InvocationTargetException e4) {
            new StringBuilder().append(str).append("an invocation problem");
            dx.m778b();
            iOException = new IOException(e4.getMessage());
        }
        if (iOException != null) {
            if (this.f834e) {
                this.f834e = false;
                C1149v a = C1149v.m841a();
                boolean c = a != null ? a.m846c() : false;
                dx.m779b("Stopping network statistics monitoring");
                if (c) {
                    return new URL(url.toExternalForm()).openConnection();
                }
            }
            throw iOException;
        }
        return uRLConnection;
    }

    private boolean m826b() {
        this.f834e = false;
        try {
            openConnection(new URL("http://www.google.com"));
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            this.f834e = true;
        }
    }

    protected abstract String m827a();

    protected abstract int getDefaultPort();

    protected URLConnection openConnection(URL url) {
        return m825a(url, null);
    }

    protected URLConnection openConnection(URL url, Proxy proxy) {
        if (url != null && proxy != null) {
            return m825a(url, proxy);
        }
        throw new IllegalArgumentException("url == null || proxy == null");
    }
}

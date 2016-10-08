package crittercism.android;

import com.voxelbusters.nativeplugins.defines.Keys.Scheme;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/* renamed from: crittercism.android.o */
public final class C1140o extends C1138m {
    private static final String[] f838f;

    static {
        f838f = new String[]{"libcore.net.http.HttpURLConnectionImpl", "org.apache.harmony.luni.internal.net.www.protocol.http.HttpURLConnectionImpl", "org.apache.harmony.luni.internal.net.www.protocol.http.HttpURLConnection"};
    }

    public C1140o(C1126e c1126e, C1116d c1116d) {
        super(c1126e, c1116d, f838f);
    }

    protected final String m831a() {
        return Scheme.HTTP;
    }

    protected final int getDefaultPort() {
        return 80;
    }

    protected final URLConnection openConnection(URL url) {
        HttpURLConnection httpURLConnection = (HttpURLConnection) super.openConnection(url);
        try {
            return new C1144r(httpURLConnection, this.f832c, this.f833d);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
            return httpURLConnection;
        }
    }

    protected final URLConnection openConnection(URL url, Proxy proxy) {
        HttpURLConnection httpURLConnection = (HttpURLConnection) super.openConnection(url, proxy);
        try {
            return new C1144r(httpURLConnection, this.f832c, this.f833d);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
            return httpURLConnection;
        }
    }
}

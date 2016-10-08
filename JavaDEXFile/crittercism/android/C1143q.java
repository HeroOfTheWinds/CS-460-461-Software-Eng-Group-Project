package crittercism.android;

import com.voxelbusters.nativeplugins.defines.Keys.Scheme;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;

/* renamed from: crittercism.android.q */
public final class C1143q extends C1138m {
    private static final String[] f840f;

    static {
        f840f = new String[]{"libcore.net.http.HttpsURLConnectionImpl", "org.apache.harmony.luni.internal.net.www.protocol.https.HttpsURLConnectionImpl", "org.apache.harmony.luni.internal.net.www.protocol.https.HttpsURLConnection"};
    }

    public C1143q(C1126e c1126e, C1116d c1116d) {
        super(c1126e, c1116d, f840f);
    }

    protected final String m832a() {
        return Scheme.HTTPS;
    }

    protected final int getDefaultPort() {
        return 443;
    }

    protected final URLConnection openConnection(URL url) {
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.openConnection(url);
        try {
            return new C1145s(httpsURLConnection, this.f832c, this.f833d);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
            return httpsURLConnection;
        }
    }

    protected final URLConnection openConnection(URL url, Proxy proxy) {
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.openConnection(url, proxy);
        try {
            return new C1145s(httpsURLConnection, this.f832c, this.f833d);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
            return httpsURLConnection;
        }
    }
}

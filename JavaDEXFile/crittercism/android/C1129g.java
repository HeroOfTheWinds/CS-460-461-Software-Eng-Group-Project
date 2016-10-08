package crittercism.android;

import android.os.ConditionVariable;
import android.util.Log;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;

/* renamed from: crittercism.android.g */
public final class C1129g implements C1065f, Runnable {
    private List f787a;
    private URL f788b;
    private long f789c;
    private ConditionVariable f790d;
    private au f791e;
    private ConditionVariable f792f;
    private volatile boolean f793g;
    private final Object f794h;
    private int f795i;
    private volatile long f796j;

    public C1129g(au auVar, URL url) {
        this(auVar, url, (byte) 0);
    }

    private C1129g(au auVar, URL url, byte b) {
        this.f787a = new LinkedList();
        this.f788b = null;
        this.f789c = System.currentTimeMillis();
        this.f790d = new ConditionVariable(false);
        this.f792f = new ConditionVariable(false);
        this.f793g = false;
        this.f794h = new Object();
        this.f795i = 50;
        this.f796j = 10000;
        this.f791e = auVar;
        this.f788b = url;
        this.f795i = 50;
        this.f796j = 10000;
    }

    private static boolean m798a(HttpURLConnection httpURLConnection, JSONObject jSONObject) {
        try {
            httpURLConnection.getOutputStream().write(jSONObject.toString().getBytes("UTF8"));
            int responseCode = httpURLConnection.getResponseCode();
            httpURLConnection.disconnect();
            return responseCode == 202;
        } catch (IOException e) {
            new StringBuilder("Request failed for ").append(httpURLConnection.getURL().toExternalForm());
            dx.m773a();
            return false;
        } catch (Exception e2) {
            new StringBuilder("Request failed for ").append(httpURLConnection.getURL().toExternalForm());
            dx.m773a();
            return false;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private long m799b() {
        /*
        r8 = this;
        r0 = 0;
        r2 = r8.f796j;
        r4 = java.lang.System.currentTimeMillis();
        r6 = r8.f789c;
        r4 = r4 - r6;
        r6 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1));
        if (r6 <= 0) goto L_0x0017;
    L_0x000f:
        r2 = r2 - r4;
        r4 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1));
        if (r4 >= 0) goto L_0x0017;
    L_0x0014:
        r2 = r8.f796j;
        return r0;
    L_0x0017:
        r0 = r2;
        goto L_0x0014;
        */
        throw new UnsupportedOperationException("Method not decompiled: crittercism.android.g.b():long");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.net.HttpURLConnection m800c() {
        /*
        r9 = this;
        r4 = 0;
        r1 = r9.f788b;	 Catch:{ IOException -> 0x004d, GeneralSecurityException -> 0x0067 }
        r1 = r1.openConnection();	 Catch:{ IOException -> 0x004d, GeneralSecurityException -> 0x0067 }
        r1 = (java.net.HttpURLConnection) r1;	 Catch:{ IOException -> 0x004d, GeneralSecurityException -> 0x0067 }
        r2 = 2500; // 0x9c4 float:3.503E-42 double:1.235E-320;
        r1.setConnectTimeout(r2);	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        r2 = "User-Agent";
        r3 = "5.0.8";
        r1.setRequestProperty(r2, r3);	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        r2 = "Content-Type";
        r3 = "application/json";
        r1.setRequestProperty(r2, r3);	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        r2 = 1;
        r1.setDoOutput(r2);	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        r2 = "POST";
        r1.setRequestMethod(r2);	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        r2 = r1 instanceof javax.net.ssl.HttpsURLConnection;	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        if (r2 == 0) goto L_0x004c;
    L_0x0029:
        r0 = r1;
        r0 = (javax.net.ssl.HttpsURLConnection) r0;	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        r2 = r0;
        r3 = "TLS";
        r3 = javax.net.ssl.SSLContext.getInstance(r3);	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r3.init(r5, r6, r7);	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        r3 = r3.getSocketFactory();	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        if (r3 == 0) goto L_0x004c;
    L_0x003f:
        r5 = r3 instanceof crittercism.android.ab;	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        if (r5 == 0) goto L_0x0049;
    L_0x0043:
        r3 = (crittercism.android.ab) r3;	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
        r3 = r3.m261a();	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
    L_0x0049:
        r2.setSSLSocketFactory(r3);	 Catch:{ IOException -> 0x0080, GeneralSecurityException -> 0x0067 }
    L_0x004c:
        return r1;
    L_0x004d:
        r1 = move-exception;
        r2 = r4;
    L_0x004f:
        r3 = new java.lang.StringBuilder;
        r4 = "Failed to instantiate URLConnection to APM server: ";
        r3.<init>(r4);
        r1 = r1.getMessage();
        r1 = r3.append(r1);
        r1 = r1.toString();
        crittercism.android.dx.m779b(r1);
        r1 = r2;
        goto L_0x004c;
    L_0x0067:
        r1 = move-exception;
        r2 = new java.lang.StringBuilder;
        r3 = "Failed to instantiate URLConnection to APM server: ";
        r2.<init>(r3);
        r1 = r1.getMessage();
        r1 = r2.append(r1);
        r1 = r1.toString();
        crittercism.android.dx.m779b(r1);
        r1 = r4;
        goto L_0x004c;
    L_0x0080:
        r2 = move-exception;
        r8 = r2;
        r2 = r1;
        r1 = r8;
        goto L_0x004f;
        */
        throw new UnsupportedOperationException("Method not decompiled: crittercism.android.g.c():java.net.HttpURLConnection");
    }

    private boolean m801d() {
        return !this.f793g && this.f787a.size() < this.f795i;
    }

    public final void m802a() {
        this.f792f.open();
    }

    public final void m803a(int i, TimeUnit timeUnit) {
        this.f796j = timeUnit.toMillis((long) i);
    }

    public final void m804a(C1108c c1108c) {
        Object obj = null;
        if (m801d()) {
            synchronized (this.f794h) {
                if (m801d()) {
                    this.f787a.add(c1108c);
                    if (!c1108c.m656a().contains(this.f788b.getHost())) {
                        String str = c1108c.f577f;
                        if (str == null || !str.toLowerCase().equals("connect")) {
                            obj = 1;
                        }
                    }
                    if (obj != null) {
                        this.f790d.open();
                    }
                    return;
                }
            }
        }
    }

    public final void run() {
        while (!this.f793g) {
            try {
                this.f792f.block();
                this.f790d.block();
                if (!this.f793g) {
                    try {
                        if (m799b() > 0) {
                            Thread.sleep(m799b());
                        }
                    } catch (InterruptedException e) {
                    }
                    this.f789c = System.currentTimeMillis();
                    HttpURLConnection c = m800c();
                    if (c == null) {
                        this.f793g = true;
                        dx.m779b("Disabling APM due to failure instantiating connection");
                        return;
                    }
                    List list;
                    synchronized (this.f794h) {
                        list = this.f787a;
                        this.f787a = new LinkedList();
                        this.f790d.close();
                    }
                    C1052a a = C1052a.m252a(this.f791e, list);
                    if (a == null) {
                        this.f793g = true;
                        dx.m779b("Disabling APM due to failure building request");
                        return;
                    }
                    C1129g.m798a(c, a.f268a);
                } else {
                    return;
                }
            } catch (Exception e2) {
                Log.e("Crittercism", "Exited APM send task due to: \n" + e2);
                return;
            }
        }
    }
}

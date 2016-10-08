package crittercism.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.Permission;
import java.security.Principal;
import java.security.cert.Certificate;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/* renamed from: crittercism.android.s */
public final class C1145s extends HttpsURLConnection {
    private C1126e f847a;
    private HttpsURLConnection f848b;
    private C1108c f849c;
    private C1116d f850d;
    private boolean f851e;
    private boolean f852f;

    public C1145s(HttpsURLConnection httpsURLConnection, C1126e c1126e, C1116d c1116d) {
        super(httpsURLConnection.getURL());
        this.f847a = null;
        this.f848b = null;
        this.f849c = null;
        this.f850d = null;
        this.f851e = false;
        this.f852f = false;
        this.f847a = c1126e;
        this.f848b = httpsURLConnection;
        this.f850d = c1116d;
        this.f849c = new C1108c(httpsURLConnection.getURL());
        SSLSocketFactory sSLSocketFactory = this.f848b.getSSLSocketFactory();
        if (sSLSocketFactory instanceof ab) {
            this.f848b.setSSLSocketFactory(((ab) sSLSocketFactory).m261a());
        }
    }

    private void m836a() {
        try {
            if (!this.f852f) {
                this.f852f = true;
                this.f849c.f577f = this.f848b.getRequestMethod();
                this.f849c.m665b();
                this.f849c.f581j = this.f850d.m717a();
                if (bc.m475b()) {
                    this.f849c.m659a(bc.m473a());
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    private void m837a(Throwable th) {
        try {
            if (!this.f851e) {
                this.f851e = true;
                this.f849c.m668c();
                this.f849c.m663a(th);
                this.f847a.m785a(this.f849c);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th2) {
            dx.m777a(th2);
        }
    }

    private void m838b() {
        Object obj = null;
        try {
            if (!this.f851e) {
                this.f851e = true;
                this.f849c.m668c();
                if (this.f848b.getHeaderFields() != null) {
                    C1142p c1142p = new C1142p(this.f848b.getHeaderFields());
                    int b = c1142p.m830b("Content-Length");
                    if (b != -1) {
                        this.f849c.m666b((long) b);
                        obj = 1;
                    }
                    long a = c1142p.m829a("X-Android-Sent-Millis");
                    long a2 = c1142p.m829a("X-Android-Received-Millis");
                    if (!(a == Long.MAX_VALUE || a2 == Long.MAX_VALUE)) {
                        this.f849c.m673e(a);
                        this.f849c.m675f(a2);
                    }
                }
                try {
                    this.f849c.f576e = this.f848b.getResponseCode();
                } catch (IOException e) {
                }
                if (obj != null) {
                    this.f847a.m785a(this.f849c);
                }
            }
        } catch (ThreadDeath e2) {
            throw e2;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public final void addRequestProperty(String str, String str2) {
        this.f848b.addRequestProperty(str, str2);
    }

    public final void connect() {
        this.f848b.connect();
    }

    public final void disconnect() {
        this.f848b.disconnect();
        try {
            if (this.f851e && !this.f849c.f573b) {
                this.f847a.m785a(this.f849c);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public final boolean equals(Object obj) {
        return this.f848b.equals(obj);
    }

    public final boolean getAllowUserInteraction() {
        return this.f848b.getAllowUserInteraction();
    }

    public final String getCipherSuite() {
        return this.f848b.getCipherSuite();
    }

    public final int getConnectTimeout() {
        return this.f848b.getConnectTimeout();
    }

    public final Object getContent() {
        m836a();
        try {
            Object content = this.f848b.getContent();
            m838b();
            return content;
        } catch (Throwable e) {
            m837a(e);
            throw e;
        }
    }

    public final Object getContent(Class[] clsArr) {
        m836a();
        try {
            Object content = this.f848b.getContent(clsArr);
            m838b();
            return content;
        } catch (Throwable e) {
            m837a(e);
            throw e;
        }
    }

    public final String getContentEncoding() {
        m836a();
        String contentEncoding = this.f848b.getContentEncoding();
        m838b();
        return contentEncoding;
    }

    public final int getContentLength() {
        return this.f848b.getContentLength();
    }

    public final String getContentType() {
        m836a();
        String contentType = this.f848b.getContentType();
        m838b();
        return contentType;
    }

    public final long getDate() {
        return this.f848b.getDate();
    }

    public final boolean getDefaultUseCaches() {
        return this.f848b.getDefaultUseCaches();
    }

    public final boolean getDoInput() {
        return this.f848b.getDoInput();
    }

    public final boolean getDoOutput() {
        return this.f848b.getDoOutput();
    }

    public final InputStream getErrorStream() {
        m836a();
        InputStream errorStream = this.f848b.getErrorStream();
        m838b();
        if (errorStream != null) {
            try {
                return new C1146t(errorStream, this.f847a, this.f849c);
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                dx.m777a(th);
            }
        }
        return errorStream;
    }

    public final long getExpiration() {
        return this.f848b.getExpiration();
    }

    public final String getHeaderField(int i) {
        m836a();
        String headerField = this.f848b.getHeaderField(i);
        m838b();
        return headerField;
    }

    public final String getHeaderField(String str) {
        m836a();
        String headerField = this.f848b.getHeaderField(str);
        m838b();
        return headerField;
    }

    public final long getHeaderFieldDate(String str, long j) {
        m836a();
        long headerFieldDate = this.f848b.getHeaderFieldDate(str, j);
        m838b();
        return headerFieldDate;
    }

    public final int getHeaderFieldInt(String str, int i) {
        m836a();
        int headerFieldInt = this.f848b.getHeaderFieldInt(str, i);
        m838b();
        return headerFieldInt;
    }

    public final String getHeaderFieldKey(int i) {
        m836a();
        String headerFieldKey = this.f848b.getHeaderFieldKey(i);
        m838b();
        return headerFieldKey;
    }

    public final Map getHeaderFields() {
        m836a();
        Map headerFields = this.f848b.getHeaderFields();
        m838b();
        return headerFields;
    }

    public final HostnameVerifier getHostnameVerifier() {
        return this.f848b.getHostnameVerifier();
    }

    public final long getIfModifiedSince() {
        return this.f848b.getIfModifiedSince();
    }

    public final InputStream getInputStream() {
        m836a();
        try {
            InputStream inputStream = this.f848b.getInputStream();
            m838b();
            if (inputStream != null) {
                try {
                    return new C1146t(inputStream, this.f847a, this.f849c);
                } catch (ThreadDeath e) {
                    throw e;
                } catch (Throwable th) {
                    dx.m777a(th);
                }
            }
            return inputStream;
        } catch (Throwable th2) {
            m837a(th2);
            throw th2;
        }
    }

    public final boolean getInstanceFollowRedirects() {
        return this.f848b.getInstanceFollowRedirects();
    }

    public final long getLastModified() {
        return this.f848b.getLastModified();
    }

    public final Certificate[] getLocalCertificates() {
        return this.f848b.getLocalCertificates();
    }

    public final Principal getLocalPrincipal() {
        return this.f848b.getLocalPrincipal();
    }

    public final OutputStream getOutputStream() {
        OutputStream outputStream = this.f848b.getOutputStream();
        if (outputStream != null) {
            try {
                return new C1147u(outputStream, this.f849c);
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                dx.m777a(th);
            }
        }
        return outputStream;
    }

    public final Principal getPeerPrincipal() {
        return this.f848b.getPeerPrincipal();
    }

    public final Permission getPermission() {
        return this.f848b.getPermission();
    }

    public final int getReadTimeout() {
        return this.f848b.getReadTimeout();
    }

    public final String getRequestMethod() {
        return this.f848b.getRequestMethod();
    }

    public final Map getRequestProperties() {
        return this.f848b.getRequestProperties();
    }

    public final String getRequestProperty(String str) {
        return this.f848b.getRequestProperty(str);
    }

    public final int getResponseCode() {
        m836a();
        try {
            int responseCode = this.f848b.getResponseCode();
            m838b();
            return responseCode;
        } catch (Throwable e) {
            m837a(e);
            throw e;
        }
    }

    public final String getResponseMessage() {
        m836a();
        try {
            String responseMessage = this.f848b.getResponseMessage();
            m838b();
            return responseMessage;
        } catch (Throwable e) {
            m837a(e);
            throw e;
        }
    }

    public final SSLSocketFactory getSSLSocketFactory() {
        return this.f848b.getSSLSocketFactory();
    }

    public final Certificate[] getServerCertificates() {
        return this.f848b.getServerCertificates();
    }

    public final URL getURL() {
        return this.f848b.getURL();
    }

    public final boolean getUseCaches() {
        return this.f848b.getUseCaches();
    }

    public final int hashCode() {
        return this.f848b.hashCode();
    }

    public final void setAllowUserInteraction(boolean z) {
        this.f848b.setAllowUserInteraction(z);
    }

    public final void setChunkedStreamingMode(int i) {
        this.f848b.setChunkedStreamingMode(i);
    }

    public final void setConnectTimeout(int i) {
        this.f848b.setConnectTimeout(i);
    }

    public final void setDefaultUseCaches(boolean z) {
        this.f848b.setDefaultUseCaches(z);
    }

    public final void setDoInput(boolean z) {
        this.f848b.setDoInput(z);
    }

    public final void setDoOutput(boolean z) {
        this.f848b.setDoOutput(z);
    }

    public final void setFixedLengthStreamingMode(int i) {
        this.f848b.setFixedLengthStreamingMode(i);
    }

    public final void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.f848b.setHostnameVerifier(hostnameVerifier);
    }

    public final void setIfModifiedSince(long j) {
        this.f848b.setIfModifiedSince(j);
    }

    public final void setInstanceFollowRedirects(boolean z) {
        this.f848b.setInstanceFollowRedirects(z);
    }

    public final void setReadTimeout(int i) {
        this.f848b.setReadTimeout(i);
    }

    public final void setRequestMethod(String str) {
        this.f848b.setRequestMethod(str);
    }

    public final void setRequestProperty(String str, String str2) {
        this.f848b.setRequestProperty(str, str2);
    }

    public final void setSSLSocketFactory(SSLSocketFactory sSLSocketFactory) {
        try {
            if (sSLSocketFactory instanceof ab) {
                sSLSocketFactory = ((ab) sSLSocketFactory).m261a();
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
        this.f848b.setSSLSocketFactory(sSLSocketFactory);
    }

    public final void setUseCaches(boolean z) {
        this.f848b.setUseCaches(z);
    }

    public final String toString() {
        return this.f848b.toString();
    }

    public final boolean usingProxy() {
        return this.f848b.usingProxy();
    }
}

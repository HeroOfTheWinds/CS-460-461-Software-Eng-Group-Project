package crittercism.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Permission;
import java.util.Map;

/* renamed from: crittercism.android.r */
public final class C1144r extends HttpURLConnection {
    private C1126e f841a;
    private HttpURLConnection f842b;
    private C1108c f843c;
    private C1116d f844d;
    private boolean f845e;
    private boolean f846f;

    public C1144r(HttpURLConnection httpURLConnection, C1126e c1126e, C1116d c1116d) {
        super(httpURLConnection.getURL());
        this.f845e = false;
        this.f846f = false;
        this.f842b = httpURLConnection;
        this.f841a = c1126e;
        this.f844d = c1116d;
        this.f843c = new C1108c(httpURLConnection.getURL());
    }

    private void m833a() {
        try {
            if (!this.f846f) {
                this.f846f = true;
                this.f843c.f577f = this.f842b.getRequestMethod();
                this.f843c.m665b();
                this.f843c.f581j = this.f844d.m717a();
                if (bc.m475b()) {
                    this.f843c.m659a(bc.m473a());
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    private void m834a(Throwable th) {
        try {
            if (!this.f845e) {
                this.f845e = true;
                this.f843c.m668c();
                this.f843c.m663a(th);
                this.f841a.m785a(this.f843c);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th2) {
            dx.m777a(th);
        }
    }

    private void m835b() {
        Object obj = null;
        try {
            if (!this.f845e) {
                this.f845e = true;
                this.f843c.m668c();
                if (this.f842b.getHeaderFields() != null) {
                    C1142p c1142p = new C1142p(this.f842b.getHeaderFields());
                    int b = c1142p.m830b("Content-Length");
                    if (b != -1) {
                        this.f843c.m666b((long) b);
                        obj = 1;
                    }
                    long a = c1142p.m829a("X-Android-Sent-Millis");
                    long a2 = c1142p.m829a("X-Android-Received-Millis");
                    if (!(a == Long.MAX_VALUE || a2 == Long.MAX_VALUE)) {
                        this.f843c.m673e(a);
                        this.f843c.m675f(a2);
                    }
                }
                try {
                    this.f843c.f576e = this.f842b.getResponseCode();
                } catch (IOException e) {
                }
                if (obj != null) {
                    this.f841a.m785a(this.f843c);
                }
            }
        } catch (ThreadDeath e2) {
            throw e2;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public final void addRequestProperty(String str, String str2) {
        this.f842b.addRequestProperty(str, str2);
    }

    public final void connect() {
        this.f842b.connect();
    }

    public final void disconnect() {
        this.f842b.disconnect();
        try {
            if (this.f845e && !this.f843c.f573b) {
                this.f841a.m785a(this.f843c);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public final boolean equals(Object obj) {
        return this.f842b.equals(obj);
    }

    public final boolean getAllowUserInteraction() {
        return this.f842b.getAllowUserInteraction();
    }

    public final int getConnectTimeout() {
        return this.f842b.getConnectTimeout();
    }

    public final Object getContent() {
        m833a();
        try {
            Object content = this.f842b.getContent();
            m835b();
            return content;
        } catch (Throwable e) {
            m834a(e);
            throw e;
        }
    }

    public final Object getContent(Class[] clsArr) {
        m833a();
        try {
            Object content = this.f842b.getContent(clsArr);
            m835b();
            return content;
        } catch (Throwable e) {
            m834a(e);
            throw e;
        }
    }

    public final String getContentEncoding() {
        m833a();
        String contentEncoding = this.f842b.getContentEncoding();
        m835b();
        return contentEncoding;
    }

    public final int getContentLength() {
        return this.f842b.getContentLength();
    }

    public final String getContentType() {
        m833a();
        String contentType = this.f842b.getContentType();
        m835b();
        return contentType;
    }

    public final long getDate() {
        return this.f842b.getDate();
    }

    public final boolean getDefaultUseCaches() {
        return this.f842b.getDefaultUseCaches();
    }

    public final boolean getDoInput() {
        return this.f842b.getDoInput();
    }

    public final boolean getDoOutput() {
        return this.f842b.getDoOutput();
    }

    public final InputStream getErrorStream() {
        m833a();
        InputStream errorStream = this.f842b.getErrorStream();
        m835b();
        if (errorStream != null) {
            try {
                return new C1146t(errorStream, this.f841a, this.f843c);
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                dx.m777a(th);
            }
        }
        return errorStream;
    }

    public final long getExpiration() {
        return this.f842b.getExpiration();
    }

    public final String getHeaderField(int i) {
        m833a();
        String headerField = this.f842b.getHeaderField(i);
        m835b();
        return headerField;
    }

    public final String getHeaderField(String str) {
        m833a();
        String headerField = this.f842b.getHeaderField(str);
        m835b();
        return headerField;
    }

    public final long getHeaderFieldDate(String str, long j) {
        m833a();
        long headerFieldDate = this.f842b.getHeaderFieldDate(str, j);
        m835b();
        return headerFieldDate;
    }

    public final int getHeaderFieldInt(String str, int i) {
        m833a();
        int headerFieldInt = this.f842b.getHeaderFieldInt(str, i);
        m835b();
        return headerFieldInt;
    }

    public final String getHeaderFieldKey(int i) {
        m833a();
        String headerFieldKey = this.f842b.getHeaderFieldKey(i);
        m835b();
        return headerFieldKey;
    }

    public final Map getHeaderFields() {
        m833a();
        Map headerFields = this.f842b.getHeaderFields();
        m835b();
        return headerFields;
    }

    public final long getIfModifiedSince() {
        return this.f842b.getIfModifiedSince();
    }

    public final InputStream getInputStream() {
        m833a();
        try {
            InputStream inputStream = this.f842b.getInputStream();
            m835b();
            if (inputStream != null) {
                try {
                    return new C1146t(inputStream, this.f841a, this.f843c);
                } catch (ThreadDeath e) {
                    throw e;
                } catch (Throwable th) {
                    dx.m777a(th);
                }
            }
            return inputStream;
        } catch (Throwable th2) {
            m834a(th2);
            throw th2;
        }
    }

    public final boolean getInstanceFollowRedirects() {
        return this.f842b.getInstanceFollowRedirects();
    }

    public final long getLastModified() {
        return this.f842b.getLastModified();
    }

    public final OutputStream getOutputStream() {
        OutputStream outputStream = this.f842b.getOutputStream();
        if (outputStream != null) {
            try {
                return new C1147u(outputStream, this.f843c);
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                dx.m777a(th);
            }
        }
        return outputStream;
    }

    public final Permission getPermission() {
        return this.f842b.getPermission();
    }

    public final int getReadTimeout() {
        return this.f842b.getReadTimeout();
    }

    public final String getRequestMethod() {
        return this.f842b.getRequestMethod();
    }

    public final Map getRequestProperties() {
        return this.f842b.getRequestProperties();
    }

    public final String getRequestProperty(String str) {
        return this.f842b.getRequestProperty(str);
    }

    public final int getResponseCode() {
        m833a();
        try {
            int responseCode = this.f842b.getResponseCode();
            m835b();
            return responseCode;
        } catch (Throwable e) {
            m834a(e);
            throw e;
        }
    }

    public final String getResponseMessage() {
        m833a();
        try {
            String responseMessage = this.f842b.getResponseMessage();
            m835b();
            return responseMessage;
        } catch (Throwable e) {
            m834a(e);
            throw e;
        }
    }

    public final URL getURL() {
        return this.f842b.getURL();
    }

    public final boolean getUseCaches() {
        return this.f842b.getUseCaches();
    }

    public final int hashCode() {
        return this.f842b.hashCode();
    }

    public final void setAllowUserInteraction(boolean z) {
        this.f842b.setAllowUserInteraction(z);
    }

    public final void setChunkedStreamingMode(int i) {
        this.f842b.setChunkedStreamingMode(i);
    }

    public final void setConnectTimeout(int i) {
        this.f842b.setConnectTimeout(i);
    }

    public final void setDefaultUseCaches(boolean z) {
        this.f842b.setDefaultUseCaches(z);
    }

    public final void setDoInput(boolean z) {
        this.f842b.setDoInput(z);
    }

    public final void setDoOutput(boolean z) {
        this.f842b.setDoOutput(z);
    }

    public final void setFixedLengthStreamingMode(int i) {
        this.f842b.setFixedLengthStreamingMode(i);
    }

    public final void setIfModifiedSince(long j) {
        this.f842b.setIfModifiedSince(j);
    }

    public final void setInstanceFollowRedirects(boolean z) {
        this.f842b.setInstanceFollowRedirects(z);
    }

    public final void setReadTimeout(int i) {
        this.f842b.setReadTimeout(i);
    }

    public final void setRequestMethod(String str) {
        this.f842b.setRequestMethod(str);
    }

    public final void setRequestProperty(String str, String str2) {
        this.f842b.setRequestProperty(str, str2);
    }

    public final void setUseCaches(boolean z) {
        this.f842b.setUseCaches(z);
    }

    public final String toString() {
        return this.f842b.toString();
    }

    public final boolean usingProxy() {
        return this.f842b.usingProxy();
    }
}

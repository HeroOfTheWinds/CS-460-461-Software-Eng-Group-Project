package crittercism.android;

import android.location.Location;
import crittercism.android.C1136k.C1135a;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import spacemadness.com.lunarconsole.BuildConfig;

/* renamed from: crittercism.android.c */
public final class C1108c extends bp {
    public long f572a;
    public boolean f573b;
    C1107a f574c;
    public long f575d;
    public int f576e;
    public String f577f;
    public cn f578g;
    public C1136k f579h;
    public String f580i;
    public C1066b f581j;
    private long f582k;
    private boolean f583l;
    private boolean f584m;
    private String f585n;
    private long f586o;
    private boolean f587p;
    private boolean f588q;
    private double[] f589r;

    /* renamed from: crittercism.android.c.a */
    public enum C1107a {
        NOT_LOGGED_YET("Not logged"),
        INPUT_STREAM_READ("InputStream.read()"),
        INPUT_STREAM_CLOSE("InputStream.close()"),
        SOCKET_CLOSE("Socket.close()"),
        LEGACY_JAVANET("Legacy java.net"),
        HTTP_CONTENT_LENGTH_PARSER("parse()"),
        INPUT_STREAM_FINISHED("finishedMessage()"),
        PARSING_INPUT_STREAM_LOG_ERROR("logError()"),
        SOCKET_IMPL_CONNECT("MonitoredSocketImpl.connect()"),
        SSL_SOCKET_START_HANDSHAKE("MonitoredSSLSocket.startHandshake"),
        UNIT_TEST("Unit test"),
        LOG_ENDPOINT("logEndpoint");
        
        private String f571m;

        private C1107a(String str) {
            this.f571m = str;
        }

        public final String toString() {
            return this.f571m;
        }
    }

    public C1108c() {
        this.f572a = Long.MAX_VALUE;
        this.f582k = Long.MAX_VALUE;
        this.f583l = false;
        this.f584m = false;
        this.f573b = false;
        this.f574c = C1107a.NOT_LOGGED_YET;
        this.f586o = 0;
        this.f575d = 0;
        this.f587p = false;
        this.f588q = false;
        this.f576e = 0;
        this.f577f = BuildConfig.FLAVOR;
        this.f578g = new cn(null);
        this.f579h = new C1136k();
        this.f581j = C1066b.MOBILE;
        this.f585n = cg.f616a.m688a();
    }

    public C1108c(String str) {
        this.f572a = Long.MAX_VALUE;
        this.f582k = Long.MAX_VALUE;
        this.f583l = false;
        this.f584m = false;
        this.f573b = false;
        this.f574c = C1107a.NOT_LOGGED_YET;
        this.f586o = 0;
        this.f575d = 0;
        this.f587p = false;
        this.f588q = false;
        this.f576e = 0;
        this.f577f = BuildConfig.FLAVOR;
        this.f578g = new cn(null);
        this.f579h = new C1136k();
        this.f581j = C1066b.MOBILE;
        this.f585n = cg.f616a.m688a();
        if (str != null) {
            this.f580i = str;
        }
    }

    public C1108c(URL url) {
        this.f572a = Long.MAX_VALUE;
        this.f582k = Long.MAX_VALUE;
        this.f583l = false;
        this.f584m = false;
        this.f573b = false;
        this.f574c = C1107a.NOT_LOGGED_YET;
        this.f586o = 0;
        this.f575d = 0;
        this.f587p = false;
        this.f588q = false;
        this.f576e = 0;
        this.f577f = BuildConfig.FLAVOR;
        this.f578g = new cn(null);
        this.f579h = new C1136k();
        this.f581j = C1066b.MOBILE;
        this.f585n = cg.f616a.m688a();
        if (url != null) {
            this.f580i = url.toExternalForm();
        }
    }

    private long m655g() {
        return (this.f572a == Long.MAX_VALUE || this.f582k == Long.MAX_VALUE) ? Long.MAX_VALUE : this.f582k - this.f572a;
    }

    public final String m656a() {
        boolean z = true;
        String str = this.f580i;
        if (str == null) {
            C1136k c1136k = this.f579h;
            String hostName = c1136k.f825b != null ? c1136k.f825b : c1136k.f824a != null ? c1136k.f824a.getHostName() : "unknown-host";
            if (c1136k.f829f) {
                int i = c1136k.f828e;
                if (i > 0) {
                    str = new StringBuilder(UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR).append(i).toString();
                    if (!hostName.endsWith(str)) {
                        str = hostName + str;
                    }
                }
                str = hostName;
            } else {
                str = c1136k.f826c;
                if (str == null || !(str.regionMatches(true, 0, "http:", 0, 5) || str.regionMatches(true, 0, "https:", 0, 6))) {
                    z = false;
                }
                if (!z) {
                    String str2 = c1136k.f827d != null ? BuildConfig.FLAVOR + c1136k.f827d.f822c + UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR : BuildConfig.FLAVOR;
                    if (str.startsWith("//")) {
                        str = str2 + str;
                    } else {
                        String str3 = str2 + "//";
                        if (str.startsWith(hostName)) {
                            str = str3 + str;
                        } else {
                            str2 = BuildConfig.FLAVOR;
                            if (c1136k.f828e > 0 && (c1136k.f827d == null || c1136k.f827d.f823d != c1136k.f828e)) {
                                String stringBuilder = new StringBuilder(UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR).append(c1136k.f828e).toString();
                                if (!hostName.endsWith(stringBuilder)) {
                                    str2 = stringBuilder;
                                }
                            }
                            str = str3 + hostName + str2 + str;
                        }
                    }
                }
            }
            this.f580i = str;
        }
        return str;
    }

    public final void m657a(int i) {
        C1136k c1136k = this.f579h;
        if (i > 0) {
            c1136k.f828e = i;
        }
    }

    public final void m658a(long j) {
        if (!this.f587p) {
            this.f586o += j;
        }
    }

    public final void m659a(Location location) {
        this.f589r = new double[]{location.getLatitude(), location.getLongitude()};
    }

    public final void m660a(C1135a c1135a) {
        this.f579h.f827d = c1135a;
    }

    public final void m661a(OutputStream outputStream) {
        outputStream.write(m670d().toString().getBytes());
    }

    public final void m662a(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        this.f580i = str;
    }

    public final void m663a(Throwable th) {
        this.f578g = new cn(th);
    }

    public final void m664a(InetAddress inetAddress) {
        this.f580i = null;
        this.f579h.f824a = inetAddress;
    }

    public final void m665b() {
        if (!this.f583l && this.f572a == Long.MAX_VALUE) {
            this.f572a = System.currentTimeMillis();
        }
    }

    public final void m666b(long j) {
        this.f587p = true;
        this.f586o = j;
    }

    public final void m667b(String str) {
        this.f580i = null;
        this.f579h.f825b = str;
    }

    public final void m668c() {
        if (!this.f584m && this.f582k == Long.MAX_VALUE) {
            this.f582k = System.currentTimeMillis();
        }
    }

    public final void m669c(long j) {
        if (!this.f588q) {
            this.f575d += j;
        }
    }

    public final JSONArray m670d() {
        JSONArray jSONArray = new JSONArray();
        try {
            jSONArray.put(this.f577f);
            jSONArray.put(m656a());
            jSONArray.put(ed.f784a.m797a(new Date(this.f572a)));
            jSONArray.put(m655g());
            jSONArray.put(this.f581j.m452a());
            jSONArray.put(this.f586o);
            jSONArray.put(this.f575d);
            jSONArray.put(this.f576e);
            jSONArray.put(this.f578g.f651a);
            jSONArray.put(this.f578g.f652b);
            if (this.f589r == null) {
                return jSONArray;
            }
            JSONArray jSONArray2 = new JSONArray();
            jSONArray2.put(this.f589r[0]);
            jSONArray2.put(this.f589r[1]);
            jSONArray.put(jSONArray2);
            return jSONArray;
        } catch (Exception e) {
            System.out.println("Failed to create statsArray");
            e.printStackTrace();
            return null;
        }
    }

    public final void m671d(long j) {
        this.f588q = true;
        this.f575d = j;
    }

    public final String m672e() {
        return this.f585n;
    }

    public final void m673e(long j) {
        this.f572a = j;
        this.f583l = true;
    }

    public final void m674f() {
        this.f579h.f829f = true;
    }

    public final void m675f(long j) {
        this.f582k = j;
        this.f584m = true;
    }

    public final String toString() {
        String str = (((((((((((((((BuildConfig.FLAVOR + "URI            : " + this.f580i + IOUtils.LINE_SEPARATOR_UNIX) + "URI Builder    : " + this.f579h.toString() + IOUtils.LINE_SEPARATOR_UNIX) + IOUtils.LINE_SEPARATOR_UNIX) + "Logged by      : " + this.f574c.toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Error type:         : " + this.f578g.f651a + IOUtils.LINE_SEPARATOR_UNIX) + "Error code:         : " + this.f578g.f652b + IOUtils.LINE_SEPARATOR_UNIX) + IOUtils.LINE_SEPARATOR_UNIX) + "Response time  : " + m655g() + IOUtils.LINE_SEPARATOR_UNIX) + "Start time     : " + this.f572a + IOUtils.LINE_SEPARATOR_UNIX) + "End time       : " + this.f582k + IOUtils.LINE_SEPARATOR_UNIX) + IOUtils.LINE_SEPARATOR_UNIX) + "Bytes out    : " + this.f575d + IOUtils.LINE_SEPARATOR_UNIX) + "Bytes in     : " + this.f586o + IOUtils.LINE_SEPARATOR_UNIX) + IOUtils.LINE_SEPARATOR_UNIX) + "Response code  : " + this.f576e + IOUtils.LINE_SEPARATOR_UNIX) + "Request method : " + this.f577f + IOUtils.LINE_SEPARATOR_UNIX;
        return this.f589r != null ? str + "Location       : " + Arrays.toString(this.f589r) + IOUtils.LINE_SEPARATOR_UNIX : str;
    }
}

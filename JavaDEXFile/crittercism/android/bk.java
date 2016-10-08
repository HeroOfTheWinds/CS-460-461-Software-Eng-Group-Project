package crittercism.android;

import com.crittercism.integrations.PluginException;
import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import com.voxelbusters.nativeplugins.defines.Keys.Twitter;
import crittercism.android.bx.C1080a;
import crittercism.android.bx.C1081b;
import crittercism.android.bx.C1082c;
import crittercism.android.bx.C1083d;
import crittercism.android.bx.C1084e;
import crittercism.android.bx.C1085f;
import crittercism.android.bx.C1087h;
import crittercism.android.bx.C1088i;
import crittercism.android.bx.C1089j;
import crittercism.android.bx.C1090k;
import crittercism.android.bx.C1091l;
import crittercism.android.bx.C1092m;
import crittercism.android.bx.C1093n;
import crittercism.android.bx.C1094o;
import crittercism.android.bx.C1095p;
import crittercism.android.bx.C1096q;
import crittercism.android.bx.C1097r;
import crittercism.android.bx.C1098s;
import crittercism.android.bx.C1099t;
import crittercism.android.bx.C1100u;
import crittercism.android.bx.C1101v;
import crittercism.android.bx.C1102w;
import crittercism.android.bx.C1103x;
import crittercism.android.bx.C1104y;
import crittercism.android.bx.C1105z;
import crittercism.android.bx.aa;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.BuildConfig;

public final class bk implements ch {
    public long f469a;
    public JSONArray f470b;
    public String f471c;
    public String f472d;
    public JSONArray f473e;
    public String f474f;
    public JSONObject f475g;
    private JSONObject f476h;
    private JSONArray f477i;
    private JSONArray f478j;
    private String f479k;
    private JSONArray f480l;
    private String f481m;
    private int f482n;
    private boolean f483o;
    private String f484p;

    public bk(Throwable th, long j) {
        int i = 0;
        this.f472d = BuildConfig.FLAVOR;
        this.f482n = -1;
        this.f483o = false;
        this.f483o = th instanceof PluginException;
        this.f484p = cg.f616a.m688a();
        this.f474f = "uhe";
        bu buVar = new bu();
        buVar.m582a(new C1080a()).m582a(new C1082c()).m582a(new C1081b()).m582a(new C1083d()).m582a(new C1084e()).m582a(new C1085f()).m582a(new C1094o()).m582a(new C1095p()).m582a(new C1088i()).m582a(new C1089j()).m582a(new C1087h()).m582a(new C1105z()).m582a(new aa()).m582a(new C1090k()).m582a(new C1091l()).m582a(new C1093n()).m582a(new C1092m()).m582a(new C1096q()).m582a(new C1097r()).m582a(new C1098s()).m582a(new C1099t()).m582a(new C1100u()).m582a(new C1101v()).m582a(new C1102w()).m582a(new C1103x()).m582a(new C1104y());
        this.f475g = buVar.m583a();
        this.f476h = new JSONObject();
        this.f469a = j;
        this.f471c = m540a(th);
        if (th.getMessage() != null) {
            this.f472d = th.getMessage();
        }
        if (!this.f483o) {
            this.f482n = m542c(th);
        }
        this.f479k = "android";
        this.f481m = ed.f784a.m796a();
        this.f480l = new JSONArray();
        String[] b = m541b(th);
        int length = b.length;
        while (i < length) {
            this.f480l.put(b[i]);
            i++;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String m540a(java.lang.Throwable r3) {
        /*
        r2 = this;
        r0 = r2.f483o;
        if (r0 == 0) goto L_0x000c;
    L_0x0004:
        r3 = (com.crittercism.integrations.PluginException) r3;
        r0 = r3.getExceptionName();
    L_0x000a:
        return r0;
    L_0x000b:
        r3 = r0;
    L_0x000c:
        r0 = r3.getClass();
        r1 = r0.getName();
        r0 = r3.getCause();
        if (r0 == 0) goto L_0x001c;
    L_0x001a:
        if (r0 != r3) goto L_0x000b;
    L_0x001c:
        r0 = r1;
        goto L_0x000a;
        */
        throw new UnsupportedOperationException("Method not decompiled: crittercism.android.bk.a(java.lang.Throwable):java.lang.String");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String[] m541b(java.lang.Throwable r3) {
        /*
        r1 = new java.io.StringWriter;
        r1.<init>();
        r2 = new java.io.PrintWriter;
        r2.<init>(r1);
    L_0x000a:
        r3.printStackTrace(r2);
        r0 = r3.getCause();
        if (r0 == 0) goto L_0x0015;
    L_0x0013:
        if (r0 != r3) goto L_0x0020;
    L_0x0015:
        r0 = r1.toString();
        r1 = "\n";
        r0 = r0.split(r1);
        return r0;
    L_0x0020:
        r3 = r0;
        goto L_0x000a;
        */
        throw new UnsupportedOperationException("Method not decompiled: crittercism.android.bk.b(java.lang.Throwable):java.lang.String[]");
    }

    private static int m542c(Throwable th) {
        StackTraceElement[] stackTrace = th.getStackTrace();
        int i = 0;
        while (i < stackTrace.length) {
            try {
                Object obj;
                Class cls = Class.forName(stackTrace[i].getClassName());
                for (ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader(); systemClassLoader != null; systemClassLoader = systemClassLoader.getParent()) {
                    if (cls.getClassLoader() == systemClassLoader) {
                        obj = 1;
                        break;
                    }
                }
                obj = null;
                if (obj == null) {
                    return i + 1;
                }
                i++;
            } catch (ClassNotFoundException e) {
            }
        }
        return -1;
    }

    public final void m543a() {
        this.f473e = new JSONArray();
        for (Entry entry : Thread.getAllStackTraces().entrySet()) {
            Map hashMap = new HashMap();
            Thread thread = (Thread) entry.getKey();
            if (thread.getId() != this.f469a) {
                hashMap.put(Twitter.NAME, thread.getName());
                hashMap.put(TriggerIfContentAvailable.ID, Long.valueOf(thread.getId()));
                hashMap.put(GameServices.STATE, thread.getState().name());
                hashMap.put("stacktrace", new JSONArray(Arrays.asList((Object[]) entry.getValue())));
                this.f473e.put(new JSONObject(hashMap));
            }
        }
    }

    public final void m544a(bs bsVar) {
        this.f477i = new bo(bsVar).f499a;
    }

    public final void m545a(OutputStream outputStream) {
        outputStream.write(m548b().toString().getBytes());
    }

    public final void m546a(String str, bs bsVar) {
        try {
            this.f476h.put(str, new bo(bsVar).f499a);
        } catch (JSONException e) {
        }
    }

    public final void m547a(List list) {
        this.f478j = new JSONArray();
        for (bg j : list) {
            try {
                this.f478j.put(j.m523j());
            } catch (Throwable e) {
                dx.m777a(e);
            }
        }
    }

    public final JSONObject m548b() {
        Map hashMap = new HashMap();
        hashMap.put("app_state", this.f475g);
        hashMap.put("breadcrumbs", this.f476h);
        hashMap.put("current_thread_id", Long.valueOf(this.f469a));
        if (this.f477i != null) {
            hashMap.put("endpoints", this.f477i);
        }
        if (this.f470b != null) {
            hashMap.put("systemBreadcrumbs", this.f470b);
        }
        if (this.f478j != null && this.f478j.length() > 0) {
            hashMap.put("transactions", this.f478j);
        }
        hashMap.put("exception_name", this.f471c);
        hashMap.put("exception_reason", this.f472d);
        hashMap.put("platform", this.f479k);
        if (this.f473e != null) {
            hashMap.put("threads", this.f473e);
        }
        hashMap.put("ts", this.f481m);
        Object obj = this.f474f;
        if (this.f469a != 1) {
            obj = obj + "-bg";
        }
        hashMap.put(Keys.TYPE, obj);
        hashMap.put("unsymbolized_stacktrace", this.f480l);
        if (!this.f483o) {
            hashMap.put("suspect_line_index", Integer.valueOf(this.f482n));
        }
        return new JSONObject(hashMap);
    }

    public final String m549e() {
        return this.f484p;
    }
}

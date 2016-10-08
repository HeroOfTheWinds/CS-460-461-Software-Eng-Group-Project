package crittercism.android;

import android.content.Context;
import android.os.ConditionVariable;
import crittercism.android.bg.C1075a;
import crittercism.android.bx.C1090k;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class bi extends di implements bt {
    private long f450a;
    private volatile long f451b;
    private ConditionVariable f452c;
    private ConditionVariable f453d;
    private au f454e;
    private bs f455f;
    private bs f456g;
    private bs f457h;
    private bs f458i;
    private URL f459j;
    private Context f460k;
    private volatile boolean f461l;

    public bi(Context context, au auVar, bs bsVar, bs bsVar2, bs bsVar3, bs bsVar4, URL url) {
        this.f450a = System.currentTimeMillis();
        this.f451b = 10000;
        this.f452c = new ConditionVariable(false);
        this.f453d = new ConditionVariable(false);
        this.f461l = false;
        this.f460k = context;
        this.f455f = bsVar;
        this.f456g = bsVar2;
        this.f457h = bsVar3;
        this.f458i = bsVar4;
        this.f454e = auVar;
        this.f459j = url;
        bs bsVar5 = this.f455f;
        if (this != null) {
            synchronized (bsVar5.f522c) {
                bsVar5.f522c.add(this);
            }
        }
    }

    private JSONObject m529a(JSONArray jSONArray) {
        JSONObject jSONObject = new JSONObject();
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("appID", this.f454e.m359a());
            jSONObject2.put("deviceID", this.f454e.m361c());
            jSONObject2.put("crPlatform", "android");
            jSONObject2.put("crVersion", this.f454e.m362d());
            jSONObject2.put("deviceModel", this.f454e.m368j());
            jSONObject2.put("osName", "android");
            jSONObject2.put("osVersion", this.f454e.m369k());
            jSONObject2.put("carrier", this.f454e.m364f());
            jSONObject2.put("mobileCountryCode", this.f454e.m365g());
            jSONObject2.put("mobileNetworkCode", this.f454e.m366h());
            jSONObject2.put("appVersion", this.f454e.m360b());
            jSONObject2.put("locale", new C1090k().f540a);
            jSONObject.put("appState", jSONObject2);
            jSONObject.put("transactions", jSONArray);
            if (!m530b(jSONArray)) {
                return jSONObject;
            }
            jSONObject.put("breadcrumbs", new bo(this.f456g).f499a);
            jSONObject.put("endpoints", new bo(this.f457h).f499a);
            jSONObject.put("systemBreadcrumbs", new bo(this.f458i).f499a);
            return jSONObject;
        } catch (JSONException e) {
            return null;
        }
    }

    private static boolean m530b(JSONArray jSONArray) {
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONArray optJSONArray = jSONArray.optJSONArray(i);
            if (optJSONArray != null) {
                try {
                    C1075a k = new bg(optJSONArray).m524k();
                    if (!(k == C1075a.SUCCESS || k == C1075a.INTERRUPTED || k == C1075a.ABORTED)) {
                        return true;
                    }
                } catch (Throwable e) {
                    dx.m777a(e);
                } catch (Throwable e2) {
                    dx.m777a(e2);
                }
            }
        }
        return false;
    }

    public final void m531a() {
        while (!this.f461l) {
            this.f452c.block();
            this.f453d.block();
            if (!this.f461l) {
                long currentTimeMillis = this.f451b - (System.currentTimeMillis() - this.f450a);
                if (currentTimeMillis > 0) {
                    try {
                        Thread.sleep(currentTimeMillis);
                    } catch (InterruptedException e) {
                    }
                }
                this.f450a = System.currentTimeMillis();
                bs a = this.f455f.m573a(this.f460k);
                this.f455f.m575a(a);
                JSONArray jSONArray = new bo(a).f499a;
                eb.m790a(a.f520a);
                if (jSONArray.length() > 0 && m529a(jSONArray) != null) {
                    JSONObject a2 = m529a(jSONArray);
                    try {
                        HttpURLConnection a3 = new dc(this.f459j).m721a();
                        OutputStream outputStream = a3.getOutputStream();
                        outputStream.write(a2.toString().getBytes("UTF8"));
                        outputStream.close();
                        a3.getResponseCode();
                        a3.disconnect();
                    } catch (IOException e2) {
                        new StringBuilder("Request failed for ").append(this.f459j);
                        dx.m773a();
                    } catch (Throwable e3) {
                        new StringBuilder("Request failed for ").append(this.f459j);
                        dx.m773a();
                        dx.m777a(e3);
                    }
                }
            } else {
                return;
            }
        }
    }

    public final void m532a(int i, TimeUnit timeUnit) {
        this.f451b = timeUnit.toMillis((long) i);
    }

    public final void m533b() {
        this.f452c.open();
    }

    public final void m534c() {
        bs bsVar = this.f455f;
        this.f453d.open();
    }

    public final void m535d() {
        bs bsVar = this.f455f;
        this.f453d.close();
    }
}

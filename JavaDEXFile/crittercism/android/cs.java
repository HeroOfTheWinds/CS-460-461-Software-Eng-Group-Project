package crittercism.android;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class cs implements cw {
    private Map f679a;

    /* renamed from: crittercism.android.cs.a */
    static final class C1112a {
        boolean f677a;
        int f678b;

        public C1112a() {
            this((byte) 0);
        }

        private C1112a(byte b) {
            this.f677a = false;
            this.f678b = 0;
            this.f677a = false;
            this.f678b = 0;
        }
    }

    /* renamed from: crittercism.android.cs.b */
    public static final class C1113b implements cx {
        public final /* synthetic */ cw m699a(au auVar) {
            return new cs();
        }
    }

    public cs() {
        this.f679a = new HashMap();
    }

    private JSONArray m702a() {
        JSONArray jSONArray = new JSONArray();
        for (Entry entry : this.f679a.entrySet()) {
            JSONObject jSONObject = new JSONObject((Map) entry.getKey());
            C1112a c1112a = (C1112a) entry.getValue();
            try {
                jSONArray.put(new JSONObject().put("appLoads", jSONObject).put("count", c1112a.f678b).put("current", c1112a.f677a));
            } catch (JSONException e) {
            }
        }
        return jSONArray;
    }

    public final /* synthetic */ cw m703a(bs bsVar) {
        Object obj = null;
        for (bq bqVar : bsVar.m580c()) {
            Object obj2;
            Object obj3;
            if (bqVar instanceof ca) {
                JSONObject jSONObject = (JSONObject) bqVar.m559a();
                if (jSONObject == null) {
                    obj2 = null;
                } else {
                    Map hashMap = new HashMap(jSONObject.length());
                    Iterator keys = jSONObject.keys();
                    while (keys.hasNext()) {
                        String str = (String) keys.next();
                        hashMap.put(str, jSONObject.opt(str));
                    }
                    Map map = hashMap;
                }
            } else {
                obj2 = null;
            }
            if (obj2 != null) {
                C1112a c1112a = (C1112a) this.f679a.get(obj2);
                if (c1112a == null) {
                    c1112a = new C1112a();
                    this.f679a.put(obj2, c1112a);
                }
                r0.f678b++;
                obj3 = obj2;
            } else {
                obj3 = obj;
            }
            obj = obj3;
        }
        if (obj != null) {
            ((C1112a) this.f679a.get(obj)).f677a = true;
        }
        return this;
    }

    public final void m704a(OutputStream outputStream) {
        outputStream.write(m702a().toString().getBytes("UTF8"));
    }

    public final String toString() {
        try {
            return m702a().toString(4);
        } catch (JSONException e) {
            dx.m773a();
            return null;
        }
    }
}

package crittercism.android;

import com.crittercism.app.CrittercismConfig;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class cu implements cw {
    public Map f688a;

    /* renamed from: crittercism.android.cu.a */
    public static final class C1115a implements cx {
        public final /* synthetic */ cw m710a(au auVar) {
            return new cu(auVar);
        }
    }

    public cu(au auVar) {
        this.f688a = new HashMap();
        this.f688a.put("app_id", auVar.m359a());
        this.f688a.put("hashed_device_id", auVar.m361c());
        this.f688a.put("library_version", CrittercismConfig.API_VERSION);
    }

    public final cu m711a(String str, String str2) {
        this.f688a.put(str, str2);
        return this;
    }

    public final cu m712a(String str, JSONArray jSONArray) {
        this.f688a.put(str, jSONArray);
        return this;
    }

    public final /* synthetic */ cw m713a(bs bsVar) {
        String str = bsVar.f521b;
        this.f688a.put(bsVar.f521b, new bo(bsVar).f499a);
        return this;
    }

    public final void m714a(OutputStream outputStream) {
        dx.m778b();
        outputStream.write(new JSONObject(this.f688a).toString().getBytes("UTF8"));
    }

    public final String toString() {
        try {
            return new JSONObject(this.f688a).toString(4);
        } catch (JSONException e) {
            dx.m773a();
            return null;
        }
    }
}

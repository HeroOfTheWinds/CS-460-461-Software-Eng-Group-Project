package crittercism.android;

import com.crittercism.app.CrittercismConfig;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: crittercism.android.a */
public final class C1052a {
    JSONObject f268a;

    private C1052a(au auVar, List list) {
        this.f268a = new JSONObject();
        list.size();
        JSONArray jSONArray = new JSONArray();
        JSONArray jSONArray2 = new JSONArray();
        jSONArray2.put(auVar.m359a());
        jSONArray2.put(auVar.m360b());
        jSONArray2.put(auVar.m361c());
        jSONArray2.put(CrittercismConfig.API_VERSION);
        jSONArray2.put(auVar.m363e());
        jSONArray.put(jSONArray2);
        jSONArray2 = new JSONArray();
        jSONArray2.put(ed.f784a.m796a());
        jSONArray2.put(auVar.m364f());
        jSONArray2.put(auVar.m368j());
        jSONArray2.put(auVar.m367i());
        jSONArray2.put(auVar.m369k());
        jSONArray2.put(auVar.m365g());
        jSONArray2.put(auVar.m366h());
        jSONArray.put(jSONArray2);
        JSONArray jSONArray3 = new JSONArray();
        for (C1108c d : list) {
            jSONArray3.put(d.m670d());
        }
        jSONArray.put(jSONArray3);
        this.f268a.put("d", jSONArray);
    }

    public static C1052a m252a(au auVar, List list) {
        try {
            return new C1052a(auVar, list);
        } catch (JSONException e) {
            dx.m779b("Unable to generate APM request's JSON: " + e);
            return null;
        }
    }
}

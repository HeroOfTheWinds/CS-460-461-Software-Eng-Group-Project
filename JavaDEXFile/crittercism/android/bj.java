package crittercism.android;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public final class bj extends ci {
    private String f465a;
    private String f466b;
    private C1076a f467c;
    private String f468d;

    /* renamed from: crittercism.android.bj.a */
    public enum C1076a {
        ACTIVATED,
        DEACTIVATED
    }

    public bj(C1076a c1076a, String str) {
        this.f465a = cg.f616a.m688a();
        this.f466b = ed.f784a.m796a();
        this.f467c = c1076a;
        this.f468d = str;
    }

    public final JSONArray m538a() {
        Map hashMap = new HashMap();
        hashMap.put(SendEvent.EVENT, Integer.valueOf(this.f467c.ordinal()));
        hashMap.put("viewName", this.f468d);
        return new JSONArray().put(this.f466b).put(5).put(new JSONObject(hashMap));
    }

    public final String m539e() {
        return this.f465a;
    }
}

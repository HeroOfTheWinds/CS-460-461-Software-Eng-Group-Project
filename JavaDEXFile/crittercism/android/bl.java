package crittercism.android;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public final class bl extends ci {
    private String f489a;
    private String f490b;
    private C1077a f491c;

    /* renamed from: crittercism.android.bl.a */
    public enum C1077a {
        FOREGROUND("foregrounded"),
        BACKGROUND("backgrounded");
        
        private String f488c;

        private C1077a(String str) {
            this.f488c = str;
        }

        public final String m550a() {
            return this.f488c;
        }
    }

    public bl(C1077a c1077a) {
        this.f489a = cg.f616a.m688a();
        this.f490b = ed.f784a.m796a();
        this.f491c = c1077a;
    }

    public final JSONArray m551a() {
        Map hashMap = new HashMap();
        hashMap.put(SendEvent.EVENT, this.f491c.m550a());
        return new JSONArray().put(this.f490b).put(3).put(new JSONObject(hashMap));
    }

    public final String m552e() {
        return this.f489a;
    }
}

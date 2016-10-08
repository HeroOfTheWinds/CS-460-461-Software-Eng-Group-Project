package crittercism.android;

import org.json.JSONArray;

public final class bo {
    public JSONArray f499a;

    public bo(bs bsVar) {
        this.f499a = new JSONArray();
        for (bq a : bsVar.m580c()) {
            Object a2 = a.m559a();
            if (a2 != null) {
                this.f499a.put(a2);
            }
        }
    }
}

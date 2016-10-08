package crittercism.android;

import com.voxelbusters.nativeplugins.defines.Keys;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public final class ce extends ci {
    private String f602a;
    private String f603b;
    private C1110a f604c;
    private String f605d;
    private String f606e;
    private String f607f;

    /* renamed from: crittercism.android.ce.a */
    public enum C1110a {
        INTERNET_UP,
        INTERNET_DOWN,
        CONN_TYPE_GAINED,
        CONN_TYPE_LOST,
        CONN_TYPE_SWITCHED
    }

    public ce(C1110a c1110a) {
        if (c1110a != C1110a.INTERNET_UP) {
            C1110a c1110a2 = C1110a.INTERNET_DOWN;
        }
        this.f602a = cg.f616a.m688a();
        this.f603b = ed.f784a.m796a();
        this.f604c = c1110a;
    }

    public ce(C1110a c1110a, String str) {
        if (c1110a != C1110a.CONN_TYPE_GAINED) {
            C1110a c1110a2 = C1110a.CONN_TYPE_LOST;
        }
        this.f602a = cg.f616a.m688a();
        this.f603b = ed.f784a.m796a();
        this.f604c = c1110a;
        this.f605d = str;
    }

    public ce(C1110a c1110a, String str, String str2) {
        C1110a c1110a2 = C1110a.CONN_TYPE_SWITCHED;
        this.f602a = cg.f616a.m688a();
        this.f603b = ed.f784a.m796a();
        this.f604c = c1110a;
        this.f606e = str;
        this.f607f = str2;
    }

    public final JSONArray m683a() {
        Map hashMap = new HashMap();
        hashMap.put("change", Integer.valueOf(this.f604c.ordinal()));
        if (this.f604c == C1110a.CONN_TYPE_GAINED || this.f604c == C1110a.CONN_TYPE_LOST) {
            hashMap.put(Keys.TYPE, this.f605d);
        } else if (this.f604c == C1110a.CONN_TYPE_SWITCHED) {
            hashMap.put("oldType", this.f606e);
            hashMap.put("newType", this.f607f);
        }
        return new JSONArray().put(this.f603b).put(4).put(new JSONObject(hashMap));
    }

    public final String m684e() {
        return this.f602a;
    }
}

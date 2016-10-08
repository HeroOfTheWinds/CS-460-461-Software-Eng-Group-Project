package crittercism.android;

import com.voxelbusters.nativeplugins.defines.Keys.Twitter;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public final class by extends ci {
    private String f554a;
    private String f555b;
    private String f556c;
    private String f557d;

    public by(String str, String str2) {
        this.f554a = cg.f616a.m688a();
        this.f555b = ed.f784a.m796a();
        this.f556c = str;
        this.f557d = str2;
    }

    public final JSONArray m650a() {
        Map hashMap = new HashMap();
        hashMap.put(Twitter.NAME, this.f556c);
        hashMap.put("reason", this.f557d);
        return new JSONArray().put(this.f555b).put(6).put(new JSONObject(hashMap));
    }

    public final String m651e() {
        return this.f554a;
    }
}

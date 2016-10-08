package crittercism.android;

import java.io.OutputStream;
import org.json.JSONArray;

public final class cf extends bp {
    public static final cf f611a;
    private String f612b;
    private String f613c;
    private String f614d;
    private C1111a f615e;

    /* renamed from: crittercism.android.cf.a */
    public enum C1111a {
        NORMAL,
        URGENT
    }

    static {
        f611a = new cf("session_start", C1111a.NORMAL);
    }

    public cf(String str, C1111a c1111a) {
        this(str, ed.f784a.m796a(), c1111a);
    }

    private cf(String str, String str2, C1111a c1111a) {
        this.f614d = cg.f616a.m688a();
        if (str.length() > 140) {
            str = str.substring(0, 140);
        }
        this.f612b = str;
        this.f613c = str2;
        this.f615e = c1111a;
    }

    public final void m685a(OutputStream outputStream) {
        JSONArray jSONArray = new JSONArray();
        jSONArray.put(this.f612b);
        jSONArray.put(this.f613c);
        String jSONArray2 = jSONArray.toString();
        new StringBuilder("BREADCRUMB WRITING ").append(jSONArray2);
        dx.m778b();
        outputStream.write(jSONArray2.getBytes());
    }

    public final String m686e() {
        return this.f614d;
    }
}

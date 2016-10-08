package crittercism.android;

import android.content.Context;
import java.io.File;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: crittercism.android.h */
public final class C1130h {
    public boolean f797a;
    public boolean f798b;
    public boolean f799c;
    public int f800d;

    public C1130h(Context context) {
        this.f797a = false;
        this.f798b = false;
        this.f799c = false;
        this.f800d = 10;
        if (C1130h.m805a(context).exists()) {
            this.f799c = true;
        }
    }

    public C1130h(JSONObject jSONObject) {
        this.f797a = false;
        this.f798b = false;
        this.f799c = false;
        this.f800d = 10;
        if (jSONObject.has("net")) {
            try {
                JSONObject jSONObject2 = jSONObject.getJSONObject("net");
                this.f797a = jSONObject2.optBoolean("enabled", false);
                this.f798b = jSONObject2.optBoolean("persist", false);
                this.f799c = jSONObject2.optBoolean("kill", false);
                this.f800d = jSONObject2.optInt("interval", 10);
            } catch (JSONException e) {
            }
        }
    }

    public static File m805a(Context context) {
        return new File(context.getFilesDir().getAbsolutePath() + "/.crittercism.apm.disabled.");
    }

    public static void m806b(Context context) {
        try {
            C1130h.m805a(context).createNewFile();
        } catch (IOException e) {
            dx.m779b("Unable to kill APM: " + e.getMessage());
        }
    }

    public final boolean equals(Object obj) {
        if (this != obj) {
            if (obj == null || !(obj instanceof C1130h)) {
                return false;
            }
            C1130h c1130h = (C1130h) obj;
            if (this.f799c != c1130h.f799c || this.f797a != c1130h.f797a || this.f798b != c1130h.f798b) {
                return false;
            }
            if (this.f800d != c1130h.f800d) {
                return false;
            }
        }
        return true;
    }

    public final int hashCode() {
        int i = 1237;
        int i2 = this.f799c ? 1231 : 1237;
        int i3 = this.f797a ? 1231 : 1237;
        if (this.f798b) {
            i = 1231;
        }
        return ((((((i2 + 31) * 31) + i3) * 31) + i) * 31) + this.f800d;
    }

    public final String toString() {
        return "OptmzConfiguration [\nisSendTaskEnabled=" + this.f797a + "\n, shouldPersist=" + this.f798b + "\n, isKilled=" + this.f799c + "\n, statisticsSendInterval=" + this.f800d + "]";
    }
}

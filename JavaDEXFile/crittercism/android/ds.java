package crittercism.android;

import org.json.JSONException;
import org.json.JSONObject;

public final class ds {
    private boolean f746a;
    private boolean f747b;

    /* renamed from: crittercism.android.ds.a */
    public static final class C1123a {
        public static ds m756a(ax axVar) {
            JSONObject jSONObject = null;
            String a = axVar.m385a(cq.OPT_OUT_STATUS_SETTING.m693a(), cq.OPT_OUT_STATUS_SETTING.m694b());
            if (a != null) {
                try {
                    jSONObject = new JSONObject(a);
                } catch (JSONException e) {
                    dx.m778b();
                }
            }
            return new ds(jSONObject != null ? jSONObject.optBoolean("optOutStatusSet", false) : false ? jSONObject.optBoolean("optOutStatus", false) : axVar.m389c(cq.OLD_OPT_OUT_STATUS_SETTING.m693a(), cq.OLD_OPT_OUT_STATUS_SETTING.m694b()));
        }
    }

    public ds(boolean z) {
        this.f746a = z;
        this.f747b = true;
    }

    public final boolean m757a() {
        boolean z;
        synchronized (this) {
            z = this.f746a;
        }
        return z;
    }
}

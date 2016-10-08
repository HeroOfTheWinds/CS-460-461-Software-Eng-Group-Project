package crittercism.android;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import org.json.JSONException;
import org.json.JSONObject;

public final class dk extends di {
    private ax f729a;
    private final boolean f730b;
    private Context f731c;

    public dk(Context context, ax axVar, boolean z) {
        this.f729a = axVar;
        this.f730b = z;
        this.f731c = context;
    }

    public final void m737a() {
        new StringBuilder("Setting opt out status to ").append(this.f730b).append(".  This will take effect in the next user session.");
        dx.m778b();
        boolean z = this.f730b;
        ax axVar = this.f729a;
        String a = cq.OPT_OUT_STATUS_SETTING.m693a();
        String b = cq.OPT_OUT_STATUS_SETTING.m694b();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("optOutStatus", z).put("optOutStatusSet", true);
        } catch (JSONException e) {
        }
        axVar.m387a(a, b, jSONObject.toString());
        if (this.f730b) {
            Editor edit = this.f731c.getSharedPreferences("com.crittercism.optmz.config", 0).edit();
            edit.clear();
            edit.commit();
            C1130h.m806b(this.f731c);
        }
    }
}

package crittercism.android;

import android.content.Context;
import com.upsight.mediation.vast.VASTPlayer;
import org.json.JSONObject;

public class da implements cy {
    private bs f680a;
    private bs f681b;

    /* renamed from: crittercism.android.da.a */
    public static final class C1117a implements cz {
        public final /* synthetic */ cy m719a(bs bsVar, bs bsVar2, String str, Context context, au auVar) {
            return new da(bsVar, bsVar2);
        }
    }

    public da(bs bsVar, bs bsVar2) {
        this.f680a = bsVar;
        this.f681b = bsVar2;
    }

    public void m708a(boolean z, int i, JSONObject jSONObject) {
        Object obj = (z || (i >= 200 && i < VASTPlayer.ERROR_GENERAL_WRAPPER)) ? 1 : null;
        if (obj != null) {
            this.f680a.m574a();
        } else {
            this.f680a.m575a(this.f681b);
        }
    }
}

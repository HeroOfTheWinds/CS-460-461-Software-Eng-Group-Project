package crittercism.android;

import crittercism.android.bx.C1090k;
import java.io.OutputStream;
import org.json.JSONException;
import org.json.JSONObject;

public final class bm implements ch {
    private JSONObject f492a;
    private String f493b;

    public bm(au auVar) {
        this.f493b = cg.f616a.m688a();
        try {
            this.f492a = new JSONObject().put("appID", auVar.m359a()).put("deviceID", auVar.m361c()).put("crPlatform", "android").put("crVersion", auVar.m362d()).put("deviceModel", auVar.m368j()).put("osName", "android").put("osVersion", auVar.m369k()).put("carrier", auVar.m364f()).put("mobileCountryCode", auVar.m365g()).put("mobileNetworkCode", auVar.m366h()).put("appVersion", auVar.m360b()).put("locale", new C1090k().f540a);
        } catch (JSONException e) {
        }
    }

    public final void m553a(OutputStream outputStream) {
        outputStream.write(this.f492a.toString().getBytes());
    }

    public final String m554e() {
        return this.f493b;
    }
}

package crittercism.android;

import android.content.Context;
import android.content.SharedPreferences;
import com.upsight.mediation.ads.adapters.NetworkWrapper;
import org.json.JSONException;
import org.json.JSONObject;

public final class bh {
    public boolean f446a;
    public int f447b;
    public int f448c;
    public JSONObject f449d;

    bh() {
        this.f446a = false;
        this.f447b = 10;
        this.f448c = 3600000;
        this.f449d = new JSONObject();
    }

    public bh(JSONObject jSONObject) {
        this.f446a = false;
        this.f447b = 10;
        this.f448c = 3600000;
        this.f449d = new JSONObject();
        this.f446a = jSONObject.optBoolean("enabled", false);
        this.f447b = jSONObject.optInt("interval", 10);
        this.f448c = jSONObject.optInt("defaultTimeout", 3600000);
        this.f449d = jSONObject.optJSONObject("transactions");
        if (this.f449d == null) {
            this.f449d = new JSONObject();
        }
    }

    public static bh m525a(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.crittercism.txn.config", 0);
        bh bhVar = new bh();
        bhVar.f446a = sharedPreferences.getBoolean("enabled", false);
        bhVar.f447b = sharedPreferences.getInt("interval", 10);
        bhVar.f448c = sharedPreferences.getInt("defaultTimeout", 3600000);
        String string = sharedPreferences.getString("transactions", null);
        bhVar.f449d = new JSONObject();
        if (string != null) {
            try {
                bhVar.f449d = new JSONObject(string);
            } catch (JSONException e) {
            }
        }
        return bhVar;
    }

    public final long m526a(String str) {
        JSONObject optJSONObject = this.f449d.optJSONObject(str);
        return optJSONObject != null ? optJSONObject.optLong(NetworkWrapper.TIMEOUT, (long) this.f448c) : (long) this.f448c;
    }
}

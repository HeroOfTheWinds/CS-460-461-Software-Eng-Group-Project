package crittercism.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.voxelbusters.nativeplugins.defines.Keys.Ui;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class dv {
    private SharedPreferences f750a;

    public dv(Context context, String str) {
        this.f750a = context.getSharedPreferences("com.crittercism." + str + ".usermetadata", 0);
        if (!this.f750a.contains(ModelColumns.DATA)) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.putOpt(Ui.USER_NAME, "anonymous");
                m767a(jSONObject);
            } catch (JSONException e) {
            }
        }
    }

    private void m765b(JSONObject jSONObject) {
        Editor edit = this.f750a.edit();
        edit.putString(ModelColumns.DATA, jSONObject.toString());
        edit.commit();
    }

    public final JSONObject m766a() {
        try {
            return new JSONObject(this.f750a.getString(ModelColumns.DATA, "{}"));
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    public final void m767a(JSONObject jSONObject) {
        JSONObject a = m766a();
        if (a.length() != 0) {
            Iterator keys = jSONObject.keys();
            boolean z = false;
            while (keys.hasNext()) {
                String str = (String) keys.next();
                Object opt = jSONObject.opt(str);
                Object opt2 = a.opt(str);
                boolean z2 = opt2 == null;
                if (opt2 != null) {
                    if ((opt instanceof JSONObject) || (opt instanceof JSONArray)) {
                        z2 = !opt2.toString().equals(opt.toString());
                    } else {
                        z2 = !opt2.equals(opt);
                    }
                }
                if (z2) {
                    try {
                        a.put(str, opt);
                        z = true;
                    } catch (JSONException e) {
                    }
                }
            }
            if (z) {
                m765b(a);
                m768a(true);
            }
        } else if (jSONObject.length() > 0) {
            m765b(jSONObject);
            m768a(true);
        }
    }

    public final void m768a(boolean z) {
        this.f750a.edit().putBoolean("dirty", z).commit();
    }

    public final boolean m769b() {
        return this.f750a.getBoolean("dirty", false);
    }
}

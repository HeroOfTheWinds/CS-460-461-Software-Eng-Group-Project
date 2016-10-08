package crittercism.android;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import com.voxelbusters.nativeplugins.defines.Keys;
import crittercism.android.dx.C1124a;
import java.io.File;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

public final class ct extends da {
    private au f682a;
    private Context f683b;
    private String f684c;
    private JSONObject f685d;
    private JSONObject f686e;
    private boolean f687f;

    /* renamed from: crittercism.android.ct.a */
    public static final class C1114a implements cz {
        public final /* synthetic */ cy m706a(bs bsVar, bs bsVar2, String str, Context context, au auVar) {
            return new ct(bsVar, bsVar2, str, context, auVar);
        }
    }

    public ct(bs bsVar, bs bsVar2, String str, Context context, au auVar) {
        super(bsVar, bsVar2);
        this.f684c = str;
        this.f683b = context;
        this.f682a = auVar;
    }

    public final void m709a(boolean z, int i, JSONObject jSONObject) {
        super.m708a(z, i, jSONObject);
        if (jSONObject != null) {
            Editor edit;
            if (jSONObject.optBoolean("internalExceptionReporting", false)) {
                dx.f757a = C1124a.ON;
                C1133i.m811d();
            } else {
                dx.f757a = C1124a.OFF;
            }
            dt m = this.f682a.m371m();
            if (m != null) {
                JSONObject optJSONObject = jSONObject.optJSONObject("rateMyApp");
                if (optJSONObject == null) {
                    m.m759a(false);
                } else {
                    try {
                        int i2 = optJSONObject.getInt("rateAfterLoadNum");
                        if (i2 < 0) {
                            i2 = 0;
                        }
                        m.f748a.edit().putInt("rateAfterNumLoads", i2).commit();
                        i2 = optJSONObject.getInt("remindAfterLoadNum");
                        if (i2 <= 0) {
                            i2 = 1;
                        }
                        m.f748a.edit().putInt("remindAfterNumLoads", i2).commit();
                        m.f748a.edit().putString("rateAppMessage", optJSONObject.getString(Keys.MESSAGE)).commit();
                        m.f748a.edit().putString("rateAppTitle", optJSONObject.getString(Keys.TITLE)).commit();
                        m.m759a(true);
                    } catch (JSONException e) {
                        m.m759a(false);
                    }
                }
            }
            if (jSONObject.optInt("needPkg", 0) == 1) {
                try {
                    new dj(new cu(this.f682a).m711a("device_name", this.f682a.m367i()).m711a("pkg", this.f683b.getPackageName()), new dc(new db(this.f684c, "/android_v2/update_package_name").m720a()), null).run();
                } catch (IOException e2) {
                    new StringBuilder("IOException in handleResponse(): ").append(e2.getMessage());
                    dx.m778b();
                    dx.m781c();
                }
                this.f687f = true;
            }
            this.f685d = jSONObject.optJSONObject("apm");
            if (this.f685d != null) {
                C1130h c1130h = new C1130h(this.f685d);
                Context context = this.f683b;
                if (c1130h.f799c) {
                    C1130h.m806b(context);
                } else {
                    File a = C1130h.m805a(context);
                    if (!a.delete() && a.exists()) {
                        dx.m779b("Unable to reenable OPTMZ instrumentation");
                    }
                }
                edit = context.getSharedPreferences("com.crittercism.optmz.config", 0).edit();
                if (c1130h.f798b) {
                    edit.putBoolean("enabled", c1130h.f797a);
                    edit.putBoolean("kill", c1130h.f799c);
                    edit.putBoolean("persist", c1130h.f798b);
                    edit.putInt("interval", c1130h.f800d);
                } else {
                    edit.clear();
                }
                edit.commit();
                az.m400A().m415a(c1130h);
            }
            this.f686e = jSONObject.optJSONObject("txnConfig");
            if (this.f686e != null) {
                bh bhVar = new bh(this.f686e);
                edit = this.f683b.getSharedPreferences("com.crittercism.txn.config", 0).edit();
                edit.putBoolean("enabled", bhVar.f446a);
                edit.putInt("interval", bhVar.f447b);
                edit.putInt("defaultTimeout", bhVar.f448c);
                edit.putString("transactions", bhVar.f449d.toString());
                edit.commit();
                az.m400A().m412a(bhVar);
            }
        }
    }
}

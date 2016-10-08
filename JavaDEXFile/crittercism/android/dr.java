package crittercism.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.provider.Settings.Secure;
import java.util.UUID;

public final class dr {
    private SharedPreferences f743a;
    private SharedPreferences f744b;
    private Context f745c;

    public dr(Context context) {
        if (context == null) {
            throw new NullPointerException("context was null");
        }
        this.f745c = context;
        this.f743a = context.getSharedPreferences("com.crittercism.usersettings", 0);
        this.f744b = context.getSharedPreferences("com.crittercism.prefs", 0);
        if (this.f743a == null) {
            throw new NullPointerException("prefs were null");
        } else if (this.f744b == null) {
            throw new NullPointerException("legacy prefs were null");
        }
    }

    private boolean m753a(String str) {
        Editor edit = this.f743a.edit();
        edit.putString("hashedDeviceID", str);
        return edit.commit();
    }

    private String m754b() {
        String string;
        try {
            string = Secure.getString(this.f745c.getContentResolver(), "android_id");
            if (!(string == null || string.length() <= 0 || string.equals("9774d56d682e549c"))) {
                UUID nameUUIDFromBytes = UUID.nameUUIDFromBytes(string.getBytes("utf8"));
                if (nameUUIDFromBytes != null) {
                    string = nameUUIDFromBytes.toString();
                    if (string == null || string.length() == 0) {
                        string = UUID.randomUUID().toString();
                    }
                    return string;
                }
            }
            string = null;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
            string = null;
        }
        try {
            string = UUID.randomUUID().toString();
        } catch (ThreadDeath e2) {
            throw e2;
        } catch (Throwable th2) {
            dx.m777a(th2);
        }
        return string;
    }

    public final String m755a() {
        String string = this.f743a.getString("hashedDeviceID", null);
        if (string == null) {
            string = this.f744b.getString("com.crittercism.prefs.did", null);
            if (string != null && m753a(string)) {
                Editor edit = this.f744b.edit();
                edit.remove("com.crittercism.prefs.did");
                edit.commit();
            }
        }
        if (string != null) {
            return string;
        }
        string = m754b();
        m753a(string);
        return string;
    }
}

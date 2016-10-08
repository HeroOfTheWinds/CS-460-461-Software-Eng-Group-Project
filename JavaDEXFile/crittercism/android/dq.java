package crittercism.android;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public final class dq {
    public static boolean f742a;

    static {
        f742a = false;
    }

    public static Boolean m751a(Context context) {
        return Boolean.valueOf(context.getSharedPreferences("com.crittercism.usersettings", 0).getBoolean("crashedOnLastLoad", false));
    }

    public static void m752a(Context context, boolean z) {
        Editor edit = context.getSharedPreferences("com.crittercism.usersettings", 0).edit();
        edit.putBoolean("crashedOnLastLoad", z);
        edit.commit();
    }
}

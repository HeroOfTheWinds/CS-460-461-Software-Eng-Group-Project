package crittercism.android;

import android.content.Context;
import android.content.SharedPreferences;

public final class dt {
    public SharedPreferences f748a;

    protected dt() {
    }

    public dt(Context context) {
        this.f748a = context.getSharedPreferences("com.crittercism.ratemyapp", 0);
    }

    public final int m758a() {
        return this.f748a.getInt("numAppLoads", 0);
    }

    public final void m759a(boolean z) {
        this.f748a.edit().putBoolean("rateMyAppEnabled", z).commit();
    }

    public final String m760b() {
        return this.f748a.getString("rateAppMessage", "Would you mind taking a second to rate my app?  I would really appreciate it!");
    }

    public final String m761c() {
        return this.f748a.getString("rateAppTitle", "Rate My App");
    }

    public final void m762d() {
        this.f748a.edit().putBoolean("hasRatedApp", true).commit();
    }
}

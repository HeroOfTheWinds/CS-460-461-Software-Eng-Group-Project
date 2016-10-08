package crittercism.android;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import com.crittercism.app.CrittercismConfig;
import spacemadness.com.lunarconsole.BuildConfig;

public final class at {
    public String f315a;
    public int f316b;

    public at(Context context, CrittercismConfig crittercismConfig) {
        this.f315a = BuildConfig.VERSION_NAME;
        this.f316b = 0;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            this.f315a = packageInfo.versionName;
            this.f316b = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
        }
        String customVersionName = crittercismConfig.getCustomVersionName();
        if (customVersionName != null && customVersionName.length() > 0) {
            this.f315a = customVersionName;
        }
        if (crittercismConfig.isVersionCodeToBeIncludedInVersionString()) {
            this.f315a += "-" + Integer.toString(this.f316b);
        }
    }
}

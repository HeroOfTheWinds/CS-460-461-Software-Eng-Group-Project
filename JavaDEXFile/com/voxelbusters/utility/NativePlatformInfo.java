package com.voxelbusters.utility;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import com.voxelbusters.common.Configuration;

public class NativePlatformInfo {
    private static PackageInfo getPackageInfo() {
        Context applicationContext = Configuration.getContext().getApplicationContext();
        try {
            return applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPackageName() {
        PackageInfo packageInfo = getPackageInfo();
        Log.e("Utility", packageInfo.toString());
        return packageInfo != null ? packageInfo.packageName : null;
    }

    public static String getPackageVersionName() {
        PackageInfo packageInfo = getPackageInfo();
        return packageInfo != null ? packageInfo.versionName : null;
    }
}

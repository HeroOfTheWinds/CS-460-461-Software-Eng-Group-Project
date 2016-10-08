package com.unity3d.player;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.util.LinkedList;
import java.util.List;

/* renamed from: com.unity3d.player.n */
public final class C0829n implements C0818i {

    /* renamed from: com.unity3d.player.n.1 */
    final class C08281 extends Fragment {
        final /* synthetic */ List f182a;
        final /* synthetic */ FragmentManager f183b;
        final /* synthetic */ Runnable f184c;
        final /* synthetic */ C0829n f185d;

        C08281(C0829n c0829n, List list, FragmentManager fragmentManager, Runnable runnable) {
            this.f185d = c0829n;
            this.f182a = list;
            this.f183b = fragmentManager;
            this.f184c = runnable;
        }

        public final void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
            if (i == 15881) {
                int i2 = 0;
                while (i2 < strArr.length && i2 < iArr.length) {
                    C0827m.Log(4, strArr[i2] + (iArr[i2] == 0 ? " granted" : " denied"));
                    i2++;
                }
                FragmentTransaction beginTransaction = this.f183b.beginTransaction();
                beginTransaction.remove(this);
                beginTransaction.commit();
                this.f184c.run();
            }
        }

        public final void onStart() {
            super.onStart();
            requestPermissions((String[]) this.f182a.toArray(new String[0]), 15881);
        }
    }

    private static boolean m154a(PackageItemInfo packageItemInfo) {
        try {
            return packageItemInfo.metaData.getBoolean("unityplayer.SkipPermissionsDialog");
        } catch (Exception e) {
            return false;
        }
    }

    public final void m155a(Activity activity, Runnable runnable) {
        if (activity != null) {
            PackageManager packageManager = activity.getPackageManager();
            try {
                PackageItemInfo activityInfo = packageManager.getActivityInfo(activity.getComponentName(), AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                PackageItemInfo applicationInfo = packageManager.getApplicationInfo(activity.getPackageName(), AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                if (C0829n.m154a(activityInfo) || C0829n.m154a(applicationInfo)) {
                    runnable.run();
                    return;
                }
            } catch (Exception e) {
            }
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(activity.getPackageName(), AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD);
                if (packageInfo.requestedPermissions == null) {
                    packageInfo.requestedPermissions = new String[0];
                }
                List linkedList = new LinkedList();
                for (String str : packageInfo.requestedPermissions) {
                    try {
                        if (packageManager.getPermissionInfo(str, AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS).protectionLevel == 1 && activity.checkCallingOrSelfPermission(str) != 0) {
                            linkedList.add(str);
                        }
                    } catch (NameNotFoundException e2) {
                        C0827m.Log(5, "Failed to get permission info for " + str + ", manifest likely missing custom permission declaration");
                        C0827m.Log(5, "Permission " + str + " ignored");
                    }
                }
                if (linkedList.isEmpty()) {
                    runnable.run();
                    return;
                }
                FragmentManager fragmentManager = activity.getFragmentManager();
                Fragment c08281 = new C08281(this, linkedList, fragmentManager, runnable);
                FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
                beginTransaction.add(0, c08281);
                beginTransaction.commit();
            } catch (Exception e3) {
                C0827m.Log(6, "Unable to query for permission: " + e3.getMessage());
            }
        }
    }
}

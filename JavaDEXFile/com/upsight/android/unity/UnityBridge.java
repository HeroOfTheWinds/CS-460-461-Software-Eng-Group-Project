package com.upsight.android.unity;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.unity3d.player.UnityPlayer;
import spacemadness.com.lunarconsole.BuildConfig;

public class UnityBridge {
    protected static final String MANAGER_NAME = "UpsightManager";
    protected static final String TAG = "Upsight";

    /* renamed from: com.upsight.android.unity.UnityBridge.1 */
    static final class C09781 implements Runnable {
        final /* synthetic */ Runnable val$r;

        C09781(Runnable runnable) {
            this.val$r = runnable;
        }

        public void run() {
            try {
                this.val$r.run();
            } catch (Exception e) {
                Log.e(UnityBridge.TAG, "Exception running command on UI thread: " + e.getMessage());
            }
        }
    }

    public static void UnitySendMessage(@NonNull String str) {
        UnityPlayer.UnitySendMessage(MANAGER_NAME, str, BuildConfig.FLAVOR);
    }

    public static void UnitySendMessage(@NonNull String str, @Nullable String str2) {
        if (str2 == null) {
            str2 = BuildConfig.FLAVOR;
        }
        UnityPlayer.UnitySendMessage(MANAGER_NAME, str, str2);
    }

    @Nullable
    public static Activity getActivity() {
        return UnityPlayer.currentActivity;
    }

    public static void runSafelyOnUiThread(@NonNull Runnable runnable) {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new C09781(runnable));
        }
    }
}

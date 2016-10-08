package com.upsight.android.unity;

import android.util.Log;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.session.UpsightSessionCallbacks;
import com.upsight.android.managedvariables.experience.UpsightUserExperience;
import com.upsight.android.managedvariables.experience.UpsightUserExperience.Handler;
import java.util.List;
import org.json.JSONArray;

public class UnitySessionCallbacks implements UpsightSessionCallbacks {
    protected static final String TAG = "UnitySessionCallbacks";
    private static boolean mShouldSynchronizeManagedVariables;

    /* renamed from: com.upsight.android.unity.UnitySessionCallbacks.1 */
    class C09791 implements Handler {
        C09791() {
        }

        public boolean onReceive() {
            return UnitySessionCallbacks.mShouldSynchronizeManagedVariables;
        }

        public void onSynchronize(List<String> list) {
            Log.i(UnitySessionCallbacks.TAG, "onSynchronize");
            JSONArray jSONArray = new JSONArray();
            for (String put : list) {
                jSONArray.put(put);
            }
            UnityBridge.UnitySendMessage("managedVariablesDidSynchronize", jSONArray.toString());
        }
    }

    static {
        mShouldSynchronizeManagedVariables = true;
    }

    public static void setShouldSynchronizeManagedVariables(boolean z) {
        mShouldSynchronizeManagedVariables = z;
    }

    public void onStart(UpsightContext upsightContext) {
        UpsightUserExperience.registerHandler(upsightContext, new C09791());
    }
}

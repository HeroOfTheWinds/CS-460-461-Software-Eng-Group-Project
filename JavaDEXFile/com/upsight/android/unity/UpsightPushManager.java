package com.upsight.android.unity;

import android.app.Activity;
import android.util.Log;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.googlepushservices.UpsightGooglePushServices;
import com.upsight.android.googlepushservices.UpsightGooglePushServices.OnRegisterListener;
import com.upsight.android.googlepushservices.UpsightGooglePushServices.OnUnregisterListener;
import com.upsight.android.googlepushservices.UpsightPushBillboard;
import com.upsight.android.marketing.UpsightBillboard;

public class UpsightPushManager implements IUpsightExtensionManager {
    protected static final String TAG = "Upsight-UnityPush";
    private UpsightBillboard mPushBillboard;
    private BillboardHandler mPushBillboardHandler;
    private UpsightContext mUpsight;

    /* renamed from: com.upsight.android.unity.UpsightPushManager.1 */
    class C09941 implements Runnable {

        /* renamed from: com.upsight.android.unity.UpsightPushManager.1.1 */
        class C09931 implements OnUnregisterListener {
            C09931() {
            }

            public void onFailure(UpsightException upsightException) {
                Log.e(UpsightPushManager.TAG, "unregistration failed: " + upsightException);
            }

            public void onSuccess() {
                Log.i(UpsightPushManager.TAG, "unregistration succeeded");
            }
        }

        C09941() {
        }

        public void run() {
            Log.i(UpsightPushManager.TAG, "unregistering for push notifications");
            UpsightGooglePushServices.unregister(UpsightPushManager.this.mUpsight, new C09931());
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPushManager.2 */
    class C09962 implements Runnable {

        /* renamed from: com.upsight.android.unity.UpsightPushManager.2.1 */
        class C09951 implements OnRegisterListener {
            C09951() {
            }

            public void onFailure(UpsightException upsightException) {
                Log.e(UpsightPushManager.TAG, "registration failed: " + upsightException);
            }

            public void onSuccess(String str) {
                Log.i(UpsightPushManager.TAG, "registration succeeded");
            }
        }

        C09962() {
        }

        public void run() {
            Log.i(UpsightPushManager.TAG, "registering for push notifications");
            UpsightGooglePushServices.register(UpsightPushManager.this.mUpsight, new C09951());
        }
    }

    public void init(UpsightContext upsightContext) {
        this.mUpsight = upsightContext;
        Activity activity = UnityBridge.getActivity();
        if (activity != null) {
            this.mPushBillboardHandler = new BillboardHandler(activity);
            this.mPushBillboard = UpsightPushBillboard.create(this.mUpsight, this.mPushBillboardHandler);
        }
    }

    public void onApplicationPaused() {
        if (this.mUpsight != null) {
            try {
                if (this.mPushBillboard != null) {
                    this.mPushBillboard.destroy();
                    this.mPushBillboard = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onApplicationResumed() {
        if (this.mUpsight != null) {
            try {
                if (this.mPushBillboard == null) {
                    this.mPushBillboard = UpsightPushBillboard.create(this.mUpsight, this.mPushBillboardHandler);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void registerForPushNotifications() {
        if (this.mUpsight != null) {
            UnityBridge.runSafelyOnUiThread(new C09962());
        }
    }

    public void unregisterForPushNotifications() {
        if (this.mUpsight != null) {
            UnityBridge.runSafelyOnUiThread(new C09941());
        }
    }
}

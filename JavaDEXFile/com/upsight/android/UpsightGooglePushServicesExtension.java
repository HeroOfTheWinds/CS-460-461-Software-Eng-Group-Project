package com.upsight.android;

import com.upsight.android.googlepushservices.UpsightGooglePushServices.OnRegisterListener;
import com.upsight.android.googlepushservices.UpsightGooglePushServicesApi;
import com.upsight.android.googlepushservices.UpsightGooglePushServicesComponent;
import com.upsight.android.googlepushservices.internal.DaggerGooglePushServicesComponent;
import com.upsight.android.googlepushservices.internal.PushConfigManager;
import com.upsight.android.googlepushservices.internal.PushConfigManager.Config;
import com.upsight.android.googlepushservices.internal.PushModule;
import java.io.IOException;
import javax.inject.Inject;
import rx.functions.Action1;

public class UpsightGooglePushServicesExtension extends UpsightExtension<UpsightGooglePushServicesComponent, UpsightGooglePushServicesApi> {
    public static final String EXTENSION_NAME = "com.upsight.extension.googlepushservices";
    private static final String LOG_TAG;
    @Inject
    PushConfigManager mPushConfigManager;
    @Inject
    UpsightGooglePushServicesApi mUpsightPush;

    /* renamed from: com.upsight.android.UpsightGooglePushServicesExtension.1 */
    class C08471 implements Action1<Config> {
        final /* synthetic */ UpsightContext val$upsight;

        /* renamed from: com.upsight.android.UpsightGooglePushServicesExtension.1.1 */
        class C08461 implements OnRegisterListener {
            C08461() {
            }

            public void onFailure(UpsightException upsightException) {
                C08471.this.val$upsight.getLogger().m207e(UpsightGooglePushServicesExtension.LOG_TAG, "Failed to auto-register for push notifications", upsightException);
            }

            public void onSuccess(String str) {
                C08471.this.val$upsight.getLogger().m205d(UpsightGooglePushServicesExtension.LOG_TAG, "Auto-registered for push notifications with registrationId=" + str, new Object[0]);
            }
        }

        C08471(UpsightContext upsightContext) {
            this.val$upsight = upsightContext;
        }

        public void call(Config config) {
            if (config.autoRegister) {
                UpsightGooglePushServicesExtension.this.mUpsightPush.register(new C08461());
            } else {
                this.val$upsight.getLogger().m205d(UpsightGooglePushServicesExtension.LOG_TAG, "Skipping auto-registration of push notifications", new Object[0]);
            }
        }
    }

    static {
        LOG_TAG = UpsightGooglePushServicesExtension.class.getSimpleName();
    }

    UpsightGooglePushServicesExtension() {
    }

    public UpsightGooglePushServicesApi getApi() {
        return this.mUpsightPush;
    }

    protected void onPostCreate(UpsightContext upsightContext) {
        try {
            this.mPushConfigManager.fetchCurrentConfigObservable().subscribeOn(upsightContext.getCoreComponent().subscribeOnScheduler()).observeOn(upsightContext.getCoreComponent().observeOnScheduler()).subscribe(new C08471(upsightContext));
        } catch (IOException e) {
            upsightContext.getLogger().m207e(LOG_TAG, "Failed to fetch push configurations", e);
        }
    }

    protected UpsightGooglePushServicesComponent onResolve(UpsightContext upsightContext) {
        return DaggerGooglePushServicesComponent.builder().pushModule(new PushModule(upsightContext)).build();
    }
}

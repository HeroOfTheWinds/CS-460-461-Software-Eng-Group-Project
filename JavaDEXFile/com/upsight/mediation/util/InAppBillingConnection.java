package com.upsight.mediation.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.upsight.android.analytics.UpsightGooglePlayHelper;
import com.upsight.mediation.log.FuseLog;
import com.voxelbusters.nativeplugins.defines.Keys.Billing;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.json.JSONObject;

public class InAppBillingConnection {
    @Nullable
    private Method asInterfaceMethod;
    @Nullable
    private Activity boundActivity;
    @Nullable
    private ServiceConnection connection;
    @Nullable
    private Method getDetailsMethod;
    @Nullable
    private Object iabService;
    @NonNull
    private Status status;

    /* renamed from: com.upsight.mediation.util.InAppBillingConnection.1 */
    class C10181 implements ServiceConnection {
        C10181() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                InAppBillingConnection.this.iabService = InAppBillingConnection.this.asInterfaceMethod.invoke(null, new Object[]{iBinder});
                InAppBillingConnection.this.status = Status.Connected;
            } catch (Throwable th) {
                FuseLog.m237e("Fuse-InAppBillingConnection", "Exception thrown while attempting to retrieve IInAppBillingService", th);
                InAppBillingConnection.this.closeConnection();
                InAppBillingConnection.this.status = Status.Unavailable;
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            InAppBillingConnection.this.closeConnection();
        }
    }

    /* renamed from: com.upsight.mediation.util.InAppBillingConnection.2 */
    class C10192 extends ArrayList<String> {
        final /* synthetic */ String val$productId;

        C10192(String str) {
            this.val$productId = str;
            add(this.val$productId);
        }
    }

    public enum Status {
        Unavailable,
        NotConnected,
        Pending,
        Connected
    }

    public InAppBillingConnection() {
        this.iabService = null;
        this.asInterfaceMethod = null;
        this.getDetailsMethod = null;
        this.connection = null;
        this.boundActivity = null;
        this.status = Status.NotConnected;
        try {
            this.getDetailsMethod = Class.forName("com.android.vending.billing.IInAppBillingService").getMethod("getSkuDetails", new Class[]{Integer.TYPE, String.class, String.class, Bundle.class});
            this.asInterfaceMethod = Class.forName("com.android.vending.billing.IInAppBillingService$Stub").getMethod("asInterface", new Class[]{IBinder.class});
        } catch (Throwable th) {
            FuseLog.public_w("Fuse-InAppBillingConnection", "com.android.vending.billing.IInAppBillingService is NOT available", th);
            this.status = Status.Unavailable;
        }
    }

    public void closeConnection() {
        if (!(this.connection == null || this.boundActivity == null)) {
            this.boundActivity.unbindService(this.connection);
        }
        this.iabService = null;
        this.connection = null;
        this.boundActivity = null;
        this.status = Status.NotConnected;
    }

    @Nullable
    public String getLocalPriceForProductId(@NonNull String str) {
        if (this.status != Status.Connected || this.getDetailsMethod == null || this.boundActivity == null) {
            return null;
        }
        new Bundle().putStringArrayList("ITEM_ID_LIST", new C10192(str));
        try {
            Bundle bundle = (Bundle) this.getDetailsMethod.invoke(this.iabService, new Object[]{Integer.valueOf(3), this.boundActivity.getPackageName(), "inapp", bundle});
            if (bundle != null && bundle.getInt(UpsightGooglePlayHelper.PURCHASE_RESPONSE_CODE) == 0) {
                ArrayList stringArrayList = bundle.getStringArrayList("DETAILS_LIST");
                if (stringArrayList != null && stringArrayList.size() == 1) {
                    JSONObject jSONObject = new JSONObject((String) stringArrayList.get(0));
                    String string = jSONObject.getString(Billing.PRODUCT_IDENTIFIER);
                    if (string != null && string.equals(str)) {
                        return jSONObject.getString("price");
                    }
                }
            }
        } catch (Throwable th) {
            FuseLog.m237e("Fuse-InAppBillingConnection", "Exception thrown while getting SKU Details", th);
        }
        return null;
    }

    public void initConnection(@NonNull Activity activity) {
        if (this.status == Status.NotConnected && this.asInterfaceMethod != null) {
            this.status = Status.Pending;
            this.boundActivity = activity;
            this.connection = new C10181();
            Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            intent.setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE);
            this.boundActivity.bindService(intent, this.connection, 1);
        }
    }

    public boolean isConnected() {
        return this.status == Status.Connected;
    }

    public boolean isPending() {
        return this.status == Status.Pending;
    }
}

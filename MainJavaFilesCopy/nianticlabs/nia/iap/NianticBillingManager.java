package com.nianticlabs.nia.iap;

import android.content.Context;
import com.nianticlabs.nia.contextservice.ContextService;
import com.nianticlabs.nia.iap.InAppBillingProvider.Delegate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class NianticBillingManager extends ContextService implements Delegate {
    private InAppBillingProvider inAppBillingProvider;
    private boolean initializing;

    /* renamed from: com.nianticlabs.nia.iap.NianticBillingManager.1 */
    class C07621 implements Runnable {
        C07621() {
        }

        public void run() {
            NianticBillingManager.this.nativeInitializeCallback();
        }
    }

    /* renamed from: com.nianticlabs.nia.iap.NianticBillingManager.2 */
    class C07632 implements Runnable {
        final /* synthetic */ ArrayList val$items;

        C07632(ArrayList arrayList) {
            this.val$items = arrayList;
        }

        public void run() {
            NianticBillingManager.this.inAppBillingProvider.getPurchasableItems(this.val$items);
        }
    }

    /* renamed from: com.nianticlabs.nia.iap.NianticBillingManager.3 */
    class C07643 implements Runnable {
        final /* synthetic */ String val$item;
        final /* synthetic */ String val$userIdToken;

        C07643(String str, String str2) {
            this.val$item = str;
            this.val$userIdToken = str2;
        }

        public void run() {
            NianticBillingManager.this.inAppBillingProvider.purchaseItem(this.val$item, this.val$userIdToken);
        }
    }

    /* renamed from: com.nianticlabs.nia.iap.NianticBillingManager.4 */
    class C07654 implements Runnable {
        final /* synthetic */ boolean val$success;
        final /* synthetic */ String val$transactionToken;

        C07654(boolean z, String str) {
            this.val$success = z;
            this.val$transactionToken = str;
        }

        public void run() {
            NianticBillingManager.this.inAppBillingProvider.onProcessedGoogleBillingTransaction(this.val$success, this.val$transactionToken);
        }
    }

    /* renamed from: com.nianticlabs.nia.iap.NianticBillingManager.5 */
    class C07665 implements Runnable {
        final /* synthetic */ boolean val$connected;

        C07665(boolean z) {
            this.val$connected = z;
        }

        public void run() {
            synchronized (NianticBillingManager.this.callbackLock) {
                NianticBillingManager.this.nativeOnConnectionStateChanged(this.val$connected);
            }
        }
    }

    public NianticBillingManager(Context context, long j) {
        super(context, j);
        this.inAppBillingProvider = new GoogleInAppBillingProvider(context);
    }

    private native void nativeInitializeCallback();

    private native void nativeOnConnectionStateChanged(boolean z);

    private native void nativeProcessReceipt(String str, String str2, String str3, int i);

    private native void nativePurchasableItemsResult(PurchasableItemDetails[] purchasableItemDetailsArr);

    private native void nativePurchaseResult(int i);

    private native void nativeRecordPurchase(boolean z, String str, int i, float f, String str2, String str3);

    public void ProcessReceipt(String str, String str2, String str3, int i) {
        synchronized (this.callbackLock) {
            nativeProcessReceipt(str, str2, str3, i);
        }
    }

    public void getPurchasableItems(String[] strArr) {
        ContextService.runOnServiceHandler(new C07632(new ArrayList(Arrays.asList(strArr))));
    }

    public void initialize() {
        this.initializing = true;
        this.inAppBillingProvider.setDelegate(this);
        ContextService.runOnServiceHandler(new C07621());
    }

    public boolean isBillingAvailable() {
        return this.inAppBillingProvider.isBillingAvailable();
    }

    public boolean isTransactionInProgress() {
        return this.inAppBillingProvider.isTransactionInProgress();
    }

    public void onConnectionStateChanged(boolean z) {
        ContextService.runOnServiceHandler(new C07665(z));
    }

    public void onPause() {
        this.inAppBillingProvider.onPause();
    }

    public void onResume() {
        this.inAppBillingProvider.onResume();
    }

    public void onStart() {
    }

    public void onStop() {
    }

    public void purchasableItemsResult(Collection<PurchasableItemDetails> collection) {
        PurchasableItemDetails[] purchasableItemDetailsArr = new PurchasableItemDetails[collection.size()];
        int i = 0;
        for (PurchasableItemDetails purchasableItemDetails : collection) {
            purchasableItemDetailsArr[i] = purchasableItemDetails;
            i++;
        }
        synchronized (this.callbackLock) {
            nativePurchasableItemsResult(purchasableItemDetailsArr);
        }
    }

    public void purchaseResult(PurchaseResult purchaseResult) {
        synchronized (this.callbackLock) {
            nativePurchaseResult(purchaseResult.ordinal());
        }
    }

    public void purchaseVendorItem(String str, String str2) {
        ContextService.runOnServiceHandler(new C07643(str, str2));
    }

    public void redeemReceiptResult(boolean z, String str) {
        ContextService.runOnServiceHandler(new C07654(z, str));
    }
}

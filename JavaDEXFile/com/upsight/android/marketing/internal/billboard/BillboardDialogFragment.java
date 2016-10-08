package com.upsight.android.marketing.internal.billboard;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import com.upsight.android.UpsightContext;
import com.upsight.android.marketing.internal.content.MarketingContent;
import com.upsight.android.marketing.internal.content.MarketingContent.PendingDialog;
import com.upsight.android.marketing.internal.content.MarketingContentStore;
import javax.inject.Inject;

public final class BillboardDialogFragment extends DialogFragment {
    private static final String FRAGMENT_BUNDLE_KEY_BUTTONS = "buttons";
    private static final String FRAGMENT_BUNDLE_KEY_DIALOG_THEME = "dialogTheme";
    private static final String FRAGMENT_BUNDLE_KEY_DISMISS_TRIGGER = "dismissTrigger";
    private static final String FRAGMENT_BUNDLE_KEY_ID = "id";
    private static final String FRAGMENT_BUNDLE_KEY_MESSAGE = "message";
    private static final String FRAGMENT_BUNDLE_KEY_TITLE = "title";
    private static final String LOG_TAG;
    @Inject
    MarketingContentStore mContentStore;
    @Inject
    UpsightContext mUpsight;

    /* renamed from: com.upsight.android.marketing.internal.billboard.BillboardDialogFragment.1 */
    class C09511 implements OnClickListener {
        final /* synthetic */ String val$buttonTrigger;
        final /* synthetic */ String val$id;

        C09511(String str, String str2) {
            this.val$id = str;
            this.val$buttonTrigger = str2;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            BillboardDialogFragment.this.executeActions(this.val$id, this.val$buttonTrigger);
        }
    }

    /* renamed from: com.upsight.android.marketing.internal.billboard.BillboardDialogFragment.2 */
    class C09522 implements OnClickListener {
        final /* synthetic */ String val$buttonTrigger;
        final /* synthetic */ String val$id;

        C09522(String str, String str2) {
            this.val$id = str;
            this.val$buttonTrigger = str2;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            BillboardDialogFragment.this.executeActions(this.val$id, this.val$buttonTrigger);
        }
    }

    /* renamed from: com.upsight.android.marketing.internal.billboard.BillboardDialogFragment.3 */
    class C09533 implements OnClickListener {
        final /* synthetic */ String val$buttonTrigger;
        final /* synthetic */ String val$id;

        C09533(String str, String str2) {
            this.val$id = str;
            this.val$buttonTrigger = str2;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            BillboardDialogFragment.this.executeActions(this.val$id, this.val$buttonTrigger);
        }
    }

    /* renamed from: com.upsight.android.marketing.internal.billboard.BillboardDialogFragment.4 */
    class C09544 implements OnClickListener {
        final /* synthetic */ String val$buttonTrigger;
        final /* synthetic */ String val$id;

        C09544(String str, String str2) {
            this.val$id = str;
            this.val$buttonTrigger = str2;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            BillboardDialogFragment.this.executeActions(this.val$id, this.val$buttonTrigger);
        }
    }

    /* renamed from: com.upsight.android.marketing.internal.billboard.BillboardDialogFragment.5 */
    class C09555 implements OnClickListener {
        final /* synthetic */ String val$buttonTrigger;
        final /* synthetic */ String val$id;

        C09555(String str, String str2) {
            this.val$id = str;
            this.val$buttonTrigger = str2;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            BillboardDialogFragment.this.executeActions(this.val$id, this.val$buttonTrigger);
        }
    }

    /* renamed from: com.upsight.android.marketing.internal.billboard.BillboardDialogFragment.6 */
    class C09566 implements OnClickListener {
        final /* synthetic */ String val$buttonTrigger;
        final /* synthetic */ String val$id;

        C09566(String str, String str2) {
            this.val$id = str;
            this.val$buttonTrigger = str2;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            BillboardDialogFragment.this.executeActions(this.val$id, this.val$buttonTrigger);
        }
    }

    static {
        LOG_TAG = BillboardDialogFragment.class.getSimpleName();
    }

    private void executeActions(String str, String str2) {
        MarketingContent marketingContent = (MarketingContent) this.mContentStore.get(str);
        if (marketingContent != null) {
            marketingContent.executeActions(str2);
        }
    }

    public static DialogFragment newInstance(PendingDialog pendingDialog) {
        DialogFragment billboardDialogFragment = new BillboardDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_BUNDLE_KEY_ID, pendingDialog.mId);
        bundle.putString(FRAGMENT_BUNDLE_KEY_TITLE, pendingDialog.mTitle);
        bundle.putString(FRAGMENT_BUNDLE_KEY_MESSAGE, pendingDialog.mMessage);
        bundle.putString(FRAGMENT_BUNDLE_KEY_BUTTONS, pendingDialog.mButtons);
        bundle.putString(FRAGMENT_BUNDLE_KEY_DISMISS_TRIGGER, pendingDialog.mDismissTrigger);
        billboardDialogFragment.setArguments(bundle);
        return billboardDialogFragment;
    }

    public static DialogFragment newInstance(PendingDialog pendingDialog, int i) {
        DialogFragment billboardDialogFragment = new BillboardDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_BUNDLE_KEY_ID, pendingDialog.mId);
        bundle.putString(FRAGMENT_BUNDLE_KEY_TITLE, pendingDialog.mTitle);
        bundle.putString(FRAGMENT_BUNDLE_KEY_MESSAGE, pendingDialog.mMessage);
        bundle.putString(FRAGMENT_BUNDLE_KEY_BUTTONS, pendingDialog.mButtons);
        bundle.putString(FRAGMENT_BUNDLE_KEY_DISMISS_TRIGGER, pendingDialog.mDismissTrigger);
        bundle.putInt(FRAGMENT_BUNDLE_KEY_DIALOG_THEME, i);
        billboardDialogFragment.setArguments(bundle);
        return billboardDialogFragment;
    }

    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        Bundle arguments = getArguments();
        executeActions(arguments.getString(FRAGMENT_BUNDLE_KEY_ID), arguments.getString(FRAGMENT_BUNDLE_KEY_DISMISS_TRIGGER));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.app.Dialog onCreateDialog(android.os.Bundle r9) {
        /*
        r8 = this;
        r7 = 0;
        r0 = r8.getActivity();
        r0 = com.upsight.android.Upsight.createContext(r0);
        r1 = "com.upsight.extension.marketing";
        r0 = r0.getUpsightExtension(r1);
        r0 = (com.upsight.android.UpsightMarketingExtension) r0;
        if (r0 != 0) goto L_0x0015;
    L_0x0013:
        r0 = 0;
    L_0x0014:
        return r0;
    L_0x0015:
        r0 = r0.getComponent();
        r0 = (com.upsight.android.marketing.UpsightMarketingComponent) r0;
        r0.inject(r8);
        r0 = r8.getArguments();
        r1 = "id";
        r2 = r0.getString(r1);
        r1 = "title";
        r3 = r0.getString(r1);
        r1 = "message";
        r4 = r0.getString(r1);
        r1 = "buttons";
        r5 = r0.getString(r1);
        r1 = "dialogTheme";
        r1 = r0.containsKey(r1);
        if (r1 == 0) goto L_0x0094;
    L_0x0042:
        r1 = "dialogTheme";
        r1 = r0.getInt(r1);
        r0 = new android.app.AlertDialog$Builder;
        r6 = r8.getActivity();
        r0.<init>(r6, r1);
        r1 = r0;
    L_0x0052:
        r0 = r1.setTitle(r3);
        r0.setMessage(r4);
        r0 = android.text.TextUtils.isEmpty(r5);
        if (r0 != 0) goto L_0x008f;
    L_0x005f:
        r0 = r8.mUpsight;
        r0 = r0.getCoreComponent();
        r0.gson();
        r0 = r8.mUpsight;	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = r0.getCoreComponent();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = r0.jsonParser();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = r0.parse(r5);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r3 = r0.isJsonArray();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        if (r3 == 0) goto L_0x01d8;
    L_0x007c:
        r3 = r0.getAsJsonArray();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r3 = r3.size();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = r0.getAsJsonArray();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r4 = r0.iterator();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        switch(r3) {
            case 1: goto L_0x009f;
            case 2: goto L_0x00e4;
            case 3: goto L_0x0146;
            default: goto L_0x008f;
        };
    L_0x008f:
        r0 = r1.create();
        goto L_0x0014;
    L_0x0094:
        r0 = new android.app.AlertDialog$Builder;
        r1 = r8.getActivity();
        r0.<init>(r1);
        r1 = r0;
        goto L_0x0052;
    L_0x009f:
        r0 = r4.hasNext();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        if (r0 == 0) goto L_0x008f;
    L_0x00a5:
        r0 = r4.next();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = (com.google.gson.JsonElement) r0;	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r3 = r0.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r4 = "text";
        r3 = r3.get(r4);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r3 = r3.getAsString();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r4 = new com.upsight.android.marketing.internal.billboard.BillboardDialogFragment$1;	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = r0.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r5 = "trigger";
        r0 = r0.get(r5);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = r0.getAsString();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r4.<init>(r2, r0);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r1.setNeutralButton(r3, r4);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        goto L_0x008f;
    L_0x00d0:
        r0 = move-exception;
        r2 = r8.mUpsight;
        r2 = r2.getLogger();
        r3 = LOG_TAG;
        r4 = "Failed to parse button due to malformed JSON";
        r5 = 1;
        r5 = new java.lang.Object[r5];
        r5[r7] = r0;
        r2.m207e(r3, r4, r5);
        goto L_0x008f;
    L_0x00e4:
        r0 = r4.hasNext();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        if (r0 == 0) goto L_0x0114;
    L_0x00ea:
        r0 = r4.next();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = (com.google.gson.JsonElement) r0;	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r3 = r0.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r5 = "text";
        r3 = r3.get(r5);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r3 = r3.getAsString();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r5 = new com.upsight.android.marketing.internal.billboard.BillboardDialogFragment$2;	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = r0.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r6 = "trigger";
        r0 = r0.get(r6);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = r0.getAsString();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r5.<init>(r2, r0);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r1.setNegativeButton(r3, r5);	 Catch:{ JsonSyntaxException -> 0x00d0 }
    L_0x0114:
        r0 = r4.hasNext();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        if (r0 == 0) goto L_0x008f;
    L_0x011a:
        r0 = r4.next();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = (com.google.gson.JsonElement) r0;	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r3 = r0.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r4 = "text";
        r3 = r3.get(r4);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r3 = r3.getAsString();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r4 = new com.upsight.android.marketing.internal.billboard.BillboardDialogFragment$3;	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = r0.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r5 = "trigger";
        r0 = r0.get(r5);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = r0.getAsString();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r4.<init>(r2, r0);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r1.setPositiveButton(r3, r4);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        goto L_0x008f;
    L_0x0146:
        r0 = r4.hasNext();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        if (r0 == 0) goto L_0x0176;
    L_0x014c:
        r0 = r4.next();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = (com.google.gson.JsonElement) r0;	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r3 = r0.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r5 = "text";
        r3 = r3.get(r5);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r3 = r3.getAsString();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r5 = new com.upsight.android.marketing.internal.billboard.BillboardDialogFragment$4;	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = r0.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r6 = "trigger";
        r0 = r0.get(r6);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = r0.getAsString();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r5.<init>(r2, r0);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r1.setNegativeButton(r3, r5);	 Catch:{ JsonSyntaxException -> 0x00d0 }
    L_0x0176:
        r0 = r4.hasNext();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        if (r0 == 0) goto L_0x01a6;
    L_0x017c:
        r0 = r4.next();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = (com.google.gson.JsonElement) r0;	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r3 = r0.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r5 = "text";
        r3 = r3.get(r5);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r3 = r3.getAsString();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r5 = new com.upsight.android.marketing.internal.billboard.BillboardDialogFragment$5;	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = r0.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r6 = "trigger";
        r0 = r0.get(r6);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = r0.getAsString();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r5.<init>(r2, r0);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r1.setNeutralButton(r3, r5);	 Catch:{ JsonSyntaxException -> 0x00d0 }
    L_0x01a6:
        r0 = r4.hasNext();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        if (r0 == 0) goto L_0x008f;
    L_0x01ac:
        r0 = r4.next();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = (com.google.gson.JsonElement) r0;	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r3 = r0.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r4 = "text";
        r3 = r3.get(r4);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r3 = r3.getAsString();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r4 = new com.upsight.android.marketing.internal.billboard.BillboardDialogFragment$6;	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = r0.getAsJsonObject();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r5 = "trigger";
        r0 = r0.get(r5);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = r0.getAsString();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r4.<init>(r2, r0);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r1.setPositiveButton(r3, r4);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        goto L_0x008f;
    L_0x01d8:
        r0 = r8.mUpsight;	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0 = r0.getLogger();	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r2 = LOG_TAG;	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r3 = "Failed to parse buttons because expected buttons array is missing";
        r4 = 0;
        r4 = new java.lang.Object[r4];	 Catch:{ JsonSyntaxException -> 0x00d0 }
        r0.m207e(r2, r3, r4);	 Catch:{ JsonSyntaxException -> 0x00d0 }
        goto L_0x008f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.upsight.android.marketing.internal.billboard.BillboardDialogFragment.onCreateDialog(android.os.Bundle):android.app.Dialog");
    }
}

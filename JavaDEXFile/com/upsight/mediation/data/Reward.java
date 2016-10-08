package com.upsight.mediation.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.upsight.mediation.RewardedInfo;
import com.upsight.mediation.log.FuseLog;
import com.upsight.mediation.util.HashMapCaster;
import com.upsight.mediation.util.StringUtil;
import java.util.HashMap;
import spacemadness.com.lunarconsole.BuildConfig;

public class Reward {
    private static final String TAG = "Reward";
    public final int itemId;
    public final int offerId;
    @NonNull
    public final String postRollMessage;
    @NonNull
    public final String preRollMessage;
    public final int rewardAmount;
    @NonNull
    public final String rewardItem;
    @Nullable
    public String richMediaPostrollScript;
    @Nullable
    public String richMediaPrerollScript;
    public final int zoneId;
    @NonNull
    public final String zoneName;

    public Reward(@Nullable String str, @Nullable String str2, @Nullable String str3, @Nullable String str4, @NonNull String str5, int i, int i2, int i3, int i4, @NonNull String str6) {
        if (str == null) {
            str = BuildConfig.FLAVOR;
        }
        this.preRollMessage = str;
        if (str2 == null) {
            str2 = BuildConfig.FLAVOR;
        }
        this.postRollMessage = str2;
        this.richMediaPrerollScript = str3;
        this.richMediaPostrollScript = str4;
        this.rewardItem = str5;
        this.rewardAmount = i;
        this.itemId = i2;
        this.offerId = i3;
        this.zoneId = i4;
        this.zoneName = str6;
    }

    @Nullable
    public static Reward createFromValues(HashMap<String, String> hashMap) {
        HashMapCaster hashMapCaster = new HashMapCaster(hashMap);
        String str = hashMapCaster.get("reward");
        String str2 = hashMapCaster.get("zone_id");
        if (str != null && str2 != null) {
            return new Reward(hashMapCaster.get("pre_roll"), hashMapCaster.get("post_roll"), hashMapCaster.get("pre_roll_script"), hashMapCaster.get("post_roll_script"), str, hashMapCaster.getInt("amount", 0), hashMapCaster.getInt("item_id"), hashMapCaster.getInt("offer_id"), hashMapCaster.getInt(TriggerIfContentAvailable.ID), str2);
        }
        FuseLog.public_w(TAG, "Reward ignored due to missing values: " + hashMapCaster.toString());
        return null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
        r4 = this;
        r0 = 1;
        r1 = 0;
        if (r4 != r5) goto L_0x0006;
    L_0x0004:
        r1 = r0;
    L_0x0005:
        return r1;
    L_0x0006:
        if (r5 == 0) goto L_0x007b;
    L_0x0008:
        r2 = r4.getClass();
        r3 = r5.getClass();
        if (r2 != r3) goto L_0x007b;
    L_0x0012:
        r5 = (com.upsight.mediation.data.Reward) r5;
        r2 = r4.rewardAmount;
        r3 = r5.rewardAmount;
        if (r2 != r3) goto L_0x007b;
    L_0x001a:
        r2 = r4.itemId;
        r3 = r5.itemId;
        if (r2 != r3) goto L_0x007b;
    L_0x0020:
        r2 = r4.offerId;
        r3 = r5.offerId;
        if (r2 != r3) goto L_0x007b;
    L_0x0026:
        r2 = r4.zoneId;
        r3 = r5.zoneId;
        if (r2 != r3) goto L_0x007b;
    L_0x002c:
        r2 = r4.preRollMessage;
        if (r2 == 0) goto L_0x0067;
    L_0x0030:
        r2 = r4.preRollMessage;
        r3 = r5.preRollMessage;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x007b;
    L_0x003a:
        r2 = r4.postRollMessage;
        if (r2 == 0) goto L_0x006c;
    L_0x003e:
        r2 = r4.postRollMessage;
        r3 = r5.postRollMessage;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x007b;
    L_0x0048:
        r2 = r4.rewardItem;
        if (r2 == 0) goto L_0x0071;
    L_0x004c:
        r2 = r4.rewardItem;
        r3 = r5.rewardItem;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x007b;
    L_0x0056:
        r2 = r4.zoneName;
        if (r2 == 0) goto L_0x0076;
    L_0x005a:
        r2 = r4.zoneName;
        r3 = r5.zoneName;
        r2 = r2.equals(r3);
        if (r2 != 0) goto L_0x0065;
    L_0x0064:
        r0 = r1;
    L_0x0065:
        r1 = r0;
        goto L_0x0005;
    L_0x0067:
        r2 = r5.preRollMessage;
        if (r2 == 0) goto L_0x003a;
    L_0x006b:
        goto L_0x0005;
    L_0x006c:
        r2 = r5.postRollMessage;
        if (r2 == 0) goto L_0x0048;
    L_0x0070:
        goto L_0x0005;
    L_0x0071:
        r2 = r5.rewardItem;
        if (r2 == 0) goto L_0x0056;
    L_0x0075:
        goto L_0x0005;
    L_0x0076:
        r2 = r5.zoneName;
        if (r2 != 0) goto L_0x0064;
    L_0x007a:
        goto L_0x0065;
    L_0x007b:
        r0 = r1;
        goto L_0x0004;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.upsight.mediation.data.Reward.equals(java.lang.Object):boolean");
    }

    @NonNull
    public RewardedInfo getInfo() {
        return new RewardedInfo(this.preRollMessage, this.postRollMessage, this.rewardItem, this.rewardAmount, this.itemId);
    }

    public boolean hasPostRollMessage() {
        return !StringUtil.isNullOrEmpty(this.postRollMessage);
    }

    public boolean hasPreRollMessage() {
        return !StringUtil.isNullOrEmpty(this.preRollMessage);
    }

    public boolean hasRichMediaPostroll() {
        return !StringUtil.isNullOrEmpty(this.richMediaPostrollScript);
    }

    public boolean hasRichMediaPreroll() {
        return !StringUtil.isNullOrEmpty(this.richMediaPrerollScript);
    }

    public int hashCode() {
        int i = 0;
        int hashCode = this.preRollMessage != null ? this.preRollMessage.hashCode() : 0;
        int hashCode2 = this.postRollMessage != null ? this.postRollMessage.hashCode() : 0;
        int hashCode3 = this.rewardItem != null ? this.rewardItem.hashCode() : 0;
        int i2 = this.rewardAmount;
        int i3 = this.itemId;
        int i4 = this.offerId;
        int i5 = this.zoneId;
        if (this.zoneName != null) {
            i = this.zoneName.hashCode();
        }
        return (((((((((((((hashCode * 31) + hashCode2) * 31) + hashCode3) * 31) + i2) * 31) + i3) * 31) + i4) * 31) + i5) * 31) + i;
    }

    public String toString() {
        return "RewardObject {zoneId:" + this.zoneId + ", offerId: " + this.offerId + ", itemId: " + this.itemId + ", rewardItem: " + this.rewardItem + ", rewardAmount: " + this.rewardAmount + ", preRollMessage: " + this.preRollMessage + ", rewardMessage: " + this.postRollMessage + "}";
    }
}

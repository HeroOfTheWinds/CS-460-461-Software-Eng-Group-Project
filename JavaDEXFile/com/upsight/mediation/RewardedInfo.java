package com.upsight.mediation;

import org.json.JSONException;
import org.json.JSONStringer;

public class RewardedInfo {
    public final String preRollMessage;
    public final int rewardAmount;
    public final String rewardItem;
    public final int rewardItemId;
    public final String rewardMessage;

    public RewardedInfo(String str, String str2, String str3, int i, int i2) {
        this.preRollMessage = str;
        this.rewardMessage = str2;
        this.rewardItem = str3;
        this.rewardAmount = i;
        this.rewardItemId = i2;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
        r4 = this;
        r0 = 1;
        r1 = 0;
        if (r4 != r5) goto L_0x0005;
    L_0x0004:
        return r0;
    L_0x0005:
        if (r5 == 0) goto L_0x005c;
    L_0x0007:
        r2 = r4.getClass();
        r3 = r5.getClass();
        if (r2 != r3) goto L_0x005c;
    L_0x0011:
        r5 = (com.upsight.mediation.RewardedInfo) r5;
        r2 = r4.rewardAmount;
        r3 = r5.rewardAmount;
        if (r2 != r3) goto L_0x005c;
    L_0x0019:
        r2 = r4.rewardItemId;
        r3 = r5.rewardItemId;
        if (r2 != r3) goto L_0x005c;
    L_0x001f:
        r2 = r4.preRollMessage;
        if (r2 == 0) goto L_0x004b;
    L_0x0023:
        r2 = r4.preRollMessage;
        r3 = r5.preRollMessage;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x005c;
    L_0x002d:
        r2 = r4.rewardMessage;
        if (r2 == 0) goto L_0x0051;
    L_0x0031:
        r2 = r4.rewardMessage;
        r3 = r5.rewardMessage;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x005c;
    L_0x003b:
        r2 = r4.rewardItem;
        if (r2 == 0) goto L_0x0057;
    L_0x003f:
        r2 = r4.rewardItem;
        r3 = r5.rewardItem;
        r2 = r2.equals(r3);
        if (r2 != 0) goto L_0x0004;
    L_0x0049:
        r0 = r1;
        goto L_0x0004;
    L_0x004b:
        r2 = r5.preRollMessage;
        if (r2 == 0) goto L_0x002d;
    L_0x004f:
        r0 = r1;
        goto L_0x0004;
    L_0x0051:
        r2 = r5.rewardMessage;
        if (r2 == 0) goto L_0x003b;
    L_0x0055:
        r0 = r1;
        goto L_0x0004;
    L_0x0057:
        r2 = r5.rewardItem;
        if (r2 != 0) goto L_0x0049;
    L_0x005b:
        goto L_0x0004;
    L_0x005c:
        r0 = r1;
        goto L_0x0004;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.upsight.mediation.RewardedInfo.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        int i = 0;
        int hashCode = this.preRollMessage != null ? this.preRollMessage.hashCode() : 0;
        int hashCode2 = this.rewardMessage != null ? this.rewardMessage.hashCode() : 0;
        if (this.rewardItem != null) {
            i = this.rewardItem.hashCode();
        }
        return (((((((hashCode * 31) + hashCode2) * 31) + i) * 31) + this.rewardAmount) * 31) + this.rewardItemId;
    }

    public String toJSONString() {
        try {
            return new JSONStringer().object().key("RewardedInfo").object().key("preRollMessage").value(this.preRollMessage).key("rewardMessage").value(this.rewardMessage).key("rewardItem").value(this.rewardItem).key("rewardAmount").value((long) this.rewardAmount).key("rewardItemId").value((long) this.rewardItemId).endObject().endObject().toString();
        } catch (JSONException e) {
            return "{ \"RewardedInfo\" : \"\" }";
        }
    }

    public String toString() {
        return "RewardedInfo{preRollMessage='" + this.preRollMessage + '\'' + ", rewardMessage='" + this.rewardMessage + '\'' + ", rewardItem='" + this.rewardItem + '\'' + ", rewardAmount=" + this.rewardAmount + ", rewardItemId=" + this.rewardItemId + '}';
    }
}

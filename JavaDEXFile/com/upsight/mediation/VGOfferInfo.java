package com.upsight.mediation;

import android.util.Base64;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONStringer;
import spacemadness.com.lunarconsole.BuildConfig;

public class VGOfferInfo {
    public int currencyID;
    public Date endTime;
    public int itemAmount;
    public String itemName;
    public String metadata;
    public String purchaseCurrency;
    public float purchasePrice;
    public Date startTime;
    public int virtualGoodID;

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
        if (r5 == 0) goto L_0x0094;
    L_0x0008:
        r2 = r4.getClass();
        r3 = r5.getClass();
        if (r2 != r3) goto L_0x0094;
    L_0x0012:
        r5 = (com.upsight.mediation.VGOfferInfo) r5;
        r2 = r5.purchasePrice;
        r3 = r4.purchasePrice;
        r2 = java.lang.Float.compare(r2, r3);
        if (r2 != 0) goto L_0x0094;
    L_0x001e:
        r2 = r4.itemAmount;
        r3 = r5.itemAmount;
        if (r2 != r3) goto L_0x0094;
    L_0x0024:
        r2 = r4.virtualGoodID;
        r3 = r5.virtualGoodID;
        if (r2 != r3) goto L_0x0094;
    L_0x002a:
        r2 = r4.currencyID;
        r3 = r5.currencyID;
        if (r2 != r3) goto L_0x0094;
    L_0x0030:
        r2 = r4.purchaseCurrency;
        if (r2 == 0) goto L_0x0079;
    L_0x0034:
        r2 = r4.purchaseCurrency;
        r3 = r5.purchaseCurrency;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0094;
    L_0x003e:
        r2 = r4.itemName;
        if (r2 == 0) goto L_0x007e;
    L_0x0042:
        r2 = r4.itemName;
        r3 = r5.itemName;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0094;
    L_0x004c:
        r2 = r4.startTime;
        if (r2 == 0) goto L_0x0083;
    L_0x0050:
        r2 = r4.startTime;
        r3 = r5.startTime;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0094;
    L_0x005a:
        r2 = r4.endTime;
        if (r2 == 0) goto L_0x0089;
    L_0x005e:
        r2 = r4.endTime;
        r3 = r5.endTime;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0094;
    L_0x0068:
        r2 = r4.metadata;
        if (r2 == 0) goto L_0x008f;
    L_0x006c:
        r2 = r4.metadata;
        r3 = r5.metadata;
        r2 = r2.equals(r3);
        if (r2 != 0) goto L_0x0077;
    L_0x0076:
        r0 = r1;
    L_0x0077:
        r1 = r0;
        goto L_0x0005;
    L_0x0079:
        r2 = r5.purchaseCurrency;
        if (r2 == 0) goto L_0x003e;
    L_0x007d:
        goto L_0x0005;
    L_0x007e:
        r2 = r5.itemName;
        if (r2 == 0) goto L_0x004c;
    L_0x0082:
        goto L_0x0005;
    L_0x0083:
        r2 = r5.startTime;
        if (r2 == 0) goto L_0x005a;
    L_0x0087:
        goto L_0x0005;
    L_0x0089:
        r2 = r5.endTime;
        if (r2 == 0) goto L_0x0068;
    L_0x008d:
        goto L_0x0005;
    L_0x008f:
        r2 = r5.metadata;
        if (r2 != 0) goto L_0x0076;
    L_0x0093:
        goto L_0x0077;
    L_0x0094:
        r0 = r1;
        goto L_0x0004;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.upsight.mediation.VGOfferInfo.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        int i = 0;
        int hashCode = this.purchaseCurrency != null ? this.purchaseCurrency.hashCode() : 0;
        int floatToIntBits = this.purchasePrice != 0.0f ? Float.floatToIntBits(this.purchasePrice) : 0;
        int hashCode2 = this.itemName != null ? this.itemName.hashCode() : 0;
        int i2 = this.itemAmount;
        int i3 = this.virtualGoodID;
        int i4 = this.currencyID;
        int hashCode3 = this.startTime != null ? this.startTime.hashCode() : 0;
        int hashCode4 = this.endTime != null ? this.endTime.hashCode() : 0;
        if (this.metadata != null) {
            i = this.metadata.hashCode();
        }
        return (((((((((((((((hashCode * 31) + floatToIntBits) * 31) + hashCode2) * 31) + i2) * 31) + i3) * 31) + i4) * 31) + hashCode3) * 31) + hashCode4) * 31) + i;
    }

    public String toJSONString() {
        long j = 0;
        try {
            JSONStringer key = new JSONStringer().object().key("VGOfferInfo").object().key("purchaseCurrency").value(this.purchaseCurrency).key("purchasePrice").value((double) this.purchasePrice).key("itemName").value(this.itemName).key("itemAmount").value((long) this.itemAmount).key("virtualGoodID").value((long) this.virtualGoodID).key("currencyID").value((long) this.currencyID).key("startTime").value(this.startTime == null ? 0 : this.startTime.getTime()).key("endTime");
            if (this.endTime != null) {
                j = this.endTime.getTime();
            }
            return key.value(j).key("metadata").value(this.metadata == null ? BuildConfig.FLAVOR : Base64.encodeToString(this.metadata.getBytes(), 2)).endObject().endObject().toString();
        } catch (JSONException e) {
            return "{ \"VGOfferInfo\" : \"\" }";
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("VGOfferInfo{");
        stringBuffer.append("purchaseCurrency='").append(this.purchaseCurrency).append('\'');
        stringBuffer.append(", purchasePrice=").append(this.purchasePrice);
        stringBuffer.append(", itemName='").append(this.itemName).append('\'');
        stringBuffer.append(", itemAmount=").append(this.itemAmount);
        stringBuffer.append(", virtualGoodID=").append(this.virtualGoodID);
        stringBuffer.append(", currencyID=").append(this.currencyID);
        stringBuffer.append(", startTime=").append(this.startTime);
        stringBuffer.append(", endTime=").append(this.endTime);
        stringBuffer.append(", metadata='").append(this.metadata).append('\'');
        stringBuffer.append('}');
        return stringBuffer.toString();
    }
}

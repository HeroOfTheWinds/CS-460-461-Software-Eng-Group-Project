package com.upsight.mediation;

import android.util.Base64;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONStringer;
import spacemadness.com.lunarconsole.BuildConfig;

public class IAPOfferInfo {
    public Date endTime;
    public int itemAmount;
    public String itemName;
    public String metadata;
    public String productID;
    public float productPrice;
    public Date startTime;

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
        if (r5 == 0) goto L_0x0086;
    L_0x0008:
        r2 = r4.getClass();
        r3 = r5.getClass();
        if (r2 != r3) goto L_0x0086;
    L_0x0012:
        r5 = (com.upsight.mediation.IAPOfferInfo) r5;
        r2 = r5.productPrice;
        r3 = r4.productPrice;
        r2 = java.lang.Float.compare(r2, r3);
        if (r2 != 0) goto L_0x0086;
    L_0x001e:
        r2 = r4.itemAmount;
        r3 = r5.itemAmount;
        if (r2 != r3) goto L_0x0086;
    L_0x0024:
        r2 = r4.itemName;
        if (r2 == 0) goto L_0x006d;
    L_0x0028:
        r2 = r4.itemName;
        r3 = r5.itemName;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0086;
    L_0x0032:
        r2 = r4.productID;
        if (r2 == 0) goto L_0x0072;
    L_0x0036:
        r2 = r4.productID;
        r3 = r5.productID;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0086;
    L_0x0040:
        r2 = r4.startTime;
        if (r2 == 0) goto L_0x0077;
    L_0x0044:
        r2 = r4.startTime;
        r3 = r5.startTime;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0086;
    L_0x004e:
        r2 = r4.endTime;
        if (r2 == 0) goto L_0x007c;
    L_0x0052:
        r2 = r4.endTime;
        r3 = r5.endTime;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0086;
    L_0x005c:
        r2 = r4.metadata;
        if (r2 == 0) goto L_0x0081;
    L_0x0060:
        r2 = r4.metadata;
        r3 = r5.metadata;
        r2 = r2.equals(r3);
        if (r2 != 0) goto L_0x006b;
    L_0x006a:
        r0 = r1;
    L_0x006b:
        r1 = r0;
        goto L_0x0005;
    L_0x006d:
        r2 = r5.itemName;
        if (r2 == 0) goto L_0x0032;
    L_0x0071:
        goto L_0x0005;
    L_0x0072:
        r2 = r5.productID;
        if (r2 == 0) goto L_0x0040;
    L_0x0076:
        goto L_0x0005;
    L_0x0077:
        r2 = r5.startTime;
        if (r2 == 0) goto L_0x004e;
    L_0x007b:
        goto L_0x0005;
    L_0x007c:
        r2 = r5.endTime;
        if (r2 == 0) goto L_0x005c;
    L_0x0080:
        goto L_0x0005;
    L_0x0081:
        r2 = r5.metadata;
        if (r2 != 0) goto L_0x006a;
    L_0x0085:
        goto L_0x006b;
    L_0x0086:
        r0 = r1;
        goto L_0x0004;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.upsight.mediation.IAPOfferInfo.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        int i = 0;
        int floatToIntBits = this.productPrice != 0.0f ? Float.floatToIntBits(this.productPrice) : 0;
        int i2 = this.itemAmount;
        int hashCode = this.itemName != null ? this.itemName.hashCode() : 0;
        int hashCode2 = this.productID != null ? this.productID.hashCode() : 0;
        int hashCode3 = this.startTime != null ? this.startTime.hashCode() : 0;
        int hashCode4 = this.endTime != null ? this.endTime.hashCode() : 0;
        if (this.metadata != null) {
            i = this.metadata.hashCode();
        }
        return (((((((((((floatToIntBits * 31) + i2) * 31) + hashCode) * 31) + hashCode2) * 31) + hashCode3) * 31) + hashCode4) * 31) + i;
    }

    public String toJSONString() {
        try {
            return new JSONStringer().object().key("IAPOfferInfo").object().key("productPrice").value((double) this.productPrice).key("itemAmount").value((long) this.itemAmount).key("itemName").value(this.itemName).key("productID").value(this.productID).key("startTime").value(this.startTime == null ? BuildConfig.FLAVOR : Long.valueOf(this.startTime.getTime())).key("endTime").value(this.endTime == null ? BuildConfig.FLAVOR : Long.valueOf(this.endTime.getTime())).key("metadata").value(this.metadata == null ? BuildConfig.FLAVOR : Base64.encodeToString(this.metadata.getBytes(), 2)).endObject().endObject().toString();
        } catch (JSONException e) {
            return "{ \"IAPOfferInfo\" : \"\" }";
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("IAPOfferInfo{");
        stringBuffer.append("productPrice=").append(this.productPrice);
        stringBuffer.append(", itemAmount=").append(this.itemAmount);
        stringBuffer.append(", itemName='").append(this.itemName).append('\'');
        stringBuffer.append(", productID='").append(this.productID).append('\'');
        stringBuffer.append(", startTime=").append(this.startTime);
        stringBuffer.append(", endTime=").append(this.endTime);
        stringBuffer.append(", metadata='").append(this.metadata).append('\'');
        stringBuffer.append('}');
        return stringBuffer.toString();
    }
}

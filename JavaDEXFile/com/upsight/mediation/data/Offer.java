package com.upsight.mediation.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.upsight.mediation.IAPOfferInfo;
import com.upsight.mediation.VGOfferInfo;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import com.upsight.mediation.util.HashMapCaster;
import com.voxelbusters.nativeplugins.defines.Keys;
import java.util.Date;
import java.util.HashMap;
import spacemadness.com.lunarconsole.C1518R;

public class Offer {
    public static final int IAP_OFFER = 51;
    public static final int VIRTUAL_GOODS_OFFER = 52;
    public final int adZoneID;
    public boolean consumed;
    @Nullable
    public final Integer contentId;
    public final Date endTime;
    public final int id;
    public final String itemId;
    public final String itemName;
    public final int itemQuantity;
    public final String metadata;
    public final String offerHtml;
    public final String purchaseCurrency;
    public final float purchasePrice;
    public final Date startTime;
    public final int f267t;
    public final OfferType type;
    public final int vg_currencyID;
    public final int vg_virtualGoodID;
    public final String zoneName;

    public @interface OfferCategory {
    }

    public enum OfferType {
        Discount(1),
        Standard(2),
        Bonus(3);
        
        public int value;

        private OfferType(int i) {
            this.value = i;
        }

        public static OfferType getType(int i) {
            switch (i) {
                case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    return Discount;
                case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
                    return Standard;
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                    return Bonus;
                default:
                    return null;
            }
        }
    }

    public Offer(int i, String str, @Nullable Integer num, int i2, int i3, int i4, String str2, int i5, String str3, int i6, float f, String str4, OfferType offerType, String str5, Date date, Date date2, String str6) {
        this.consumed = false;
        this.id = i;
        this.itemId = str;
        this.contentId = num;
        this.vg_virtualGoodID = i2;
        this.vg_currencyID = i3;
        this.adZoneID = i4;
        this.zoneName = str2;
        this.f267t = i5;
        this.itemName = str3;
        this.itemQuantity = i6;
        this.purchasePrice = f;
        this.purchaseCurrency = str4;
        this.type = offerType;
        this.offerHtml = str5;
        this.startTime = date;
        this.endTime = date2;
        this.metadata = str6;
    }

    public static Offer createFromValues(HashMap<String, String> hashMap) {
        HashMapCaster hashMapCaster = new HashMapCaster(hashMap);
        Integer valueOf = Integer.valueOf(hashMapCaster.getInt("content_id"));
        if (valueOf.intValue() == -1) {
            valueOf = null;
        }
        return new Offer(hashMapCaster.getInt("offer_id"), hashMapCaster.get("bundle_id"), valueOf, hashMapCaster.getInt("virtualgoodID"), hashMapCaster.getInt("currencyID"), hashMapCaster.getInt(TriggerIfContentAvailable.ID), hashMapCaster.get("zone_id"), hashMapCaster.getInt("t"), hashMapCaster.get("item"), hashMapCaster.getInt("itemQty", 0), hashMapCaster.getFloat("price", 0.0f), hashMapCaster.get("currency"), OfferType.getType(hashMapCaster.getInt(Keys.TYPE)), hashMapCaster.get("script"), hashMapCaster.getDate("start_date"), hashMapCaster.getDate("end_date"), hashMapCaster.get("metadata"));
    }

    public void consume() {
        this.consumed = true;
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
        if (r5 == 0) goto L_0x0105;
    L_0x0008:
        r2 = r4.getClass();
        r3 = r5.getClass();
        if (r2 != r3) goto L_0x0105;
    L_0x0012:
        r5 = (com.upsight.mediation.data.Offer) r5;
        r2 = r4.id;
        r3 = r5.id;
        if (r2 != r3) goto L_0x0105;
    L_0x001a:
        r2 = r4.contentId;
        if (r2 == 0) goto L_0x00d0;
    L_0x001e:
        r2 = r4.contentId;
        r3 = r5.contentId;
        r2 = java.util.Objects.equals(r2, r3);
        if (r2 == 0) goto L_0x0105;
    L_0x0028:
        r2 = r4.vg_virtualGoodID;
        r3 = r5.vg_virtualGoodID;
        if (r2 != r3) goto L_0x0105;
    L_0x002e:
        r2 = r4.vg_currencyID;
        r3 = r5.vg_currencyID;
        if (r2 != r3) goto L_0x0105;
    L_0x0034:
        r2 = r4.adZoneID;
        r3 = r5.adZoneID;
        if (r2 != r3) goto L_0x0105;
    L_0x003a:
        r2 = r4.f267t;
        r3 = r5.f267t;
        if (r2 != r3) goto L_0x0105;
    L_0x0040:
        r2 = r4.itemQuantity;
        r3 = r5.itemQuantity;
        if (r2 != r3) goto L_0x0105;
    L_0x0046:
        r2 = r5.purchasePrice;
        r3 = r4.purchasePrice;
        r2 = java.lang.Float.compare(r2, r3);
        if (r2 != 0) goto L_0x0105;
    L_0x0050:
        r2 = r4.consumed;
        r3 = r5.consumed;
        if (r2 != r3) goto L_0x0105;
    L_0x0056:
        r2 = r4.itemId;
        if (r2 == 0) goto L_0x00d6;
    L_0x005a:
        r2 = r4.itemId;
        r3 = r5.itemId;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0105;
    L_0x0064:
        r2 = r4.zoneName;
        if (r2 == 0) goto L_0x00dc;
    L_0x0068:
        r2 = r4.zoneName;
        r3 = r5.zoneName;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0105;
    L_0x0072:
        r2 = r4.itemName;
        if (r2 == 0) goto L_0x00e2;
    L_0x0076:
        r2 = r4.itemName;
        r3 = r5.itemName;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0105;
    L_0x0080:
        r2 = r4.purchaseCurrency;
        if (r2 == 0) goto L_0x00e8;
    L_0x0084:
        r2 = r4.purchaseCurrency;
        r3 = r5.purchaseCurrency;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0105;
    L_0x008e:
        r2 = r4.type;
        r3 = r5.type;
        if (r2 != r3) goto L_0x0105;
    L_0x0094:
        r2 = r4.offerHtml;
        if (r2 == 0) goto L_0x00ee;
    L_0x0098:
        r2 = r4.offerHtml;
        r3 = r5.offerHtml;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0105;
    L_0x00a2:
        r2 = r4.startTime;
        if (r2 == 0) goto L_0x00f4;
    L_0x00a6:
        r2 = r4.startTime;
        r3 = r5.startTime;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0105;
    L_0x00b0:
        r2 = r4.endTime;
        if (r2 == 0) goto L_0x00fa;
    L_0x00b4:
        r2 = r4.endTime;
        r3 = r5.endTime;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0105;
    L_0x00be:
        r2 = r4.metadata;
        if (r2 == 0) goto L_0x0100;
    L_0x00c2:
        r2 = r4.metadata;
        r3 = r5.metadata;
        r2 = r2.equals(r3);
        if (r2 != 0) goto L_0x00cd;
    L_0x00cc:
        r0 = r1;
    L_0x00cd:
        r1 = r0;
        goto L_0x0005;
    L_0x00d0:
        r2 = r5.contentId;
        if (r2 == 0) goto L_0x0028;
    L_0x00d4:
        goto L_0x0005;
    L_0x00d6:
        r2 = r5.itemId;
        if (r2 == 0) goto L_0x0064;
    L_0x00da:
        goto L_0x0005;
    L_0x00dc:
        r2 = r5.zoneName;
        if (r2 == 0) goto L_0x0072;
    L_0x00e0:
        goto L_0x0005;
    L_0x00e2:
        r2 = r5.itemName;
        if (r2 == 0) goto L_0x0080;
    L_0x00e6:
        goto L_0x0005;
    L_0x00e8:
        r2 = r5.purchaseCurrency;
        if (r2 == 0) goto L_0x008e;
    L_0x00ec:
        goto L_0x0005;
    L_0x00ee:
        r2 = r5.offerHtml;
        if (r2 == 0) goto L_0x00a2;
    L_0x00f2:
        goto L_0x0005;
    L_0x00f4:
        r2 = r5.startTime;
        if (r2 == 0) goto L_0x00b0;
    L_0x00f8:
        goto L_0x0005;
    L_0x00fa:
        r2 = r5.endTime;
        if (r2 == 0) goto L_0x00be;
    L_0x00fe:
        goto L_0x0005;
    L_0x0100:
        r2 = r5.metadata;
        if (r2 != 0) goto L_0x00cc;
    L_0x0104:
        goto L_0x00cd;
    L_0x0105:
        r0 = r1;
        goto L_0x0004;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.upsight.mediation.data.Offer.equals(java.lang.Object):boolean");
    }

    public Object getInfo() {
        if (this.f267t == IAP_OFFER) {
            IAPOfferInfo iAPOfferInfo = new IAPOfferInfo();
            iAPOfferInfo.itemAmount = this.itemQuantity;
            iAPOfferInfo.productPrice = this.purchasePrice;
            iAPOfferInfo.itemName = this.itemName;
            iAPOfferInfo.productID = this.itemId;
            iAPOfferInfo.startTime = this.startTime;
            iAPOfferInfo.endTime = this.endTime;
            iAPOfferInfo.metadata = this.metadata;
            return iAPOfferInfo;
        }
        VGOfferInfo vGOfferInfo = new VGOfferInfo();
        vGOfferInfo.purchaseCurrency = this.purchaseCurrency;
        vGOfferInfo.purchasePrice = this.purchasePrice;
        vGOfferInfo.itemName = this.itemName;
        vGOfferInfo.itemAmount = this.itemQuantity;
        vGOfferInfo.virtualGoodID = this.vg_virtualGoodID;
        vGOfferInfo.currencyID = this.vg_currencyID;
        vGOfferInfo.startTime = this.startTime;
        vGOfferInfo.endTime = this.endTime;
        vGOfferInfo.metadata = this.metadata;
        return vGOfferInfo;
    }

    public int hashCode() {
        int i = 0;
        int i2 = this.id;
        int hashCode = this.itemId != null ? this.itemId.hashCode() : 0;
        int hashCode2 = this.contentId != null ? this.contentId.hashCode() : 0;
        int i3 = this.vg_virtualGoodID;
        int i4 = this.vg_currencyID;
        int i5 = this.adZoneID;
        int hashCode3 = this.zoneName != null ? this.zoneName.hashCode() : 0;
        int i6 = this.f267t;
        int hashCode4 = this.itemName != null ? this.itemName.hashCode() : 0;
        int i7 = this.itemQuantity;
        int floatToIntBits = this.purchasePrice != 0.0f ? Float.floatToIntBits(this.purchasePrice) : 0;
        int hashCode5 = this.purchaseCurrency != null ? this.purchaseCurrency.hashCode() : 0;
        int hashCode6 = this.type != null ? this.type.hashCode() : 0;
        int hashCode7 = this.offerHtml != null ? this.offerHtml.hashCode() : 0;
        int hashCode8 = this.startTime != null ? this.startTime.hashCode() : 0;
        int hashCode9 = this.endTime != null ? this.endTime.hashCode() : 0;
        int hashCode10 = this.metadata != null ? this.metadata.hashCode() : 0;
        if (this.consumed) {
            i = 1;
        }
        return ((((((((((((((((((((((((((((((((hashCode + (i2 * 31)) * 31) + hashCode2) * 31) + i3) * 31) + i4) * 31) + i5) * 31) + hashCode3) * 31) + i6) * 31) + hashCode4) * 31) + i7) * 31) + floatToIntBits) * 31) + hashCode5) * 31) + hashCode6) * 31) + hashCode7) * 31) + hashCode8) * 31) + hashCode9) * 31) + hashCode10) * 31) + i;
    }

    public boolean isExpired(@NonNull Date date) {
        return this.endTime != null && this.endTime.before(date);
    }

    public String toString() {
        return "Offer{id=" + this.id + ", itemId='" + this.itemId + '\'' + ", contentId=" + this.contentId + ", vg_virtualGoodID=" + this.vg_virtualGoodID + ", vg_currencyID=" + this.vg_currencyID + ", adZoneID=" + this.adZoneID + ", zoneName='" + this.zoneName + '\'' + ", t=" + this.f267t + ", itemName='" + this.itemName + '\'' + ", itemQuantity=" + this.itemQuantity + ", purchasePrice=" + this.purchasePrice + ", purchaseCurrency='" + this.purchaseCurrency + '\'' + ", type=" + this.type + ", offerHtml='" + this.offerHtml + '\'' + ", startTime=" + this.startTime + ", endTime=" + this.endTime + ", metadata='" + this.metadata + '\'' + ", consumed=" + this.consumed + '}';
    }
}

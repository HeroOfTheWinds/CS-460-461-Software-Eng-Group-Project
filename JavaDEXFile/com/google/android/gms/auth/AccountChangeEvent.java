package com.google.android.gms.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import spacemadness.com.lunarconsole.C1518R;

public class AccountChangeEvent implements SafeParcelable {
    public static final Creator<AccountChangeEvent> CREATOR;
    final int mVersion;
    final long zzRr;
    final String zzRs;
    final int zzRt;
    final int zzRu;
    final String zzRv;

    static {
        CREATOR = new zza();
    }

    AccountChangeEvent(int i, long j, String str, int i2, int i3, String str2) {
        this.mVersion = i;
        this.zzRr = j;
        this.zzRs = (String) zzx.zzw(str);
        this.zzRt = i2;
        this.zzRu = i3;
        this.zzRv = str2;
    }

    public AccountChangeEvent(long j, String str, int i, int i2, String str2) {
        this.mVersion = 1;
        this.zzRr = j;
        this.zzRs = (String) zzx.zzw(str);
        this.zzRt = i;
        this.zzRu = i2;
        this.zzRv = str2;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj != this) {
            if (!(obj instanceof AccountChangeEvent)) {
                return false;
            }
            AccountChangeEvent accountChangeEvent = (AccountChangeEvent) obj;
            if (this.mVersion != accountChangeEvent.mVersion || this.zzRr != accountChangeEvent.zzRr || !zzw.equal(this.zzRs, accountChangeEvent.zzRs) || this.zzRt != accountChangeEvent.zzRt || this.zzRu != accountChangeEvent.zzRu) {
                return false;
            }
            if (!zzw.equal(this.zzRv, accountChangeEvent.zzRv)) {
                return false;
            }
        }
        return true;
    }

    public String getAccountName() {
        return this.zzRs;
    }

    public String getChangeData() {
        return this.zzRv;
    }

    public int getChangeType() {
        return this.zzRt;
    }

    public int getEventIndex() {
        return this.zzRu;
    }

    public int hashCode() {
        return zzw.hashCode(Integer.valueOf(this.mVersion), Long.valueOf(this.zzRr), this.zzRs, Integer.valueOf(this.zzRt), Integer.valueOf(this.zzRu), this.zzRv);
    }

    public String toString() {
        String str = "UNKNOWN";
        switch (this.zzRt) {
            case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                str = "ADDED";
                break;
            case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
                str = "REMOVED";
                break;
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                str = "RENAMED_FROM";
                break;
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_LEFT /*4*/:
                str = "RENAMED_TO";
                break;
        }
        return "AccountChangeEvent {accountName = " + this.zzRs + ", changeType = " + str + ", changeData = " + this.zzRv + ", eventIndex = " + this.zzRu + "}";
    }

    public void writeToParcel(Parcel parcel, int i) {
        zza.zza(this, parcel, i);
    }
}

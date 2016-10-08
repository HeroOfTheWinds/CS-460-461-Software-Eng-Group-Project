package com.google.android.gms.ads.internal.client;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.location.places.Place;
import com.mopub.volley.Request.Method;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import spacemadness.com.lunarconsole.C1518R;

public class zzi implements Creator<AdSizeParcel> {
    static void zza(AdSizeParcel adSizeParcel, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, adSizeParcel.versionCode);
        zzb.zza(parcel, 2, adSizeParcel.zzte, false);
        zzb.zzc(parcel, 3, adSizeParcel.height);
        zzb.zzc(parcel, 4, adSizeParcel.heightPixels);
        zzb.zza(parcel, 5, adSizeParcel.zztf);
        zzb.zzc(parcel, 6, adSizeParcel.width);
        zzb.zzc(parcel, 7, adSizeParcel.widthPixels);
        zzb.zza(parcel, 8, adSizeParcel.zztg, i, false);
        zzb.zza(parcel, 9, adSizeParcel.zzth);
        zzb.zza(parcel, 10, adSizeParcel.zzti);
        zzb.zzI(parcel, zzaq);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzc(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzl(i);
    }

    public AdSizeParcel zzc(Parcel parcel) {
        String str = null;
        boolean z = false;
        int zzap = zza.zzap(parcel);
        AdSizeParcel[] adSizeParcelArr = null;
        boolean z2 = false;
        boolean z3 = false;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (parcel.dataPosition() < zzap) {
            int zzao = zza.zzao(parcel);
            switch (zza.zzbM(zzao)) {
                case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    i = zza.zzg(parcel, zzao);
                    break;
                case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
                    str = zza.zzp(parcel, zzao);
                    break;
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                    i2 = zza.zzg(parcel, zzao);
                    break;
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_LEFT /*4*/:
                    i3 = zza.zzg(parcel, zzao);
                    break;
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                    z = zza.zzc(parcel, zzao);
                    break;
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_RIGHT /*6*/:
                    i4 = zza.zzg(parcel, zzao);
                    break;
                case Method.PATCH /*7*/:
                    i5 = zza.zzg(parcel, zzao);
                    break;
                case Place.TYPE_BANK /*8*/:
                    adSizeParcelArr = (AdSizeParcel[]) zza.zzb(parcel, zzao, AdSizeParcel.CREATOR);
                    break;
                case Place.TYPE_BAR /*9*/:
                    z2 = zza.zzc(parcel, zzao);
                    break;
                case Place.TYPE_BEAUTY_SALON /*10*/:
                    z3 = zza.zzc(parcel, zzao);
                    break;
                default:
                    zza.zzb(parcel, zzao);
                    break;
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new AdSizeParcel(i, str, i2, i3, z, i4, i5, adSizeParcelArr, z2, z3);
        }
        throw new zza.zza("Overread allowed size end=" + zzap, parcel);
    }

    public AdSizeParcel[] zzl(int i) {
        return new AdSizeParcel[i];
    }
}

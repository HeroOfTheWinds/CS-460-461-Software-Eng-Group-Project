package com.google.android.gms.auth.api.signin.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.auth.api.signin.EmailSignInConfig;
import com.google.android.gms.auth.api.signin.FacebookSignInConfig;
import com.google.android.gms.auth.api.signin.GoogleSignInConfig;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.mopub.volley.Request.Method;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import spacemadness.com.lunarconsole.C1518R;

public class zzh implements Creator<SignInConfiguration> {
    static void zza(SignInConfiguration signInConfiguration, Parcel parcel, int i) {
        int zzaq = zzb.zzaq(parcel);
        zzb.zzc(parcel, 1, signInConfiguration.versionCode);
        zzb.zza(parcel, 2, signInConfiguration.zzme(), false);
        zzb.zza(parcel, 3, signInConfiguration.zzmb(), false);
        zzb.zza(parcel, 4, signInConfiguration.zzmf(), i, false);
        zzb.zza(parcel, 5, signInConfiguration.zzmg(), i, false);
        zzb.zza(parcel, 6, signInConfiguration.zzmh(), i, false);
        zzb.zza(parcel, 7, signInConfiguration.zzmi(), false);
        zzb.zzI(parcel, zzaq);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzS(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzaJ(i);
    }

    public SignInConfiguration zzS(Parcel parcel) {
        String str = null;
        int zzap = zza.zzap(parcel);
        int i = 0;
        String str2 = null;
        EmailSignInConfig emailSignInConfig = null;
        GoogleSignInConfig googleSignInConfig = null;
        FacebookSignInConfig facebookSignInConfig = null;
        String str3 = null;
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
                    str2 = zza.zzp(parcel, zzao);
                    break;
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_LEFT /*4*/:
                    emailSignInConfig = (EmailSignInConfig) zza.zza(parcel, zzao, EmailSignInConfig.CREATOR);
                    break;
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                    googleSignInConfig = (GoogleSignInConfig) zza.zza(parcel, zzao, GoogleSignInConfig.CREATOR);
                    break;
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_RIGHT /*6*/:
                    facebookSignInConfig = (FacebookSignInConfig) zza.zza(parcel, zzao, FacebookSignInConfig.CREATOR);
                    break;
                case Method.PATCH /*7*/:
                    str3 = zza.zzp(parcel, zzao);
                    break;
                default:
                    zza.zzb(parcel, zzao);
                    break;
            }
        }
        if (parcel.dataPosition() == zzap) {
            return new SignInConfiguration(i, str, str2, emailSignInConfig, googleSignInConfig, facebookSignInConfig, str3);
        }
        throw new zza.zza("Overread allowed size end=" + zzap, parcel);
    }

    public SignInConfiguration[] zzaJ(int i) {
        return new SignInConfiguration[i];
    }
}

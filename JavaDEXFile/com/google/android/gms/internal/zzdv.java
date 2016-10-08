package com.google.android.gms.internal;

import android.text.TextUtils;
import com.google.android.gms.ads.internal.client.zzl;
import com.google.android.gms.ads.internal.util.client.zza;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.location.places.Place;
import com.mopub.volley.Request.Method;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import java.util.HashMap;
import java.util.Map;
import spacemadness.com.lunarconsole.C1518R;

@zzgr
public abstract class zzdv implements Releasable {
    protected zziz zzoM;

    /* renamed from: com.google.android.gms.internal.zzdv.1 */
    class C04851 implements Runnable {
        final /* synthetic */ String zzyc;
        final /* synthetic */ String zzyd;
        final /* synthetic */ int zzye;
        final /* synthetic */ int zzyf;
        final /* synthetic */ boolean zzyg;
        final /* synthetic */ zzdv zzyh;

        C04851(zzdv com_google_android_gms_internal_zzdv, String str, String str2, int i, int i2, boolean z) {
            this.zzyh = com_google_android_gms_internal_zzdv;
            this.zzyc = str;
            this.zzyd = str2;
            this.zzye = i;
            this.zzyf = i2;
            this.zzyg = z;
        }

        public void run() {
            Map hashMap = new HashMap();
            hashMap.put(SendEvent.EVENT, "precacheProgress");
            hashMap.put("src", this.zzyc);
            hashMap.put("cachedSrc", this.zzyd);
            hashMap.put("bytesLoaded", Integer.toString(this.zzye));
            hashMap.put("totalBytes", Integer.toString(this.zzyf));
            hashMap.put("cacheReady", this.zzyg ? "1" : "0");
            this.zzyh.zzoM.zzb("onPrecacheEvent", hashMap);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzdv.2 */
    class C04862 implements Runnable {
        final /* synthetic */ String zzyc;
        final /* synthetic */ String zzyd;
        final /* synthetic */ int zzyf;
        final /* synthetic */ zzdv zzyh;

        C04862(zzdv com_google_android_gms_internal_zzdv, String str, String str2, int i) {
            this.zzyh = com_google_android_gms_internal_zzdv;
            this.zzyc = str;
            this.zzyd = str2;
            this.zzyf = i;
        }

        public void run() {
            Map hashMap = new HashMap();
            hashMap.put(SendEvent.EVENT, "precacheComplete");
            hashMap.put("src", this.zzyc);
            hashMap.put("cachedSrc", this.zzyd);
            hashMap.put("totalBytes", Integer.toString(this.zzyf));
            this.zzyh.zzoM.zzb("onPrecacheEvent", hashMap);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzdv.3 */
    class C04873 implements Runnable {
        final /* synthetic */ String zzyc;
        final /* synthetic */ String zzyd;
        final /* synthetic */ zzdv zzyh;
        final /* synthetic */ String zzyi;
        final /* synthetic */ String zzyj;

        C04873(zzdv com_google_android_gms_internal_zzdv, String str, String str2, String str3, String str4) {
            this.zzyh = com_google_android_gms_internal_zzdv;
            this.zzyc = str;
            this.zzyd = str2;
            this.zzyi = str3;
            this.zzyj = str4;
        }

        public void run() {
            Map hashMap = new HashMap();
            hashMap.put(SendEvent.EVENT, "precacheCanceled");
            hashMap.put("src", this.zzyc);
            if (!TextUtils.isEmpty(this.zzyd)) {
                hashMap.put("cachedSrc", this.zzyd);
            }
            hashMap.put(Keys.TYPE, this.zzyh.zzad(this.zzyi));
            hashMap.put("reason", this.zzyi);
            if (!TextUtils.isEmpty(this.zzyj)) {
                hashMap.put(Keys.MESSAGE, this.zzyj);
            }
            this.zzyh.zzoM.zzb("onPrecacheEvent", hashMap);
        }
    }

    public zzdv(zziz com_google_android_gms_internal_zziz) {
        this.zzoM = com_google_android_gms_internal_zziz;
    }

    private String zzad(String str) {
        Object obj = -1;
        switch (str.hashCode()) {
            case -1396664534:
                if (str.equals("badUrl")) {
                    obj = 6;
                    break;
                }
                break;
            case -1347010958:
                if (str.equals("inProgress")) {
                    obj = 2;
                    break;
                }
                break;
            case -918817863:
                if (str.equals("downloadTimeout")) {
                    obj = 7;
                    break;
                }
                break;
            case -659376217:
                if (str.equals("contentLengthMissing")) {
                    obj = 3;
                    break;
                }
                break;
            case -642208130:
                if (str.equals("playerFailed")) {
                    obj = 1;
                    break;
                }
                break;
            case -354048396:
                if (str.equals("sizeExceeded")) {
                    obj = 8;
                    break;
                }
                break;
            case -32082395:
                if (str.equals("externalAbort")) {
                    obj = 9;
                    break;
                }
                break;
            case 96784904:
                if (str.equals(GameServices.ERROR)) {
                    obj = null;
                    break;
                }
                break;
            case 580119100:
                if (str.equals("expireFailed")) {
                    obj = 5;
                    break;
                }
                break;
            case 725497484:
                if (str.equals("noCacheDir")) {
                    obj = 4;
                    break;
                }
                break;
        }
        switch (obj) {
            case C1518R.styleable.AdsAttrs_adSize /*0*/:
            case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
            case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                return "internal";
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_LEFT /*4*/:
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                return "io";
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_RIGHT /*6*/:
            case Method.PATCH /*7*/:
                return "network";
            case Place.TYPE_BANK /*8*/:
            case Place.TYPE_BAR /*9*/:
                return "policy";
            default:
                return "internal";
        }
    }

    public abstract void abort();

    public void release() {
    }

    protected void zza(String str, String str2, int i) {
        zza.zzJt.post(new C04862(this, str, str2, i));
    }

    protected void zza(String str, String str2, int i, int i2, boolean z) {
        zza.zzJt.post(new C04851(this, str, str2, i, i2, z));
    }

    protected void zza(String str, String str2, String str3, String str4) {
        zza.zzJt.post(new C04873(this, str, str2, str3, str4));
    }

    public abstract boolean zzab(String str);

    protected String zzac(String str) {
        return zzl.zzcF().zzaE(str);
    }
}

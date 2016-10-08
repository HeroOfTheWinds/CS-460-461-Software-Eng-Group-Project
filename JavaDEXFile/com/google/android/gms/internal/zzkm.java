package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.auth.api.proxy.ProxyApi;
import com.google.android.gms.auth.api.proxy.ProxyApi.ProxyResult;
import com.google.android.gms.auth.api.proxy.ProxyRequest;
import com.google.android.gms.auth.api.proxy.ProxyResponse;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.internal.zzx;

public class zzkm implements ProxyApi {

    /* renamed from: com.google.android.gms.internal.zzkm.1 */
    class C05831 extends zzkl {
        final /* synthetic */ ProxyRequest zzSQ;
        final /* synthetic */ zzkm zzSR;

        /* renamed from: com.google.android.gms.internal.zzkm.1.1 */
        class C05821 extends zzkh {
            final /* synthetic */ C05831 zzSS;

            C05821(C05831 c05831) {
                this.zzSS = c05831;
            }

            public void zza(ProxyResponse proxyResponse) {
                this.zzSS.zzb(new zzkn(proxyResponse));
            }
        }

        C05831(zzkm com_google_android_gms_internal_zzkm, GoogleApiClient googleApiClient, ProxyRequest proxyRequest) {
            this.zzSR = com_google_android_gms_internal_zzkm;
            this.zzSQ = proxyRequest;
            super(googleApiClient);
        }

        protected void zza(Context context, zzkk com_google_android_gms_internal_zzkk) throws RemoteException {
            com_google_android_gms_internal_zzkk.zza(new C05821(this), this.zzSQ);
        }
    }

    public PendingResult<ProxyResult> performProxyRequest(GoogleApiClient googleApiClient, ProxyRequest proxyRequest) {
        zzx.zzw(googleApiClient);
        zzx.zzw(proxyRequest);
        return googleApiClient.zzb(new C05831(this, googleApiClient, proxyRequest));
    }
}

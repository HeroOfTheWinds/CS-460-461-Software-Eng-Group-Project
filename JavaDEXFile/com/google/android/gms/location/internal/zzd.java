package com.google.android.gms.location.internal;

import android.app.PendingIntent;
import android.location.Location;
import android.os.Looper;
import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class zzd implements FusedLocationProviderApi {

    private static abstract class zza extends com.google.android.gms.location.LocationServices.zza<Status> {
        public zza(GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }

        public /* synthetic */ Result zzb(Status status) {
            return zzd(status);
        }

        public Status zzd(Status status) {
            return status;
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.1 */
    class C06231 extends zza {
        final /* synthetic */ LocationRequest zzaFd;
        final /* synthetic */ LocationListener zzaFe;
        final /* synthetic */ zzd zzaFf;

        /* renamed from: com.google.android.gms.location.internal.zzd.1.1 */
        class C06221 extends com.google.android.gms.location.internal.zzg.zza {
            final /* synthetic */ C06231 zzaFg;

            C06221(C06231 c06231) {
                this.zzaFg = c06231;
            }

            public void zza(FusedLocationProviderResult fusedLocationProviderResult) {
                this.zzaFg.zzb(fusedLocationProviderResult.getStatus());
            }
        }

        C06231(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationListener locationListener) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFd = locationRequest;
            this.zzaFe = locationListener;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaFd, this.zzaFe, null, new C06221(this));
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.2 */
    class C06252 extends zza {
        final /* synthetic */ zzd zzaFf;
        final /* synthetic */ LocationCallback zzaFh;

        /* renamed from: com.google.android.gms.location.internal.zzd.2.1 */
        class C06241 extends com.google.android.gms.location.internal.zzg.zza {
            final /* synthetic */ C06252 zzaFi;

            C06241(C06252 c06252) {
                this.zzaFi = c06252;
            }

            public void zza(FusedLocationProviderResult fusedLocationProviderResult) {
                this.zzaFi.zzb(fusedLocationProviderResult.getStatus());
            }
        }

        C06252(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, LocationCallback locationCallback) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFh = locationCallback;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaFh, new C06241(this));
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.3 */
    class C06263 extends zza {
        final /* synthetic */ zzd zzaFf;
        final /* synthetic */ boolean zzaFj;

        C06263(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, boolean z) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFj = z;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zzah(this.zzaFj);
            zzb(Status.zzabb);
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.4 */
    class C06274 extends zza {
        final /* synthetic */ zzd zzaFf;
        final /* synthetic */ Location zzaFk;

        C06274(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, Location location) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFk = location;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zzc(this.zzaFk);
            zzb(Status.zzabb);
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.5 */
    class C06295 extends zza {
        final /* synthetic */ LocationRequest zzaFd;
        final /* synthetic */ LocationListener zzaFe;
        final /* synthetic */ zzd zzaFf;
        final /* synthetic */ Looper zzaFl;

        /* renamed from: com.google.android.gms.location.internal.zzd.5.1 */
        class C06281 extends com.google.android.gms.location.internal.zzg.zza {
            final /* synthetic */ C06295 zzaFm;

            C06281(C06295 c06295) {
                this.zzaFm = c06295;
            }

            public void zza(FusedLocationProviderResult fusedLocationProviderResult) {
                this.zzaFm.zzb(fusedLocationProviderResult.getStatus());
            }
        }

        C06295(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationListener locationListener, Looper looper) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFd = locationRequest;
            this.zzaFe = locationListener;
            this.zzaFl = looper;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaFd, this.zzaFe, this.zzaFl, new C06281(this));
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.6 */
    class C06316 extends zza {
        final /* synthetic */ LocationRequest zzaFd;
        final /* synthetic */ zzd zzaFf;
        final /* synthetic */ LocationCallback zzaFh;
        final /* synthetic */ Looper zzaFl;

        /* renamed from: com.google.android.gms.location.internal.zzd.6.1 */
        class C06301 extends com.google.android.gms.location.internal.zzg.zza {
            final /* synthetic */ C06316 zzaFn;

            C06301(C06316 c06316) {
                this.zzaFn = c06316;
            }

            public void zza(FusedLocationProviderResult fusedLocationProviderResult) {
                this.zzaFn.zzb(fusedLocationProviderResult.getStatus());
            }
        }

        C06316(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationCallback locationCallback, Looper looper) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFd = locationRequest;
            this.zzaFh = locationCallback;
            this.zzaFl = looper;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(LocationRequestInternal.zzb(this.zzaFd), this.zzaFh, this.zzaFl, new C06301(this));
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.7 */
    class C06337 extends zza {
        final /* synthetic */ PendingIntent zzaEY;
        final /* synthetic */ LocationRequest zzaFd;
        final /* synthetic */ zzd zzaFf;

        /* renamed from: com.google.android.gms.location.internal.zzd.7.1 */
        class C06321 extends com.google.android.gms.location.internal.zzg.zza {
            final /* synthetic */ C06337 zzaFo;

            C06321(C06337 c06337) {
                this.zzaFo = c06337;
            }

            public void zza(FusedLocationProviderResult fusedLocationProviderResult) {
                this.zzaFo.zzb(fusedLocationProviderResult.getStatus());
            }
        }

        C06337(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, LocationRequest locationRequest, PendingIntent pendingIntent) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFd = locationRequest;
            this.zzaEY = pendingIntent;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaFd, this.zzaEY, new C06321(this));
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.8 */
    class C06358 extends zza {
        final /* synthetic */ LocationListener zzaFe;
        final /* synthetic */ zzd zzaFf;

        /* renamed from: com.google.android.gms.location.internal.zzd.8.1 */
        class C06341 extends com.google.android.gms.location.internal.zzg.zza {
            final /* synthetic */ C06358 zzaFp;

            C06341(C06358 c06358) {
                this.zzaFp = c06358;
            }

            public void zza(FusedLocationProviderResult fusedLocationProviderResult) {
                this.zzaFp.zzb(fusedLocationProviderResult.getStatus());
            }
        }

        C06358(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, LocationListener locationListener) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaFe = locationListener;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaFe, new C06341(this));
        }
    }

    /* renamed from: com.google.android.gms.location.internal.zzd.9 */
    class C06379 extends zza {
        final /* synthetic */ PendingIntent zzaEY;
        final /* synthetic */ zzd zzaFf;

        /* renamed from: com.google.android.gms.location.internal.zzd.9.1 */
        class C06361 extends com.google.android.gms.location.internal.zzg.zza {
            final /* synthetic */ C06379 zzaFq;

            C06361(C06379 c06379) {
                this.zzaFq = c06379;
            }

            public void zza(FusedLocationProviderResult fusedLocationProviderResult) {
                this.zzaFq.zzb(fusedLocationProviderResult.getStatus());
            }
        }

        C06379(zzd com_google_android_gms_location_internal_zzd, GoogleApiClient googleApiClient, PendingIntent pendingIntent) {
            this.zzaFf = com_google_android_gms_location_internal_zzd;
            this.zzaEY = pendingIntent;
            super(googleApiClient);
        }

        protected void zza(zzl com_google_android_gms_location_internal_zzl) throws RemoteException {
            com_google_android_gms_location_internal_zzl.zza(this.zzaEY, new C06361(this));
        }
    }

    public Location getLastLocation(GoogleApiClient googleApiClient) {
        try {
            return LocationServices.zzd(googleApiClient).getLastLocation();
        } catch (Exception e) {
            return null;
        }
    }

    public LocationAvailability getLocationAvailability(GoogleApiClient googleApiClient) {
        try {
            return LocationServices.zzd(googleApiClient).zzwD();
        } catch (Exception e) {
            return null;
        }
    }

    public PendingResult<Status> removeLocationUpdates(GoogleApiClient googleApiClient, PendingIntent pendingIntent) {
        return googleApiClient.zzb(new C06379(this, googleApiClient, pendingIntent));
    }

    public PendingResult<Status> removeLocationUpdates(GoogleApiClient googleApiClient, LocationCallback locationCallback) {
        return googleApiClient.zzb(new C06252(this, googleApiClient, locationCallback));
    }

    public PendingResult<Status> removeLocationUpdates(GoogleApiClient googleApiClient, LocationListener locationListener) {
        return googleApiClient.zzb(new C06358(this, googleApiClient, locationListener));
    }

    public PendingResult<Status> requestLocationUpdates(GoogleApiClient googleApiClient, LocationRequest locationRequest, PendingIntent pendingIntent) {
        return googleApiClient.zzb(new C06337(this, googleApiClient, locationRequest, pendingIntent));
    }

    public PendingResult<Status> requestLocationUpdates(GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationCallback locationCallback, Looper looper) {
        return googleApiClient.zzb(new C06316(this, googleApiClient, locationRequest, locationCallback, looper));
    }

    public PendingResult<Status> requestLocationUpdates(GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationListener locationListener) {
        return googleApiClient.zzb(new C06231(this, googleApiClient, locationRequest, locationListener));
    }

    public PendingResult<Status> requestLocationUpdates(GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationListener locationListener, Looper looper) {
        return googleApiClient.zzb(new C06295(this, googleApiClient, locationRequest, locationListener, looper));
    }

    public PendingResult<Status> setMockLocation(GoogleApiClient googleApiClient, Location location) {
        return googleApiClient.zzb(new C06274(this, googleApiClient, location));
    }

    public PendingResult<Status> setMockMode(GoogleApiClient googleApiClient, boolean z) {
        return googleApiClient.zzb(new C06263(this, googleApiClient, z));
    }
}

package com.nianticlabs.nia.location;

import android.content.Context;
import android.location.Location;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.nianticlabs.nia.contextservice.ContextService;
import com.nianticlabs.nia.contextservice.GoogleApiManager;
import com.nianticlabs.nia.contextservice.GoogleApiManager.Listener;
import com.nianticlabs.nia.contextservice.ServiceStatus;
import com.nianticlabs.nia.location.Provider.ProviderListener;

public class FusedLocationProvider implements Provider {
    private static final boolean ENABLE_VERBOSE_LOGS = false;
    private static final String TAG = "FusedLocationProvider";
    private AppState appState;
    private LocationCallback fusedListener;
    Listener googleApiListener;
    private final GoogleApiManager googleApiManager;
    private GoogleApiState googleApiState;
    private LocationRequest locationRequest;
    private ProviderListener providerListener;

    /* renamed from: com.nianticlabs.nia.location.FusedLocationProvider.1 */
    class C07671 implements Listener {
        C07671() {
        }

        public void onConnected() {
            FusedLocationProvider.this.googleApiState = GoogleApiState.STARTED;
            FusedLocationProvider.this.updateStatus(ServiceStatus.INITIALIZED);
            if (FusedLocationProvider.this.appState == AppState.RESUME) {
                FusedLocationProvider.this.startProvider();
            }
        }

        public void onConnectionFailed(ConnectionResult connectionResult) {
            FusedLocationProvider.this.googleApiState = GoogleApiState.STOPPED;
            if (connectionResult != null) {
                switch (connectionResult.getErrorCode()) {
                    case Place.TYPE_CAR_REPAIR /*19*/:
                        FusedLocationProvider.this.updateStatus(ServiceStatus.PERMISSION_DENIED);
                        return;
                    default:
                        FusedLocationProvider.this.updateStatus(ServiceStatus.FAILED);
                        return;
                }
            }
            FusedLocationProvider.this.updateStatus(ServiceStatus.FAILED);
        }

        public void onDisconnected() {
            FusedLocationProvider.this.googleApiState = GoogleApiState.STOPPED;
        }
    }

    /* renamed from: com.nianticlabs.nia.location.FusedLocationProvider.2 */
    class C07682 implements ResultCallback<Status> {
        C07682() {
        }

        public void onResult(Status status) {
            if (status.isSuccess()) {
                FusedLocationProvider.this.updateStatus(ServiceStatus.RUNNING);
            } else {
                FusedLocationProvider.this.updateStatus(ServiceStatus.FAILED);
            }
        }
    }

    /* renamed from: com.nianticlabs.nia.location.FusedLocationProvider.3 */
    class C07693 implements ResultCallback<Status> {
        C07693() {
        }

        public void onResult(Status status) {
            if (status.isSuccess()) {
                FusedLocationProvider.this.updateStatus(ServiceStatus.STOPPED);
            }
        }
    }

    /* renamed from: com.nianticlabs.nia.location.FusedLocationProvider.4 */
    class C07704 extends LocationCallback {
        C07704() {
        }

        public void onLocationAvailability(LocationAvailability locationAvailability) {
            if (locationAvailability.isLocationAvailable()) {
                FusedLocationProvider.this.updateStatus(ServiceStatus.RUNNING);
            } else {
                FusedLocationProvider.this.updateStatus(ServiceStatus.PERMISSION_DENIED);
            }
        }

        public void onLocationResult(LocationResult locationResult) {
            Location lastLocation = locationResult.getLastLocation();
            boolean isLocationAvailable = LocationServices.FusedLocationApi.getLocationAvailability(FusedLocationProvider.this.googleApiManager.getClient()).isLocationAvailable();
            if (lastLocation != null && isLocationAvailable) {
                ProviderListener access$500 = FusedLocationProvider.this.providerListener;
                if (access$500 != null) {
                    access$500.onProviderLocation(lastLocation);
                }
            }
        }
    }

    private enum AppState {
        START,
        STOP,
        PAUSE,
        RESUME
    }

    private enum GoogleApiState {
        STARTED,
        STOPPED
    }

    public FusedLocationProvider(Context context, int i, float f) {
        this.googleApiState = GoogleApiState.STOPPED;
        this.appState = AppState.STOP;
        this.locationRequest = new LocationRequest();
        this.providerListener = null;
        this.googleApiListener = new C07671();
        this.fusedListener = new C07704();
        this.locationRequest.setInterval((long) i);
        this.locationRequest.setFastestInterval((long) i);
        this.locationRequest.setPriority(100);
        this.locationRequest.setSmallestDisplacement(f);
        this.googleApiManager = new GoogleApiManager(context);
        this.googleApiManager.setListener(this.googleApiListener);
        this.googleApiManager.builder().addApi(LocationServices.API);
        this.googleApiManager.build();
    }

    private void startProvider() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(this.googleApiManager.getClient(), this.locationRequest, this.fusedListener, ContextService.getServiceLooper()).setResultCallback(new C07682());
        } catch (SecurityException e) {
            updateStatus(ServiceStatus.PERMISSION_DENIED);
        }
    }

    private void stopProvider() {
        LocationServices.FusedLocationApi.removeLocationUpdates(this.googleApiManager.getClient(), this.fusedListener).setResultCallback(new C07693());
    }

    private void updateStatus(ServiceStatus serviceStatus) {
        ProviderListener providerListener = this.providerListener;
        if (providerListener != null) {
            providerListener.onProviderStatus(serviceStatus);
        }
    }

    public void onPause() {
        this.appState = AppState.PAUSE;
        if (this.googleApiState == GoogleApiState.STARTED) {
            stopProvider();
        }
        this.googleApiManager.onPause();
    }

    public void onResume() {
        this.appState = AppState.RESUME;
        if (this.googleApiState == GoogleApiState.STARTED) {
            startProvider();
        }
        this.googleApiManager.onResume();
    }

    public void onStart() {
        this.appState = AppState.START;
        this.googleApiManager.onStart();
    }

    public void onStop() {
        this.appState = AppState.STOP;
        this.googleApiManager.onStop();
    }

    public void setListener(ProviderListener providerListener) {
        this.providerListener = providerListener;
    }
}

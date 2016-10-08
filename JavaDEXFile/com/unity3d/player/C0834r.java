package com.unity3d.player;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import java.util.List;

/* renamed from: com.unity3d.player.r */
final class C0834r implements LocationListener {
    private final Context f204a;
    private final UnityPlayer f205b;
    private Location f206c;
    private float f207d;
    private boolean f208e;
    private int f209f;
    private boolean f210g;
    private int f211h;

    protected C0834r(Context context, UnityPlayer unityPlayer) {
        this.f207d = 0.0f;
        this.f208e = false;
        this.f209f = 0;
        this.f210g = false;
        this.f211h = 0;
        this.f204a = context;
        this.f205b = unityPlayer;
    }

    private void m164a(int i) {
        this.f211h = i;
        this.f205b.nativeSetLocationStatus(i);
    }

    private void m165a(Location location) {
        if (location != null && C0834r.m166a(location, this.f206c)) {
            this.f206c = location;
            this.f205b.nativeSetLocation((float) location.getLatitude(), (float) location.getLongitude(), (float) location.getAltitude(), location.getAccuracy(), ((double) location.getTime()) / 1000.0d, new GeomagneticField((float) this.f206c.getLatitude(), (float) this.f206c.getLongitude(), (float) this.f206c.getAltitude(), this.f206c.getTime()).getDeclination());
        }
    }

    private static boolean m166a(Location location, Location location2) {
        if (location2 != null) {
            long time = location.getTime() - location2.getTime();
            boolean z = time > 120000;
            boolean z2 = time < -120000;
            boolean z3 = time > 0;
            if (!z) {
                if (z2) {
                    return false;
                }
                int accuracy = (int) (location.getAccuracy() - location2.getAccuracy());
                boolean z4 = accuracy > 0;
                boolean z5 = accuracy < 0;
                accuracy = accuracy > 200 ? 1 : 0;
                int i = location.getAccuracy() == 0.0f ? 1 : 0;
                boolean a = C0834r.m167a(location.getProvider(), location2.getProvider());
                if (!z5 && (!z3 || z4)) {
                    if (!z3 || (accuracy | i) != 0) {
                        return false;
                    }
                    if (!a) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static boolean m167a(String str, String str2) {
        return str == null ? str2 == null : str.equals(str2);
    }

    public final void m168a(float f) {
        this.f207d = f;
    }

    public final boolean m169a() {
        return !((LocationManager) this.f204a.getSystemService("location")).getProviders(new Criteria(), true).isEmpty();
    }

    public final void m170b() {
        this.f210g = false;
        if (this.f208e) {
            C0827m.Log(5, "Location_StartUpdatingLocation already started!");
        } else if (m169a()) {
            LocationManager locationManager = (LocationManager) this.f204a.getSystemService("location");
            m164a(1);
            List<String> providers = locationManager.getProviders(true);
            if (providers.isEmpty()) {
                m164a(3);
                return;
            }
            LocationProvider locationProvider;
            if (this.f209f == 2) {
                for (String provider : providers) {
                    LocationProvider provider2 = locationManager.getProvider(provider);
                    if (provider2.getAccuracy() == 2) {
                        locationProvider = provider2;
                        break;
                    }
                }
            }
            locationProvider = null;
            for (String provider3 : providers) {
                if (locationProvider == null || locationManager.getProvider(provider3).getAccuracy() != 1) {
                    m165a(locationManager.getLastKnownLocation(provider3));
                    locationManager.requestLocationUpdates(provider3, 0, this.f207d, this, this.f204a.getMainLooper());
                    this.f208e = true;
                }
            }
        } else {
            m164a(3);
        }
    }

    public final void m171b(float f) {
        if (f < 100.0f) {
            this.f209f = 1;
        } else if (f < 500.0f) {
            this.f209f = 1;
        } else {
            this.f209f = 2;
        }
    }

    public final void m172c() {
        ((LocationManager) this.f204a.getSystemService("location")).removeUpdates(this);
        this.f208e = false;
        this.f206c = null;
        m164a(0);
    }

    public final void m173d() {
        if (this.f211h == 1 || this.f211h == 2) {
            this.f210g = true;
            m172c();
        }
    }

    public final void m174e() {
        if (this.f210g) {
            m170b();
        }
    }

    public final void onLocationChanged(Location location) {
        m164a(2);
        m165a(location);
    }

    public final void onProviderDisabled(String str) {
        this.f206c = null;
    }

    public final void onProviderEnabled(String str) {
    }

    public final void onStatusChanged(String str, int i, Bundle bundle) {
    }
}

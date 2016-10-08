package crittercism.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.os.EnvironmentCompat;
import com.google.android.gms.location.places.Place;
import com.mopub.volley.Request.Method;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import spacemadness.com.lunarconsole.C1518R;

/* renamed from: crittercism.android.d */
public final class C1116d {
    private ConnectivityManager f691a;

    public C1116d(Context context) {
        if (context == null) {
            dx.m779b("Given a null Context.");
        } else if (context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", context.getPackageName()) == 0) {
            this.f691a = (ConnectivityManager) context.getSystemService("connectivity");
        } else {
            dx.m779b("Add android.permission.ACCESS_NETWORK_STATE to AndroidManifest.xml to get more detailed OPTMZ data");
        }
    }

    public final C1066b m717a() {
        if (this.f691a == null) {
            return C1066b.UNKNOWN;
        }
        NetworkInfo activeNetworkInfo = this.f691a.getActiveNetworkInfo();
        return (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) ? C1066b.NOT_CONNECTED : C1066b.m451a(activeNetworkInfo.getType());
    }

    public final String m718b() {
        if (this.f691a == null) {
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        NetworkInfo activeNetworkInfo = this.f691a.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return "disconnected";
        }
        int type = activeNetworkInfo.getType();
        if (type == 0) {
            switch (activeNetworkInfo.getSubtype()) {
                case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_LEFT /*4*/:
                case Method.PATCH /*7*/:
                case Place.TYPE_BICYCLE_STORE /*11*/:
                    return "2G";
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_RIGHT /*6*/:
                case Place.TYPE_BANK /*8*/:
                case Place.TYPE_BAR /*9*/:
                case Place.TYPE_BEAUTY_SALON /*10*/:
                case Place.TYPE_BOOK_STORE /*12*/:
                case Place.TYPE_BUS_STATION /*14*/:
                case Place.TYPE_CAFE /*15*/:
                    return "3G";
                case Place.TYPE_BOWLING_ALLEY /*13*/:
                    return "LTE";
            }
        } else if (type == 1) {
            return "wifi";
        }
        return EnvironmentCompat.MEDIA_UNKNOWN;
    }
}

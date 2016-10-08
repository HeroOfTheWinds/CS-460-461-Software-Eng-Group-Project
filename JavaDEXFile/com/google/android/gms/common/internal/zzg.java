package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import com.google.android.gms.C0148R;
import com.google.android.gms.internal.zzmq;
import com.google.android.gms.location.places.Place;
import com.mopub.volley.Request.Method;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import spacemadness.com.lunarconsole.C1518R;

public final class zzg {
    public static String zzc(Context context, int i, String str) {
        Resources resources = context.getResources();
        switch (i) {
            case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                if (zzmq.zzb(resources)) {
                    return resources.getString(C0148R.string.common_google_play_services_install_text_tablet, new Object[]{str});
                }
                return resources.getString(C0148R.string.common_google_play_services_install_text_phone, new Object[]{str});
            case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
                return resources.getString(C0148R.string.common_google_play_services_update_text, new Object[]{str});
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                return resources.getString(C0148R.string.common_google_play_services_enable_text, new Object[]{str});
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                return resources.getString(C0148R.string.common_google_play_services_invalid_account_text);
            case Method.PATCH /*7*/:
                return resources.getString(C0148R.string.common_google_play_services_network_error_text);
            case Place.TYPE_BAR /*9*/:
                return resources.getString(C0148R.string.common_google_play_services_unsupported_text, new Object[]{str});
            case Place.TYPE_CAMPGROUND /*16*/:
                return resources.getString(C0148R.string.common_google_play_services_api_unavailable_text, new Object[]{str});
            case Place.TYPE_CAR_DEALER /*17*/:
                return resources.getString(C0148R.string.common_google_play_services_sign_in_failed_text);
            case Place.TYPE_CAR_RENTAL /*18*/:
                return resources.getString(C0148R.string.common_google_play_services_updating_text, new Object[]{str});
            case Place.TYPE_GENERAL_CONTRACTOR /*42*/:
                return resources.getString(C0148R.string.common_android_wear_update_text, new Object[]{str});
            default:
                return resources.getString(C0148R.string.common_google_play_services_unknown_issue);
        }
    }

    public static String zzd(Context context, int i, String str) {
        Resources resources = context.getResources();
        switch (i) {
            case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                if (zzmq.zzb(resources)) {
                    return resources.getString(C0148R.string.common_google_play_services_install_text_tablet, new Object[]{str});
                }
                return resources.getString(C0148R.string.common_google_play_services_install_text_phone, new Object[]{str});
            case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
                return resources.getString(C0148R.string.common_google_play_services_update_text, new Object[]{str});
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                return resources.getString(C0148R.string.common_google_play_services_enable_text, new Object[]{str});
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                return resources.getString(C0148R.string.common_google_play_services_invalid_account_text);
            case Method.PATCH /*7*/:
                return resources.getString(C0148R.string.common_google_play_services_network_error_text);
            case Place.TYPE_BAR /*9*/:
                return resources.getString(C0148R.string.common_google_play_services_unsupported_text, new Object[]{str});
            case Place.TYPE_CAMPGROUND /*16*/:
                return resources.getString(C0148R.string.common_google_play_services_api_unavailable_text, new Object[]{str});
            case Place.TYPE_CAR_DEALER /*17*/:
                return resources.getString(C0148R.string.common_google_play_services_sign_in_failed_text);
            case Place.TYPE_CAR_RENTAL /*18*/:
                return resources.getString(C0148R.string.common_google_play_services_updating_text, new Object[]{str});
            case Place.TYPE_GENERAL_CONTRACTOR /*42*/:
                return resources.getString(C0148R.string.common_android_wear_notification_needs_update_text, new Object[]{str});
            default:
                return resources.getString(C0148R.string.common_google_play_services_unknown_issue);
        }
    }

    public static final String zzg(Context context, int i) {
        Resources resources = context.getResources();
        switch (i) {
            case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                return resources.getString(C0148R.string.common_google_play_services_install_title);
            case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
                return resources.getString(C0148R.string.common_google_play_services_update_title);
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                return resources.getString(C0148R.string.common_google_play_services_enable_title);
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_LEFT /*4*/:
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_RIGHT /*6*/:
                return null;
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                Log.e("GoogleApiAvailability", "An invalid account was specified when connecting. Please provide a valid account.");
                return resources.getString(C0148R.string.common_google_play_services_invalid_account_title);
            case Method.PATCH /*7*/:
                Log.e("GoogleApiAvailability", "Network error occurred. Please retry request later.");
                return resources.getString(C0148R.string.common_google_play_services_network_error_title);
            case Place.TYPE_BANK /*8*/:
                Log.e("GoogleApiAvailability", "Internal error occurred. Please see logs for detailed information");
                return null;
            case Place.TYPE_BAR /*9*/:
                Log.e("GoogleApiAvailability", "Google Play services is invalid. Cannot recover.");
                return resources.getString(C0148R.string.common_google_play_services_unsupported_title);
            case Place.TYPE_BEAUTY_SALON /*10*/:
                Log.e("GoogleApiAvailability", "Developer error occurred. Please see logs for detailed information");
                return null;
            case Place.TYPE_BICYCLE_STORE /*11*/:
                Log.e("GoogleApiAvailability", "The application is not licensed to the user.");
                return null;
            case Place.TYPE_CAMPGROUND /*16*/:
                Log.e("GoogleApiAvailability", "One of the API components you attempted to connect to is not available.");
                return null;
            case Place.TYPE_CAR_DEALER /*17*/:
                Log.e("GoogleApiAvailability", "The specified account could not be signed in.");
                return resources.getString(C0148R.string.common_google_play_services_sign_in_failed_title);
            case Place.TYPE_CAR_RENTAL /*18*/:
                return resources.getString(C0148R.string.common_google_play_services_updating_title);
            case Place.TYPE_GENERAL_CONTRACTOR /*42*/:
                return resources.getString(C0148R.string.common_android_wear_update_title);
            default:
                Log.e("GoogleApiAvailability", "Unexpected error code " + i);
                return null;
        }
    }

    public static String zzh(Context context, int i) {
        Resources resources = context.getResources();
        switch (i) {
            case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                return resources.getString(C0148R.string.common_google_play_services_install_button);
            case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
            case Place.TYPE_GENERAL_CONTRACTOR /*42*/:
                return resources.getString(C0148R.string.common_google_play_services_update_button);
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                return resources.getString(C0148R.string.common_google_play_services_enable_button);
            default:
                return resources.getString(17039370);
        }
    }

    public static final String zzi(Context context, int i) {
        Resources resources = context.getResources();
        switch (i) {
            case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                return resources.getString(C0148R.string.common_google_play_services_install_title);
            case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
                return resources.getString(C0148R.string.common_google_play_services_update_title);
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                return resources.getString(C0148R.string.common_google_play_services_enable_title);
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_LEFT /*4*/:
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_RIGHT /*6*/:
                return null;
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                Log.e("GoogleApiAvailability", "An invalid account was specified when connecting. Please provide a valid account.");
                return resources.getString(C0148R.string.common_google_play_services_invalid_account_title);
            case Method.PATCH /*7*/:
                Log.e("GoogleApiAvailability", "Network error occurred. Please retry request later.");
                return resources.getString(C0148R.string.common_google_play_services_network_error_title);
            case Place.TYPE_BANK /*8*/:
                Log.e("GoogleApiAvailability", "Internal error occurred. Please see logs for detailed information");
                return null;
            case Place.TYPE_BAR /*9*/:
                Log.e("GoogleApiAvailability", "Google Play services is invalid. Cannot recover.");
                return resources.getString(C0148R.string.common_google_play_services_unsupported_title);
            case Place.TYPE_BEAUTY_SALON /*10*/:
                Log.e("GoogleApiAvailability", "Developer error occurred. Please see logs for detailed information");
                return null;
            case Place.TYPE_BICYCLE_STORE /*11*/:
                Log.e("GoogleApiAvailability", "The application is not licensed to the user.");
                return null;
            case Place.TYPE_CAMPGROUND /*16*/:
                Log.e("GoogleApiAvailability", "One of the API components you attempted to connect to is not available.");
                return null;
            case Place.TYPE_CAR_DEALER /*17*/:
                Log.e("GoogleApiAvailability", "The specified account could not be signed in.");
                return resources.getString(C0148R.string.common_google_play_services_sign_in_failed_title);
            case Place.TYPE_CAR_RENTAL /*18*/:
                return resources.getString(C0148R.string.common_google_play_services_updating_title);
            case Place.TYPE_GENERAL_CONTRACTOR /*42*/:
                return resources.getString(C0148R.string.common_android_wear_update_title);
            default:
                Log.e("GoogleApiAvailability", "Unexpected error code " + i);
                return null;
        }
    }
}

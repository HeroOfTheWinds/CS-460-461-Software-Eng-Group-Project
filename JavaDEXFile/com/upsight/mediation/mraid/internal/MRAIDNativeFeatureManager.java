package com.upsight.mediation.mraid.internal;

import android.content.Context;
import android.os.Build.VERSION;
import com.upsight.mediation.mraid.MRAIDNativeFeature;
import com.voxelbusters.nativeplugins.defines.Keys.Sharing;
import java.util.List;

public class MRAIDNativeFeatureManager {
    private static final String TAG = "MRAIDNativeFeatureManager";
    private Context context;
    private List<String> supportedNativeFeatures;

    public MRAIDNativeFeatureManager(Context context, List<String> list) {
        this.context = context;
        this.supportedNativeFeatures = list;
    }

    public boolean isCalendarSupported() {
        boolean z = this.supportedNativeFeatures.contains(MRAIDNativeFeature.CALENDAR) && VERSION.SDK_INT >= 14 && this.context.checkCallingOrSelfPermission("android.permission.WRITE_CALENDAR") == 0;
        MRAIDLog.m249v(TAG, "isCalendarSupported " + z);
        return z;
    }

    public boolean isInlineVideoSupported() {
        boolean contains = this.supportedNativeFeatures.contains(MRAIDNativeFeature.INLINE_VIDEO);
        MRAIDLog.m249v(TAG, "isInlineVideoSupported " + contains);
        return contains;
    }

    public boolean isSmsSupported() {
        boolean z = this.supportedNativeFeatures.contains(Sharing.SMS) && this.context.checkCallingOrSelfPermission("android.permission.SEND_SMS") == 0;
        MRAIDLog.m249v(TAG, "isSmsSupported " + z);
        return z;
    }

    public boolean isStorePictureSupported() {
        boolean contains = this.supportedNativeFeatures.contains(MRAIDNativeFeature.STORE_PICTURE);
        MRAIDLog.m249v(TAG, "isStorePictureSupported " + contains);
        return contains;
    }

    public boolean isTelSupported() {
        boolean z = this.supportedNativeFeatures.contains(MRAIDNativeFeature.TEL) && this.context.checkCallingOrSelfPermission("android.permission.CALL_PHONE") == 0;
        MRAIDLog.m249v(TAG, "isTelSupported " + z);
        return z;
    }
}

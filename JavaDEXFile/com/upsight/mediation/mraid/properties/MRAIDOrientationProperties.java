package com.upsight.mediation.mraid.properties;

import com.upsight.android.internal.util.NetworkHelper;
import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import java.util.Arrays;
import spacemadness.com.lunarconsole.C1518R;

public final class MRAIDOrientationProperties {
    public static final int FORCE_ORIENTATION_LANDSCAPE = 1;
    public static final int FORCE_ORIENTATION_NONE = 2;
    public static final int FORCE_ORIENTATION_PORTRAIT = 0;
    public boolean allowOrientationChange;
    public int forceOrientation;

    public MRAIDOrientationProperties() {
        this(true, FORCE_ORIENTATION_NONE);
    }

    public MRAIDOrientationProperties(boolean z, int i) {
        this.allowOrientationChange = z;
        this.forceOrientation = i;
    }

    public static int forceOrientationFromString(String str) {
        int indexOf = Arrays.asList(new String[]{"portrait", "landscape", NetworkHelper.NETWORK_OPERATOR_NONE}).indexOf(str);
        return indexOf != -1 ? indexOf : FORCE_ORIENTATION_NONE;
    }

    public String forceOrientationString() {
        switch (this.forceOrientation) {
            case C1518R.styleable.AdsAttrs_adSize /*0*/:
                return "portrait";
            case FORCE_ORIENTATION_LANDSCAPE /*1*/:
                return "landscape";
            case FORCE_ORIENTATION_NONE /*2*/:
                return NetworkHelper.NETWORK_OPERATOR_NONE;
            default:
                return GameServices.ERROR;
        }
    }
}

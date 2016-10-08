package com.upsight.mediation.mraid.properties;

import java.util.Arrays;

public final class MRAIDResizeProperties {
    public static final int CUSTOM_CLOSE_POSITION_BOTTOM_CENTER = 5;
    public static final int CUSTOM_CLOSE_POSITION_BOTTOM_LEFT = 4;
    public static final int CUSTOM_CLOSE_POSITION_BOTTOM_RIGHT = 6;
    public static final int CUSTOM_CLOSE_POSITION_CENTER = 3;
    public static final int CUSTOM_CLOSE_POSITION_TOP_CENTER = 1;
    public static final int CUSTOM_CLOSE_POSITION_TOP_LEFT = 0;
    public static final int CUSTOM_CLOSE_POSITION_TOP_RIGHT = 2;
    public boolean allowOffscreen;
    public int customClosePosition;
    public int height;
    public int offsetX;
    public int offsetY;
    public int width;

    public MRAIDResizeProperties() {
        this(CUSTOM_CLOSE_POSITION_TOP_LEFT, CUSTOM_CLOSE_POSITION_TOP_LEFT, CUSTOM_CLOSE_POSITION_TOP_LEFT, CUSTOM_CLOSE_POSITION_TOP_LEFT, CUSTOM_CLOSE_POSITION_TOP_RIGHT, true);
    }

    public MRAIDResizeProperties(int i, int i2, int i3, int i4, int i5, boolean z) {
        this.width = i;
        this.height = i2;
        this.offsetX = i3;
        this.offsetY = i4;
        this.customClosePosition = i5;
        this.allowOffscreen = z;
    }

    public static int customClosePositionFromString(String str) {
        int indexOf = Arrays.asList(new String[]{"top-left", "top-center", "top-right", "center", "bottom-left", "bottom-center", "bottom-right"}).indexOf(str);
        return indexOf != -1 ? indexOf : CUSTOM_CLOSE_POSITION_TOP_RIGHT;
    }
}

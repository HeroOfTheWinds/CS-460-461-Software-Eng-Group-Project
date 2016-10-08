package com.upsight.mediation.mraid.internal;

import com.upsight.mediation.mraid.MRAIDNativeFeature;
import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.defines.Keys.Billing.Validation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MRAIDParser {
    private static final String TAG = "MRAIDParser";

    private boolean checkParamsForCommand(String str, Map<String, String> map) {
        return str.equals("createCalendarEvent") ? map.containsKey("eventJSON") : (str.equals("open") || str.equals("playVideo") || str.equals(MRAIDNativeFeature.STORE_PICTURE)) ? map.containsKey(Keys.URL) : str.equals("setOrientationProperties") ? map.containsKey("allowOrientationChange") && map.containsKey("forceOrientation") : str.equals("setResizeProperties") ? map.containsKey("width") && map.containsKey("height") && map.containsKey("offsetX") && map.containsKey("offsetY") && map.containsKey("customClosePosition") && map.containsKey("allowOffscreen") : str.equals("useCustomClose") ? map.containsKey("useCustomClose") : true;
    }

    private boolean isValidCommand(String str) {
        return Arrays.asList(new String[]{"replay", Validation.SUCCESS, "close", "createCalendarEvent", "expand", "open", "playVideo", "resize", "rewardComplete", "setOrientationProperties", "setResizeProperties", MRAIDNativeFeature.STORE_PICTURE, "useCustomClose"}).contains(str);
    }

    public Map<String, String> parseCommandUrl(String str) {
        String substring;
        MRAIDLog.m249v(TAG, "parseCommandUrl " + str);
        String substring2 = str.substring(8);
        Map hashMap = new HashMap();
        int indexOf = substring2.indexOf(63);
        if (indexOf != -1) {
            substring = substring2.substring(0, indexOf);
            for (String str2 : substring2.substring(indexOf + 1).split("&")) {
                int indexOf2 = str2.indexOf(61);
                hashMap.put(str2.substring(0, indexOf2), str2.substring(indexOf2 + 1));
            }
        } else {
            substring = substring2;
        }
        if (!isValidCommand(substring)) {
            MRAIDLog.m246i("command " + substring + " is unknown");
            return null;
        } else if (checkParamsForCommand(substring, hashMap)) {
            Map<String, String> hashMap2 = new HashMap();
            hashMap2.put("command", substring);
            hashMap2.putAll(hashMap);
            return hashMap2;
        } else {
            MRAIDLog.m246i("command URL " + str + " is missing parameters");
            return null;
        }
    }
}

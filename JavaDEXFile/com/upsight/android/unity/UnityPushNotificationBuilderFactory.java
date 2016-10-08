package com.upsight.android.unity;

import android.support.v4.app.NotificationCompat.Builder;
import com.upsight.android.UpsightContext;
import com.upsight.android.googlepushservices.UpsightPushNotificationBuilderFactory.Default;

public class UnityPushNotificationBuilderFactory extends Default {
    private static final String NOTIFICATION_ICON_RES_NAME = "upsight_notification_icon";
    private static final String NOTIFICATION_ICON_RES_TYPE = "drawable";

    public Builder getNotificationBuilder(UpsightContext upsightContext, String str, String str2, String str3) {
        int identifier = upsightContext.getResources().getIdentifier(NOTIFICATION_ICON_RES_NAME, NOTIFICATION_ICON_RES_TYPE, upsightContext.getPackageName());
        if (identifier == 0) {
            identifier = upsightContext.getApplicationInfo().icon;
        }
        return super.getNotificationBuilder(upsightContext, str, str2, str3).setSmallIcon(identifier);
    }
}

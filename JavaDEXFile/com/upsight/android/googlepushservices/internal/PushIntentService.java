package com.upsight.android.googlepushservices.internal;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.text.TextUtils;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightGooglePushServicesExtension;
import com.upsight.android.googlepushservices.UpsightGooglePushServicesComponent;
import com.upsight.android.googlepushservices.UpsightPushNotificationBuilderFactory;
import com.upsight.android.googlepushservices.UpsightPushNotificationBuilderFactory.Default;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import javax.inject.Inject;
import spacemadness.com.lunarconsole.C1518R;

public final class PushIntentService extends IntentService {
    private static final String ACTION_ACTIVITY = "activity";
    private static final String ACTION_CONTENT_UNIT = "content_id";
    private static final String ACTION_PLACEMENT = "placement";
    private static final Integer INVALID_MSG_ID;
    private static final String LOG_TAG;
    private static final String NOTIFICATION_BUILDER_FACTORY_KEY_NAME = "com.upsight.notification_builder_factory";
    private static final String SERVICE_NAME = "UpsightGcmPushIntentService";
    private static final String URI_HOST = "com.playhaven.android";
    private static final String URI_SCHEME = "playhaven";
    @Inject
    GoogleCloudMessaging mGcm;
    private UpsightPushNotificationBuilderFactory mNotificationBuilderFactory;
    @Inject
    UpsightContext mUpsight;

    /* renamed from: com.upsight.android.googlepushservices.internal.PushIntentService.1 */
    static /* synthetic */ class C09001 {
        static final /* synthetic */ int[] f258x51d6a9c0;

        static {
            f258x51d6a9c0 = new int[UriTypes.values().length];
            try {
                f258x51d6a9c0[UriTypes.CUSTOM.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f258x51d6a9c0[UriTypes.DEFAULT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f258x51d6a9c0[UriTypes.ACTIVITY.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f258x51d6a9c0[UriTypes.PLACEMENT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    class PushIds {
        final Integer campaignId;
        final Integer contentId;
        final Integer messageId;

        private PushIds(Integer num, Integer num2, Integer num3) {
            this.messageId = num;
            this.campaignId = num2;
            this.contentId = num3;
        }
    }

    public enum PushParams {
        message_id,
        msg_campaign_id,
        content_id,
        title,
        text,
        uri,
        image_url
    }

    public enum UriTypes {
        DEFAULT,
        CUSTOM,
        INVALID,
        PLACEMENT,
        ACTIVITY
    }

    static {
        LOG_TAG = PushIntentService.class.getSimpleName();
        INVALID_MSG_ID = Integer.valueOf(0);
    }

    public PushIntentService() {
        super(SERVICE_NAME);
    }

    private void interpretPushEvent(Bundle bundle) {
        Intent intent = null;
        boolean z = false;
        Object string = bundle.getString(PushParams.uri.name());
        Uri parse = !TextUtils.isEmpty(string) ? Uri.parse(string) : null;
        if (parse != null) {
            UpsightLogger logger = this.mUpsight.getLogger();
            PushIds parsePushIds = parsePushIds(parse, bundle, logger);
            switch (C09001.f258x51d6a9c0[checkUri(logger, parse).ordinal()]) {
                case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    intent = new Intent("android.intent.action.VIEW", parse);
                    z = true;
                    break;
                case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
                    intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                    break;
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                    try {
                        intent = new Intent(this, Class.forName(parse.getQueryParameter(ACTION_ACTIVITY)));
                        z = true;
                        break;
                    } catch (Throwable e) {
                        logger.m208e(LOG_TAG, e, "Could not parse class name", new Object[0]);
                        break;
                    }
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_LEFT /*4*/:
                    intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                    break;
            }
            if (intent != null) {
                showNotification(intent, z, parsePushIds.campaignId, parsePushIds.messageId, parsePushIds.contentId, bundle.getString(PushParams.title.name()), bundle.getString(PushParams.text.name()), bundle.getString(PushParams.image_url.name()));
            }
        }
    }

    private static Integer parseAsInt(String str, Integer num, UpsightLogger upsightLogger) {
        if (TextUtils.isEmpty(str) || !TextUtils.isDigitsOnly(str)) {
            return num;
        }
        try {
            return Integer.valueOf(Integer.parseInt(str));
        } catch (Throwable e) {
            if (num == null) {
                upsightLogger.m208e(LOG_TAG, e, String.format("Could not parse %s. Setting to null.", new Object[]{str}), new Object[0]);
                return num;
            }
            upsightLogger.m208e(LOG_TAG, e, String.format("Could not parse %s. Setting to %d.", new Object[]{str, num}), new Object[0]);
            return num;
        }
    }

    private void showNotification(Intent intent, boolean z, Integer num, Integer num2, Integer num3, String str, String str2, String str3) {
        PendingIntent service = PendingIntent.getService(this, num2.intValue(), PushClickIntentService.newIntent(this, intent, z, num, num2, num3), 268435456);
        if (this.mNotificationBuilderFactory == null) {
            this.mNotificationBuilderFactory = loadNotificationBuilderFactory();
        }
        ((NotificationManager) getSystemService("notification")).notify(num2.intValue(), this.mNotificationBuilderFactory.getNotificationBuilder(this.mUpsight, str, str2, str3).setContentIntent(service).setAutoCancel(true).build());
    }

    public UriTypes checkUri(UpsightLogger upsightLogger, Uri uri) {
        CharSequence host = uri.getHost();
        CharSequence scheme = uri.getScheme();
        if (TextUtils.isEmpty(host) || TextUtils.isEmpty(scheme)) {
            upsightLogger.m207e(LOG_TAG, String.format("Invalid URI, host or scheme is null or empty: %s.", new Object[]{uri}), new Object[0]);
            return UriTypes.INVALID;
        } else if (URI_SCHEME.equals(scheme) && URI_HOST.equals(host)) {
            return uri.getQueryParameter(ACTION_ACTIVITY) != null ? UriTypes.ACTIVITY : uri.getQueryParameter(ACTION_PLACEMENT) != null ? UriTypes.PLACEMENT : uri.getQueryParameter(ACTION_CONTENT_UNIT) != null ? UriTypes.PLACEMENT : UriTypes.DEFAULT;
        } else {
            try {
                return getPackageManager().resolveActivity(new Intent().setData(uri), 0) == null ? UriTypes.INVALID : UriTypes.CUSTOM;
            } catch (Exception e) {
                upsightLogger.m207e(LOG_TAG, String.format("Nothing registered for %s", new Object[]{uri}), new Object[0]);
                return UriTypes.INVALID;
            }
        }
    }

    UpsightPushNotificationBuilderFactory loadNotificationBuilderFactory() {
        UpsightLogger logger = this.mUpsight.getLogger();
        try {
            Bundle bundle = getPackageManager().getApplicationInfo(getPackageName(), AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS).metaData;
            if (bundle != null && bundle.containsKey(NOTIFICATION_BUILDER_FACTORY_KEY_NAME)) {
                try {
                    Class cls = Class.forName(bundle.getString(NOTIFICATION_BUILDER_FACTORY_KEY_NAME));
                    if (UpsightPushNotificationBuilderFactory.class.isAssignableFrom(cls)) {
                        return (UpsightPushNotificationBuilderFactory) cls.newInstance();
                    }
                    logger.m207e(Upsight.LOG_TAG, String.format("Class %s must implement %s!", new Object[]{cls.getName(), UpsightPushNotificationBuilderFactory.class.getName()}), new Object[0]);
                } catch (ClassNotFoundException e) {
                    logger.m207e(Upsight.LOG_TAG, String.format("Unexpected error: Class: %s was not found.", new Object[]{r2}), e);
                } catch (InstantiationException e2) {
                    logger.m207e(Upsight.LOG_TAG, String.format("Unexpected error: Class: %s does not have public access.", new Object[]{r2}), e2);
                } catch (IllegalAccessException e3) {
                    logger.m207e(Upsight.LOG_TAG, String.format("Unexpected error: Class: %s could not be instantiated.", new Object[]{r2}), e3);
                }
            }
        } catch (NameNotFoundException e4) {
            logger.m207e(Upsight.LOG_TAG, "Unexpected error: Package name missing!?", e4);
        }
        return new Default();
    }

    protected void onHandleIntent(Intent intent) {
        UpsightGooglePushServicesExtension upsightGooglePushServicesExtension = (UpsightGooglePushServicesExtension) Upsight.createContext(this).getUpsightExtension(UpsightGooglePushServicesExtension.EXTENSION_NAME);
        if (upsightGooglePushServicesExtension != null) {
            ((UpsightGooglePushServicesComponent) upsightGooglePushServicesExtension.getComponent()).inject(this);
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(this.mGcm.getMessageType(intent))) {
                Bundle extras = intent.getExtras();
                if (!(extras.isEmpty() || TextUtils.isEmpty(extras.getString(PushParams.message_id.name())))) {
                    interpretPushEvent(extras);
                }
            }
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    PushIds parsePushIds(Uri uri, Bundle bundle, UpsightLogger upsightLogger) {
        return new PushIds(parseAsInt(bundle.getString(PushParams.message_id.name()), INVALID_MSG_ID, upsightLogger), parseAsInt(bundle.getString(PushParams.msg_campaign_id.name()), null, upsightLogger), parseAsInt(uri.getQueryParameter(PushParams.content_id.name()), null, upsightLogger), null);
    }
}

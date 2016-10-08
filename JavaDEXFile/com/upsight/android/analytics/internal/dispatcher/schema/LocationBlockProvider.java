package com.upsight.android.analytics.internal.dispatcher.schema;

import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.provider.UpsightDataProvider;
import com.upsight.android.analytics.provider.UpsightLocationTracker.Data;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class LocationBlockProvider extends UpsightDataProvider {
    private static final String DATETIME_FORMAT_ISO_8601 = "yyyy-MM-dd HH:mm:ss.SSSZ";
    public static final String LATITUDE_KEY = "location.lat";
    public static final String LONGITUDE_KEY = "location.lon";
    public static final String TIME_ZONE_KEY = "location.tz";
    private static final int TIME_ZONE_OFFSET_LENGTH = 5;
    private static final Pattern TIME_ZONE_OFFSET_PATTERN;
    private TimeZone mCurrentTimeZone;
    private String mTimeZoneOffset;
    private UpsightContext mUpsight;

    static {
        TIME_ZONE_OFFSET_PATTERN = Pattern.compile("[+-][0-9]{4}");
    }

    LocationBlockProvider(UpsightContext upsightContext) {
        this.mCurrentTimeZone = null;
        this.mTimeZoneOffset = null;
        this.mUpsight = upsightContext;
    }

    private String fetchCurrentTimeZone() {
        TimeZone timeZone = TimeZone.getDefault();
        if (!(timeZone == null || timeZone.equals(this.mCurrentTimeZone))) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_FORMAT_ISO_8601, Locale.US);
            simpleDateFormat.setTimeZone(timeZone);
            String format = simpleDateFormat.format(new Date());
            if (format != null) {
                int length = format.length();
                if (length > TIME_ZONE_OFFSET_LENGTH) {
                    format = format.substring(length - 5, length);
                    if (isTimeZoneOffsetValid(format)) {
                        this.mCurrentTimeZone = timeZone;
                        this.mTimeZoneOffset = format;
                    }
                }
            }
        }
        return this.mTimeZoneOffset;
    }

    private Data fetchLatestLocation() {
        return (Data) this.mUpsight.getDataStore().fetchObservable(Data.class).lastOrDefault(null).toBlocking().first();
    }

    public Set<String> availableKeys() {
        return new HashSet(Arrays.asList(new String[]{TIME_ZONE_KEY, LATITUDE_KEY, LONGITUDE_KEY}));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object get(java.lang.String r4) {
        /*
        r3 = this;
        r1 = 0;
        r0 = 0;
        monitor-enter(r3);
        r2 = r4.hashCode();	 Catch:{ all -> 0x0056 }
        switch(r2) {
            case -59422746: goto L_0x001d;
            case -59422318: goto L_0x0027;
            case 552272735: goto L_0x0014;
            default: goto L_0x000a;
        };	 Catch:{ all -> 0x0056 }
    L_0x000a:
        r1 = -1;
    L_0x000b:
        switch(r1) {
            case 0: goto L_0x0031;
            case 1: goto L_0x0036;
            case 2: goto L_0x0046;
            default: goto L_0x000e;
        };	 Catch:{ all -> 0x0056 }
    L_0x000e:
        r0 = super.get(r4);	 Catch:{ all -> 0x0056 }
    L_0x0012:
        monitor-exit(r3);
        return r0;
    L_0x0014:
        r2 = "location.tz";
        r2 = r4.equals(r2);	 Catch:{ all -> 0x0056 }
        if (r2 == 0) goto L_0x000a;
    L_0x001c:
        goto L_0x000b;
    L_0x001d:
        r1 = "location.lat";
        r1 = r4.equals(r1);	 Catch:{ all -> 0x0056 }
        if (r1 == 0) goto L_0x000a;
    L_0x0025:
        r1 = 1;
        goto L_0x000b;
    L_0x0027:
        r1 = "location.lon";
        r1 = r4.equals(r1);	 Catch:{ all -> 0x0056 }
        if (r1 == 0) goto L_0x000a;
    L_0x002f:
        r1 = 2;
        goto L_0x000b;
    L_0x0031:
        r0 = r3.fetchCurrentTimeZone();	 Catch:{ all -> 0x0056 }
        goto L_0x0012;
    L_0x0036:
        r1 = r3.fetchLatestLocation();	 Catch:{ all -> 0x0056 }
        if (r1 == 0) goto L_0x0012;
    L_0x003c:
        r0 = r1.getLatitude();	 Catch:{ all -> 0x0056 }
        r2 = 0;
        r0 = android.location.Location.convert(r0, r2);	 Catch:{ all -> 0x0056 }
        goto L_0x0012;
    L_0x0046:
        r1 = r3.fetchLatestLocation();	 Catch:{ all -> 0x0056 }
        if (r1 == 0) goto L_0x0012;
    L_0x004c:
        r0 = r1.getLongitude();	 Catch:{ all -> 0x0056 }
        r2 = 0;
        r0 = android.location.Location.convert(r0, r2);	 Catch:{ all -> 0x0056 }
        goto L_0x0012;
    L_0x0056:
        r0 = move-exception;
        monitor-exit(r3);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.upsight.android.analytics.internal.dispatcher.schema.LocationBlockProvider.get(java.lang.String):java.lang.Object");
    }

    boolean isTimeZoneOffsetValid(String str) {
        return TIME_ZONE_OFFSET_PATTERN.matcher(str).matches();
    }
}

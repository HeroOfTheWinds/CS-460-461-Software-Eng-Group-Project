package com.upsight.android.marketing.internal.content;

import android.text.TextUtils;
import com.squareup.otto.Bus;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.marketing.UpsightMarketingContentStore;
import com.upsight.android.marketing.internal.content.MarketingContent.ScopelessAvailabilityEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class MarketingContentStoreImpl extends UpsightMarketingContentStore implements MarketingContentStore {
    public static final long DEFAULT_TIME_TO_LIVE_MS = 600000;
    private static final String LOG_TAG;
    private Bus mBus;
    private Clock mClock;
    private final Map<String, MarketingContent> mContentMap;
    private UpsightLogger mLogger;
    private final Map<String, String> mParentEligibilityMap;
    private final Map<String, Set<String>> mScopeEligibilityMap;
    private final Map<String, Long> mTimestamps;

    static {
        LOG_TAG = MarketingContentStore.class.getSimpleName();
    }

    public MarketingContentStoreImpl(Bus bus, Clock clock, UpsightLogger upsightLogger) {
        this.mTimestamps = new HashMap();
        this.mContentMap = new HashMap();
        this.mScopeEligibilityMap = new HashMap();
        this.mParentEligibilityMap = new HashMap();
        this.mBus = bus;
        this.mClock = clock;
        this.mLogger = upsightLogger;
    }

    public MarketingContent get(String str) {
        MarketingContent marketingContent;
        synchronized (this) {
            Long l = (Long) this.mTimestamps.get(str);
            if (l == null || this.mClock.currentTimeMillis() > l.longValue() + DEFAULT_TIME_TO_LIVE_MS) {
                remove(str);
                marketingContent = null;
            } else {
                this.mLogger.m205d(LOG_TAG, "get id=" + str, new Object[0]);
                marketingContent = (MarketingContent) this.mContentMap.get(str);
            }
        }
        return marketingContent;
    }

    public Set<String> getIdsForScope(String str) {
        Set<String> hashSet;
        synchronized (this) {
            Set set = (Set) this.mScopeEligibilityMap.get(str);
            hashSet = set == null ? new HashSet() : new HashSet(set);
            StringBuilder stringBuilder = new StringBuilder();
            for (String append : hashSet) {
                stringBuilder.append(append).append(" ");
            }
            this.mLogger.m205d(LOG_TAG, "getIdsForScope scope=" + str + " ids=[ " + stringBuilder + " ]", new Object[0]);
        }
        return hashSet;
    }

    public boolean isContentReady(String str) {
        boolean z;
        synchronized (this) {
            z = !getIdsForScope(str).isEmpty();
        }
        return z;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean presentScopedContent(java.lang.String r7, java.lang.String[] r8) {
        /*
        r6 = this;
        r1 = 0;
        monitor-enter(r6);
        r0 = r6.mContentMap;	 Catch:{ all -> 0x0036 }
        r0 = r0.get(r7);	 Catch:{ all -> 0x0036 }
        r0 = (com.upsight.android.marketing.internal.content.MarketingContent) r0;	 Catch:{ all -> 0x0036 }
        if (r0 == 0) goto L_0x0063;
    L_0x000c:
        if (r8 == 0) goto L_0x0063;
    L_0x000e:
        r2 = r8.length;	 Catch:{ all -> 0x0036 }
        if (r2 <= 0) goto L_0x0063;
    L_0x0011:
        r3 = r8.length;	 Catch:{ all -> 0x0036 }
        r2 = r1;
    L_0x0013:
        if (r2 >= r3) goto L_0x0039;
    L_0x0015:
        r4 = r8[r2];
        r1 = r6.mScopeEligibilityMap;	 Catch:{ all -> 0x0036 }
        r1 = r1.get(r4);	 Catch:{ all -> 0x0036 }
        r1 = (java.util.Set) r1;	 Catch:{ all -> 0x0036 }
        if (r1 == 0) goto L_0x0028;
    L_0x0021:
        r1.add(r7);	 Catch:{ all -> 0x0036 }
    L_0x0024:
        r1 = r2 + 1;
        r2 = r1;
        goto L_0x0013;
    L_0x0028:
        r1 = new java.util.HashSet;	 Catch:{ all -> 0x0036 }
        r1.<init>();	 Catch:{ all -> 0x0036 }
        r1.add(r7);	 Catch:{ all -> 0x0036 }
        r5 = r6.mScopeEligibilityMap;	 Catch:{ all -> 0x0036 }
        r5.put(r4, r1);	 Catch:{ all -> 0x0036 }
        goto L_0x0024;
    L_0x0036:
        r0 = move-exception;
        monitor-exit(r6);
        throw r0;
    L_0x0039:
        r1 = new com.upsight.android.marketing.internal.content.MarketingContent$ScopedAvailabilityEvent;	 Catch:{ all -> 0x0036 }
        r1.<init>(r7, r8);	 Catch:{ all -> 0x0036 }
        r2 = r6.mBus;	 Catch:{ all -> 0x0036 }
        r0.markPresentable(r1, r2);	 Catch:{ all -> 0x0036 }
        r0 = r6.mLogger;	 Catch:{ all -> 0x0036 }
        r1 = LOG_TAG;	 Catch:{ all -> 0x0036 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0036 }
        r2.<init>();	 Catch:{ all -> 0x0036 }
        r3 = "presentScopedContent id=";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0036 }
        r2 = r2.append(r7);	 Catch:{ all -> 0x0036 }
        r2 = r2.toString();	 Catch:{ all -> 0x0036 }
        r3 = 0;
        r3 = new java.lang.Object[r3];	 Catch:{ all -> 0x0036 }
        r0.m205d(r1, r2, r3);	 Catch:{ all -> 0x0036 }
        r0 = 1;
    L_0x0061:
        monitor-exit(r6);
        return r0;
    L_0x0063:
        r0 = r1;
        goto L_0x0061;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.upsight.android.marketing.internal.content.MarketingContentStoreImpl.presentScopedContent(java.lang.String, java.lang.String[]):boolean");
    }

    public boolean presentScopelessContent(String str, String str2) {
        boolean z;
        synchronized (this) {
            MarketingContent marketingContent = (MarketingContent) this.mContentMap.get(str);
            if (marketingContent == null || TextUtils.isEmpty(str2)) {
                z = false;
            } else {
                this.mParentEligibilityMap.put(str2, str);
                marketingContent.markPresentable(new ScopelessAvailabilityEvent(str, str2), this.mBus);
                this.mLogger.m205d(LOG_TAG, "presentScopelessContent id=" + str + " parentId=" + str2, new Object[0]);
                z = true;
            }
        }
        return z;
    }

    public boolean put(String str, MarketingContent marketingContent) {
        boolean z = false;
        synchronized (this) {
            if (!(TextUtils.isEmpty(str) || marketingContent == null)) {
                this.mContentMap.put(str, marketingContent);
                this.mTimestamps.put(str, Long.valueOf(this.mClock.currentTimeMillis()));
                z = true;
            }
            this.mLogger.m205d(LOG_TAG, "put id=" + str + " isAdded=" + z, new Object[0]);
        }
        return z;
    }

    public boolean remove(String str) {
        boolean z = false;
        synchronized (this) {
            if (!TextUtils.isEmpty(str)) {
                boolean z2 = this.mContentMap.remove(str) != null;
                if (z2) {
                    String str2;
                    for (String str22 : this.mScopeEligibilityMap.keySet()) {
                        Set set = (Set) this.mScopeEligibilityMap.get(str22);
                        if (set != null && set.contains(str)) {
                            set.remove(str);
                        }
                    }
                    Iterator it = this.mParentEligibilityMap.keySet().iterator();
                    while (it.hasNext()) {
                        str22 = (String) it.next();
                        String str3 = (String) this.mParentEligibilityMap.get(str22);
                        if (str.equals(str22) || str.equals(str3)) {
                            it.remove();
                        }
                    }
                    this.mTimestamps.remove(str);
                    z = z2;
                } else {
                    z = z2;
                }
            }
            this.mLogger.m205d(LOG_TAG, "remove id=" + str + " isRemoved=" + z, new Object[0]);
        }
        return z;
    }
}

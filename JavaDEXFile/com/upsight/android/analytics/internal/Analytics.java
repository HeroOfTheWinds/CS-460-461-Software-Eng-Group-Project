package com.upsight.android.analytics.internal;

import android.app.Activity;
import android.content.Intent;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.UpsightAnalyticsApi;
import com.upsight.android.analytics.UpsightGooglePlayHelper;
import com.upsight.android.analytics.UpsightLifeCycleTracker;
import com.upsight.android.analytics.UpsightLifeCycleTracker.ActivityState;
import com.upsight.android.analytics.event.UpsightAnalyticsEvent;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.DataStoreRecord.Action;
import com.upsight.android.analytics.internal.association.AssociationManager;
import com.upsight.android.analytics.internal.dispatcher.schema.SchemaSelectorBuilder;
import com.upsight.android.analytics.internal.session.Session;
import com.upsight.android.analytics.internal.session.SessionManager;
import com.upsight.android.analytics.provider.UpsightDataProvider;
import com.upsight.android.analytics.provider.UpsightLocationTracker;
import com.upsight.android.analytics.provider.UpsightLocationTracker.Data;
import com.upsight.android.analytics.provider.UpsightOptOutStatus;
import com.upsight.android.analytics.provider.UpsightUserAttributes;
import com.upsight.android.analytics.provider.UpsightUserAttributes.Entry;
import com.upsight.android.internal.util.PreferencesHelper;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.persistence.UpsightDataStore;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class Analytics implements UpsightAnalyticsApi {
    private static final String LOG_TAG;
    private static final String SEQUENCE_ID_FIELD_NAME = "seq_id";
    private static final String USER_ATTRIBUTES_FIELD_NAME = "user_attributes";
    private final AssociationManager mAssociationManager;
    private final UpsightDataStore mDataStore;
    private final Set<Entry> mDefaultUserAttributes;
    private final UpsightGooglePlayHelper mGooglePlayHelper;
    private final Gson mGson;
    private final UpsightLifeCycleTracker mLifeCycleTracker;
    private final UpsightLocationTracker mLocationTracker;
    private final UpsightLogger mLogger;
    private final UpsightOptOutStatus mOptOutStatus;
    private final SchemaSelectorBuilder mSchemaSelector;
    private final SessionManager mSessionManager;
    private final UpsightContext mUpsight;
    private final UpsightUserAttributes mUserAttributes;

    static {
        LOG_TAG = Analytics.class.getSimpleName();
    }

    @Inject
    public Analytics(UpsightContext upsightContext, UpsightLifeCycleTracker upsightLifeCycleTracker, SessionManager sessionManager, SchemaSelectorBuilder schemaSelectorBuilder, AssociationManager associationManager, UpsightOptOutStatus upsightOptOutStatus, UpsightLocationTracker upsightLocationTracker, UpsightUserAttributes upsightUserAttributes, UpsightGooglePlayHelper upsightGooglePlayHelper) {
        this.mUpsight = upsightContext;
        this.mDataStore = upsightContext.getDataStore();
        this.mLifeCycleTracker = upsightLifeCycleTracker;
        this.mSessionManager = sessionManager;
        this.mGson = upsightContext.getCoreComponent().gson();
        this.mLogger = upsightContext.getLogger();
        this.mSchemaSelector = schemaSelectorBuilder;
        this.mAssociationManager = associationManager;
        this.mOptOutStatus = upsightOptOutStatus;
        this.mLocationTracker = upsightLocationTracker;
        this.mUserAttributes = upsightUserAttributes;
        this.mDefaultUserAttributes = this.mUserAttributes.getDefault();
        this.mGooglePlayHelper = upsightGooglePlayHelper;
    }

    private void appendAssociationData(String str, JsonObject jsonObject) {
        this.mAssociationManager.associate(str, jsonObject);
    }

    private JsonElement getAllAsJsonElement(Set<Entry> set) {
        JsonElement jsonObject = new JsonObject();
        for (Entry entry : set) {
            if (String.class.equals(entry.getType())) {
                jsonObject.addProperty(entry.getKey(), PreferencesHelper.getString(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + entry.getKey(), (String) entry.getDefaultValue()));
            } else if (Integer.class.equals(entry.getType())) {
                jsonObject.addProperty(entry.getKey(), Integer.valueOf(PreferencesHelper.getInt(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + entry.getKey(), ((Integer) entry.getDefaultValue()).intValue())));
            } else if (Boolean.class.equals(entry.getType())) {
                jsonObject.addProperty(entry.getKey(), Boolean.valueOf(PreferencesHelper.getBoolean(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + entry.getKey(), ((Boolean) entry.getDefaultValue()).booleanValue())));
            } else if (Float.class.equals(entry.getType())) {
                jsonObject.addProperty(entry.getKey(), Float.valueOf(PreferencesHelper.getFloat(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + entry.getKey(), ((Float) entry.getDefaultValue()).floatValue())));
            } else if (Date.class.equals(entry.getType())) {
                long j = PreferencesHelper.getLong(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + entry.getKey(), TimeUnit.SECONDS.convert(((Date) entry.getDefaultValue()).getTime(), TimeUnit.MILLISECONDS));
                if (j != UpsightUserAttributes.DATETIME_NULL_S) {
                    jsonObject.addProperty(entry.getKey(), Long.valueOf(j));
                } else {
                    jsonObject.addProperty(entry.getKey(), (Long) null);
                }
            }
        }
        return jsonObject;
    }

    private JsonObject toJsonElement(UpsightAnalyticsEvent upsightAnalyticsEvent) {
        JsonObject asJsonObject = this.mGson.toJsonTree(upsightAnalyticsEvent).getAsJsonObject();
        asJsonObject.addProperty(SEQUENCE_ID_FIELD_NAME, Long.valueOf(EventSequenceId.getAndIncrement(this.mUpsight)));
        asJsonObject.add(USER_ATTRIBUTES_FIELD_NAME, getAllAsJsonElement(this.mDefaultUserAttributes));
        return asJsonObject;
    }

    public Boolean getBooleanUserAttribute(String str) {
        return this.mUserAttributes.getBoolean(str);
    }

    public Date getDatetimeUserAttribute(String str) {
        return this.mUserAttributes.getDatetime(str);
    }

    public Set<Entry> getDefaultUserAttributes() {
        return this.mUserAttributes.getDefault();
    }

    public Float getFloatUserAttribute(String str) {
        return this.mUserAttributes.getFloat(str);
    }

    public Integer getIntUserAttribute(String str) {
        return this.mUserAttributes.getInt(str);
    }

    public boolean getOptOutStatus() {
        return this.mOptOutStatus.get();
    }

    public String getStringUserAttribute(String str) {
        return this.mUserAttributes.getString(str);
    }

    public void purgeLocation() {
        this.mLocationTracker.purge();
    }

    public void putUserAttribute(String str, Boolean bool) {
        this.mUserAttributes.put(str, bool);
    }

    public void putUserAttribute(String str, Float f) {
        this.mUserAttributes.put(str, f);
    }

    public void putUserAttribute(String str, Integer num) {
        this.mUserAttributes.put(str, num);
    }

    public void putUserAttribute(String str, String str2) {
        this.mUserAttributes.put(str, str2);
    }

    public void putUserAttribute(String str, Date date) {
        this.mUserAttributes.put(str, date);
    }

    public void record(UpsightAnalyticsEvent upsightAnalyticsEvent) {
        Session currentSession = this.mSessionManager.getCurrentSession();
        long timeStamp = currentSession.getTimeStamp();
        Integer messageID = currentSession.getMessageID();
        Integer campaignID = currentSession.getCampaignID();
        int sessionNumber = currentSession.getSessionNumber();
        long previousTos = currentSession.getPreviousTos();
        JsonObject toJsonElement = toJsonElement(upsightAnalyticsEvent);
        appendAssociationData(upsightAnalyticsEvent.getType(), toJsonElement);
        DataStoreRecord create = DataStoreRecord.create(Action.Created, timeStamp, messageID, campaignID, sessionNumber, previousTos, toJsonElement.toString(), upsightAnalyticsEvent.getType());
        if (upsightAnalyticsEvent instanceof DynamicIdentifiers) {
            create.setIdentifiers(((DynamicIdentifiers) upsightAnalyticsEvent).getIdentifiersName());
        }
        this.mDataStore.store(create);
    }

    public void registerDataProvider(UpsightDataProvider upsightDataProvider) {
        this.mSchemaSelector.registerDataProvider(upsightDataProvider);
    }

    public void setOptOutStatus(boolean z) {
        this.mOptOutStatus.set(z);
    }

    public void trackActivity(Activity activity, ActivityState activityState) {
        this.mLifeCycleTracker.track(activity, activityState, null);
    }

    public void trackLocation(Data data) {
        this.mLocationTracker.track(data);
    }

    public void trackPurchase(int i, String str, double d, double d2, String str2, Intent intent, UpsightPublisherData upsightPublisherData) throws UpsightException {
        this.mGooglePlayHelper.trackPurchase(i, str, d, d2, str2, intent, upsightPublisherData);
    }
}

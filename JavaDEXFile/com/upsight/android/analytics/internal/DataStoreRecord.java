package com.upsight.android.analytics.internal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.datastore.record")
public final class DataStoreRecord {
    @SerializedName("action")
    @Expose
    Action action;
    @SerializedName("campaign_id")
    @Expose
    Integer campaignID;
    @SerializedName("id")
    @UpsightStorableIdentifier
    @Expose
    String id;
    @SerializedName("identifiers")
    @Expose
    String identifiers;
    @SerializedName("message_id")
    @Expose
    Integer messageID;
    @SerializedName("past_session_time")
    @Expose
    long pastSessionTime;
    @SerializedName("session_id")
    @Expose
    long sessionID;
    @SerializedName("session_num")
    @Expose
    int sessionNumber;
    @SerializedName("source")
    @Expose
    String source;
    @SerializedName("source_type")
    @Expose
    String sourceType;

    public enum Action {
        Created,
        Updated,
        Removed
    }

    DataStoreRecord() {
    }

    private DataStoreRecord(Action action, long j, Integer num, Integer num2, int i, long j2, String str, String str2) {
        this.action = action;
        this.sessionID = j;
        this.messageID = num;
        this.campaignID = num2;
        this.source = str;
        this.sourceType = str2;
        this.sessionNumber = i;
        this.pastSessionTime = j2;
    }

    public static DataStoreRecord create(Action action, long j, int i, long j2, String str, String str2) {
        return create(action, j, null, null, i, j2, str, str2);
    }

    public static DataStoreRecord create(Action action, long j, Integer num, Integer num2, int i, long j2, String str, String str2) {
        return new DataStoreRecord(action, j, num, num2, i, j2, str, str2);
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            DataStoreRecord dataStoreRecord = (DataStoreRecord) obj;
            if (this.id != null) {
                if (!this.id.equals(dataStoreRecord.id)) {
                    return false;
                }
            } else if (dataStoreRecord.id != null) {
                return false;
            }
        }
        return true;
    }

    public Action getAction() {
        return this.action;
    }

    public Integer getCampaignID() {
        return this.campaignID;
    }

    public String getID() {
        return this.id;
    }

    public String getIdentifiers() {
        return this.identifiers;
    }

    public Integer getMessageID() {
        return this.messageID;
    }

    public long getPastSessionTime() {
        return this.pastSessionTime;
    }

    public long getSessionID() {
        return this.sessionID;
    }

    public int getSessionNumber() {
        return this.sessionNumber;
    }

    public String getSource() {
        return this.source;
    }

    public String getSourceType() {
        return this.sourceType;
    }

    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }

    public void setIdentifiers(String str) {
        this.identifiers = str;
    }
}

package com.upsight.android.analytics.internal.dispatcher.delivery;

import android.text.TextUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.upsight.android.analytics.internal.DataStoreRecord;
import java.io.IOException;

class Session {
    private static final String EVENTS = "events";
    private static final String INSTALL_TS = "install_ts";
    private static final String MSG_CAMPAIGN_ID = "msg_campaign_id";
    private static final String MSG_ID = "msg_id";
    private static final String PAST_SESSION_TIME = "past_session_time";
    private static final String SESSION_NUM = "session_num";
    private static final String SESSION_START = "session_start";
    @SerializedName("events")
    @Expose
    private JsonArray mEvents;
    @SerializedName("install_ts")
    @Expose
    private long mInstallTs;
    @SerializedName("msg_campaign_id")
    @Expose
    private Integer mMsgCampaignId;
    @SerializedName("msg_id")
    @Expose
    private Integer mMsgId;
    @SerializedName("past_session_time")
    @Expose
    private long mPastSessionTime;
    @SerializedName("session_num")
    @Expose
    private int mSessionNum;
    @SerializedName("session_start")
    @Expose
    private long mSessionStart;

    static final class DefaultTypeAdapter extends TypeAdapter<Session> {
        DefaultTypeAdapter() {
        }

        public Session read(JsonReader jsonReader) throws IOException {
            throw new IllegalStateException(getClass().getSimpleName() + " does not implement read().");
        }

        public void write(JsonWriter jsonWriter, Session session) throws IOException {
            jsonWriter.beginObject();
            jsonWriter.name(Session.SESSION_NUM).value((long) session.mSessionNum);
            jsonWriter.name(Session.SESSION_START).value(session.mSessionStart);
            jsonWriter.name(Session.PAST_SESSION_TIME).value(session.mPastSessionTime);
            jsonWriter.name(Session.MSG_ID).value(session.mMsgId);
            jsonWriter.name(Session.MSG_CAMPAIGN_ID).value(session.mMsgCampaignId);
            jsonWriter.name(Session.INSTALL_TS).value(session.mInstallTs);
            jsonWriter.name(Session.EVENTS);
            jsonWriter.setSerializeNulls(true);
            Streams.write(session.mEvents, jsonWriter);
            jsonWriter.setSerializeNulls(false);
            jsonWriter.endObject();
        }
    }

    public Session(DataStoreRecord dataStoreRecord, long j) {
        this.mEvents = new JsonArray();
        this.mSessionStart = dataStoreRecord.getSessionID();
        this.mInstallTs = j;
        this.mMsgId = dataStoreRecord.getMessageID();
        this.mMsgCampaignId = dataStoreRecord.getCampaignID();
        this.mPastSessionTime = dataStoreRecord.getPastSessionTime();
        this.mSessionNum = dataStoreRecord.getSessionNumber();
    }

    void addEvent(DataStoreRecord dataStoreRecord, JsonParser jsonParser) {
        String source = dataStoreRecord.getSource();
        if (!TextUtils.isEmpty(source)) {
            JsonElement parse = jsonParser.parse(source);
            if (parse != null && parse.isJsonObject()) {
                this.mEvents.add(parse.getAsJsonObject());
            }
        }
    }
}

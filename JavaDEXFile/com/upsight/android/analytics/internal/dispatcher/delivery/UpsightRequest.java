package com.upsight.android.analytics.internal.dispatcher.delivery;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.DataStoreRecord;
import com.upsight.android.analytics.internal.dispatcher.delivery.BatchSender.Request;
import com.upsight.android.analytics.internal.dispatcher.routing.Packet;
import com.upsight.android.analytics.internal.dispatcher.schema.Schema;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.analytics.provider.UpsightOptOutStatus;
import com.upsight.android.internal.util.PreferencesHelper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@JsonAdapter(DefaultTypeAdapter.class)
class UpsightRequest {
    private long mInstallTs;
    private final JsonParser mJsonParser;
    private boolean mOptOut;
    private long mRequestTs;
    private Schema mSchema;
    private Session[] mSessions;
    private UpsightContext mUpsight;

    public static final class DefaultTypeAdapter extends TypeAdapter<UpsightRequest> {
        private static final Gson GSON;
        private static final String IDENTIFIERS_KEY = "identifiers";
        private static final String LOCALE_KEY = "locale";
        private static final String OPT_OUT_KEY = "opt_out";
        private static final String REQUEST_TS_KEY = "request_ts";
        private static final String SESSIONS_KEY = "sessions";
        private static final TypeAdapter<Session> SESSION_TYPE_ADAPTER;

        static {
            GSON = new Gson();
            SESSION_TYPE_ADAPTER = new DefaultTypeAdapter();
        }

        public UpsightRequest read(JsonReader jsonReader) throws IOException {
            throw new IOException(UpsightRequest.class.getSimpleName() + " cannot be deserialized");
        }

        public void write(JsonWriter jsonWriter, UpsightRequest upsightRequest) throws IOException {
            String name;
            jsonWriter.beginObject();
            for (String name2 : upsightRequest.mSchema.availableKeys()) {
                Object valueFor = upsightRequest.mSchema.getValueFor(name2);
                if (valueFor != null) {
                    jsonWriter.name(name2);
                    Streams.write(GSON.toJsonTree(valueFor), jsonWriter);
                }
            }
            jsonWriter.name(REQUEST_TS_KEY);
            jsonWriter.value(upsightRequest.mRequestTs);
            jsonWriter.name(OPT_OUT_KEY);
            jsonWriter.value(upsightRequest.mOptOut);
            Schema access$000 = upsightRequest.mSchema;
            if (access$000 != null) {
                name2 = access$000.getName();
                if (!TextUtils.isEmpty(name2)) {
                    jsonWriter.name(IDENTIFIERS_KEY);
                    jsonWriter.value(name2);
                }
            }
            Locale locale = Locale.getDefault();
            if (locale != null) {
                name2 = locale.toString();
                if (!TextUtils.isEmpty(name2)) {
                    jsonWriter.name(LOCALE_KEY);
                    jsonWriter.value(name2);
                }
            }
            jsonWriter.name(SESSIONS_KEY);
            jsonWriter.beginArray();
            for (Object write : upsightRequest.mSessions) {
                SESSION_TYPE_ADAPTER.write(jsonWriter, write);
            }
            jsonWriter.endArray();
            jsonWriter.endObject();
        }
    }

    public UpsightRequest(UpsightContext upsightContext, Request request, JsonParser jsonParser, Clock clock) {
        this.mUpsight = upsightContext;
        this.mJsonParser = jsonParser;
        this.mInstallTs = PreferencesHelper.getLong(upsightContext, PreferencesHelper.INSTALL_TIMESTAMP_NAME, 0);
        this.mSessions = getSessions(request.batch);
        this.mOptOut = UpsightOptOutStatus.get(this.mUpsight);
        this.mRequestTs = clock.currentTimeSeconds();
        this.mSchema = request.schema;
    }

    private Session[] getSessions(Batch batch) {
        Map hashMap = new HashMap();
        for (Packet record : batch.getPackets()) {
            DataStoreRecord record2 = record.getRecord();
            Session session = (Session) hashMap.get(Long.valueOf(record2.getSessionID()));
            if (session == null) {
                session = new Session(record2, this.mInstallTs);
                hashMap.put(Long.valueOf(record2.getSessionID()), session);
            }
            session.addEvent(record2, this.mJsonParser);
        }
        return (Session[]) hashMap.values().toArray(new Session[hashMap.values().size()]);
    }
}

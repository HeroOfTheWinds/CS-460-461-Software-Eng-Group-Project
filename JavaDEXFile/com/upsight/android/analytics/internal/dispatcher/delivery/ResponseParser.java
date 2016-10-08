package com.upsight.android.analytics.internal.dispatcher.delivery;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.dispatcher.EndpointResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

class ResponseParser {
    private Gson mGson;

    public static class Response {
        public final String error;
        public final Collection<EndpointResponse> responses;

        public Response(Collection<EndpointResponse> collection, String str) {
            this.responses = collection;
            this.error = str;
        }
    }

    public static class ResponseElementJson {
        @SerializedName("content")
        @Expose
        public JsonElement content;
        @SerializedName("type")
        @Expose
        public String type;
    }

    public static class ResponseJson {
        @SerializedName("error")
        @Expose
        public String error;
        @SerializedName("response")
        @Expose
        public List<ResponseElementJson> response;
    }

    @Inject
    public ResponseParser(@Named("config-gson") Gson gson) {
        this.mGson = gson;
    }

    public Response parse(String str) throws IOException {
        Response response;
        synchronized (this) {
            try {
                ResponseJson responseJson = (ResponseJson) this.mGson.fromJson(str, ResponseJson.class);
                Collection linkedList = new LinkedList();
                if (responseJson.response != null) {
                    for (ResponseElementJson responseElementJson : responseJson.response) {
                        linkedList.add(EndpointResponse.create(responseElementJson.type, responseElementJson.content.toString()));
                    }
                }
                response = new Response(linkedList, responseJson.error);
            } catch (Throwable e) {
                throw new IOException(e);
            }
        }
        return response;
    }
}

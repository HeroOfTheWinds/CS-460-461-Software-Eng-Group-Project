package com.upsight.android.analytics.internal.dispatcher;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.dispatcher.delivery.Queue.Trash;
import com.upsight.android.analytics.internal.dispatcher.delivery.QueueBuilder;
import com.upsight.android.analytics.internal.dispatcher.delivery.QueueConfig;
import com.upsight.android.analytics.internal.dispatcher.routing.RoutingConfig;
import com.upsight.android.analytics.internal.dispatcher.schema.IdentifierConfig;
import com.upsight.android.analytics.internal.dispatcher.schema.IdentifierConfig.Builder;
import com.upsight.android.logger.UpsightLogger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
class ConfigParser {
    private static final String LOG_TAG = "Dispatcher";
    private Gson mGson;
    private UpsightLogger mLogger;

    public static class Config {
        @SerializedName("identifier_filters")
        @Expose
        public List<IdentifierFilter> identifierFilters;
        @SerializedName("identifiers")
        @Expose
        public List<Identifier> identifiers;
        @SerializedName("queues")
        @Expose
        public List<Queue> queues;
        @SerializedName("route_filters")
        @Expose
        public List<RouteFilter> routeFilters;
    }

    public static class Identifier {
        @SerializedName("ids")
        @Expose
        public List<String> ids;
        @SerializedName("name")
        @Expose
        public String name;
    }

    public static class IdentifierFilter {
        @SerializedName("filter")
        @Expose
        public String filter;
        @SerializedName("identifiers")
        @Expose
        public String identifiers;
    }

    public static class Queue {
        @SerializedName("count_network_fail_retries")
        @Expose
        public boolean countNetworkFail;
        @SerializedName("host")
        @Expose
        public String host;
        @SerializedName("max_age")
        @Expose
        public int maxAge;
        @SerializedName("max_size")
        @Expose
        public int maxSize;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("protocol")
        @Expose
        public String protocol;
        @SerializedName("max_retry_count")
        @Expose
        public int retryCount;
        @SerializedName("retry_interval")
        @Expose
        public int retryInterval;
        @SerializedName("url_fmt")
        @Expose
        public String urlFormat;

        public String formatEndpointAddress() {
            return this.urlFormat.replace(QueueBuilder.MACRO_PROTOCOL, this.protocol).replace(QueueBuilder.MACRO_HOST, this.host);
        }
    }

    public static class RouteFilter {
        @SerializedName("filter")
        @Expose
        public String filter;
        @SerializedName("queues")
        @Expose
        public List<String> queues;
    }

    @Inject
    public ConfigParser(UpsightContext upsightContext, @Named("config-gson") Gson gson) {
        this.mGson = gson;
        this.mLogger = upsightContext.getLogger();
    }

    private IdentifierConfig parseIdentifierConfig(Config config) {
        Builder builder = new Builder();
        if (config.identifiers != null) {
            for (Identifier identifier : config.identifiers) {
                builder.addIdentifiers(identifier.name, new HashSet(identifier.ids));
            }
            for (IdentifierFilter identifierFilter : config.identifierFilters) {
                builder.addIdentifierFilter(identifierFilter.filter, identifierFilter.identifiers);
            }
        }
        return builder.build();
    }

    private QueueConfig parseQueueConfig(Queue queue) {
        return new QueueConfig.Builder().setEndpointAddress(queue.formatEndpointAddress()).setBatchSenderConfig(new com.upsight.android.analytics.internal.dispatcher.delivery.BatchSender.Config(queue.countNetworkFail, queue.retryInterval, queue.retryCount)).setBatcherConfig(new com.upsight.android.analytics.internal.dispatcher.delivery.Batcher.Config(queue.maxSize, queue.maxAge)).build();
    }

    private RoutingConfig parseRoutingConfig(Config config) {
        RoutingConfig.Builder builder = new RoutingConfig.Builder();
        if (config.queues != null) {
            List arrayList = new ArrayList();
            for (Queue queue : config.queues) {
                builder.addQueueConfig(queue.name, parseQueueConfig(queue));
                arrayList.add(queue.name);
            }
            for (RouteFilter routeFilter : config.routeFilters) {
                List arrayList2 = new ArrayList();
                for (String str : routeFilter.queues) {
                    if (arrayList.contains(str) || Trash.NAME.equals(str)) {
                        arrayList2.add(str);
                    } else {
                        this.mLogger.m213w(LOG_TAG, "Dispatcher ignored a route to a non-existent queue: " + str + " in the dispatcher configuration", new Object[0]);
                    }
                }
                if (arrayList2.size() > 0) {
                    builder.addRoute(routeFilter.filter, arrayList2);
                }
            }
        }
        return builder.build();
    }

    public Config parseConfig(String str) throws IOException {
        try {
            Config config = (Config) this.mGson.fromJson(str, Config.class);
            return new Config.Builder().setRoutingConfig(parseRoutingConfig(config)).setIdentifierConfig(parseIdentifierConfig(config)).build();
        } catch (Throwable e) {
            throw new IOException(e);
        }
    }
}

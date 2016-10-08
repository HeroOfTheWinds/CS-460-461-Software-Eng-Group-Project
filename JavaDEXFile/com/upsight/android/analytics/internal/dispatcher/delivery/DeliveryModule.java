package com.upsight.android.analytics.internal.dispatcher.delivery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.logger.UpsightLogger;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import rx.Scheduler;

@Module
public final class DeliveryModule {
    private static final boolean USE_PRETTY_JSON_LOGGING = false;

    @Singleton
    @Provides
    public QueueBuilder provideQueueBuilder(UpsightContext upsightContext, Clock clock, @Named("dispatcher-threadpool") Scheduler scheduler, @Named("dispatcher-batching") Scheduler scheduler2, SignatureVerifier signatureVerifier, Provider<ResponseParser> provider) {
        Gson gson = upsightContext.getCoreComponent().gson();
        JsonParser jsonParser = upsightContext.getCoreComponent().jsonParser();
        UpsightLogger logger = upsightContext.getLogger();
        return new QueueBuilder(upsightContext, gson, new GsonBuilder().create(), jsonParser, clock, logger, scheduler, scheduler2, signatureVerifier, provider);
    }

    @Singleton
    @Provides
    public SignatureVerifier provideResponseVerifier(UpsightContext upsightContext) {
        return new BouncySignatureVerifier(upsightContext);
    }
}

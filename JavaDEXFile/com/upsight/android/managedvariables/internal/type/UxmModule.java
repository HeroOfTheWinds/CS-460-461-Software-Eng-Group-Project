package com.upsight.android.managedvariables.internal.type;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.squareup.otto.Bus;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightCoreComponent;
import com.upsight.android.analytics.UpsightAnalyticsComponent;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.managedvariables.experience.UpsightUserExperience;
import com.upsight.android.managedvariables.internal.type.UxmContentActions.UxmContentActionContext;
import dagger.Module;
import dagger.Provides;
import java.io.InputStream;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.commons.io.IOUtils;
import rx.Scheduler;
import spacemadness.com.lunarconsole.BuildConfig;

@Module
public class UxmModule {
    public static final String GSON_UXM_SCHEMA = "gsonUxmSchema";
    public static final String JSON_PARSER_UXM_SCHEMA = "jsonParserUxmSchema";
    public static final String STRING_RAW_UXM_SCHEMA = "stringRawUxmSchema";

    @Singleton
    @Provides
    ManagedVariableManager provideManagedVariableManager(UpsightContext upsightContext, @Named("main") Scheduler scheduler, UxmSchema uxmSchema) {
        return new ManagedVariableManager(scheduler, upsightContext.getDataStore(), uxmSchema);
    }

    @Singleton
    @Provides
    UxmBlockProvider provideUxmBlockProvider(UpsightContext upsightContext, @Named("stringRawUxmSchema") String str, UxmSchema uxmSchema) {
        return new UxmBlockProvider(upsightContext, str, uxmSchema);
    }

    @Singleton
    @Provides
    UxmContentFactory provideUxmContentFactory(UpsightContext upsightContext, @Named("main") Scheduler scheduler, UpsightUserExperience upsightUserExperience) {
        Gson gson;
        Clock clock;
        Bus bus = null;
        UpsightCoreComponent coreComponent = upsightContext.getCoreComponent();
        UpsightAnalyticsExtension upsightAnalyticsExtension = (UpsightAnalyticsExtension) upsightContext.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        UpsightLogger logger = upsightContext.getLogger();
        if (coreComponent == null || upsightAnalyticsExtension == null) {
            gson = null;
            clock = null;
        } else {
            bus = coreComponent.bus();
            gson = coreComponent.gson();
            clock = ((UpsightAnalyticsComponent) upsightAnalyticsExtension.getComponent()).clock();
        }
        return new UxmContentFactory(new UxmContentActionContext(upsightContext, bus, gson, clock, scheduler.createWorker(), logger), upsightUserExperience);
    }

    @Singleton
    @Provides
    UxmSchema provideUxmSchema(UpsightContext upsightContext, @Named("gsonUxmSchema") Gson gson, @Named("jsonParserUxmSchema") JsonParser jsonParser, @Named("stringRawUxmSchema") String str) {
        UpsightLogger logger = upsightContext.getLogger();
        UxmSchema uxmSchema = null;
        if (!TextUtils.isEmpty(str)) {
            try {
                uxmSchema = UxmSchema.create(str, gson, jsonParser, logger);
            } catch (Throwable e) {
                logger.m208e(Upsight.LOG_TAG, e, "Failed to parse UXM schema", new Object[0]);
            }
        }
        if (uxmSchema != null) {
            return uxmSchema;
        }
        uxmSchema = new UxmSchema(logger);
        logger.m205d(Upsight.LOG_TAG, "Empty UXM schema used", new Object[0]);
        return uxmSchema;
    }

    @Singleton
    @Provides
    @Named("gsonUxmSchema")
    Gson provideUxmSchemaGson(UpsightContext upsightContext) {
        return upsightContext.getCoreComponent().gson();
    }

    @Singleton
    @Provides
    @Named("jsonParserUxmSchema")
    JsonParser provideUxmSchemaJsonParser(UpsightContext upsightContext) {
        return upsightContext.getCoreComponent().jsonParser();
    }

    @Singleton
    @Provides
    @Named("stringRawUxmSchema")
    String provideUxmSchemaRawString(UpsightContext upsightContext, @Named("resUxmSchema") Integer num) {
        UpsightLogger logger = upsightContext.getLogger();
        try {
            InputStream openRawResource = upsightContext.getResources().openRawResource(num.intValue());
            if (openRawResource != null) {
                return IOUtils.toString(openRawResource);
            }
            logger.m207e(Upsight.LOG_TAG, "Failed to find UXM schema file", new Object[0]);
            return BuildConfig.FLAVOR;
        } catch (Throwable e) {
            logger.m208e(Upsight.LOG_TAG, e, "Failed to read UXM schema file", new Object[0]);
            return BuildConfig.FLAVOR;
        }
    }
}

package com.upsight.android;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.upsight.android.analytics.dispatcher.EndpointResponse;
import com.upsight.android.analytics.internal.action.ActionMapResponse;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.marketing.UpsightContentMediator;
import com.upsight.android.marketing.UpsightMarketingApi;
import com.upsight.android.marketing.UpsightMarketingComponent;
import com.upsight.android.marketing.internal.BaseMarketingModule;
import com.upsight.android.marketing.internal.DaggerMarketingComponent;
import com.upsight.android.marketing.internal.content.DefaultContentMediator;
import com.upsight.android.marketing.internal.content.MarketingContent;
import com.upsight.android.marketing.internal.content.MarketingContentActions.MarketingContentActionFactory;
import com.upsight.android.marketing.internal.content.MarketingContentFactory;
import com.upsight.android.persistence.annotation.Created;
import javax.inject.Inject;

public class UpsightMarketingExtension extends UpsightExtension<UpsightMarketingComponent, UpsightMarketingApi> {
    public static final String EXTENSION_NAME = "com.upsight.extension.marketing";
    private static final String UPSIGHT_ACTION_MAP = "upsight.action_map";
    @Inject
    DefaultContentMediator mDefaultContentMediator;
    private Gson mGson;
    private JsonParser mJsonParser;
    private UpsightLogger mLogger;
    @Inject
    UpsightMarketingApi mMarketing;
    @Inject
    MarketingContentFactory mMarketingContentFactory;

    UpsightMarketingExtension() {
    }

    public UpsightMarketingApi getApi() {
        return this.mMarketing;
    }

    protected void onCreate(UpsightContext upsightContext) {
        this.mGson = upsightContext.getCoreComponent().gson();
        this.mJsonParser = upsightContext.getCoreComponent().jsonParser();
        this.mLogger = upsightContext.getLogger();
        UpsightContentMediator.register(upsightContext, this.mDefaultContentMediator);
        upsightContext.getDataStore().subscribe(this);
    }

    protected UpsightMarketingComponent onResolve(UpsightContext upsightContext) {
        return DaggerMarketingComponent.builder().baseMarketingModule(new BaseMarketingModule(upsightContext)).build();
    }

    @Created
    public void onResponse(EndpointResponse endpointResponse) {
        if (UPSIGHT_ACTION_MAP.equals(endpointResponse.getType())) {
            try {
                ActionMapResponse actionMapResponse = (ActionMapResponse) this.mGson.fromJson(this.mJsonParser.parse(endpointResponse.getContent()), ActionMapResponse.class);
                if (MarketingContentActionFactory.TYPE.equals(actionMapResponse.getActionFactory())) {
                    MarketingContent create = this.mMarketingContentFactory.create(actionMapResponse);
                    if (create != null) {
                        create.executeActions(MarketingContent.TRIGGER_CONTENT_RECEIVED);
                    }
                }
            } catch (JsonSyntaxException e) {
                this.mLogger.m213w(Upsight.LOG_TAG, "Unable to parse action map", e);
            }
        }
    }
}

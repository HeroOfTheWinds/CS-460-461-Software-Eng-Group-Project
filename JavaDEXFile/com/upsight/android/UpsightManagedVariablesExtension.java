package com.upsight.android;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.upsight.android.analytics.dispatcher.EndpointResponse;
import com.upsight.android.analytics.internal.action.ActionMapResponse;
import com.upsight.android.analytics.provider.UpsightDataProvider;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.managedvariables.UpsightManagedVariablesApi;
import com.upsight.android.managedvariables.UpsightManagedVariablesComponent;
import com.upsight.android.managedvariables.internal.BaseManagedVariablesModule;
import com.upsight.android.managedvariables.internal.DaggerManagedVariablesComponent;
import com.upsight.android.managedvariables.internal.type.UxmBlockProvider;
import com.upsight.android.managedvariables.internal.type.UxmContent;
import com.upsight.android.managedvariables.internal.type.UxmContentActions.UxmContentActionFactory;
import com.upsight.android.managedvariables.internal.type.UxmContentFactory;
import com.upsight.android.marketing.internal.content.MarketingContent;
import com.upsight.android.persistence.annotation.Created;
import javax.inject.Inject;

public class UpsightManagedVariablesExtension extends UpsightExtension<UpsightManagedVariablesComponent, UpsightManagedVariablesApi> {
    public static final String EXTENSION_NAME = "com.upsight.extension.managedvariables";
    private static final String UPSIGHT_ACTION_MAP = "upsight.action_map";
    private Gson mGson;
    private JsonParser mJsonParser;
    private UpsightLogger mLogger;
    @Inject
    UpsightManagedVariablesApi mManagedVariables;
    @Inject
    UxmBlockProvider mUxmBlockProvider;
    @Inject
    UxmContentFactory mUxmContentFactory;

    UpsightManagedVariablesExtension() {
    }

    public UpsightManagedVariablesApi getApi() {
        return this.mManagedVariables;
    }

    protected void onCreate(UpsightContext upsightContext) {
        this.mGson = upsightContext.getCoreComponent().gson();
        this.mJsonParser = upsightContext.getCoreComponent().jsonParser();
        this.mLogger = upsightContext.getLogger();
        UpsightDataProvider.register(upsightContext, this.mUxmBlockProvider);
        upsightContext.getDataStore().subscribe(this);
    }

    protected UpsightManagedVariablesComponent onResolve(UpsightContext upsightContext) {
        return DaggerManagedVariablesComponent.builder().baseManagedVariablesModule(new BaseManagedVariablesModule(upsightContext)).build();
    }

    @Created
    public void onResponse(EndpointResponse endpointResponse) {
        if (UPSIGHT_ACTION_MAP.equals(endpointResponse.getType())) {
            try {
                ActionMapResponse actionMapResponse = (ActionMapResponse) this.mGson.fromJson(this.mJsonParser.parse(endpointResponse.getContent()), ActionMapResponse.class);
                if (UxmContentActionFactory.TYPE.equals(actionMapResponse.getActionFactory())) {
                    UxmContent create = this.mUxmContentFactory.create(actionMapResponse);
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

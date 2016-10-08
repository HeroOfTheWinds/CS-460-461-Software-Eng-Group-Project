.class public Lcom/upsight/mediation/ads/adapters/VastAdAdapter;
.super Lcom/upsight/mediation/ads/adapters/NetworkWrapperFuseInternal;
.source "VastAdAdapter.java"

# interfaces
.implements Lcom/upsight/mediation/vast/VASTPlayer$VASTPlayerListener;


# static fields
.field public static final NAME:Ljava/lang/String; = "VAST"

.field private static final TAG:Ljava/lang/String; = "VastAdAdapter"


# instance fields
.field private activity:Landroid/app/Activity;

.field private interstitial:Lcom/upsight/mediation/vast/VASTPlayer;

.field private isRewarded:Z


# direct methods
.method public constructor <init>()V
    .locals 1

    .prologue
    .line 18
    invoke-direct {p0}, Lcom/upsight/mediation/ads/adapters/NetworkWrapperFuseInternal;-><init>()V

    .line 26
    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->isRewarded:Z

    return-void
.end method


# virtual methods
.method public displayAd()V
    .locals 1

    .prologue
    .line 124
    invoke-virtual {p0}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->isAdAvailable()Z

    move-result v0

    if-nez v0, :cond_0

    .line 125
    invoke-virtual {p0}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->onAdFailedToDisplay()V

    .line 130
    :goto_0
    return-void

    .line 129
    :cond_0
    iget-object v0, p0, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->interstitial:Lcom/upsight/mediation/vast/VASTPlayer;

    invoke-virtual {v0}, Lcom/upsight/mediation/vast/VASTPlayer;->play()V

    goto :goto_0
.end method

.method public getName()Ljava/lang/String;
    .locals 2

    .prologue
    .line 36
    new-instance v0, Ljava/lang/StringBuilder;

    const-string v1, "VAST"

    invoke-direct {v0, v1}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    const-string v1, ": "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {p0}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->getID()I

    move-result v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    return-object v0
.end method

.method public init()V
    .locals 0
    .annotation build Landroid/support/annotation/CallSuper;
    .end annotation

    .prologue
    .line 32
    return-void
.end method

.method public isAdAvailable()Z
    .locals 2

    .prologue
    .line 118
    iget-object v1, p0, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->interstitial:Lcom/upsight/mediation/vast/VASTPlayer;

    if-eqz v1, :cond_0

    iget-object v1, p0, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->interstitial:Lcom/upsight/mediation/vast/VASTPlayer;

    invoke-virtual {v1}, Lcom/upsight/mediation/vast/VASTPlayer;->isLoaded()Z

    move-result v1

    if-eqz v1, :cond_0

    const/4 v0, 0x1

    .line 119
    .local v0, "isAvailable":Z
    :goto_0
    return v0

    .line 118
    .end local v0    # "isAvailable":Z
    :cond_0
    const/4 v0, 0x0

    goto :goto_0
.end method

.method public loadAd(Landroid/app/Activity;Ljava/util/HashMap;)V
    .locals 20
    .param p1, "activity"    # Landroid/app/Activity;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param
    .param p2    # Ljava/util/HashMap;
        .annotation build Landroid/support/annotation/NonNull;
        .end annotation
    .end param
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Landroid/app/Activity;",
            "Ljava/util/HashMap",
            "<",
            "Ljava/lang/String;",
            "Ljava/lang/String;",
            ">;)V"
        }
    .end annotation

    .prologue
    .line 55
    .local p2, "parameters":Ljava/util/HashMap;, "Ljava/util/HashMap<Ljava/lang/String;Ljava/lang/String;>;"
    const-string v3, "isRewarded"

    move-object/from16 v0, p2

    invoke-virtual {v0, v3}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/lang/String;

    invoke-static {v3}, Ljava/lang/Boolean;->parseBoolean(Ljava/lang/String;)Z

    move-result v3

    move-object/from16 v0, p0

    iput-boolean v3, v0, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->isRewarded:Z

    .line 57
    const-string v3, "cta"

    move-object/from16 v0, p2

    invoke-virtual {v0, v3}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v13

    check-cast v13, Ljava/lang/String;

    .line 58
    .local v13, "callToAction":Ljava/lang/String;
    if-eqz v13, :cond_0

    invoke-virtual {v13}, Ljava/lang/String;->length()I

    move-result v3

    if-nez v3, :cond_1

    .line 59
    :cond_0
    const-string v13, "Learn More"

    .line 62
    :cond_1
    const-string v3, "maxFileSize"

    move-object/from16 v0, p2

    invoke-virtual {v0, v3}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v11

    check-cast v11, Ljava/lang/String;

    .line 63
    .local v11, "maxVastFileSize":Ljava/lang/String;
    invoke-static {v11}, Lcom/upsight/mediation/util/StringUtil;->isNullOrEmpty(Ljava/lang/String;)Z

    move-result v3

    if-eqz v3, :cond_2

    .line 64
    sget-object v3, Lcom/upsight/mediation/ads/model/AdapterLoadError;->INVALID_PARAMETERS:Lcom/upsight/mediation/ads/model/AdapterLoadError;

    move-object/from16 v0, p0

    invoke-virtual {v0, v3}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->onAdFailedToLoad(Lcom/upsight/mediation/ads/model/AdapterLoadError;)V

    .line 113
    :goto_0
    return-void

    .line 68
    :cond_2
    const-string v3, "closeButtonDelay"

    move-object/from16 v0, p2

    invoke-virtual {v0, v3}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/String;

    .line 70
    .local v2, "cbms":Ljava/lang/String;
    :try_start_0
    invoke-static {v2}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I
    :try_end_0
    .catch Ljava/lang/NumberFormatException; {:try_start_0 .. :try_end_0} :catch_1
    .catch Ljava/lang/NullPointerException; {:try_start_0 .. :try_end_0} :catch_2

    move-result v15

    .line 76
    .local v15, "closeButtonDelay":I
    :goto_1
    const-string v3, "postroll"

    move-object/from16 v0, p2

    invoke-virtual {v0, v3}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v17

    check-cast v17, Ljava/lang/String;

    .line 77
    .local v17, "pr":Ljava/lang/String;
    if-eqz v17, :cond_4

    const-string v3, "1"

    move-object/from16 v0, v17

    invoke-virtual {v0, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v3

    if-eqz v3, :cond_4

    const/4 v6, 0x1

    .line 79
    .local v6, "postroll":Z
    :goto_2
    const-string v3, "shouldValidateSchema"

    move-object/from16 v0, p2

    invoke-virtual {v0, v3}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v18

    check-cast v18, Ljava/lang/String;

    .line 80
    .local v18, "svs":Ljava/lang/String;
    if-eqz v18, :cond_5

    invoke-static/range {v18 .. v18}, Ljava/lang/Boolean;->parseBoolean(Ljava/lang/String;)Z

    move-result v3

    if-eqz v3, :cond_5

    const/4 v12, 0x1

    .line 82
    .local v12, "shouldValidateSchema":Z
    :goto_3
    const-string v3, "endcard_script"

    move-object/from16 v0, p2

    invoke-virtual {v0, v3}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/lang/String;

    .line 84
    .local v7, "endCardHtml":Ljava/lang/String;
    const/4 v14, -0x1

    .line 86
    .local v14, "downloadTimeout":I
    :try_start_1
    const-string v3, "vast_cache_to"

    move-object/from16 v0, p2

    invoke-virtual {v0, v3}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/lang/String;

    invoke-static {v3}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I
    :try_end_1
    .catch Ljava/lang/NumberFormatException; {:try_start_1 .. :try_end_1} :catch_3

    move-result v14

    .line 90
    :goto_4
    const-string v3, "script"

    move-object/from16 v0, p2

    invoke-virtual {v0, v3}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v19

    check-cast v19, Ljava/lang/String;

    .line 92
    .local v19, "vastScript":Ljava/lang/String;
    if-eqz v19, :cond_3

    .line 93
    :try_start_2
    new-instance v3, Lcom/upsight/mediation/vast/VASTPlayer;

    int-to-long v8, v15

    move-object/from16 v0, p0

    iget-boolean v10, v0, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->isRewarded:Z

    move-object/from16 v4, p1

    move-object/from16 v5, p0

    invoke-direct/range {v3 .. v14}, Lcom/upsight/mediation/vast/VASTPlayer;-><init>(Landroid/content/Context;Lcom/upsight/mediation/vast/VASTPlayer$VASTPlayerListener;ZLjava/lang/String;JZLjava/lang/String;ZLjava/lang/String;I)V

    move-object/from16 v0, p0

    iput-object v3, v0, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->interstitial:Lcom/upsight/mediation/vast/VASTPlayer;

    .line 102
    invoke-static/range {v19 .. v19}, Landroid/webkit/URLUtil;->isValidUrl(Ljava/lang/String;)Z

    move-result v3

    if-eqz v3, :cond_6

    .line 103
    move-object/from16 v0, p0

    iget-object v3, v0, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->interstitial:Lcom/upsight/mediation/vast/VASTPlayer;

    move-object/from16 v0, v19

    invoke-virtual {v3, v0}, Lcom/upsight/mediation/vast/VASTPlayer;->loadVastResponseViaURL(Ljava/lang/String;)V
    :try_end_2
    .catch Ljava/lang/Exception; {:try_start_2 .. :try_end_2} :catch_0

    goto :goto_0

    .line 109
    :catch_0
    move-exception v16

    .line 110
    .local v16, "ex":Ljava/lang/Exception;
    const-string v3, "Vast failed to load to to unexpected error"

    move-object/from16 v0, p0

    move-object/from16 v1, v16

    invoke-virtual {v0, v3, v1}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->logError(Ljava/lang/String;Ljava/lang/Throwable;)V

    .line 112
    .end local v16    # "ex":Ljava/lang/Exception;
    :cond_3
    sget-object v3, Lcom/upsight/mediation/ads/model/AdapterLoadError;->PROVIDER_ADAPTER_ERROR:Lcom/upsight/mediation/ads/model/AdapterLoadError;

    move-object/from16 v0, p0

    invoke-virtual {v0, v3}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->onAdFailedToLoad(Lcom/upsight/mediation/ads/model/AdapterLoadError;)V

    goto/16 :goto_0

    .line 71
    .end local v6    # "postroll":Z
    .end local v7    # "endCardHtml":Ljava/lang/String;
    .end local v12    # "shouldValidateSchema":Z
    .end local v14    # "downloadTimeout":I
    .end local v15    # "closeButtonDelay":I
    .end local v17    # "pr":Ljava/lang/String;
    .end local v18    # "svs":Ljava/lang/String;
    .end local v19    # "vastScript":Ljava/lang/String;
    :catch_1
    move-exception v16

    .line 72
    .local v16, "ex":Ljava/lang/RuntimeException;
    :goto_5
    const/4 v15, -0x1

    .line 73
    .restart local v15    # "closeButtonDelay":I
    const-string v3, "Could not parse close button delay"

    move-object/from16 v0, p0

    invoke-virtual {v0, v3}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->log(Ljava/lang/String;)V

    goto/16 :goto_1

    .line 77
    .end local v16    # "ex":Ljava/lang/RuntimeException;
    .restart local v17    # "pr":Ljava/lang/String;
    :cond_4
    const/4 v6, 0x0

    goto :goto_2

    .line 80
    .restart local v6    # "postroll":Z
    .restart local v18    # "svs":Ljava/lang/String;
    :cond_5
    const/4 v12, 0x0

    goto :goto_3

    .line 105
    .restart local v7    # "endCardHtml":Ljava/lang/String;
    .restart local v12    # "shouldValidateSchema":Z
    .restart local v14    # "downloadTimeout":I
    .restart local v19    # "vastScript":Ljava/lang/String;
    :cond_6
    :try_start_3
    move-object/from16 v0, p0

    iget-object v3, v0, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->interstitial:Lcom/upsight/mediation/vast/VASTPlayer;

    move-object/from16 v0, v19

    invoke-virtual {v3, v0}, Lcom/upsight/mediation/vast/VASTPlayer;->loadVastResponseViaXML(Ljava/lang/String;)V
    :try_end_3
    .catch Ljava/lang/Exception; {:try_start_3 .. :try_end_3} :catch_0

    goto/16 :goto_0

    .line 71
    .end local v6    # "postroll":Z
    .end local v7    # "endCardHtml":Ljava/lang/String;
    .end local v12    # "shouldValidateSchema":Z
    .end local v14    # "downloadTimeout":I
    .end local v15    # "closeButtonDelay":I
    .end local v17    # "pr":Ljava/lang/String;
    .end local v18    # "svs":Ljava/lang/String;
    .end local v19    # "vastScript":Ljava/lang/String;
    :catch_2
    move-exception v16

    goto :goto_5

    .line 87
    .restart local v6    # "postroll":Z
    .restart local v7    # "endCardHtml":Ljava/lang/String;
    .restart local v12    # "shouldValidateSchema":Z
    .restart local v14    # "downloadTimeout":I
    .restart local v15    # "closeButtonDelay":I
    .restart local v17    # "pr":Ljava/lang/String;
    .restart local v18    # "svs":Ljava/lang/String;
    :catch_3
    move-exception v3

    goto :goto_4
.end method

.method public vastClick()V
    .locals 0

    .prologue
    .line 181
    invoke-virtual {p0}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->onAdClicked()V

    .line 182
    return-void
.end method

.method public vastComplete()V
    .locals 0

    .prologue
    .line 186
    invoke-virtual {p0}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->onAdCompleted()V

    .line 187
    return-void
.end method

.method public vastDismiss()V
    .locals 0

    .prologue
    .line 191
    invoke-virtual {p0}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->onAdClosed()V

    .line 192
    return-void
.end method

.method public vastDisplay()V
    .locals 0

    .prologue
    .line 176
    invoke-virtual {p0}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->onAdDisplayed()V

    .line 177
    return-void
.end method

.method public vastError(I)V
    .locals 2
    .param p1, "error"    # I

    .prologue
    .line 141
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "Error: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->log(Ljava/lang/String;)V

    .line 142
    invoke-virtual {p0, p1}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->onVastError(I)V

    .line 144
    sparse-switch p1, :sswitch_data_0

    .line 170
    sget-object v0, Lcom/upsight/mediation/ads/model/AdapterLoadError;->PROVIDER_UNRECOGNIZED:Lcom/upsight/mediation/ads/model/AdapterLoadError;

    invoke-virtual {p0, v0}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->onAdFailedToLoad(Lcom/upsight/mediation/ads/model/AdapterLoadError;)V

    .line 172
    :goto_0
    return-void

    .line 146
    :sswitch_0
    invoke-virtual {p0}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->onAdFailedToDisplay()V

    goto :goto_0

    .line 149
    :sswitch_1
    sget-object v0, Lcom/upsight/mediation/ads/model/AdapterLoadError;->PROVIDER_UNDEFINED:Lcom/upsight/mediation/ads/model/AdapterLoadError;

    invoke-virtual {p0, v0}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->onAdFailedToLoad(Lcom/upsight/mediation/ads/model/AdapterLoadError;)V

    goto :goto_0

    .line 153
    :sswitch_2
    sget-object v0, Lcom/upsight/mediation/ads/model/AdapterLoadError;->PROVIDER_TIMED_OUT:Lcom/upsight/mediation/ads/model/AdapterLoadError;

    invoke-virtual {p0, v0}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->onAdFailedToLoad(Lcom/upsight/mediation/ads/model/AdapterLoadError;)V

    goto :goto_0

    .line 158
    :sswitch_3
    sget-object v0, Lcom/upsight/mediation/ads/model/AdapterLoadError;->PROVIDER_LOAD_NOT_STARTED:Lcom/upsight/mediation/ads/model/AdapterLoadError;

    invoke-virtual {p0, v0}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->onAdFailedToLoad(Lcom/upsight/mediation/ads/model/AdapterLoadError;)V

    goto :goto_0

    .line 166
    :sswitch_4
    sget-object v0, Lcom/upsight/mediation/ads/model/AdapterLoadError;->PROVIDER_ADAPTER_ERROR:Lcom/upsight/mediation/ads/model/AdapterLoadError;

    invoke-virtual {p0, v0}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->onAdFailedToLoad(Lcom/upsight/mediation/ads/model/AdapterLoadError;)V

    goto :goto_0

    .line 144
    :sswitch_data_0
    .sparse-switch
        0x1 -> :sswitch_3
        0x64 -> :sswitch_4
        0x65 -> :sswitch_4
        0x66 -> :sswitch_4
        0x12c -> :sswitch_4
        0x12d -> :sswitch_2
        0x12e -> :sswitch_4
        0x12f -> :sswitch_1
        0x190 -> :sswitch_4
        0x191 -> :sswitch_3
        0x192 -> :sswitch_2
        0x193 -> :sswitch_3
        0x195 -> :sswitch_0
    .end sparse-switch
.end method

.method public vastProgress(I)V
    .locals 0
    .param p1, "progress"    # I

    .prologue
    .line 202
    invoke-virtual {p0, p1}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->onVastProgress(I)V

    .line 203
    return-void
.end method

.method public vastReady()V
    .locals 1

    .prologue
    .line 135
    const-string v0, "Ad loaded"

    invoke-virtual {p0, v0}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->log(Ljava/lang/String;)V

    .line 136
    invoke-virtual {p0}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->onAdLoaded()V

    .line 137
    return-void
.end method

.method public vastReplay()V
    .locals 0

    .prologue
    .line 212
    invoke-virtual {p0}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->onVastReplay()V

    .line 213
    return-void
.end method

.method public vastRewardedVideoComplete()V
    .locals 0

    .prologue
    .line 207
    invoke-virtual {p0}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->onRewardedVideoCompleted()V

    .line 208
    return-void
.end method

.method public vastSkip()V
    .locals 0

    .prologue
    .line 196
    invoke-virtual {p0}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->onAdSkipped()V

    .line 197
    invoke-virtual {p0}, Lcom/upsight/mediation/ads/adapters/VastAdAdapter;->onVastSkip()V

    .line 198
    return-void
.end method

.method public verifyParameters(Ljava/util/HashMap;)Z
    .locals 4
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/util/HashMap",
            "<",
            "Ljava/lang/String;",
            "Ljava/lang/String;",
            ">;)Z"
        }
    .end annotation

    .prologue
    .local p1, "params":Ljava/util/HashMap;, "Ljava/util/HashMap<Ljava/lang/String;Ljava/lang/String;>;"
    const/4 v2, 0x1

    const/4 v3, 0x0

    .line 41
    const-string v1, "maxFileSize"

    invoke-virtual {p1, v1}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/String;

    invoke-static {v1}, Lcom/upsight/mediation/util/StringUtil;->isNullOrEmpty(Ljava/lang/String;)Z

    move-result v0

    .line 42
    .local v0, "invalid":Z
    if-nez v0, :cond_0

    const-string v1, "script"

    invoke-virtual {p1, v1}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/String;

    invoke-static {v1}, Lcom/upsight/mediation/util/StringUtil;->isNullOrEmpty(Ljava/lang/String;)Z

    move-result v1

    if-eqz v1, :cond_1

    :cond_0
    move v0, v2

    .line 43
    :goto_0
    if-nez v0, :cond_2

    move v1, v2

    :goto_1
    return v1

    :cond_1
    move v0, v3

    .line 42
    goto :goto_0

    :cond_2
    move v1, v3

    .line 43
    goto :goto_1
.end method

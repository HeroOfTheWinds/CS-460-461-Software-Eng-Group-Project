.class public Lcom/upsight/mediation/ads/adapters/MRaidActivity;
.super Landroid/app/Activity;
.source "MRaidActivity.java"


# static fields
.field private static final TAG:Ljava/lang/String; = "MRaidActivity"


# instance fields
.field private firstResume:Z

.field private interstitial:Lcom/upsight/mediation/mraid/MRAIDInterstitial;

.field public isVisible:Z

.field private provider:Lcom/upsight/mediation/ads/adapters/MRaidAdAdapter;

.field private registryId:I

.field private rotateMode:I


# direct methods
.method public constructor <init>()V
    .locals 1

    .prologue
    .line 17
    invoke-direct {p0}, Landroid/app/Activity;-><init>()V

    .line 23
    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->firstResume:Z

    return-void
.end method

.method private getOrientationValue(I)I
    .locals 1
    .param p1, "rotateMode"    # I

    .prologue
    .line 63
    packed-switch p1, :pswitch_data_0

    .line 72
    const/4 v0, -0x1

    :goto_0
    return v0

    .line 65
    :pswitch_0
    const/16 v0, 0xe

    goto :goto_0

    .line 67
    :pswitch_1
    const/4 v0, 0x6

    goto :goto_0

    .line 69
    :pswitch_2
    const/4 v0, 0x7

    goto :goto_0

    .line 63
    :pswitch_data_0
    .packed-switch 0x2
        :pswitch_0
        :pswitch_1
        :pswitch_2
    .end packed-switch
.end method


# virtual methods
.method protected onActivityResult(IILandroid/content/Intent;)V
    .locals 2
    .param p1, "requestCode"    # I
    .param p2, "resultCode"    # I
    .param p3, "intent"    # Landroid/content/Intent;

    .prologue
    .line 121
    const/4 v0, 0x1

    if-ne p1, v0, :cond_1

    .line 122
    const/4 v0, -0x1

    if-ne p2, v0, :cond_2

    .line 123
    iget-object v0, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->provider:Lcom/upsight/mediation/ads/adapters/MRaidAdAdapter;

    invoke-virtual {v0}, Lcom/upsight/mediation/ads/adapters/MRaidAdAdapter;->onAdCompleted()V

    .line 128
    :cond_0
    :goto_0
    iget-object v0, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->provider:Lcom/upsight/mediation/ads/adapters/MRaidAdAdapter;

    iget-object v1, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->interstitial:Lcom/upsight/mediation/mraid/MRAIDInterstitial;

    invoke-virtual {v0, v1}, Lcom/upsight/mediation/ads/adapters/MRaidAdAdapter;->mraidInterstitialHide(Lcom/upsight/mediation/mraid/MRAIDInterstitial;)V

    .line 130
    :cond_1
    return-void

    .line 124
    :cond_2
    if-nez p2, :cond_0

    .line 125
    iget-object v0, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->provider:Lcom/upsight/mediation/ads/adapters/MRaidAdAdapter;

    invoke-virtual {v0}, Lcom/upsight/mediation/ads/adapters/MRaidAdAdapter;->onAdSkipped()V

    goto :goto_0
.end method

.method public onBackPressed()V
    .locals 2

    .prologue
    .line 116
    invoke-super {p0}, Landroid/app/Activity;->onBackPressed()V

    .line 117
    iget-object v0, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->provider:Lcom/upsight/mediation/ads/adapters/MRaidAdAdapter;

    iget-object v1, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->interstitial:Lcom/upsight/mediation/mraid/MRAIDInterstitial;

    invoke-virtual {v0, v1}, Lcom/upsight/mediation/ads/adapters/MRaidAdAdapter;->mraidInterstitialHide(Lcom/upsight/mediation/mraid/MRAIDInterstitial;)V

    .line 118
    return-void
.end method

.method public onConfigurationChanged(Landroid/content/res/Configuration;)V
    .locals 0
    .param p1, "newConfig"    # Landroid/content/res/Configuration;

    .prologue
    .line 100
    invoke-super {p0, p1}, Landroid/app/Activity;->onConfigurationChanged(Landroid/content/res/Configuration;)V

    .line 101
    return-void
.end method

.method protected onCreate(Landroid/os/Bundle;)V
    .locals 3
    .param p1, "savedInstanceState"    # Landroid/os/Bundle;

    .prologue
    .line 36
    invoke-super {p0, p1}, Landroid/app/Activity;->onCreate(Landroid/os/Bundle;)V

    .line 37
    invoke-virtual {p0}, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->getIntent()Landroid/content/Intent;

    move-result-object v0

    const-string v1, "registryId"

    const/4 v2, -0x1

    invoke-virtual {v0, v1, v2}, Landroid/content/Intent;->getIntExtra(Ljava/lang/String;I)I

    move-result v0

    iput v0, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->registryId:I

    .line 38
    sget-object v0, Lcom/upsight/mediation/ads/adapters/MRaidAdAdapter;->mRaidRegistry:Lcom/upsight/mediation/ads/adapters/MRaidRegistry;

    iget v1, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->registryId:I

    invoke-virtual {v0, v1}, Lcom/upsight/mediation/ads/adapters/MRaidRegistry;->getProvider(I)Lcom/upsight/mediation/ads/adapters/MRaidAdAdapter;

    move-result-object v0

    iput-object v0, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->provider:Lcom/upsight/mediation/ads/adapters/MRaidAdAdapter;

    .line 39
    iget-object v0, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->provider:Lcom/upsight/mediation/ads/adapters/MRaidAdAdapter;

    invoke-virtual {v0, p0}, Lcom/upsight/mediation/ads/adapters/MRaidAdAdapter;->setMRaidActivity(Lcom/upsight/mediation/ads/adapters/MRaidActivity;)V

    .line 44
    invoke-virtual {p0}, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->getIntent()Landroid/content/Intent;

    move-result-object v0

    const-string v1, "rotate"

    const/4 v2, 0x1

    invoke-virtual {v0, v1, v2}, Landroid/content/Intent;->getIntExtra(Ljava/lang/String;I)I

    move-result v0

    iput v0, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->rotateMode:I

    .line 46
    iget v0, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->rotateMode:I

    invoke-direct {p0, v0}, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->getOrientationValue(I)I

    move-result v0

    invoke-virtual {p0, v0}, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->setRequestedOrientation(I)V

    .line 48
    iget-object v0, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->provider:Lcom/upsight/mediation/ads/adapters/MRaidAdAdapter;

    invoke-virtual {v0}, Lcom/upsight/mediation/ads/adapters/MRaidAdAdapter;->getInterstitial()Lcom/upsight/mediation/mraid/MRAIDInterstitial;

    move-result-object v0

    iput-object v0, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->interstitial:Lcom/upsight/mediation/mraid/MRAIDInterstitial;

    .line 49
    iget-object v0, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->interstitial:Lcom/upsight/mediation/mraid/MRAIDInterstitial;

    invoke-virtual {v0, p0}, Lcom/upsight/mediation/mraid/MRAIDInterstitial;->updateContext(Landroid/content/Context;)V

    .line 57
    const-string v0, "MRaidActivity"

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p0}, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->getRequestedOrientation()I

    move-result v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v1

    const-string v2, ""

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {v0, v1}, Lcom/upsight/mediation/log/FuseLog;->e(Ljava/lang/String;Ljava/lang/String;)V

    .line 60
    return-void
.end method

.method protected onDestroy()V
    .locals 0

    .prologue
    .line 105
    invoke-super {p0}, Landroid/app/Activity;->onDestroy()V

    .line 106
    return-void
.end method

.method protected onPause()V
    .locals 1

    .prologue
    .line 110
    invoke-super {p0}, Landroid/app/Activity;->onPause()V

    .line 111
    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->isVisible:Z

    .line 112
    return-void
.end method

.method protected onResume()V
    .locals 2

    .prologue
    .line 78
    invoke-super {p0}, Landroid/app/Activity;->onResume()V

    .line 80
    const/4 v1, 0x1

    iput-boolean v1, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->isVisible:Z

    .line 81
    iget-boolean v1, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->firstResume:Z

    if-eqz v1, :cond_0

    .line 82
    const/4 v1, 0x0

    iput-boolean v1, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->firstResume:Z

    .line 87
    iget-object v1, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->interstitial:Lcom/upsight/mediation/mraid/MRAIDInterstitial;

    if-nez v1, :cond_1

    .line 88
    invoke-virtual {p0}, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->finish()V

    .line 96
    :cond_0
    :goto_0
    return-void

    .line 90
    :cond_1
    iget-object v1, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->interstitial:Lcom/upsight/mediation/mraid/MRAIDInterstitial;

    invoke-virtual {v1}, Lcom/upsight/mediation/mraid/MRAIDInterstitial;->show()Z

    move-result v0

    .line 91
    .local v0, "didShow":Z
    if-nez v0, :cond_0

    .line 92
    iget-object v1, p0, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->provider:Lcom/upsight/mediation/ads/adapters/MRaidAdAdapter;

    invoke-virtual {v1}, Lcom/upsight/mediation/ads/adapters/MRaidAdAdapter;->mraidInterstitialFailedToShow()V

    .line 93
    invoke-virtual {p0}, Lcom/upsight/mediation/ads/adapters/MRaidActivity;->finish()V

    goto :goto_0
.end method

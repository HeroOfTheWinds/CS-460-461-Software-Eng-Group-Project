.class public Lcom/upsight/android/marketing/UpsightBillboardHandlers$DefaultHandler;
.super Lcom/upsight/android/marketing/UpsightBillboardHandlers$SimpleHandler;
.source "UpsightBillboardHandlers.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lcom/upsight/android/marketing/UpsightBillboardHandlers;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x9
    name = "DefaultHandler"
.end annotation


# direct methods
.method public constructor <init>(Landroid/app/Activity;)V
    .locals 0
    .param p1, "activity"    # Landroid/app/Activity;

    .prologue
    .line 148
    invoke-direct {p0, p1}, Lcom/upsight/android/marketing/UpsightBillboardHandlers$SimpleHandler;-><init>(Landroid/app/Activity;)V

    .line 149
    return-void
.end method


# virtual methods
.method public onAttach(Ljava/lang/String;)Lcom/upsight/android/marketing/UpsightBillboard$AttachParameters;
    .locals 3
    .param p1, "scope"    # Ljava/lang/String;

    .prologue
    .line 153
    invoke-virtual {p0}, Lcom/upsight/android/marketing/UpsightBillboardHandlers$DefaultHandler;->getActivity()Landroid/app/Activity;

    move-result-object v0

    .line 154
    .local v0, "activity":Landroid/app/Activity;
    if-eqz v0, :cond_0

    invoke-virtual {v0}, Landroid/app/Activity;->isFinishing()Z

    move-result v1

    if-eqz v1, :cond_1

    .line 155
    :cond_0
    const/4 v1, 0x0

    .line 158
    :goto_0
    return-object v1

    .line 157
    :cond_1
    new-instance v1, Lcom/upsight/android/marketing/UpsightBillboard$AttachParameters;

    invoke-direct {v1, v0}, Lcom/upsight/android/marketing/UpsightBillboard$AttachParameters;-><init>(Landroid/app/Activity;)V

    sget-object v2, Lcom/upsight/android/marketing/UpsightBillboard$PresentationStyle;->None:Lcom/upsight/android/marketing/UpsightBillboard$PresentationStyle;

    .line 158
    invoke-virtual {v1, v2}, Lcom/upsight/android/marketing/UpsightBillboard$AttachParameters;->putPreferredPresentationStyle(Lcom/upsight/android/marketing/UpsightBillboard$PresentationStyle;)Lcom/upsight/android/marketing/UpsightBillboard$AttachParameters;

    move-result-object v1

    goto :goto_0
.end method

.method public bridge synthetic onDetach()V
    .locals 0

    .prologue
    .line 139
    invoke-super {p0}, Lcom/upsight/android/marketing/UpsightBillboardHandlers$SimpleHandler;->onDetach()V

    return-void
.end method

.method public bridge synthetic onNextView()V
    .locals 0

    .prologue
    .line 139
    invoke-super {p0}, Lcom/upsight/android/marketing/UpsightBillboardHandlers$SimpleHandler;->onNextView()V

    return-void
.end method

.method public bridge synthetic onPurchases(Ljava/util/List;)V
    .locals 0

    .prologue
    .line 139
    invoke-super {p0, p1}, Lcom/upsight/android/marketing/UpsightBillboardHandlers$SimpleHandler;->onPurchases(Ljava/util/List;)V

    return-void
.end method

.method public bridge synthetic onRewards(Ljava/util/List;)V
    .locals 0

    .prologue
    .line 139
    invoke-super {p0, p1}, Lcom/upsight/android/marketing/UpsightBillboardHandlers$SimpleHandler;->onRewards(Ljava/util/List;)V

    return-void
.end method

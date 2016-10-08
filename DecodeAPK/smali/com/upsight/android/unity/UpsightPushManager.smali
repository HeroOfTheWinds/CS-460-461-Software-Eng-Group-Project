.class public Lcom/upsight/android/unity/UpsightPushManager;
.super Ljava/lang/Object;
.source "UpsightPushManager.java"

# interfaces
.implements Lcom/upsight/android/unity/IUpsightExtensionManager;


# static fields
.field protected static final TAG:Ljava/lang/String; = "Upsight-UnityPush"


# instance fields
.field private mPushBillboard:Lcom/upsight/android/marketing/UpsightBillboard;

.field private mPushBillboardHandler:Lcom/upsight/android/unity/BillboardHandler;

.field private mUpsight:Lcom/upsight/android/UpsightContext;


# direct methods
.method public constructor <init>()V
    .locals 0

    .prologue
    .line 17
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method static synthetic access$000(Lcom/upsight/android/unity/UpsightPushManager;)Lcom/upsight/android/UpsightContext;
    .locals 1
    .param p0, "x0"    # Lcom/upsight/android/unity/UpsightPushManager;

    .prologue
    .line 17
    iget-object v0, p0, Lcom/upsight/android/unity/UpsightPushManager;->mUpsight:Lcom/upsight/android/UpsightContext;

    return-object v0
.end method


# virtual methods
.method public init(Lcom/upsight/android/UpsightContext;)V
    .locals 3
    .param p1, "context"    # Lcom/upsight/android/UpsightContext;

    .prologue
    .line 28
    iput-object p1, p0, Lcom/upsight/android/unity/UpsightPushManager;->mUpsight:Lcom/upsight/android/UpsightContext;

    .line 30
    invoke-static {}, Lcom/upsight/android/unity/UnityBridge;->getActivity()Landroid/app/Activity;

    move-result-object v0

    .line 31
    .local v0, "activity":Landroid/app/Activity;
    if-nez v0, :cond_0

    .line 38
    :goto_0
    return-void

    .line 36
    :cond_0
    new-instance v1, Lcom/upsight/android/unity/BillboardHandler;

    invoke-direct {v1, v0}, Lcom/upsight/android/unity/BillboardHandler;-><init>(Landroid/app/Activity;)V

    iput-object v1, p0, Lcom/upsight/android/unity/UpsightPushManager;->mPushBillboardHandler:Lcom/upsight/android/unity/BillboardHandler;

    .line 37
    iget-object v1, p0, Lcom/upsight/android/unity/UpsightPushManager;->mUpsight:Lcom/upsight/android/UpsightContext;

    iget-object v2, p0, Lcom/upsight/android/unity/UpsightPushManager;->mPushBillboardHandler:Lcom/upsight/android/unity/BillboardHandler;

    invoke-static {v1, v2}, Lcom/upsight/android/googlepushservices/UpsightPushBillboard;->create(Lcom/upsight/android/UpsightContext;Lcom/upsight/android/marketing/UpsightBillboard$Handler;)Lcom/upsight/android/marketing/UpsightBillboard;

    move-result-object v1

    iput-object v1, p0, Lcom/upsight/android/unity/UpsightPushManager;->mPushBillboard:Lcom/upsight/android/marketing/UpsightBillboard;

    goto :goto_0
.end method

.method public onApplicationPaused()V
    .locals 2

    .prologue
    .line 91
    iget-object v1, p0, Lcom/upsight/android/unity/UpsightPushManager;->mUpsight:Lcom/upsight/android/UpsightContext;

    if-nez v1, :cond_1

    .line 104
    :cond_0
    :goto_0
    return-void

    .line 97
    :cond_1
    :try_start_0
    iget-object v1, p0, Lcom/upsight/android/unity/UpsightPushManager;->mPushBillboard:Lcom/upsight/android/marketing/UpsightBillboard;

    if-eqz v1, :cond_0

    .line 98
    iget-object v1, p0, Lcom/upsight/android/unity/UpsightPushManager;->mPushBillboard:Lcom/upsight/android/marketing/UpsightBillboard;

    invoke-virtual {v1}, Lcom/upsight/android/marketing/UpsightBillboard;->destroy()V

    .line 99
    const/4 v1, 0x0

    iput-object v1, p0, Lcom/upsight/android/unity/UpsightPushManager;->mPushBillboard:Lcom/upsight/android/marketing/UpsightBillboard;
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    .line 101
    :catch_0
    move-exception v0

    .line 102
    .local v0, "e":Ljava/lang/Exception;
    invoke-virtual {v0}, Ljava/lang/Exception;->printStackTrace()V

    goto :goto_0
.end method

.method public onApplicationResumed()V
    .locals 3

    .prologue
    .line 108
    iget-object v1, p0, Lcom/upsight/android/unity/UpsightPushManager;->mUpsight:Lcom/upsight/android/UpsightContext;

    if-nez v1, :cond_1

    .line 119
    :cond_0
    :goto_0
    return-void

    .line 113
    :cond_1
    :try_start_0
    iget-object v1, p0, Lcom/upsight/android/unity/UpsightPushManager;->mPushBillboard:Lcom/upsight/android/marketing/UpsightBillboard;

    if-nez v1, :cond_0

    .line 114
    iget-object v1, p0, Lcom/upsight/android/unity/UpsightPushManager;->mUpsight:Lcom/upsight/android/UpsightContext;

    iget-object v2, p0, Lcom/upsight/android/unity/UpsightPushManager;->mPushBillboardHandler:Lcom/upsight/android/unity/BillboardHandler;

    invoke-static {v1, v2}, Lcom/upsight/android/googlepushservices/UpsightPushBillboard;->create(Lcom/upsight/android/UpsightContext;Lcom/upsight/android/marketing/UpsightBillboard$Handler;)Lcom/upsight/android/marketing/UpsightBillboard;

    move-result-object v1

    iput-object v1, p0, Lcom/upsight/android/unity/UpsightPushManager;->mPushBillboard:Lcom/upsight/android/marketing/UpsightBillboard;
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    .line 116
    :catch_0
    move-exception v0

    .line 117
    .local v0, "e":Ljava/lang/Exception;
    invoke-virtual {v0}, Ljava/lang/Exception;->printStackTrace()V

    goto :goto_0
.end method

.method public registerForPushNotifications()V
    .locals 1

    .prologue
    .line 65
    iget-object v0, p0, Lcom/upsight/android/unity/UpsightPushManager;->mUpsight:Lcom/upsight/android/UpsightContext;

    if-nez v0, :cond_0

    .line 86
    :goto_0
    return-void

    .line 69
    :cond_0
    new-instance v0, Lcom/upsight/android/unity/UpsightPushManager$2;

    invoke-direct {v0, p0}, Lcom/upsight/android/unity/UpsightPushManager$2;-><init>(Lcom/upsight/android/unity/UpsightPushManager;)V

    invoke-static {v0}, Lcom/upsight/android/unity/UnityBridge;->runSafelyOnUiThread(Ljava/lang/Runnable;)V

    goto :goto_0
.end method

.method public unregisterForPushNotifications()V
    .locals 1

    .prologue
    .line 41
    iget-object v0, p0, Lcom/upsight/android/unity/UpsightPushManager;->mUpsight:Lcom/upsight/android/UpsightContext;

    if-nez v0, :cond_0

    .line 62
    :goto_0
    return-void

    .line 45
    :cond_0
    new-instance v0, Lcom/upsight/android/unity/UpsightPushManager$1;

    invoke-direct {v0, p0}, Lcom/upsight/android/unity/UpsightPushManager$1;-><init>(Lcom/upsight/android/unity/UpsightPushManager;)V

    invoke-static {v0}, Lcom/upsight/android/unity/UnityBridge;->runSafelyOnUiThread(Ljava/lang/Runnable;)V

    goto :goto_0
.end method

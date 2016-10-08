.class public Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;
.super Landroid/app/Activity;
.source "BillboardManagementActivity.java"


# static fields
.field static final INTENT_EXTRA_MARKETING_CONTENT_DIALOG_THEME:Ljava/lang/String; = "marketingContentDialogTheme"

.field static final INTENT_EXTRA_MARKETING_CONTENT_ID:Ljava/lang/String; = "marketingContentId"

.field static final INTENT_EXTRA_MARKETING_CONTENT_PREFERRED_STYLE:Ljava/lang/String; = "marketingContentPreferredStyle"

.field private static final LOG_TAG:Ljava/lang/String; = "BillboardActivity"

.field private static final STYLE_DIALOG:I

.field private static final STYLE_FULLSCREEN:I = 0x103012a


# instance fields
.field private mBillboard:Lcom/upsight/android/marketing/internal/billboard/Billboard;

.field private mContent:Lcom/upsight/android/marketing/internal/content/MarketingContent;

.field mContentStore:Lcom/upsight/android/marketing/internal/content/MarketingContentStore;
    .annotation runtime Ljavax/inject/Inject;
    .end annotation
.end field

.field private mDataStoreSubscription:Lcom/upsight/android/persistence/UpsightSubscription;

.field private mFragment:Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;

.field private mIsForeground:Z

.field private mShouldAttachOnResume:Z

.field mUpsight:Lcom/upsight/android/UpsightContext;
    .annotation runtime Ljavax/inject/Inject;
    .end annotation
.end field


# direct methods
.method static constructor <clinit>()V
    .locals 1

    .prologue
    .line 51
    sget v0, Lcom/upsight/android/marketing/R$style;->Theme_DeviceDefault_Light_Dialog_NoActionBar_UpsightDialog:I

    sput v0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->STYLE_DIALOG:I

    return-void
.end method

.method public constructor <init>()V
    .locals 2

    .prologue
    const/4 v1, 0x0

    const/4 v0, 0x0

    .line 38
    invoke-direct {p0}, Landroid/app/Activity;-><init>()V

    .line 64
    iput-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mContent:Lcom/upsight/android/marketing/internal/content/MarketingContent;

    .line 66
    iput-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mBillboard:Lcom/upsight/android/marketing/internal/billboard/Billboard;

    .line 68
    iput-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mFragment:Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;

    .line 70
    iput-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mDataStoreSubscription:Lcom/upsight/android/persistence/UpsightSubscription;

    .line 72
    iput-boolean v1, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mIsForeground:Z

    .line 74
    iput-boolean v1, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mShouldAttachOnResume:Z

    return-void
.end method

.method private handle(Lcom/upsight/android/marketing/internal/content/MarketingContent$ScopelessAvailabilityEvent;)V
    .locals 8
    .param p1, "event"    # Lcom/upsight/android/marketing/internal/content/MarketingContent$ScopelessAvailabilityEvent;

    .prologue
    .line 273
    iget-object v1, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mContent:Lcom/upsight/android/marketing/internal/content/MarketingContent;

    .line 274
    .local v1, "content":Lcom/upsight/android/marketing/internal/content/MarketingContent;
    if-eqz v1, :cond_0

    invoke-virtual {v1}, Lcom/upsight/android/marketing/internal/content/MarketingContent;->getId()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {p1}, Lcom/upsight/android/marketing/internal/content/MarketingContent$ScopelessAvailabilityEvent;->getParentId()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v6, v7}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v6

    if-eqz v6, :cond_0

    .line 276
    iget-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mBillboard:Lcom/upsight/android/marketing/internal/billboard/Billboard;

    .line 277
    .local v0, "billboard":Lcom/upsight/android/marketing/internal/billboard/Billboard;
    if-eqz v0, :cond_0

    .line 278
    iget-object v6, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mContentStore:Lcom/upsight/android/marketing/internal/content/MarketingContentStore;

    invoke-virtual {p1}, Lcom/upsight/android/marketing/internal/content/MarketingContent$ScopelessAvailabilityEvent;->getId()Ljava/lang/String;

    move-result-object v7

    invoke-interface {v6, v7}, Lcom/upsight/android/marketing/internal/content/MarketingContentStore;->get(Ljava/lang/String;)Lcom/upsight/android/analytics/internal/action/Actionable;

    move-result-object v4

    check-cast v4, Lcom/upsight/android/marketing/internal/content/MarketingContent;

    .line 279
    .local v4, "nextContent":Lcom/upsight/android/marketing/internal/content/MarketingContent;
    if-eqz v4, :cond_0

    invoke-virtual {v4}, Lcom/upsight/android/marketing/internal/content/MarketingContent;->isAvailable()Z

    move-result v6

    if-eqz v6, :cond_0

    .line 280
    invoke-virtual {v1}, Lcom/upsight/android/marketing/internal/content/MarketingContent;->getContentMediator()Lcom/upsight/android/marketing/UpsightContentMediator;

    move-result-object v3

    .line 281
    .local v3, "mediator":Lcom/upsight/android/marketing/UpsightContentMediator;, "Lcom/upsight/android/marketing/UpsightContentMediator<*>;"
    invoke-virtual {v4}, Lcom/upsight/android/marketing/internal/content/MarketingContent;->getContentMediator()Lcom/upsight/android/marketing/UpsightContentMediator;

    move-result-object v5

    .line 282
    .local v5, "nextMediator":Lcom/upsight/android/marketing/UpsightContentMediator;, "Lcom/upsight/android/marketing/UpsightContentMediator<*>;"
    if-eqz v3, :cond_0

    if-eqz v5, :cond_0

    .line 284
    iput-object v4, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mContent:Lcom/upsight/android/marketing/internal/content/MarketingContent;

    .line 287
    invoke-virtual {v4, v0}, Lcom/upsight/android/marketing/internal/content/MarketingContent;->bindBillboard(Lcom/upsight/android/marketing/internal/billboard/Billboard;)V

    .line 290
    invoke-virtual {v0}, Lcom/upsight/android/marketing/internal/billboard/Billboard;->getHandler()Lcom/upsight/android/marketing/UpsightBillboard$Handler;

    move-result-object v6

    invoke-interface {v6}, Lcom/upsight/android/marketing/UpsightBillboard$Handler;->onNextView()V

    .line 293
    invoke-virtual {p0}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->getFragmentManager()Landroid/app/FragmentManager;

    move-result-object v2

    .line 294
    .local v2, "fragmentManager":Landroid/app/FragmentManager;
    iget-object v6, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mFragment:Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;

    invoke-virtual {v3, v1, v2, v6}, Lcom/upsight/android/marketing/UpsightContentMediator;->hideContent(Lcom/upsight/android/marketing/internal/content/MarketingContent;Landroid/app/FragmentManager;Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;)V

    .line 295
    iget-object v6, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mFragment:Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;

    invoke-virtual {v5, v4, v2, v6}, Lcom/upsight/android/marketing/UpsightContentMediator;->displayContent(Lcom/upsight/android/marketing/internal/content/MarketingContent;Landroid/app/FragmentManager;Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;)V

    .line 300
    .end local v0    # "billboard":Lcom/upsight/android/marketing/internal/billboard/Billboard;
    .end local v2    # "fragmentManager":Landroid/app/FragmentManager;
    .end local v3    # "mediator":Lcom/upsight/android/marketing/UpsightContentMediator;, "Lcom/upsight/android/marketing/UpsightContentMediator<*>;"
    .end local v4    # "nextContent":Lcom/upsight/android/marketing/internal/content/MarketingContent;
    .end local v5    # "nextMediator":Lcom/upsight/android/marketing/UpsightContentMediator;, "Lcom/upsight/android/marketing/UpsightContentMediator<*>;"
    :cond_0
    return-void
.end method

.method private handle(Lcom/upsight/android/marketing/internal/content/MarketingContent$SubcontentAvailabilityEvent;)V
    .locals 3
    .param p1, "event"    # Lcom/upsight/android/marketing/internal/content/MarketingContent$SubcontentAvailabilityEvent;

    .prologue
    .line 259
    iget-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mContent:Lcom/upsight/android/marketing/internal/content/MarketingContent;

    .line 260
    .local v0, "content":Lcom/upsight/android/marketing/internal/content/MarketingContent;
    if-eqz v0, :cond_0

    invoke-virtual {p1}, Lcom/upsight/android/marketing/internal/content/MarketingContent$SubcontentAvailabilityEvent;->getId()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0}, Lcom/upsight/android/marketing/internal/content/MarketingContent;->getId()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_0

    .line 261
    invoke-virtual {p0, v0}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->attachBillboard(Lcom/upsight/android/marketing/internal/content/MarketingContent;)V

    .line 263
    :cond_0
    return-void
.end method

.method private handle(Lcom/upsight/android/marketing/internal/content/MarketingContent$SubdialogAvailabilityEvent;)V
    .locals 3
    .param p1, "event"    # Lcom/upsight/android/marketing/internal/content/MarketingContent$SubdialogAvailabilityEvent;

    .prologue
    .line 266
    iget-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mContent:Lcom/upsight/android/marketing/internal/content/MarketingContent;

    .line 267
    .local v0, "content":Lcom/upsight/android/marketing/internal/content/MarketingContent;
    if-eqz v0, :cond_0

    invoke-virtual {p1}, Lcom/upsight/android/marketing/internal/content/MarketingContent$SubdialogAvailabilityEvent;->getId()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0}, Lcom/upsight/android/marketing/internal/content/MarketingContent;->getId()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_0

    .line 268
    invoke-virtual {p1}, Lcom/upsight/android/marketing/internal/content/MarketingContent$SubdialogAvailabilityEvent;->getPendingDialog()Lcom/upsight/android/marketing/internal/content/MarketingContent$PendingDialog;

    move-result-object v1

    invoke-virtual {p0, v1}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->attachDialog(Lcom/upsight/android/marketing/internal/content/MarketingContent$PendingDialog;)V

    .line 270
    :cond_0
    return-void
.end method

.method private handle(Lcom/upsight/android/marketing/internal/content/MarketingContentActions$DestroyEvent;)V
    .locals 3
    .param p1, "event"    # Lcom/upsight/android/marketing/internal/content/MarketingContentActions$DestroyEvent;

    .prologue
    .line 303
    iget-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mContent:Lcom/upsight/android/marketing/internal/content/MarketingContent;

    .line 304
    .local v0, "content":Lcom/upsight/android/marketing/internal/content/MarketingContent;
    if-eqz v0, :cond_0

    invoke-virtual {v0}, Lcom/upsight/android/marketing/internal/content/MarketingContent;->getId()Ljava/lang/String;

    move-result-object v1

    iget-object v2, p1, Lcom/upsight/android/marketing/internal/content/MarketingContentActions$DestroyEvent;->mId:Ljava/lang/String;

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_0

    .line 305
    invoke-virtual {p0}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->detachBillboard()V

    .line 307
    :cond_0
    return-void
.end method


# virtual methods
.method attachBillboard(Lcom/upsight/android/marketing/internal/content/MarketingContent;)V
    .locals 9
    .param p1, "content"    # Lcom/upsight/android/marketing/internal/content/MarketingContent;

    .prologue
    const/4 v8, 0x0

    .line 332
    iget-object v4, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mUpsight:Lcom/upsight/android/UpsightContext;

    invoke-virtual {v4}, Lcom/upsight/android/UpsightContext;->getLogger()Lcom/upsight/android/logger/UpsightLogger;

    move-result-object v4

    const-string v5, "BillboardActivity"

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "Attach billboard activity="

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v6, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    move-result-object v6

    const-string v7, " marketingContentId="

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {p1}, Lcom/upsight/android/marketing/internal/content/MarketingContent;->getId()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    new-array v7, v8, [Ljava/lang/Object;

    invoke-interface {v4, v5, v6, v7}, Lcom/upsight/android/logger/UpsightLogger;->d(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V

    .line 334
    invoke-virtual {p1}, Lcom/upsight/android/marketing/internal/content/MarketingContent;->getContentMediator()Lcom/upsight/android/marketing/UpsightContentMediator;

    move-result-object v1

    .line 335
    .local v1, "mediator":Lcom/upsight/android/marketing/UpsightContentMediator;, "Lcom/upsight/android/marketing/UpsightContentMediator<*>;"
    if-eqz v1, :cond_2

    .line 338
    invoke-virtual {p0}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->getIntent()Landroid/content/Intent;

    move-result-object v4

    const-string v5, "marketingContentPreferredStyle"

    invoke-virtual {v4, v5}, Landroid/content/Intent;->getSerializableExtra(Ljava/lang/String;)Ljava/io/Serializable;

    move-result-object v2

    check-cast v2, Lcom/upsight/android/marketing/UpsightBillboard$PresentationStyle;

    .line 339
    .local v2, "preferredStyle":Lcom/upsight/android/marketing/UpsightBillboard$PresentationStyle;
    if-eqz v2, :cond_0

    sget-object v4, Lcom/upsight/android/marketing/UpsightBillboard$PresentationStyle;->None:Lcom/upsight/android/marketing/UpsightBillboard$PresentationStyle;

    invoke-virtual {v2, v4}, Lcom/upsight/android/marketing/UpsightBillboard$PresentationStyle;->equals(Ljava/lang/Object;)Z

    move-result v4

    if-eqz v4, :cond_1

    .line 341
    :cond_0
    invoke-virtual {v1, p1}, Lcom/upsight/android/marketing/UpsightContentMediator;->getPresentationStyle(Lcom/upsight/android/marketing/internal/content/MarketingContent;)Lcom/upsight/android/marketing/UpsightBillboard$PresentationStyle;

    move-result-object v2

    .line 346
    :cond_1
    const/4 v0, 0x0

    .line 347
    .local v0, "fragmentDimensions":Ljava/util/Set;, "Ljava/util/Set<Lcom/upsight/android/marketing/UpsightBillboard$Dimensions;>;"
    sget-object v4, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity$1;->$SwitchMap$com$upsight$android$marketing$UpsightBillboard$PresentationStyle:[I

    invoke-virtual {v2}, Lcom/upsight/android/marketing/UpsightBillboard$PresentationStyle;->ordinal()I

    move-result v5

    aget v4, v4, v5

    packed-switch v4, :pswitch_data_0

    .line 354
    const v3, 0x103012a

    .line 358
    .local v3, "style":I
    :goto_0
    invoke-static {p0, v0}, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->newInstance(Landroid/content/Context;Ljava/util/Set;)Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;

    move-result-object v4

    iput-object v4, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mFragment:Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;

    .line 359
    iget-object v4, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mFragment:Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;

    const/4 v5, 0x1

    invoke-virtual {v4, v5, v3}, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->setStyle(II)V

    .line 360
    iget-object v4, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mFragment:Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;

    invoke-virtual {v4, v8}, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->setCancelable(Z)V

    .line 361
    invoke-virtual {p0}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->getFragmentManager()Landroid/app/FragmentManager;

    move-result-object v4

    iget-object v5, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mFragment:Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;

    invoke-virtual {v1, p1, v4, v5}, Lcom/upsight/android/marketing/UpsightContentMediator;->displayContent(Lcom/upsight/android/marketing/internal/content/MarketingContent;Landroid/app/FragmentManager;Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;)V

    .line 363
    .end local v0    # "fragmentDimensions":Ljava/util/Set;, "Ljava/util/Set<Lcom/upsight/android/marketing/UpsightBillboard$Dimensions;>;"
    .end local v2    # "preferredStyle":Lcom/upsight/android/marketing/UpsightBillboard$PresentationStyle;
    .end local v3    # "style":I
    :cond_2
    return-void

    .line 349
    .restart local v0    # "fragmentDimensions":Ljava/util/Set;, "Ljava/util/Set<Lcom/upsight/android/marketing/UpsightBillboard$Dimensions;>;"
    .restart local v2    # "preferredStyle":Lcom/upsight/android/marketing/UpsightBillboard$PresentationStyle;
    :pswitch_0
    sget v3, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->STYLE_DIALOG:I

    .line 350
    .restart local v3    # "style":I
    invoke-virtual {v1, p1}, Lcom/upsight/android/marketing/UpsightContentMediator;->getDimensions(Lcom/upsight/android/marketing/internal/content/MarketingContent;)Ljava/util/Set;

    move-result-object v0

    .line 351
    goto :goto_0

    .line 347
    :pswitch_data_0
    .packed-switch 0x1
        :pswitch_0
    .end packed-switch
.end method

.method attachDialog(Lcom/upsight/android/marketing/internal/content/MarketingContent$PendingDialog;)V
    .locals 7
    .param p1, "pendingDialog"    # Lcom/upsight/android/marketing/internal/content/MarketingContent$PendingDialog;

    .prologue
    const/4 v6, 0x0

    .line 315
    iget-object v2, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mUpsight:Lcom/upsight/android/UpsightContext;

    invoke-virtual {v2}, Lcom/upsight/android/UpsightContext;->getLogger()Lcom/upsight/android/logger/UpsightLogger;

    move-result-object v2

    const-string v3, "BillboardActivity"

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "Attach dialog activity="

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    invoke-virtual {v4, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    move-result-object v4

    const-string v5, " marketingContentId="

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    iget-object v5, p1, Lcom/upsight/android/marketing/internal/content/MarketingContent$PendingDialog;->mId:Ljava/lang/String;

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    const/4 v5, 0x0

    new-array v5, v5, [Ljava/lang/Object;

    invoke-interface {v2, v3, v4, v5}, Lcom/upsight/android/logger/UpsightLogger;->d(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V

    .line 317
    invoke-virtual {p0}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->getIntent()Landroid/content/Intent;

    move-result-object v1

    .line 318
    .local v1, "intent":Landroid/content/Intent;
    const-string v2, "marketingContentDialogTheme"

    invoke-virtual {v1, v2}, Landroid/content/Intent;->hasExtra(Ljava/lang/String;)Z

    move-result v2

    if-eqz v2, :cond_0

    .line 319
    const-string v2, "marketingContentDialogTheme"

    const/high16 v3, -0x80000000

    invoke-virtual {v1, v2, v3}, Landroid/content/Intent;->getIntExtra(Ljava/lang/String;I)I

    move-result v0

    .line 320
    .local v0, "dialogTheme":I
    invoke-static {p1, v0}, Lcom/upsight/android/marketing/internal/billboard/BillboardDialogFragment;->newInstance(Lcom/upsight/android/marketing/internal/content/MarketingContent$PendingDialog;I)Landroid/app/DialogFragment;

    move-result-object v2

    invoke-virtual {p0}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->getFragmentManager()Landroid/app/FragmentManager;

    move-result-object v3

    invoke-virtual {v2, v3, v6}, Landroid/app/DialogFragment;->show(Landroid/app/FragmentManager;Ljava/lang/String;)V

    .line 324
    .end local v0    # "dialogTheme":I
    :goto_0
    return-void

    .line 322
    :cond_0
    invoke-static {p1}, Lcom/upsight/android/marketing/internal/billboard/BillboardDialogFragment;->newInstance(Lcom/upsight/android/marketing/internal/content/MarketingContent$PendingDialog;)Landroid/app/DialogFragment;

    move-result-object v2

    invoke-virtual {p0}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->getFragmentManager()Landroid/app/FragmentManager;

    move-result-object v3

    invoke-virtual {v2, v3, v6}, Landroid/app/DialogFragment;->show(Landroid/app/FragmentManager;Ljava/lang/String;)V

    goto :goto_0
.end method

.method detachBillboard()V
    .locals 7

    .prologue
    const/4 v6, 0x0

    .line 369
    iget-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mBillboard:Lcom/upsight/android/marketing/internal/billboard/Billboard;

    .line 370
    .local v0, "billboard":Lcom/upsight/android/marketing/internal/billboard/Billboard;
    if-eqz v0, :cond_1

    .line 371
    iget-object v2, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mUpsight:Lcom/upsight/android/UpsightContext;

    invoke-virtual {v2}, Lcom/upsight/android/UpsightContext;->getLogger()Lcom/upsight/android/logger/UpsightLogger;

    move-result-object v2

    const-string v3, "BillboardActivity"

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "Detach billboard activity="

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    invoke-virtual {v4, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    move-result-object v4

    const-string v5, " scope="

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    invoke-virtual {v0}, Lcom/upsight/android/marketing/internal/billboard/Billboard;->getScope()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    const/4 v5, 0x0

    new-array v5, v5, [Ljava/lang/Object;

    invoke-interface {v2, v3, v4, v5}, Lcom/upsight/android/logger/UpsightLogger;->d(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V

    .line 374
    iget-object v1, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mFragment:Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;

    .line 375
    .local v1, "fragment":Landroid/app/DialogFragment;
    if-eqz v1, :cond_0

    invoke-virtual {v1}, Landroid/app/DialogFragment;->isAdded()Z

    move-result v2

    if-eqz v2, :cond_0

    .line 376
    invoke-virtual {v1}, Landroid/app/DialogFragment;->dismiss()V

    .line 380
    :cond_0
    iput-object v6, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mContent:Lcom/upsight/android/marketing/internal/content/MarketingContent;

    .line 381
    iput-object v6, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mBillboard:Lcom/upsight/android/marketing/internal/billboard/Billboard;

    .line 382
    iput-object v6, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mFragment:Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;

    .line 385
    invoke-virtual {p0}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->finish()V

    .line 388
    invoke-virtual {v0}, Lcom/upsight/android/marketing/internal/billboard/Billboard;->getHandler()Lcom/upsight/android/marketing/UpsightBillboard$Handler;

    move-result-object v2

    invoke-interface {v2}, Lcom/upsight/android/marketing/UpsightBillboard$Handler;->onDetach()V

    .line 390
    .end local v1    # "fragment":Landroid/app/DialogFragment;
    :cond_1
    return-void
.end method

.method public handleActionEvent(Lcom/upsight/android/marketing/internal/content/MarketingContentActions$DestroyEvent;)V
    .locals 5
    .param p1, "event"    # Lcom/upsight/android/marketing/internal/content/MarketingContentActions$DestroyEvent;
    .annotation runtime Lcom/squareup/otto/Subscribe;
    .end annotation

    .prologue
    .line 233
    iget-object v1, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mUpsight:Lcom/upsight/android/UpsightContext;

    invoke-virtual {v1}, Lcom/upsight/android/UpsightContext;->getLogger()Lcom/upsight/android/logger/UpsightLogger;

    move-result-object v1

    const-string v2, "BillboardActivity"

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "Received DestroyEvent activity="

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v3, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    move-result-object v3

    const-string v4, " marketingContentId="

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    iget-object v4, p1, Lcom/upsight/android/marketing/internal/content/MarketingContentActions$DestroyEvent;->mId:Ljava/lang/String;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    const/4 v4, 0x0

    new-array v4, v4, [Ljava/lang/Object;

    invoke-interface {v1, v2, v3, v4}, Lcom/upsight/android/logger/UpsightLogger;->d(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V

    .line 235
    iget-boolean v1, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mIsForeground:Z

    if-eqz v1, :cond_1

    .line 236
    invoke-direct {p0, p1}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->handle(Lcom/upsight/android/marketing/internal/content/MarketingContentActions$DestroyEvent;)V

    .line 243
    :cond_0
    :goto_0
    return-void

    .line 238
    :cond_1
    iget-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mContent:Lcom/upsight/android/marketing/internal/content/MarketingContent;

    .line 239
    .local v0, "content":Lcom/upsight/android/marketing/internal/content/MarketingContent;, "Lcom/upsight/android/marketing/internal/content/MarketingContent<*>;"
    if-eqz v0, :cond_0

    .line 240
    invoke-virtual {v0}, Lcom/upsight/android/marketing/internal/content/MarketingContent;->getEventQueue()Ljava/util/Queue;

    move-result-object v1

    invoke-interface {v1, p1}, Ljava/util/Queue;->add(Ljava/lang/Object;)Z

    goto :goto_0
.end method

.method public handleAvailabilityEvent(Lcom/upsight/android/marketing/internal/content/MarketingContent$ScopelessAvailabilityEvent;)V
    .locals 5
    .param p1, "event"    # Lcom/upsight/android/marketing/internal/content/MarketingContent$ScopelessAvailabilityEvent;
    .annotation runtime Lcom/squareup/otto/Subscribe;
    .end annotation

    .prologue
    .line 219
    iget-object v1, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mUpsight:Lcom/upsight/android/UpsightContext;

    invoke-virtual {v1}, Lcom/upsight/android/UpsightContext;->getLogger()Lcom/upsight/android/logger/UpsightLogger;

    move-result-object v1

    const-string v2, "BillboardActivity"

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "Received ScopelessAvailabilityEvent activity="

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v3, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    move-result-object v3

    const-string v4, " marketingContentId="

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {p1}, Lcom/upsight/android/marketing/internal/content/MarketingContent$ScopelessAvailabilityEvent;->getId()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    const/4 v4, 0x0

    new-array v4, v4, [Ljava/lang/Object;

    invoke-interface {v1, v2, v3, v4}, Lcom/upsight/android/logger/UpsightLogger;->d(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V

    .line 221
    iget-boolean v1, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mIsForeground:Z

    if-eqz v1, :cond_1

    .line 222
    invoke-direct {p0, p1}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->handle(Lcom/upsight/android/marketing/internal/content/MarketingContent$ScopelessAvailabilityEvent;)V

    .line 229
    :cond_0
    :goto_0
    return-void

    .line 224
    :cond_1
    iget-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mContent:Lcom/upsight/android/marketing/internal/content/MarketingContent;

    .line 225
    .local v0, "content":Lcom/upsight/android/marketing/internal/content/MarketingContent;, "Lcom/upsight/android/marketing/internal/content/MarketingContent<*>;"
    if-eqz v0, :cond_0

    .line 226
    invoke-virtual {v0}, Lcom/upsight/android/marketing/internal/content/MarketingContent;->getEventQueue()Ljava/util/Queue;

    move-result-object v1

    invoke-interface {v1, p1}, Ljava/util/Queue;->add(Ljava/lang/Object;)Z

    goto :goto_0
.end method

.method public handleAvailabilityEvent(Lcom/upsight/android/marketing/internal/content/MarketingContent$SubcontentAvailabilityEvent;)V
    .locals 5
    .param p1, "event"    # Lcom/upsight/android/marketing/internal/content/MarketingContent$SubcontentAvailabilityEvent;
    .annotation runtime Lcom/squareup/otto/Subscribe;
    .end annotation

    .prologue
    .line 191
    iget-object v1, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mUpsight:Lcom/upsight/android/UpsightContext;

    invoke-virtual {v1}, Lcom/upsight/android/UpsightContext;->getLogger()Lcom/upsight/android/logger/UpsightLogger;

    move-result-object v1

    const-string v2, "BillboardActivity"

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "Received SubcontentAvailabilityEvent activity="

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v3, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    move-result-object v3

    const-string v4, " marketingContentId="

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {p1}, Lcom/upsight/android/marketing/internal/content/MarketingContent$SubcontentAvailabilityEvent;->getId()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    const/4 v4, 0x0

    new-array v4, v4, [Ljava/lang/Object;

    invoke-interface {v1, v2, v3, v4}, Lcom/upsight/android/logger/UpsightLogger;->d(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V

    .line 193
    iget-boolean v1, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mIsForeground:Z

    if-eqz v1, :cond_1

    .line 194
    invoke-direct {p0, p1}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->handle(Lcom/upsight/android/marketing/internal/content/MarketingContent$SubcontentAvailabilityEvent;)V

    .line 201
    :cond_0
    :goto_0
    return-void

    .line 196
    :cond_1
    iget-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mContent:Lcom/upsight/android/marketing/internal/content/MarketingContent;

    .line 197
    .local v0, "content":Lcom/upsight/android/marketing/internal/content/MarketingContent;, "Lcom/upsight/android/marketing/internal/content/MarketingContent<*>;"
    if-eqz v0, :cond_0

    .line 198
    invoke-virtual {v0}, Lcom/upsight/android/marketing/internal/content/MarketingContent;->getEventQueue()Ljava/util/Queue;

    move-result-object v1

    invoke-interface {v1, p1}, Ljava/util/Queue;->add(Ljava/lang/Object;)Z

    goto :goto_0
.end method

.method public handleAvailabilityEvent(Lcom/upsight/android/marketing/internal/content/MarketingContent$SubdialogAvailabilityEvent;)V
    .locals 5
    .param p1, "event"    # Lcom/upsight/android/marketing/internal/content/MarketingContent$SubdialogAvailabilityEvent;
    .annotation runtime Lcom/squareup/otto/Subscribe;
    .end annotation

    .prologue
    .line 205
    iget-object v1, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mUpsight:Lcom/upsight/android/UpsightContext;

    invoke-virtual {v1}, Lcom/upsight/android/UpsightContext;->getLogger()Lcom/upsight/android/logger/UpsightLogger;

    move-result-object v1

    const-string v2, "BillboardActivity"

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "Received SubdialogAvailabilityEvent activity="

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v3, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    move-result-object v3

    const-string v4, " marketingContentId="

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {p1}, Lcom/upsight/android/marketing/internal/content/MarketingContent$SubdialogAvailabilityEvent;->getId()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    const/4 v4, 0x0

    new-array v4, v4, [Ljava/lang/Object;

    invoke-interface {v1, v2, v3, v4}, Lcom/upsight/android/logger/UpsightLogger;->d(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V

    .line 207
    iget-boolean v1, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mIsForeground:Z

    if-eqz v1, :cond_1

    .line 208
    invoke-direct {p0, p1}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->handle(Lcom/upsight/android/marketing/internal/content/MarketingContent$SubdialogAvailabilityEvent;)V

    .line 215
    :cond_0
    :goto_0
    return-void

    .line 210
    :cond_1
    iget-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mContent:Lcom/upsight/android/marketing/internal/content/MarketingContent;

    .line 211
    .local v0, "content":Lcom/upsight/android/marketing/internal/content/MarketingContent;, "Lcom/upsight/android/marketing/internal/content/MarketingContent<*>;"
    if-eqz v0, :cond_0

    .line 212
    invoke-virtual {v0}, Lcom/upsight/android/marketing/internal/content/MarketingContent;->getEventQueue()Ljava/util/Queue;

    move-result-object v1

    invoke-interface {v1, p1}, Ljava/util/Queue;->add(Ljava/lang/Object;)Z

    goto :goto_0
.end method

.method public onApplicationStatus(Lcom/upsight/android/analytics/internal/session/ApplicationStatus;)V
    .locals 5
    .param p1, "appStatus"    # Lcom/upsight/android/analytics/internal/session/ApplicationStatus;
    .annotation runtime Lcom/upsight/android/persistence/annotation/Created;
    .end annotation

    .annotation runtime Lcom/upsight/android/persistence/annotation/Updated;
    .end annotation

    .prologue
    .line 248
    invoke-virtual {p1}, Lcom/upsight/android/analytics/internal/session/ApplicationStatus;->getState()Lcom/upsight/android/analytics/internal/session/ApplicationStatus$State;

    move-result-object v1

    sget-object v2, Lcom/upsight/android/analytics/internal/session/ApplicationStatus$State;->BACKGROUND:Lcom/upsight/android/analytics/internal/session/ApplicationStatus$State;

    if-ne v1, v2, :cond_0

    .line 249
    iget-object v1, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mUpsight:Lcom/upsight/android/UpsightContext;

    invoke-virtual {v1}, Lcom/upsight/android/UpsightContext;->getLogger()Lcom/upsight/android/logger/UpsightLogger;

    move-result-object v1

    const-string v2, "BillboardActivity"

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "Received application background event activity="

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v3, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    const/4 v4, 0x0

    new-array v4, v4, [Ljava/lang/Object;

    invoke-interface {v1, v2, v3, v4}, Lcom/upsight/android/logger/UpsightLogger;->d(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V

    .line 251
    iget-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mContent:Lcom/upsight/android/marketing/internal/content/MarketingContent;

    .line 252
    .local v0, "content":Lcom/upsight/android/marketing/internal/content/MarketingContent;
    if-eqz v0, :cond_0

    .line 253
    const-string v1, "app_backgrounded"

    invoke-virtual {v0, v1}, Lcom/upsight/android/marketing/internal/content/MarketingContent;->executeActions(Ljava/lang/String;)V

    .line 256
    .end local v0    # "content":Lcom/upsight/android/marketing/internal/content/MarketingContent;
    :cond_0
    return-void
.end method

.method public onAttachFragment(Landroid/app/Fragment;)V
    .locals 1
    .param p1, "fragment"    # Landroid/app/Fragment;

    .prologue
    .line 181
    invoke-super {p0, p1}, Landroid/app/Activity;->onAttachFragment(Landroid/app/Fragment;)V

    .line 184
    instance-of v0, p1, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;

    if-eqz v0, :cond_0

    .line 185
    check-cast p1, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;

    .end local p1    # "fragment":Landroid/app/Fragment;
    iput-object p1, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mFragment:Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;

    .line 187
    :cond_0
    return-void
.end method

.method protected onCreate(Landroid/os/Bundle;)V
    .locals 10
    .param p1, "savedInstanceState"    # Landroid/os/Bundle;

    .prologue
    const/4 v6, 0x0

    .line 78
    invoke-super {p0, p1}, Landroid/app/Activity;->onCreate(Landroid/os/Bundle;)V

    .line 80
    invoke-static {p0}, Lcom/upsight/android/Upsight;->createContext(Landroid/content/Context;)Lcom/upsight/android/UpsightContext;

    move-result-object v4

    .line 81
    .local v4, "upsight":Lcom/upsight/android/UpsightContext;
    const-string v5, "com.upsight.extension.marketing"

    invoke-virtual {v4, v5}, Lcom/upsight/android/UpsightContext;->getUpsightExtension(Ljava/lang/String;)Lcom/upsight/android/UpsightExtension;

    move-result-object v5

    check-cast v5, Lcom/upsight/android/UpsightMarketingExtension;

    move-object v1, v5

    check-cast v1, Lcom/upsight/android/UpsightMarketingExtension;

    .line 82
    .local v1, "extension":Lcom/upsight/android/UpsightMarketingExtension;
    if-nez v1, :cond_0

    .line 115
    :goto_0
    return-void

    .line 85
    :cond_0
    invoke-virtual {v1}, Lcom/upsight/android/UpsightMarketingExtension;->getComponent()Lcom/upsight/android/UpsightExtension$BaseComponent;

    move-result-object v5

    check-cast v5, Lcom/upsight/android/marketing/UpsightMarketingComponent;

    invoke-interface {v5, p0}, Lcom/upsight/android/marketing/UpsightMarketingComponent;->inject(Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;)V

    .line 87
    sget v5, Lcom/upsight/android/marketing/R$layout;->upsight_activity_billboard_management:I

    invoke-virtual {p0, v5}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->setContentView(I)V

    .line 90
    invoke-virtual {p0}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->getIntent()Landroid/content/Intent;

    move-result-object v3

    .line 91
    .local v3, "intent":Landroid/content/Intent;
    const-string v5, "marketingContentId"

    invoke-virtual {v3, v5}, Landroid/content/Intent;->getStringExtra(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    .line 93
    .local v2, "id":Ljava/lang/String;
    iget-object v5, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mUpsight:Lcom/upsight/android/UpsightContext;

    invoke-virtual {v5}, Lcom/upsight/android/UpsightContext;->getLogger()Lcom/upsight/android/logger/UpsightLogger;

    move-result-object v5

    const-string v7, "BillboardActivity"

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    const-string v9, "onCreate activity="

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v8, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    move-result-object v8

    const-string v9, " marketingContentId="

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v8, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    new-array v9, v6, [Ljava/lang/Object;

    invoke-interface {v5, v7, v8, v9}, Lcom/upsight/android/logger/UpsightLogger;->d(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V

    .line 95
    invoke-static {v2}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v5

    if-eqz v5, :cond_1

    .line 96
    invoke-virtual {p0}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->finish()V

    goto :goto_0

    .line 101
    :cond_1
    iget-object v5, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mContentStore:Lcom/upsight/android/marketing/internal/content/MarketingContentStore;

    invoke-interface {v5, v2}, Lcom/upsight/android/marketing/internal/content/MarketingContentStore;->get(Ljava/lang/String;)Lcom/upsight/android/analytics/internal/action/Actionable;

    move-result-object v0

    check-cast v0, Lcom/upsight/android/marketing/internal/content/MarketingContent;

    .line 102
    .local v0, "content":Lcom/upsight/android/marketing/internal/content/MarketingContent;
    if-eqz v0, :cond_3

    .line 103
    iput-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mContent:Lcom/upsight/android/marketing/internal/content/MarketingContent;

    .line 104
    invoke-virtual {v0}, Lcom/upsight/android/marketing/internal/content/MarketingContent;->getBoundBillboard()Lcom/upsight/android/marketing/internal/billboard/Billboard;

    move-result-object v5

    iput-object v5, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mBillboard:Lcom/upsight/android/marketing/internal/billboard/Billboard;

    .line 107
    if-nez p1, :cond_2

    const/4 v5, 0x1

    :goto_1
    iput-boolean v5, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mShouldAttachOnResume:Z

    .line 113
    :goto_2
    iget-object v5, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mUpsight:Lcom/upsight/android/UpsightContext;

    invoke-virtual {v5}, Lcom/upsight/android/UpsightContext;->getDataStore()Lcom/upsight/android/persistence/UpsightDataStore;

    move-result-object v5

    invoke-interface {v5, p0}, Lcom/upsight/android/persistence/UpsightDataStore;->subscribe(Ljava/lang/Object;)Lcom/upsight/android/persistence/UpsightSubscription;

    move-result-object v5

    iput-object v5, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mDataStoreSubscription:Lcom/upsight/android/persistence/UpsightSubscription;

    .line 114
    iget-object v5, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mUpsight:Lcom/upsight/android/UpsightContext;

    invoke-virtual {v5}, Lcom/upsight/android/UpsightContext;->getCoreComponent()Lcom/upsight/android/UpsightCoreComponent;

    move-result-object v5

    invoke-interface {v5}, Lcom/upsight/android/UpsightCoreComponent;->bus()Lcom/squareup/otto/Bus;

    move-result-object v5

    invoke-virtual {v5, p0}, Lcom/squareup/otto/Bus;->register(Ljava/lang/Object;)V

    goto :goto_0

    :cond_2
    move v5, v6

    .line 107
    goto :goto_1

    .line 109
    :cond_3
    invoke-virtual {p0}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->finish()V

    goto :goto_2
.end method

.method protected onDestroy()V
    .locals 4

    .prologue
    .line 171
    iget-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mDataStoreSubscription:Lcom/upsight/android/persistence/UpsightSubscription;

    invoke-interface {v0}, Lcom/upsight/android/persistence/UpsightSubscription;->unsubscribe()V

    .line 172
    iget-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mUpsight:Lcom/upsight/android/UpsightContext;

    invoke-virtual {v0}, Lcom/upsight/android/UpsightContext;->getCoreComponent()Lcom/upsight/android/UpsightCoreComponent;

    move-result-object v0

    invoke-interface {v0}, Lcom/upsight/android/UpsightCoreComponent;->bus()Lcom/squareup/otto/Bus;

    move-result-object v0

    invoke-virtual {v0, p0}, Lcom/squareup/otto/Bus;->unregister(Ljava/lang/Object;)V

    .line 174
    iget-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mUpsight:Lcom/upsight/android/UpsightContext;

    invoke-virtual {v0}, Lcom/upsight/android/UpsightContext;->getLogger()Lcom/upsight/android/logger/UpsightLogger;

    move-result-object v0

    const-string v1, "BillboardActivity"

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "onDestroy activity="

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {v2, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    const/4 v3, 0x0

    new-array v3, v3, [Ljava/lang/Object;

    invoke-interface {v0, v1, v2, v3}, Lcom/upsight/android/logger/UpsightLogger;->d(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V

    .line 176
    invoke-super {p0}, Landroid/app/Activity;->onDestroy()V

    .line 177
    return-void
.end method

.method protected onPause()V
    .locals 5

    .prologue
    const/4 v4, 0x0

    .line 162
    iput-boolean v4, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mIsForeground:Z

    .line 164
    iget-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mUpsight:Lcom/upsight/android/UpsightContext;

    invoke-virtual {v0}, Lcom/upsight/android/UpsightContext;->getLogger()Lcom/upsight/android/logger/UpsightLogger;

    move-result-object v0

    const-string v1, "BillboardActivity"

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "onPause activity="

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {v2, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    new-array v3, v4, [Ljava/lang/Object;

    invoke-interface {v0, v1, v2, v3}, Lcom/upsight/android/logger/UpsightLogger;->d(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V

    .line 166
    invoke-super {p0}, Landroid/app/Activity;->onPause()V

    .line 167
    return-void
.end method

.method protected onResume()V
    .locals 8

    .prologue
    const/4 v7, 0x0

    .line 119
    invoke-super {p0}, Landroid/app/Activity;->onResume()V

    .line 121
    const/4 v3, 0x1

    iput-boolean v3, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mIsForeground:Z

    .line 123
    iget-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mContent:Lcom/upsight/android/marketing/internal/content/MarketingContent;

    .line 125
    .local v0, "content":Lcom/upsight/android/marketing/internal/content/MarketingContent;, "Lcom/upsight/android/marketing/internal/content/MarketingContent<*>;"
    if-nez v0, :cond_1

    .line 158
    :cond_0
    return-void

    .line 130
    :cond_1
    iget-boolean v3, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mShouldAttachOnResume:Z

    if-eqz v3, :cond_2

    .line 131
    iput-boolean v7, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mShouldAttachOnResume:Z

    .line 134
    invoke-virtual {v0}, Lcom/upsight/android/marketing/internal/content/MarketingContent;->hasPendingDialog()Z

    move-result v3

    if-eqz v3, :cond_4

    .line 135
    invoke-virtual {v0}, Lcom/upsight/android/marketing/internal/content/MarketingContent;->popPendingDialog()Lcom/upsight/android/marketing/internal/content/MarketingContent$PendingDialog;

    move-result-object v3

    invoke-virtual {p0, v3}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->attachDialog(Lcom/upsight/android/marketing/internal/content/MarketingContent$PendingDialog;)V

    .line 142
    :cond_2
    :goto_0
    invoke-virtual {v0}, Lcom/upsight/android/marketing/internal/content/MarketingContent;->getEventQueue()Ljava/util/Queue;

    move-result-object v2

    .line 144
    .local v2, "eventQueue":Ljava/util/Queue;, "Ljava/util/Queue<Ljava/lang/Object;>;"
    iget-object v3, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->mUpsight:Lcom/upsight/android/UpsightContext;

    invoke-virtual {v3}, Lcom/upsight/android/UpsightContext;->getLogger()Lcom/upsight/android/logger/UpsightLogger;

    move-result-object v3

    const-string v4, "BillboardActivity"

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "onResume activity="

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    invoke-virtual {v5, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    move-result-object v5

    const-string v6, " eventQueueSize="

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    invoke-interface {v2}, Ljava/util/Queue;->size()I

    move-result v6

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v5

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    new-array v6, v7, [Ljava/lang/Object;

    invoke-interface {v3, v4, v5, v6}, Lcom/upsight/android/logger/UpsightLogger;->d(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V

    .line 146
    :cond_3
    :goto_1
    invoke-interface {v2}, Ljava/util/Queue;->isEmpty()Z

    move-result v3

    if-nez v3, :cond_0

    .line 147
    invoke-interface {v2}, Ljava/util/Queue;->poll()Ljava/lang/Object;

    move-result-object v1

    .line 148
    .local v1, "event":Ljava/lang/Object;
    instance-of v3, v1, Lcom/upsight/android/marketing/internal/content/MarketingContent$SubcontentAvailabilityEvent;

    if-eqz v3, :cond_5

    .line 149
    check-cast v1, Lcom/upsight/android/marketing/internal/content/MarketingContent$SubcontentAvailabilityEvent;

    .end local v1    # "event":Ljava/lang/Object;
    invoke-direct {p0, v1}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->handle(Lcom/upsight/android/marketing/internal/content/MarketingContent$SubcontentAvailabilityEvent;)V

    goto :goto_1

    .line 137
    .end local v2    # "eventQueue":Ljava/util/Queue;, "Ljava/util/Queue<Ljava/lang/Object;>;"
    :cond_4
    invoke-virtual {p0, v0}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->attachBillboard(Lcom/upsight/android/marketing/internal/content/MarketingContent;)V

    goto :goto_0

    .line 150
    .restart local v1    # "event":Ljava/lang/Object;
    .restart local v2    # "eventQueue":Ljava/util/Queue;, "Ljava/util/Queue<Ljava/lang/Object;>;"
    :cond_5
    instance-of v3, v1, Lcom/upsight/android/marketing/internal/content/MarketingContent$SubdialogAvailabilityEvent;

    if-eqz v3, :cond_6

    .line 151
    check-cast v1, Lcom/upsight/android/marketing/internal/content/MarketingContent$SubdialogAvailabilityEvent;

    .end local v1    # "event":Ljava/lang/Object;
    invoke-direct {p0, v1}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->handle(Lcom/upsight/android/marketing/internal/content/MarketingContent$SubdialogAvailabilityEvent;)V

    goto :goto_1

    .line 152
    .restart local v1    # "event":Ljava/lang/Object;
    :cond_6
    instance-of v3, v1, Lcom/upsight/android/marketing/internal/content/MarketingContent$ScopelessAvailabilityEvent;

    if-eqz v3, :cond_7

    .line 153
    check-cast v1, Lcom/upsight/android/marketing/internal/content/MarketingContent$ScopelessAvailabilityEvent;

    .end local v1    # "event":Ljava/lang/Object;
    invoke-direct {p0, v1}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->handle(Lcom/upsight/android/marketing/internal/content/MarketingContent$ScopelessAvailabilityEvent;)V

    goto :goto_1

    .line 154
    .restart local v1    # "event":Ljava/lang/Object;
    :cond_7
    instance-of v3, v1, Lcom/upsight/android/marketing/internal/content/MarketingContentActions$DestroyEvent;

    if-eqz v3, :cond_3

    .line 155
    check-cast v1, Lcom/upsight/android/marketing/internal/content/MarketingContentActions$DestroyEvent;

    .end local v1    # "event":Ljava/lang/Object;
    invoke-direct {p0, v1}, Lcom/upsight/android/marketing/internal/billboard/BillboardManagementActivity;->handle(Lcom/upsight/android/marketing/internal/content/MarketingContentActions$DestroyEvent;)V

    goto :goto_1
.end method

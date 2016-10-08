.class public final Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;
.super Landroid/app/DialogFragment;
.source "BillboardFragment.java"


# annotations
.annotation system Ldalvik/annotation/MemberClasses;
    value = {
        Lcom/upsight/android/marketing/internal/billboard/BillboardFragment$BackPressHandler;
    }
.end annotation


# static fields
.field private static final BUNDLE_KEY_LANDSCAPE_HEIGHT:Ljava/lang/String; = "landscapeHeight"

.field private static final BUNDLE_KEY_LANDSCAPE_WIDTH:Ljava/lang/String; = "landscapeWidth"

.field private static final BUNDLE_KEY_PORTRAIT_HEIGHT:Ljava/lang/String; = "portraitHeight"

.field private static final BUNDLE_KEY_PORTRAIT_WIDTH:Ljava/lang/String; = "portraitWidth"


# instance fields
.field private mBackPressHandler:Lcom/upsight/android/marketing/internal/billboard/BillboardFragment$BackPressHandler;

.field private mContentViewContainer:Landroid/view/ViewGroup;

.field private mRootView:Landroid/view/ViewGroup;


# direct methods
.method public constructor <init>()V
    .locals 1

    .prologue
    const/4 v0, 0x0

    .line 33
    invoke-direct {p0}, Landroid/app/DialogFragment;-><init>()V

    .line 58
    iput-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->mRootView:Landroid/view/ViewGroup;

    .line 63
    iput-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->mContentViewContainer:Landroid/view/ViewGroup;

    .line 68
    iput-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->mBackPressHandler:Lcom/upsight/android/marketing/internal/billboard/BillboardFragment$BackPressHandler;

    return-void
.end method

.method static synthetic access$000(Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;)Lcom/upsight/android/marketing/internal/billboard/BillboardFragment$BackPressHandler;
    .locals 1
    .param p0, "x0"    # Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;

    .prologue
    .line 33
    iget-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->mBackPressHandler:Lcom/upsight/android/marketing/internal/billboard/BillboardFragment$BackPressHandler;

    return-object v0
.end method

.method public static newInstance(Landroid/content/Context;Ljava/util/Set;)Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;
    .locals 6
    .param p0, "context"    # Landroid/content/Context;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Landroid/content/Context;",
            "Ljava/util/Set",
            "<",
            "Lcom/upsight/android/marketing/UpsightBillboard$Dimensions;",
            ">;)",
            "Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;"
        }
    .end annotation

    .prologue
    .line 78
    .local p1, "dimensions":Ljava/util/Set;, "Ljava/util/Set<Lcom/upsight/android/marketing/UpsightBillboard$Dimensions;>;"
    new-instance v2, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;

    invoke-direct {v2}, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;-><init>()V

    .line 80
    .local v2, "fragment":Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;
    new-instance v0, Landroid/os/Bundle;

    invoke-direct {v0}, Landroid/os/Bundle;-><init>()V

    .line 81
    .local v0, "args":Landroid/os/Bundle;
    if-eqz p1, :cond_1

    .line 82
    invoke-interface {p1}, Ljava/util/Set;->iterator()Ljava/util/Iterator;

    move-result-object v3

    :cond_0
    :goto_0
    invoke-interface {v3}, Ljava/util/Iterator;->hasNext()Z

    move-result v4

    if-eqz v4, :cond_1

    invoke-interface {v3}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/upsight/android/marketing/UpsightBillboard$Dimensions;

    .line 83
    .local v1, "dimension":Lcom/upsight/android/marketing/UpsightBillboard$Dimensions;
    iget v4, v1, Lcom/upsight/android/marketing/UpsightBillboard$Dimensions;->width:I

    if-lez v4, :cond_0

    iget v4, v1, Lcom/upsight/android/marketing/UpsightBillboard$Dimensions;->height:I

    if-lez v4, :cond_0

    .line 84
    sget-object v4, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment$2;->$SwitchMap$com$upsight$android$marketing$UpsightBillboard$Dimensions$LayoutOrientation:[I

    iget-object v5, v1, Lcom/upsight/android/marketing/UpsightBillboard$Dimensions;->layout:Lcom/upsight/android/marketing/UpsightBillboard$Dimensions$LayoutOrientation;

    invoke-virtual {v5}, Lcom/upsight/android/marketing/UpsightBillboard$Dimensions$LayoutOrientation;->ordinal()I

    move-result v5

    aget v4, v4, v5

    packed-switch v4, :pswitch_data_0

    goto :goto_0

    .line 86
    :pswitch_0
    const-string v4, "portraitWidth"

    iget v5, v1, Lcom/upsight/android/marketing/UpsightBillboard$Dimensions;->width:I

    invoke-virtual {v0, v4, v5}, Landroid/os/Bundle;->putInt(Ljava/lang/String;I)V

    .line 87
    const-string v4, "portraitHeight"

    iget v5, v1, Lcom/upsight/android/marketing/UpsightBillboard$Dimensions;->height:I

    invoke-virtual {v0, v4, v5}, Landroid/os/Bundle;->putInt(Ljava/lang/String;I)V

    goto :goto_0

    .line 90
    :pswitch_1
    const-string v4, "landscapeWidth"

    iget v5, v1, Lcom/upsight/android/marketing/UpsightBillboard$Dimensions;->width:I

    invoke-virtual {v0, v4, v5}, Landroid/os/Bundle;->putInt(Ljava/lang/String;I)V

    .line 91
    const-string v4, "landscapeHeight"

    iget v5, v1, Lcom/upsight/android/marketing/UpsightBillboard$Dimensions;->height:I

    invoke-virtual {v0, v4, v5}, Landroid/os/Bundle;->putInt(Ljava/lang/String;I)V

    goto :goto_0

    .line 97
    .end local v1    # "dimension":Lcom/upsight/android/marketing/UpsightBillboard$Dimensions;
    :cond_1
    invoke-virtual {v2, v0}, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->setArguments(Landroid/os/Bundle;)V

    .line 102
    const/4 v3, 0x1

    invoke-virtual {v2, v3}, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->setRetainInstance(Z)V

    .line 103
    new-instance v3, Landroid/widget/LinearLayout;

    invoke-virtual {p0}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v4

    invoke-direct {v3, v4}, Landroid/widget/LinearLayout;-><init>(Landroid/content/Context;)V

    iput-object v3, v2, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->mContentViewContainer:Landroid/view/ViewGroup;

    .line 105
    return-object v2

    .line 84
    nop

    :pswitch_data_0
    .packed-switch 0x1
        :pswitch_0
        :pswitch_1
    .end packed-switch
.end method

.method private setDialogSize(II)V
    .locals 3
    .param p1, "width"    # I
    .param p2, "height"    # I

    .prologue
    .line 217
    invoke-virtual {p0}, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->getDialog()Landroid/app/Dialog;

    move-result-object v0

    .line 218
    .local v0, "dialog":Landroid/app/Dialog;
    if-eqz v0, :cond_0

    .line 219
    invoke-virtual {v0}, Landroid/app/Dialog;->getWindow()Landroid/view/Window;

    move-result-object v2

    .line 220
    .local v2, "window":Landroid/view/Window;
    invoke-virtual {v2}, Landroid/view/Window;->getAttributes()Landroid/view/WindowManager$LayoutParams;

    move-result-object v1

    .line 221
    .local v1, "params":Landroid/view/WindowManager$LayoutParams;
    iput p1, v1, Landroid/view/WindowManager$LayoutParams;->width:I

    .line 222
    iput p2, v1, Landroid/view/WindowManager$LayoutParams;->height:I

    .line 223
    invoke-virtual {v2, v1}, Landroid/view/Window;->setAttributes(Landroid/view/WindowManager$LayoutParams;)V

    .line 225
    .end local v1    # "params":Landroid/view/WindowManager$LayoutParams;
    .end local v2    # "window":Landroid/view/Window;
    :cond_0
    return-void
.end method


# virtual methods
.method public getContentViewContainer()Landroid/view/ViewGroup;
    .locals 1

    .prologue
    .line 198
    iget-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->mContentViewContainer:Landroid/view/ViewGroup;

    return-object v0
.end method

.method public onConfigurationChanged(Landroid/content/res/Configuration;)V
    .locals 1
    .param p1, "newConfig"    # Landroid/content/res/Configuration;

    .prologue
    .line 110
    invoke-super {p0, p1}, Landroid/app/DialogFragment;->onConfigurationChanged(Landroid/content/res/Configuration;)V

    .line 114
    invoke-virtual {p0}, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->getFragmentManager()Landroid/app/FragmentManager;

    move-result-object v0

    invoke-virtual {v0}, Landroid/app/FragmentManager;->beginTransaction()Landroid/app/FragmentTransaction;

    move-result-object v0

    .line 115
    invoke-virtual {v0, p0}, Landroid/app/FragmentTransaction;->detach(Landroid/app/Fragment;)Landroid/app/FragmentTransaction;

    move-result-object v0

    .line 116
    invoke-virtual {v0, p0}, Landroid/app/FragmentTransaction;->attach(Landroid/app/Fragment;)Landroid/app/FragmentTransaction;

    move-result-object v0

    .line 117
    invoke-virtual {v0}, Landroid/app/FragmentTransaction;->commit()I

    .line 118
    return-void
.end method

.method public onCreateDialog(Landroid/os/Bundle;)Landroid/app/Dialog;
    .locals 2
    .param p1, "savedInstanceState"    # Landroid/os/Bundle;

    .prologue
    .line 131
    invoke-super {p0, p1}, Landroid/app/DialogFragment;->onCreateDialog(Landroid/os/Bundle;)Landroid/app/Dialog;

    move-result-object v0

    .line 134
    .local v0, "dialog":Landroid/app/Dialog;
    new-instance v1, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment$1;

    invoke-direct {v1, p0}, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment$1;-><init>(Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;)V

    invoke-virtual {v0, v1}, Landroid/app/Dialog;->setOnKeyListener(Landroid/content/DialogInterface$OnKeyListener;)V

    .line 145
    return-object v0
.end method

.method public onCreateView(Landroid/view/LayoutInflater;Landroid/view/ViewGroup;Landroid/os/Bundle;)Landroid/view/View;
    .locals 4
    .param p1, "inflater"    # Landroid/view/LayoutInflater;
    .param p2, "container"    # Landroid/view/ViewGroup;
    .param p3, "savedInstanceState"    # Landroid/os/Bundle;

    .prologue
    const/4 v3, -0x1

    .line 122
    iget-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->mContentViewContainer:Landroid/view/ViewGroup;

    if-eqz v0, :cond_0

    .line 123
    sget v0, Lcom/upsight/android/marketing/R$layout;->upsight_fragment_billboard:I

    const/4 v1, 0x0

    invoke-virtual {p1, v0, p2, v1}, Landroid/view/LayoutInflater;->inflate(ILandroid/view/ViewGroup;Z)Landroid/view/View;

    move-result-object v0

    check-cast v0, Landroid/view/ViewGroup;

    iput-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->mRootView:Landroid/view/ViewGroup;

    .line 124
    iget-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->mRootView:Landroid/view/ViewGroup;

    iget-object v1, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->mContentViewContainer:Landroid/view/ViewGroup;

    new-instance v2, Landroid/view/ViewGroup$LayoutParams;

    invoke-direct {v2, v3, v3}, Landroid/view/ViewGroup$LayoutParams;-><init>(II)V

    invoke-virtual {v0, v1, v2}, Landroid/view/ViewGroup;->addView(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V

    .line 126
    :cond_0
    iget-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->mRootView:Landroid/view/ViewGroup;

    return-object v0
.end method

.method public onDestroyView()V
    .locals 3

    .prologue
    .line 175
    iget-object v1, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->mContentViewContainer:Landroid/view/ViewGroup;

    if-eqz v1, :cond_0

    .line 176
    iget-object v1, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->mRootView:Landroid/view/ViewGroup;

    iget-object v2, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->mContentViewContainer:Landroid/view/ViewGroup;

    invoke-virtual {v1, v2}, Landroid/view/ViewGroup;->removeView(Landroid/view/View;)V

    .line 181
    :cond_0
    invoke-virtual {p0}, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->getDialog()Landroid/app/Dialog;

    move-result-object v0

    .line 182
    .local v0, "dialog":Landroid/app/Dialog;
    if-eqz v0, :cond_1

    invoke-virtual {p0}, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->getRetainInstance()Z

    move-result v1

    if-eqz v1, :cond_1

    .line 183
    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Landroid/app/Dialog;->setDismissMessage(Landroid/os/Message;)V

    .line 186
    :cond_1
    invoke-super {p0}, Landroid/app/DialogFragment;->onDestroyView()V

    .line 187
    return-void
.end method

.method public onResume()V
    .locals 4

    .prologue
    .line 161
    invoke-virtual {p0}, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->getResources()Landroid/content/res/Resources;

    move-result-object v2

    invoke-virtual {v2}, Landroid/content/res/Resources;->getConfiguration()Landroid/content/res/Configuration;

    move-result-object v2

    iget v1, v2, Landroid/content/res/Configuration;->orientation:I

    .line 162
    .local v1, "orientation":I
    invoke-virtual {p0}, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->getArguments()Landroid/os/Bundle;

    move-result-object v0

    .line 163
    .local v0, "args":Landroid/os/Bundle;
    const/4 v2, 0x1

    if-ne v1, v2, :cond_1

    const-string v2, "portraitWidth"

    invoke-virtual {v0, v2}, Landroid/os/Bundle;->containsKey(Ljava/lang/String;)Z

    move-result v2

    if-eqz v2, :cond_1

    .line 164
    const-string v2, "portraitWidth"

    invoke-virtual {v0, v2}, Landroid/os/Bundle;->getInt(Ljava/lang/String;)I

    move-result v2

    const-string v3, "portraitHeight"

    invoke-virtual {v0, v3}, Landroid/os/Bundle;->getInt(Ljava/lang/String;)I

    move-result v3

    invoke-direct {p0, v2, v3}, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->setDialogSize(II)V

    .line 169
    :cond_0
    :goto_0
    invoke-super {p0}, Landroid/app/DialogFragment;->onResume()V

    .line 170
    return-void

    .line 165
    :cond_1
    const/4 v2, 0x2

    if-ne v1, v2, :cond_0

    const-string v2, "landscapeWidth"

    invoke-virtual {v0, v2}, Landroid/os/Bundle;->containsKey(Ljava/lang/String;)Z

    move-result v2

    if-eqz v2, :cond_0

    .line 166
    const-string v2, "landscapeWidth"

    invoke-virtual {v0, v2}, Landroid/os/Bundle;->getInt(Ljava/lang/String;)I

    move-result v2

    const-string v3, "landscapeHeight"

    invoke-virtual {v0, v3}, Landroid/os/Bundle;->getInt(Ljava/lang/String;)I

    move-result v3

    invoke-direct {p0, v2, v3}, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->setDialogSize(II)V

    goto :goto_0
.end method

.method public onStart()V
    .locals 1

    .prologue
    .line 150
    invoke-super {p0}, Landroid/app/DialogFragment;->onStart()V

    .line 154
    iget-object v0, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->mContentViewContainer:Landroid/view/ViewGroup;

    if-nez v0, :cond_0

    .line 155
    invoke-virtual {p0}, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->dismiss()V

    .line 157
    :cond_0
    return-void
.end method

.method public setBackPressHandler(Lcom/upsight/android/marketing/internal/billboard/BillboardFragment$BackPressHandler;)V
    .locals 0
    .param p1, "handler"    # Lcom/upsight/android/marketing/internal/billboard/BillboardFragment$BackPressHandler;

    .prologue
    .line 207
    iput-object p1, p0, Lcom/upsight/android/marketing/internal/billboard/BillboardFragment;->mBackPressHandler:Lcom/upsight/android/marketing/internal/billboard/BillboardFragment$BackPressHandler;

    .line 208
    return-void
.end method

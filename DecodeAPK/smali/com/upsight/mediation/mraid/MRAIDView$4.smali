.class Lcom/upsight/mediation/mraid/MRAIDView$4;
.super Ljava/lang/Object;
.source "MRAIDView.java"

# interfaces
.implements Ljava/lang/Runnable;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/upsight/mediation/mraid/MRAIDView;->close(Z)V
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x0
    name = null
.end annotation


# instance fields
.field final synthetic this$0:Lcom/upsight/mediation/mraid/MRAIDView;

.field final synthetic val$sendCallback:Z


# direct methods
.method constructor <init>(Lcom/upsight/mediation/mraid/MRAIDView;Z)V
    .locals 0
    .param p1, "this$0"    # Lcom/upsight/mediation/mraid/MRAIDView;

    .prologue
    .line 399
    iput-object p1, p0, Lcom/upsight/mediation/mraid/MRAIDView$4;->this$0:Lcom/upsight/mediation/mraid/MRAIDView;

    iput-boolean p2, p0, Lcom/upsight/mediation/mraid/MRAIDView$4;->val$sendCallback:Z

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public run()V
    .locals 3

    .prologue
    const/4 v2, 0x1

    .line 402
    iget-object v0, p0, Lcom/upsight/mediation/mraid/MRAIDView$4;->this$0:Lcom/upsight/mediation/mraid/MRAIDView;

    # getter for: Lcom/upsight/mediation/mraid/MRAIDView;->state:I
    invoke-static {v0}, Lcom/upsight/mediation/mraid/MRAIDView;->access$900(Lcom/upsight/mediation/mraid/MRAIDView;)I

    move-result v0

    if-eqz v0, :cond_1

    iget-object v0, p0, Lcom/upsight/mediation/mraid/MRAIDView$4;->this$0:Lcom/upsight/mediation/mraid/MRAIDView;

    # getter for: Lcom/upsight/mediation/mraid/MRAIDView;->state:I
    invoke-static {v0}, Lcom/upsight/mediation/mraid/MRAIDView;->access$900(Lcom/upsight/mediation/mraid/MRAIDView;)I

    move-result v0

    if-ne v0, v2, :cond_0

    iget-object v0, p0, Lcom/upsight/mediation/mraid/MRAIDView$4;->this$0:Lcom/upsight/mediation/mraid/MRAIDView;

    # getter for: Lcom/upsight/mediation/mraid/MRAIDView;->isInterstitial:Z
    invoke-static {v0}, Lcom/upsight/mediation/mraid/MRAIDView;->access$400(Lcom/upsight/mediation/mraid/MRAIDView;)Z

    move-result v0

    if-eqz v0, :cond_1

    :cond_0
    iget-object v0, p0, Lcom/upsight/mediation/mraid/MRAIDView$4;->this$0:Lcom/upsight/mediation/mraid/MRAIDView;

    # getter for: Lcom/upsight/mediation/mraid/MRAIDView;->state:I
    invoke-static {v0}, Lcom/upsight/mediation/mraid/MRAIDView;->access$900(Lcom/upsight/mediation/mraid/MRAIDView;)I

    move-result v0

    const/4 v1, 0x4

    if-ne v0, v1, :cond_2

    .line 410
    :cond_1
    :goto_0
    return-void

    .line 405
    :cond_2
    iget-object v0, p0, Lcom/upsight/mediation/mraid/MRAIDView$4;->this$0:Lcom/upsight/mediation/mraid/MRAIDView;

    # getter for: Lcom/upsight/mediation/mraid/MRAIDView;->state:I
    invoke-static {v0}, Lcom/upsight/mediation/mraid/MRAIDView;->access$900(Lcom/upsight/mediation/mraid/MRAIDView;)I

    move-result v0

    if-eq v0, v2, :cond_3

    iget-object v0, p0, Lcom/upsight/mediation/mraid/MRAIDView$4;->this$0:Lcom/upsight/mediation/mraid/MRAIDView;

    # getter for: Lcom/upsight/mediation/mraid/MRAIDView;->state:I
    invoke-static {v0}, Lcom/upsight/mediation/mraid/MRAIDView;->access$900(Lcom/upsight/mediation/mraid/MRAIDView;)I

    move-result v0

    const/4 v1, 0x2

    if-ne v0, v1, :cond_4

    .line 406
    :cond_3
    iget-object v0, p0, Lcom/upsight/mediation/mraid/MRAIDView$4;->this$0:Lcom/upsight/mediation/mraid/MRAIDView;

    iget-boolean v1, p0, Lcom/upsight/mediation/mraid/MRAIDView$4;->val$sendCallback:Z

    # invokes: Lcom/upsight/mediation/mraid/MRAIDView;->closeFromExpanded(Z)V
    invoke-static {v0, v1}, Lcom/upsight/mediation/mraid/MRAIDView;->access$1000(Lcom/upsight/mediation/mraid/MRAIDView;Z)V

    goto :goto_0

    .line 407
    :cond_4
    iget-object v0, p0, Lcom/upsight/mediation/mraid/MRAIDView$4;->this$0:Lcom/upsight/mediation/mraid/MRAIDView;

    # getter for: Lcom/upsight/mediation/mraid/MRAIDView;->state:I
    invoke-static {v0}, Lcom/upsight/mediation/mraid/MRAIDView;->access$900(Lcom/upsight/mediation/mraid/MRAIDView;)I

    move-result v0

    const/4 v1, 0x3

    if-ne v0, v1, :cond_1

    .line 408
    iget-object v0, p0, Lcom/upsight/mediation/mraid/MRAIDView$4;->this$0:Lcom/upsight/mediation/mraid/MRAIDView;

    iget-boolean v1, p0, Lcom/upsight/mediation/mraid/MRAIDView$4;->val$sendCallback:Z

    # invokes: Lcom/upsight/mediation/mraid/MRAIDView;->closeFromResized(Z)V
    invoke-static {v0, v1}, Lcom/upsight/mediation/mraid/MRAIDView;->access$1100(Lcom/upsight/mediation/mraid/MRAIDView;Z)V

    goto :goto_0
.end method

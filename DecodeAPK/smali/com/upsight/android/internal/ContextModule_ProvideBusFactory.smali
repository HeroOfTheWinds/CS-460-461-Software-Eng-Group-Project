.class public final Lcom/upsight/android/internal/ContextModule_ProvideBusFactory;
.super Ljava/lang/Object;
.source "ContextModule_ProvideBusFactory.java"

# interfaces
.implements Ldagger/internal/Factory;


# annotations
.annotation system Ldalvik/annotation/Signature;
    value = {
        "Ljava/lang/Object;",
        "Ldagger/internal/Factory",
        "<",
        "Lcom/squareup/otto/Bus;",
        ">;"
    }
.end annotation


# static fields
.field static final synthetic $assertionsDisabled:Z


# instance fields
.field private final module:Lcom/upsight/android/internal/ContextModule;


# direct methods
.method static constructor <clinit>()V
    .locals 1

    .prologue
    .line 8
    const-class v0, Lcom/upsight/android/internal/ContextModule_ProvideBusFactory;

    invoke-virtual {v0}, Ljava/lang/Class;->desiredAssertionStatus()Z

    move-result v0

    if-nez v0, :cond_0

    const/4 v0, 0x1

    :goto_0
    sput-boolean v0, Lcom/upsight/android/internal/ContextModule_ProvideBusFactory;->$assertionsDisabled:Z

    return-void

    :cond_0
    const/4 v0, 0x0

    goto :goto_0
.end method

.method public constructor <init>(Lcom/upsight/android/internal/ContextModule;)V
    .locals 1
    .param p1, "module"    # Lcom/upsight/android/internal/ContextModule;

    .prologue
    .line 15
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    .line 16
    sget-boolean v0, Lcom/upsight/android/internal/ContextModule_ProvideBusFactory;->$assertionsDisabled:Z

    if-nez v0, :cond_0

    if-nez p1, :cond_0

    new-instance v0, Ljava/lang/AssertionError;

    invoke-direct {v0}, Ljava/lang/AssertionError;-><init>()V

    throw v0

    .line 17
    :cond_0
    iput-object p1, p0, Lcom/upsight/android/internal/ContextModule_ProvideBusFactory;->module:Lcom/upsight/android/internal/ContextModule;

    .line 18
    return-void
.end method

.method public static create(Lcom/upsight/android/internal/ContextModule;)Ldagger/internal/Factory;
    .locals 1
    .param p0, "module"    # Lcom/upsight/android/internal/ContextModule;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Lcom/upsight/android/internal/ContextModule;",
            ")",
            "Ldagger/internal/Factory",
            "<",
            "Lcom/squareup/otto/Bus;",
            ">;"
        }
    .end annotation

    .prologue
    .line 27
    new-instance v0, Lcom/upsight/android/internal/ContextModule_ProvideBusFactory;

    invoke-direct {v0, p0}, Lcom/upsight/android/internal/ContextModule_ProvideBusFactory;-><init>(Lcom/upsight/android/internal/ContextModule;)V

    return-object v0
.end method


# virtual methods
.method public get()Lcom/squareup/otto/Bus;
    .locals 2

    .prologue
    .line 22
    iget-object v0, p0, Lcom/upsight/android/internal/ContextModule_ProvideBusFactory;->module:Lcom/upsight/android/internal/ContextModule;

    .line 23
    invoke-virtual {v0}, Lcom/upsight/android/internal/ContextModule;->provideBus()Lcom/squareup/otto/Bus;

    move-result-object v0

    const-string v1, "Cannot return null from a non-@Nullable @Provides method"

    .line 22
    invoke-static {v0, v1}, Ldagger/internal/Preconditions;->checkNotNull(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/squareup/otto/Bus;

    return-object v0
.end method

.method public bridge synthetic get()Ljava/lang/Object;
    .locals 1

    .prologue
    .line 8
    invoke-virtual {p0}, Lcom/upsight/android/internal/ContextModule_ProvideBusFactory;->get()Lcom/squareup/otto/Bus;

    move-result-object v0

    return-object v0
.end method

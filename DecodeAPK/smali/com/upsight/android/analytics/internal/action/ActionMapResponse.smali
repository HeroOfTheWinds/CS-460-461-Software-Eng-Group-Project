.class public final Lcom/upsight/android/analytics/internal/action/ActionMapResponse;
.super Ljava/lang/Object;
.source "ActionMapResponse.java"


# annotations
.annotation runtime Lcom/upsight/android/persistence/annotation/UpsightStorableType;
    value = "upsight.action_map"
.end annotation


# instance fields
.field actionFactory:Ljava/lang/String;
    .annotation runtime Lcom/google/gson/annotations/Expose;
    .end annotation

    .annotation runtime Lcom/google/gson/annotations/SerializedName;
        value = "action_factory"
    .end annotation
.end field

.field actionMap:Lcom/google/gson/JsonArray;
    .annotation runtime Lcom/google/gson/annotations/Expose;
    .end annotation

    .annotation runtime Lcom/google/gson/annotations/SerializedName;
        value = "action_map"
    .end annotation
.end field

.field actionMapId:Ljava/lang/String;
    .annotation runtime Lcom/google/gson/annotations/Expose;
    .end annotation

    .annotation runtime Lcom/google/gson/annotations/SerializedName;
        value = "id"
    .end annotation
.end field

.field id:Ljava/lang/String;
    .annotation runtime Lcom/upsight/android/persistence/annotation/UpsightStorableIdentifier;
    .end annotation
.end field


# direct methods
.method public constructor <init>()V
    .locals 0

    .prologue
    .line 17
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public equals(Ljava/lang/Object;)Z
    .locals 5
    .param p1, "o"    # Ljava/lang/Object;

    .prologue
    const/4 v1, 0x1

    const/4 v2, 0x0

    .line 63
    if-ne p0, p1, :cond_1

    .line 76
    :cond_0
    :goto_0
    return v1

    .line 66
    :cond_1
    if-eqz p1, :cond_2

    invoke-virtual {p0}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object v3

    invoke-virtual {p1}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object v4

    if-eq v3, v4, :cond_3

    :cond_2
    move v1, v2

    .line 67
    goto :goto_0

    :cond_3
    move-object v0, p1

    .line 70
    check-cast v0, Lcom/upsight/android/analytics/internal/action/ActionMapResponse;

    .line 72
    .local v0, "that":Lcom/upsight/android/analytics/internal/action/ActionMapResponse;
    iget-object v3, p0, Lcom/upsight/android/analytics/internal/action/ActionMapResponse;->id:Ljava/lang/String;

    if-eqz v3, :cond_4

    iget-object v3, p0, Lcom/upsight/android/analytics/internal/action/ActionMapResponse;->id:Ljava/lang/String;

    iget-object v4, v0, Lcom/upsight/android/analytics/internal/action/ActionMapResponse;->id:Ljava/lang/String;

    invoke-virtual {v3, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v3

    if-nez v3, :cond_0

    :goto_1
    move v1, v2

    .line 73
    goto :goto_0

    .line 72
    :cond_4
    iget-object v3, v0, Lcom/upsight/android/analytics/internal/action/ActionMapResponse;->id:Ljava/lang/String;

    if-eqz v3, :cond_0

    goto :goto_1
.end method

.method public getActionFactory()Ljava/lang/String;
    .locals 1

    .prologue
    .line 49
    iget-object v0, p0, Lcom/upsight/android/analytics/internal/action/ActionMapResponse;->actionFactory:Ljava/lang/String;

    return-object v0
.end method

.method public getActionMap()Lcom/google/gson/JsonArray;
    .locals 1

    .prologue
    .line 58
    iget-object v0, p0, Lcom/upsight/android/analytics/internal/action/ActionMapResponse;->actionMap:Lcom/google/gson/JsonArray;

    return-object v0
.end method

.method public getActionMapId()Ljava/lang/String;
    .locals 1

    .prologue
    .line 40
    iget-object v0, p0, Lcom/upsight/android/analytics/internal/action/ActionMapResponse;->actionMapId:Ljava/lang/String;

    return-object v0
.end method

.method public hashCode()I
    .locals 1

    .prologue
    .line 81
    iget-object v0, p0, Lcom/upsight/android/analytics/internal/action/ActionMapResponse;->id:Ljava/lang/String;

    if-eqz v0, :cond_0

    iget-object v0, p0, Lcom/upsight/android/analytics/internal/action/ActionMapResponse;->id:Ljava/lang/String;

    invoke-virtual {v0}, Ljava/lang/String;->hashCode()I

    move-result v0

    :goto_0
    return v0

    :cond_0
    const/4 v0, 0x0

    goto :goto_0
.end method

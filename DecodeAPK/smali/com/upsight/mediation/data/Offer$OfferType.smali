.class public final enum Lcom/upsight/mediation/data/Offer$OfferType;
.super Ljava/lang/Enum;
.source "Offer.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lcom/upsight/mediation/data/Offer;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x4019
    name = "OfferType"
.end annotation

.annotation system Ldalvik/annotation/Signature;
    value = {
        "Ljava/lang/Enum",
        "<",
        "Lcom/upsight/mediation/data/Offer$OfferType;",
        ">;"
    }
.end annotation


# static fields
.field private static final synthetic $VALUES:[Lcom/upsight/mediation/data/Offer$OfferType;

.field public static final enum Bonus:Lcom/upsight/mediation/data/Offer$OfferType;

.field public static final enum Discount:Lcom/upsight/mediation/data/Offer$OfferType;

.field public static final enum Standard:Lcom/upsight/mediation/data/Offer$OfferType;


# instance fields
.field public value:I


# direct methods
.method static constructor <clinit>()V
    .locals 6

    .prologue
    const/4 v5, 0x3

    const/4 v4, 0x0

    const/4 v3, 0x2

    const/4 v2, 0x1

    .line 22
    new-instance v0, Lcom/upsight/mediation/data/Offer$OfferType;

    const-string v1, "Discount"

    invoke-direct {v0, v1, v4, v2}, Lcom/upsight/mediation/data/Offer$OfferType;-><init>(Ljava/lang/String;II)V

    sput-object v0, Lcom/upsight/mediation/data/Offer$OfferType;->Discount:Lcom/upsight/mediation/data/Offer$OfferType;

    .line 23
    new-instance v0, Lcom/upsight/mediation/data/Offer$OfferType;

    const-string v1, "Standard"

    invoke-direct {v0, v1, v2, v3}, Lcom/upsight/mediation/data/Offer$OfferType;-><init>(Ljava/lang/String;II)V

    sput-object v0, Lcom/upsight/mediation/data/Offer$OfferType;->Standard:Lcom/upsight/mediation/data/Offer$OfferType;

    .line 24
    new-instance v0, Lcom/upsight/mediation/data/Offer$OfferType;

    const-string v1, "Bonus"

    invoke-direct {v0, v1, v3, v5}, Lcom/upsight/mediation/data/Offer$OfferType;-><init>(Ljava/lang/String;II)V

    sput-object v0, Lcom/upsight/mediation/data/Offer$OfferType;->Bonus:Lcom/upsight/mediation/data/Offer$OfferType;

    .line 21
    new-array v0, v5, [Lcom/upsight/mediation/data/Offer$OfferType;

    sget-object v1, Lcom/upsight/mediation/data/Offer$OfferType;->Discount:Lcom/upsight/mediation/data/Offer$OfferType;

    aput-object v1, v0, v4

    sget-object v1, Lcom/upsight/mediation/data/Offer$OfferType;->Standard:Lcom/upsight/mediation/data/Offer$OfferType;

    aput-object v1, v0, v2

    sget-object v1, Lcom/upsight/mediation/data/Offer$OfferType;->Bonus:Lcom/upsight/mediation/data/Offer$OfferType;

    aput-object v1, v0, v3

    sput-object v0, Lcom/upsight/mediation/data/Offer$OfferType;->$VALUES:[Lcom/upsight/mediation/data/Offer$OfferType;

    return-void
.end method

.method private constructor <init>(Ljava/lang/String;II)V
    .locals 0
    .param p3, "value"    # I
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(I)V"
        }
    .end annotation

    .prologue
    .line 29
    invoke-direct {p0, p1, p2}, Ljava/lang/Enum;-><init>(Ljava/lang/String;I)V

    .line 30
    iput p3, p0, Lcom/upsight/mediation/data/Offer$OfferType;->value:I

    .line 31
    return-void
.end method

.method public static getType(I)Lcom/upsight/mediation/data/Offer$OfferType;
    .locals 1
    .param p0, "value"    # I

    .prologue
    .line 34
    packed-switch p0, :pswitch_data_0

    .line 42
    const/4 v0, 0x0

    :goto_0
    return-object v0

    .line 36
    :pswitch_0
    sget-object v0, Lcom/upsight/mediation/data/Offer$OfferType;->Discount:Lcom/upsight/mediation/data/Offer$OfferType;

    goto :goto_0

    .line 38
    :pswitch_1
    sget-object v0, Lcom/upsight/mediation/data/Offer$OfferType;->Standard:Lcom/upsight/mediation/data/Offer$OfferType;

    goto :goto_0

    .line 40
    :pswitch_2
    sget-object v0, Lcom/upsight/mediation/data/Offer$OfferType;->Bonus:Lcom/upsight/mediation/data/Offer$OfferType;

    goto :goto_0

    .line 34
    :pswitch_data_0
    .packed-switch 0x1
        :pswitch_0
        :pswitch_1
        :pswitch_2
    .end packed-switch
.end method

.method public static valueOf(Ljava/lang/String;)Lcom/upsight/mediation/data/Offer$OfferType;
    .locals 1
    .param p0, "name"    # Ljava/lang/String;

    .prologue
    .line 21
    const-class v0, Lcom/upsight/mediation/data/Offer$OfferType;

    invoke-static {v0, p0}, Ljava/lang/Enum;->valueOf(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;

    move-result-object v0

    check-cast v0, Lcom/upsight/mediation/data/Offer$OfferType;

    return-object v0
.end method

.method public static values()[Lcom/upsight/mediation/data/Offer$OfferType;
    .locals 1

    .prologue
    .line 21
    sget-object v0, Lcom/upsight/mediation/data/Offer$OfferType;->$VALUES:[Lcom/upsight/mediation/data/Offer$OfferType;

    invoke-virtual {v0}, [Lcom/upsight/mediation/data/Offer$OfferType;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Lcom/upsight/mediation/data/Offer$OfferType;

    return-object v0
.end method

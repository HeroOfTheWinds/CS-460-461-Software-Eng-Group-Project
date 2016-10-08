.class final Lcom/upsight/mediation/vast/util/HttpTools$1;
.super Ljava/lang/Thread;
.source "HttpTools.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/upsight/mediation/vast/util/HttpTools;->httpGetURL(Ljava/lang/String;Ljava/lang/String;Lcom/upsight/mediation/vast/VASTPlayer;)V
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x8
    name = null
.end annotation


# instance fields
.field final synthetic val$currentPlayer:Lcom/upsight/mediation/vast/VASTPlayer;

.field final synthetic val$type:Ljava/lang/String;

.field final synthetic val$url:Ljava/lang/String;


# direct methods
.method constructor <init>(Ljava/lang/String;Ljava/lang/String;Lcom/upsight/mediation/vast/VASTPlayer;)V
    .locals 0

    .prologue
    .line 22
    iput-object p1, p0, Lcom/upsight/mediation/vast/util/HttpTools$1;->val$url:Ljava/lang/String;

    iput-object p2, p0, Lcom/upsight/mediation/vast/util/HttpTools$1;->val$type:Ljava/lang/String;

    iput-object p3, p0, Lcom/upsight/mediation/vast/util/HttpTools$1;->val$currentPlayer:Lcom/upsight/mediation/vast/VASTPlayer;

    invoke-direct {p0}, Ljava/lang/Thread;-><init>()V

    return-void
.end method


# virtual methods
.method public run()V
    .locals 8

    .prologue
    .line 25
    const/4 v2, 0x0

    .line 27
    .local v2, "conn":Ljava/net/HttpURLConnection;
    :try_start_0
    # getter for: Lcom/upsight/mediation/vast/util/HttpTools;->TAG:Ljava/lang/String;
    invoke-static {}, Lcom/upsight/mediation/vast/util/HttpTools;->access$000()Ljava/lang/String;

    move-result-object v5

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "connection to URL:"

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    iget-object v7, p0, Lcom/upsight/mediation/vast/util/HttpTools$1;->val$url:Ljava/lang/String;

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-static {v5, v6}, Lcom/upsight/mediation/log/FuseLog;->v(Ljava/lang/String;Ljava/lang/String;)V

    .line 28
    new-instance v4, Ljava/net/URL;

    iget-object v5, p0, Lcom/upsight/mediation/vast/util/HttpTools$1;->val$url:Ljava/lang/String;

    invoke-direct {v4, v5}, Ljava/net/URL;-><init>(Ljava/lang/String;)V

    .line 30
    .local v4, "httpUrl":Ljava/net/URL;
    const/4 v5, 0x1

    invoke-static {v5}, Ljava/net/HttpURLConnection;->setFollowRedirects(Z)V

    .line 31
    invoke-virtual {v4}, Ljava/net/URL;->openConnection()Ljava/net/URLConnection;

    move-result-object v5

    move-object v0, v5

    check-cast v0, Ljava/net/HttpURLConnection;

    move-object v2, v0

    .line 32
    const/16 v5, 0x1388

    invoke-virtual {v2, v5}, Ljava/net/HttpURLConnection;->setConnectTimeout(I)V

    .line 33
    const-string v5, "Connection"

    const-string v6, "close"

    invoke-virtual {v2, v5, v6}, Ljava/net/HttpURLConnection;->setRequestProperty(Ljava/lang/String;Ljava/lang/String;)V

    .line 34
    const-string v5, "GET"

    invoke-virtual {v2, v5}, Ljava/net/HttpURLConnection;->setRequestMethod(Ljava/lang/String;)V

    .line 36
    invoke-virtual {v2}, Ljava/net/HttpURLConnection;->getResponseCode()I

    move-result v1

    .line 37
    .local v1, "code":I
    # getter for: Lcom/upsight/mediation/vast/util/HttpTools;->TAG:Ljava/lang/String;
    invoke-static {}, Lcom/upsight/mediation/vast/util/HttpTools;->access$000()Ljava/lang/String;

    move-result-object v5

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "response code:"

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v6, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v6

    const-string v7, ", for URL:"

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    iget-object v7, p0, Lcom/upsight/mediation/vast/util/HttpTools$1;->val$url:Ljava/lang/String;

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-static {v5, v6}, Lcom/upsight/mediation/log/FuseLog;->v(Ljava/lang/String;Ljava/lang/String;)V

    .line 42
    const/16 v5, 0xc8

    if-ge v1, v5, :cond_0

    const/16 v5, 0xe2

    if-le v1, v5, :cond_0

    iget-object v5, p0, Lcom/upsight/mediation/vast/util/HttpTools$1;->val$type:Ljava/lang/String;

    const-string v6, "impression"

    invoke-virtual {v5, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_0

    .line 43
    iget-object v5, p0, Lcom/upsight/mediation/vast/util/HttpTools$1;->val$currentPlayer:Lcom/upsight/mediation/vast/VASTPlayer;

    iget-object v5, v5, Lcom/upsight/mediation/vast/VASTPlayer;->listener:Lcom/upsight/mediation/vast/VASTPlayer$VASTPlayerListener;

    const/16 v6, 0x384

    invoke-interface {v5, v6}, Lcom/upsight/mediation/vast/VASTPlayer$VASTPlayerListener;->vastError(I)V
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_1
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    .line 53
    :cond_0
    if-eqz v2, :cond_1

    .line 55
    :try_start_1
    invoke-virtual {v2}, Ljava/net/HttpURLConnection;->disconnect()V
    :try_end_1
    .catch Ljava/lang/Exception; {:try_start_1 .. :try_end_1} :catch_0

    .line 61
    .end local v1    # "code":I
    .end local v4    # "httpUrl":Ljava/net/URL;
    :cond_1
    :goto_0
    return-void

    .line 56
    .restart local v1    # "code":I
    .restart local v4    # "httpUrl":Ljava/net/URL;
    :catch_0
    move-exception v3

    .line 57
    .local v3, "e":Ljava/lang/Exception;
    # getter for: Lcom/upsight/mediation/vast/util/HttpTools;->TAG:Ljava/lang/String;
    invoke-static {}, Lcom/upsight/mediation/vast/util/HttpTools;->access$000()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v3}, Ljava/lang/Exception;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-static {v5, v6}, Lcom/upsight/mediation/log/FuseLog;->w(Ljava/lang/String;Ljava/lang/String;)V

    goto :goto_0

    .line 45
    .end local v1    # "code":I
    .end local v3    # "e":Ljava/lang/Exception;
    .end local v4    # "httpUrl":Ljava/net/URL;
    :catch_1
    move-exception v3

    .line 46
    .restart local v3    # "e":Ljava/lang/Exception;
    :try_start_2
    # getter for: Lcom/upsight/mediation/vast/util/HttpTools;->TAG:Ljava/lang/String;
    invoke-static {}, Lcom/upsight/mediation/vast/util/HttpTools;->access$000()Ljava/lang/String;

    move-result-object v5

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v7, p0, Lcom/upsight/mediation/vast/util/HttpTools$1;->val$url:Ljava/lang/String;

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    const-string v7, ": "

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v3}, Ljava/lang/Exception;->getMessage()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    const-string v7, ":"

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    .line 47
    invoke-virtual {v3}, Ljava/lang/Exception;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    .line 46
    invoke-static {v5, v6}, Lcom/upsight/mediation/log/FuseLog;->w(Ljava/lang/String;Ljava/lang/String;)V

    .line 48
    iget-object v5, p0, Lcom/upsight/mediation/vast/util/HttpTools$1;->val$type:Ljava/lang/String;

    const-string v6, "impression"

    invoke-virtual {v5, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_2

    .line 49
    iget-object v5, p0, Lcom/upsight/mediation/vast/util/HttpTools$1;->val$currentPlayer:Lcom/upsight/mediation/vast/VASTPlayer;

    iget-object v5, v5, Lcom/upsight/mediation/vast/VASTPlayer;->listener:Lcom/upsight/mediation/vast/VASTPlayer$VASTPlayerListener;

    const/16 v6, 0x384

    invoke-interface {v5, v6}, Lcom/upsight/mediation/vast/VASTPlayer$VASTPlayerListener;->vastError(I)V
    :try_end_2
    .catchall {:try_start_2 .. :try_end_2} :catchall_0

    .line 53
    :cond_2
    if-eqz v2, :cond_1

    .line 55
    :try_start_3
    invoke-virtual {v2}, Ljava/net/HttpURLConnection;->disconnect()V
    :try_end_3
    .catch Ljava/lang/Exception; {:try_start_3 .. :try_end_3} :catch_2

    goto :goto_0

    .line 56
    :catch_2
    move-exception v3

    .line 57
    # getter for: Lcom/upsight/mediation/vast/util/HttpTools;->TAG:Ljava/lang/String;
    invoke-static {}, Lcom/upsight/mediation/vast/util/HttpTools;->access$000()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v3}, Ljava/lang/Exception;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-static {v5, v6}, Lcom/upsight/mediation/log/FuseLog;->w(Ljava/lang/String;Ljava/lang/String;)V

    goto :goto_0

    .line 53
    .end local v3    # "e":Ljava/lang/Exception;
    :catchall_0
    move-exception v5

    if-eqz v2, :cond_3

    .line 55
    :try_start_4
    invoke-virtual {v2}, Ljava/net/HttpURLConnection;->disconnect()V
    :try_end_4
    .catch Ljava/lang/Exception; {:try_start_4 .. :try_end_4} :catch_3

    .line 58
    :cond_3
    :goto_1
    throw v5

    .line 56
    :catch_3
    move-exception v3

    .line 57
    .restart local v3    # "e":Ljava/lang/Exception;
    # getter for: Lcom/upsight/mediation/vast/util/HttpTools;->TAG:Ljava/lang/String;
    invoke-static {}, Lcom/upsight/mediation/vast/util/HttpTools;->access$000()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v3}, Ljava/lang/Exception;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-static {v6, v7}, Lcom/upsight/mediation/log/FuseLog;->w(Ljava/lang/String;Ljava/lang/String;)V

    goto :goto_1
.end method

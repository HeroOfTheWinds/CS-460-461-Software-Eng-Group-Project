.class Lcom/upsight/mediation/vast/model/VASTModel$DownloadTask;
.super Landroid/os/AsyncTask;
.source "VASTModel.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lcom/upsight/mediation/vast/model/VASTModel;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x2
    name = "DownloadTask"
.end annotation

.annotation system Ldalvik/annotation/Signature;
    value = {
        "Landroid/os/AsyncTask",
        "<",
        "Ljava/lang/String;",
        "Ljava/lang/Void;",
        "Ljava/lang/Integer;",
        ">;"
    }
.end annotation


# instance fields
.field private final context:Landroid/content/Context;

.field private final downloadTimeout:J

.field private final mVastPlayer:Lcom/upsight/mediation/vast/VASTPlayer;

.field final synthetic this$0:Lcom/upsight/mediation/vast/model/VASTModel;


# direct methods
.method public constructor <init>(Lcom/upsight/mediation/vast/model/VASTModel;Lcom/upsight/mediation/vast/VASTPlayer;Landroid/content/Context;I)V
    .locals 2
    .param p2, "vastPlayer"    # Lcom/upsight/mediation/vast/VASTPlayer;
    .param p3, "context"    # Landroid/content/Context;
    .param p4, "downloadTimeout"    # I

    .prologue
    .line 457
    iput-object p1, p0, Lcom/upsight/mediation/vast/model/VASTModel$DownloadTask;->this$0:Lcom/upsight/mediation/vast/model/VASTModel;

    invoke-direct {p0}, Landroid/os/AsyncTask;-><init>()V

    .line 458
    iput-object p2, p0, Lcom/upsight/mediation/vast/model/VASTModel$DownloadTask;->mVastPlayer:Lcom/upsight/mediation/vast/VASTPlayer;

    .line 459
    iput-object p3, p0, Lcom/upsight/mediation/vast/model/VASTModel$DownloadTask;->context:Landroid/content/Context;

    .line 460
    if-lez p4, :cond_0

    int-to-long v0, p4

    :goto_0
    iput-wide v0, p0, Lcom/upsight/mediation/vast/model/VASTModel$DownloadTask;->downloadTimeout:J

    .line 461
    return-void

    .line 460
    :cond_0
    const-wide/16 v0, 0x7530

    goto :goto_0
.end method


# virtual methods
.method protected varargs doInBackground([Ljava/lang/String;)Ljava/lang/Integer;
    .locals 26
    .param p1, "sUrl"    # [Ljava/lang/String;

    .prologue
    .line 465
    const/4 v15, 0x0

    .line 466
    .local v15, "input":Ljava/io/InputStream;
    const/16 v16, 0x0

    .line 467
    .local v16, "output":Ljava/io/OutputStream;
    const/4 v3, 0x0

    .line 468
    .local v3, "connection":Ljava/net/HttpURLConnection;
    const/4 v9, 0x0

    .line 470
    .local v9, "file":Ljava/io/File;
    :try_start_0
    invoke-static {}, Ljava/lang/System;->nanoTime()J

    move-result-wide v18

    .line 471
    .local v18, "startTime":J
    new-instance v20, Ljava/net/URL;

    const/16 v21, 0x0

    aget-object v21, p1, v21

    invoke-direct/range {v20 .. v21}, Ljava/net/URL;-><init>(Ljava/lang/String;)V

    .line 472
    .local v20, "url":Ljava/net/URL;
    const/16 v21, 0x0

    aget-object v21, p1, v21

    const/16 v22, 0x0

    aget-object v22, p1, v22

    const/16 v23, 0x2f

    invoke-virtual/range {v22 .. v23}, Ljava/lang/String;->lastIndexOf(I)I

    move-result v22

    add-int/lit8 v22, v22, 0x1

    const/16 v23, 0x0

    aget-object v23, p1, v23

    invoke-virtual/range {v23 .. v23}, Ljava/lang/String;->length()I

    move-result v23

    invoke-virtual/range {v21 .. v23}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v14

    .line 473
    .local v14, "filename":Ljava/lang/String;
    invoke-virtual/range {v20 .. v20}, Ljava/net/URL;->openConnection()Ljava/net/URLConnection;

    move-result-object v21

    move-object/from16 v0, v21

    check-cast v0, Ljava/net/HttpURLConnection;

    move-object v3, v0

    .line 475
    const/16 v21, 0x1388

    move/from16 v0, v21

    invoke-virtual {v3, v0}, Ljava/net/HttpURLConnection;->setConnectTimeout(I)V

    .line 476
    invoke-virtual {v3}, Ljava/net/HttpURLConnection;->connect()V

    .line 477
    invoke-virtual {v3}, Ljava/net/HttpURLConnection;->getContentLength()I

    move-result v13

    .line 481
    .local v13, "fileSize":I
    invoke-virtual {v3}, Ljava/net/HttpURLConnection;->getResponseCode()I

    move-result v21

    const/16 v22, 0xc8

    move/from16 v0, v21

    move/from16 v1, v22

    if-eq v0, v1, :cond_3

    .line 482
    # getter for: Lcom/upsight/mediation/vast/model/VASTModel;->TAG:Ljava/lang/String;
    invoke-static {}, Lcom/upsight/mediation/vast/model/VASTModel;->access$100()Ljava/lang/String;

    move-result-object v21

    new-instance v22, Ljava/lang/StringBuilder;

    invoke-direct/range {v22 .. v22}, Ljava/lang/StringBuilder;-><init>()V

    const-string v23, "Server returned HTTP "

    invoke-virtual/range {v22 .. v23}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v22

    .line 483
    invoke-virtual {v3}, Ljava/net/HttpURLConnection;->getResponseCode()I

    move-result v23

    invoke-virtual/range {v22 .. v23}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v22

    const-string v23, " "

    invoke-virtual/range {v22 .. v23}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v22

    invoke-virtual {v3}, Ljava/net/HttpURLConnection;->getResponseMessage()Ljava/lang/String;

    move-result-object v23

    invoke-virtual/range {v22 .. v23}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v22

    invoke-virtual/range {v22 .. v22}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v22

    .line 482
    invoke-static/range {v21 .. v22}, Lcom/upsight/mediation/log/FuseLog;->d(Ljava/lang/String;Ljava/lang/String;)V

    .line 484
    const/16 v21, 0x191

    invoke-static/range {v21 .. v21}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;
    :try_end_0
    .catch Ljava/net/SocketTimeoutException; {:try_start_0 .. :try_end_0} :catch_7
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_1
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    move-result-object v21

    .line 548
    if-eqz v16, :cond_0

    .line 549
    :try_start_1
    invoke-virtual/range {v16 .. v16}, Ljava/io/OutputStream;->close()V

    .line 551
    :cond_0
    if-eqz v15, :cond_1

    .line 552
    invoke-virtual {v15}, Ljava/io/InputStream;->close()V
    :try_end_1
    .catch Ljava/io/IOException; {:try_start_1 .. :try_end_1} :catch_f

    .line 557
    :cond_1
    :goto_0
    if-eqz v3, :cond_2

    .line 558
    invoke-virtual {v3}, Ljava/net/HttpURLConnection;->disconnect()V

    .line 562
    .end local v13    # "fileSize":I
    .end local v14    # "filename":Ljava/lang/String;
    .end local v18    # "startTime":J
    .end local v20    # "url":Ljava/net/URL;
    :cond_2
    :goto_1
    return-object v21

    .line 494
    .restart local v13    # "fileSize":I
    .restart local v14    # "filename":Ljava/lang/String;
    .restart local v18    # "startTime":J
    .restart local v20    # "url":Ljava/net/URL;
    :cond_3
    :try_start_2
    new-instance v2, Ljava/io/File;

    move-object/from16 v0, p0

    iget-object v0, v0, Lcom/upsight/mediation/vast/model/VASTModel$DownloadTask;->context:Landroid/content/Context;

    move-object/from16 v21, v0

    invoke-virtual/range {v21 .. v21}, Landroid/content/Context;->getCacheDir()Ljava/io/File;

    move-result-object v21

    const-string v22, "fuse_vast_cache"

    move-object/from16 v0, v21

    move-object/from16 v1, v22

    invoke-direct {v2, v0, v1}, Ljava/io/File;-><init>(Ljava/io/File;Ljava/lang/String;)V

    .line 495
    .local v2, "cacheDir":Ljava/io/File;
    invoke-virtual {v2}, Ljava/io/File;->exists()Z

    move-result v21

    if-nez v21, :cond_6

    invoke-virtual {v2}, Ljava/io/File;->mkdir()Z

    move-result v21

    if-nez v21, :cond_6

    .line 496
    const/16 v21, 0x191

    invoke-static/range {v21 .. v21}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;
    :try_end_2
    .catch Ljava/net/SocketTimeoutException; {:try_start_2 .. :try_end_2} :catch_7
    .catch Ljava/lang/Exception; {:try_start_2 .. :try_end_2} :catch_1
    .catchall {:try_start_2 .. :try_end_2} :catchall_0

    move-result-object v21

    .line 548
    if-eqz v16, :cond_4

    .line 549
    :try_start_3
    invoke-virtual/range {v16 .. v16}, Ljava/io/OutputStream;->close()V

    .line 551
    :cond_4
    if-eqz v15, :cond_5

    .line 552
    invoke-virtual {v15}, Ljava/io/InputStream;->close()V
    :try_end_3
    .catch Ljava/io/IOException; {:try_start_3 .. :try_end_3} :catch_e

    .line 557
    :cond_5
    :goto_2
    if-eqz v3, :cond_2

    .line 558
    invoke-virtual {v3}, Ljava/net/HttpURLConnection;->disconnect()V

    goto :goto_1

    .line 498
    :cond_6
    :try_start_4
    new-instance v12, Ljava/io/File;

    invoke-direct {v12, v2, v14}, Ljava/io/File;-><init>(Ljava/io/File;Ljava/lang/String;)V
    :try_end_4
    .catch Ljava/net/SocketTimeoutException; {:try_start_4 .. :try_end_4} :catch_7
    .catch Ljava/lang/Exception; {:try_start_4 .. :try_end_4} :catch_1
    .catchall {:try_start_4 .. :try_end_4} :catchall_0

    .line 501
    .end local v9    # "file":Ljava/io/File;
    .local v12, "file":Ljava/io/File;
    :try_start_5
    invoke-virtual {v12}, Ljava/io/File;->exists()Z

    move-result v21

    if-eqz v21, :cond_a

    invoke-virtual {v12}, Ljava/io/File;->length()J

    move-result-wide v22

    int-to-long v0, v13

    move-wide/from16 v24, v0

    cmp-long v21, v22, v24

    if-nez v21, :cond_a

    .line 502
    move-object/from16 v0, p0

    iget-object v0, v0, Lcom/upsight/mediation/vast/model/VASTModel$DownloadTask;->this$0:Lcom/upsight/mediation/vast/model/VASTModel;

    move-object/from16 v21, v0

    invoke-virtual {v12}, Ljava/io/File;->getAbsolutePath()Ljava/lang/String;

    move-result-object v22

    # setter for: Lcom/upsight/mediation/vast/model/VASTModel;->mediaFileLocation:Ljava/lang/String;
    invoke-static/range {v21 .. v22}, Lcom/upsight/mediation/vast/model/VASTModel;->access$002(Lcom/upsight/mediation/vast/model/VASTModel;Ljava/lang/String;)Ljava/lang/String;

    .line 503
    const/16 v21, 0x0

    invoke-static/range {v21 .. v21}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;
    :try_end_5
    .catch Ljava/net/SocketTimeoutException; {:try_start_5 .. :try_end_5} :catch_8
    .catch Ljava/lang/Exception; {:try_start_5 .. :try_end_5} :catch_4
    .catchall {:try_start_5 .. :try_end_5} :catchall_1

    move-result-object v21

    .line 548
    if-eqz v16, :cond_7

    .line 549
    :try_start_6
    invoke-virtual/range {v16 .. v16}, Ljava/io/OutputStream;->close()V

    .line 551
    :cond_7
    if-eqz v15, :cond_8

    .line 552
    invoke-virtual {v15}, Ljava/io/InputStream;->close()V
    :try_end_6
    .catch Ljava/io/IOException; {:try_start_6 .. :try_end_6} :catch_d

    .line 557
    :cond_8
    :goto_3
    if-eqz v3, :cond_9

    .line 558
    invoke-virtual {v3}, Ljava/net/HttpURLConnection;->disconnect()V

    :cond_9
    move-object v9, v12

    .end local v12    # "file":Ljava/io/File;
    .restart local v9    # "file":Ljava/io/File;
    goto :goto_1

    .line 507
    .end local v9    # "file":Ljava/io/File;
    .restart local v12    # "file":Ljava/io/File;
    :cond_a
    int-to-long v0, v13

    move-wide/from16 v22, v0

    :try_start_7
    move-object/from16 v0, p0

    iget-object v0, v0, Lcom/upsight/mediation/vast/model/VASTModel$DownloadTask;->mVastPlayer:Lcom/upsight/mediation/vast/VASTPlayer;

    move-object/from16 v21, v0

    invoke-virtual/range {v21 .. v21}, Lcom/upsight/mediation/vast/VASTPlayer;->getMaxFileSize()J

    move-result-wide v24

    cmp-long v21, v22, v24

    if-lez v21, :cond_e

    .line 508
    const/16 v21, 0x190

    invoke-static/range {v21 .. v21}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;
    :try_end_7
    .catch Ljava/net/SocketTimeoutException; {:try_start_7 .. :try_end_7} :catch_8
    .catch Ljava/lang/Exception; {:try_start_7 .. :try_end_7} :catch_4
    .catchall {:try_start_7 .. :try_end_7} :catchall_1

    move-result-object v21

    .line 548
    if-eqz v16, :cond_b

    .line 549
    :try_start_8
    invoke-virtual/range {v16 .. v16}, Ljava/io/OutputStream;->close()V

    .line 551
    :cond_b
    if-eqz v15, :cond_c

    .line 552
    invoke-virtual {v15}, Ljava/io/InputStream;->close()V
    :try_end_8
    .catch Ljava/io/IOException; {:try_start_8 .. :try_end_8} :catch_c

    .line 557
    :cond_c
    :goto_4
    if-eqz v3, :cond_d

    .line 558
    invoke-virtual {v3}, Ljava/net/HttpURLConnection;->disconnect()V

    :cond_d
    move-object v9, v12

    .end local v12    # "file":Ljava/io/File;
    .restart local v9    # "file":Ljava/io/File;
    goto/16 :goto_1

    .line 512
    .end local v9    # "file":Ljava/io/File;
    .restart local v12    # "file":Ljava/io/File;
    :cond_e
    :try_start_9
    invoke-virtual {v3}, Ljava/net/HttpURLConnection;->getInputStream()Ljava/io/InputStream;

    move-result-object v15

    .line 513
    new-instance v17, Ljava/io/FileOutputStream;

    move-object/from16 v0, v17

    invoke-direct {v0, v12}, Ljava/io/FileOutputStream;-><init>(Ljava/io/File;)V
    :try_end_9
    .catch Ljava/net/SocketTimeoutException; {:try_start_9 .. :try_end_9} :catch_8
    .catch Ljava/lang/Exception; {:try_start_9 .. :try_end_9} :catch_4
    .catchall {:try_start_9 .. :try_end_9} :catchall_1

    .line 515
    .end local v16    # "output":Ljava/io/OutputStream;
    .local v17, "output":Ljava/io/OutputStream;
    const/16 v21, 0x1000

    :try_start_a
    move/from16 v0, v21

    new-array v5, v0, [B

    .line 519
    .local v5, "data":[B
    :goto_5
    invoke-virtual {v15, v5}, Ljava/io/InputStream;->read([B)I

    move-result v4

    .local v4, "count":I
    const/16 v21, -0x1

    move/from16 v0, v21

    if-eq v4, v0, :cond_16

    .line 520
    invoke-static {}, Ljava/lang/System;->nanoTime()J

    move-result-wide v10

    .line 521
    .local v10, "endTime":J
    sub-long v22, v10, v18

    const-wide/32 v24, 0xf4240

    div-long v6, v22, v24

    .line 523
    .local v6, "duration":J
    move-object/from16 v0, p0

    iget-wide v0, v0, Lcom/upsight/mediation/vast/model/VASTModel$DownloadTask;->downloadTimeout:J

    move-wide/from16 v22, v0

    cmp-long v21, v6, v22

    if-ltz v21, :cond_f

    .line 524
    const/16 v21, 0x1

    move-object/from16 v0, p0

    move/from16 v1, v21

    invoke-virtual {v0, v1}, Lcom/upsight/mediation/vast/model/VASTModel$DownloadTask;->cancel(Z)Z

    .line 527
    :cond_f
    invoke-virtual/range {p0 .. p0}, Lcom/upsight/mediation/vast/model/VASTModel$DownloadTask;->isCancelled()Z

    move-result v21

    if-eqz v21, :cond_13

    .line 528
    invoke-virtual {v15}, Ljava/io/InputStream;->close()V

    .line 530
    const/16 v21, 0x192

    invoke-static/range {v21 .. v21}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;
    :try_end_a
    .catch Ljava/net/SocketTimeoutException; {:try_start_a .. :try_end_a} :catch_0
    .catch Ljava/lang/Exception; {:try_start_a .. :try_end_a} :catch_5
    .catchall {:try_start_a .. :try_end_a} :catchall_2

    move-result-object v21

    .line 548
    if-eqz v17, :cond_10

    .line 549
    :try_start_b
    invoke-virtual/range {v17 .. v17}, Ljava/io/OutputStream;->close()V

    .line 551
    :cond_10
    if-eqz v15, :cond_11

    .line 552
    invoke-virtual {v15}, Ljava/io/InputStream;->close()V
    :try_end_b
    .catch Ljava/io/IOException; {:try_start_b .. :try_end_b} :catch_b

    .line 557
    :cond_11
    :goto_6
    if-eqz v3, :cond_12

    .line 558
    invoke-virtual {v3}, Ljava/net/HttpURLConnection;->disconnect()V

    :cond_12
    move-object v9, v12

    .end local v12    # "file":Ljava/io/File;
    .restart local v9    # "file":Ljava/io/File;
    move-object/from16 v16, v17

    .end local v17    # "output":Ljava/io/OutputStream;
    .restart local v16    # "output":Ljava/io/OutputStream;
    goto/16 :goto_1

    .line 533
    .end local v9    # "file":Ljava/io/File;
    .end local v16    # "output":Ljava/io/OutputStream;
    .restart local v12    # "file":Ljava/io/File;
    .restart local v17    # "output":Ljava/io/OutputStream;
    :cond_13
    const/16 v21, 0x0

    :try_start_c
    move-object/from16 v0, v17

    move/from16 v1, v21

    invoke-virtual {v0, v5, v1, v4}, Ljava/io/OutputStream;->write([BII)V
    :try_end_c
    .catch Ljava/net/SocketTimeoutException; {:try_start_c .. :try_end_c} :catch_0
    .catch Ljava/lang/Exception; {:try_start_c .. :try_end_c} :catch_5
    .catchall {:try_start_c .. :try_end_c} :catchall_2

    goto :goto_5

    .line 541
    .end local v4    # "count":I
    .end local v5    # "data":[B
    .end local v6    # "duration":J
    .end local v10    # "endTime":J
    :catch_0
    move-exception v8

    move-object v9, v12

    .end local v12    # "file":Ljava/io/File;
    .restart local v9    # "file":Ljava/io/File;
    move-object/from16 v16, v17

    .line 542
    .end local v2    # "cacheDir":Ljava/io/File;
    .end local v13    # "fileSize":I
    .end local v14    # "filename":Ljava/lang/String;
    .end local v17    # "output":Ljava/io/OutputStream;
    .end local v18    # "startTime":J
    .end local v20    # "url":Ljava/net/URL;
    .local v8, "e":Ljava/net/SocketTimeoutException;
    .restart local v16    # "output":Ljava/io/OutputStream;
    :goto_7
    const/16 v21, 0x192

    :try_start_d
    invoke-static/range {v21 .. v21}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;
    :try_end_d
    .catchall {:try_start_d .. :try_end_d} :catchall_0

    move-result-object v21

    .line 548
    if-eqz v16, :cond_14

    .line 549
    :try_start_e
    invoke-virtual/range {v16 .. v16}, Ljava/io/OutputStream;->close()V

    .line 551
    :cond_14
    if-eqz v15, :cond_15

    .line 552
    invoke-virtual {v15}, Ljava/io/InputStream;->close()V
    :try_end_e
    .catch Ljava/io/IOException; {:try_start_e .. :try_end_e} :catch_6

    .line 557
    :cond_15
    :goto_8
    if-eqz v3, :cond_2

    .line 558
    invoke-virtual {v3}, Ljava/net/HttpURLConnection;->disconnect()V

    goto/16 :goto_1

    .line 538
    .end local v8    # "e":Ljava/net/SocketTimeoutException;
    .end local v9    # "file":Ljava/io/File;
    .end local v16    # "output":Ljava/io/OutputStream;
    .restart local v2    # "cacheDir":Ljava/io/File;
    .restart local v4    # "count":I
    .restart local v5    # "data":[B
    .restart local v12    # "file":Ljava/io/File;
    .restart local v13    # "fileSize":I
    .restart local v14    # "filename":Ljava/lang/String;
    .restart local v17    # "output":Ljava/io/OutputStream;
    .restart local v18    # "startTime":J
    .restart local v20    # "url":Ljava/net/URL;
    :cond_16
    :try_start_f
    invoke-virtual {v12}, Ljava/io/File;->exists()Z

    move-result v21

    if-eqz v21, :cond_17

    invoke-virtual {v12}, Ljava/io/File;->length()J

    move-result-wide v22

    int-to-long v0, v13

    move-wide/from16 v24, v0

    cmp-long v21, v22, v24

    if-eqz v21, :cond_1b

    .line 539
    :cond_17
    const/16 v21, 0x190

    invoke-static/range {v21 .. v21}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;
    :try_end_f
    .catch Ljava/net/SocketTimeoutException; {:try_start_f .. :try_end_f} :catch_0
    .catch Ljava/lang/Exception; {:try_start_f .. :try_end_f} :catch_5
    .catchall {:try_start_f .. :try_end_f} :catchall_2

    move-result-object v21

    .line 548
    if-eqz v17, :cond_18

    .line 549
    :try_start_10
    invoke-virtual/range {v17 .. v17}, Ljava/io/OutputStream;->close()V

    .line 551
    :cond_18
    if-eqz v15, :cond_19

    .line 552
    invoke-virtual {v15}, Ljava/io/InputStream;->close()V
    :try_end_10
    .catch Ljava/io/IOException; {:try_start_10 .. :try_end_10} :catch_a

    .line 557
    :cond_19
    :goto_9
    if-eqz v3, :cond_1a

    .line 558
    invoke-virtual {v3}, Ljava/net/HttpURLConnection;->disconnect()V

    :cond_1a
    move-object v9, v12

    .end local v12    # "file":Ljava/io/File;
    .restart local v9    # "file":Ljava/io/File;
    move-object/from16 v16, v17

    .end local v17    # "output":Ljava/io/OutputStream;
    .restart local v16    # "output":Ljava/io/OutputStream;
    goto/16 :goto_1

    .line 548
    .end local v9    # "file":Ljava/io/File;
    .end local v16    # "output":Ljava/io/OutputStream;
    .restart local v12    # "file":Ljava/io/File;
    .restart local v17    # "output":Ljava/io/OutputStream;
    :cond_1b
    if-eqz v17, :cond_1c

    .line 549
    :try_start_11
    invoke-virtual/range {v17 .. v17}, Ljava/io/OutputStream;->close()V

    .line 551
    :cond_1c
    if-eqz v15, :cond_1d

    .line 552
    invoke-virtual {v15}, Ljava/io/InputStream;->close()V
    :try_end_11
    .catch Ljava/io/IOException; {:try_start_11 .. :try_end_11} :catch_9

    .line 557
    :cond_1d
    :goto_a
    if-eqz v3, :cond_1e

    .line 558
    invoke-virtual {v3}, Ljava/net/HttpURLConnection;->disconnect()V

    .line 561
    :cond_1e
    move-object/from16 v0, p0

    iget-object v0, v0, Lcom/upsight/mediation/vast/model/VASTModel$DownloadTask;->this$0:Lcom/upsight/mediation/vast/model/VASTModel;

    move-object/from16 v21, v0

    invoke-virtual {v12}, Ljava/io/File;->getAbsolutePath()Ljava/lang/String;

    move-result-object v22

    # setter for: Lcom/upsight/mediation/vast/model/VASTModel;->mediaFileLocation:Ljava/lang/String;
    invoke-static/range {v21 .. v22}, Lcom/upsight/mediation/vast/model/VASTModel;->access$002(Lcom/upsight/mediation/vast/model/VASTModel;Ljava/lang/String;)Ljava/lang/String;

    .line 562
    const/16 v21, 0x0

    invoke-static/range {v21 .. v21}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v21

    move-object v9, v12

    .end local v12    # "file":Ljava/io/File;
    .restart local v9    # "file":Ljava/io/File;
    move-object/from16 v16, v17

    .end local v17    # "output":Ljava/io/OutputStream;
    .restart local v16    # "output":Ljava/io/OutputStream;
    goto/16 :goto_1

    .line 543
    .end local v2    # "cacheDir":Ljava/io/File;
    .end local v4    # "count":I
    .end local v5    # "data":[B
    .end local v13    # "fileSize":I
    .end local v14    # "filename":Ljava/lang/String;
    .end local v18    # "startTime":J
    .end local v20    # "url":Ljava/net/URL;
    :catch_1
    move-exception v8

    .line 545
    .local v8, "e":Ljava/lang/Exception;
    :goto_b
    const/16 v21, 0x190

    :try_start_12
    invoke-static/range {v21 .. v21}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;
    :try_end_12
    .catchall {:try_start_12 .. :try_end_12} :catchall_0

    move-result-object v21

    .line 548
    if-eqz v16, :cond_1f

    .line 549
    :try_start_13
    invoke-virtual/range {v16 .. v16}, Ljava/io/OutputStream;->close()V

    .line 551
    :cond_1f
    if-eqz v15, :cond_20

    .line 552
    invoke-virtual {v15}, Ljava/io/InputStream;->close()V
    :try_end_13
    .catch Ljava/io/IOException; {:try_start_13 .. :try_end_13} :catch_3

    .line 557
    :cond_20
    :goto_c
    if-eqz v3, :cond_2

    .line 558
    invoke-virtual {v3}, Ljava/net/HttpURLConnection;->disconnect()V

    goto/16 :goto_1

    .line 547
    .end local v8    # "e":Ljava/lang/Exception;
    :catchall_0
    move-exception v21

    .line 548
    :goto_d
    if-eqz v16, :cond_21

    .line 549
    :try_start_14
    invoke-virtual/range {v16 .. v16}, Ljava/io/OutputStream;->close()V

    .line 551
    :cond_21
    if-eqz v15, :cond_22

    .line 552
    invoke-virtual {v15}, Ljava/io/InputStream;->close()V
    :try_end_14
    .catch Ljava/io/IOException; {:try_start_14 .. :try_end_14} :catch_2

    .line 557
    :cond_22
    :goto_e
    if-eqz v3, :cond_23

    .line 558
    invoke-virtual {v3}, Ljava/net/HttpURLConnection;->disconnect()V

    :cond_23
    throw v21

    .line 554
    :catch_2
    move-exception v22

    goto :goto_e

    .line 547
    .end local v9    # "file":Ljava/io/File;
    .restart local v2    # "cacheDir":Ljava/io/File;
    .restart local v12    # "file":Ljava/io/File;
    .restart local v13    # "fileSize":I
    .restart local v14    # "filename":Ljava/lang/String;
    .restart local v18    # "startTime":J
    .restart local v20    # "url":Ljava/net/URL;
    :catchall_1
    move-exception v21

    move-object v9, v12

    .end local v12    # "file":Ljava/io/File;
    .restart local v9    # "file":Ljava/io/File;
    goto :goto_d

    .end local v9    # "file":Ljava/io/File;
    .end local v16    # "output":Ljava/io/OutputStream;
    .restart local v12    # "file":Ljava/io/File;
    .restart local v17    # "output":Ljava/io/OutputStream;
    :catchall_2
    move-exception v21

    move-object v9, v12

    .end local v12    # "file":Ljava/io/File;
    .restart local v9    # "file":Ljava/io/File;
    move-object/from16 v16, v17

    .end local v17    # "output":Ljava/io/OutputStream;
    .restart local v16    # "output":Ljava/io/OutputStream;
    goto :goto_d

    .line 554
    .end local v2    # "cacheDir":Ljava/io/File;
    .end local v13    # "fileSize":I
    .end local v14    # "filename":Ljava/lang/String;
    .end local v18    # "startTime":J
    .end local v20    # "url":Ljava/net/URL;
    .restart local v8    # "e":Ljava/lang/Exception;
    :catch_3
    move-exception v22

    goto :goto_c

    .line 543
    .end local v8    # "e":Ljava/lang/Exception;
    .end local v9    # "file":Ljava/io/File;
    .restart local v2    # "cacheDir":Ljava/io/File;
    .restart local v12    # "file":Ljava/io/File;
    .restart local v13    # "fileSize":I
    .restart local v14    # "filename":Ljava/lang/String;
    .restart local v18    # "startTime":J
    .restart local v20    # "url":Ljava/net/URL;
    :catch_4
    move-exception v8

    move-object v9, v12

    .end local v12    # "file":Ljava/io/File;
    .restart local v9    # "file":Ljava/io/File;
    goto :goto_b

    .end local v9    # "file":Ljava/io/File;
    .end local v16    # "output":Ljava/io/OutputStream;
    .restart local v12    # "file":Ljava/io/File;
    .restart local v17    # "output":Ljava/io/OutputStream;
    :catch_5
    move-exception v8

    move-object v9, v12

    .end local v12    # "file":Ljava/io/File;
    .restart local v9    # "file":Ljava/io/File;
    move-object/from16 v16, v17

    .end local v17    # "output":Ljava/io/OutputStream;
    .restart local v16    # "output":Ljava/io/OutputStream;
    goto :goto_b

    .line 554
    .end local v2    # "cacheDir":Ljava/io/File;
    .end local v13    # "fileSize":I
    .end local v14    # "filename":Ljava/lang/String;
    .end local v18    # "startTime":J
    .end local v20    # "url":Ljava/net/URL;
    .local v8, "e":Ljava/net/SocketTimeoutException;
    :catch_6
    move-exception v22

    goto/16 :goto_8

    .line 541
    .end local v8    # "e":Ljava/net/SocketTimeoutException;
    :catch_7
    move-exception v8

    goto/16 :goto_7

    .end local v9    # "file":Ljava/io/File;
    .restart local v2    # "cacheDir":Ljava/io/File;
    .restart local v12    # "file":Ljava/io/File;
    .restart local v13    # "fileSize":I
    .restart local v14    # "filename":Ljava/lang/String;
    .restart local v18    # "startTime":J
    .restart local v20    # "url":Ljava/net/URL;
    :catch_8
    move-exception v8

    move-object v9, v12

    .end local v12    # "file":Ljava/io/File;
    .restart local v9    # "file":Ljava/io/File;
    goto/16 :goto_7

    .line 554
    .end local v9    # "file":Ljava/io/File;
    .end local v16    # "output":Ljava/io/OutputStream;
    .restart local v4    # "count":I
    .restart local v5    # "data":[B
    .restart local v12    # "file":Ljava/io/File;
    .restart local v17    # "output":Ljava/io/OutputStream;
    :catch_9
    move-exception v21

    goto :goto_a

    :catch_a
    move-exception v22

    goto :goto_9

    .restart local v6    # "duration":J
    .restart local v10    # "endTime":J
    :catch_b
    move-exception v22

    goto/16 :goto_6

    .end local v4    # "count":I
    .end local v5    # "data":[B
    .end local v6    # "duration":J
    .end local v10    # "endTime":J
    .end local v17    # "output":Ljava/io/OutputStream;
    .restart local v16    # "output":Ljava/io/OutputStream;
    :catch_c
    move-exception v22

    goto/16 :goto_4

    :catch_d
    move-exception v22

    goto/16 :goto_3

    .end local v12    # "file":Ljava/io/File;
    .restart local v9    # "file":Ljava/io/File;
    :catch_e
    move-exception v22

    goto/16 :goto_2

    .end local v2    # "cacheDir":Ljava/io/File;
    :catch_f
    move-exception v22

    goto/16 :goto_0
.end method

.method protected bridge synthetic doInBackground([Ljava/lang/Object;)Ljava/lang/Object;
    .locals 1

    .prologue
    .line 451
    check-cast p1, [Ljava/lang/String;

    invoke-virtual {p0, p1}, Lcom/upsight/mediation/vast/model/VASTModel$DownloadTask;->doInBackground([Ljava/lang/String;)Ljava/lang/Integer;

    move-result-object v0

    return-object v0
.end method

.method protected onCancelled()V
    .locals 2

    .prologue
    .line 579
    iget-object v0, p0, Lcom/upsight/mediation/vast/model/VASTModel$DownloadTask;->mVastPlayer:Lcom/upsight/mediation/vast/VASTPlayer;

    iget-object v0, v0, Lcom/upsight/mediation/vast/VASTPlayer;->listener:Lcom/upsight/mediation/vast/VASTPlayer$VASTPlayerListener;

    const/16 v1, 0x192

    invoke-interface {v0, v1}, Lcom/upsight/mediation/vast/VASTPlayer$VASTPlayerListener;->vastError(I)V

    .line 580
    return-void
.end method

.method protected onPostExecute(Ljava/lang/Integer;)V
    .locals 2
    .param p1, "error"    # Ljava/lang/Integer;

    .prologue
    .line 567
    invoke-virtual {p1}, Ljava/lang/Integer;->intValue()I

    move-result v0

    if-eqz v0, :cond_0

    .line 568
    iget-object v0, p0, Lcom/upsight/mediation/vast/model/VASTModel$DownloadTask;->mVastPlayer:Lcom/upsight/mediation/vast/VASTPlayer;

    iget-object v0, v0, Lcom/upsight/mediation/vast/VASTPlayer;->listener:Lcom/upsight/mediation/vast/VASTPlayer$VASTPlayerListener;

    invoke-virtual {p1}, Ljava/lang/Integer;->intValue()I

    move-result v1

    invoke-interface {v0, v1}, Lcom/upsight/mediation/vast/VASTPlayer$VASTPlayerListener;->vastError(I)V

    .line 573
    :goto_0
    return-void

    .line 570
    :cond_0
    # getter for: Lcom/upsight/mediation/vast/model/VASTModel;->TAG:Ljava/lang/String;
    invoke-static {}, Lcom/upsight/mediation/vast/model/VASTModel;->access$100()Ljava/lang/String;

    move-result-object v0

    const-string v1, "on execute complete"

    invoke-static {v0, v1}, Lcom/upsight/mediation/log/FuseLog;->v(Ljava/lang/String;Ljava/lang/String;)V

    .line 571
    iget-object v0, p0, Lcom/upsight/mediation/vast/model/VASTModel$DownloadTask;->mVastPlayer:Lcom/upsight/mediation/vast/VASTPlayer;

    const/4 v1, 0x1

    invoke-virtual {v0, v1}, Lcom/upsight/mediation/vast/VASTPlayer;->setLoaded(Z)V

    goto :goto_0
.end method

.method protected bridge synthetic onPostExecute(Ljava/lang/Object;)V
    .locals 0

    .prologue
    .line 451
    check-cast p1, Ljava/lang/Integer;

    invoke-virtual {p0, p1}, Lcom/upsight/mediation/vast/model/VASTModel$DownloadTask;->onPostExecute(Ljava/lang/Integer;)V

    return-void
.end method

.class public Lcom/mopub/volley/CacheDispatcher;
.super Ljava/lang/Thread;
.source "CacheDispatcher.java"


# static fields
.field private static final DEBUG:Z


# instance fields
.field private final mCache:Lcom/mopub/volley/Cache;

.field private final mCacheQueue:Ljava/util/concurrent/BlockingQueue;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/concurrent/BlockingQueue",
            "<",
            "Lcom/mopub/volley/Request",
            "<*>;>;"
        }
    .end annotation
.end field

.field private final mDelivery:Lcom/mopub/volley/ResponseDelivery;

.field private final mNetworkQueue:Ljava/util/concurrent/BlockingQueue;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/concurrent/BlockingQueue",
            "<",
            "Lcom/mopub/volley/Request",
            "<*>;>;"
        }
    .end annotation
.end field

.field private volatile mQuit:Z


# direct methods
.method static constructor <clinit>()V
    .locals 1

    .prologue
    .line 34
    sget-boolean v0, Lcom/mopub/volley/VolleyLog;->DEBUG:Z

    sput-boolean v0, Lcom/mopub/volley/CacheDispatcher;->DEBUG:Z

    return-void
.end method

.method public constructor <init>(Ljava/util/concurrent/BlockingQueue;Ljava/util/concurrent/BlockingQueue;Lcom/mopub/volley/Cache;Lcom/mopub/volley/ResponseDelivery;)V
    .locals 1
    .param p3, "cache"    # Lcom/mopub/volley/Cache;
    .param p4, "delivery"    # Lcom/mopub/volley/ResponseDelivery;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/util/concurrent/BlockingQueue",
            "<",
            "Lcom/mopub/volley/Request",
            "<*>;>;",
            "Ljava/util/concurrent/BlockingQueue",
            "<",
            "Lcom/mopub/volley/Request",
            "<*>;>;",
            "Lcom/mopub/volley/Cache;",
            "Lcom/mopub/volley/ResponseDelivery;",
            ")V"
        }
    .end annotation

    .prologue
    .line 62
    .local p1, "cacheQueue":Ljava/util/concurrent/BlockingQueue;, "Ljava/util/concurrent/BlockingQueue<Lcom/mopub/volley/Request<*>;>;"
    .local p2, "networkQueue":Ljava/util/concurrent/BlockingQueue;, "Ljava/util/concurrent/BlockingQueue<Lcom/mopub/volley/Request<*>;>;"
    invoke-direct {p0}, Ljava/lang/Thread;-><init>()V

    .line 49
    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/mopub/volley/CacheDispatcher;->mQuit:Z

    .line 63
    iput-object p1, p0, Lcom/mopub/volley/CacheDispatcher;->mCacheQueue:Ljava/util/concurrent/BlockingQueue;

    .line 64
    iput-object p2, p0, Lcom/mopub/volley/CacheDispatcher;->mNetworkQueue:Ljava/util/concurrent/BlockingQueue;

    .line 65
    iput-object p3, p0, Lcom/mopub/volley/CacheDispatcher;->mCache:Lcom/mopub/volley/Cache;

    .line 66
    iput-object p4, p0, Lcom/mopub/volley/CacheDispatcher;->mDelivery:Lcom/mopub/volley/ResponseDelivery;

    .line 67
    return-void
.end method

.method static synthetic access$000(Lcom/mopub/volley/CacheDispatcher;)Ljava/util/concurrent/BlockingQueue;
    .locals 1
    .param p0, "x0"    # Lcom/mopub/volley/CacheDispatcher;

    .prologue
    .line 32
    iget-object v0, p0, Lcom/mopub/volley/CacheDispatcher;->mNetworkQueue:Ljava/util/concurrent/BlockingQueue;

    return-object v0
.end method


# virtual methods
.method public quit()V
    .locals 1

    .prologue
    .line 74
    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/mopub/volley/CacheDispatcher;->mQuit:Z

    .line 75
    invoke-virtual {p0}, Lcom/mopub/volley/CacheDispatcher;->interrupt()V

    .line 76
    return-void
.end method

.method public run()V
    .locals 7

    .prologue
    .line 80
    sget-boolean v4, Lcom/mopub/volley/CacheDispatcher;->DEBUG:Z

    if-eqz v4, :cond_0

    const-string v4, "start new dispatcher"

    const/4 v5, 0x0

    new-array v5, v5, [Ljava/lang/Object;

    invoke-static {v4, v5}, Lcom/mopub/volley/VolleyLog;->v(Ljava/lang/String;[Ljava/lang/Object;)V

    .line 81
    :cond_0
    const/16 v4, 0xa

    invoke-static {v4}, Landroid/os/Process;->setThreadPriority(I)V

    .line 84
    iget-object v4, p0, Lcom/mopub/volley/CacheDispatcher;->mCache:Lcom/mopub/volley/Cache;

    invoke-interface {v4}, Lcom/mopub/volley/Cache;->initialize()V

    .line 90
    :cond_1
    :goto_0
    :try_start_0
    iget-object v4, p0, Lcom/mopub/volley/CacheDispatcher;->mCacheQueue:Ljava/util/concurrent/BlockingQueue;

    invoke-interface {v4}, Ljava/util/concurrent/BlockingQueue;->take()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lcom/mopub/volley/Request;

    .line 91
    .local v2, "request":Lcom/mopub/volley/Request;, "Lcom/mopub/volley/Request<*>;"
    const-string v4, "cache-queue-take"

    invoke-virtual {v2, v4}, Lcom/mopub/volley/Request;->addMarker(Ljava/lang/String;)V

    .line 94
    invoke-virtual {v2}, Lcom/mopub/volley/Request;->isCanceled()Z

    move-result v4

    if-eqz v4, :cond_2

    .line 95
    const-string v4, "cache-discard-canceled"

    invoke-virtual {v2, v4}, Lcom/mopub/volley/Request;->finish(Ljava/lang/String;)V
    :try_end_0
    .catch Ljava/lang/InterruptedException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    .line 149
    .end local v2    # "request":Lcom/mopub/volley/Request;, "Lcom/mopub/volley/Request<*>;"
    :catch_0
    move-exception v0

    .line 151
    .local v0, "e":Ljava/lang/InterruptedException;
    iget-boolean v4, p0, Lcom/mopub/volley/CacheDispatcher;->mQuit:Z

    if-eqz v4, :cond_1

    .line 152
    return-void

    .line 100
    .end local v0    # "e":Ljava/lang/InterruptedException;
    .restart local v2    # "request":Lcom/mopub/volley/Request;, "Lcom/mopub/volley/Request<*>;"
    :cond_2
    :try_start_1
    iget-object v4, p0, Lcom/mopub/volley/CacheDispatcher;->mCache:Lcom/mopub/volley/Cache;

    invoke-virtual {v2}, Lcom/mopub/volley/Request;->getCacheKey()Ljava/lang/String;

    move-result-object v5

    invoke-interface {v4, v5}, Lcom/mopub/volley/Cache;->get(Ljava/lang/String;)Lcom/mopub/volley/Cache$Entry;

    move-result-object v1

    .line 101
    .local v1, "entry":Lcom/mopub/volley/Cache$Entry;
    if-nez v1, :cond_3

    .line 102
    const-string v4, "cache-miss"

    invoke-virtual {v2, v4}, Lcom/mopub/volley/Request;->addMarker(Ljava/lang/String;)V

    .line 104
    iget-object v4, p0, Lcom/mopub/volley/CacheDispatcher;->mNetworkQueue:Ljava/util/concurrent/BlockingQueue;

    invoke-interface {v4, v2}, Ljava/util/concurrent/BlockingQueue;->put(Ljava/lang/Object;)V

    goto :goto_0

    .line 109
    :cond_3
    invoke-virtual {v1}, Lcom/mopub/volley/Cache$Entry;->isExpired()Z

    move-result v4

    if-eqz v4, :cond_4

    .line 110
    const-string v4, "cache-hit-expired"

    invoke-virtual {v2, v4}, Lcom/mopub/volley/Request;->addMarker(Ljava/lang/String;)V

    .line 111
    invoke-virtual {v2, v1}, Lcom/mopub/volley/Request;->setCacheEntry(Lcom/mopub/volley/Cache$Entry;)Lcom/mopub/volley/Request;

    .line 112
    iget-object v4, p0, Lcom/mopub/volley/CacheDispatcher;->mNetworkQueue:Ljava/util/concurrent/BlockingQueue;

    invoke-interface {v4, v2}, Ljava/util/concurrent/BlockingQueue;->put(Ljava/lang/Object;)V

    goto :goto_0

    .line 117
    :cond_4
    const-string v4, "cache-hit"

    invoke-virtual {v2, v4}, Lcom/mopub/volley/Request;->addMarker(Ljava/lang/String;)V

    .line 118
    new-instance v4, Lcom/mopub/volley/NetworkResponse;

    iget-object v5, v1, Lcom/mopub/volley/Cache$Entry;->data:[B

    iget-object v6, v1, Lcom/mopub/volley/Cache$Entry;->responseHeaders:Ljava/util/Map;

    invoke-direct {v4, v5, v6}, Lcom/mopub/volley/NetworkResponse;-><init>([BLjava/util/Map;)V

    invoke-virtual {v2, v4}, Lcom/mopub/volley/Request;->parseNetworkResponse(Lcom/mopub/volley/NetworkResponse;)Lcom/mopub/volley/Response;

    move-result-object v3

    .line 120
    .local v3, "response":Lcom/mopub/volley/Response;, "Lcom/mopub/volley/Response<*>;"
    const-string v4, "cache-hit-parsed"

    invoke-virtual {v2, v4}, Lcom/mopub/volley/Request;->addMarker(Ljava/lang/String;)V

    .line 122
    invoke-virtual {v1}, Lcom/mopub/volley/Cache$Entry;->refreshNeeded()Z

    move-result v4

    if-nez v4, :cond_5

    .line 124
    iget-object v4, p0, Lcom/mopub/volley/CacheDispatcher;->mDelivery:Lcom/mopub/volley/ResponseDelivery;

    invoke-interface {v4, v2, v3}, Lcom/mopub/volley/ResponseDelivery;->postResponse(Lcom/mopub/volley/Request;Lcom/mopub/volley/Response;)V

    goto :goto_0

    .line 129
    :cond_5
    const-string v4, "cache-hit-refresh-needed"

    invoke-virtual {v2, v4}, Lcom/mopub/volley/Request;->addMarker(Ljava/lang/String;)V

    .line 130
    invoke-virtual {v2, v1}, Lcom/mopub/volley/Request;->setCacheEntry(Lcom/mopub/volley/Cache$Entry;)Lcom/mopub/volley/Request;

    .line 133
    const/4 v4, 0x1

    iput-boolean v4, v3, Lcom/mopub/volley/Response;->intermediate:Z

    .line 137
    iget-object v4, p0, Lcom/mopub/volley/CacheDispatcher;->mDelivery:Lcom/mopub/volley/ResponseDelivery;

    new-instance v5, Lcom/mopub/volley/CacheDispatcher$1;

    invoke-direct {v5, p0, v2}, Lcom/mopub/volley/CacheDispatcher$1;-><init>(Lcom/mopub/volley/CacheDispatcher;Lcom/mopub/volley/Request;)V

    invoke-interface {v4, v2, v3, v5}, Lcom/mopub/volley/ResponseDelivery;->postResponse(Lcom/mopub/volley/Request;Lcom/mopub/volley/Response;Ljava/lang/Runnable;)V
    :try_end_1
    .catch Ljava/lang/InterruptedException; {:try_start_1 .. :try_end_1} :catch_0

    goto/16 :goto_0
.end method

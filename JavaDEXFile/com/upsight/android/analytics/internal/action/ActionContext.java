package com.upsight.android.analytics.internal.action;

import com.google.gson.Gson;
import com.squareup.otto.Bus;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.logger.UpsightLogger;
import rx.Scheduler.Worker;

public class ActionContext {
    public final Bus mBus;
    public final Clock mClock;
    public final Gson mGson;
    public final UpsightLogger mLogger;
    public final Worker mMainWorker;
    public final UpsightContext mUpsight;

    public ActionContext(UpsightContext upsightContext, Bus bus, Gson gson, Clock clock, Worker worker, UpsightLogger upsightLogger) {
        this.mUpsight = upsightContext;
        this.mBus = bus;
        this.mGson = gson;
        this.mClock = clock;
        this.mMainWorker = worker;
        this.mLogger = upsightLogger;
    }
}

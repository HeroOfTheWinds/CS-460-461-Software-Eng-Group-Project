package com.upsight.android.marketing.internal.content;

import android.view.View;
import com.squareup.otto.Bus;
import com.upsight.android.analytics.internal.action.ActionMap;
import com.upsight.android.analytics.internal.action.Actionable;
import com.upsight.android.marketing.UpsightContentMediator;
import com.upsight.android.marketing.internal.billboard.Billboard;
import com.upsight.android.marketing.internal.content.MarketingContentActions.MarketingContentActionContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class MarketingContent<T> extends Actionable {
    public static final String TRIGGER_APP_BACKGROUNDED = "app_backgrounded";
    public static final String TRIGGER_CONTENT_DISMISSED = "content_dismissed";
    public static final String TRIGGER_CONTENT_DISMISSED_WITH_REWARD = "content_dismissed_with_reward";
    public static final String TRIGGER_CONTENT_DISPLAYED = "content_displayed";
    public static final String TRIGGER_CONTENT_RECEIVED = "content_received";
    private AvailabilityEvent mAvailabilityEvent;
    private Billboard mBillboard;
    private UpsightContentMediator mContentMediator;
    private T mContentModel;
    private View mContentView;
    private Queue<Object> mEventQueue;
    private Map<String, String> mExtras;
    private boolean mIsLoaded;
    private boolean mIsRewardGranted;
    private PendingDialog mPendingDialog;

    public static abstract class AvailabilityEvent {
        private final String mId;

        private AvailabilityEvent(String str) {
            this.mId = str;
        }

        public String getId() {
            return this.mId;
        }
    }

    public static class ContentLoadedEvent {
        private final String mId;

        private ContentLoadedEvent(String str) {
            this.mId = str;
        }

        public String getId() {
            return this.mId;
        }
    }

    public static class PendingDialog {
        public static final String TEXT = "text";
        public static final String TRIGGER = "trigger";
        public final String mButtons;
        public final String mDismissTrigger;
        public final String mId;
        public final String mMessage;
        public final String mTitle;

        public PendingDialog(String str, String str2, String str3, String str4, String str5) {
            this.mId = str;
            this.mTitle = str2;
            this.mMessage = str3;
            this.mButtons = str4;
            this.mDismissTrigger = str5;
        }
    }

    public static class ScopedAvailabilityEvent extends AvailabilityEvent {
        private final String[] mScopes;

        public ScopedAvailabilityEvent(String str, String[] strArr) {
            super(null);
            this.mScopes = strArr;
        }

        public List<String> getScopes() {
            return Arrays.asList(this.mScopes);
        }
    }

    public static class ScopelessAvailabilityEvent extends AvailabilityEvent {
        private final String mParentId;

        public ScopelessAvailabilityEvent(String str, String str2) {
            super(null);
            this.mParentId = str2;
        }

        public String getParentId() {
            return this.mParentId;
        }
    }

    public static class SubcontentAvailabilityEvent extends AvailabilityEvent {
        public SubcontentAvailabilityEvent(String str) {
            super(null);
        }
    }

    public static class SubdialogAvailabilityEvent extends AvailabilityEvent {
        private final PendingDialog mPendingDialog;

        public SubdialogAvailabilityEvent(String str, PendingDialog pendingDialog) {
            super(null);
            this.mPendingDialog = pendingDialog;
        }

        public PendingDialog getPendingDialog() {
            return this.mPendingDialog;
        }
    }

    private MarketingContent(String str, ActionMap<MarketingContent, MarketingContentActionContext> actionMap) {
        super(str, actionMap);
        this.mContentMediator = null;
        this.mContentModel = null;
        this.mContentView = null;
        this.mIsLoaded = false;
        this.mIsRewardGranted = false;
        this.mEventQueue = new LinkedBlockingQueue();
        this.mBillboard = null;
        this.mExtras = new HashMap();
    }

    public static MarketingContent create(String str, ActionMap<MarketingContent, MarketingContentActionContext> actionMap) {
        return new MarketingContent(str, actionMap);
    }

    private void notifyAvailability(Bus bus) {
        if (isAvailable()) {
            bus.post(this.mAvailabilityEvent);
        }
    }

    public void addPendingDialog(PendingDialog pendingDialog) {
        this.mPendingDialog = pendingDialog;
    }

    public void bindBillboard(Billboard billboard) {
        this.mBillboard = billboard;
    }

    public Billboard getBoundBillboard() {
        return this.mBillboard;
    }

    public UpsightContentMediator getContentMediator() {
        return this.mContentMediator;
    }

    public T getContentModel() {
        return this.mContentModel;
    }

    public View getContentView() {
        return this.mContentView;
    }

    public Queue<Object> getEventQueue() {
        return this.mEventQueue;
    }

    public String getExtra(String str) {
        return (String) this.mExtras.get(str);
    }

    public boolean hasPendingDialog() {
        return this.mPendingDialog != null;
    }

    public boolean isAvailable() {
        return isLoaded() && this.mAvailabilityEvent != null && getBoundBillboard() == null;
    }

    boolean isLoaded() {
        return this.mContentModel != null && this.mIsLoaded;
    }

    public boolean isRewardGranted() {
        return this.mIsRewardGranted;
    }

    public void markLoaded(Bus bus) {
        this.mIsLoaded = true;
        bus.post(new ContentLoadedEvent(null));
        notifyAvailability(bus);
    }

    public void markPresentable(AvailabilityEvent availabilityEvent, Bus bus) {
        this.mAvailabilityEvent = availabilityEvent;
        notifyAvailability(bus);
    }

    public void markRewardGranted() {
        this.mIsRewardGranted = true;
    }

    public PendingDialog popPendingDialog() {
        PendingDialog pendingDialog = this.mPendingDialog;
        this.mPendingDialog = null;
        return pendingDialog;
    }

    public void putExtra(String str, String str2) {
        this.mExtras.put(str, str2);
    }

    public void setContentMediator(UpsightContentMediator upsightContentMediator) {
        this.mContentMediator = upsightContentMediator;
    }

    public void setContentModel(T t) {
        this.mContentModel = t;
    }

    public void setContentView(View view) {
        this.mContentView = view;
    }
}

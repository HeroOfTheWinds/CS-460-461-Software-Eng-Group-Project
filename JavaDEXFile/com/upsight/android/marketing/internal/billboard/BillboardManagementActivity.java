package com.upsight.android.marketing.internal.billboard;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ExploreByTouchHelper;
import android.text.TextUtils;
import com.squareup.otto.Subscribe;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightMarketingExtension;
import com.upsight.android.analytics.internal.session.ApplicationStatus;
import com.upsight.android.analytics.internal.session.ApplicationStatus.State;
import com.upsight.android.marketing.C0949R;
import com.upsight.android.marketing.UpsightBillboard.PresentationStyle;
import com.upsight.android.marketing.UpsightContentMediator;
import com.upsight.android.marketing.UpsightMarketingComponent;
import com.upsight.android.marketing.internal.content.MarketingContent;
import com.upsight.android.marketing.internal.content.MarketingContent.PendingDialog;
import com.upsight.android.marketing.internal.content.MarketingContent.ScopelessAvailabilityEvent;
import com.upsight.android.marketing.internal.content.MarketingContent.SubcontentAvailabilityEvent;
import com.upsight.android.marketing.internal.content.MarketingContent.SubdialogAvailabilityEvent;
import com.upsight.android.marketing.internal.content.MarketingContentActions.DestroyEvent;
import com.upsight.android.marketing.internal.content.MarketingContentStore;
import com.upsight.android.persistence.UpsightSubscription;
import com.upsight.android.persistence.annotation.Created;
import com.upsight.android.persistence.annotation.Updated;
import java.util.Queue;
import java.util.Set;
import javax.inject.Inject;
import spacemadness.com.lunarconsole.C1518R;

public class BillboardManagementActivity extends Activity {
    static final String INTENT_EXTRA_MARKETING_CONTENT_DIALOG_THEME = "marketingContentDialogTheme";
    static final String INTENT_EXTRA_MARKETING_CONTENT_ID = "marketingContentId";
    static final String INTENT_EXTRA_MARKETING_CONTENT_PREFERRED_STYLE = "marketingContentPreferredStyle";
    private static final String LOG_TAG = "BillboardActivity";
    private static final int STYLE_DIALOG;
    private static final int STYLE_FULLSCREEN = 16974122;
    private Billboard mBillboard;
    private MarketingContent mContent;
    @Inject
    MarketingContentStore mContentStore;
    private UpsightSubscription mDataStoreSubscription;
    private BillboardFragment mFragment;
    private boolean mIsForeground;
    private boolean mShouldAttachOnResume;
    @Inject
    UpsightContext mUpsight;

    /* renamed from: com.upsight.android.marketing.internal.billboard.BillboardManagementActivity.1 */
    static /* synthetic */ class C09591 {
        static final /* synthetic */ int[] f260x10de5d48;

        static {
            f260x10de5d48 = new int[PresentationStyle.values().length];
            try {
                f260x10de5d48[PresentationStyle.Dialog.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f260x10de5d48[PresentationStyle.Fullscreen.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    static {
        STYLE_DIALOG = C0949R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_UpsightDialog;
    }

    public BillboardManagementActivity() {
        this.mContent = null;
        this.mBillboard = null;
        this.mFragment = null;
        this.mDataStoreSubscription = null;
        this.mIsForeground = false;
        this.mShouldAttachOnResume = false;
    }

    private void handle(ScopelessAvailabilityEvent scopelessAvailabilityEvent) {
        MarketingContent marketingContent = this.mContent;
        if (marketingContent != null && marketingContent.getId().equals(scopelessAvailabilityEvent.getParentId())) {
            Billboard billboard = this.mBillboard;
            if (billboard != null) {
                MarketingContent marketingContent2 = (MarketingContent) this.mContentStore.get(scopelessAvailabilityEvent.getId());
                if (marketingContent2 != null && marketingContent2.isAvailable()) {
                    UpsightContentMediator contentMediator = marketingContent.getContentMediator();
                    UpsightContentMediator contentMediator2 = marketingContent2.getContentMediator();
                    if (contentMediator != null && contentMediator2 != null) {
                        this.mContent = marketingContent2;
                        marketingContent2.bindBillboard(billboard);
                        billboard.getHandler().onNextView();
                        FragmentManager fragmentManager = getFragmentManager();
                        contentMediator.hideContent(marketingContent, fragmentManager, this.mFragment);
                        contentMediator2.displayContent(marketingContent2, fragmentManager, this.mFragment);
                    }
                }
            }
        }
    }

    private void handle(SubcontentAvailabilityEvent subcontentAvailabilityEvent) {
        MarketingContent marketingContent = this.mContent;
        if (marketingContent != null && subcontentAvailabilityEvent.getId().equals(marketingContent.getId())) {
            attachBillboard(marketingContent);
        }
    }

    private void handle(SubdialogAvailabilityEvent subdialogAvailabilityEvent) {
        MarketingContent marketingContent = this.mContent;
        if (marketingContent != null && subdialogAvailabilityEvent.getId().equals(marketingContent.getId())) {
            attachDialog(subdialogAvailabilityEvent.getPendingDialog());
        }
    }

    private void handle(DestroyEvent destroyEvent) {
        MarketingContent marketingContent = this.mContent;
        if (marketingContent != null && marketingContent.getId().equals(destroyEvent.mId)) {
            detachBillboard();
        }
    }

    void attachBillboard(MarketingContent marketingContent) {
        this.mUpsight.getLogger().m205d(LOG_TAG, "Attach billboard activity=" + this + " marketingContentId=" + marketingContent.getId(), new Object[STYLE_DIALOG]);
        UpsightContentMediator contentMediator = marketingContent.getContentMediator();
        if (contentMediator != null) {
            int i;
            Set dimensions;
            PresentationStyle presentationStyle = (PresentationStyle) getIntent().getSerializableExtra(INTENT_EXTRA_MARKETING_CONTENT_PREFERRED_STYLE);
            if (presentationStyle == null || presentationStyle.equals(PresentationStyle.None)) {
                presentationStyle = contentMediator.getPresentationStyle(marketingContent);
            }
            switch (C09591.f260x10de5d48[presentationStyle.ordinal()]) {
                case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    i = STYLE_DIALOG;
                    dimensions = contentMediator.getDimensions(marketingContent);
                    break;
                default:
                    i = STYLE_FULLSCREEN;
                    dimensions = null;
                    break;
            }
            this.mFragment = BillboardFragment.newInstance(this, dimensions);
            this.mFragment.setStyle(1, i);
            this.mFragment.setCancelable(false);
            contentMediator.displayContent(marketingContent, getFragmentManager(), this.mFragment);
        }
    }

    void attachDialog(PendingDialog pendingDialog) {
        this.mUpsight.getLogger().m205d(LOG_TAG, "Attach dialog activity=" + this + " marketingContentId=" + pendingDialog.mId, new Object[STYLE_DIALOG]);
        Intent intent = getIntent();
        if (intent.hasExtra(INTENT_EXTRA_MARKETING_CONTENT_DIALOG_THEME)) {
            BillboardDialogFragment.newInstance(pendingDialog, intent.getIntExtra(INTENT_EXTRA_MARKETING_CONTENT_DIALOG_THEME, ExploreByTouchHelper.INVALID_ID)).show(getFragmentManager(), null);
        } else {
            BillboardDialogFragment.newInstance(pendingDialog).show(getFragmentManager(), null);
        }
    }

    void detachBillboard() {
        Billboard billboard = this.mBillboard;
        if (billboard != null) {
            this.mUpsight.getLogger().m205d(LOG_TAG, "Detach billboard activity=" + this + " scope=" + billboard.getScope(), new Object[STYLE_DIALOG]);
            DialogFragment dialogFragment = this.mFragment;
            if (dialogFragment != null && dialogFragment.isAdded()) {
                dialogFragment.dismiss();
            }
            this.mContent = null;
            this.mBillboard = null;
            this.mFragment = null;
            finish();
            billboard.getHandler().onDetach();
        }
    }

    @Subscribe
    public void handleActionEvent(DestroyEvent destroyEvent) {
        this.mUpsight.getLogger().m205d(LOG_TAG, "Received DestroyEvent activity=" + this + " marketingContentId=" + destroyEvent.mId, new Object[STYLE_DIALOG]);
        if (this.mIsForeground) {
            handle(destroyEvent);
            return;
        }
        MarketingContent marketingContent = this.mContent;
        if (marketingContent != null) {
            marketingContent.getEventQueue().add(destroyEvent);
        }
    }

    @Subscribe
    public void handleAvailabilityEvent(ScopelessAvailabilityEvent scopelessAvailabilityEvent) {
        this.mUpsight.getLogger().m205d(LOG_TAG, "Received ScopelessAvailabilityEvent activity=" + this + " marketingContentId=" + scopelessAvailabilityEvent.getId(), new Object[STYLE_DIALOG]);
        if (this.mIsForeground) {
            handle(scopelessAvailabilityEvent);
            return;
        }
        MarketingContent marketingContent = this.mContent;
        if (marketingContent != null) {
            marketingContent.getEventQueue().add(scopelessAvailabilityEvent);
        }
    }

    @Subscribe
    public void handleAvailabilityEvent(SubcontentAvailabilityEvent subcontentAvailabilityEvent) {
        this.mUpsight.getLogger().m205d(LOG_TAG, "Received SubcontentAvailabilityEvent activity=" + this + " marketingContentId=" + subcontentAvailabilityEvent.getId(), new Object[STYLE_DIALOG]);
        if (this.mIsForeground) {
            handle(subcontentAvailabilityEvent);
            return;
        }
        MarketingContent marketingContent = this.mContent;
        if (marketingContent != null) {
            marketingContent.getEventQueue().add(subcontentAvailabilityEvent);
        }
    }

    @Subscribe
    public void handleAvailabilityEvent(SubdialogAvailabilityEvent subdialogAvailabilityEvent) {
        this.mUpsight.getLogger().m205d(LOG_TAG, "Received SubdialogAvailabilityEvent activity=" + this + " marketingContentId=" + subdialogAvailabilityEvent.getId(), new Object[STYLE_DIALOG]);
        if (this.mIsForeground) {
            handle(subdialogAvailabilityEvent);
            return;
        }
        MarketingContent marketingContent = this.mContent;
        if (marketingContent != null) {
            marketingContent.getEventQueue().add(subdialogAvailabilityEvent);
        }
    }

    @Created
    @Updated
    public void onApplicationStatus(ApplicationStatus applicationStatus) {
        if (applicationStatus.getState() == State.BACKGROUND) {
            this.mUpsight.getLogger().m205d(LOG_TAG, "Received application background event activity=" + this, new Object[STYLE_DIALOG]);
            MarketingContent marketingContent = this.mContent;
            if (marketingContent != null) {
                marketingContent.executeActions(MarketingContent.TRIGGER_APP_BACKGROUNDED);
            }
        }
    }

    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof BillboardFragment) {
            this.mFragment = (BillboardFragment) fragment;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        UpsightMarketingExtension upsightMarketingExtension = (UpsightMarketingExtension) Upsight.createContext(this).getUpsightExtension(UpsightMarketingExtension.EXTENSION_NAME);
        if (upsightMarketingExtension != null) {
            ((UpsightMarketingComponent) upsightMarketingExtension.getComponent()).inject(this);
            setContentView(C0949R.layout.upsight_activity_billboard_management);
            Object stringExtra = getIntent().getStringExtra(INTENT_EXTRA_MARKETING_CONTENT_ID);
            this.mUpsight.getLogger().m205d(LOG_TAG, "onCreate activity=" + this + " marketingContentId=" + stringExtra, new Object[STYLE_DIALOG]);
            if (TextUtils.isEmpty(stringExtra)) {
                finish();
                return;
            }
            MarketingContent marketingContent = (MarketingContent) this.mContentStore.get(stringExtra);
            if (marketingContent != null) {
                this.mContent = marketingContent;
                this.mBillboard = marketingContent.getBoundBillboard();
                this.mShouldAttachOnResume = bundle == null;
            } else {
                finish();
            }
            this.mDataStoreSubscription = this.mUpsight.getDataStore().subscribe(this);
            this.mUpsight.getCoreComponent().bus().register(this);
        }
    }

    protected void onDestroy() {
        this.mDataStoreSubscription.unsubscribe();
        this.mUpsight.getCoreComponent().bus().unregister(this);
        this.mUpsight.getLogger().m205d(LOG_TAG, "onDestroy activity=" + this, new Object[STYLE_DIALOG]);
        super.onDestroy();
    }

    protected void onPause() {
        this.mIsForeground = false;
        this.mUpsight.getLogger().m205d(LOG_TAG, "onPause activity=" + this, new Object[STYLE_DIALOG]);
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        this.mIsForeground = true;
        MarketingContent marketingContent = this.mContent;
        if (marketingContent != null) {
            if (this.mShouldAttachOnResume) {
                this.mShouldAttachOnResume = false;
                if (marketingContent.hasPendingDialog()) {
                    attachDialog(marketingContent.popPendingDialog());
                } else {
                    attachBillboard(marketingContent);
                }
            }
            Queue eventQueue = marketingContent.getEventQueue();
            this.mUpsight.getLogger().m205d(LOG_TAG, "onResume activity=" + this + " eventQueueSize=" + eventQueue.size(), new Object[STYLE_DIALOG]);
            while (!eventQueue.isEmpty()) {
                Object poll = eventQueue.poll();
                if (poll instanceof SubcontentAvailabilityEvent) {
                    handle((SubcontentAvailabilityEvent) poll);
                } else if (poll instanceof SubdialogAvailabilityEvent) {
                    handle((SubdialogAvailabilityEvent) poll);
                } else if (poll instanceof ScopelessAvailabilityEvent) {
                    handle((ScopelessAvailabilityEvent) poll);
                } else if (poll instanceof DestroyEvent) {
                    handle((DestroyEvent) poll);
                }
            }
        }
    }
}

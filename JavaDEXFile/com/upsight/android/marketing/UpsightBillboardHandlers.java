package com.upsight.android.marketing;

import android.app.Activity;
import com.upsight.android.marketing.UpsightBillboard.AttachParameters;
import com.upsight.android.marketing.UpsightBillboard.Handler;
import com.upsight.android.marketing.UpsightBillboard.PresentationStyle;
import java.lang.ref.WeakReference;
import java.util.List;

public final class UpsightBillboardHandlers {

    private static abstract class SimpleHandler implements Handler {
        private WeakReference<Activity> mActivity;

        protected SimpleHandler(Activity activity) {
            this.mActivity = new WeakReference(activity);
        }

        Activity getActivity() {
            return (Activity) this.mActivity.get();
        }

        public void onDetach() {
        }

        public void onNextView() {
        }

        public void onPurchases(List<UpsightPurchase> list) {
        }

        public void onRewards(List<UpsightReward> list) {
        }
    }

    public static class DefaultHandler extends SimpleHandler {
        public DefaultHandler(Activity activity) {
            super(activity);
        }

        public AttachParameters onAttach(String str) {
            Activity activity = getActivity();
            return (activity == null || activity.isFinishing()) ? null : new AttachParameters(activity).putPreferredPresentationStyle(PresentationStyle.None);
        }

        public /* bridge */ /* synthetic */ void onDetach() {
            super.onDetach();
        }

        public /* bridge */ /* synthetic */ void onNextView() {
            super.onNextView();
        }

        public /* bridge */ /* synthetic */ void onPurchases(List list) {
            super.onPurchases(list);
        }

        public /* bridge */ /* synthetic */ void onRewards(List list) {
            super.onRewards(list);
        }
    }

    public static class DialogHandler extends SimpleHandler {
        public DialogHandler(Activity activity) {
            super(activity);
        }

        public AttachParameters onAttach(String str) {
            Activity activity = getActivity();
            return (activity == null || activity.isFinishing()) ? null : new AttachParameters(activity).putPreferredPresentationStyle(PresentationStyle.Dialog);
        }

        public /* bridge */ /* synthetic */ void onDetach() {
            super.onDetach();
        }

        public /* bridge */ /* synthetic */ void onNextView() {
            super.onNextView();
        }

        public /* bridge */ /* synthetic */ void onPurchases(List list) {
            super.onPurchases(list);
        }

        public /* bridge */ /* synthetic */ void onRewards(List list) {
            super.onRewards(list);
        }
    }

    public static class FullscreenHandler extends SimpleHandler {
        public FullscreenHandler(Activity activity) {
            super(activity);
        }

        public AttachParameters onAttach(String str) {
            Activity activity = getActivity();
            return (activity == null || activity.isFinishing()) ? null : new AttachParameters(activity).putPreferredPresentationStyle(PresentationStyle.Fullscreen);
        }

        public /* bridge */ /* synthetic */ void onDetach() {
            super.onDetach();
        }

        public /* bridge */ /* synthetic */ void onNextView() {
            super.onNextView();
        }

        public /* bridge */ /* synthetic */ void onPurchases(List list) {
            super.onPurchases(list);
        }

        public /* bridge */ /* synthetic */ void onRewards(List list) {
            super.onRewards(list);
        }
    }

    public static Handler forDefault(Activity activity) {
        return new DefaultHandler(activity);
    }

    public static Handler forDialog(Activity activity) {
        return new DialogHandler(activity);
    }

    public static Handler forFullscreen(Activity activity) {
        return new FullscreenHandler(activity);
    }
}

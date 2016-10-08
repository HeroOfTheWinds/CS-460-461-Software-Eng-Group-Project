package com.upsight.android.marketing;

import android.app.Activity;
import android.text.TextUtils;
import com.upsight.android.UpsightContext;
import com.upsight.android.marketing.internal.billboard.Billboard;
import java.util.List;

public abstract class UpsightBillboard {

    public static class AttachParameters {
        private Activity mActivity;
        private Integer mDialogTheme;
        private PresentationStyle mPresentationStyle;

        public AttachParameters(Activity activity) {
            this.mActivity = activity;
        }

        public Activity getActivity() {
            return this.mActivity;
        }

        public Integer getDialogTheme() {
            return this.mDialogTheme;
        }

        public PresentationStyle getPreferredPresentationStyle() {
            return this.mPresentationStyle;
        }

        public AttachParameters putDialogTheme(int i) {
            this.mDialogTheme = Integer.valueOf(i);
            return this;
        }

        public AttachParameters putPreferredPresentationStyle(PresentationStyle presentationStyle) {
            this.mPresentationStyle = presentationStyle;
            return this;
        }
    }

    public static class Dimensions {
        public final int height;
        public final LayoutOrientation layout;
        public final int width;

        public enum LayoutOrientation {
            Portrait,
            Landscape
        }

        public Dimensions(LayoutOrientation layoutOrientation, int i, int i2) {
            this.layout = layoutOrientation;
            this.width = i;
            this.height = i2;
        }
    }

    public interface Handler {
        AttachParameters onAttach(String str);

        void onDetach();

        void onNextView();

        void onPurchases(List<UpsightPurchase> list);

        void onRewards(List<UpsightReward> list);
    }

    public enum PresentationStyle {
        None,
        Dialog,
        Fullscreen
    }

    public static UpsightBillboard create(UpsightContext upsightContext, String str, Handler handler) throws IllegalArgumentException, IllegalStateException {
        if (!TextUtils.isEmpty(str) || handler == null) {
            UpsightBillboard billboard = new Billboard(str, handler);
            billboard.setUp(upsightContext);
            return billboard;
        }
        throw new IllegalArgumentException("The billboard scope and handler must be non-null and non-empty.");
    }

    public abstract void destroy();

    protected abstract UpsightBillboard setUp(UpsightContext upsightContext) throws IllegalStateException;
}

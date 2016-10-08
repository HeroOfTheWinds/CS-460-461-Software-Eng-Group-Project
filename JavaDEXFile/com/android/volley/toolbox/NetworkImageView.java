package com.android.volley.toolbox;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;

public class NetworkImageView extends ImageView {
    private int mDefaultImageId;
    private int mErrorImageId;
    private ImageContainer mImageContainer;
    private ImageLoader mImageLoader;
    private String mUrl;

    /* renamed from: com.android.volley.toolbox.NetworkImageView.1 */
    class C01461 implements ImageListener {
        final /* synthetic */ boolean val$isInLayoutPass;

        /* renamed from: com.android.volley.toolbox.NetworkImageView.1.1 */
        class C01451 implements Runnable {
            final /* synthetic */ ImageContainer val$response;

            C01451(ImageContainer imageContainer) {
                this.val$response = imageContainer;
            }

            public void run() {
                C01461.this.onResponse(this.val$response, false);
            }
        }

        C01461(boolean z) {
            this.val$isInLayoutPass = z;
        }

        public void onErrorResponse(VolleyError volleyError) {
            if (NetworkImageView.this.mErrorImageId != 0) {
                NetworkImageView.this.setImageResource(NetworkImageView.this.mErrorImageId);
            }
        }

        public void onResponse(ImageContainer imageContainer, boolean z) {
            if (z && this.val$isInLayoutPass) {
                NetworkImageView.this.post(new C01451(imageContainer));
            } else if (imageContainer.getBitmap() != null) {
                NetworkImageView.this.setImageBitmap(imageContainer.getBitmap());
            } else if (NetworkImageView.this.mDefaultImageId != 0) {
                NetworkImageView.this.setImageResource(NetworkImageView.this.mDefaultImageId);
            }
        }
    }

    public NetworkImageView(Context context) {
        this(context, null);
    }

    public NetworkImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NetworkImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    private void setDefaultImageOrNull() {
        if (this.mDefaultImageId != 0) {
            setImageResource(this.mDefaultImageId);
        } else {
            setImageBitmap(null);
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    void loadImageIfNecessary(boolean z) {
        Object obj;
        Object obj2;
        Object obj3 = 1;
        int width = getWidth();
        int height = getHeight();
        ScaleType scaleType = getScaleType();
        if (getLayoutParams() != null) {
            obj = getLayoutParams().width == -2 ? 1 : null;
            obj2 = getLayoutParams().height == -2 ? 1 : null;
        } else {
            obj = null;
            obj2 = null;
        }
        if (obj == null || obj2 == null) {
            obj3 = null;
        }
        if (width != 0 || height != 0 || r1 != null) {
            if (TextUtils.isEmpty(this.mUrl)) {
                if (this.mImageContainer != null) {
                    this.mImageContainer.cancelRequest();
                    this.mImageContainer = null;
                }
                setDefaultImageOrNull();
                return;
            }
            if (!(this.mImageContainer == null || this.mImageContainer.getRequestUrl() == null)) {
                if (!this.mImageContainer.getRequestUrl().equals(this.mUrl)) {
                    this.mImageContainer.cancelRequest();
                    setDefaultImageOrNull();
                } else {
                    return;
                }
            }
            if (obj != null) {
                width = 0;
            }
            if (obj2 != null) {
                height = 0;
            }
            this.mImageContainer = this.mImageLoader.get(this.mUrl, new C01461(z), width, height, scaleType);
        }
    }

    protected void onDetachedFromWindow() {
        if (this.mImageContainer != null) {
            this.mImageContainer.cancelRequest();
            setImageBitmap(null);
            this.mImageContainer = null;
        }
        super.onDetachedFromWindow();
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        loadImageIfNecessary(true);
    }

    public void setDefaultImageResId(int i) {
        this.mDefaultImageId = i;
    }

    public void setErrorImageResId(int i) {
        this.mErrorImageId = i;
    }

    public void setImageUrl(String str, ImageLoader imageLoader) {
        this.mUrl = str;
        this.mImageLoader = imageLoader;
        loadImageIfNecessary(false);
    }
}

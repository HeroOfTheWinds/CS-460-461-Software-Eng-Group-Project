package com.android.volley.toolbox;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.widget.ImageView.ScaleType;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Request.Priority;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;

public class ImageRequest extends Request<Bitmap> {
    public static final float DEFAULT_IMAGE_BACKOFF_MULT = 2.0f;
    public static final int DEFAULT_IMAGE_MAX_RETRIES = 2;
    public static final int DEFAULT_IMAGE_TIMEOUT_MS = 1000;
    private static final Object sDecodeLock;
    private final Config mDecodeConfig;
    private final Listener<Bitmap> mListener;
    private final int mMaxHeight;
    private final int mMaxWidth;
    private ScaleType mScaleType;

    static {
        sDecodeLock = new Object();
    }

    @Deprecated
    public ImageRequest(String str, Listener<Bitmap> listener, int i, int i2, Config config, ErrorListener errorListener) {
        this(str, listener, i, i2, ScaleType.CENTER_INSIDE, config, errorListener);
    }

    public ImageRequest(String str, Listener<Bitmap> listener, int i, int i2, ScaleType scaleType, Config config, ErrorListener errorListener) {
        super(0, str, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(DEFAULT_IMAGE_TIMEOUT_MS, DEFAULT_IMAGE_MAX_RETRIES, DEFAULT_IMAGE_BACKOFF_MULT));
        this.mListener = listener;
        this.mDecodeConfig = config;
        this.mMaxWidth = i;
        this.mMaxHeight = i2;
        this.mScaleType = scaleType;
    }

    private Response<Bitmap> doParse(NetworkResponse networkResponse) {
        Object decodeByteArray;
        byte[] bArr = networkResponse.data;
        Options options = new Options();
        if (this.mMaxWidth == 0 && this.mMaxHeight == 0) {
            options.inPreferredConfig = this.mDecodeConfig;
            decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
        } else {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
            int i = options.outWidth;
            int i2 = options.outHeight;
            int resizedDimension = getResizedDimension(this.mMaxWidth, this.mMaxHeight, i, i2, this.mScaleType);
            int resizedDimension2 = getResizedDimension(this.mMaxHeight, this.mMaxWidth, i2, i, this.mScaleType);
            options.inJustDecodeBounds = false;
            options.inSampleSize = findBestSampleSize(i, i2, resizedDimension, resizedDimension2);
            Bitmap decodeByteArray2 = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
            if (decodeByteArray2 == null || (decodeByteArray2.getWidth() <= resizedDimension && decodeByteArray2.getHeight() <= resizedDimension2)) {
                Bitmap bitmap = decodeByteArray2;
            } else {
                decodeByteArray = Bitmap.createScaledBitmap(decodeByteArray2, resizedDimension, resizedDimension2, true);
                decodeByteArray2.recycle();
            }
        }
        return decodeByteArray == null ? Response.error(new ParseError(networkResponse)) : Response.success(decodeByteArray, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    static int findBestSampleSize(int i, int i2, int i3, int i4) {
        double min = Math.min(((double) i) / ((double) i3), ((double) i2) / ((double) i4));
        float f = com.mopub.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        while (((double) (DEFAULT_IMAGE_BACKOFF_MULT * f)) <= min) {
            f *= DEFAULT_IMAGE_BACKOFF_MULT;
        }
        return (int) f;
    }

    private static int getResizedDimension(int i, int i2, int i3, int i4, ScaleType scaleType) {
        if (!(i == 0 && i2 == 0)) {
            if (scaleType == ScaleType.FIT_XY) {
                if (i != 0) {
                    return i;
                }
            } else if (i == 0) {
                return (int) ((((double) i2) / ((double) i4)) * ((double) i3));
            } else {
                if (i2 == 0) {
                    return i;
                }
                double d = ((double) i4) / ((double) i3);
                return scaleType == ScaleType.CENTER_CROP ? ((double) i) * d < ((double) i2) ? (int) (((double) i2) / d) : i : ((double) i) * d > ((double) i2) ? (int) (((double) i2) / d) : i;
            }
        }
        return i3;
    }

    protected void deliverResponse(Bitmap bitmap) {
        this.mListener.onResponse(bitmap);
    }

    public Priority getPriority() {
        return Priority.LOW;
    }

    protected Response<Bitmap> parseNetworkResponse(NetworkResponse networkResponse) {
        Response<Bitmap> doParse;
        synchronized (sDecodeLock) {
            try {
                doParse = doParse(networkResponse);
            } catch (Throwable e) {
                Object[] objArr = new Object[DEFAULT_IMAGE_MAX_RETRIES];
                objArr[0] = Integer.valueOf(networkResponse.data.length);
                objArr[1] = getUrl();
                VolleyLog.m17e("Caught OOM for %d byte image, url=%s", objArr);
                doParse = Response.error(new ParseError(e));
            }
        }
        return doParse;
    }
}

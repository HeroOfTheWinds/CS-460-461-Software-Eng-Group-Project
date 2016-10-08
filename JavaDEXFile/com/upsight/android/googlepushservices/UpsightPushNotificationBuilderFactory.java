package com.upsight.android.googlepushservices;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v4.app.NotificationCompat.BigPictureStyle;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ImageView.ScaleType;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NoCache;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

public interface UpsightPushNotificationBuilderFactory {

    public static class Default implements UpsightPushNotificationBuilderFactory {
        public static final float HTTP_REQUEST_BACKOFF_MULT = 2.0f;
        public static final int HTTP_REQUEST_MAX_RETRIES = 3;
        public static final int HTTP_REQUEST_TIMEOUT_MS = 5000;
        private RequestQueue mRequestQueue;

        /* renamed from: com.upsight.android.googlepushservices.UpsightPushNotificationBuilderFactory.Default.1 */
        class C08901 implements OnSubscribe<Bitmap> {
            final /* synthetic */ String val$imageUrl;

            /* renamed from: com.upsight.android.googlepushservices.UpsightPushNotificationBuilderFactory.Default.1.1 */
            class C08881 implements Listener<Bitmap> {
                final /* synthetic */ Subscriber val$subscriber;

                C08881(Subscriber subscriber) {
                    this.val$subscriber = subscriber;
                }

                public void onResponse(Bitmap bitmap) {
                    this.val$subscriber.onNext(bitmap);
                    this.val$subscriber.onCompleted();
                }
            }

            /* renamed from: com.upsight.android.googlepushservices.UpsightPushNotificationBuilderFactory.Default.1.2 */
            class C08892 implements ErrorListener {
                final /* synthetic */ Subscriber val$subscriber;

                C08892(Subscriber subscriber) {
                    this.val$subscriber = subscriber;
                }

                public void onErrorResponse(VolleyError volleyError) {
                    this.val$subscriber.onError(volleyError);
                }
            }

            C08901(String str) {
                this.val$imageUrl = str;
            }

            public void call(Subscriber<? super Bitmap> subscriber) {
                Default.this.mRequestQueue.add(new ImageRequest(this.val$imageUrl, new C08881(subscriber), 0, 0, ScaleType.CENTER_INSIDE, Config.ARGB_8888, new C08892(subscriber)).setRetryPolicy(new DefaultRetryPolicy(Default.HTTP_REQUEST_TIMEOUT_MS, Default.HTTP_REQUEST_MAX_RETRIES, Default.HTTP_REQUEST_BACKOFF_MULT)));
            }
        }

        public Default() {
            this.mRequestQueue = new RequestQueue(new NoCache(), new BasicNetwork(new HurlStack()));
            this.mRequestQueue.start();
        }

        protected Observable<Bitmap> getImageObservable(String str) {
            return Observable.create(new C08901(str));
        }

        public Builder getNotificationBuilder(UpsightContext upsightContext, String str, String str2, String str3) {
            Bitmap bitmap;
            if (TextUtils.isEmpty(str3)) {
                bitmap = null;
            } else if (isImageUrlValid(str3)) {
                try {
                    bitmap = (Bitmap) getImageObservable(str3).toBlocking().first();
                } catch (Throwable th) {
                    upsightContext.getLogger().m207e(Upsight.LOG_TAG, "Failed to download notification picture, displaying notification without", th);
                    bitmap = null;
                }
            } else {
                upsightContext.getLogger().m207e(Upsight.LOG_TAG, "Malformed notification picture URL, displaying notification without", new Object[0]);
                bitmap = null;
            }
            return new Builder(upsightContext.getApplicationContext()).setSmallIcon(upsightContext.getApplicationInfo().icon).setStyle(bitmap != null ? new BigPictureStyle().bigPicture(bitmap).setSummaryText(str2) : new BigTextStyle().bigText(str2)).setContentTitle(str).setContentText(str2);
        }

        protected boolean isImageUrlValid(String str) {
            return Patterns.WEB_URL.matcher(str).matches();
        }
    }

    Builder getNotificationBuilder(UpsightContext upsightContext, String str, String str2, String str3);
}

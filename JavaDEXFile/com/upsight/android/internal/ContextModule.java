package com.upsight.android.internal;

import android.content.Context;
import android.util.Log;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import com.upsight.android.internal.logger.LogWriter;
import com.upsight.android.internal.persistence.storable.StorableIdFactory;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.logger.UpsightLogger.Level;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import dagger.Module;
import dagger.Provides;
import java.util.UUID;
import javax.inject.Singleton;
import spacemadness.com.lunarconsole.C1518R;

@Module
public final class ContextModule {
    private final Context mApplicationContext;

    /* renamed from: com.upsight.android.internal.ContextModule.1 */
    class C09021 implements LogWriter {
        C09021() {
        }

        private void logMessage(String str, Level level, String str2) {
            switch (C09043.$SwitchMap$com$upsight$android$logger$UpsightLogger$Level[level.ordinal()]) {
                case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    Log.v(str, str2);
                case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
                    Log.d(str, str2);
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                    Log.i(str, str2);
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_LEFT /*4*/:
                    Log.w(str, str2);
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                    Log.e(str, str2);
                default:
            }
        }

        public void write(String str, Level level, String str2) {
            if (str2.length() > UpsightLogger.MAX_LENGTH) {
                int length = str2.length() / UpsightLogger.MAX_LENGTH;
                for (int i = 0; i <= length; i++) {
                    int i2 = (i + 1) * UpsightLogger.MAX_LENGTH;
                    if (i2 >= str2.length()) {
                        logMessage(str, level, str2.substring(i * UpsightLogger.MAX_LENGTH));
                    } else {
                        logMessage(str, level, str2.substring(i * UpsightLogger.MAX_LENGTH, i2));
                    }
                }
                return;
            }
            logMessage(str, level, str2);
        }
    }

    /* renamed from: com.upsight.android.internal.ContextModule.2 */
    class C09032 implements StorableIdFactory {
        C09032() {
        }

        public String createObjectID() {
            return UUID.randomUUID().toString();
        }
    }

    /* renamed from: com.upsight.android.internal.ContextModule.3 */
    static /* synthetic */ class C09043 {
        static final /* synthetic */ int[] $SwitchMap$com$upsight$android$logger$UpsightLogger$Level;

        static {
            $SwitchMap$com$upsight$android$logger$UpsightLogger$Level = new int[Level.values().length];
            try {
                $SwitchMap$com$upsight$android$logger$UpsightLogger$Level[Level.VERBOSE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$upsight$android$logger$UpsightLogger$Level[Level.DEBUG.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$upsight$android$logger$UpsightLogger$Level[Level.INFO.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$upsight$android$logger$UpsightLogger$Level[Level.WARN.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$upsight$android$logger$UpsightLogger$Level[Level.ERROR.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public ContextModule(Context context) {
        this.mApplicationContext = context.getApplicationContext();
    }

    @Singleton
    @Provides
    Context provideApplicationContext() {
        return this.mApplicationContext;
    }

    @Singleton
    @Provides
    Bus provideBus() {
        return new Bus(ThreadEnforcer.ANY);
    }

    @Singleton
    @Provides
    LogWriter provideLogWriter() {
        return new C09021();
    }

    @Singleton
    @Provides
    StorableIdFactory provideTypeIdGenerator() {
        return new C09032();
    }
}

package com.nianticlabs.nia.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.media.TransportMediator;
import android.view.Display;
import android.view.WindowManager;
import com.google.android.gms.location.places.Place;
import com.nianticlabs.nia.contextservice.ContextService;
import com.nianticlabs.nia.contextservice.ServiceStatus;
import com.upsight.android.googlepushservices.UpsightPushNotificationBuilderFactory.Default;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import spacemadness.com.lunarconsole.C1518R;

public class NianticSensorManager extends ContextService implements SensorEventListener {
    private static final float ANGLE_CHANGE_THRESHOLD_DEGREES = 1.0f;
    private static final int DECLINATION_UPDATE_INTERVAL_MSEC = 600000;
    private static final boolean ENABLE_VERBOSE_LOGS = false;
    private static final int MAX_SENSOR_UPDATE_DIFF_MSEC = 5000;
    private static final int MIN_SENSOR_UPDATE_INTERVAL_MSEC = 50;
    private static final float SINE_OF_45_DEGREES;
    private static final String TAG = "NianticSensorManager";
    private Sensor accelerometer;
    private float[] accelerometerData;
    private long accelerometerReadingMs;
    private float declination;
    private long declinationUpdateTimeMs;
    private final Display display;
    private Sensor gravity;
    private Sensor gyroscope;
    private float lastAzimuthUpdate;
    private float lastPitchUpdate;
    private long lastUpdateTimeMs;
    private Sensor linearAcceleration;
    private Sensor magnetic;
    private float[] magneticData;
    private long magnetometerReadingMs;
    private final AngleFilter orientationFilter;
    private Sensor rotation;
    private float[] rotationData;
    private final SensorManager sensorManager;
    private ServiceStatus status;
    private final float[] tmpMatrix1;
    private final float[] tmpMatrix2;
    private final float[] tmpMatrix3;
    private final float[] tmpOrientationAngles;

    static {
        SINE_OF_45_DEGREES = ((float) Math.sqrt(2.0d)) / Default.HTTP_REQUEST_BACKOFF_MULT;
    }

    public NianticSensorManager(Context context, long j) {
        super(context, j);
        this.tmpMatrix1 = new float[9];
        this.tmpMatrix2 = new float[9];
        this.tmpMatrix3 = new float[9];
        this.tmpOrientationAngles = new float[3];
        this.orientationFilter = new AngleFilter(true);
        this.status = ServiceStatus.UNDEFINED;
        this.accelerometerData = new float[3];
        this.magneticData = new float[3];
        this.rotationData = new float[5];
        this.display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        this.sensorManager = (SensorManager) context.getSystemService("sensor");
        this.gravity = this.sensorManager.getDefaultSensor(9);
        this.gyroscope = this.sensorManager.getDefaultSensor(4);
        this.accelerometer = this.sensorManager.getDefaultSensor(1);
        this.magnetic = this.sensorManager.getDefaultSensor(2);
        this.rotation = this.sensorManager.getDefaultSensor(11);
        this.linearAcceleration = this.sensorManager.getDefaultSensor(10);
    }

    private void calcMatrixFromRotationVector(float[] fArr, float[] fArr2) {
        float f = fArr[3];
        float f2 = fArr[0];
        float f3 = fArr[1];
        float f4 = fArr[2];
        float f5 = (Default.HTTP_REQUEST_BACKOFF_MULT * f2) * f2;
        float f6 = (Default.HTTP_REQUEST_BACKOFF_MULT * f3) * f3;
        float f7 = (Default.HTTP_REQUEST_BACKOFF_MULT * f4) * f4;
        float f8 = (Default.HTTP_REQUEST_BACKOFF_MULT * f2) * f3;
        float f9 = (Default.HTTP_REQUEST_BACKOFF_MULT * f4) * f;
        float f10 = (Default.HTTP_REQUEST_BACKOFF_MULT * f2) * f4;
        float f11 = (Default.HTTP_REQUEST_BACKOFF_MULT * f3) * f;
        f3 = (f3 * Default.HTTP_REQUEST_BACKOFF_MULT) * f4;
        f *= f2 * Default.HTTP_REQUEST_BACKOFF_MULT;
        fArr2[0] = (ANGLE_CHANGE_THRESHOLD_DEGREES - f6) - f7;
        fArr2[1] = f8 - f9;
        fArr2[2] = f10 + f11;
        fArr2[3] = f8 + f9;
        fArr2[4] = (ANGLE_CHANGE_THRESHOLD_DEGREES - f5) - f7;
        fArr2[5] = f3 - f;
        fArr2[6] = f10 - f11;
        fArr2[7] = f + f3;
        fArr2[8] = (ANGLE_CHANGE_THRESHOLD_DEGREES - f5) - f6;
    }

    private float computeRotationVectorW(float[] fArr) {
        float f = SINE_OF_45_DEGREES;
        for (float f2 : fArr) {
            f += f2 * f2;
        }
        return (float) Math.sqrt((double) (ANGLE_CHANGE_THRESHOLD_DEGREES - Math.min(f, ANGLE_CHANGE_THRESHOLD_DEGREES)));
    }

    private float getDeclination() {
        if (this.declinationUpdateTimeMs + MarketingContentStoreImpl.DEFAULT_TIME_TO_LIVE_MS > System.currentTimeMillis()) {
        }
        return this.declination;
    }

    private native void nativeCompassUpdate(long j, float f);

    private native void nativeSensorUpdate(int i, long j, float[] fArr);

    private void safeCompassUpdate(long j, float f) {
        synchronized (this.callbackLock) {
            nativeCompassUpdate(j, f);
        }
    }

    private void safeSensorUpdate(int i, long j, float[] fArr) {
        synchronized (this.callbackLock) {
            nativeSensorUpdate(i, j, fArr);
        }
    }

    private void startSensorManager() {
        if (this.gravity != null) {
            this.sensorManager.registerListener(this, this.gravity, 3, ContextService.getServiceHandler());
        }
        if (this.gyroscope != null) {
            this.sensorManager.registerListener(this, this.gyroscope, 3, ContextService.getServiceHandler());
        }
        if (this.accelerometer != null) {
            this.sensorManager.registerListener(this, this.accelerometer, 2, ContextService.getServiceHandler());
        }
        if (this.magnetic != null) {
            this.sensorManager.registerListener(this, this.magnetic, 2, ContextService.getServiceHandler());
        }
        if (this.rotation != null) {
            this.sensorManager.registerListener(this, this.rotation, 2, ContextService.getServiceHandler());
        }
        if (this.linearAcceleration != null) {
            this.sensorManager.registerListener(this, this.linearAcceleration, 3, ContextService.getServiceHandler());
        }
        this.status = ServiceStatus.INITIALIZED;
    }

    private void stopSensorManager() {
        this.sensorManager.unregisterListener(this);
        this.status = ServiceStatus.STOPPED;
    }

    private boolean updateOrientation(long j, float[] fArr) {
        int i = 129;
        int i2 = 2;
        switch (this.display.getRotation()) {
            case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                i = 2;
                i2 = 129;
                break;
            case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
                i2 = TransportMediator.KEYCODE_MEDIA_RECORD;
                break;
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                i = TransportMediator.KEYCODE_MEDIA_RECORD;
                i2 = 1;
                break;
            default:
                i = 1;
                break;
        }
        float[] fArr2 = this.tmpOrientationAngles;
        if (SensorManager.remapCoordinateSystem(fArr, i, i2, this.tmpMatrix2)) {
            float toDegrees;
            if (this.tmpMatrix2[7] <= SINE_OF_45_DEGREES) {
                SensorManager.getOrientation(this.tmpMatrix2, fArr2);
                toDegrees = (float) Math.toDegrees((double) fArr2[1]);
            } else if (SensorManager.remapCoordinateSystem(this.tmpMatrix2, 1, 3, this.tmpMatrix3)) {
                SensorManager.getOrientation(this.tmpMatrix3, fArr2);
                toDegrees = ((float) Math.toDegrees((double) fArr2[1])) - 90.0f;
            }
            float filter = this.orientationFilter.filter(j, MathUtil.wrapAngle(fArr2[0] + (MathUtil.DEGREES_TO_RADIANS * getDeclination())) * MathUtil.RADIANS_TO_DEGREES);
            if (Math.abs(filter - this.lastAzimuthUpdate) >= ANGLE_CHANGE_THRESHOLD_DEGREES || Math.abs(toDegrees - this.lastPitchUpdate) >= ANGLE_CHANGE_THRESHOLD_DEGREES) {
                this.lastAzimuthUpdate = filter;
                this.lastPitchUpdate = toDegrees;
                this.lastUpdateTimeMs = j;
                return true;
            }
        }
        return ENABLE_VERBOSE_LOGS;
    }

    private boolean updateOrientationFromRaw(long j) {
        if (this.lastUpdateTimeMs + 50 <= j && Math.abs(this.accelerometerReadingMs - this.magnetometerReadingMs) <= 5000) {
            float[] fArr = this.tmpMatrix1;
            if (SensorManager.getRotationMatrix(fArr, null, this.accelerometerData, this.magneticData)) {
                return updateOrientation(j, fArr);
            }
        }
        return ENABLE_VERBOSE_LOGS;
    }

    private boolean updateOrientationFromRotation(long j) {
        if (this.lastUpdateTimeMs + 50 > j) {
            return ENABLE_VERBOSE_LOGS;
        }
        float[] fArr = this.tmpMatrix1;
        calcMatrixFromRotationVector(this.rotationData, fArr);
        return updateOrientation(j, fArr);
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void onPause() {
        stopSensorManager();
    }

    public void onResume() {
        startSensorManager();
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        this.status = ServiceStatus.RUNNING;
        long currentTimeMillis = System.currentTimeMillis();
        switch (sensorEvent.sensor.getType()) {
            case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                this.accelerometerReadingMs = currentTimeMillis;
                System.arraycopy(sensorEvent.values, 0, this.accelerometerData, 0, this.accelerometerData.length);
                if (updateOrientationFromRaw(currentTimeMillis)) {
                    safeCompassUpdate(this.lastUpdateTimeMs, this.lastAzimuthUpdate);
                    break;
                }
                break;
            case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
                this.magnetometerReadingMs = currentTimeMillis;
                System.arraycopy(sensorEvent.values, 0, this.magneticData, 0, this.magneticData.length);
                if (updateOrientationFromRaw(currentTimeMillis)) {
                    safeCompassUpdate(this.lastUpdateTimeMs, this.lastAzimuthUpdate);
                    break;
                }
                break;
            case Place.TYPE_BICYCLE_STORE /*11*/:
                System.arraycopy(sensorEvent.values, 0, this.rotationData, 0, Math.min(sensorEvent.values.length, this.rotationData.length));
                if (sensorEvent.values.length == 3) {
                    this.rotationData[3] = computeRotationVectorW(this.rotationData);
                }
                if (updateOrientationFromRotation(currentTimeMillis)) {
                    safeCompassUpdate(this.lastUpdateTimeMs, this.lastAzimuthUpdate);
                    break;
                }
                break;
        }
        safeSensorUpdate(sensorEvent.sensor.getType(), currentTimeMillis, sensorEvent.values);
    }

    public void onStart() {
    }

    public void onStop() {
    }
}

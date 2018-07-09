package org.myspecialway.android.arrow;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import org.myspecialway.android.R;
import org.myspecialway.android.SdkExample;

@SdkExample(description = R.string.example_arrow_on_camera)
public class ArrowActivity extends FragmentActivity {
    // Overlay
    ImageView iv;
    float currentAngle = 0;
    //
    Context context;

    //camera
    private SurfaceView preview = null;
    private SurfaceHolder previewHolder = null;
    private Camera camera = null;
    private boolean inPreview = false;
    //

    // sensors
    private SensorManager mSensorManager;
    SensorEventListener sensorEventListener;
    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];
    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        removeScreenHeaders();

        setContentView(R.layout.arrow_main);

        context = this;

        iv = findViewById(R.id.iv);

        initSensors();
        initCamera();
    }

    private void removeScreenHeaders() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initSensors() {
        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener = new SensorListener();
    }

    private void initCamera() {
        preview = (SurfaceView) findViewById(R.id.surface);

        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
                //rotate camera to portrait
                camera.setDisplayOrientation(90);
            } catch (Throwable t) {}
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            camera.startPreview();
            inPreview = true;
        }

        public void surfaceDestroyed(SurfaceHolder holder) { }
    };


    @Override
    protected void onResume() {
        super.onResume();
        // for the system's orientation sensor registered listeners
        registerSensors();
        camera = Camera.open();
    }

    private void registerSensors() {
        mSensorManager.registerListener(sensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(sensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
    }

    class SensorListener implements SensorEventListener{

        @Override
        public void onSensorChanged(SensorEvent event) {

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, mAccelerometerReading,
                        0, mAccelerometerReading.length);
            }
            else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, mMagnetometerReading,
                        0, mMagnetometerReading.length);
            }

            updateOrientationAngles();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) { }
    }

    // Compute the three orientation angles based on the most recent readings from
    // the device's accelerometer and magnetometer.
    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        mSensorManager.getRotationMatrix(mRotationMatrix, null,
                mAccelerometerReading, mMagnetometerReading);
        // "mRotationMatrix" now has up-to-date information.

        float[] orientation = mSensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
        // "mOrientationAngles" now has up-to-date information.

        float azimuth = orientation[0];
        float azimuthInDegrees = (float) (azimuth * (180 / 3.14));// result is in PI
        if(azimuth < 0){
            azimuthInDegrees = 360 + azimuthInDegrees;
        }
        Log.d("Peleg", "Orientation:" + azimuthInDegrees);

        updateArrow(azimuthInDegrees);
    }

    private void updateArrow(float azimuthInDegrees) {
        Animation animation = createRotationAnimation(currentAngle,azimuthInDegrees);
        iv.startAnimation(animation);
        currentAngle = azimuthInDegrees;
    }

    @Override
    protected void onPause() {
        if (inPreview) {
            camera.stopPreview();
        }

        camera.release();
        camera = null;
        inPreview = false;

        super.onPause();
        mSensorManager.unregisterListener(sensorEventListener);

    }

    @NonNull
    private Animation createRotationAnimation(float from, float to) {
        Animation animation = new RotateAnimation(from, to, iv.getPivotX(), iv.getPivotY());
        animation.setDuration(200); // rotate 12 rounds in 3 seconds
        animation.setInterpolator(context, android.R.interpolator.linear);
        animation.setFillAfter(true);
        return animation;
    }
}

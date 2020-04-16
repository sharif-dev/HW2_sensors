package edu.sharif.sharif_dev.sensors;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class ShakeService extends Service {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ShakeDetector shakeDetector;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int seekBarValue = intent.getIntExtra("seekValue", 2);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector();
        shakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                // TODO: 4/16/20
                Log.d("shook", "shake!!!!!!!!!!!!!!!!!!");
            }
        });
        shakeDetector.setShakeTreshold(seekBarValue);
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(shakeDetector);
    }

    @Override
    public void onCreate() {
    }
}

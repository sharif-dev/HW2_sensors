package edu.sharif.sharif_dev.sensors.sleep;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;


public class SleepService extends Service {
    private SleepDetector sleepDetector;
    private SensorManager sensorManager;
    private Sensor sensor;

    @Override
    public void onCreate() {
        sleepDetector = SleepDetector.getInstance();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SleepDetector sleepDetector = SleepDetector.getInstance();
        if (sensor != null) {
            sensorManager.registerListener(sleepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(SleepDetector.getInstance());
    }
}

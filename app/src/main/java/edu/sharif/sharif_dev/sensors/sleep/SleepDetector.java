package edu.sharif.sharif_dev.sensors.sleep;

import android.app.admin.DevicePolicyManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;


public class SleepDetector implements SensorEventListener {
    private int customDegree = 10;
    private static DevicePolicyManager devicePolicyManager;
    private static SleepDetector instance ;

    private SleepDetector(){

    }

    public static SleepDetector getInstance(){
        if(instance == null){
            instance = new SleepDetector();
        }
        return instance;
    }

    public void setCustomDegree(int customDegree) {
        this.customDegree = customDegree;
    }

    public static void setDevicePolicyManager(DevicePolicyManager deviceManager) {
        devicePolicyManager = deviceManager;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values.length == 3) {
            float[] gravity = event.values;
            double normalGravity = Math.sqrt(gravity[0] * gravity[0] + gravity[1] * gravity[1] + gravity[2] * gravity[2]);

            // Normalize the accelerometer vector
            // gravity[0] = (float) (gravity[0] / normalGravity);
            // gravity[1] = (float) (gravity[1] / normalGravity);
            gravity[2] = (float) (gravity[2] / normalGravity);

            int inclination = (int) Math.round(Math.toDegrees(Math.acos(gravity[2])));

            if (inclination < customDegree || inclination > 180 - customDegree) {
                Log.d("tag",customDegree+"");
                lockScreen();
            }
        }
    }

    private void lockScreen() {
        devicePolicyManager.lockNow();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

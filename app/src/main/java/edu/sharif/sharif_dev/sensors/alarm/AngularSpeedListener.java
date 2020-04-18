package edu.sharif.sharif_dev.sensors.alarm;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class AngularSpeedListener implements SensorEventListener {
    private static float[] SENSITIVITIES = {20F, 15F, 12F, 6F, 3F, 2F};
    private int sensitivity = 3;
    private OnSpeedLimitReachListener limitReachListener;

    public void setLimitReachListener(OnSpeedLimitReachListener limitReachListener) {
        this.limitReachListener = limitReachListener;
    }

    public void setSensitivity(int sensitivity) {
        this.sensitivity = sensitivity;
    }

    public interface OnSpeedLimitReachListener {
        void onSpeedLimitReach();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float axisZ = event.values[2];
        if (Math.abs(axisZ) > SENSITIVITIES[sensitivity]) {
            limitReachListener.onSpeedLimitReach();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

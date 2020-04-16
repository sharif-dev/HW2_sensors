package edu.sharif.sharif_dev.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;


public class ShakeActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ShakeDetector shakeDetector;
    private CustomHandler handler;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        handler = new CustomHandler(this);
        setContentView(R.layout.activity_shake);
        setSensor();
        setSwitch();
    }

    private void setSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer == null) {
            handler.sendIntMessage(R.string.ACCELEROMETER_NOT_FOUND);
            // no sensor
            return;
        }
        shakeDetector = new ShakeDetector();
        shakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                // TODO: 4/16/20
                System.out.println("shake!!!!!!!!!!!!!!!!!!");
            }
        });
        sensorManager.unregisterListener(shakeDetector);
    }


    private void startService() {
        // TODO: 4/16/20
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    private void setSwitch() {
        Switch swtch = findViewById(R.id.shake_enable);
        swtch.setChecked(false);
        swtch.setTextOff("disabled");
        swtch.setTextOn("enabled");
        swtch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // checked
                    startService();
                } else {
                    // unchecked
                    sensorManager.unregisterListener(shakeDetector);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        sensorManager.unregisterListener(shakeDetector);
        super.onPause();
    }
}

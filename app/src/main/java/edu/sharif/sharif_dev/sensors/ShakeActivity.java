package edu.sharif.sharif_dev.sensors;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;


public class ShakeActivity extends AppCompatActivity {


    private CustomHandler handler;
    private SeekBar seekBar;
    private int seekBarValue = 2;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        handler = new CustomHandler(this);
        setContentView(R.layout.activity_shake);
        setSwitch();
        seekBar = findViewById(R.id.seekBar);
        setSeekBar();
    }

    private void setSeekBar() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBarValue = i;
                System.out.println(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void startService() {
        // check sensor availability
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer == null) {
            handler.sendIntMessage(R.string.ACCELEROMETER_NOT_FOUND);
            return;
        }
        Intent intent = new Intent(getApplicationContext(), ShakeService.class);
        intent.putExtra("seekValue", seekBarValue);
        startService(intent);
    }

    private void setSwitch() {
        Switch swtch = findViewById(R.id.shake_enable);
        swtch.setChecked(isServiceRunning());
        swtch.setTextOff("disabled");
        swtch.setTextOn("enabled");
        swtch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // checked
                    startService();
                    seekBar.setVisibility(View.GONE);
                } else {
                    // unchecked
                    stopService(new Intent(getApplicationContext(), ShakeService.class));
                    seekBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ShakeService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

package edu.sharif.sharif_dev.sensors.shake;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import edu.sharif.sharif_dev.sensors.CustomHandler;
import edu.sharif.sharif_dev.sensors.R;


public class ShakeActivity extends AppCompatActivity {


    private CustomHandler handler;
    private SeekBar seekBar;
    private int seekBarValue = 3;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        handler = new CustomHandler(this);
        setContentView(R.layout.activity_shake);
        boolean serviceStatus = isServiceRunning();
        setSwitch(serviceStatus);
        setStates(serviceStatus);
        seekBar = findViewById(R.id.seekBar);
        if (serviceStatus)
            seekBar.setVisibility(View.GONE);
        setSeekBar();
    }

    private void setStates(boolean status) {
        TextView statusText = findViewById(R.id.service_states);
        statusText.setText(status ? getString(R.string.enabled) : getString(R.string.disabled));
    }

    private void setSeekBar() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBarValue = i;
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

    private void setSwitch(boolean serviceStatus) {
        Switch swtch = findViewById(R.id.shake_enable);
        swtch.setChecked(serviceStatus);
        swtch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // checked
                    startService();
                    setStates(true);
                    seekBar.setVisibility(View.GONE);
                } else {
                    // unchecked
                    stopService(new Intent(getApplicationContext(), ShakeService.class));
                    setStates(false);
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

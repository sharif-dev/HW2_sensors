package edu.sharif.sharif_dev.sensors;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSensorManager();

    }

    private void setSensorManager(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    private void startShakeActivity(){

    }

    private void startSleepActivity(){
        ImageView sleep_mode_icon = findViewById(R.id.sleep_mode_icon);
        sleep_mode_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SleepActivity.class);
                startActivity(intent);
            }
        });

    }

    private void startAlarmActivity(){

    }



}

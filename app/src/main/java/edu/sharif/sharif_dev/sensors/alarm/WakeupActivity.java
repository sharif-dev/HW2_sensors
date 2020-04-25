package edu.sharif.sharif_dev.sensors.alarm;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import edu.sharif.sharif_dev.sensors.R;

public class WakeupActivity extends AppCompatActivity {
    MediaPlayer mp;
    Vibrator vibrator;
    int sensitivity = 5;
    SensorManager sensorManager;
    AngularSpeedListener speedListener;
    Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wakeup);
        setVibrate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                audioPlayer();
            }
        }).start();
        startActivityTimer();
        sensitivity = getIntent().getIntExtra("sensitivity", -1);
        setSensor();
    }

    private void setSensor() {
        sensorManager = ((SensorManager) getSystemService(Context.SENSOR_SERVICE));
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        speedListener = new AngularSpeedListener();
        speedListener.setSensitivity(sensitivity);
        speedListener.setLimitReachListener(new AngularSpeedListener.OnSpeedLimitReachListener() {
            @Override
            public void onSpeedLimitReach() {
                finish();
            }
        });
        sensorManager.registerListener(speedListener, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    private void startActivityTimer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 60 * 10);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setVibrate() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 400, 200, 400};
        vibrator.vibrate(pattern, 0);
    }

    private void audioPlayer() {
        try {
            mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm_sound);
            mp.setLooping(true);
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopAudio() {
        if (mp != null)
            mp.stop();
    }


    @Override
    protected void onDestroy() {
        Log.d("tag", "onDestroy");
        stopAudio();
        vibrator.cancel();
        sensorManager.unregisterListener(speedListener);
        super.onDestroy();
    }
}

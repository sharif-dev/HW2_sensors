package edu.sharif.sharif_dev.sensors.alarm;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

import edu.sharif.sharif_dev.sensors.R;

public class WakeupActivity extends AppCompatActivity {
    MediaPlayer mp;
    Vibrator vibrator;

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
    }

    private void startActivityTimer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setVibrate() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(60 * 10 * 1000);
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
        super.onDestroy();

    }
}

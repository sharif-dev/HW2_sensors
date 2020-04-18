package edu.sharif.sharif_dev.sensors.alarm;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

import edu.sharif.sharif_dev.sensors.R;

public class WakeupActivity extends AppCompatActivity {
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wakeup);

        new Thread(new Runnable() {
            @Override
            public void run() {
                audioPlayer();
            }
        }).start();

    }

    private void audioPlayer(){
        try {
            mp = MediaPlayer.create(getApplicationContext(),R.raw.alarm_sound);
            mp.setLooping(true);
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void stopAudio(){
        if(mp != null)
            mp.stop();
    }


    @Override
    protected void onDestroy() {
        Log.d("tag","onDestroy");
        stopAudio();
        super.onDestroy();

    }
}

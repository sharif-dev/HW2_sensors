package edu.sharif.sharif_dev.sensors;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class SleepActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        setSensorManager();

        final Switch turn_switch = findViewById(R.id.switch_sleep);
        turn_switch.setText(R.string.off);
        turn_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    onSwitch(turn_switch);
                else
                    offSwitch(turn_switch);
            }
        });

    }


    private void setSensorManager(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    private void onSwitch(Switch turn_switch){
        turn_switch.setText(R.string.on);
        setEditTextAnim(R.animator.fade_in);

    }
    private void offSwitch(Switch turn_switch){
        turn_switch.setText(R.string.off);
        setEditTextAnim(R.animator.fade_out);
    }

    private void setEditTextAnim(int animRes){
        EditText degree_inp = findViewById(R.id.degree_inp);

        AnimatorSet animationSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, animRes);
        animationSet.setTarget(degree_inp);
        animationSet.start();
    }

}

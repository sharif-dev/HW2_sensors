package edu.sharif.sharif_dev.sensors;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class SleepActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private int customDegree = 10;
    private CustomHandler customHandler ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        customHandler = new CustomHandler(getApplicationContext());

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

        setCustomDegree();

    }


    private void setSensorManager(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    private void onSwitch(Switch turn_switch){
        turn_switch.setText(R.string.on);
        setEditTextAnim(R.animator.fade_in);

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if( sensor == null)
            showError(R.string.ACCELEROMETER_NOT_FOUND);
        else
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void setCustomDegree(){
         EditText degree_inp  = findViewById(R.id.degree_inp);
         if(degree_inp.getText() != null){
             try {
                 customDegree = Integer.parseInt(degree_inp.getText().toString());
             }catch (Exception e){
                 showError(R.string.false_degree);
             }
         }
    }

    private void offSwitch(Switch turn_switch){
        turn_switch.setText(R.string.off);
        setEditTextAnim(R.animator.fade_out);
        sensor = null;
    }

    private void setEditTextAnim(int animRes){
        EditText degree_inp = findViewById(R.id.degree_inp);

        AnimatorSet animationSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, animRes);
        animationSet.setTarget(degree_inp);
        animationSet.start();
    }

    //Todo use gravity sensor too
    @Override
    public void onSensorChanged(SensorEvent event) {
//        if(event.sensor == Sensor.STRING_TYPE_ACCELEROMETER)
        if(event.values.length ==3 ) {
            float[] gravity = event.values;

            double normalGravity = Math.sqrt(gravity[0] * gravity[0] + gravity[1] * gravity[1] + gravity[2] * gravity[2]);

// Normalize the accelerometer vector
            gravity[0] = (float) (gravity[0] / normalGravity);
            gravity[1] = (float) (gravity[1] / normalGravity);
            gravity[2] = (float) (gravity[2] / normalGravity);

            int inclination = (int) Math.round(Math.toDegrees(Math.acos(gravity[2])));

            if(inclination < customDegree || inclination > 180 - customDegree ){
                Log.d("tag","workinggg");
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
          //TODO
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensor != null)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void showError(int string){
        customHandler.sendIntMessage(string);
    }
}

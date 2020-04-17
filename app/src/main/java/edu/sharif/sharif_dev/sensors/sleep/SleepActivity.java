package edu.sharif.sharif_dev.sensors.sleep;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import edu.sharif.sharif_dev.sensors.CustomHandler;
import edu.sharif.sharif_dev.sensors.R;

public class SleepActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor sensor;
    private CustomHandler customHandler;
   // private final int RESULT_ENABLE = 1;
    private static final String PREFERENCE_FILE = "sleep_preference";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        customHandler = new CustomHandler(getApplicationContext());

        setSensorManager();

        final Switch turn_switch = findViewById(R.id.switch_sleep);
        turn_switch.setText(R.string.off);
        setLastDataSwitch(turn_switch);

        turn_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    onSwitch(turn_switch);
                else
                    offSwitch(turn_switch);
                setNewDataSwitch(isChecked);
            }
        });

        setCustomDegree();

    }

    private void setNewDataSwitch(boolean isChecked){
        SharedPreferences settings = getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("SWITCH_KEY", isChecked);
        editor.apply();
    }

    private void setLastDataSwitch(Switch turn_switch){
        SharedPreferences settings = getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        boolean switch_key = settings.getBoolean("SWITCH_KEY", false);
        turn_switch.setChecked(switch_key);
        if(switch_key){
            turn_switch.setText(R.string.on);
            setEditTextAnim(R.animator.fade_in);
        }
    }

    private void setSensorManager() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    private void onSwitch(Switch turn_switch) {
        turn_switch.setText(R.string.on);
        setEditTextAnim(R.animator.fade_in);

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor == null)
            showError(R.string.ACCELEROMETER_NOT_FOUND);
        else
            startSleepService();
    }


    private void setCustomDegree() {
        final EditText degree_inp = findViewById(R.id.degree_inp);
        degree_inp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    Editable text = degree_inp.getText();
                    if (text != null && !text.toString().equals("")) {
                        try {
                            int customDegree = Integer.parseInt(degree_inp.getText().toString());
                            SleepDetector.getInstance().setCustomDegree(customDegree);
                        } catch (Exception e) {
                            showError(R.string.false_degree);
                        }
                    }
                }
                return handled;
            }
        });


    }

    private void offSwitch(Switch turn_switch) {
        turn_switch.setText(R.string.off);
        setEditTextAnim(R.animator.fade_out);
        stopSleepService();
        sensor = null;
    }

    private void startSleepService(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), SleepService.class);
                startService(intent);
            }
        }).start();

    }
    private void stopSleepService(){
        stopService(new Intent(getApplicationContext(),SleepService.class));
    }

    private void setEditTextAnim(int animRes) {
        EditText degree_inp = findViewById(R.id.degree_inp);

        AnimatorSet animationSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, animRes);
        animationSet.setTarget(degree_inp);
        animationSet.start();
    }

    private void showError(int string) {
        customHandler.sendIntMessage(string);
    }

}

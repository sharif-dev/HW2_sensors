package edu.sharif.sharif_dev.sensors;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import edu.sharif.sharif_dev.sensors.shake.ShakeActivity;
import edu.sharif.sharif_dev.sensors.sleep.Admin;
import edu.sharif.sharif_dev.sensors.sleep.SleepActivity;
import edu.sharif.sharif_dev.sensors.sleep.SleepDetector;

public class MainActivity extends AppCompatActivity {
    private int RESULT_ENABLE = 1;
    private CustomHandler customHandler ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customHandler = new CustomHandler(getApplicationContext());
        setAlarmActivity();
        setSleepActivity();
        setShakeActivity();
    }

    private void setShakeActivity() {
        ImageView shakeIcon = findViewById(R.id.shake_icon);
        shakeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShakeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setSleepActivity() {
        ImageView sleep_mode_icon = findViewById(R.id.sleep_mode_icon);
        sleep_mode_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DevicePolicyManager deviceManger = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
                ComponentName compName = new ComponentName(getApplicationContext(), Admin.class);

                // to set manager for sleep listener
                SleepDetector.setDevicePolicyManager(deviceManger);

                boolean active = deviceManger.isAdminActive(compName);
                if (active) {
                    startSleepActivity();
                } else {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable the app!");
                    startActivityForResult(intent, RESULT_ENABLE);
                }
            }
        });

    }

    private void startSleepActivity(){
        // always device policy is on here
        Intent intent = new Intent(MainActivity.this, SleepActivity.class);
        startActivity(intent);
    }

    private void setAlarmActivity() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_ENABLE && resultCode == Activity.RESULT_OK) {
            startSleepActivity();
        } else
            showError(R.string.fail);
    }

    private void showError(int string) {
        customHandler.sendIntMessage(string);
    }


}

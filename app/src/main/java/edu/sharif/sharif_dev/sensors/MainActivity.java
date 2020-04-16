package edu.sharif.sharif_dev.sensors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                Intent intent = new Intent(MainActivity.this, SleepActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setAlarmActivity() {

    }


}

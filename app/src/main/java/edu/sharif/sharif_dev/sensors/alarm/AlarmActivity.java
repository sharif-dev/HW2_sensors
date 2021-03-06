package edu.sharif.sharif_dev.sensors.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Objects;

import edu.sharif.sharif_dev.sensors.CustomHandler;
import edu.sharif.sharif_dev.sensors.R;

public class AlarmActivity extends AppCompatActivity {
    private PendingIntent alarmIntentPending;
    private CustomHandler customHandler;
    private int sensitivity = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.alarm);
        setContentView(R.layout.activity_alarm);
        customHandler = new CustomHandler(getApplicationContext());

        setTimeEditText();

        Button alarm_btn = findViewById(R.id.alarm_btn);
        alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });

        Button cancel_btn = findViewById(R.id.cancel_alarm_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offAlarm();
            }
        });

        setSeekBar();
        initializeTime();
    }

    private void initializeTime(){
        EditText time = findViewById(R.id.time_edit_text);
        time.setText("11:11");
    }


    private void setSeekBar() {
        SeekBar seekBar = findViewById(R.id.seekBar2);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                sensitivity = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setTimeEditText() {
        final EditText time_editText = findViewById(R.id.time_edit_text);
        time_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AlarmActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time_editText.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                Objects.requireNonNull(timePickerDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.setTitle("Select Alarm Time");

                timePickerDialog.show();
            }
        });
    }

    private void setAlarm() {
        //offAlarm();

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        EditText time_edit = findViewById(R.id.time_edit_text);
        Editable time_edit_text = time_edit.getText();
        if (time_edit_text != null) {
            String[] time_slices = time_edit.getText().toString().split(":");

            // set time
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time_slices[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(time_slices[1]));

            // set intents
            Intent mAlarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
            mAlarmIntent.putExtra("sensitivity", sensitivity);
            alarmIntentPending = PendingIntent.getBroadcast(getApplicationContext(), 0,
                    mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntentPending);
            showMessage(R.string.alarm_set);
        } else {
            showMessage(R.string.time_not_correct);
        }
    }

    private void offAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmIntentPending != null) {
            alarmManager.cancel(alarmIntentPending);
            showMessage(R.string.alarm_canceled);
        }
    }

    private void showMessage(int string) {
        customHandler.sendIntMessage(string);
    }
}

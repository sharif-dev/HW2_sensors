package edu.sharif.sharif_dev.sensors.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent wake_up_intent = new Intent(context, WakeupActivity.class);
        wake_up_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        wake_up_intent.putExtra("sensitivity", intent.getIntExtra("sensitivity", 5));
        context.startActivity(wake_up_intent);
    }
}

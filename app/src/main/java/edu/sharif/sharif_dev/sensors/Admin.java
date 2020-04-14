package edu.sharif.sharif_dev.sensors;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

public class Admin extends DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
    }
}

package edu.sharif.sharif_dev.sensors;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

public class CustomHandler extends Handler {
    Context context;

    public CustomHandler(Context context) {
        this.context = context;
    }

    public void sendIntMessage(int number) {
        Message message = new Message();
        message.what = number;
        message.arg1 = 1;
        handleMessage(message);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case R.string.ACCELEROMETER_NOT_FOUND:
                makeToast(R.string.ACCELEROMETER_NOT_FOUND);
                return;
            case R.string.false_degree:
                makeToast(R.string.false_degree);
                return;
            case R.string.fail:
                makeToast(R.string.fail);

        }

    }

    private void makeToast(int stringCode) {
        Toast toast = Toast.makeText(context, stringCode, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
        toast.show();
    }
}
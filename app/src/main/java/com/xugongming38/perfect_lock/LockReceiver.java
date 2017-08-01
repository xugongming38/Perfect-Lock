package com.xugongming38.perfect_lock;

/**
 * Created by dell on 2017/6/7.
 */

import android.app.admin.DeviceAdminReceiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class LockReceiver extends DeviceAdminReceiver {
    public LockReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"on receive!",Toast.LENGTH_SHORT).show();
        super.onReceive(context,intent);
        // TODO: This method is called when the BroadcastReceiver is receiving
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        Toast.makeText(context,"已经激活!",Toast.LENGTH_SHORT).show();
        super.onEnabled(context, intent);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        Toast.makeText(context,"取消激活!",Toast.LENGTH_SHORT).show();
        super.onDisabled(context, intent);
    }
}

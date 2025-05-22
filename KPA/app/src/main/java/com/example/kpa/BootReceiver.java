package com.example.kpa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//Pending Intent Receiver
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            WorkScheduler.scheduleWorkReminder(context);
        }
    }
}

package com.example.adlawanpowerreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CustomReceiver extends BroadcastReceiver {
    public static final String CUSTOM_BROADCAST_ACTION =
            "com.example.adlawanpowerreceiver.ACTION_CUSTOM_BROADCAST";
    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();

        if (intentAction != null) {
            String toastMessage = "Unknown broadcast received";

            switch (intentAction) {
                case Intent.ACTION_POWER_CONNECTED:
                    toastMessage = "Power connected!";
                    break;

                case Intent.ACTION_POWER_DISCONNECTED:
                    toastMessage = "Power disconnected!";
                    break;
                case CUSTOM_BROADCAST_ACTION:
                    toastMessage = "Custom Broadcast Received!";
                    break;
            }

            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
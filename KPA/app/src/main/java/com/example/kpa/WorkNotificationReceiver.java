package com.example.kpa;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.widget.Toast;

public class WorkNotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationTest";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Broadcast received, attempting to show notification.");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Notification permission is not granted", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String channelId = "work_channel";
        createNotificationChannel(context, channelId);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.baseline_alarm_24)
                .setContentTitle("Sipag Lang")
                .setContentText("Reminder: Your work starts in 1 hour!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(null)
                .setVibrate(new long[] { 0, 500, 1000 })
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE);


        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);


        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(1, builder.build());
    }

    private void createNotificationChannel(Context context, String channelId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Work Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifies you about upcoming work schedules");

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}

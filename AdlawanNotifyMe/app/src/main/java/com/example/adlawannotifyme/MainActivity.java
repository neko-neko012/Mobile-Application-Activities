package com.example.adlawannotifyme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;


public class MainActivity extends AppCompatActivity {

    public static final String ACTION_UPDATE_NOTIFICATION =
            "com.example.adlawannotifyme.ACTION_UPDATE_NOTIFICATION";

    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final int NOTIFICATION_ID = 0;

    private NotificationManager mNotifyManager;

    private Button notifyButton;
    private Button updateButton;
    private Button cancelButton;

    private NotificationReceiver mReceiver = new NotificationReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notifyButton = findViewById(R.id.notify_button);
        updateButton = findViewById(R.id.update_button);
        cancelButton = findViewById(R.id.cancel_button);

        notifyButton.setOnClickListener(view -> sendNotification());
        updateButton.setOnClickListener(view -> updateNotification());
        cancelButton.setOnClickListener(view -> cancelNotification());

        createNotificationChannel();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        registerReceiver(mReceiver, new IntentFilter(ACTION_UPDATE_NOTIFICATION));

        setNotificationButtonState(true, false, false);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private void setNotificationButtonState(boolean isNotifyEnabled,
                                            boolean isUpdateEnabled,
                                            boolean isCancelEnabled) {
        notifyButton.setEnabled(isNotifyEnabled);
        updateButton.setEnabled(isUpdateEnabled);
        cancelButton.setEnabled(isCancelEnabled);
    }

    private void sendNotification() {
        // Create an Intent to handle the notification action
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(
                this, NOTIFICATION_ID, updateIntent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        // Load the mascot image as a Bitmap from resources
        Bitmap mascotBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mascot_1);

        // Create a basic notification builder
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder()
                .setLargeIcon(mascotBitmap)  // Set large icon
                .addAction(R.drawable.ic_update, "Update Notification", updatePendingIntent);

        // Send the notification with the large icon
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());

        // Update the button states for the UI
        setNotificationButtonState(false, true, true);
    }


    private void updateNotification() {

        Bitmap mascotBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_update);


        NotificationCompat.Builder notifyBuilder = getNotificationBuilder()
                .setLargeIcon(mascotBitmap)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("This is the updated notification with more details. Yay!")
                        .setBigContentTitle("Notification Updated")
                        .setSummaryText("Summary text"));


        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());

        setNotificationButtonState(false, false, true);
    }

    private void cancelNotification() {
        mNotifyManager.cancel(NOTIFICATION_ID);
        setNotificationButtonState(true, false, false);
    }

    public static void sendNotificationUpdate(Context context) {
        NotificationManager notifyManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Load your image
        Bitmap mascotBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mascot_1);

        // Create the updated notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.mascot_1)
                .setLargeIcon(mascotBitmap)
                .setContentTitle("Notification Updated via Broadcast")
                .setContentText("This update came from the broadcast receiver.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Broadcast receiver triggered this update with a lot more text!"));

        // Send the notification
        notifyManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    PRIMARY_CHANNEL_ID,
                    "Mascot Notification",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notifies when button is tapped");

            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(
                this,
                NOTIFICATION_ID,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        return new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("You've been notified!")
                .setContentText("This is your notification text.")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
    }

    public class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateNotification();
        }
    }
}

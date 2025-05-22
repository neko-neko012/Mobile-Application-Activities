package com.example.kpa;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class OptionsActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private TextView tvSelectedDate, tvWorkTime;
    private Switch switchNotifications;
    private Button btnSave, btnCancelWork;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "WorkPrefs";
    private static final int NOTIFICATION_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        calendarView = findViewById(R.id.calendarView);
        tvSelectedDate = findViewById(R.id.tv_selected_date);
        tvWorkTime = findViewById(R.id.tv_work_time);
        switchNotifications = findViewById(R.id.switch_notifications);
        btnSave = findViewById(R.id.btn_save);
        btnCancelWork = findViewById(R.id.btn_cancel_work);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        loadSavedSettings();

        tvWorkTime.setOnClickListener(v -> showTimePickerDialog());

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);

            String nextWorkDate = selectedDate.get(Calendar.DAY_OF_MONTH) + " " +
                    getMonthName(selectedDate.get(Calendar.MONTH)) + " " +
                    selectedDate.get(Calendar.YEAR);

            new AlertDialog.Builder(this)
                    .setTitle("Confirm Workday")
                    .setMessage("Will you work on this day?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        tvSelectedDate.setText("You have work on: " + nextWorkDate);
                        tvSelectedDate.setTextColor(ContextCompat.getColor(this, android.R.color.black));

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("next_work_date", nextWorkDate);
                        editor.apply();

                        scheduleNotification(selectedDate);
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        tvSelectedDate.setText("Not working on: " + nextWorkDate);
                        tvSelectedDate.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));

                        cancelNotification();
                    })
                    .show();
        });
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Request notification permission if it's not already granted
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
                    } else {
                        Toast.makeText(this, "Notification permission already granted", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                // If the switch is off, save the preference and cancel any scheduled notifications
                saveSettings();
                cancelNotification();
            }
        });


        btnCancelWork.setOnClickListener(v -> cancelWork());

        btnSave.setOnClickListener(v -> saveSettings());

    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(this,
                (view, selectedHour, selectedMinute) -> {
                    String formattedTime = formatTime(selectedHour, selectedMinute);
                    tvWorkTime.setText(formattedTime);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("work_time", formattedTime);
                    editor.apply();

                    Toast.makeText(this, "Work time updated to " + formattedTime, Toast.LENGTH_SHORT).show();
                },
                hour, minute, false).show();
    }

    private String formatTime(int hour, int minute) {
        String amPm = (hour >= 12) ? "PM" : "AM";
        int displayHour = (hour > 12) ? hour - 12 : (hour == 0 ? 12 : hour);
        return String.format("%02d:%02d %s", displayHour, minute, amPm);
    }

    private int getMonthIndex(String monthName) {
        String[] months = new DateFormatSymbols().getMonths();
        for (int i = 0; i < months.length; i++) {
            if (months[i].equalsIgnoreCase(monthName)) {
                return i;
            }
        }
        return -1;
    }

    private void cancelWork() {
        new AlertDialog.Builder(this)
                .setTitle("Cancel Work?")
                .setMessage("Are you sure you want to cancel your scheduled work?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("next_work_date");
                    editor.apply();

                    tvSelectedDate.setText("Work canceled");
                    tvSelectedDate.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
                    btnCancelWork.setVisibility(View.GONE);

                    cancelNotification();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void loadSavedSettings() {
        String nextWorkDate = sharedPreferences.getString("next_work_date", null);
        if (nextWorkDate != null) {
            tvSelectedDate.setText("You have work on: " + nextWorkDate);
            btnCancelWork.setVisibility(View.VISIBLE);
        } else {
            tvSelectedDate.setText("No work scheduled");
            btnCancelWork.setVisibility(View.GONE);
        }
        tvWorkTime.setText(sharedPreferences.getString("work_time", "8:00 AM"));
        switchNotifications.setChecked(sharedPreferences.getBoolean("notifications", false));
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("notifications", switchNotifications.isChecked());
        editor.apply();

        if (switchNotifications.isChecked()) {

            scheduleNotification(Calendar.getInstance()); // Or the selected date
        } else {

            cancelNotification();
        }
    }


    @SuppressLint("ScheduleExactAlarm")
    private void scheduleNotification(Calendar selectedDate) {
        if (!switchNotifications.isChecked()) return;

        // Get saved work time
        String savedWorkTime = sharedPreferences.getString("work_time", "8:00 AM");
        String[] timeParts = savedWorkTime.split(" ");
        String[] hourMin = timeParts[0].split(":");

        int hour = Integer.parseInt(hourMin[0]);
        int minute = Integer.parseInt(hourMin[1]);

        if (timeParts[1].equals("PM") && hour != 12) {
            hour += 12;
        } else if (timeParts[1].equals("AM") && hour == 12) {
            hour = 0;
        }

        Calendar notificationTime = (Calendar) selectedDate.clone();
        notificationTime.set(Calendar.HOUR_OF_DAY, hour);
        notificationTime.set(Calendar.MINUTE, minute);
        notificationTime.set(Calendar.SECOND, 0);

        // Subtract one hour for the 1 hour prior notification
        notificationTime.add(Calendar.HOUR_OF_DAY, -1);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, WorkNotificationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    notificationTime.getTimeInMillis(),
                    pendingIntent
            );

            Toast.makeText(this, "Work notification set for " + notificationTime.getTime(), Toast.LENGTH_LONG).show();
        }
    }


    private void cancelNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, WorkNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(this, "Work notification canceled", Toast.LENGTH_SHORT).show();
        }
    }

    private String getMonthName(int monthIndex) {
        String[] months = new DateFormatSymbols().getMonths();
        return months[monthIndex];
    }

    // Handle permission result (notification permission)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
                // Enable notifications and schedule them if the switch is on
                if (switchNotifications.isChecked()) {
                    scheduleNotification(Calendar.getInstance()); // or the selected date
                }
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
                switchNotifications.setChecked(false); // Turn off the switch if permission is denied
            }
        }
    }

}

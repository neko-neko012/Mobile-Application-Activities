package com.example.adlawanpowerreceiver;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private CustomReceiver mReceiver;

    // Define the custom broadcast action constant
    public static final String CUSTOM_BROADCAST_ACTION =
            "com.example.adlawanpowerreceiver.ACTION_CUSTOM_BROADCAST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Button button = findViewById(R.id.button_send_broadcast);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCustomBroadcast(view);
            }
        });

        mReceiver = new CustomReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(mReceiver, filter);


        IntentFilter customFilter = new IntentFilter(CUSTOM_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, customFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    public void sendCustomBroadcast(View view) {
        Intent customBroadcastIntent = new Intent(CUSTOM_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(customBroadcastIntent);
    }
}

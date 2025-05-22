package com.example.kpa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WorkOptionsActivity extends AppCompatActivity {

    Button btnCreateWork, btnJoinWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_options);

        btnCreateWork = findViewById(R.id.btnCreateWork);
        btnJoinWork = findViewById(R.id.btnJoinWork);

        btnCreateWork.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class); // you'll create this
            startActivity(intent);
        });

        btnJoinWork.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class); // you'll create this
            startActivity(intent);
        });
        String firstName = getIntent().getStringExtra("firstName");
        TextView tvGreeting = findViewById(R.id.tvGreeting);
        tvGreeting.setText("Hello, " + firstName + "!");

    }
}

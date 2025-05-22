package com.example.adlawananimationsample;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button buttonGoToSecondActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonGoToSecondActivity = findViewById(R.id.buttonGoToSecondActivity);
    }


    public void onGoToSecondActivity(View view) {

        View sharedImage = findViewById(R.id.sharedImage);


        Intent intent = new Intent(MainActivity.this, SecondActivity.class);


        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedImage, "imageTransition");

        startActivity(intent, options.toBundle());
    }
}
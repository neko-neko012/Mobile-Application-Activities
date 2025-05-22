package com.example.adlawanhellosharedpreferences;

import android.content.SharedPreferences;
import android.graphics.Color;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    private static final String COUNT_KEY = "count";
    private static final String COLOR_KEY = "color";

    private int mCount;
    private int mColor;
    private TextView mShowCountTextView;
    private View mainLayout;

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.sharedpref";

    private final int defaultColor = Color.GRAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_landscape);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Set the toolbar as the ActionBar
        setSupportActionBar(toolbar);

        // Optionally, set the title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Hello SharedPreferences");

            mShowCountTextView = findViewById(R.id.textView_count);
            mainLayout = findViewById(R.id.textView_count); // Corrected to main layout

            mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

            mCount = mPreferences.getInt(COUNT_KEY, 0);
            mColor = mPreferences.getInt(COLOR_KEY, defaultColor);

            mShowCountTextView.setText(String.format("%s", mCount));
            mShowCountTextView.setBackgroundColor(mColor);
            mainLayout.setBackgroundColor(mColor);
        }

    }

    public void changeBackground(View v) {
        int color = Color.GRAY;

        if (v.getId() == R.id.button_black) {
            color = getResources().getColor(R.color.color_black);
        } else if (v.getId() == R.id.button_red) {
            color = getResources().getColor(R.color.color_red);
        } else if (v.getId() == R.id.button_blue) {
            color = getResources().getColor(R.color.color_blue);
        } else if (v.getId() == R.id.button_green) {
            color = getResources().getColor(R.color.color_green);
        }

        mColor = color;
        mShowCountTextView.setBackgroundColor(color);
        findViewById(R.id.textView_count).setBackgroundColor(color);
    }

    public void countUp(View view) {
        mCount++;
        mShowCountTextView.setText(String.format("%s", mCount));
    }

    public void reset(View view) {
        mCount = 0;
        mColor = defaultColor;

        mShowCountTextView.setText(String.format("%s", mCount));
        mShowCountTextView.setBackgroundColor(mColor);
        mainLayout.setBackgroundColor(mColor);

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();


        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putInt(COUNT_KEY, mCount);
        preferencesEditor.putInt(COLOR_KEY, mColor);
        preferencesEditor.apply();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(COUNT_KEY, mCount);
        outState.putInt(COLOR_KEY, mColor);
    }
}

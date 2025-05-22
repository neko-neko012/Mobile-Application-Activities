package com.example.adlawansimpleasynchtask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private static final String TEXT_STATE = "currentText";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.textView1);

        // Restore state if available
        if (savedInstanceState != null) {
            mTextView.setText(savedInstanceState.getString(TEXT_STATE));
        }
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Set the Toolbar as the ActionBar
        setSupportActionBar(toolbar);
    }

    public void startTask(View view) {
        mTextView.setText(R.string.napping);
        new SimpleAsynchTask(mTextView).execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the TextView state
        outState.putString(TEXT_STATE, mTextView.getText().toString());
    }
}

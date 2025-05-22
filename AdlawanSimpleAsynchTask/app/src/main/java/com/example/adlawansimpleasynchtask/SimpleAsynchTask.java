package com.example.adlawansimpleasynchtask;

import android.os.AsyncTask;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Random;


public class SimpleAsynchTask extends AsyncTask<Void, Void, String> {

    private WeakReference<TextView> mTextView;

    public SimpleAsynchTask(TextView textView) {
        mTextView = new WeakReference<>(textView);
    }

    @Override
    protected String doInBackground(Void... voids) {
        Random r = new Random();
        int n = r.nextInt(11); // 0 to 10
        int s = n * 200; // Sleep duration

        try {
            Thread.sleep(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "Awake at last after sleeping for " + s + " milliseconds!";
    }

    @Override
    protected void onPostExecute(String result) {
        TextView textView = mTextView.get();
        if (textView != null) {
            textView.setText(result);
        }
    }
}

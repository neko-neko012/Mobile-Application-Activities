package com.example.adlawananimationsample;

import android.os.Bundle;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private ImageView secondActivityImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        secondActivityImage = findViewById(R.id.secondActivityImage);
        applyShakeEffect();
    }

    private void applyShakeEffect() {
        ObjectAnimator shakeLeft = ObjectAnimator.ofFloat(secondActivityImage, "translationX", -25f);
        ObjectAnimator shakeRight = ObjectAnimator.ofFloat(secondActivityImage, "translationX", 25f);


        shakeLeft.setDuration(100);
        shakeRight.setDuration(100);

        shakeLeft.setRepeatCount(3);
        shakeRight.setRepeatCount(3);

        shakeLeft.setRepeatMode(ObjectAnimator.REVERSE);
        shakeRight.setRepeatMode(ObjectAnimator.REVERSE);


        AnimatorSet shakeAnimator = new AnimatorSet();
        shakeAnimator.playSequentially(shakeLeft, shakeRight);  // Play shakeLeft first, then shakeRight


        shakeAnimator.start();
    }
}

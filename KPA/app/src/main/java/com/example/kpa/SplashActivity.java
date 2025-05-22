package com.example.kpa;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Step 1: Get the ImageView for the logo
        ImageView logo = findViewById(R.id.logoImage);

        // Step 2: Load and start the old logo animation (with bounce and fade-in)
        Animation logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        logo.startAnimation(logoAnim);  // Starts the scaling and fading animation

        // Step 3: Animate walking man across the loading bar
        ImageView walkingMan = findViewById(R.id.walkingMan);
        FrameLayout loadingBar = findViewById(R.id.loadingBar);

        walkingMan.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            float barWidth = loadingBar.getWidth();
            float manWidth = walkingMan.getWidth();

            if (barWidth > 0 && manWidth > 0) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(walkingMan, "translationX", 0, barWidth - manWidth);
                animator.setDuration(SPLASH_DELAY);
                animator.start();
            }
        });

        // Step 4: Navigate to MainActivity after the delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, SPLASH_DELAY);
    }
}

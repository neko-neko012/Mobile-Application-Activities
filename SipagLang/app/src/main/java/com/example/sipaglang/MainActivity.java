package com.example.sipaglang;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private TextView tvWorkDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new fragment_home()).commit();
        // Initialize the TextView
        tvWorkDetails = findViewById(R.id.tvWorkDetails);

        // Retrieve the work details passed from CreateWorkActivity
        String workTitle = getIntent().getStringExtra("workTitle");
        String workDescription = getIntent().getStringExtra("workDescription");

        // Check if the data exists, then update the TextView
        if (workTitle != null && workDescription != null) {
            String workDetails = "Title: " + workTitle + "\nDescription: " + workDescription;
            tvWorkDetails.setText(workDetails);
        } else {
            tvWorkDetails.setText("No work details available.");
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.nav_home) {
                    selectedFragment = new fragment_home();
                } else if (item.getItemId() == R.id.nav_history) {
                    selectedFragment = new fragment_history();
                } else if (item.getItemId() == R.id.nav_summary) {
                    selectedFragment = new fragment_summary();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }

                return true;
            }

        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.dark_mode) {
            // Handle Dark Mode toggle
            return true;
        } else if (item.getItemId() == R.id.options) {
            //  Intent intent = new Intent(MainActivity.this,OptionsActivity.class);
            // startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.logout) {
            showLogoutDialog();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }


    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Handle logout logic
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
                    startActivity(intent);
                    finish(); // Finish current activity
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }


}



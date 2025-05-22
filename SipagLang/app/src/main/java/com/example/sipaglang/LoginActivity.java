package com.example.sipaglang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsername, editPassword;
    private UserDatabase db;

    private User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views and the UserDatabase
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        db = UserDatabase.getInstance(this);

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Run Room query in a background thread
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    User user = db.userDao().getUserByUsername(username);

                    runOnUiThread(() -> {
                        if (user != null && user.getPassword().equals(password)) {
                            currentUser = user;
                            authenticateFingerprint(); // safe to call from main thread
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        });


        TextView createAccountTextView = findViewById(R.id.createAccount);
        createAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.fade_out); // Optional transition
            }
        });

    }

    private void authenticateFingerprint() {
        // Check if biometric authentication is available
        BiometricManager biometricManager = BiometricManager.from(this);
        int canAuthenticate = biometricManager.canAuthenticate();

        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            // Create an executor and biometric prompt for fingerprint authentication
            Executor executor = ContextCompat.getMainExecutor(this);
            BiometricPrompt biometricPrompt = new BiometricPrompt(LoginActivity.this,
                    executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(LoginActivity.this, "Fingerprint authentication successful", Toast.LENGTH_SHORT).show();
                    // Proceed to the next activity after successful fingerprint authentication
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("firstName", currentUser.getFirstName());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up, R.anim.fade_out);
                    finish();  // Close the login activity
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    // Optionally, allow the user to retry or fallback to manual login
                }
            });

            // Set up the biometric prompt info
            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Fingerprint Login")
                    .setDescription("Authenticate using your fingerprint")
                    .setNegativeButtonText("Cancel")
                    .build();

            // Show the biometric authentication dialog
            biometricPrompt.authenticate(promptInfo);

        } else {
            Toast.makeText(this, "Biometric authentication not available", Toast.LENGTH_SHORT).show();
        }
    }
}

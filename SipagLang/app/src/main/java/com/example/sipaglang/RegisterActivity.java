package com.example.sipaglang;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    EditText editUsername, editPassword, editFirstName, editLastName;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(view -> {
            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            String firstName = editFirstName.getText().toString().trim();
            String lastName = editLastName.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User(username, password, firstName, lastName);

            Executors.newSingleThreadExecutor().execute(() -> {
                UserDatabase.getInstance(getApplicationContext()).userDao().insertUser(user);
                runOnUiThread(() -> Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show());
                finish(); // Go back to login or close register
            });
        });
    }
}

package com.example.sipaglang;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CreateWorkActivity extends AppCompatActivity {

    private EditText editWorkTitle, editWorkDescription;
    private Button btnCreateWork, btnCancelWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_work);

        // Initialize views
        editWorkTitle = findViewById(R.id.createeditWorkTitle);
        editWorkDescription = findViewById(R.id.createeditWorkDescription);
        btnCreateWork = findViewById(R.id.createbtnCreateWork);
        btnCancelWork = findViewById(R.id.createbtnCancelWork);

        // Handle Create Work button click
        btnCreateWork.setOnClickListener(v -> {
            String title = editWorkTitle.getText().toString();
            String description = editWorkDescription.getText().toString();

            if (title.isEmpty() || description.isEmpty()) {
                // Display error if title or description is empty
                Toast.makeText(CreateWorkActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save the work details (you can add your database saving logic here)
            saveWorkDetails(title, description);

            // Optionally, show a success message
            Toast.makeText(CreateWorkActivity.this, "Work Created Successfully!", Toast.LENGTH_SHORT).show();

            // Pass work details to MainActivity via Intent
            Intent intent = new Intent(CreateWorkActivity.this, MainActivity.class);
            intent.putExtra("workTitle", title);
            intent.putExtra("workDescription", description);
            startActivity(intent);  // Start MainActivity
        });

        // Handle Cancel button click
        btnCancelWork.setOnClickListener(v -> {
            finish();  // Close the activity without saving
        });
    }

    private void saveWorkDetails(String title, String description) {
        // Add your database saving logic here (e.g., save the work in SQLite or Firebase)
    }
}

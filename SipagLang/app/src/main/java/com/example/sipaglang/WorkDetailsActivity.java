package com.example.sipaglang;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;

public class WorkDetailsActivity extends AppCompatActivity {
    private TextView tvWorkType, tvQuantity, tvPrice, tvTotal;
    private ImageView imgWork;
    private Button btnDeleteWork;

    private int workId = -1;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_details);

        // Initialize UI elements
        tvWorkType = findViewById(R.id.tv_work_type);
        tvQuantity = findViewById(R.id.tv_quantity);
        tvPrice = findViewById(R.id.tv_price);
        tvTotal = findViewById(R.id.tv_total);
        imgWork = findViewById(R.id.img_work);
        btnDeleteWork = findViewById(R.id.btn_delete_work);



        Intent intent = getIntent();
        if (intent != null) {
            workId = intent.getIntExtra("workId", -1);
            String workType = intent.getStringExtra("workType");
            int quantity = intent.getIntExtra("quantity", 0);
            double price = intent.getDoubleExtra("price", 0.0);
            imagePath = intent.getStringExtra("imagePath");

            Log.e("WorkDetailsActivity", "Received workId: " + workId);
            Log.e("WorkDetailsActivity", "Received workType: " + workType);
            Log.e("WorkDetailsActivity", "Received quantity: " + quantity);
            Log.e("WorkDetailsActivity", "Received price: " + price);
            Log.e("WorkDetailsActivity", "Received imagePath: " + imagePath);

            double total = quantity * price;

            // Display data
            tvWorkType.setText("Work: " + (workType != null ? workType : "N/A"));
            tvQuantity.setText("Quantity: " + quantity);
            tvPrice.setText("Price: ₱" + price);
            tvTotal.setText("Total: ₱" + total);

            // Load Image
            loadImage(imagePath);
        } else {
            Toast.makeText(this, "Error: No work details received", Toast.LENGTH_SHORT).show();
            Log.e("WorkDetailsActivity", "Intent is null!");
            finish();
        }


        btnDeleteWork.setOnClickListener(v -> deleteWork());
    }

    private void loadImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            File imgFile = new File(imagePath);
            Log.e("WorkDetailsActivity", "Checking file: " + imgFile.getAbsolutePath());

            if (imgFile.exists()) {
                Glide.with(this)
                        .load(imgFile)
                        .into(imgWork);
                imgWork.setVisibility(View.VISIBLE);
            } else {
                Log.e("WorkDetailsActivity", "Image file does not exist!");
                Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show();
                imgWork.setVisibility(View.GONE);
            }
        } else {
            Log.e("WorkDetailsActivity", "imagePath is NULL or EMPTY!");
            imgWork.setVisibility(View.GONE);
        }
    }

    private void deleteWork() {
        Log.d("WorkDetailsActivity", "deleteWork() method triggered");

        if (workId == -1) {
            Toast.makeText(this, "Error: Work not found", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            WorkDatabase db = WorkDatabase.getInstance(this);
            WorkDao workDao = db.workDao();
            workDao.deleteById(workId); // Deletes the work by workId

            // No need for image deletion now

            runOnUiThread(() -> {
                Toast.makeText(this, "Work deleted", Toast.LENGTH_SHORT).show();
                finish();  // Close the activity after deletion
            });
        }).start();
    }

}

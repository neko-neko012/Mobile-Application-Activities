package com.example.kpa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import java.io.File;
import java.util.Locale;

public class WorkDetailsActivity extends AppCompatActivity {
    private WorkEntity currentWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_details);

        currentWork = getIntent().getParcelableExtra("work");
        if (currentWork == null) {
            finish();
            return;
        }

        initViews();
    }

    private void initViews() {
        TextView tvWorkType = findViewById(R.id.tv_work_type);
        TextView tvQuantity = findViewById(R.id.tv_quantity);
        TextView tvPrice = findViewById(R.id.tv_price);
        TextView tvTotal = findViewById(R.id.tv_total);
        TextView tvDate = findViewById(R.id.tv_date);
        TextView tvLocation = findViewById(R.id.tv_location);
        ImageView imgWork = findViewById(R.id.img_work);
        WebView mapWebView = findViewById(R.id.mapWebView);

        // Set work details
        tvWorkType.setText("Work Type: " + currentWork.getWorkType());
        tvQuantity.setText("Quantity: " + currentWork.getQuantity());
        tvPrice.setText("Price: $" + String.format("%.2f", currentWork.getPrice()));
        tvTotal.setText("Total: $" + String.format("%.2f", currentWork.getTotal()));
        tvDate.setText("Date: " + currentWork.getDate());

        // Set location
        if (currentWork.getLatitude() != 0.0 && currentWork.getLongitude() != 0.0) {
            String locationText = String.format(Locale.getDefault(),
                    "Location: %s\nCoordinates: %.6f, %.6f",
                    currentWork.getLocation(),
                    currentWork.getLatitude(),
                    currentWork.getLongitude());

            tvLocation.setText(locationText);
            setupMap(mapWebView, currentWork.getLatitude(), currentWork.getLongitude());
        } else {
            tvLocation.setText("Location: Not specified");
            mapWebView.setVisibility(View.GONE);
        }

        // Load image
        try {
            Glide.with(this)
                    .load(currentWork.getImageUri())
                    .placeholder(R.drawable.logo)
                    .into(imgWork);
        } catch (Exception e) {
            imgWork.setImageResource(R.drawable.logo);
        }
    }

    private void setupMap(WebView webView, double lat, double lng) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        String html = "<!DOCTYPE html>" +
                "<html><head>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<link rel='stylesheet' href='https://unpkg.com/leaflet@1.7.1/dist/leaflet.css'/>" +
                "<script src='https://unpkg.com/leaflet@1.7.1/dist/leaflet.js'></script>" +
                "<style>#map { height: 100%; width: 100%; }</style>" +
                "</head><body>" +
                "<div id='map'></div>" +
                "<script>" +
                "var map = L.map('map').setView([" + lat + ", " + lng + "], 15);" +
                "L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);" +
                "var marker = L.marker([" + lat + ", " + lng + "]).addTo(map);" +
                "marker.bindPopup('Work Location<br>Lat: " + lat + "<br>Lng: " + lng + "').openPopup();" +
                "</script></body></html>";

        webView.loadDataWithBaseURL("https://localhost", html, "text/html", "UTF-8", null);
    }
}
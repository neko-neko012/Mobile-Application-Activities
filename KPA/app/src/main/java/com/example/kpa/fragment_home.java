package com.example.kpa;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import android.location.LocationManager;
import android.content.Context;
import android.location.LocationListener;

import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;




public class fragment_home extends Fragment {
    private LocationManager locationManager;
    private ImageView imgPreview;
    private WorkAdapter workAdapter;
    private WorkViewModel workViewModel;
    private String imagePath;
    private Uri imageUri;
    private WebView mapWebView; // Add this
    private CheckBox checkBoxShowMap; // Add this
    private double selectedLatitude = 0.0;
    private double selectedLongitude = 0.0;

    private static final int PERMISSION_REQUEST_CODE = 100;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<String[]> permissionLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        workViewModel = new ViewModelProvider(this).get(WorkViewModel.class);

        workAdapter = new WorkAdapter(requireContext(), new ArrayList<>());

        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);


        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(workAdapter);

        loadWorkEntries();

        FloatingActionButton fabAddWork = view.findViewById(R.id.fab_add_work);
        fabAddWork.setOnClickListener(v -> showAddWorkDialog());

        // Inside onCreate() or onViewCreated()
        TextView tvCurrentDate = view.findViewById(R.id.tvCurrentDate);


        // Get the current date and format it
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());


        tvCurrentDate.setText(currentDate);

        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    if (result.containsValue(false)) {
                        Toast.makeText(requireContext(), "Permissions Denied!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Permissions Granted!", Toast.LENGTH_SHORT).show();
                    }
                });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && imageUri != null) {

                        imgPreview.setImageURI(imageUri);
                        imgPreview.setVisibility(View.VISIBLE);


                        String savedImagePath = saveImageToInternalStorage(imageUri);
                        Log.d("ImageSave", "Image saved at: " + savedImagePath);
                    }
                });

        // Gallery Handler
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            // Set the image URI
                            imageUri = selectedImageUri;

                            // Display in the ImageView
                            imgPreview.setImageURI(selectedImageUri);
                            imgPreview.setVisibility(View.VISIBLE);


                            String savedImagePath = saveImageToInternalStorage(selectedImageUri);
                            Log.d("ImageSave", "Image saved at: " + savedImagePath);
                            imagePath = savedImagePath;
                        }
                    }
                });

        recyclerView.setAdapter(workAdapter);


        // Add the ItemTouchHelper swipe swipe
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // No movement support
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                WorkEntity deletedWork = workAdapter.getWorkList().get(position);

                // Show confirmation dialog
                new AlertDialog.Builder(requireContext())  // Using the context of the fragment
                        .setMessage("Are you sure you want to delete this item?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> {
                            workAdapter.removeItem(position);
                        })
                        .setNegativeButton("No", (dialog, id) -> {

                            workAdapter.notifyItemChanged(position);
                        })
                        .create()
                        .show();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        // Attach the ItemTouchHelper to your RecyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;


    }

    private void loadWorkEntries() {
        workViewModel.getAllWork().observe(getViewLifecycleOwner(), workEntities -> {
            if (workAdapter != null) {
                workAdapter.setWorkList(workEntities);
            }
        });
    }

    private void showAddWorkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_work, null);
        builder.setView(dialogView);

        // Initialize views
        EditText etWorkType = dialogView.findViewById(R.id.et_work_type);
        EditText etQuantity = dialogView.findViewById(R.id.et_quantity);
        EditText etPrice = dialogView.findViewById(R.id.et_price);
        imgPreview = dialogView.findViewById(R.id.img_preview);
        mapWebView = dialogView.findViewById(R.id.mapWebView);
        checkBoxShowMap = dialogView.findViewById(R.id.checkBox_show_maps);
        ImageButton btnTakePhoto = dialogView.findViewById(R.id.btn_take_photo);
        ImageButton btnSelectPhoto = dialogView.findViewById(R.id.btn_select_photo);
        Button btnSave = dialogView.findViewById(R.id.btn_save);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);

        setupMapWebView();
        checkBoxShowMap.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mapWebView.setVisibility(View.VISIBLE);
                loadLeafletMap();
            } else {
                mapWebView.setVisibility(View.GONE);
            }
        });

        // Add these click listeners
        btnTakePhoto.setOnClickListener(v -> {
            if (hasPermissions()) {
                openCamera();
            } else {
                requestPermissions();
            }
        });

        btnSelectPhoto.setOnClickListener(v -> {
            if (hasPermissions()) {
                openGallery();
            } else {
                requestPermissions();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


        // Save button click listener
        btnSave.setOnClickListener(v -> {
            String workType = etWorkType.getText().toString().trim();
            String quantityStr = etQuantity.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String locationAddress = "Selected Location"; // You can get this from reverse geocoding

            // Validation
            if (workType.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (imageUri == null) {
                Toast.makeText(requireContext(), "Please select an image!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int quantity = Integer.parseInt(quantityStr);
                double price = Double.parseDouble(priceStr);

                // Save image
                String savedImagePath = saveImageToInternalStorage(imageUri);
                if (savedImagePath == null) {
                    Toast.makeText(requireContext(), "Failed to save image", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create work entity with all location data
                WorkEntity newWork = new WorkEntity(
                        workType,
                        quantity,
                        price,
                        savedImagePath,
                        null, // mapUri can be null or you can save a map snapshot
                        locationAddress,
                        todayDate,
                        selectedLatitude,
                        selectedLongitude
                );

                // Save to database
                new Thread(() -> {
                    try {
                        WorkRepository repository = new WorkRepository(requireActivity().getApplication());
                        long insertedId = repository.insert(newWork);

                        requireActivity().runOnUiThread(() -> {
                            if (insertedId > 0) {
                                Toast.makeText(requireContext(), "Work saved successfully!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                loadWorkEntries();
                            } else {
                                Toast.makeText(requireContext(), "Failed to save work", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
                        Log.e("SaveError", "Failed to save work", e);
                    }
                }).start();

            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Invalid number format", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setupMapWebView() {
        WebSettings webSettings = mapWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        mapWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false); // Allow location access
            }
        });
    }

    private void loadLeafletMap() {
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.7.1/dist/leaflet.css\" />\n" +
                "  <script src=\"https://unpkg.com/leaflet@1.7.1/dist/leaflet.js\"></script>\n" +
                "  <style> #map { height: 100%; margin: 0; } </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div id=\"map\"></div>\n" +
                "  <script>\n" +
                "    var map = L.map('map').setView([13.5869, 122.9657], 15);\n" +
                "    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);\n" +
                "\n" +
                "    var marker;\n" +
                "    map.on('click', function(e) {\n" +
                "      if (marker) map.removeLayer(marker);\n" +
                "      marker = L.marker(e.latlng).addTo(map)\n" +
                "        .bindPopup(\"Selected Location\").openPopup();\n" +
                "\n" +
                "      // Send coordinates back to Android\n" +
                "      AndroidInterface.saveLocation(e.latlng.lat, e.latlng.lng);\n" +
                "    });\n" +
                "  </script>\n" +
                "</body>\n" +
                "</html>";

        mapWebView.addJavascriptInterface(new WebAppInterface(), "AndroidInterface");
        mapWebView.loadDataWithBaseURL("https://localhost", html, "text/html", "UTF-8", null);
    }

    // Interface to receive coordinates from JavaScript
    private class WebAppInterface {
        @android.webkit.JavascriptInterface
        public void saveLocation(double lat, double lng) {
            selectedLatitude = lat;
            selectedLongitude = lng;
            Toast.makeText(requireContext(),
                    "Location set: " + lat + ", " + lng, Toast.LENGTH_SHORT).show();
        }
    }
    private void openCamera() { // Camera Function
        try {
            File photoFile = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "photo_" + System.currentTimeMillis() + ".jpg");


            imageUri = FileProvider.getUriForFile(requireContext(),
                    "com.example.kpa.fileprovider", photoFile);


            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            cameraLauncher.launch(cameraIntent);

        } catch (Exception e) {
            Log.e("CameraError", "Failed to open camera", e);
            Toast.makeText(requireContext(), "Camera failed to open!", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    private boolean hasPermissions() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermissions() {
        permissionLauncher.launch(new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION
        });
    }


    private String saveImageToInternalStorage(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);

            // Get the file name and save it to internal storage
            File outputFile = new File(requireContext().getFilesDir(), "image_" + System.currentTimeMillis() + ".jpg");

            FileOutputStream fos = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            return outputFile.getAbsolutePath();

        } catch (IOException e) {
            Log.e("SaveError", "Error saving image to internal storage", e);
            return null;
        }
    }


}

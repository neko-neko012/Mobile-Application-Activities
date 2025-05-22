package com.example.kpa;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.provider.MediaStore;
import android.content.ContentValues;
import android.net.Uri;

public class PDFgenerator {

    public static void generatePdf(Context context, double totalEarnings, double totalPerPerson, int presentPeople) {
        // Check permissions for external storage (for Android below 10)
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }

        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(400, 600, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        int yPos = 50;
        int lineSpacing = 50; // Spacing between lines
        paint.setTextSize(16);

        // Combine "Date Today" and "Work Summary" for the title
        String title = "Date Today: " + new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(new Date()) + " - Work Summary";

        canvas.drawText(title, 50, yPos, paint); // Draw dynamic title
        yPos += lineSpacing;

        // Content
        canvas.drawText("Total Earnings: ₱" + String.format("%.2f", totalEarnings), 50, yPos, paint);
        yPos += lineSpacing;

        canvas.drawText("Total People Present: " + presentPeople, 50, yPos, paint);
        yPos += lineSpacing;

        canvas.drawText("Earnings Per Person: ₱" + String.format("%.2f", totalPerPerson), 50, yPos, paint);
        yPos += lineSpacing;

        canvas.drawText("Heraldo Rice Mill", 50, yPos, paint);
        yPos += lineSpacing;

        // Multi-line address text handling
        String address = "Purok 4, Barangay San Nicolas, Talisay,\n" +
                "Camarines Norte, Region V, 4601 Philippines";

        // Split the address into lines and draw each line
        String[] addressLines = address.split("\n");
        for (String line : addressLines) {
            yPos += lineSpacing;
            canvas.drawText(line, 50, yPos, paint);
        }

        pdfDocument.finishPage(page);

        // Save to Downloads directory for Android 10+ devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Use MediaStore for Android 10+ devices (Scoped Storage)
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "WorkSummary.pdf");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            Uri uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), contentValues);

            try (FileOutputStream fos = (FileOutputStream) context.getContentResolver().openOutputStream(uri)) {
                pdfDocument.writeTo(fos);
                pdfDocument.close();
                Toast.makeText(context, "PDF saved to Downloads", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(context, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            // For devices before Android 10, save to public Downloads directory (deprecated in Android 10+)
            File pdfDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "KargaPalay");
            if (!pdfDir.exists()) {
                pdfDir.mkdirs();
            }

            File pdfFile = new File(pdfDir, "WorkSummary.pdf");
            try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
                pdfDocument.writeTo(fos);
                pdfDocument.close();
                Toast.makeText(context, "PDF saved to: " + pdfFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(context, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

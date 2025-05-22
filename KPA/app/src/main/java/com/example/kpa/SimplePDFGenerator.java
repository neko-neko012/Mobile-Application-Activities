package com.example.kpa;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SimplePDFGenerator {

    public static void generateSimplePdf(Context context, String content) {
        // Create a new PdfDocument
        PdfDocument pdfDocument = new PdfDocument();

        // Set up the page size and start a new page
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // Create a Paint object to draw text
        Paint paint = new Paint();
        paint.setTextSize(16);
        paint.setColor(android.graphics.Color.BLACK);

        // Write content to the canvas
        canvas.drawText(content, 100, 100, paint);

        // Finish the page
        pdfDocument.finishPage(page);

        // Create a directory for storing the PDF file
        File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "SimplePDF");
        if (!filePath.exists()) {
            filePath.mkdirs();  // Create directory if it doesn't exist
        }

        // Specify the file where the PDF will be saved
        File pdfFile = new File(filePath, "SimplePDFExample.pdf");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);
            pdfDocument.writeTo(fileOutputStream);
            fileOutputStream.close();
            pdfDocument.close();
            Toast.makeText(context, "PDF generated successfully at: " + pdfFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error creating PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}


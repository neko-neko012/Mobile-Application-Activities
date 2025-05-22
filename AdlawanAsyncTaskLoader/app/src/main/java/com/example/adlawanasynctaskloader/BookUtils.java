package com.example.adlawanasynctaskloader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BookUtils {
    public static List<Book> getBookList(String query) {
        List<Book> books = new ArrayList<>();
        try {
            String urlStr = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&maxResults=10";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonResult = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonResult.append(line);
            }

            JSONObject jsonObject = new JSONObject(jsonResult.toString());
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject bookItem = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = bookItem.getJSONObject("volumeInfo");
                String title = volumeInfo.optString("title", "No Title");
                JSONArray authorsArray = volumeInfo.optJSONArray("authors");
                String author = authorsArray != null ? authorsArray.getString(0) : "No Author";

                books.add(new Book(title, author));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return books;
    }
}

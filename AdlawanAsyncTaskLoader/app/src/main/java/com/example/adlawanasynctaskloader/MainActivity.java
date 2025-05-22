package com.example.adlawanasynctaskloader;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private List<Book> bookList = new ArrayList<>();
    private EditText bookInput;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bookInput = findViewById(R.id.book_input);
        searchButton = findViewById(R.id.search_button);
        recyclerView = findViewById(R.id.book_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookAdapter(this, bookList);
        recyclerView.setAdapter(adapter);

        searchButton.setOnClickListener(v -> {
            String query = bookInput.getText().toString().trim();
            if (!query.isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putString("queryString", query);
                LoaderManager.getInstance(this).restartLoader(0, bundle, this);
            }
        });

        if (LoaderManager.getInstance(this).getLoader(0) != null) {
            LoaderManager.getInstance(this).initLoader(0, null, this);
        }
    }

    @NonNull
    @Override
    public Loader<List<Book>> onCreateLoader(int id, @Nullable Bundle args) {
        String query = args != null ? args.getString("queryString") : "";
        return new BookLoader(this, query);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Book>> loader, List<Book> data) {
        bookList.clear();
        if (data != null && !data.isEmpty()) {
            bookList.addAll(data);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Book>> loader) {
        bookList.clear();
        adapter.notifyDataSetChanged();
    }
}

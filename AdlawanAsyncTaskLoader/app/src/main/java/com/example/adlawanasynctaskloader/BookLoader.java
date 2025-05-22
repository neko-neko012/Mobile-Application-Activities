package com.example.adlawanasynctaskloader;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private String query;

    public BookLoader(Context context, String query) {
        super(context);
        this.query = query;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        return BookUtils.getBookList(query);
    }
}

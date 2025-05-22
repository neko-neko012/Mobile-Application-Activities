package com.example.kpa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class fragment_history_calendar extends Fragment {

    private CalendarView calendarView2;
    private RecyclerView recyclerView;




    public fragment_history_calendar() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history_calendar, container, false);

        CalendarView calendarView = view.findViewById(R.id.calendarView2); // fixed ID!

        if (calendarView != null) {
            calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
                // handle selected date
            });
        }

        return view;
    }




}

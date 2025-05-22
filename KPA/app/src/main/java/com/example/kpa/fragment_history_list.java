package com.example.kpa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class fragment_history_list extends Fragment implements HistoryShiftAdapter.OnShiftClickListener {

    private RecyclerView recyclerView;
    private HistoryShiftAdapter adapter;
    private List<ShiftEntity> shiftHistoryList = new ArrayList<>();
    private ShiftViewModel shiftViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);

        //  RecyclerView
        recyclerView = view.findViewById(R.id.rv_history_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Adapter
        adapter = new HistoryShiftAdapter(getContext(), shiftHistoryList, this);
        recyclerView.setAdapter(adapter);

        // ViewModel
        shiftViewModel = new ViewModelProvider(this).get(ShiftViewModel.class);

        shiftViewModel.getAllShifts().observe(getViewLifecycleOwner(), new Observer<List<ShiftEntity>>() {
            @Override
            public void onChanged(List<ShiftEntity> shiftEntities) {
                // Clear the current list and update it with new data
                shiftHistoryList.clear();

                if (shiftEntities != null && !shiftEntities.isEmpty()) {
                    shiftHistoryList.addAll(shiftEntities);  // Add all shifts
                }

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onShiftClick(String shiftDate) {
        showShiftsForDate(shiftDate);
    }

    private void showShiftsForDate(String shiftDate) {
        shiftViewModel.getShiftsForDate(shiftDate).observe(getViewLifecycleOwner(), new Observer<List<WorkEntity>>() {
            @Override
            public void onChanged(List<WorkEntity> workList) {
                if (workList != null && !workList.isEmpty()) {

                    Toast.makeText(requireContext(), "Found " + workList.size() + " work items for this date.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "No work found for this date.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

package com.example.kpa;


import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class fragment_summary extends Fragment {

    private WorkViewModel workViewModel;
    private RecyclerView rvSummary;
    private WorkAdapter workAdapter;
    private List<ShiftEntity> shiftList = new ArrayList<>();
    private TextView tvCurrentDate, sumTotalEarnings, sumTotalPerPerson;
    private EditText presentTotalPeople;
    private double totalEarnings = 0;
    private ShiftViewModel shiftViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        rvSummary = view.findViewById(R.id.rvSummary);
        tvCurrentDate = view.findViewById(R.id.tvCurrentDate);
        sumTotalEarnings = view.findViewById(R.id.sumTotalEarnings);
        sumTotalPerPerson = view.findViewById(R.id.sumTotalPerPerson);
        presentTotalPeople = view.findViewById(R.id.presentTotalPeople);
        Button btnPrintSummary = view.findViewById(R.id.btnPrintPdf);
        Button btnEndShift = view.findViewById(R.id.btnEndShift);
        workViewModel = new ViewModelProvider(this).get(WorkViewModel.class);

        tvCurrentDate.setText(getTodayDate());


        btnPrintSummary.setOnClickListener(v -> printSummaryToPDF());
        btnEndShift.setOnClickListener(v -> endShift());


        rvSummary.setLayoutManager(new LinearLayoutManager(getContext()));
        workAdapter = new WorkAdapter(requireContext(), new ArrayList<>());
        rvSummary.setAdapter(workAdapter);

        shiftViewModel = new ViewModelProvider(requireActivity()).get(ShiftViewModel.class); // Use ShiftViewModel

        loadWorkEntries();


        presentTotalPeople.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateTotalPerPerson();
            }
        });

        return view;
    }

    private String getTodayDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    private void loadWorkEntries() {

        String today = getTodayDate();

        WorkViewModel workViewModel = new ViewModelProvider(this).get(WorkViewModel.class);


        workViewModel.getWorkForTheDay(today).observe(getViewLifecycleOwner(), new Observer<List<WorkEntity>>() {
            @Override
            public void onChanged(List<WorkEntity> workEntities) {
                if (workEntities != null) {
                    workAdapter.setWorkList(workEntities);
                    updateEarnings(workEntities);
                }
            }
        });
    }





    private void updateEarnings(List<WorkEntity> workEntities) { //Earning update
        totalEarnings = 0;


        for (WorkEntity work : workEntities) {
            totalEarnings += work.getTotal();
        }


        sumTotalEarnings.setText(String.format(Locale.getDefault(), "Total: ₱%.2f", totalEarnings));


        calculateTotalPerPerson();
    }

    private void calculateTotalPerPerson() { //total Calculation
        String peopleText = presentTotalPeople.getText().toString().trim();


        if (TextUtils.isEmpty(peopleText) || !TextUtils.isDigitsOnly(peopleText)) {
            sumTotalPerPerson.setText("Total per person: ₱0.00");
            return;
        }

        try {
            int totalPeople = Integer.parseInt(peopleText);


            if (totalPeople > 0) {
                double perPersonEarnings = totalEarnings / totalPeople;
                sumTotalPerPerson.setText(String.format(Locale.getDefault(), "Total per person: ₱%.2f", perPersonEarnings));
            } else {
                sumTotalPerPerson.setText("Total per person: ₱0.00");
            }
        } catch (NumberFormatException e) {

            sumTotalPerPerson.setText("Invalid input");
        }
    }




    private void printSummaryToPDF() { // PDF

        try {
            // Debugging: Log the start of the process
            Log.d("PDFGeneration", "Starting PDF generation...");

            // Get the number of present people safely
            String presentPeopleString = presentTotalPeople.getText().toString();
            if (presentPeopleString.isEmpty()) {
                throw new NumberFormatException("Present People input is empty.");
            }

            int presentPeople = Integer.parseInt(presentPeopleString);

            Log.d("PDFGeneration", "Present people: " + presentPeople);

            // Calculate earnings per person
            double totalPerPerson = (presentPeople > 0) ? totalEarnings / presentPeople : 0;

            Log.d("PDFGeneration", "Total earnings: ₱" + totalEarnings);
            Log.d("PDFGeneration", "Earnings per person: ₱" + totalPerPerson);

            // Generate the PDF content
            String content = "Total Earnings: ₱" + String.format("%.2f", totalEarnings) + "\n"
                    + "Earnings Per Person: ₱" + String.format("%.2f", totalPerPerson) + "\n"
                    + "Total People Present: " + presentPeople;

            // Generate the PDF using the SimplePDFGenerator class
            SimplePDFGenerator.generateSimplePdf(requireContext(), content);

            Log.d("PDFGeneration", "PDF generation called successfully.");

            // Show success toast
            Toast.makeText(requireContext(), "PDF Generated Successfully!", Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            Log.e("PDFGeneration", "Invalid input: " + e.getMessage());
            Toast.makeText(requireContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("PDFGeneration", "An unexpected error occurred: " + e.getMessage());
            Toast.makeText(requireContext(), "An unexpected error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private void endShift() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm Shift End")
                .setMessage("Are you sure you want to end your shift? This action cannot be undone.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


                    workViewModel.getWorkForTheDay(today).observe(getViewLifecycleOwner(), workList -> {
                        if (workList != null && !workList.isEmpty()) {
                            // Calculate total earnings
                            double totalEarnings = 0;
                            for (WorkEntity work : workList) {
                                totalEarnings += work.getTotal();


                                HistoryEntity history = new HistoryEntity(
                                        today,
                                        work.getTotal(),
                                        (work.getTotal() / 1),
                                        1
                                );

                                workViewModel.delete(work);
                            }

                            workViewModel.clearAllWork();



                            Toast.makeText(requireContext(), "Shift Ended. Summary saved to history.", Toast.LENGTH_SHORT).show();


                            ShiftEntity shiftEntity = new ShiftEntity(today, totalEarnings);
                            shiftViewModel.insert(shiftEntity);  // This must be called to save it!


                        } else {
                            Toast.makeText(requireContext(), "No work entries for today.", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }


}

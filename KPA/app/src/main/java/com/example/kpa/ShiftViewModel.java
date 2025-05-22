package com.example.kpa;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ShiftViewModel extends AndroidViewModel {

    private ShiftRepository shiftRepository;
    private LiveData<List<ShiftEntity>> allShifts;
    private LiveData<List<ShiftEntity>> completedShifts;
    private LiveData<List<ShiftEntity>> ongoingShifts;
    private WorkRepository workRepository; // Declare the work repository
    private LiveData<List<WorkEntity>> shiftsForToday;
    public ShiftViewModel(Application application) {
        super(application);
        // Initialize ShiftRepository
        shiftRepository = new ShiftRepository(application);
        workRepository = new WorkRepository(application);

        // Fetch shift data
        allShifts = shiftRepository.getAllShifts();
        completedShifts = shiftRepository.getCompletedShifts();
        ongoingShifts = shiftRepository.getOngoingShifts();
    }

    // Getter for LiveData<List<ShiftEntity>> to observe changes in all shifts
    public LiveData<List<ShiftEntity>> getAllShifts() {
        return allShifts;
    }

    // Getter for LiveData<List<ShiftEntity>> to observe completed shifts
    public LiveData<List<ShiftEntity>> getCompletedShifts() {
        return completedShifts;
    }

    // Getter for LiveData<List<ShiftEntity>> to observe ongoing shifts
    public LiveData<List<ShiftEntity>> getOngoingShifts() {
        return ongoingShifts;
    }

    // Insert a shift into the database
    public void insert(ShiftEntity shift) {
        shiftRepository.insert(shift);
    }

    // Update an existing shift record
    public void update(ShiftEntity shift) {
        shiftRepository.update(shift);
    }

    // Delete a shift record
    public void delete(ShiftEntity shift) {
        shiftRepository.delete(shift);
    }

    // Delete all shifts from the database
    public void deleteAllShifts() {
        shiftRepository.deleteAllShifts();
    }

    // Fetch shifts for a specific date

    public LiveData<List<WorkEntity>> getShiftsForDate(String date) {
        // This calls a method from your repository to fetch work data for the given date
        return workRepository.getAllWork();
    }
    public void moveShiftToHistory(ShiftEntity shift) {
        shiftRepository.moveShiftToHistory(shift);
    }


    public void insertHistory(HistoryEntity history) {
        shiftRepository.insertHistory(history);
    }

}

package com.example.kpa;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShiftRepository {

    private ShiftDao shiftDao;
    private HistoryDao historyDao;
    private LiveData<List<ShiftEntity>> allShifts;
    private LiveData<List<ShiftEntity>> completedShifts;
    private LiveData<List<ShiftEntity>> ongoingShifts;
    private ExecutorService executorService;

    public ShiftRepository(Application application) {

        ShiftDatabase db = ShiftDatabase.getInstance(application);
        shiftDao = db.shiftDao();
        historyDao = db.historyDao();


        allShifts = shiftDao.getAllShifts();
        completedShifts = shiftDao.getCompletedShifts();
        ongoingShifts = shiftDao.getOngoingShifts();


        executorService = Executors.newSingleThreadExecutor();
    }


    public LiveData<List<ShiftEntity>> getAllShifts() {
        return allShifts;
    }


    public LiveData<List<ShiftEntity>> getCompletedShifts() {
        return completedShifts;
    }


    public LiveData<List<ShiftEntity>> getOngoingShifts() {
        return ongoingShifts;
    }


    public void insert(ShiftEntity shift) {
        executorService.execute(() -> shiftDao.insert(shift));
    }


    public void update(ShiftEntity shift) {
        executorService.execute(() -> shiftDao.update(shift));
    }


    public void delete(ShiftEntity shift) {
        executorService.execute(() -> shiftDao.delete(shift));
    }


    public void deleteAllShifts() {
        executorService.execute(() -> shiftDao.deleteAllShifts());
    }


    public LiveData<List<ShiftEntity>> getShiftsForDate(String date) {
        return shiftDao.getShiftsForDate(date);
    }

    public void moveShiftToHistory(ShiftEntity shift) {
        executorService.execute(() -> {

            double totalEarnings = shift.getTotal();

            double earningsPerPerson = totalEarnings / shift.getTotalPeople();


            HistoryEntity history = new HistoryEntity(shift.getShiftDate(), totalEarnings, earningsPerPerson, shift.getTotalPeople());


            historyDao.insertHistory(history);


            shiftDao.delete(shift);
        });
    }


    public void insertHistory(HistoryEntity history) {
        executorService.execute(() -> historyDao.insertHistory(history));
    }

}

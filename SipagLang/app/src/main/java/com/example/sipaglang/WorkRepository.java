package com.example.sipaglang;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkRepository {
    private WorkDao workDao;
    private LiveData<List<WorkEntity>> allWork;
    private ExecutorService executorService;
    private LiveData<List<WorkEntity>> allWorkEntities;

    public WorkRepository(Application application) {
        WorkDatabase db = WorkDatabase.getInstance(application); // Ensure the database class name is correct
        workDao = db.workDao();


        allWork = workDao.getAllWork();  // LiveData of all work entries

        executorService = Executors.newSingleThreadExecutor();
    }


    // Fetch all work records (LiveData)
    public LiveData<List<WorkEntity>> getAllWork() {
        return allWork;
    }

    // Insert a new WorkEntity into the database
    public void insert(WorkEntity work) {
        executorService.execute(() -> workDao.insert(work));
    }

    // Update an existing WorkEntity
    public void update(WorkEntity work) {
        executorService.execute(() -> workDao.update(work));
    }

    // Delete an existing WorkEntity
    public void delete(WorkEntity work) {
        executorService.execute(() -> workDao.delete(work));
    }

    // Clear all work records from the database
    public void clearAllWork() {
        executorService.execute(() -> workDao.deleteAllWork());
    }

    // Fetch work records for a specific date (LiveData)
    public LiveData<List<WorkEntity>> getWorkForTheDay(String date) {
        return workDao.getWorkForTheDay(date);
    }


    public List<WorkEntity> getWorkByDate(String date) {
        return workDao.getWorkByDate(date);

    }



}

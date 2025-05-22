package com.example.kpa;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WorkViewModel extends AndroidViewModel {

    private WorkRepository workRepository;
    private LiveData<List<WorkEntity>> allWork;

    // Constructor - Initialize the ViewModel
    public WorkViewModel(Application application) {
        super(application);
        workRepository = new WorkRepository(application);
        allWork = workRepository.getAllWork();
    }


    public LiveData<List<WorkEntity>> getAllWork() {
        return allWork;
    }




    public void insert(WorkEntity work) {
        workRepository.insert(work);
    }


    public void update(WorkEntity work) {
        workRepository.update(work);
    }


    public void delete(WorkEntity work) {
        workRepository.delete(work);
    }


    public void clearAllWork() {
        workRepository.clearAllWork();
    }


    public LiveData<List<WorkEntity>> getWorkForTheDay(String date) {
        return workRepository.getWorkForTheDay(date);
    }


}

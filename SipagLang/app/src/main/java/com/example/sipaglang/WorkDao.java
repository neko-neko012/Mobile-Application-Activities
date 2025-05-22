package com.example.sipaglang;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface WorkDao {

    @Insert
    long insert(WorkEntity work);

    @Update
    void update(WorkEntity work);

    @Delete
    void delete(WorkEntity work);

    @Query("DELETE FROM work_table WHERE id = :workId")
    void deleteById(long workId);

    @Query("SELECT * FROM work_table ORDER BY id DESC")
    LiveData<List<WorkEntity>> getAllWork();

    @Query("SELECT * FROM work_table WHERE date = :today")
    List<WorkEntity> getWorkByDate(String today);

    @Query("DELETE FROM work_table")
    void deleteAllWork();

    @Query("SELECT * FROM work_table WHERE date = :date")
    LiveData<List<WorkEntity>> getWorkForTheDay(String date);
}

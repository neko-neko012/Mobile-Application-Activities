package com.example.kpa;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WorkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(WorkEntity work);

    @Update
    void update(WorkEntity work);

    @Delete
    void delete(WorkEntity work);

    @Query("DELETE FROM work_table WHERE id = :workId")
    void deleteById(long workId);

    @Query("SELECT * FROM work_table ORDER BY id DESC")
    LiveData<List<WorkEntity>> getAllWork();

    @Query("SELECT * FROM work_table WHERE date = :date")
    LiveData<List<WorkEntity>> getWorkForTheDay(String date);

    @Query("SELECT * FROM work_table WHERE date = :today")
    List<WorkEntity> getWorkByDate(String today);

    @Query("DELETE FROM work_table")
    void deleteAllWork();

    @Query("SELECT * FROM work_table WHERE id = :id")
    LiveData<WorkEntity> getWorkById(long id);

    @Query("SELECT * FROM work_table WHERE latitude = :latitude AND longitude = :longitude")
    LiveData<List<WorkEntity>> getWorkByCoordinates(double latitude, double longitude);

    @Query("SELECT * FROM work_table WHERE location LIKE '%' || :query || '%'")
    LiveData<List<WorkEntity>> searchByLocation(String query);

    @Query("SELECT * FROM work_table WHERE workType LIKE '%' || :query || '%'")
    LiveData<List<WorkEntity>> searchByWorkType(String query);

    @Query("SELECT DISTINCT date FROM work_table ORDER BY date DESC")
    LiveData<List<String>> getAllDates();

    @Query("SELECT * FROM work_table WHERE mapUri IS NOT NULL")
    LiveData<List<WorkEntity>> getWorkWithMaps();
}
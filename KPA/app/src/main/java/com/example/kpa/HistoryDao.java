package com.example.kpa;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDao {


    @Insert
    long insertHistory(HistoryEntity history);


    @Query("SELECT * FROM history_table ORDER BY date DESC")
    LiveData<List<HistoryEntity>> getAllHistory();


    @Query("SELECT * FROM history_table WHERE date = :date ORDER BY date DESC")
    LiveData<List<HistoryEntity>> getHistoryForDate(String date);


    @Query("SELECT * FROM history_table WHERE id = :id")
    LiveData<HistoryEntity> getHistoryById(long id);


    @Query("DELETE FROM history_table")
    void deleteAllHistory();
}

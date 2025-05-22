package com.example.kpa;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface ShiftDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)  // Replace the record if it already exists
    long insert(ShiftEntity shift);

    @Update
    void update(ShiftEntity shift);

    @Delete
    void delete(ShiftEntity shift);


    @Query("SELECT * FROM shift_table ORDER BY shiftDate DESC")
    LiveData<List<ShiftEntity>> getAllShifts();

    @Query("SELECT * FROM shift_table WHERE shiftDate = :date ORDER BY shiftDate DESC")
    LiveData<List<ShiftEntity>> getShiftsForDate(String date);


    @Query("SELECT * FROM shift_table WHERE shiftEnded = 1 ORDER BY shiftDate DESC")
    LiveData<List<ShiftEntity>> getCompletedShifts();

    @Query("SELECT * FROM shift_table WHERE shiftEnded = 0 ORDER BY shiftDate DESC")
    LiveData<List<ShiftEntity>> getOngoingShifts();

    @Query("DELETE FROM shift_table")
    void deleteAllShifts();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertHistory(HistoryEntity history);
}

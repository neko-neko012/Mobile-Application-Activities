package com.example.kpa;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ShiftEntity.class, HistoryEntity.class}, version = 1)
public abstract class ShiftDatabase extends RoomDatabase {

    private static ShiftDatabase instance;

    public abstract ShiftDao shiftDao();
    public abstract HistoryDao historyDao();

    public static synchronized ShiftDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ShiftDatabase.class, "shift_database")
                    .fallbackToDestructiveMigration()  // Optional: handles migration on schema change
                    .build();
        }
        return instance;
    }
}

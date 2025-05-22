package com.example.kpa;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {WorkEntity.class, HistoryEntity.class, WorkEntry.class}, version = 3, exportSchema = false)
public abstract class WorkDatabase extends RoomDatabase {

    private static volatile WorkDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    private static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract WorkDao workDao();

    public static WorkDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (WorkDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    WorkDatabase.class, "work_database")
                            .fallbackToDestructiveMigration() // In case of DB schema change
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static ExecutorService getDatabaseExecutor() {
        return databaseWriteExecutor;
    }
}

package com.example.kpa;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "work_entries")
public class WorkEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String date;
    private double earnings;
    private int peopleCount;

    public WorkEntry(String date, double earnings, int peopleCount) {
        this.date = date;
        this.earnings = earnings;
        this.peopleCount = peopleCount;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public double getEarnings() { return earnings; }
    public void setEarnings(double earnings) { this.earnings = earnings; }

    public int getPeopleCount() { return peopleCount; }
    public void setPeopleCount(int peopleCount) { this.peopleCount = peopleCount; }
}

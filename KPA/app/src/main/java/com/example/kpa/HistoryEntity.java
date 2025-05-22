package com.example.kpa;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
@Entity(tableName = "history_table")
public class HistoryEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String date;
    private double totalEarnings;
    private double earningsPerPerson;
    private int totalPeople;


    public HistoryEntity(String date, double totalEarnings, double earningsPerPerson, int totalPeople) {
        this.date = date;
        this.totalEarnings = totalEarnings;
        this.earningsPerPerson = earningsPerPerson;
        this.totalPeople = totalPeople;
    }

    // Getter and Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(double totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public double getEarningsPerPerson() {
        return earningsPerPerson;
    }

    public void setEarningsPerPerson(double earningsPerPerson) {
        this.earningsPerPerson = earningsPerPerson;
    }

    public int getTotalPeople() {
        return totalPeople;
    }

    public void setTotalPeople(int totalPeople) {
        this.totalPeople = totalPeople;
    }
}

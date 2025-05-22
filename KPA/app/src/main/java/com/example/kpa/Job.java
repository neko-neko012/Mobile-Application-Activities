package com.example.kpa;

public class Job {
    private String jobName;
    private int quantity;
    private double price;
    private String location;
    private double total;

    // Constructor
    public Job(String jobName, int quantity, double price, String location) {
        this.jobName = jobName;
        this.quantity = quantity;
        this.price = price;
        this.location = location;
        this.total = quantity * price; // Calculate total
    }

    // Getters
    public String getJobName() {
        return jobName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public double getTotal() {
        return total;
    }
}



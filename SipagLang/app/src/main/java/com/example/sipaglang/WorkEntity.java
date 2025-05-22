package com.example.sipaglang;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "work_table")
public class WorkEntity implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String workType;
    private int quantity;
    private double price;
    private double total;
    private String imageUri;
    private String location;
    private String date;
    private String mapUri;  // Add the new field for mapUri

    public WorkEntity(String workType, int quantity, double price, String imageUri, String mapUri, String location, String date) {
        this.workType = workType;
        this.quantity = quantity;
        this.price = price;
        this.total = quantity * price;
        this.imageUri = imageUri;
        this.mapUri = mapUri;  // Initialize the mapUri field
        this.location = location;
        this.date = date;
    }

    // Getter and Setter for mapUri
    public String getMapUri() {
        return mapUri;
    }

    public void setMapUri(String mapUri) {
        this.mapUri = mapUri;
    }

    // The rest of your getters and setters remain the same...
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Parcelable methods
    protected WorkEntity(Parcel in) {
        workType = in.readString();
        quantity = in.readInt();
        price = in.readDouble();
        total = in.readDouble();
        imageUri = in.readString();
        mapUri = in.readString();  // Read mapUri from Parcel
        location = in.readString();
        date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(workType);
        dest.writeInt(quantity);
        dest.writeDouble(price);
        dest.writeDouble(total);
        dest.writeString(imageUri);
        dest.writeString(mapUri);  // Write mapUri to Parcel
        dest.writeString(location);
        dest.writeString(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WorkEntity> CREATOR = new Creator<WorkEntity>() {
        @Override
        public WorkEntity createFromParcel(Parcel in) {
            return new WorkEntity(in);
        }

        @Override
        public WorkEntity[] newArray(int size) {
            return new WorkEntity[size];
        }
    };
}

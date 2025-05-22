package com.example.kpa;

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
    private String location;  // Textual address
    private String date;
    private String mapUri;    // URI for map snapshot
    private double latitude;  // Latitude coordinate
    private double longitude; // Longitude coordinate

    public WorkEntity() {}

    public WorkEntity(String workType, int quantity, double price, String imageUri,
                      String mapUri, String location, String date,
                      double latitude, double longitude) {
        this.workType = workType;
        this.quantity = quantity;
        this.price = price;
        this.total = quantity * price;
        this.imageUri = imageUri;
        this.mapUri = mapUri;
        this.location = location;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and setters for all fields
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getWorkType() { return workType; }
    public void setWorkType(String workType) { this.workType = workType; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getMapUri() { return mapUri; }
    public void setMapUri(String mapUri) { this.mapUri = mapUri; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    // Parcelable implementation
    protected WorkEntity(Parcel in) {
        id = in.readLong();
        workType = in.readString();
        quantity = in.readInt();
        price = in.readDouble();
        total = in.readDouble();
        imageUri = in.readString();
        mapUri = in.readString();
        location = in.readString();
        date = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(workType);
        dest.writeInt(quantity);
        dest.writeDouble(price);
        dest.writeDouble(total);
        dest.writeString(imageUri);
        dest.writeString(mapUri);
        dest.writeString(location);
        dest.writeString(date);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
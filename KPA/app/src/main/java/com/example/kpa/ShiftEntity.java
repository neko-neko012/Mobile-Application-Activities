package com.example.kpa;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
@Entity(tableName = "shift_table")
public class ShiftEntity implements Parcelable {

    public ShiftEntity(String shiftDate, double totalEarnings) {
        this.shiftDate = shiftDate;
        this.total = totalEarnings;
    }

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String workType;
    private int quantity;
    private double price;
    private double total;
    private String imageUri;
    private String shiftDate;
    private String location;
    private int shiftEnded;
    private int totalPeople;


    public ShiftEntity(String workType, int quantity, double price, String imageUri, String shiftDate, String location, int shiftEnded) {
        this.workType = workType;
        this.quantity = quantity;
        this.price = price;
        this.total = quantity * price;
        this.imageUri = imageUri;
        this.shiftDate = shiftDate;
        this.location = location;
        this.shiftEnded = shiftEnded;
        this.totalPeople = totalPeople;
    }

    @Ignore
    public ShiftEntity(String shiftDate) {
        this.shiftDate = shiftDate;
    }

    public ShiftEntity() {
    }

    protected ShiftEntity(Parcel in) {
        id = in.readLong();
        workType = in.readString();
        quantity = in.readInt();
        price = in.readDouble();
        total = in.readDouble();
        imageUri = in.readString();
        shiftDate = in.readString();
        location = in.readString();
        shiftEnded = in.readInt();
        totalPeople = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(workType);
        dest.writeInt(quantity);
        dest.writeDouble(price);
        dest.writeDouble(total);
        dest.writeString(imageUri);
        dest.writeString(shiftDate);
        dest.writeString(location);
        dest.writeInt(shiftEnded);
        dest.writeInt(totalPeople);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShiftEntity> CREATOR = new Creator<ShiftEntity>() {
        @Override
        public ShiftEntity createFromParcel(Parcel in) {
            return new ShiftEntity(in);
        }

        @Override
        public ShiftEntity[] newArray(int size) {
            return new ShiftEntity[size];
        }
    };

    // Getters and setters
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
    public String getShiftDate() { return shiftDate; }
    public void setShiftDate(String shiftDate) { this.shiftDate = shiftDate; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public int getShiftEnded() { return shiftEnded; }
    public void setShiftEnded(int shiftEnded) { this.shiftEnded = shiftEnded; }
    public int getTotalPeople() { return totalPeople; }
    public void setTotalPeople(int totalPeople) { this.totalPeople = totalPeople; }
}

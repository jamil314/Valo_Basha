package com.example.valo_basha;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;

public class Apartment implements Parcelable {
    String name = "Default building", owner= "Default owner", contactInfo = "2441139";
    int totalFloors=7, bedrooms=3, bathrooms=2, rent=15000;
    double area=1780;
    boolean furniture=false;
    long mask=127;
    Apartment(String name, String owner,double area, int rent, boolean furniture, int totalFloors, int bedrooms, int bathrooms, String contactInfo, long mask){
        this.name = name;
        this.owner = owner;
        this.area = area;
        this.rent = rent;
        this.furniture = furniture;
        this.totalFloors = totalFloors;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.contactInfo = contactInfo;
        this.mask = mask;
    }
    Apartment(){}

    protected Apartment(Parcel in) {
        name = in.readString();
        owner = in.readString();
        contactInfo = in.readString();
        totalFloors = in.readInt();
        bedrooms = in.readInt();
        bathrooms = in.readInt();
        rent = in.readInt();
        mask = in.readLong();
        area = in.readDouble();
        furniture = in.readByte() != 0;
    }

    public static final Creator<Apartment> CREATOR = new Creator<Apartment>() {
        @Override
        public Apartment createFromParcel(Parcel in) {
            return new Apartment(in);
        }

        @Override
        public Apartment[] newArray(int size) {
            return new Apartment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(owner);
        parcel.writeString(contactInfo);
        parcel.writeInt(totalFloors);
        parcel.writeInt(bedrooms);
        parcel.writeInt(bathrooms);
        parcel.writeInt(rent);
        parcel.writeLong(mask);
        parcel.writeDouble(area);
        parcel.writeByte((byte) (furniture ? 1 : 0));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public int getTotalFloors() {
        return totalFloors;
    }

    public void setTotalFloors(int totalFloors) {
        this.totalFloors = totalFloors;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public boolean isFurniture() {
        return furniture;
    }

    public void setFurniture(boolean furniture) {
        this.furniture = furniture;
    }

    public long getMask() {
        return mask;
    }

    public void setMask(long mask) {
        this.mask = mask;
    }

    public static Creator<Apartment> getCREATOR() {
        return CREATOR;
    }
}

package com.example.valo_basha;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;

public class Apartment implements Parcelable {
    String name, owner, contactInfo;
    int totalFloors, bedrooms, bathrooms, rent;
    double area;
    boolean furniture;
    long mask;
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
}

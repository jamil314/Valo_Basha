package com.example.valo_basha;

import java.util.ArrayList;

public class Apartment {
    String name, owner, contactInfo;
    int totalFloors, bedrooms, bathrooms;
    ArrayList<Integer> availableFloors = new ArrayList<Integer>();
    Apartment(String name, String owner, int totalFloors, int bedrooms, int bathrooms, String contactInfo, int ... available_floors){
        this.name = name;
        this.owner = owner;
        this.totalFloors = totalFloors;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.contactInfo = contactInfo;
        for(int t: available_floors) this.availableFloors.add(t);
    }
    Apartment(){}
}

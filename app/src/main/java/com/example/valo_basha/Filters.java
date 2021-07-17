package com.example.valo_basha;

public class Filters {
    int minRent, maxRent, maxBed, minBed, maxBath, minBath;
    double maxArea, minArea;
    boolean furn, isRent, isBed, isBath, isArea, isFurn;
    Filters(){
        minRent = minBath = minBed = 0;
        maxRent = 10000000;
        maxBath = maxBed = 10;
        minArea = 0;
        maxArea = 100000000;
        furn = isArea = isBath = isBed = isFurn = isRent = false;
    }
}

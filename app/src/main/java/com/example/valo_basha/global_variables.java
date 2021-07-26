package com.example.valo_basha;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class global_variables {
    public static double xco;
    public static double yco;
    public static double buildingX;
    public static double buildingY;
    public static Location user_location;
    public static String user_addressline, building_addressline;
    public static int BuildingStatus = 0; // 0-> no new building, 1-> new building started, 2-> location defined, 3-> upload clicked
    public static boolean flag = false;
    public static ArrayList<Apartment> all_apartment = new ArrayList<>();
    public static ArrayList<Apartment> query_apartment = new ArrayList<>();
    public static boolean list_update = true, idDone = false, cntDone = false;
    public static Filters prev_filter = new Filters();
    public static int id, cnt;


}

package com.example.valo_basha;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link map_container#newInstance} factory method to
 * create an instance of this fragment.
 */
public class map_container extends Fragment {


    // for filter
    Spinner furniture;
    EditText minBed, minBath, minRent, minArea, maxBed, maxBath, maxRent, maxArea;
    Switch isBed, isBath, isRent, isArea, isFurn;
    Button done;
    Filters filter = global_variables.prev_filter;
    //------------------

    Button centreButton, b_list, b_filter;
    FusedLocationProviderClient client;

    SearchView searchView;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public map_container() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment map_container.
     */
    // TODO: Rename and change types and number of parameters
    public static map_container newInstance(String param1, String param2) {
        Log.d("JAMIL", "map_container -> (;;)");
        map_container fragment = new map_container();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("JAMIL", "map_container -> onCreat");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("JAMIL", "map_container -> onCreatView");
        // Inflate the layout for this fragment



        View view = inflater.inflate(R.layout.fragment_map_container, container, false);
        Log.d("JAMIL", "==========");

        client  = LocationServices.getFusedLocationProviderClient(getActivity());
        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        ==PackageManager.PERMISSION_GRANTED){
            getLocation();
        }else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                    ,Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
        b_filter = view.findViewById(R.id.btn_filter);
        b_list = view.findViewById(R.id.btn_confirm_list);
        if(global_variables.BuildingStatus == 3){
            b_filter.setVisibility(View.INVISIBLE);
            b_list.setText(" Confirm Location ");
        } else{
            b_filter.setVisibility(View.VISIBLE);
            b_list.setText(" Show List View ");
        }
        centreButton = view.findViewById(R.id.centreButton);
        centreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                                ==PackageManager.PERMISSION_GRANTED){
                    getLocation();
                }else{
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                            ,Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }

            }
        });




        b_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(getActivity(), filter_activity.class);
                startActivity(intent);*/
                filter();
            }
        });


        b_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(global_variables.BuildingStatus == 3){
                    global_variables.BuildingStatus = 1;
                    getActivity().onBackPressed();

                } else {
                    Intent intent2 = new Intent(getActivity(), apartment_list.class);
                    intent2.putExtra("key", "tenent");
                    startActivity(intent2);
                }

            }
        });


       /*     searchView = view.findViewById(R.id.search_view);
        Places.initialize(getActivity(), "AIzaSyDXBv_MRdJayyAwvnPMPFs4-oNP7Y_sxHA ");
        searchView.setFocusable(false);
        searchView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS
                ,Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY
                        , fieldList).build(getActivity());
                startActivityForResult(intent, 100);
                Log.d("JAMIL", "so far so good");
            }
        });*/






        Log.d("JAMIL", "Center button ok");
        searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("JAMIL", "search box clicked");
                String qlocation = searchView.getQuery().toString();
                Log.d("JAMIL", "Searched location: " + qlocation);
                if(qlocation.equals("jamil")){
                    LatLng qpos = new LatLng(24.892456, 91.884148);
                    gmap.moveTo(qpos, (float) 19.0);
                    return false;
                }
                List<Address> addressList = null;
                if (qlocation != null || !qlocation.equals("")) {
                    Geocoder geocoder = new Geocoder(getActivity());
                    try {
                        addressList = geocoder.getFromLocationName(qlocation, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast toast = Toast.makeText(getActivity(),
                                "Try again please", Toast.LENGTH_LONG);
                        toast.show();
                        Log.d("jAMIL", "Address fetching failed: "+e.getMessage());
                        return false;
                    }
                    Log.d("JAMIL", "getting address");
                    Log.d("JAMIL", String.valueOf(addressList.size()));
                    if (addressList.size() == 0) {
                        Log.d("JAMIL", "no result found");
                        Toast toast = Toast.makeText(getActivity(),
                                "No result found for "+qlocation, Toast.LENGTH_LONG);
                        toast.show();
                        return false;
                    }
                    Address address = addressList.get(0);
                    Log.d("JAMIL", address.getLatitude() + " " + address.getLongitude());
                    LatLng qpos = new LatLng(address.getLatitude(), address.getLongitude());
                    gmap.moveTo(qpos, (float) 10.0);
                    gmap.moveMarker(qpos);
                } else {
                    Log.d("JAMIL", "location null");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });



        return view;




    }




   /* @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("JAMIL", "Here");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            searchView.setText(place.getAddress());
            Log.d("JAMIL", "LOCATION: " + String.valueOf(place.getLatLng()));
        }else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Log.d("JAMIL", "AutocompleteActivity.RESULT_ERROR");
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100 && grantResults.length>0 && grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED){
            getLocation();
        }else{
            Toast.makeText(getActivity()
                    ,"PERMISSION DENOED", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation(){
        Log.d("JAMIL", "getting location");
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();

                    if(location != null){
                        LatLng qpos = new LatLng(location.getLatitude(), location.getLongitude());
                        gmap.moveTo(qpos, (float) 15.0);
                        gmap.moveMarker(qpos);
                        Log.d("JAMIL", "YAY!! "+String.valueOf(location.getLatitude())+" : "
                                +String.valueOf(location.getLongitude()));
                        global_variables.user_location = location;
                        global_variables.flag = true;
                    } else {
                        Log.d("JAMIL", "NO LUCK");
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                //super.onLocationResult(locationResult);
                                Location location1 = locationResult.getLastLocation();
                                LatLng qpos = new LatLng(location1.getLatitude(), location1.getLongitude());
                                gmap.moveTo(qpos, (float) 15.0);
                                gmap.moveMarker(qpos);
                                Log.d("JAMIL", "YAY!!!!! "+String.valueOf(location1.getLatitude())+" : "
                                        +String.valueOf(location1.getLongitude()));
                                global_variables.user_location = location1;
                                global_variables.flag = true;
                            }
                        };
                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });
        }else{
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }





    private void filter() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View popup = getLayoutInflater().inflate(R.layout.popup_filter, null);
        minArea = popup.findViewById(R.id.area_min);
        minBath = popup.findViewById(R.id.bathroom_min);
        minBed = popup.findViewById(R.id.bedroom_min);
        minRent = popup.findViewById(R.id.rent_min);
        maxArea = popup.findViewById(R.id.area_max);
        maxBath = popup.findViewById(R.id.bathroom_max);
        maxBed = popup.findViewById(R.id.bedroom_max);
        maxRent = popup.findViewById(R.id.rent_max);
        isArea = popup.findViewById(R.id.sw_area);
        isBath = popup.findViewById(R.id.sw_bath);
        isBed = popup.findViewById(R.id.sw_bed);
        isRent = popup.findViewById(R.id.sw_rent);
        isFurn = popup.findViewById(R.id.sw_furniture);
        furniture = popup.findViewById(R.id.spinner);
        done = popup.findViewById(R.id.filter_confirm);
        reset();
        builder.setView(popup);
        AlertDialog dialog = builder.create();
        dialog.show();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(error()) return;
                gmap.marker_filter(filter);
                global_variables.prev_filter = filter;
                dialog.dismiss();
            }
        });
    }


    private void reset() {
        if(filter.isArea) isArea.setChecked(true);
        if(filter.isBed) isBed.setChecked(true);
        if(filter.isBath) isBath.setChecked(true);
        if(filter.isRent) isRent.setChecked(true);
        if(filter.isFurn) isFurn.setChecked(true);

        if(filter.minArea!=0) minArea.setText(String.valueOf(filter.minArea));
        if(filter.minBed!=0) minBed.setText(String.valueOf(filter.minBed));
        if(filter.minBath!=0) minBath.setText(String.valueOf(filter.minBath));
        if(filter.minRent!=0) minRent.setText(String.valueOf(filter.minRent));

        if(filter.maxArea!=100000000) maxArea.setText(String.valueOf(filter.maxArea));
        if(filter.maxBed!=10) maxBed.setText(String.valueOf(filter.maxBed));
        if(filter.maxBath!=10) maxBath.setText(String.valueOf(filter.maxBath));
        if(filter.maxRent!=10000000) maxRent.setText(String.valueOf(filter.maxRent));

        if(filter.furn) furniture.setSelection(1);
    }

    boolean error(){
        boolean err = false;
        String str;
        if(isArea.isChecked()) {
            filter.isArea = true;

            str = minArea.getText().toString();
            if(str.equals("")) filter.minArea = 0;
            else{
                if (Double.parseDouble(str) < 0) {
                    err = true;
                    minArea.setError("0 - 100000000");
                } else if (Double.parseDouble(str) > 100000000) {
                    err = true;
                    minArea.setError("0 - 100000000");
                } else filter.minArea = Double.parseDouble(str);
            }

            str = maxArea.getText().toString();
            if(str.equals("")) filter.maxArea = 100000000;
            else{
                if (Double.parseDouble(str) < 0) {
                    err = true;
                    maxArea.setError("0 - 100000000");
                } else if (Double.parseDouble(str) > 100000000) {
                    err = true;
                    maxArea.setError("0 - 100000000");
                } else filter.maxArea = Double.parseDouble(str);
            }

        } else filter.isArea = false;

        if(isBed.isChecked()) {
            filter.isBed = true;

            str = minBed.getText().toString();
            if(str.equals("")) filter.minBed = 0;
            else{
                if (Integer.parseInt(str) < 0) {
                    err = true;
                    minBed.setError("0 - 10");
                } else if (Integer.parseInt(str) > 10) {
                    err = true;
                    minBed.setError("0 - 10");
                } else filter.minBed = Integer.parseInt(str);
            }

            str = maxBed.getText().toString();
            if(str.equals("")) filter.maxBed = 10;
            else{
                if (Integer.parseInt(str) < 0) {
                    err = true;
                    maxBed.setError("0 - 10");
                } else if (Integer.parseInt(str) > 10) {
                    err = true;
                    maxBed.setError("0 - 10");
                } else filter.maxBed = Integer.parseInt(str);
            }

        } else filter.isBed = false;

        if(isBath.isChecked()) {
            filter.isBath = true;

            str = minBath.getText().toString();
            if(str.equals("")) filter.minBath = 0;
            else{
                if (Integer.parseInt(str) < 0) {
                    err = true;
                    minBath.setError("0 - 10");
                } else if (Integer.parseInt(str) > 10) {
                    err = true;
                    minBath.setError("0 - 10");
                } else filter.minBath = Integer.parseInt(str);
            }

            str = maxBath.getText().toString();
            if(str.equals("")) filter.maxBath = 10;
            else{
                if (Integer.parseInt(str) < 0) {
                    err = true;
                    maxBath.setError("0 - 10");
                } else if (Integer.parseInt(str) > 10) {
                    err = true;
                    maxBath.setError("0 - 10");
                } else filter.maxBath = Integer.parseInt(str);
            }

        } else filter.isBath = false;

        if(isRent.isChecked()) {
            filter.isRent = true;

            str = minRent.getText().toString();
            if(str.equals("")) filter.minRent = 0;
            else{
                if (Integer.parseInt(str) < 0) {
                    err = true;
                    minRent.setError("0 - 10000000");
                } else if (Integer.parseInt(str) > 10000000) {
                    err = true;
                    minRent.setError("0 - 10000000");
                } else filter.minRent = Integer.parseInt(str);
            }

            str = maxRent.getText().toString();
            if(str.equals("")) filter.maxRent = 10000000;
            else{
                if (Integer.parseInt(str) < 0) {
                    err = true;
                    maxRent.setError("0 - 10000000");
                } else if (Integer.parseInt(str) > 10000000) {
                    err = true;
                    maxRent.setError("0 - 10000000");
                } else filter.maxRent = Integer.parseInt(str);
            }

        } else filter.isRent = false;

        if(isFurn.isChecked()){
            filter.isFurn = true;
            if(furniture.getSelectedItem().equals("With furniture")) filter.furn = true;
            else filter.furn = false;
        } else filter.isFurn = false;

        return err;
    }



}
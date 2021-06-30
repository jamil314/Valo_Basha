package com.example.valo_basha;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
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

    Button centreButton;
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


         centreButton = view.findViewById(R.id.centreButton);
            client  = LocationServices.getFusedLocationProviderClient(getActivity());
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
                List<Address> addressList = null;
                if (qlocation != null || !qlocation.equals("")) {
                    Geocoder geocoder = new Geocoder(getActivity());
                    try {
                        addressList = geocoder.getFromLocationName(qlocation, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("JAMIL", "getting address");
                    Log.d("JAMIL", String.valueOf(addressList.size()));
                    if (addressList.size() == 0) {
                        Log.d("JAMIL", "no result found");
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
                                LatLng qpos = new LatLng(location.getLatitude(), location.getLongitude());
                                gmap.moveTo(qpos, (float) 15.0);
                                gmap.moveMarker(qpos);
                                Log.d("JAMIL", "YAY!! "+String.valueOf(location.getLatitude())+" : "
                                        +String.valueOf(location.getLongitude()));
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




}
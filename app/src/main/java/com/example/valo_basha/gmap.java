package com.example.valo_basha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class gmap extends Fragment {
    private static GoogleMap gMap;
    TextView tvLat, tvlong;
    FusedLocationProviderClient client;
    Location currentLocation;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private boolean locationPermission = false;
    private FusedLocationProviderClient locationProviderClient;
    private static final int REQUEST_CODE = 101;
    public static Marker marker ;
    SearchView searchView;
    private AppBarConfiguration mAppBarConfiguration;
    GoogleMap map;
    SupportMapFragment mapFragment;
    Button centreButton;
    LocationManager locationManager;
    FusedLocationProviderClient fusedLocationProviderClient;

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            Log.d("JAMIL", "Gmap - > onMapReady");
            gMap = googleMap;
            gMap.getUiSettings().setZoomControlsEnabled(true);
            LatLng sylhet = new LatLng(24.8924503, 91.884156);
            LatLng userLocation = new LatLng(global_variables.xco, global_variables.yco);
            Log.d(userLocation.latitude + ":JAMIL: ", userLocation.longitude + "");
            marker = gMap.addMarker(new MarkerOptions().position(sylhet).title("You are here!!"));

            //marker.setPosition(sylhet);
            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sylhet));

            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sylhet, 15), 5000, null);




        }
    };


    public static void moveTo(LatLng location, float zoom){
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoom), 5000, null);
    }



    public static void moveMarker(LatLng location){
        marker.setPosition(location);
    }

        @Nullable
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            Log.d("JAMIL", "Gmap -> onCreateView");







            View view = inflater.inflate(R.layout.fragment_gmap, container, false);


            /*centreButton = view.findViewById(R.id.centreButton);
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
*/

            return view;
        }



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

    private void getLocation(){
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();

                    if(location != null){
                        tvLat.setText(String.valueOf(location.getLatitude()));
                        tvlong.setText(String.valueOf(location.getLongitude()));
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
                                tvLat.setText(String.valueOf(location1.getLatitude()));
                                tvlong.setText(String.valueOf(location1.getLongitude()));
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



        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            Log.d("JAMIL", "Gmap -> onViewCreated");
            super.onViewCreated(view, savedInstanceState);
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.gmap);
            if (mapFragment != null) {
                mapFragment.getMapAsync(callback);
            }
        }
}

package com.example.valo_basha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.gms.location.FusedLocationProviderClient;
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

public class gmap extends Fragment {
    private static GoogleMap gMap;
    Location currentLocation;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private boolean locationPermission = false;
    private FusedLocationProviderClient locationProviderClient;
    private static final int REQUEST_CODE = 101;
    public static Marker marker ;

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
            return inflater.inflate(R.layout.fragment_gmap, container, false);
        }




        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.gmap);
            if (mapFragment != null) {
                mapFragment.getMapAsync(callback);
            }
        }
}

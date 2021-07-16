package com.example.valo_basha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class gmap extends Fragment {
    private static GoogleMap gMap;
    public static Marker marker ;
    ArrayList<Marker> houses = new ArrayList<Marker>();
    ArrayList<Apartment> apartments = new ArrayList<Apartment>();
    Apartment razaTower = new Apartment("Raja Tower", "Z. S. Raja", 1200, 15000, false, 6, 3, 3, "2441139", 56);
    Apartment jahedVilla = new Apartment("Jahed Villa", "Jahed Ahmed", 800, 12000, false,  5, 2, 2, "0199999", 26);
    Apartment dubaiTower = new Apartment("Dubai Tower", "Haroon Miah", 1000, 25000, true, 5, 3, 2, "0177777", 69);
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
            apartments.add(razaTower);
            apartments.add(jahedVilla);
            apartments.add(dubaiTower);
            gMap = googleMap;
            gMap.getUiSettings().setZoomControlsEnabled(true);
            LatLng sylhet = new LatLng(24.9178183, 91.8309513);
            LatLng userLocation = new LatLng(global_variables.xco, global_variables.yco);
            Log.d(userLocation.latitude + ":JAMIL: ", userLocation.longitude + "");
            if(global_variables.flag) sylhet = new LatLng(global_variables.user_location.getLatitude(), global_variables.user_location.getLongitude());
            marker = gMap.addMarker(new MarkerOptions().position(sylhet).title("You are here!!").snippet("-1"));

            //marker.setPosition(sylhet);
            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sylhet));

            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sylhet, 17), 5000, null);


            if(gMap!=null){
                gMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(@NonNull  Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(@NonNull Marker marker) {
                        int id = Integer.parseInt(marker.getSnippet());
                        if(id==-1){
                            View v = getLayoutInflater().inflate(R.layout.markertext, null);
                            return v;

                        }
                        View v = getLayoutInflater().inflate(R.layout.house_details, null);
                        TextView name = v.findViewById(R.id.name);
                        TextView area = v.findViewById(R.id.area);
                        TextView bedroom = v.findViewById(R.id.bedroom);
                        TextView bathroom = v.findViewById(R.id.bathroom);
                        TextView furniture = v.findViewById(R.id.furniture);
                        TextView rent = v.findViewById(R.id.rent);
                        Log.d("JAMIL", marker.getSnippet());
                        name.setText(apartments.get(id).name);
                        bedroom.setText(String.valueOf(apartments.get(id).bedrooms));
                        bathroom.setText(String.valueOf(apartments.get(id).bathrooms));
                        area.setText(String.valueOf(apartments.get(id).area));
                        rent.setText(String.valueOf(apartments.get(id).rent));
                        if(apartments.get(id).furniture) furniture.setText("With");
                        else furniture.setText("without");
                        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(@NonNull  Marker marker) {
                                if(marker.getSnippet() == "-1") return;
                                Log.d("JAMIL", name.getText()+" is clicked");
                                Intent i = new Intent(getActivity(), InDepthApartmentDetails.class);
                                i.putExtra("apartment", (Parcelable) apartments.get(id));
                                Log.d("JAMIL", "passed data successfully");
                                startActivity(i);
                            }
                        });
                        return v;
                    }
                });
            }


            houses.add(gMap.addMarker(new MarkerOptions().position(new LatLng(24.892496, 91.884184))
                    .icon(bitmapDescriptor(getActivity().getApplicationContext(), R.drawable.ic_apartment))
                    .snippet("0")));
            houses.add(gMap.addMarker(new MarkerOptions().position(new LatLng(24.892376, 91.883834))
                    .icon(bitmapDescriptor(getActivity().getApplicationContext(), R.drawable.ic_apartment))
                    .snippet("1")));
            houses.add(gMap.addMarker(new MarkerOptions().position(new LatLng(24.892523, 91.883842))
                    .icon(bitmapDescriptor(getActivity().getApplicationContext(), R.drawable.ic_apartment))
                    .snippet("2")));



        }
    };


    public static void moveTo(LatLng location, float zoom){
        Log.d("JAMIL", "moving......");
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoom), 5000, null);
    }



    public static void moveMarker(LatLng location){
        marker.setPosition(location);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    BitmapDescriptor bitmapDescriptor(Context context, int vectorResId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("JAMIL", "Gmap -> onCreateView");
        View view = inflater.inflate(R.layout.fragment_gmap, container, false);
        return view;
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
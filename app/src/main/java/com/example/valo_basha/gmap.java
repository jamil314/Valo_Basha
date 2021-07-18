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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class gmap extends Fragment {
    Button btn;
    private static GoogleMap gMap;
    public static Marker marker ;
    static ArrayList<Marker> houses = new ArrayList<>();
    static ArrayList<Apartment> apartments =new ArrayList<>();
    static boolean flag = false;
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
            flag = true;
            Log.d("JAMIL", "Gmap - > onMapReady");
            gMap = googleMap;
            gMap.getUiSettings().setZoomControlsEnabled(true);
            LatLng sylhet = new LatLng(24.9178183, 91.8309513);
            LatLng userLocation = new LatLng(global_variables.xco, global_variables.yco);
            Log.d(userLocation.latitude + ":JAMIL: ", userLocation.longitude + "");
            if(global_variables.flag) sylhet = new LatLng(global_variables.user_location.getLatitude(), global_variables.user_location.getLongitude());
            marker = gMap.addMarker(new MarkerOptions().position(sylhet).title("You are here!!").snippet("-1"));
            if(global_variables.BuildingStatus == 3) {
                marker.setDraggable(true);
                googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(@NonNull Marker marker) {
                        Log.d("JAMIL", "marker drag started");
                    }

                    @Override
                    public void onMarkerDrag(@NonNull Marker marker) {
                        Log.d("JAMIL", "marker dragging...");

                    }

                    @Override
                    public void onMarkerDragEnd(@NonNull Marker marker) {
                        Log.d("JAMIL", "marker drag finished");
                        global_variables.buildingX = marker.getPosition().latitude;
                        global_variables.buildingY = marker.getPosition().longitude;
                        //global_variables.building_addressline = marker.getPosition().
                        Log.d("JAMIL", "marker drag finished: "+global_variables.buildingX+" "+global_variables.buildingY);
                    }
                });
            } else marker.setDraggable(false);
            //marker.setPosition(sylhet);
            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sylhet));

            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sylhet, 17), 5000, null);


            DatabaseReference mDatabase;
            mDatabase = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference().child("ads");
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(flag) {
                        apartments.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Log.d("JAMIL", dataSnapshot.getKey() + ": " + String.valueOf(dataSnapshot.child("name").getValue()));
                            Apartment apartment = dataSnapshot.getValue(Apartment.class);
                            apartments.add(apartment);
                        }
                        Log.d("JAMIL", "done");
                        resetMarkers();
                        flag = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


    };

    public void resetMarkers() {
        int count = 0;
        houses.clear();
        Log.d("JAMIL" , "Houses cleared");
        for(Apartment a:apartments){
            Log.d("JAMIL", count+" "+a.name);
            houses.add(gMap.addMarker(new MarkerOptions().position(new LatLng(a.lat, a.lon))
                    .icon(bitmapDescriptor(getActivity().getApplicationContext(), R.drawable.ic_apartment))
                    .snippet(count+"")));
            count++;
        }
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
                            i.putExtra("key", "tenent");
                            Log.d("JAMIL", "passed data successfully");
                            startActivity(i);
                        }
                    });
                    return v;
                }
            });
        }
    }

    public static void marker_filter(Filters filter){
        for(Marker mark: houses) mark.setVisible(true);
        for(Marker mark: houses){
            int id = Integer.parseInt(mark.getSnippet());
            if(id==-1) continue;
            Apartment check = apartments.get(id);

            if(filter.isFurn){
                if(filter.furn != check.isFurniture()) mark.setVisible(false);
            }

            if(filter.isArea){
                if(filter.maxArea < check.area || filter.minArea > check.area) mark.setVisible(false);
            }

            if(filter.isBed){
                if(filter.maxBed < check.bedrooms || filter.minBed > check.bedrooms) mark.setVisible(false);
            }

            if(filter.isBath){
                if(filter.maxBath < check.bathrooms || filter.minBath > check.bathrooms) mark.setVisible(false);
            }

            if(filter.isRent){
                if(filter.maxRent < check.rent || filter.minRent > check.rent) mark.setVisible(false);
            }
        }
    }

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
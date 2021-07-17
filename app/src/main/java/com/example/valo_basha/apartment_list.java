package com.example.valo_basha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class apartment_list extends AppCompatActivity {
    LinearLayout apartmentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_list);
        getSupportActionBar().setTitle("Available apartments near you");
        apartmentList = findViewById(R.id.layout);
        for(Apartment apartment: gmap.apartments){
            Filters filter = global_variables.prev_filter;
            if(filter.isFurn){
                if(filter.furn != apartment.isFurniture()) continue;
            }

            if(filter.isArea){
                if(filter.maxArea < apartment.area || filter.minArea > apartment.area) continue;
            }

            if(filter.isBed){
                if(filter.maxBed < apartment.bedrooms || filter.minBed > apartment.bedrooms) continue;
            }

            if(filter.isBath){
                if(filter.maxBath < apartment.bathrooms || filter.minBath > apartment.bathrooms) continue;
            }

            if(filter.isRent){
                if(filter.maxRent < apartment.rent || filter.minRent > apartment.rent) continue;
            }


            View v = getLayoutInflater().inflate(R.layout.house_details, null, false);
            TextView name = v.findViewById(R.id.name);
            TextView area = v.findViewById(R.id.area);
            TextView bedroom = v.findViewById(R.id.bedroom);
            TextView bathroom = v.findViewById(R.id.bathroom);
            TextView furniture = v.findViewById(R.id.furniture);
            TextView rent = v.findViewById(R.id.rent);
            name.setText(apartment.name);
            bedroom.setText(String.valueOf(apartment.bedrooms));
            bathroom.setText(String.valueOf(apartment.bathrooms));
            area.setText(String.valueOf(apartment.area));
            rent.setText(String.valueOf(apartment.rent));
            if(apartment.furniture) furniture.setText("With");
            else furniture.setText("without");
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), InDepthApartmentDetails.class);
                    i.putExtra("apartment", (Parcelable) apartment);
                    Log.d("JAMIL", "passed data successfully");
                    startActivity(i);
                }
            });
            apartmentList.addView(v);
        }

    }
}
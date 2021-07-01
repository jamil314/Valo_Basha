package com.example.valo_basha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class InDepthApartmentDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_depth_apartment_details);
        Intent intent = getIntent();
        Apartment apartment = intent.getParcelableExtra("apartment");
        getSupportActionBar().setTitle(apartment.name);
        TextView owner = findViewById(R.id.owner);
        TextView area = findViewById(R.id.area);
        TextView bedroom = findViewById(R.id.bedroom);
        TextView bathroom = findViewById(R.id.bathroom);
        TextView furniture = findViewById(R.id.furniture);
        TextView rent = findViewById(R.id.rent);
        bedroom.setText(String.valueOf(apartment.bedrooms));
        bathroom.setText(String.valueOf(apartment.bathrooms));
        area.setText(String.valueOf(apartment.area));
        rent.setText(String.valueOf(apartment.rent));
        if(apartment.furniture) furniture.setText("With");
        else furniture.setText("without");
        TextView total_floors = findViewById(R.id.total_floors);
        TextView available_floors = findViewById(R.id.available_floors);
        TextView mobile_no = findViewById(R.id.mobile_no);
        owner.setText(apartment.owner);
        total_floors.setText(String.valueOf(apartment.totalFloors));
        String floors="";
        long t = apartment.mask;
        int f=0, c=1;
        while(t>0){
            if(t%2 == 1) {
                if(f!=0) floors = floors + ", ";
                f++;
                floors = floors + String.valueOf(c);
            }
            t = t/2;
            c++;
        }
        available_floors.setText(floors);
        mobile_no.setText(apartment.contactInfo);
    }
}
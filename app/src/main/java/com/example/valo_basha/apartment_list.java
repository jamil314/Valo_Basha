package com.example.valo_basha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class apartment_list extends AppCompatActivity {
    LinearLayout apartmentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_list);
        getSupportActionBar().setTitle("Available apartments near you");
        apartmentList = findViewById(R.id.layout);
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        mDatabase.child("ads").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    View apartment = getLayoutInflater().inflate(R.layout.house_details, null, false);
                    Log.d("JAMIL", dataSnapshot.getKey()+String.valueOf(dataSnapshot.child("name").getValue()));
                    TextView v = apartment.findViewById(R.id.bedroom);
                    v.setText( String.valueOf(dataSnapshot.child("bedrooms").getValue()));
                    v = apartment.findViewById(R.id.bathroom);
                    v.setText(String.valueOf(dataSnapshot.child("bathrooms").getValue()));
                    v = apartment.findViewById(R.id.area);
                    v.setText(String.valueOf(dataSnapshot.child("area").getValue()));
                    v = apartment.findViewById(R.id.rent);
                    v.setText(String.valueOf(dataSnapshot.child("rent").getValue()));
                    v = apartment.findViewById(R.id.name);
                    v.setText(String.valueOf(dataSnapshot.child("name").getValue()));
                    v = apartment.findViewById(R.id.furniture);
                    if(String.valueOf(dataSnapshot.child("furniture").getValue()).equals("true"))
                        v.setText("With");
                    else v.setText("Without");
                    Log.d("JAMIL", String.valueOf(v.getText()));
                    apartmentList.addView(apartment);
                    v = apartment.findViewById(R.id.extra);
                    v.setText("Tap to see details\n\n");
                }
                Log.d("JAMIL", "done");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
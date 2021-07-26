package com.example.valo_basha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class listFavs extends AppCompatActivity {
    LinearLayout apartmentList;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_favs);
        getSupportActionBar().setTitle("Your favourite apartments");
        apartmentList = findViewById(R.id.layout);
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference().child("users").child(user.getUid()).child("fav_list");
        Log.d("JAMIL", String.valueOf(mDatabase));
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                apartmentList.removeAllViews();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String id = String.valueOf(dataSnapshot.getValue());
                    DatabaseReference mDatabase2;
                    mDatabase2 = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            .getReference().child("ads").child(id);
                    mDatabase2.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot2) {
                            Apartment apartment = dataSnapshot2.getValue(Apartment.class);
                            addView(apartment);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void addView(Apartment apartment) {
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
        if (apartment.furniture) furniture.setText("With");
        else furniture.setText("without");
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), InDepthApartmentDetails.class);
                i.putExtra("apartment", (Parcelable) apartment);
                i.putExtra("key", "tenent");
                Log.d("JAMIL", "passed data successfully");
                startActivity(i);
            }
        });
        apartmentList.addView(v);
    }
}
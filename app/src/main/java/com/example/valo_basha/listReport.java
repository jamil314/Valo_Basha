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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class listReport extends AppCompatActivity {
    LinearLayout list;
    FirebaseUser user;
    Apartment apartment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_report);
        getSupportActionBar().setTitle("Your Reports:");
        user = FirebaseAuth.getInstance().getCurrentUser();
        list = findViewById(R.id.list);
        DatabaseReference rtdb = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference().child("users").child(user.getUid()).child("reports");
        rtdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String id = String.valueOf(dataSnapshot.getValue());
                    View v = getLayoutInflater().inflate(R.layout.unit_report, null, false);
                    TextView name = v.findViewById(R.id.building_name);
                    TextView massage = v.findViewById(R.id.report_message);
                    final String[] building = new String[1];
                    FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            .getReference().child("Reports").child(id).child("building_id")
                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            building[0] = task.getResult().getValue().toString();
                            FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                    .getReference().child("ads").child(building[0])
                                    .get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    apartment = dataSnapshot.getValue(Apartment.class);
                                    name.setText(apartment.name);
                                }
                            });
                        }
                    });
                    FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            .getReference().child("Reports").child(id).child("report")
                            .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            massage.setText(task.getResult().getValue().toString());
                        }
                    });
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
                    list.addView(v);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
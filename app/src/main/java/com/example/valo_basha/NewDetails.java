package com.example.valo_basha;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class NewDetails extends AppCompatActivity {
    SeekBar skbed, skbath;
    TextView bed, bath;
    EditText t_floors, rent, area, name, extra;
    CheckBox furniture;
    LinearLayout layoutlist;
    Button add, gallery, camera, proceed;
    Apartment apartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_details);
        Log.d("JAMIL", "Opened intent");
        Intent Cintent = getIntent();
        apartment = new Apartment();
        apartment.owner = Cintent.getStringExtra("name");
        apartment.contactInfo = Cintent.getStringExtra("phone");
        apartment.uid = Cintent.getStringExtra("id");
        Log.d("JAMIL", apartment.uid);
        getSupportActionBar().setTitle("Fill in all the details");
        if(global_variables.BuildingStatus == 2) {
            global_variables.BuildingStatus = 0;
            finish();
        }
        Log.d("JAMIL", "passed data");
        skbed = findViewById(R.id.seekBar_bedroom);
        bed = findViewById(R.id.bedroom_count);
        skbath = findViewById(R.id.seekBar_bathroom);
        bath = findViewById(R.id.bathroom_count);
        t_floors = findViewById(R.id.t_floors);
        rent = findViewById(R.id.rent);
        area = findViewById(R.id.area);
        furniture = findViewById(R.id.furniture);
        layoutlist = findViewById(R.id.layout);
        add = findViewById(R.id.add_floors);
        name = findViewById(R.id.name);
        extra = findViewById(R.id.extra);
        proceed = findViewById(R.id.btn_proceed_to_place);
        skbed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                bed.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        skbath.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                bath.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View floors = getLayoutInflater().inflate(R.layout.available_floor, null, false);
                Button delete = (Button)floors.findViewById(R.id.delete_floor);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        layoutlist.removeView(floors);
                    }
                });
                layoutlist.addView(floors);
                //https://www.youtube.com/watch?v=EJrmgJT2NnI
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verify()) {
                    apartment.name = name.getText().toString();
                    apartment.extra = extra.getText().toString();
                    apartment.bedrooms = Integer.parseInt(bed.getText().toString());
                    apartment.bathrooms = Integer.parseInt(bath.getText().toString());
                    apartment.rent = Integer.parseInt(rent.getText().toString());
                    apartment.totalFloors = Integer.parseInt(t_floors.getText().toString());
                    apartment.area = Double.parseDouble(area.getText().toString());
                    apartment.furniture = furniture.isChecked();
                    Intent ri = new Intent(getApplicationContext(), AddPicture.class);
                    ri.putExtra("apartment", (Parcelable) apartment);
                    Log.d("JAMIL", "going to AddPicture");
                    global_variables.BuildingStatus = 1;
                    startActivity(ri);
                    finish();
                }

            }
        });
    }

    private boolean verify() {
        boolean isValid = true;
        if(name.getText().toString().equals("")){
            isValid = false;
            name.setError("Building name can not be empty");
        }

        if(area.getText().toString().equals("")){
            isValid = false;
            area.setError("Area can not be empty");
        } else {
            if (Double.parseDouble(area.getText().toString()) <= 10) {
                isValid = false;
                area.setError("Area is too small");
            }
            if (Double.parseDouble(area.getText().toString()) > 100000000) {
                isValid = false;
                area.setError("Be realistic");
            }
        }
        if(rent.getText().toString().equals("")){
            isValid = false;
            rent.setError("Rent can not be empty");
        } else {
            if (Integer.parseInt(rent.getText().toString()) > 10000000) {
                isValid = false;
                rent.setError("Be realistic");
            }
            if (Integer.parseInt(rent.getText().toString()) < 10) {
                isValid = false;
                rent.setError("Be realistic");
            }
        }
        if(t_floors.getText().toString().equals("")){
            isValid = false;
            t_floors.setError("Number of floors can not be empty");
        } else {
            if (Integer.parseInt(t_floors.getText().toString()) > 50) {
                isValid = false;
                t_floors.setError("Total floor can not exceed 50 floors");
            }
            if (Integer.parseInt(t_floors.getText().toString()) < 1) {
                isValid = false;
                t_floors.setError("There should be at least one floor");
            }
        }
        int mask=0;
        Log.d("JAMIL", "childs: "+layoutlist.getChildCount());
        for(int i=1; i<layoutlist.getChildCount(); i++) {
            View v = layoutlist.getChildAt(i);
            EditText c_floor = (EditText)v.findViewById(R.id.a_floor);
            int x = Integer.parseInt(c_floor.getText().toString());
            Log.d("JAMIL", "Floor: "+x);
            if(x>Integer.parseInt(t_floors.getText().toString())){
                isValid = false;
                c_floor.setError("Available floor can not exceed total floor");
            }
            int test = mask;
            mask = mask |  (1<<x);
            if(mask == test){
                isValid = false;
                c_floor.setError("Reapeted value found");
            }
        }
        apartment.mask = mask;
        Log.d("JAMIL", "mask: "+mask);

        Log.d("JAMIL", "Varification result: "+isValid);
        return isValid;
    }


}
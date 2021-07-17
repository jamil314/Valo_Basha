package com.example.valo_basha;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class filter_activity extends AppCompatActivity {

    Spinner furniture;
    EditText minBed, minBath, minRent, minArea, maxBed, maxBath, maxRent, maxArea;
    Switch isBed, isBath, isRent, isArea, isFurn;
    Button done;
    Filters filter = global_variables.prev_filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getSupportActionBar().setTitle("Filter");
        minArea = findViewById(R.id.area_min);
        minBath = findViewById(R.id.bathroom_min);
        minBed = findViewById(R.id.bedroom_min);
        minRent = findViewById(R.id.rent_min);
        maxArea = findViewById(R.id.area_max);
        maxBath = findViewById(R.id.bathroom_max);
        maxBed = findViewById(R.id.bedroom_max);
        maxRent = findViewById(R.id.rent_max);
        isArea = findViewById(R.id.sw_area);
        isBath = findViewById(R.id.sw_bath);
        isBed = findViewById(R.id.sw_bed);
        isRent = findViewById(R.id.sw_rent);
        isFurn = findViewById(R.id.sw_furniture);
        furniture = findViewById(R.id.spinner);
        done = findViewById(R.id.filter_confirm);
        reset();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(error()) return;
                gmap.marker_filter(filter);
                global_variables.prev_filter = filter;
                finish();
            }
        });
    }

    private void reset() {
        if(filter.isArea) isArea.setChecked(true);
        if(filter.isBed) isBed.setChecked(true);
        if(filter.isBath) isBath.setChecked(true);
        if(filter.isRent) isRent.setChecked(true);
        if(filter.isFurn) isFurn.setChecked(true);

        if(filter.minArea!=0) minArea.setText(String.valueOf(filter.minArea));
        if(filter.minBed!=0) minBed.setText(String.valueOf(filter.minBed));
        if(filter.minBath!=0) minBath.setText(String.valueOf(filter.minBath));
        if(filter.minRent!=0) minRent.setText(String.valueOf(filter.minRent));

        if(filter.maxArea!=100000000) maxArea.setText(String.valueOf(filter.maxArea));
        if(filter.maxBed!=10) maxBed.setText(String.valueOf(filter.maxBed));
        if(filter.maxBath!=10) maxBath.setText(String.valueOf(filter.maxBath));
        if(filter.maxRent!=10000000) maxRent.setText(String.valueOf(filter.maxRent));

        if(filter.furn) furniture.setSelection(1);
    }

    boolean error(){
        boolean err = false;
        String str;
        if(isArea.isChecked()) {
            filter.isArea = true;

            str = minArea.getText().toString();
            if(str.equals("")) filter.minArea = 0;
            else{
                if (Double.parseDouble(str) < 0) {
                    err = true;
                    minArea.setError("0 - 100000000");
                } else if (Double.parseDouble(str) > 100000000) {
                    err = true;
                    minArea.setError("0 - 100000000");
                } else filter.minArea = Double.parseDouble(str);
            }

            str = maxArea.getText().toString();
            if(str.equals("")) filter.maxArea = 100000000;
            else{
                if (Double.parseDouble(str) < 0) {
                    err = true;
                    maxArea.setError("0 - 100000000");
                } else if (Double.parseDouble(str) > 100000000) {
                    err = true;
                    maxArea.setError("0 - 100000000");
                } else filter.maxArea = Double.parseDouble(str);
            }

        } else filter.isArea = false;

        if(isBed.isChecked()) {
            filter.isBed = true;

            str = minBed.getText().toString();
            if(str.equals("")) filter.minBed = 0;
            else{
                if (Integer.parseInt(str) < 0) {
                    err = true;
                    minBed.setError("0 - 10");
                } else if (Integer.parseInt(str) > 10) {
                    err = true;
                    minBed.setError("0 - 10");
                } else filter.minBed = Integer.parseInt(str);
            }

            str = maxBed.getText().toString();
            if(str.equals("")) filter.maxBed = 10;
            else{
                if (Integer.parseInt(str) < 0) {
                    err = true;
                    maxBed.setError("0 - 10");
                } else if (Integer.parseInt(str) > 10) {
                    err = true;
                    maxBed.setError("0 - 10");
                } else filter.maxBed = Integer.parseInt(str);
            }

        } else filter.isBed = false;

        if(isBath.isChecked()) {
            filter.isBath = true;

            str = minBath.getText().toString();
            if(str.equals("")) filter.minBath = 0;
            else{
                if (Integer.parseInt(str) < 0) {
                    err = true;
                    minBath.setError("0 - 10");
                } else if (Integer.parseInt(str) > 10) {
                    err = true;
                    minBath.setError("0 - 10");
                } else filter.minBath = Integer.parseInt(str);
            }

            str = maxBath.getText().toString();
            if(str.equals("")) filter.maxBath = 10;
            else{
                if (Integer.parseInt(str) < 0) {
                    err = true;
                    maxBath.setError("0 - 10");
                } else if (Integer.parseInt(str) > 10) {
                    err = true;
                    maxBath.setError("0 - 10");
                } else filter.maxBath = Integer.parseInt(str);
            }

        } else filter.isBath = false;

        if(isRent.isChecked()) {
            filter.isRent = true;

            str = minRent.getText().toString();
            if(str.equals("")) filter.minRent = 0;
            else{
                if (Integer.parseInt(str) < 0) {
                    err = true;
                    minRent.setError("0 - 10000000");
                } else if (Integer.parseInt(str) > 10000000) {
                    err = true;
                    minRent.setError("0 - 10000000");
                } else filter.minRent = Integer.parseInt(str);
            }

            str = maxRent.getText().toString();
            if(str.equals("")) filter.maxRent = 10000000;
            else{
                if (Integer.parseInt(str) < 0) {
                    err = true;
                    maxRent.setError("0 - 10000000");
                } else if (Integer.parseInt(str) > 10000000) {
                    err = true;
                    maxRent.setError("0 - 10000000");
                } else filter.maxRent = Integer.parseInt(str);
            }

        } else filter.isRent = false;

        if(isFurn.isChecked()){
            filter.isFurn = true;
            if(furniture.getSelectedItem().equals("With furniture")) filter.furn = true;
            else filter.furn = false;
        } else filter.isFurn = false;

        return err;
    }
}
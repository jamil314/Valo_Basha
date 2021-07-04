package com.example.valo_basha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class NewDetails extends AppCompatActivity {
    SeekBar skbed, skbath;
    TextView bed, bath;
    EditText t_floors;
    LinearLayout layoutlist;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_details);
        getSupportActionBar().setTitle("Fill in all the details");
        skbed = findViewById(R.id.seekBar_bedroom);
        bed = findViewById(R.id.bedroom_count);
        skbath = findViewById(R.id.seekBar_bathroom);
        bath = findViewById(R.id.bathroom_count);
        layoutlist = findViewById(R.id.layout);
        add = findViewById(R.id.add_floors);
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

    }
}
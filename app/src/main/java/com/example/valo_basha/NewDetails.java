package com.example.valo_basha;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
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

public class NewDetails extends AppCompatActivity {
    SeekBar skbed, skbath;
    TextView bed, bath;
    EditText t_floors, rent, area;
    CheckBox furniture;
    LinearLayout layoutlist;
    Button add, gallery, camera, proceed;
    ImageView imageView;
    LinearLayout imagelist;
    Apartment apartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_details);
        Log.d("JAMIL", "Opened intent");
        Intent intent = getIntent();
        apartment = intent.getParcelableExtra("apartment");
        getSupportActionBar().setTitle("Fill in all the details");
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
        gallery = findViewById(R.id.pic_from_gallery);
        camera = findViewById(R.id.pic_from_camera);
        imagelist = findViewById(R.id.image_container);
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
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select picture"), 1);
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apartment.bedrooms = Integer.parseInt(bed.getText().toString());
                apartment.bathrooms = Integer.parseInt(bath.getText().toString());
                apartment.rent = Integer.parseInt(rent.getText().toString());
                apartment.totalFloors = Integer.parseInt(t_floors.getText().toString());
                apartment.area = Double.parseDouble(area.getText().toString());
                apartment.furniture = furniture.isChecked();
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
                Log.d("JAMIL", String.valueOf(mDatabase));
                mDatabase.child(apartment.name).setValue(apartment);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            Log.d("JAMIL", "Picture selected: "+data);
            View image = getLayoutInflater().inflate(R.layout.image, null, false);
            imageView = (ImageView) image.findViewById(R.id.image_view);
            imageView.setImageURI(data.getData());
            imagelist.addView(image);
        }
    }
}
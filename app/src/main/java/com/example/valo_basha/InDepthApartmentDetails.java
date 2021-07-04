package com.example.valo_basha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class InDepthApartmentDetails extends AppCompatActivity {
    int i=0, n=7;
    Button report, right, left, copy, call;
    ImageView imageView;
    int images[] = new int[7];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_depth_apartment_details);
        Intent intent = getIntent();
        Apartment apartment = intent.getParcelableExtra("apartment");
        getSupportActionBar().setTitle(apartment.name);
        getSupportActionBar().setHomeButtonEnabled(true);
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


        report = findViewById(R.id.btn_report);
        right = findViewById(R.id.btn_right);
        left = findViewById(R.id.btn_left);
        copy = findViewById(R.id.btn_copy);
        call = findViewById(R.id.btn_call);
        imageView = findViewById(R.id.image_view);
        images[0]= R.drawable.apartment_entrance;
        images[1]= R.drawable.apartment_with_furniture;
        images[2]= R.drawable.apartment_with_furniture_2;
        images[3]= R.drawable.apartment_with_furniture_3;
        images[4]= R.drawable.apartment_empty;
        images[5]= R.drawable.apartment_empty_2;
        images[6]= R.drawable.apartment_view;
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=(i+1)%n;
                imageView.setImageResource(images[i]);
                Log.d("JAMIL", "i++");
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=(i-1+n)%n;
                imageView.setImageResource(images[i]);
                Log.d("JAMIL", "i--");
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ri = new Intent(getApplicationContext(), Report_activity.class);
                startActivity(ri);
            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("number", mobile_no.getText().toString());
                clipboardManager.setPrimaryClip(clip);
                Toast toast = Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s ="tel:"+apartment.contactInfo;
                Log.d("JAMIL", s);
                Toast toast = Toast.makeText(getApplicationContext(), "Calling "+apartment.owner , Toast.LENGTH_SHORT);
                toast.show();
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse(s));
                startActivity(intent1);
            }
        });
    }
}

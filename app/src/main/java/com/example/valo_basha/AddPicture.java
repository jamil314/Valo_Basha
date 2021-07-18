package com.example.valo_basha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PatternMatcher;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddPicture extends AppCompatActivity {
    Button gallery, camera, cont, current, fromMap;
    ImageView imageView;
    LinearLayout imagelist;
    Apartment apartment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_picture);
        Intent intent = getIntent();
        apartment = intent.getParcelableExtra("apartment");
        Log.d("JAMIL", "inside AddPicture");
        imagelist = findViewById(R.id.image_container);
        gallery = findViewById(R.id.pic_from_gallery);
        camera = findViewById(R.id.pic_from_camera);
        cont = findViewById(R.id.conti);
        current = findViewById(R.id.btn_current);
        fromMap = findViewById(R.id.btn_from_map);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select picture"), 1);
            }
        });


        if(ContextCompat.checkSelfPermission(AddPicture.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AddPicture.this, new String[]{Manifest.permission.CAMERA}, 100);}
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global_variables.buildingX = global_variables.user_location.getLatitude();
                global_variables.buildingY = global_variables.user_location.getLongitude();
            }
        });

        fromMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("JAMIL", global_variables.BuildingStatus+"");
                global_variables.BuildingStatus = 3;
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
            }
        });

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "New Apartment added successfully", Toast.LENGTH_LONG);
                toast.show();
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
                Log.d("JAMIL", String.valueOf(mDatabase));
                mDatabase.child("mandatory_info").child("id").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        int id = Integer.parseInt(task.getResult().getValue().toString());
                        apartment.id = id;
                        apartment.lat = global_variables.buildingX;
                        apartment.lon = global_variables.buildingY;
                        Log.d("JAMIL_Location", apartment.lat+" "+apartment.lon);
                        apartment.address = fetchaddress(apartment.lat, apartment.lon);
                        Log.d("JAMIL_Address", apartment.address);



                        Log.d("JAMIL", "id: "+id);
                        mDatabase.child("mandatory_info").child("id").setValue(id+1);
                        mDatabase.child("mandatory_info").child("owner->id").child(apartment.owner).
                                child(String.valueOf(id)).setValue(id);
                        mDatabase.child("ads").child(id+"").setValue(apartment);
                    }
                });
                mDatabase.child("mandatory_info").child("count").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        int cnt = Integer.parseInt(task.getResult().getValue().toString());
                        mDatabase.child("mandatory_info").child("count").setValue(cnt+1);
                        global_variables.BuildingStatus = 2;
                        Log.d("JAMIL", "cnt: "+cnt);
                    }
                });
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
    }

    private String fetchaddress(double lat, double lon) {
        String adr="";
        Geocoder geocoder = new Geocoder(AddPicture.this, Locale.getDefault());
        try{
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if(addresses!=null){
                Address address = addresses.get(0);
                StringBuilder stringBuilder = new StringBuilder("");
                for(int i=0; i<=address.getMaxAddressLineIndex(); i++){
                    stringBuilder.append(address.getAddressLine(i)).append("\n");
                }
                return stringBuilder.toString();
            }
            else return "No address found";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "No address found";
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            Log.d("JAMIL", "Picture selected: "+data);
            View image = getLayoutInflater().inflate(R.layout.new_picture_unit, null, false);
            imageView = (ImageView) image.findViewById(R.id.image);
            imageView.setImageURI(data.getData());
            Button delete = (Button)image.findViewById(R.id.img_dlt);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imagelist.removeView(image);
                }
            });
            imagelist.addView(image);
        }
        if(requestCode == 100){
            Log.d("JAMIL", "Photo captured: "+data);
            Bitmap captureImage = (Bitmap)data.getExtras().get("data");
            View image = getLayoutInflater().inflate(R.layout.new_picture_unit, null, false);
            imageView = (ImageView) image.findViewById(R.id.image);
            imageView.setImageBitmap(captureImage);
            Button delete = (Button)image.findViewById(R.id.img_dlt);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imagelist.removeView(image);
                }
            });
            imagelist.addView(image);
        }
    }
}
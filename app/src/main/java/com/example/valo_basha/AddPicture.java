package com.example.valo_basha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddPicture extends AppCompatActivity {
    Button gallery, camera, cont, current, fromMap;
    ImageView imageView;
    LinearLayout imagelist;
    Apartment apartment;
    ProgressDialog dialog;
    ArrayList<Uri> urilist = new ArrayList<>();
    String currentPhotoPath;
    Boolean locationPicked = false;
    ProgressBar stall;
    TextView stallMsg;
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
        stall = findViewById(R.id.stall);
        stallMsg = findViewById(R.id.stall_msg);
        stall.setVisibility(View.INVISIBLE);
        stallMsg.setVisibility(View.INVISIBLE);
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
                dispatchTakePictureIntent();
            }
        });

        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationPicked = true;
                global_variables.buildingX = global_variables.user_location.getLatitude();
                global_variables.buildingY = global_variables.user_location.getLongitude();
            }
        });

        fromMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationPicked = true;
                Log.d("JAMIL", global_variables.BuildingStatus+"");
                global_variables.BuildingStatus = 3;
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
            }
        });

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!locationPicked){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Please set a location", Toast.LENGTH_LONG);
                    toast.show();
                    current.setError("set a location");
                    fromMap.setError("set a location");
                    return;
                }
                DatabaseReference mDatabaseAdd;
                mDatabaseAdd = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
                Log.d("JAMIL", String.valueOf(mDatabaseAdd));
                int id = global_variables.id;
                apartment.id = id;
                uploadImage();
                apartment.lat = global_variables.buildingX;
                apartment.lon = global_variables.buildingY;
                apartment.image_count = urilist.size();
                Log.d("JAMIL_Location", apartment.lat+" "+apartment.lon);
                apartment.address = fetchaddress(apartment.lat, apartment.lon);
                Log.d("JAMIL_Address", apartment.address);
                Log.d("JAMIL", "id: "+id);
                mDatabaseAdd.child("mandatory_info").child("id").setValue(id+1);
                mDatabaseAdd.child("users").child(apartment.uid).child("apartment").child(String.valueOf(id)).setValue(id);
                mDatabaseAdd.child("ads").child(id+"").setValue(apartment); //called listener in gMap
                int cnt = global_variables.cnt;
                mDatabaseAdd.child("mandatory_info").child("count").setValue(cnt+1);
                global_variables.BuildingStatus = 2;
                Log.d("JAMIL", "cnt: "+cnt);
                global_variables.id++;
                global_variables.cnt++;
                //finish();
            }
        });
    }

    private void uploadImage() {
        FirebaseStorage storage =FirebaseStorage.getInstance();
        Log.d("JAMIL","Storage: " +String.valueOf(storage));
        int i=0;
        final int[] x = { urilist.size() };
        int y=urilist.size();
        if(y==0){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Successfuly added new apartment", Toast.LENGTH_LONG);
            toast.show();
            finish();
        } else {
            stall.setVisibility(View.VISIBLE);
            stallMsg.setVisibility(View.VISIBLE);
            stallMsg.setText("Uploading image :"+(y-x[0]+1)+"/"+y+"\nPlease wait");

        }
        for(Uri uri:urilist){
            i++;
            StorageReference ref = storage.getReference("img/"+apartment.id+"/"+i);
            ref.putFile(uri).addOnSuccessListener(AddPicture.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("JAMIL", "Upload successfull");
                            x[0]--;
                            if(x[0] == 0) {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Successfuly added new apartment", Toast.LENGTH_LONG);
                                toast.show();
                                finish();
                            } else stallMsg.setText("Uploading image :"+(y-x[0]+1)+"/"+y+"\nPlease wait");

                        }
                    }).addOnFailureListener(AddPicture.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("JAMIL", "Upload not successfull");
                    x[0]--;
                    if(x[0] == 0) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Successfuly added new apartment", Toast.LENGTH_LONG);
                        toast.show();
                        finish();
                    } else stallMsg.setText("Uploading image :"+(y-x[0]+1)+"/"+y+"\nPlease wait");

                }
            });
        }

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
                    urilist.remove(data.getData());
                    imagelist.removeView(image);
                }
            });
            imagelist.addView(image);
            urilist.add((Uri) data.getData());
        }
        if(requestCode == 100){
            Log.d("JAMIL", "Photo captured: ");
            File f = new File(currentPhotoPath);
            View image = getLayoutInflater().inflate(R.layout.new_picture_unit, null, false);
            imageView = (ImageView) image.findViewById(R.id.image);
            imageView.setImageURI(Uri.fromFile(f));
            Button delete = (Button)image.findViewById(R.id.img_dlt);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imagelist.removeView(image);
                    urilist.remove(Uri.fromFile(f));
                }
            });
            imagelist.addView(image);
            urilist.add(Uri.fromFile(f));
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.valo_basha.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 100);
            }
        }
    }
}
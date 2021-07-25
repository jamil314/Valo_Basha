package com.example.valo_basha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileEdit extends AppCompatActivity {
    TextView editPropic, confirmPhone;
    ImageView propic;
    EditText name, email, phone, about;
    String uid, lastPhone="-";
    String currentPhotoPath;
    boolean upload = false;
    Uri uri;
    Button confirm;
    DatabaseReference rtdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);
        editPropic = findViewById(R.id.change_profile_pic);
        propic = findViewById(R.id.profile_pic);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        about = findViewById(R.id.about);
        Intent Cintent = getIntent();
        name.setText(Cintent.getStringExtra("name"));
        lastPhone = Cintent.getStringExtra("phone");
        email.setText(Cintent.getStringExtra("email"));
        about.setText(Cintent.getStringExtra("about"));
        uid = Cintent.getStringExtra("id");
        phone.setText(lastPhone);
        editPropic = findViewById(R.id.change_profile_pic);
        propic = findViewById(R.id.profile_pic);
        confirm = findViewById(R.id.confirm);
        rtdb = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference().child("users").child(uid);

        changePropic(uid);
        editPropic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileEdit.this);
                final View popup = getLayoutInflater().inflate(R.layout.popup_change_propic, null);
                Button remove, camera, gallery;
                remove = popup.findViewById(R.id.remove);
                gallery = popup.findViewById(R.id.gallery);
                camera = popup.findViewById(R.id.camera);
                builder.setView(popup);
                AlertDialog dialog = builder.create();
                dialog.show();

                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rtdb.child("propic").setValue("0");
                        propic.setImageResource(R.drawable.anonymous);
                        upload = false;

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


                if(ContextCompat.checkSelfPermission(ProfileEdit.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ProfileEdit.this, new String[]{Manifest.permission.CAMERA}, 100);}
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dispatchTakePictureIntent();
                    }
                });

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rtdb.child("phone").setValue(phone.getText().toString());
                rtdb.child("extra").setValue(about.getText().toString());
                Log.d("JAMIL", rtdb.toString());
                if(upload){
                    FirebaseStorage storage =FirebaseStorage.getInstance();
                    StorageReference ref = storage.getReference("propics").child(uid);
                    ref.putFile(uri).addOnSuccessListener(ProfileEdit.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            rtdb.child("propic").setValue("1");
                            Log.d("JAMIL", "Upload successfull");
                            finish();

                        }
                    }).addOnFailureListener(ProfileEdit.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("JAMIL", "Upload not successfull");
                            finish();
                        }
                    });
                } else finish();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            Log.d("JAMIL", "Picture selected: "+data);
            propic.setImageURI(data.getData());
            uri = data.getData();
            upload = true;
        }
        if(requestCode == 100){
            Log.d("JAMIL", "Photo captured: ");
            File f = new File(currentPhotoPath);
            propic.setImageURI(Uri.fromFile(f));
            uri = Uri.fromFile(f);
            upload = true;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,   // prefix
                ".jpg",    // suffix
                storageDir      // directory
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
                photoFile = null;
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

    private void changePropic(String userId) {
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference().child("users").child(userId).child("propic");
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().getValue().toString().equals("0")){
                    propic.setImageResource(R.drawable.anonymous);
                } else {
                    Log.d("JAMIL", "Photo download request sent");
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    StorageReference storage = FirebaseStorage.getInstance().getReference().child("propics").child(userId);
                    try {
                        final File file = File.createTempFile(timeStamp, userId);
                        storage.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.d("JAMIL", "Photo download successful");
                                Bitmap bt = BitmapFactory.decodeFile(file.getAbsolutePath());
                                propic.setImageBitmap(bt);
                                //propic.setImageURI(Uri.fromFile(file));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("JAMIL", "Photo download failed:"+e);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }


}
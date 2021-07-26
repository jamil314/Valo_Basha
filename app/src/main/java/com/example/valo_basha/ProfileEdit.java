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
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
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
import java.util.concurrent.TimeUnit;

public class ProfileEdit extends AppCompatActivity {
    TextView editPropic, confirmPhone;
    ImageView propic;
    EditText name, phone, about, otp;
    String uid;
    String currentPhotoPath;
    boolean upload = false;
    Uri uri;
    Button confirm, otp_confirm;
    DatabaseReference rtdb;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);
        Log.d("JAMIL", "editing");
        auth = FirebaseAuth.getInstance();
        editPropic = findViewById(R.id.change_profile_pic);
        propic = findViewById(R.id.profile_pic);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        confirmPhone = findViewById(R.id.change_phone);
        otp = findViewById(R.id.otp);
        otp_confirm = findViewById(R.id.otp_confirm);
        about = findViewById(R.id.about);
        editPropic = findViewById(R.id.change_profile_pic);
        propic = findViewById(R.id.profile_pic);
        confirm = findViewById(R.id.confirm);
        uid = auth.getUid();
        rtdb = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference().child("users").child(uid);
        Log.d("JAMIL", rtdb.toString());
        changePropic(uid);
        rtdb.child("phone").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().exists()){
                    LinearLayout layout = findViewById(R.id.phone_layout);
                    layout.removeAllViews();
                    confirmPhone.setVisibility(View.INVISIBLE);
                } else {
                    Log.d("JAMIL", "no number");
                }
            }
        });

        rtdb.child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                name.setText(task.getResult().getValue().toString());
            }
        });

        rtdb.child("extra").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                about.setText(task.getResult().getValue().toString());
            }
        });

        otp_confirm.setVisibility(View.INVISIBLE);
        otp.setVisibility(View.INVISIBLE);

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
                        rtdb.child("propic").removeValue();
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
                rtdb.child("name").setValue(name.getText().toString());
                rtdb.child("extra").setValue(about.getText().toString());
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

        confirmPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String no = phone.getText().toString();
                if(no.length() == 11) no = "+88"+no;
                String finalNo = no;
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(no).setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(ProfileEdit.this).setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Phone verification successful", Toast.LENGTH_LONG);
                                toast.show();
                                LinearLayout layout = findViewById(R.id.phone_layout);
                                layout.removeAllViews();
                                confirmPhone.setVisibility(View.INVISIBLE);
                                rtdb.child("phone").setValue(finalNo);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
                                toast.show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        otp.setVisibility(View.VISIBLE);
                                        otp_confirm.setVisibility(View.VISIBLE);
                                        String OTP = otp.getText().toString();
                                        if(otp.equals(s)) {
                                            Toast toast = Toast.makeText(getApplicationContext(),
                                                    "Phone verification successful", Toast.LENGTH_LONG);
                                            toast.show();
                                            LinearLayout layout = findViewById(R.id.phone_layout);
                                            layout.removeAllViews();
                                            confirmPhone.setVisibility(View.INVISIBLE);
                                            rtdb.child("phone").setValue(finalNo);
                                        }
                                    }
                                }, 10000);
                            }
                        }).build();
                PhoneAuthProvider.verifyPhoneNumber(options);
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
                if(!task.getResult().exists()){
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
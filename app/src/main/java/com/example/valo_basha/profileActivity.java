package com.example.valo_basha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class profileActivity extends AppCompatActivity {

    Button refresh, edit, add, list, fav, logout, reports;
    TextView name, email, phone, about, nullUser, login, signup;
    ImageView propic;
    FirebaseAuth fAuth;
    FirebaseUser user;
    ProgressBar stall, progressBar;
    String last;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        last = intent.getStringExtra("last");
        Log.d("JAMIL", "From: "+last);

        stall = findViewById(R.id.stall);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        about = findViewById(R.id.about);
        nullUser = findViewById(R.id.textView6);
        login = findViewById(R.id.sign_in);
        signup = findViewById(R.id.sign_up);
        edit = findViewById(R.id.btn_ep);
        add = findViewById(R.id.btn_add);
        list = findViewById(R.id.btn_list);
        fav = findViewById(R.id.btn_fav);
        refresh = findViewById(R.id.btn_refresh);
        logout = findViewById(R.id.btn_logout);
        propic = findViewById(R.id.profile_pic);
        reports = findViewById(R.id.btn_reports);
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        if(user == null){
            stall.setVisibility(View.INVISIBLE);
            getSupportActionBar().setTitle("Anonymous");
            propic.setImageResource(R.drawable.anonymous);
            nullUser.setVisibility(View.VISIBLE);
            login.setVisibility(View.VISIBLE);
            signup.setVisibility(View.VISIBLE);
            edit.setVisibility(View.INVISIBLE);
            add.setVisibility(View.INVISIBLE);
            list.setVisibility(View.INVISIBLE);
            fav.setVisibility(View.INVISIBLE);
            logout.setVisibility(View.INVISIBLE);
            reports.setVisibility(View.INVISIBLE);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), emailLogin.class));
                }
            });

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), emailReg.class));
                }
            });

        } else {
            nullUser.setVisibility(View.INVISIBLE);
            login.setVisibility(View.INVISIBLE);
            signup.setVisibility(View.INVISIBLE);
            edit.setVisibility(View.VISIBLE);
            add.setVisibility(View.VISIBLE);
            list.setVisibility(View.VISIBLE);
            fav.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
            reports.setVisibility(View.VISIBLE);
            String userId = user.getUid();

            user_info(userId);

            changePropic(userId);

            Log.d("JAMIL", String.valueOf(user.isEmailVerified()));

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ProfileEdit.class);
                    startActivity(intent);
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), NewDetails.class);
                    intent.putExtra("last", "profile");
                    startActivity(intent);
                }
            });

            list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), apartment_list.class);
                    intent.putExtra("key", userId);
                    startActivity(intent);
                }
            });



            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    final View popup = getLayoutInflater().inflate(R.layout.popup_two_option, null);

                    TextView text = popup.findViewById(R.id.text);
                    Button bt_email = popup.findViewById(R.id.op1);
                    TextView bt_phone = popup.findViewById(R.id.op2);
                    bt_email.setText("Cancel");
                    bt_phone.setText("Log out");
                    text.setText("  Are you sure?");

                    builder.setView(popup);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    bt_phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseAuth.getInstance().signOut();
                            Intent ri = new Intent(getApplicationContext(), profileActivity.class);
                            ri.putExtra("last", last);
                            startActivity(ri);
                            finish();
                            dialog.dismiss();
                        }
                    });
                    bt_email.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
            });
            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), listFavs.class);
                    startActivity(intent);
                }
            });
            reports.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ri = new Intent(getApplicationContext(), profileActivity.class);
                ri.putExtra("last", last);
                startActivity(ri);
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
        if(last.equals("skip")) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            finish();
        }
    }

    void user_info(String uid){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference().child("users").child(uid);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("name").getValue(String.class));
                email.setText(snapshot.child("email").getValue(String.class));
                phone.setText(snapshot.child("phone").getValue(String.class));
                about.setText(snapshot.child("extra").getValue(String.class));
                getSupportActionBar().setTitle(name.getText());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                    stall.setVisibility(View.INVISIBLE);
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
                                stall.setVisibility(View.INVISIBLE);
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
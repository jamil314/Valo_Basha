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
import android.widget.TextView;

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

    Button refresh, edit, add, list, fav, logout;
    TextView name, email, phone, about, nullUser, login, signup;
    ImageView propic;
    FirebaseAuth fAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
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
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        if(user == null){
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
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder login_popup = new AlertDialog.Builder(view.getContext());
                    login_popup.setTitle("Log in using:");

                    login_popup.setNegativeButton("Email", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(), emailLogin.class));
                        }
                    });

                    login_popup.setPositiveButton("Phone", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //startActivity(new Intent(getApplicationContext(), Login_Register.class));
                        }
                    });
                    login_popup.create().show();
                }
            });

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder signup_popup = new AlertDialog.Builder(view.getContext());
                    signup_popup.setTitle("Sign up using:");

                    signup_popup.setNegativeButton("Email", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(), emailReg.class));
                        }
                    });

                    signup_popup.setPositiveButton("Phone", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //startActivity(new Intent(getApplicationContext(), Login_Register.class));
                        }
                    });
                    signup_popup.create().show();
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
            String userId = user.getUid();

            user_info(userId);

            changePropic(userId);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ProfileEdit.class);
                    intent.putExtra("name", name.getText());
                    intent.putExtra("phone", phone.getText());
                    intent.putExtra("email", email.getText());
                    intent.putExtra("about", about.getText());
                    intent.putExtra("id", userId);
                    startActivity(intent);
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), NewDetails.class);
                    intent.putExtra("name", name.getText());
                    intent.putExtra("phone", phone.getText());
                    intent.putExtra("id", userId);
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

        }

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                recreate();
            }
        });
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
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
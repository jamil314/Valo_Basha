package com.example.valo_basha;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class profileActivity extends AppCompatActivity {

    Button refresh, edit, add, list, fav, logout;
    TextView name, email, phone, about, nullUser, login, signup;
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
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        if(user == null){
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
}
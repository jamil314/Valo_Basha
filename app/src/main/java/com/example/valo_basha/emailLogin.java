package com.example.valo_basha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class emailLogin extends AppCompatActivity {
    EditText email, pass;
    Button login;
    TextView forgot, phone, signup, signupPhone, msg;
    ProgressBar stall;
    FirebaseAuth fAuth;
    FirebaseUser user;
    ImageView icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        getSupportActionBar().hide();
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        login = findViewById(R.id.login);
        forgot = findViewById(R.id.forgot);
        signup = findViewById(R.id.sign_up);
        stall = findViewById(R.id.stall);
        msg = findViewById(R.id.stallMsg);
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        if(user!=null){
            Log.d("JAMIL", user.getUid());
            startActivity(new Intent(emailLogin.this, profileActivity.class));
            finish();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString();
                String Pass = pass.getText().toString();
                if(TextUtils.isEmpty(Email)){
                    email.setError("Email is Required.");
                    return;
                }
                if(Pass.length() < 6){
                    pass.setError("Password Must be at least 6 Characters");
                    return;
                }
                Log.d("JAMIL", "Login req sent");
                email.setVisibility(View.INVISIBLE);
                pass.setVisibility(View.INVISIBLE);
                login.setVisibility(View.INVISIBLE);
                forgot.setVisibility(View.INVISIBLE);
                signup.setVisibility(View.INVISIBLE);
                stall.setVisibility(View.VISIBLE);
                msg.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(emailLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("JAMIL", "Login Successfull");
                            Toast.makeText(emailLogin.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(emailLogin.this, profileActivity.class));
                            finish();
                        }
                        else{
                            email.setVisibility(View.VISIBLE);
                            pass.setVisibility(View.VISIBLE);
                            login.setVisibility(View.VISIBLE);
                            forgot.setVisibility(View.VISIBLE);
                            signup.setVisibility(View.VISIBLE);
                            stall.setVisibility(View.INVISIBLE);
                            msg.setVisibility(View.INVISIBLE);
                            Log.d("JAMIL", "Login failed: "+task.getException().getMessage());
                            Toast.makeText(emailLogin.this, "Login failed: "+task.getException().getMessage()+".Try again", Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                final View popup = getLayoutInflater().inflate(R.layout.popup_pw_reset, null);
                EditText text = popup.findViewById(R.id.email);
                Button bt_email = popup.findViewById(R.id.op1);
                TextView bt_phone = popup.findViewById(R.id.op2);

                builder.setView(popup);
                AlertDialog dialog = builder.create();
                dialog.show();
                bt_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mail = text.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(emailLogin.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(emailLogin.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
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
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), emailReg.class));
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(emailLogin.this, profileActivity.class));
        finish();
    }

}
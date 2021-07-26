package com.example.valo_basha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class emailReg extends AppCompatActivity {
    EditText email, pass, con, name;
    Button signup;
    TextView phone, login;
    FirebaseAuth fAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_reg);
        getSupportActionBar().hide();
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        con = findViewById(R.id.confirm);
        signup = findViewById(R.id.sign_up);
        phone = findViewById(R.id.phone);
        login = findViewById(R.id.login);
        name = findViewById(R.id.name);
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        if(user!=null){
            Log.d("JAMIL", user.getUid());
            startActivity(new Intent(emailReg.this, profileActivity.class));
            finish();
        }
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString();
                String Pass = pass.getText().toString();
                String Con = con.getText().toString();
                String Name = name.getText().toString();
                if(TextUtils.isEmpty(Email)){
                    email.setError("Email is Required.");
                    return;
                }
                if(Pass.length() < 6){
                    pass.setError("Password Must be at least 6 Characters");
                    return;
                }
                if(!Pass.equals(Con)){
                    con.setError("Password does not match");
                    return;
                }
                Log.d("JAMIL", "Reg req sent");
                fAuth = FirebaseAuth.getInstance();
                fAuth.createUserWithEmailAndPassword(Email, Pass).addOnCompleteListener(emailReg.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("JAMIL", "tsk completed");
                        if(task.isSuccessful()){
                            Log.d("JAMIL", "successfull");
                            FirebaseUser fUser = fAuth.getCurrentUser();
                            Log.d("JAMIL", fUser.getUid());
                            fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("JAMIL", "Verification Email Has been Sent.");
                                    Toast.makeText(emailReg.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                    String uid = fUser.getUid();
                                    DatabaseReference mDatabase;
                                    mDatabase = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                            .getReference().child("users").child(uid);
                                    mDatabase.child("email").setValue(Email);
                                    mDatabase.child("name").setValue(Name);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("JAMIL", "Reg failed: "+e.getMessage());
                                    Toast.makeText(emailReg.this, "Email sending failed: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            Log.d("JAMIL", "task error: "+task.getException().getMessage());
                            Toast.makeText(emailReg.this, "Failed: "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), emailLogin.class));
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
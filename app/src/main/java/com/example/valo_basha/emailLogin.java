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
    TextView forgot, phone, signup, signupPhone;
    FirebaseAuth fAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        login = findViewById(R.id.login);
        forgot = findViewById(R.id.forgot);
        phone = findViewById(R.id.phone);
        signup = findViewById(R.id.sign_up);
        signupPhone = findViewById(R.id.sign_up_phone);
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
                fAuth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(emailLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("JAMIL", "Login Successfull");
                            Toast.makeText(emailLogin.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(emailLogin.this, profileActivity.class));
                        }
                        else{
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
                final EditText resetMail = new EditText(view.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetMail);
                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
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

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();
            }
        });
    }
}
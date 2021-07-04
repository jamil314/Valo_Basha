package com.example.valo_basha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LogIn extends AppCompatActivity {

    Button btn;
    TextView number;
    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getSupportActionBar().setTitle("Log in");
        Log.d("JAMIL", "login_called");

        number = findViewById(R.id.phn_number);
        btn = findViewById(R.id.btn);
        auth = FirebaseAuth.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("JAMIL", "Phone no given: "+number.getText());
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(number.getText().toString())
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(LogIn.this)
                        .setCallbacks(callbacks)
                        .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d("JAMIL", "Verification Completed");
                signIn(phoneAuthCredential);
                //finish();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d("JAMIL", "Verification Failed");
                Toast toast = Toast.makeText(getApplicationContext(), "Log in failed", Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {*/
                        Log.d("JAMIL", "Code sent: "+s);
                        Intent otpIntent = new Intent(LogIn.this, OTP.class);
                        otpIntent.putExtra("auth", s);
                        startActivity(otpIntent);
                   /* }
                }, 10000);*/
            }
        };

    }

    private void signIn(PhoneAuthCredential credential){
         auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 if(task.isSuccessful()){
                     Log.d("JAMIL", "task is successfull");
                     finish();
                 } else {
                     Log.d("JAMIL", "Task failed");
                     Toast toast = Toast.makeText(getApplicationContext(), "Log in failed", Toast.LENGTH_LONG);
                     toast.show();
                 }
             }
         });
    }


}
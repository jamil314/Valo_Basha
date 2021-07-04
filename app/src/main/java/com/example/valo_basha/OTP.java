package com.example.valo_basha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTP extends AppCompatActivity {
    TextView otp;
    Button btn;
    String OTP;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        getSupportActionBar().setTitle("Confirm OTP");

        auth = FirebaseAuth.getInstance();
        btn = findViewById(R.id.btn_otp_confirm);
        otp = findViewById(R.id.otp_code);
        OTP = getIntent().getStringExtra("auth");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTP, otp.getText().toString());
                signIn(credential);
            }
        });
    }
    private void signIn(PhoneAuthCredential credential){
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("JAMIL", "login successfull");
                    finish();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Log in failed", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}
package com.example.valo_basha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.opengl.ETC1;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class confirm_phone extends AppCompatActivity {

    boolean otpSent = false;
    EditText phone;
    FirebaseAuth auth;
    Button get_otp, confirm_otp;
    EditText otp;
    DatabaseReference rtdb;
    FirebaseUser user;
    String uid;
    TextView mock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_phone);
        getSupportActionBar().hide();
        phone = findViewById(R.id.phone);
        mock = findViewById(R.id.mock_phone);
        get_otp = findViewById(R.id.get_otp);
        confirm_otp = findViewById(R.id.confirm_otp);
        otp = findViewById(R.id.otp);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        uid = user.getUid();
        rtdb = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference().child("users").child(uid);
        get_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otpSent) return;
                phone.setVisibility(View.INVISIBLE);
                mock.setVisibility(View.VISIBLE);
                otpSent = true;
                String no = phone.getText().toString();
                mock.setText(no);
                if(no.length() == 11) no = "+88"+no;
                String finalNo = no;
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(no).setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(confirm_phone.this).setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Phone verification successful", Toast.LENGTH_LONG);
                                toast.show();
                                rtdb.child("phone").setValue(finalNo);
                                finish();
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
                                        confirm_otp.setVisibility(View.VISIBLE);
                                        otp.setVisibility(View.VISIBLE);
                                        String OTP = otp.getText().toString();

                                        confirm_otp.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if(otp.equals(s)) {
                                                    Toast toast = Toast.makeText(getApplicationContext(),
                                                            "Phone verification successful", Toast.LENGTH_LONG);
                                                    toast.show();
                                                    rtdb.child("phone").setValue(finalNo);
                                                    finish();
                                                } else{
                                                    Toast toast = Toast.makeText(getApplicationContext(),
                                                            "Phone verification failed\nWrong OTP", Toast.LENGTH_LONG);
                                                    toast.show();
                                                }

                                            }
                                        });


                                    }
                                }, 10000);
                            }
                        }).build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
/*

        String no = phone.getText().toString();
        if(no.length() == 11) no = "+88"+no;
        String finalNo = no;
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(no).setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(confirm_phone.this).setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Phone verification successful", Toast.LENGTH_LONG);
                        toast.show();
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

*/


    }
}
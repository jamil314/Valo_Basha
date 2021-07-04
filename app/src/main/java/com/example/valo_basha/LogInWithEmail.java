package com.example.valo_basha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInWithEmail extends AppCompatActivity {
    EditText name, email1, pw1, email2, pw2;
    Button reg, login;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_with_email);
        getSupportActionBar().setTitle("Log in / Register");

        name = findViewById(R.id.name_reg);
        email1 = findViewById(R.id.email_reg);
        pw1 = findViewById(R.id.pw_reg);
        email2 = findViewById(R.id.email_login);
        pw2 = findViewById(R.id.pw_login);
        reg = findViewById(R.id.btn_reg);
        login = findViewById(R.id.btn_login);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        Log.d("JAMIL", String.valueOf(fAuth.getCurrentUser()));
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("JAMIL", "register: /"+email1.getText().toString()+"/  /"+pw1.getText().toString()+"/");
                progressBar.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(email1.getText().toString(), pw1.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull  Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    Log.d("JAMIL", "Successfull");
                                    finish();
                                }else{
                                    Log.d("JAMIL", "unsuccessfull");
                                }
                            }
                        });
            }
        });
    }
}
package com.example.valo_basha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class login extends AppCompatActivity implements View.OnClickListener {
    private Button login;
    private EditText email, password;
    private TextView msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.loginButton_id);
        email = findViewById(R.id.email_id);
        password = findViewById(R.id.password_id);
        msg = findViewById(R.id.msg_id);
        login.setOnClickListener(this);
        msg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.loginButton_id){

        }
        else{
            Intent intent = new Intent(getApplicationContext(), registerActivity.class);
            startActivity(intent);
        }
    }
}
package com.example.valo_basha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        Log.d("JAMIL", "Splash Screen");

        initialize();





        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }, 3000);
    }

    private void initialize() {
        DatabaseReference mDatabaseAdd;
        mDatabaseAdd = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        Log.d("JAMIL", String.valueOf(mDatabaseAdd));
        mDatabaseAdd.child("mandatory_info").child("id").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("JAMIL", "Id accessed");
                if(global_variables.idDone) return;
                global_variables.idDone = true;
                Log.d("JAMIL", task.isSuccessful()+"");
                global_variables.id = Integer.parseInt(task.getResult().getValue().toString());
                Log.d("JAMIL", "Id: "+global_variables.id);
            }
        });
        mDatabaseAdd.child("mandatory_info").child("count").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("JAMIL", "Cnt accessed");
                if(global_variables.cntDone) return;
                global_variables.cntDone = true;
                global_variables.cnt = Integer.parseInt(task.getResult().getValue().toString());
                Log.d("JAMIL", "Cnt: "+global_variables.cnt);
            }
        });
    }
}
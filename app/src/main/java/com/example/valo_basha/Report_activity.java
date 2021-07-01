package com.example.valo_basha;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Report_activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getSupportActionBar().setTitle("Report");
        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("JAMIL", "reported");
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Your report has been recorded\nThanks for bringing it to our attention", Toast.LENGTH_LONG);
                toast.show();
                finish();
            }
        });
    }
}
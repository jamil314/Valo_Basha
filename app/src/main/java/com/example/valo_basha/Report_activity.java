package com.example.valo_basha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.opengl.ETC1;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Report_activity extends AppCompatActivity {
    CheckBox cb1, cb2, cb3, cb4, cb5;
    Reports report = new Reports();
    EditText name, contact, messege;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        getSupportActionBar().setTitle("Report");
        Button submit = findViewById(R.id.submit);
        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
        cb5 = findViewById(R.id.cb5);
        name = findViewById(R.id.reporter_name);
        contact = findViewById(R.id.reporter_contact);
        messege = findViewById(R.id.report_message);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(invalid()) return;
                Log.d("JAMIL", "reported "+id);
                report.building_id = id;
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference().child("Reports");
                Log.d("JAMIL", String.valueOf(mDatabase));
                mDatabase.push().setValue(report);

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Your report has been recorded\nThanks for bringing it to our attention", Toast.LENGTH_LONG);
                toast.show();
                finish();
            }
        });
    }

    private boolean invalid() {
        if(cb1.isChecked()) report.report = report.report +"Vulger/abusive name\n";
        if(cb2.isChecked()) report.report = report.report +"Vulger/abusive picture\n";
        if(cb3.isChecked()) report.report = report.report +"Demanding more rent than mentioned\n";
        if(cb4.isChecked()) report.report = report.report +"Fake data\n";
        if(cb5.isChecked()) report.report = report.report +"Apartment not available\n";
        report.report = report.report +messege.getText();
        report.name = String.valueOf(name.getText());
        report.contact = String.valueOf(contact.getText());
        if (report.name.equals("")) report.name = "Anonymous";
        if(report.report == "") {
            cb1.setError("At least one report statement required");
            cb2.setError("At least one report statement required");
            cb3.setError("At least one report statement required");
            cb4.setError("At least one report statement required");
            cb5.setError("At least one report statement required");
            messege.setError("At least one report statement required");
            return true;
        }
        return false;
    }
}
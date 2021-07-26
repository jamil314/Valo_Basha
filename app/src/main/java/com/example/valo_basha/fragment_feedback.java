package com.example.valo_basha;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class fragment_feedback extends Fragment {
    Button mail, post;
    EditText text;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        mail = view.findViewById(R.id.btn_mail);
        post = view.findViewById(R.id.btn_post);
        text = view.findViewById(R.id.editTextTextMultiLine);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:jamil31415926@gmail.com"));
                startActivity(intent);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    final View popup = getLayoutInflater().inflate(R.layout.popup_two_option, null);

                    TextView text = popup.findViewById(R.id.text);
                    Button yes = popup.findViewById(R.id.op1);
                    TextView no = popup.findViewById(R.id.op2);
                    yes.setText("Log In");
                    no.setText("Anonymous");
                    text.setText("You are not logged in.");

                    builder.setView(popup);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent ri = new Intent(getActivity(), profileActivity.class);
                            startActivity(ri);
                            dialog.dismiss();
                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatabaseReference mDatabase;
                            mDatabase = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                    .getReference().child("Feedbacks");
                            String msg = text.getText().toString();
                            mDatabase.push().setValue("Anonymous:\n"+msg);
                            Toast toast = Toast.makeText(getActivity(),
                                    "Your feedback has been recorded", Toast.LENGTH_LONG);
                            toast.show();
                            dialog.dismiss();
                        }
                    });
                } else {
                    DatabaseReference mDatabase;
                    mDatabase = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            .getReference().child("Feedbacks");
                    String msg = text.getText().toString();
                    mDatabase.push().setValue(user.getUid()+":\n"+msg);
                    Toast toast = Toast.makeText(getActivity(),
                            "Your feedback has been recorded", Toast.LENGTH_LONG);
                    toast.show();

                }
            }
        });


        return view;
    }
}
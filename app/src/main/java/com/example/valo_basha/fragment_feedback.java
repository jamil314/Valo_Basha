package com.example.valo_basha;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class fragment_feedback extends Fragment {
    Button mail, passMsg, text;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        mail = view.findViewById(R.id.btn_mail);
        text = view.findViewById(R.id.btn_text);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:jamil31415926@gmail.com"));
                startActivity(intent);
            }
        });

        return view;
    }
}
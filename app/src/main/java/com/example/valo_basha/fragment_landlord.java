package com.example.valo_basha;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_landlord#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_landlord extends Fragment {
    private FirebaseAuth fAuth;
    Button proceed, confirm;
    EditText building_name, name, phn_no, otp;
    TextView msg;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_landlord() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_landlord.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_landlord newInstance(String param1, String param2) {
        fragment_landlord fragment = new fragment_landlord();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landlord, container, false);
        building_name = view.findViewById(R.id.building_name);
        name = view.findViewById(R.id.name);
        phn_no = view.findViewById(R.id.phone_no);
        otp = view.findViewById(R.id.otp);
        msg = view.findViewById(R.id.msg);
        proceed = view.findViewById(R.id.btn_proceed);
        confirm = view.findViewById(R.id.btn_otp);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp.setVisibility(View.VISIBLE);
                confirm.setVisibility(View.VISIBLE);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //msg.setVisibility(View.VISIBLE);
                Intent intent = new Intent(getActivity(), NewDetails.class);
                //intent.putExtra("name", building_name.getText().toString());
                //intent.putExtra("owner", name.getText().toString());
                //intent.putExtra("phn", phn_no.getText().toString());
                startActivity(intent);
            }
        });

        return view;
    }
}
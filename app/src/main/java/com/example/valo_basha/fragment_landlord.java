package com.example.valo_basha;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
  /*  private Button login;
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
    }*/
    //////********************************************************************////////////////////////////////////////////////////////////
    private FirebaseAuth fAuth;
    private Button login;
    private EditText email, password;
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
        email = (EditText) view.findViewById(R.id.email_id);
        password = view.findViewById(R.id.password_id);
       // msg = view.findViewById(R.id.msg);
        login = view.findViewById(R.id.loginButton_id);
      //  name = (EditText) view.findViewById(R.id.name);
      //  phn_no = (EditText) view.findViewById(R.id.phn_no);
    //    otp = view.findViewById(R.id.otp);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        msg.setOnClickListener(new View.OnClickListener(){
            @Override
                    public  void onClick(View view){
                
            }
        });

        return view;
    }
}
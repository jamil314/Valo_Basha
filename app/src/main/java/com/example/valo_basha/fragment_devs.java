package com.example.valo_basha;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_devs#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_devs extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_devs() {
        Log.d("JAMIL", "Devs -> ()");
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_devs.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_devs newInstance(String param1, String param2) {
        Log.d("JAMIL", "Devs -> (;;)");
        fragment_devs fragment = new fragment_devs();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("JAMIL", "Devs -> onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_devs, container, false);

        TextView text = view.findViewById(R.id.text);
        ImageView image = view.findViewById(R.id.image);
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference().child("dev").child("info");
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                text.setText(task.getResult().getValue().toString());
            }
        });

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        StorageReference storage = FirebaseStorage.getInstance().getReference().child("dev").child("jamil.jpg");
        try {
            final File file = File.createTempFile(timeStamp, "jamil");
            storage.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d("JAMIL", "Photo download successful");
                    Bitmap bt = BitmapFactory.decodeFile(file.getAbsolutePath());
                    image.setImageBitmap(bt);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("JAMIL", "Photo download failed:"+e);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


        return view;
    }
}
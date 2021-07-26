package com.example.valo_basha;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("JAMIL", "MainActivity -> onCreate");
        //FirebaseAuth.getInstance().signOut();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.setId(R.id.nav_login);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //navigationView.setCheckedItem(2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_maps, R.id.nav_feedback, R.id.nav_tutorial, R.id.nav_login, R.id.gmap, R.id.nav_developers, R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //selectItem(1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("JAMIL", "MainActivity -> onCreateOptionMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener( new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d("JAMIL", "MainActivity onAuthStateChange");
                FirebaseUser fuser = firebaseAuth.getCurrentUser();
                if(fuser!=null){
                    changeIcon(fuser.getUid(), menu.getItem(0));
                } else menu.getItem(0).setIcon(R.drawable.anonymous);
            }
        });
        FirebaseUser user = auth.getCurrentUser();
        if(user!=null){
            changeIcon(user.getUid(), menu.getItem(0));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_profile:
                Intent ri = new Intent(MainActivity.this, profileActivity.class);
                ri.putExtra("last", "main");
                startActivity(ri);
                break;
        }
        return true;
    }

    private void changeIcon(String uid, MenuItem item) {
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference().child("users").child(uid).child("propic");
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().exists()){
                    Log.d("JAMIL", "Photo download request sent");
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    StorageReference storage = FirebaseStorage.getInstance().getReference().child("propics").child(uid);
                    try {
                        final File file = File.createTempFile(timeStamp, uid);
                        storage.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.d("JAMIL", "Photo download successful");
                                Bitmap bt = BitmapFactory.decodeFile(file.getAbsolutePath());
                                item.setIcon(new BitmapDrawable(getResources(), bt));
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

                } else {
                    item.setIcon(R.drawable.anonymous);
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        Log.d("JAMIL", "MainActivity -> onSupportNavigateUp");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void selectItem(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        switch (position) {
            case 0:
                ft.replace(R.id.fragmentContainerView, new map_container()).commit();
                break;


            case 1:
                ft.replace(R.id.mobile_navigation, new fragment_login()).commit();
                break;

        }
        /*mDrawerList.setItemChecked(position, true);
        //setTitle(title[position]);
        mDrawerLayout.closeDrawer(mDrawerList);*/
    }
}
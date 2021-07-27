package com.example.valo_basha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InDepthApartmentDetails extends AppCompatActivity {
    int i=0, n=7;
    Button report, right, left, copy, call;
    ImageView imageView, propic;
    LinearLayout imagelist;
    Apartment apartment;
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    String key;
    ProgressBar progressBar, stall;
    TextView load, name, email, phone, about;
    boolean isFav = false;
    DatabaseReference mDatabase;
    FirebaseUser user;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_manu, menu);
        if(!key.equals("tenent")) {
            menu.getItem(0).setVisible(false);
            menu.getItem(2).setVisible(false);
        }
        else{
            menu.getItem(1).setVisible(false);
            menu.getItem(3).setVisible(false);

            user = FirebaseAuth.getInstance().getCurrentUser();
            if(user == null){

            } else {
                mDatabase = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference().child("users").child(user.getUid()).child("fav_list").child(String.valueOf(apartment.id));
                mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.getResult().exists())
                            menu.getItem(0).setIcon(R.drawable.ic_fav_true);
                    }
                });
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btn_fav:
                user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(InDepthApartmentDetails.this);
                    final View popup = getLayoutInflater().inflate(R.layout.popup_two_option, null);

                    TextView text = popup.findViewById(R.id.text);
                    Button yes = popup.findViewById(R.id.op1);
                    TextView no = popup.findViewById(R.id.op2);
                    yes.setText("Yes");
                    no.setText("No");
                    text.setText("You need to be logged in before you can add/remove favourites.\n  Do you want to log in?");

                    builder.setView(popup);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent ri = new Intent(getApplicationContext(), profileActivity.class);
                            startActivity(ri);
                            dialog.dismiss();
                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    mDatabase = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            .getReference().child("users").child(user.getUid()).child("fav_list").child(String.valueOf(apartment.id));
                    mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(task.getResult().exists()) {
                                item.setIcon(R.drawable.ic_fav_false);
                                mDatabase.removeValue();


                                DatabaseReference mDatabase2;
                                mDatabase2 = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                        .getReference().child("favourites").child(String.valueOf(apartment.id)).child(user.getUid());
                                mDatabase2.removeValue();


                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Removed from favourites", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                item.setIcon(R.drawable.ic_fav_true);
                                mDatabase.setValue(apartment.id);

                                DatabaseReference mDatabase2;
                                mDatabase2 = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                        .getReference().child("favourites").child(String.valueOf(apartment.id)).child(user.getUid());
                                mDatabase2.setValue(user.getUid());


                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Added to favourites", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    });
                }
                break;
            case R.id.btn_report:
                Intent ri = new Intent(getApplicationContext(), Report_activity.class);
                ri.putExtra("id", apartment.id);
                startActivity(ri);
                break;
            case R.id.btn_remove:
                AlertDialog.Builder builder = new AlertDialog.Builder(InDepthApartmentDetails.this);
                final View popup = getLayoutInflater().inflate(R.layout.popup_two_option, null);

                TextView text = popup.findViewById(R.id.text);
                Button yes = popup.findViewById(R.id.op1);
                TextView no = popup.findViewById(R.id.op2);
                yes.setText("Delete");
                no.setText("Cancel");
                text.setText("Are you sure?");

                builder.setView(popup);
                AlertDialog dialog = builder.create();
                dialog.show();
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        delete();
                        dialog.dismiss();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                break;

            case R.id.btn_int_list:
                Intent li = new Intent(getApplicationContext(), listInterested.class);
                li.putExtra("id", String.valueOf(apartment.id));
                startActivity(li);
                break;
        }

        return true;
    }

    private void delete() {
        DatabaseReference mDatabase3 = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference().child("favourites").child(String.valueOf(apartment.id));
        mDatabase3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String likedBy = String.valueOf(dataSnapshot.getValue());
                    DatabaseReference mDatabase4 = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            .getReference().child("users").child(likedBy).child("fav_list").child(String.valueOf(apartment.id));
                    mDatabase4.removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mDatabase3.removeValue();



        Toast toast = Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_LONG);
        toast.show();
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/").
                getReference();

        mDatabase.child("ads").child(String.valueOf(apartment.id)).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("JAMIL", "removed 1");
                mDatabase.child("users").child(apartment.uid).child("apartment")
                        .child(String.valueOf(apartment.id)).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("JAMIL", "removed 2");
                        global_variables.cnt--;
                        mDatabase.child("mandatory_info").child("count").setValue(global_variables.cnt)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull  Task<Void> task) {
                                Log.d("JAMIL", "removed 3");
                                finish();
                            }
                        });
                    }
                });
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_depth_apartment_details);
        Intent intent = getIntent();
        apartment = intent.getParcelableExtra("apartment");
        key = intent.getStringExtra("key");
        getSupportActionBar().setTitle(apartment.name);
        TextView owner = findViewById(R.id.owner);
        TextView area = findViewById(R.id.area);
        TextView bedroom = findViewById(R.id.bedroom);
        TextView bathroom = findViewById(R.id.bathroom);
        TextView furniture = findViewById(R.id.furniture);
        TextView rent = findViewById(R.id.rent);
        TextView extra = findViewById(R.id.extra_info);
        progressBar = findViewById(R.id.wait_progress);
        load = findViewById(R.id.wait_msg);
        bedroom.setText(String.valueOf(apartment.bedrooms));
        bathroom.setText(String.valueOf(apartment.bathrooms));
        area.setText(String.valueOf(apartment.area));
        rent.setText(String.valueOf(apartment.rent));
        extra.setText(apartment.extra);
        if (apartment.furniture) furniture.setText("With");
        else furniture.setText("without");
        TextView total_floors = findViewById(R.id.total_floors);
        TextView available_floors = findViewById(R.id.available_floors);
        TextView mobile_no = findViewById(R.id.mobile_no);
        owner.setText(apartment.owner);
        total_floors.setText(String.valueOf(apartment.totalFloors));
        String floors = "";
        final long[] t = {apartment.mask};
        int f = 0, c = 1;
        while (t[0] > 0) {
            if (t[0] % 2 == 1) {
                if (f != 0) floors = floors + ", ";
                f++;
                floors = floors + String.valueOf(c);
            }
            t[0] = t[0] / 2;
            c++;
        }
        available_floors.setText(floors);
        mobile_no.setText(apartment.contactInfo);

        if(key.equals("tenent")){
            owner.setTextColor(getResources().getColor(R.color.quantum_yellow));
            owner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(InDepthApartmentDetails.this);
                    final View popup = getLayoutInflater().inflate(R.layout.profile_peak, null);
                    builder.setView(popup);
                    AlertDialog dialog = builder.create();
                    stall = popup.findViewById(R.id.stall);
                    name = popup.findViewById(R.id.name);
                    email = popup.findViewById(R.id.email);
                    phone = popup.findViewById(R.id.phone);
                    about = popup.findViewById(R.id.about);
                    propic = popup.findViewById(R.id.profile_pic);

                    user_info(apartment.uid);
                    changePropic(apartment.uid);

                    dialog.show();
                }
            });
        }

        //report = custom_toolbar.findViewById(R.id.btn_report);
        right = findViewById(R.id.btn_right);
        left = findViewById(R.id.btn_left);
        copy = findViewById(R.id.btn_copy);
        call = findViewById(R.id.btn_call);
        imageView = findViewById(R.id.image_view);
        n = apartment.image_count;
        final int[] ct = {1};
        if(n==0){
            load.setText("No image given");
            progressBar.setVisibility(View.INVISIBLE);
        } else load.setText("Image loading "+ ct[0] +"/"+n+"\nPlease wait");
        for(int ii= 1; ii<=n; ii++){
            Log.d("JAMIL", "Download requested: "+ii);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            StorageReference storage = FirebaseStorage.getInstance().getReference().child("img").
                    child(String.valueOf(apartment.id)).child(ii+"");
            try {
                final File file = File.createTempFile(timeStamp, apartment.uid);
                storage.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("JAMIL", "Photo download successful");
                        Bitmap bt = BitmapFactory.decodeFile(file.getAbsolutePath());
                        //propic.setImageBitmap(bt);
                        bitmaps.add(bt);
                        //propic.setImageURI(Uri.fromFile(file));
                        if(ct[0]==1) imageView.setImageBitmap(bt);

                        ct[0]++;
                        if(ct[0]<=n) load.setText("Image loading "+ ct[0] +"/"+n+"\nPlease wait");
                        else {
                            load.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("JAMIL", "Photo download failed:"+e);
                        ct[0]++;
                        if(ct[0]<=n) load.setText("Image loading "+ ct[0] +"/"+n+"\nPlease wait");
                        else {
                            load.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmaps.isEmpty()) return;
                i=(i+1)%bitmaps.size();
                //imageView.setImageResource(images[i]);
                imageView.setImageBitmap(bitmaps.get(i));
                Log.d("JAMIL", "i++");
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmaps.isEmpty()) return;
                i=(i-1+n)%bitmaps.size();
                //imageView.setImageResource(images[i]);
                imageView.setImageBitmap(bitmaps.get(i));
                Log.d("JAMIL", "i--");
            }
        });
       /* report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ri = new Intent(getApplicationContext(), Report_activity.class);
                startActivity(ri);
            }
        });*/
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("number", mobile_no.getText().toString());
                clipboardManager.setPrimaryClip(clip);
                Toast toast = Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s ="tel:"+apartment.contactInfo;
                Log.d("JAMIL", s);
                Toast toast = Toast.makeText(getApplicationContext(), "Calling "+apartment.owner , Toast.LENGTH_SHORT);
                toast.show();
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse(s));
                startActivity(intent1);
            }
        });
    }


    void user_info(String uid){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference().child("users").child(uid);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("name").getValue(String.class));
                email.setText(snapshot.child("email").getValue(String.class));
                phone.setText(snapshot.child("phone").getValue(String.class));
                about.setText(snapshot.child("extra").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void changePropic(String userId) {
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance("https://maaaaap-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference().child("users").child(userId).child("propic");
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.getResult().exists()){
                    propic.setImageResource(R.drawable.anonymous);
                    stall.setVisibility(View.INVISIBLE);
                } else {
                    Log.d("JAMIL", "Photo download request sent");
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    StorageReference storage = FirebaseStorage.getInstance().getReference().child("propics").child(userId);
                    try {
                        final File file = File.createTempFile(timeStamp, userId);
                        storage.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.d("JAMIL", "Photo download successful");
                                Bitmap bt = BitmapFactory.decodeFile(file.getAbsolutePath());
                                propic.setImageBitmap(bt);
                                stall.setVisibility(View.INVISIBLE);
                                //propic.setImageURI(Uri.fromFile(file));
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

                }
            }
        });
    }


}

package com.example.theroos.OsekApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextView profilename, profilenumber, profilestatus;
    private ImageView backarrow;
    private CircularImageView profilepicture;
    private Button updateprofile;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String uid;
    private FirebaseUser user;
    public String profilepicURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilenew);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        uid = user.getUid();

        profilename = findViewById(R.id.display_name);
        profilenumber = findViewById(R.id.display_number);
        profilestatus = findViewById(R.id.display_status);
        profilepicture = findViewById(R.id.display_picture);
        updateprofile = findViewById(R.id.button_updateprofile);
        backarrow = findViewById(R.id.backarrow);


        clicks();

        loaduserinformation();

        displayprofilepicture();

    }

    private void loaduserinformation() {

        FirebaseUser user = mAuth.getCurrentUser();

        final DatabaseReference mDatabasephone = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("phone");
        mDatabasephone.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String phone = dataSnapshot.getValue().toString();
                profilenumber.setText(phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference mDatabasename = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("name");
        mDatabasename.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue().toString();
                profilename.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final DatabaseReference mDatabasestatus = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());
        mDatabasestatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("status"))
                {
                    final FirebaseUser DBuser;
                    DBuser = FirebaseAuth.getInstance().getCurrentUser();

                    final DatabaseReference mDB = FirebaseDatabase.getInstance().getReference().child("user").child(DBuser.getUid()).child("status");
                    mDB.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String status = dataSnapshot.getValue().toString();
                            profilestatus.setText(status);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    profilestatus.setText(R.string.please_enter_your_status);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void displayprofilepicture(){

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ProfilePictures").child(user.getUid());

        final DatabaseReference mdatabaseURL = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("ProfileImageURI");
        mdatabaseURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    profilepicURI = dataSnapshot.getValue().toString();
                    Toast.makeText(getApplicationContext(),profilepicURI,Toast.LENGTH_LONG);
                    Glide.with(getApplicationContext()).load(profilepicURI).into(profilepicture);
                }
                else{
                    profilepicture.setImageResource(R.drawable.ic_person_white_24dp);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void clicks() {

        updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity.this.finish();
            }
        });

    }

    private void openDialog() {
            FragmentManager fm = getSupportFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putString("text",profilepicURI);
            ProfileUpdateDialog profileupdatedialog = new ProfileUpdateDialog();
            profileupdatedialog.setArguments(bundle);
            profileupdatedialog.show(fm, "dialog_update_profile");
    }

}



package com.example.theroos.OsekApp;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    private RecyclerView mUserlist;
    private RecyclerView.Adapter mUserlistAdapter;
    private RecyclerView.LayoutManager mUserlistLayoutManager;
    private FirebaseAuth mAuth;
    private UserListAdapter adapter;

    ArrayList<UserObject> userList, contactlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        mUserlist = findViewById(R.id.userList);
        mUserlist.setLayoutManager( new LinearLayoutManager(this));
        contactlist = new ArrayList<>();
        userList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        
        initializeRecyclerView();
        getContacts();
    }


    private void getContacts() {
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC",null);
        while (phones.moveToNext()){
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            UserObject mObject = new UserObject(name,phone);
            contactlist.add(mObject);
            //mUserlistAdapter.notifyDataSetChanged();
            getUserDetails(mObject);
        }
    }


    private void getUserDetails(final UserObject mObject) {
        final DatabaseReference mUserDb = FirebaseDatabase.getInstance().getReference().child("user");
        //Query query = mUserDb.orderByChild("phone").equalTo(mObject.getPhone());

        mUserDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    userList = new ArrayList<UserObject>();

                    for(DataSnapshot ds1: dataSnapshot.getChildren())
                    {
                        UserObject uscls = ds1.getValue(UserObject.class);
                        userList.add(uscls);
                    }

                    adapter = new UserListAdapter(getApplicationContext(),contactlist);
                    mUserlist.setAdapter(adapter);
                }

                /*if(dataSnapshot.child("user").exists()){
                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){

                        String name = "", phone = "";
                        if(childSnapshot.child("name").exists())
                            name = childSnapshot.child("name").getValue().toString();
                        if(childSnapshot.child("phone").exists())
                            phone = childSnapshot.child("phone").getValue().toString();

                        //UserObject mUser = new UserObject(childSnapshot.getKey(),name,phone);
                        UserObject mUser = new UserObject("",name,phone);

                        if(name.equals(phone))
                            for (UserObject mcontactIterator : contactlist){
                                if(mcontactIterator.getPhone().equals(mUser.getPhone())){
                                    mUser.setName(mcontactIterator.getName());
                                }
                            }

                        userList.add(mUser);
                        mUserlistAdapter.notifyDataSetChanged();
                        return;
                    }
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializeRecyclerView() {
        mUserlist = findViewById(R.id.userList);
        mUserlist.setNestedScrollingEnabled(false);
        mUserlist.setHasFixedSize(false);
        mUserlistLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false);
        mUserlist.setLayoutManager(mUserlistLayoutManager);
        mUserlistAdapter = new UserListAdapter(getApplicationContext(), userList);
        mUserlist.setAdapter(mUserlistAdapter);
    }
}

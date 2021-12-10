package com.example.theroos.OsekApp;

import android.Manifest;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentContacts extends Fragment {

    private RecyclerView mUserlist;
    private RecyclerView.Adapter mUserlistAdapter;
    private RecyclerView.LayoutManager mUserlistLayoutManager;
    private FirebaseAuth mAuth;
    private UserListAdapter adapter;
    private String name, phone;

    ArrayList<UserObject> userList;
    ArrayList<UserObject> contactlist;
    ArrayList<UserObject> mUsers;
    boolean commonuserlist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_contacts, container, false);

        mUserlist = V.findViewById(R.id.userList_recyclerview);
        mUserlist.setHasFixedSize(true);
        mUserlist.setLayoutManager(new LinearLayoutManager(getActivity()));

        contactlist = new ArrayList<>();
        userList = new ArrayList<>();
        mUsers = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        initializeRecyclerView();

        getContacts();
        getpermission();

        return V;
    }

    private void getContacts() {
        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC", null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            UserObject mObject = new UserObject(name,phone);
            contactlist.add(mObject);
            mUserlistAdapter.notifyDataSetChanged();
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
                        contactlist.retainAll(userList);
                    }

                    adapter = new UserListAdapter(getActivity().getApplicationContext(),contactlist);
                    mUserlist.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

        private void getpermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS},1);
         }
        }


        private void initializeRecyclerView () {
            mUserlist = mUserlist.findViewById(R.id.userList_recyclerview);
            mUserlist.setNestedScrollingEnabled(false);
            mUserlist.setHasFixedSize(true);
            mUserlistLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL,false);
            mUserlist.setLayoutManager(mUserlistLayoutManager);
            mUserlistAdapter = new UserListAdapter(getActivity().getApplicationContext(),contactlist);
            mUserlist.setAdapter(mUserlistAdapter);

        }
    }


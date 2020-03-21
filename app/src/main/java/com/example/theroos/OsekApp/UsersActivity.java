package com.example.theroos.OsekApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UsersActivity extends AppCompatActivity {

    RecyclerView userList;
    DatabaseReference mUsersDB, UsersRef;
    FirebaseAuth mAuth;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        userList = findViewById(R.id.userList2);
        userList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        mUsersDB = FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Modelclass>().setQuery(mUsersDB,Modelclass.class).build();

        FirebaseRecyclerAdapter<Modelclass,ContactsViewHolder> adapter = new FirebaseRecyclerAdapter<Modelclass, ContactsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ContactsViewHolder contactsViewHolder, int i, @NonNull Modelclass modelclass) {

                final String UserIDs = getRef(i).getKey();

                UsersRef = FirebaseDatabase.getInstance().getReference().child("user");
                Query query = UsersRef.orderByChild("phone").equalTo(modelclass.getUnumber());

                UsersRef.child(String.valueOf(query)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String profilename = dataSnapshot.child("name").getValue().toString();
                        String profilenumber = dataSnapshot.child("phone").getValue().toString();

                        contactsViewHolder.username.setText(profilename);
                        contactsViewHolder.usernumber.setText(profilenumber);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.object_user,parent,false);
                ContactsViewHolder viewHolder = new ContactsViewHolder(view);
                return viewHolder;
            }
        };

        userList.setAdapter(adapter);
        adapter.startListening();

    }


    public static class ContactsViewHolder extends RecyclerView.ViewHolder {

        TextView username, usernumber;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.mname);
            usernumber = itemView.findViewById(R.id.mphone);
        }
    }
}




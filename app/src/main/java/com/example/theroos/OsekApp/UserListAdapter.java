package com.example.theroos.OsekApp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    ArrayList<UserObject> userList;

    public UserListAdapter(Context applicationContext, ArrayList<UserObject> userList){

        this.userList = userList;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View layoutview = LayoutInflater.from(parent.getContext()).inflate(R.layout.object_user,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutview.setLayoutParams(lp);
        UserListViewHolder rcv = new UserListViewHolder(layoutview);
        return rcv;

    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder mholder, int position) {
        mholder.mName.setText(userList.get(position).getName());
        mholder.mPhone.setText(userList.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder {

        public TextView mName, mPhone;



        public UserListViewHolder(View view){
            super(view);
            mName = view.findViewById(R.id.mname);
            mPhone = view.findViewById(R.id.mphone);


        }

    }
}



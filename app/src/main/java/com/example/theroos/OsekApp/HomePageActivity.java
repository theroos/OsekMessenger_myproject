package com.example.theroos.OsekApp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomePageActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    FirebaseAuth mAuth;
    BottomNavigationView bottom_main;
    FrameLayout container_main;

    FragmentChat fragmentChat;
    FragmentCall fragmentCall;
    FragmentContacts fragmentContacts;
    FragmentProfile fragmentProfile;

    ImageView addstory, threedot_menu;
    Window window;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        bottom_main = findViewById(R.id.homepage_bottomnavigation);
        container_main = findViewById(R.id.main_container);
        bottom_main.setOnNavigationItemSelectedListener(navlistner);
        addstory = findViewById(R.id.addstory);
        threedot_menu = findViewById(R.id.option_menu);

        fragmentChat = new FragmentChat();
        fragmentCall = new FragmentCall();
        fragmentContacts = new FragmentContacts();
        fragmentProfile = new FragmentProfile();

        threedot_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(HomePageActivity.this,v);
                popup.setOnMenuItemClickListener(HomePageActivity.this);
                popup.inflate(R.menu.menu_homepage);
                popup.show();
            }
        });

        setfragment(fragmentChat);

        bottom_main.setSelectedItemId(R.id.nav_chat);

        if(Build.VERSION.SDK_INT >= 21){
            window = this.getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.White));
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_chat:
                    setfragment(fragmentChat);
                    return true;
                case R.id.nav_calls:
                    setfragment(fragmentCall);
                    return true;
                case R.id.nav_contact:
                    setfragment(fragmentContacts);
                    return true;
                case R.id.nav_profile:
                    setfragment(fragmentProfile);
                    return true;
                default:
                    return false;
            }
        }

    };

    public void setfragment(Fragment fragment){
        FragmentTransaction fragtrans = getSupportFragmentManager().beginTransaction();
        fragtrans.replace(R.id.main_container,fragment);
        fragtrans.commit();
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_homepage,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.contacts_item:
                Intent i = new Intent(HomePageActivity.this,ContactsActivity.class);
                startActivity(i);
                Toast.makeText(HomePageActivity.this, "Contacts",Toast.LENGTH_LONG).show();
                return true;
            case R.id.profile_item:
                Intent in = new Intent(HomePageActivity.this,ProfileActivity.class);
                startActivity(in);
                return true;
            case R.id.logout_item:
                opendialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    private void opendialog() {
        LogoutDialog logoutdialog = new LogoutDialog();
        logoutdialog.show(getSupportFragmentManager(),"logout manager");
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.contacts_item:
                Intent i = new Intent(HomePageActivity.this,ContactsActivity.class);
                startActivity(i);
                Toast.makeText(HomePageActivity.this, "Contacts",Toast.LENGTH_LONG).show();
                return true;
            case R.id.profile_item:
                Intent in = new Intent(HomePageActivity.this,ProfileActivity.class);
                startActivity(in);
                return true;
            case R.id.logout_item:
                opendialog();
                return true;
            default:return false;
        }
    }
}

package com.example.theroos.OsekApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomePageActivity extends AppCompatActivity {


    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

    }

    @Override
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
    }

    private void opendialog() {
        LogoutDialog logoutdialog = new LogoutDialog();
        logoutdialog.show(getSupportFragmentManager(),"logout manager");
    }
}

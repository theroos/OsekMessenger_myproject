package com.example.theroos.OsekApp;

import android.Manifest.permission;
import android.content.Intent;
import android.os.Build;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_TEXT = "com.example.theroos.contacts.EXTRA_TEXT";
    Button buttoncontacts;
    Switch nightmode;
    ConstraintLayout mainlayout;
    TextView maintext,version;
    String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttoncontacts = findViewById(R.id.contactsbutton);
        nightmode = findViewById(R.id.nightmodeswitch);
        mainlayout = findViewById(R.id.main_layout);
        maintext = findViewById(R.id.maintext);
        version = findViewById(R.id.version);

        String versionnum = Build.VERSION.RELEASE;
        version.setText("This app is currently running on Android " + versionnum + " device");

        mainlayout.setBackgroundResource(R.color.White);
        maintext.setTextColor(this.getResources().getColor(R.color.Black));
        nightmode.setText(R.string.Darkmdtext);
        nightmode.setTextColor(this.getResources().getColor(R.color.Black));
        version.setTextColor(this.getResources().getColor(R.color.Black));


        nightmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nightmode.isChecked()){
                    mainlayout.setBackgroundResource(R.color.DarkBackground);
                    maintext.setTextColor(MainActivity.this.getResources().getColor(R.color.White));
                    nightmode.setTextColor(MainActivity.this.getResources().getColor(R.color.White));
                    version.setTextColor(MainActivity.this.getResources().getColor(R.color.White));
                    nightmode.setText(MainActivity.this.getResources().getText(R.string.Lightmdtext));
                    text = "Dark";
                }
                    //Toast.makeText(MainActivity.this,"Dark Mode will on",Toast.LENGTH_LONG).show();                }
                else {
                    mainlayout.setBackgroundResource(R.color.White);
                    maintext.setTextColor(MainActivity.this.getResources().getColor(R.color.Black));
                    nightmode.setTextColor(MainActivity.this.getResources().getColor(R.color.Black));
                    version.setTextColor(MainActivity.this.getResources().getColor(R.color.Black));
                    nightmode.setText(MainActivity.this.getResources().getText(R.string.Darkmdtext));
                    text = "Light";

                }
            }
        });


        buttoncontacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ContactsActivity.class);
                i.putExtra(EXTRA_TEXT,text);
                startActivity(i);
            }
        });
        
        getPermissions();
    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{permission.WRITE_CONTACTS, permission.READ_CONTACTS},1);
        }
    }
}

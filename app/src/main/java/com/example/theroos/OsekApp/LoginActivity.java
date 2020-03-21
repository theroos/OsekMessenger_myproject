package com.example.theroos.OsekApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    TextView versiontext;
    EditText enternum;
    Button gotoverify;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        versiontext = findViewById(R.id.versiontext);
        enternum = findViewById(R.id.enternum);
        gotoverify = findViewById(R.id.gotoverify);

        mAuth = FirebaseAuth.getInstance();

        showversion();

        usersigninornot();

        gotoverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseApp.initializeApp(LoginActivity.this);
                String number = enternum.getText().toString().trim();
                if (number.isEmpty() || number.length()<10) {
                    enternum.setError("Valid number is required");
                    enternum.requestFocus();
                }
                else{
                    String PhoneNumber = "+" + "91" + number;
                    Intent i = new Intent(LoginActivity.this, VerfiyActivity.class);
                    i.putExtra("PhoneNumber", PhoneNumber);
                    startActivity(i);
                }
            }
        });

    }

    private void showversion() {
        String versionnum = Build.VERSION.RELEASE;
        versiontext.setText("This app is still under development & currently running on Android " + versionnum + " device");
    }

    private void usersigninornot() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            startActivity(new Intent(LoginActivity.this,HomePageActivity.class));
        }
    }
}

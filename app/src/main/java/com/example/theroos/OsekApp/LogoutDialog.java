package com.example.theroos.OsekApp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;

public class LogoutDialog extends AppCompatDialogFragment {
    FirebaseAuth mAuth;
    private Context context;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        AlertDialog alert = builder.create();

        alert.setCancelable(false);
        alert.setMessage("Are you sure you want to log out ?");

        alert.setButton(Dialog.BUTTON_POSITIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.getInstance().signOut();
                        startActivity(new Intent(LogoutDialog.this.getContext(), LoginActivity.class));
                        Toast.makeText(LogoutDialog.this.getContext(), "Logout",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });

        alert.setButton(Dialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.show();

        Button button_negative = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        button_negative.setTextColor(getResources().getColor(R.color.colorPrimary));

        Button button_positive = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        button_positive.setTextColor(getResources().getColor(R.color.colorPrimary));

        return alert;
    }
}

package com.example.theroos.OsekApp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class NameUpdateDialog extends AppCompatDialogFragment {

    private EditText updatename;
    private TextView update_name_text, cancel_text;



    public NameUpdateDialog(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_name_update, null);
        updatename = view.findViewById(R.id.nameupdate_edittext);
        update_name_text = view.findViewById(R.id.update_name_text);
        cancel_text = view.findViewById(R.id.cancel_text);
        return view;

    }
}

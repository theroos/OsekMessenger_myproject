package com.example.theroos.OsekApp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CameraGalleryOpenBottomsheetDialog extends BottomSheetDialogFragment {
    private BottomsheetListener mListener = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_bottomsheet_gallery_camera,container,false);

        ImageView opencamera = view.findViewById(R.id.open_camera_profilepicture);
        ImageView opengallery = view.findViewById(R.id.open_gallery_profilepicture);

        opencamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBottonClicked("open_camera");
                //Toast.makeText(getActivity(),"Camera will open",Toast.LENGTH_LONG).show();
                dismiss();
            }
        });

        opengallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBottonClicked("open_gallery");
                //Toast.makeText(getActivity(),"Gallery will open",Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
        return view;
    }


    public interface BottomsheetListener {
        void onBottonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListener = (BottomsheetListener) context;
        }catch (Exception e){
            //throw new ClassCastException(context.toString() + " must implement BottomsheetListener");
        }

    }
}

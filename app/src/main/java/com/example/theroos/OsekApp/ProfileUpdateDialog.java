package com.example.theroos.OsekApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileUpdateDialog extends AppCompatDialogFragment {

    private static final String TAG = "ProfileUpdateDialog";

    public static final int RESULT_OK = -1;
    public Uri imguri, resultimgUri;
    public String profilepicLink;
    private EditText updatename, updatestatus;
    private Button savebutton, cancelbutton;
    private CircularImageView updateprofilepicture;
    private ImageView updatepicturecameraicon;
    private BottomSheetDialog buttomsheetdialog;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private StorageReference mStorageRef;
    private Context context;

    public ProfileUpdateDialog(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_update_profile,null);
        Bundle bundle = getArguments();
        profilepicLink = bundle.getString("text","");
        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        //mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        updatename = view.findViewById(R.id.update_name);
        updatestatus = view.findViewById(R.id.update_status);
        savebutton = view.findViewById(R.id.save_button);
        cancelbutton = view.findViewById(R.id.cancel_button);
        updateprofilepicture = view.findViewById(R.id.update_profilepicture);
        updatepicturecameraicon = view.findViewById(R.id.update_profilepicture_camera);
        buttomsheetdialog = new BottomSheetDialog(getActivity());
        final View bottomSheetDialogview = getLayoutInflater().inflate(R.layout.dialog_bottomsheet_gallery_camera, null);
        buttomsheetdialog.setContentView(bottomSheetDialogview);
        ImageView open_camera = bottomSheetDialogview.findViewById(R.id.open_camera_profilepicture);
        ImageView open_gallery = bottomSheetDialogview.findViewById(R.id.open_gallery_profilepicture);

        open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "IMG_FOLDER");
                resultimgUri = Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + "profile_img.jpg"));
                startActivityForResult(i, 100);
                buttomsheetdialog.dismiss();
            }
        });

        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setDataAndType(imguri,"image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i,1);
                buttomsheetdialog.dismiss();
            }
        });


        defaultedittext();

        displayprofilepicture();

        buttonsclick();

        profilepictureupdate();

        return view;
    }

    private void buttonsclick() {
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                savenamestatus();

                saveprofilepicture();

                dismiss();
            }

        });
    }

    private void savenamestatus() {

        final String name = updatename.getText().toString().trim();
        final String status = updatestatus.getText().toString().trim();
        FirebaseUser user = mAuth.getInstance().getCurrentUser();


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map<String,Object> usermap = new HashMap<>();
                    usermap.put("name",name);
                    usermap.put("status",status);
                    databaseReference.updateChildren(usermap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Toast.makeText(getContext(),"Profile Updated",Toast.LENGTH_LONG).show();
    }

    private void defaultedittext(){

        FirebaseUser user = mAuth.getCurrentUser();

        final DatabaseReference mDBupdatename = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("name");
        mDBupdatename.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String defaultname = dataSnapshot.getValue().toString();
                updatename.setText(defaultname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference mDBupdatestatus = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());
        mDBupdatestatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("status"))
                {
                    final FirebaseUser DBuser;
                    DBuser = FirebaseAuth.getInstance().getCurrentUser();

                    final DatabaseReference mDB = FirebaseDatabase.getInstance().getReference().child("user").child(DBuser.getUid()).child("status");
                    mDB.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String defaultstatus = dataSnapshot.getValue().toString();
                            updatestatus.setText(defaultstatus);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else { }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void displayprofilepicture(){
        if(profilepicLink != null)
        {
            Glide.with(getActivity()).load(profilepicLink).into(updateprofilepicture);
        } else {
            updateprofilepicture.setImageResource(R.drawable.ic_person_white_24dp);
        }

    }

    private void profilepictureupdate(){
        updatepicturecameraicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttomsheetdialog.show();
            }
        });

    }


    public Uri getImageUri( Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    //onActivityResult to open gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
        {
            if(data != null){
                resultimgUri = data.getData();
                updateprofilepicture.setImageURI(resultimgUri);
            }
        } else if(requestCode == 100)
        {
            if (data != null) {
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                resultimgUri = getImageUri(bitmap);
                updateprofilepicture.setImageBitmap(bitmap);
            }
        }
    }

    private String getExtension (Uri uri){
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    @SuppressWarnings("VisibleForTests")
    private void saveprofilepicture(){
        if(resultimgUri != null) {
            final ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.show();

            //creating references & image title
            mStorageRef = FirebaseStorage.getInstance().getReference().child("ProfilePictures").child(user.getUid());
            final StorageReference Ref = mStorageRef.child(System.currentTimeMillis() + "." + getExtension(resultimgUri));
            //final StorageReference imgname = mStorageRef.child("photo"+resultimgUri.getLastPathSegment());
            final StorageReference imgname = mStorageRef.child("profilepicture_osek"+".jpg");

            //storing image into firebase storage
            imgname.putFile(resultimgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                {
                    if (task.isSuccessful()) {

                        dialog.dismiss();

                        //getting downloadUrl
                        imgname.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {
                                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            //storing downloadurl into database
                                            Map<String, Object> usermap = new HashMap<>();
                                            usermap.put("ProfileImageURI", String.valueOf(uri));
                                            databaseReference.updateChildren(usermap);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle unsuccessful uploads
                                // ...
                                dialog.dismiss();
                                Toast.makeText(getActivity(),"Failed",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else {
                        String errmsg = task.getException().toString();
                        Toast.makeText(context.getApplicationContext(),"Failed  "+errmsg,Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    //show upload progress
                    double progress = (100 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                    dialog.setMessage("Uploading " + (int)progress+"%");
                }
            });
        }
    }


}



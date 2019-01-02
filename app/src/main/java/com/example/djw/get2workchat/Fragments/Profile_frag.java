package com.example.djw.get2workchat.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.djw.get2workchat.Data_Models.User;
import com.example.djw.get2workchat.Database.DBUtil;
import com.example.djw.get2workchat.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Profile_frag extends Fragment {
     private  View v;

    private FirebaseDatabase db;
    private DBUtil  dbUtil;
    private EditText profile_name, profile_email, profile_phone, profile_profession;
    private ImageView profile_image;
    private Button btn_save;
    private FirebaseAuth mAuth;
    private String UserID;
    private Uri resulturi;
    private String profilePicturePath;

    public Profile_frag() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_profile,container,false);

       mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();



    dbUtil = new DBUtil();


        profile_name = (EditText) v.findViewById(R.id.profile_name);
        profile_email = (EditText) v.findViewById(R.id.profile_email);
        profile_phone = (EditText) v.findViewById(R.id.profile_phone_number);
        profile_profession = (EditText) v.findViewById(R.id.profile_professions);
        profile_image = (ImageView) v.findViewById(R.id.profile_picture);
        btn_save = (Button) v.findViewById(R.id.btn_save);

        getUserDetails();

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                saveUserDetails();

            }
        });

        return v;

    }


    private void saveUserDetails() {

        String name = profile_name.getText().toString();
        String mail = profile_email.getText().toString();
        String phone = profile_phone.getText().toString();
        String proff = profile_profession.getText().toString();


        try {
            dbUtil.updateUser(name, mail,phone,proff, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (resulturi != null) {

            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profile_image").child(mAuth.getCurrentUser().getUid());
            Bitmap bitmap = null;

            try {
                    // Gets the image from the resultURI
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), resulturi);
            } catch (IOException e) {
                e.printStackTrace();
            }


            //Images Compression
            ByteArrayOutputStream boas = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, boas);
            byte[] data = boas.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);


            // Uploads the profile image to the filepath and updates the users profile.
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map newImage = new HashMap();
                            newImage.put("profilePicturePath", uri.toString());


                            dbUtil.updateUser(null, null, null, null, uri.toString());
                        //    finish();
                            return;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                          //  finish();
                            return;
                        }
                    });
                }
            });


        }

    }




    private void getUserDetails() {

        dbUtil.getUserById(UserID, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                if (dataSnapshot.exists()) {

                    //    User user = dataSnapshot.getValue(User.class);
                    Map<String, Object> UserInfo = (Map<String, Object>) dataSnapshot.getValue();
                    if (UserInfo.get("userName") != null) {
                        String UserNmame = UserInfo.get("userName").toString();
                        profile_name.setText(UserNmame);
                    }
                    if (UserInfo.get("email") != null) {
                        String email = UserInfo.get("email").toString();
                        profile_email.setText(email);
                    }
                    if (UserInfo.get("profilePicturePath") != null) {
                        String profilepicture = UserInfo.get("profilePicturePath").toString();
                        Glide.with(getContext()).load(profilepicture).apply(new RequestOptions().placeholder(R.drawable.baseline_account_circle_black_18dp)).into(profile_image);

                    }

                    if (UserInfo.get("profession") != null) {

                        String profession = UserInfo.get("profession").toString();
                        profile_profession.setText(profession);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {


            final Uri imageuri = data.getData();
            resulturi = imageuri;
            profile_image.setImageURI(resulturi);

        }
    }
}

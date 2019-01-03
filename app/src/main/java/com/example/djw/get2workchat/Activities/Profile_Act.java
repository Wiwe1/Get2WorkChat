package com.example.djw.get2workchat.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
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

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class Profile_Act extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference myref;
    private TextView profile_name, profile_email, profile_phone, profile_profession;

    private ImageView profile_image;
    private FirebaseAuth mAuth;

    private Uri resulturi;
    private String profilePicturePath;
    private  DBUtil dbUtil;
private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        dbUtil = new DBUtil();

        profile_name = (TextView) findViewById(R.id.profile_name);
        profile_email = (TextView) findViewById(R.id.profile_email);
        profile_phone = (TextView) findViewById(R.id.profile_phone_number);
        profile_profession = (TextView) findViewById(R.id.profile_professions);
        profile_image = (ImageView) findViewById(R.id.profile_picture);



        // Gets the UserID  passed via an Intent from the fragment.
        Bundle extras = getIntent().getExtras();


        if(extras!= null){
            userId = extras.getString("userId","");

            Log.d(userId, "onCreate: EXTRAS");
        }

        getUserDetails();


    }





    private void getUserDetails() {

        dbUtil.getUserById(userId,new  ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    Map<String, Object> UserInfo = (Map<String, Object>) dataSnapshot.getValue();
                    if (UserInfo.get("userName") != null) {
                        String UserNmame = UserInfo.get("userName").toString();
                        profile_name.setText(UserNmame);
                    }
                    if (UserInfo.get("email") != null) {
                        String email = UserInfo.get("email").toString();
                        profile_email.setText(email);
                    }
                    if(UserInfo.get("phone_number")!=null){


                        String phonNum = UserInfo.get("phone_number").toString();
                        profile_phone.setText(phonNum   );

                    }

                    if (UserInfo.get("profilePicturePath") != null) {
                        String profilepicture = UserInfo.get("profilePicturePath").toString();
                        Glide.with(getApplication()).load(profilepicture).apply(new RequestOptions().placeholder(R.drawable.baseline_account_circle_black_18dp)).into(profile_image);





                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {


            final Uri imageuri = data.getData();
            resulturi = imageuri;
            profile_image.setImageURI(resulturi);

        }
    }
}

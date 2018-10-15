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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import java.util.concurrent.atomic.AtomicMarkableReference;

public class Profile_Act extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference myref;
    private EditText profile_name, profile_email, profile_phone, profile_profession;

    private ImageView profile_image;
    private Button btn_save;
    private FirebaseAuth mAuth;
    private String UserID;
    private Uri resulturi;
    private String profilePicturePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        UserID = mAuth.getCurrentUser().getUid();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        myref = db.getReference("users").child(UserID);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        profile_name = (EditText) findViewById(R.id.profile_name);
        profile_email = (EditText) findViewById(R.id.profile_email);
        profile_phone = (EditText) findViewById(R.id.profile_phone_number);
        profile_profession = (EditText) findViewById(R.id.profile_professions);
        profile_image = (ImageView) findViewById(R.id.profile_picture);
        btn_save = (Button) findViewById(R.id.btn_save);

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


    }


    private void saveUserDetails() {

        String name = profile_name.getText().toString();
        String mail = profile_email.getText().toString();
        String phone = profile_phone.getText().toString();
        String proff = profile_profession.getText().toString();

        final DBUtil db = new DBUtil();
        db.updateUser(name, mail,phone,proff, null);

        if (resulturi != null) {

            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profile_image").child(mAuth.getCurrentUser().getUid());
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resulturi);
            } catch (IOException e) {
                e.printStackTrace();
            }


            //Images Compression
            ByteArrayOutputStream boas = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, boas);
            byte[] data = boas.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);


            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //  Map newImage = new HashMap();
                            //newImage.put("profileImageUrl", uri.toString());
                            // /mDriverDatabase.updateChildren(newImage);

                            db.updateUser(null, null, null, null, uri.toString());
                            finish();
                            return;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            finish();
                            return;
                        }
                    });
                }
            });


        }

    }

    private void getUserDetails() {

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    User user = dataSnapshot.getValue(User.class);
                    profile_name.setText(user.getUserName());
                    profile_email.setText(user.getEmail());
                    profile_phone.setText(user.getPhone_number());
                    profilePicturePath = user.getProfilePicturePath();
                    profile_profession.setText(user.getProfression());
                    Glide.with(getApplication()).load(profilePicturePath).into(profile_image);

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

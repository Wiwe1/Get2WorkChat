package com.example.djw.get2workchat.Database;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.djw.get2workchat.Data_Models.Chat_room;
import com.example.djw.get2workchat.Data_Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class DBUtil {




    FirebaseDatabase db = FirebaseDatabase.getInstance();


    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference myref = db.getReference("users").child(auth.getCurrentUser().getUid());
    DatabaseReference chatrooms = db.getReference("users").child(auth.getCurrentUser().getUid()).child("chatrooms");
    public void intCurrentUser() {





        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){



                        String Auth_id= auth.getUid();

                        User user = new User(auth.getCurrentUser().getDisplayName().toString(),auth.getCurrentUser().getEmail().toString(),null,null,null);
                        myref.setValue(user);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        myref.addListenerForSingleValueEvent(valueEventListener);

    }


    public void updateUser(final String name, final String email, final String phone_number, final String prof, final String ProfilePicturePath)
    {

            myref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    User user = dataSnapshot.getValue(User.class);
                   if( name!=null)
                    user.setUserName(name);
                   if ( email!=null)
                           user.setEmail(email);
                   if(  phone_number!=null)
                   user.setPhone_number(phone_number);
                   if(  prof !=null)
                   user.setProfression(prof);
                   if(ProfilePicturePath!= null)
                       user.setProfilePicturePath(ProfilePicturePath);
                    myref.setValue(user);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


    }




    public void CreateChatroom(final String roomname  ){


        final String mkey = UUID.randomUUID().toString();

            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()){

                        Chat_room room = new Chat_room(mkey,roomname.toString());
                    chatrooms.setValue(room);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            };


        chatrooms.addValueEventListener(valueEventListener);
    }


}

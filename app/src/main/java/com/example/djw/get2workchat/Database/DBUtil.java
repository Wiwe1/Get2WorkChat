package com.example.djw.get2workchat.Database;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.djw.get2workchat.Data_Models.Chat_room;
import com.example.djw.get2workchat.Data_Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.UUID;

public class DBUtil {




        FirebaseDatabase db = FirebaseDatabase.getInstance();


    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference test = db.getReference();
    DatabaseReference myref = db.getReference("users").child(auth.getCurrentUser().getUid());
    DatabaseReference chatrooms = db.getReference("chatrooms").push();
    DatabaseReference getChatRooms = db.getReference("chatrooms");
    DatabaseReference addUsserChat = db.getReference("users").child(auth.getCurrentUser().getUid()).child("engaged chats");


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




    public void CreateChatroom(final String roomname){


        //final String mkey = UUID.randomUUID().toString();

            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   //If a chat room does not exsist, create it. Logs when completed
                    if(!dataSnapshot.exists()){

                        final Chat_room room = new Chat_room(null,roomname.toString(),null);

                        List<String> userid = new ArrayList<String>();

                        userid.add(auth.getCurrentUser().getUid().toString());

                    chatrooms.setValue(new Chat_room(null,roomname,userid), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            Log.d("DB UTIL CREATED ROOM", "onComplete: createde chat room  "+room.getName());
                        }
                    });



  //                      DatabaseReference groupmembers =db.getReference("chatrooms").child(chatrooms.getKey());
//                        groupmembers.push().setValue(auth.getCurrentUser().getUid());
                    //Addes the user to the chatroom he just created;
                        addUsserChat.push().setValue(chatrooms.getKey());

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            };


        chatrooms.addListenerForSingleValueEvent(valueEventListener);
    }

    public void getRooms(ValueEventListener listener){

        getChatRooms.addValueEventListener( listener);

    }


    public  void getUserRooms(){

        addUsserChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String groupKey = dataSnapshot.getValue().toString();
                test.child("chatrooms/"+groupKey+"/name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.d(dataSnapshot.getValue().toString(),"User is member of this group");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                            databaseError.getDetails();
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                databaseError.getDetails();
            }
        });



    }



}

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DBUtil {




     private    FirebaseDatabase db = FirebaseDatabase.getInstance();


    private  FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference test = db.getReference();
    private  DatabaseReference user = db.getReference().child("users");

    private DatabaseReference myref = db.getReference("users").child(auth.getCurrentUser().getUid());
    private    DatabaseReference chatrooms = db.getReference("chatrooms").push();
    private  DatabaseReference getChatRooms = db.getReference("chatrooms");
    private DatabaseReference  UpdateChatRoom = db.getReference("chatrooms").child("UserIds").push();
    private    DatabaseReference addUsserChat = db.getReference("users").child(auth.getCurrentUser().getUid()).child("engaged chats");
    private  DatabaseReference messages = db.getReference("messeges").push();
    private     List<Chat_room> chtrom;


public interface firebasCallback{

    void OnCallBack(List<Chat_room> list);

    }
//region UserMethods

    public void intCurrentUser() {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){
                        String Auth_id= auth.getUid();
                        User user = new User(null,auth.getCurrentUser().getDisplayName().toString(),auth.getCurrentUser().getEmail().toString(),null,null,null);
                        myref.setValue(user);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        myref.addListenerForSingleValueEvent(valueEventListener);

    }


    public void getUserById(String Userid, ValueEventListener valueEventListener){

         DatabaseReference getUser = db.getReference().child("users").child(Userid);

         getUser.addValueEventListener(valueEventListener);


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

//endregion

//region RoomMethos
    public void CreateChatroom(final String roomname){


        //final String mkey = UUID.randomUUID().toString();

            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   //If a chat room does not exsist, create it. Logs when completed
                    if(!dataSnapshot.exists()){

                        final Chat_room room = new Chat_room(null,roomname.toString(),null);

                        HashMap<String,Object> userid = new HashMap<>();

                        userid.put(roomname,auth.getCurrentUser().getUid());
                     //   userid.add(auth.getCurrentUser().getUid().to String());

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


    public void addUserToRoom(String email, final String roomid){

        final String uuid = UUID.randomUUID().toString();
        final HashMap<String,Object> userid = new HashMap<>();
        final DatabaseReference  UpdateChatRoom = db.getReference("chatrooms").child(roomid).child("UserIds");

        //Finds the user to be added based on email;
        Query SearchUser = user.orderByChild("email").equalTo(email);
        SearchUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Userid = null;
                if(dataSnapshot.exists()){

                    for(DataSnapshot dsp: dataSnapshot.getChildren()){

                        Userid = dsp.getKey().toString();
                        Log.d("USERIDFROMEMAIL", "Got user ID:"+dsp.getKey());

                    }

                    final    DatabaseReference addUsserChat = db.getReference("users").child(Userid).child("engaged chats");
                    userid.put(uuid,Userid);
                    Log.d("USERID", "onDataChange: "+userid.get("test"));
                    // addUsserChat.push().setValue(roomid);
                    UpdateChatRoom.updateChildren(userid);
                    addUsserChat.push().setValue(roomid);



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                databaseError.getDetails().toString();

            }
        });
    }

    public void getRooms(ValueEventListener listener){

        getChatRooms.addValueEventListener( listener);


    }


    public void getUserRooms(final firebasCallback firebasCallback){
        chtrom = new ArrayList<Chat_room>();


        addUsserChat.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //    chtrom.clear();
                final String groupKey = dataSnapshot.getValue().toString();
                test.child("chatrooms/"+groupKey+"/name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        chtrom.add(new Chat_room(groupKey,dataSnapshot.getValue().toString(),null));
                            Log.d(dataSnapshot.getValue().toString(),"User is member of this group");
                            firebasCallback.OnCallBack(chtrom);

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

    public void sendMessageChatRoom(String roomid, String senderId, final String message,final String type, DatabaseReference.CompletionListener completionListener){


        Map<String,Object> mapMessage = new HashMap<>();

        mapMessage.put("chat_room_id",roomid);
        mapMessage.put("sender_id",senderId);
        mapMessage.put("message",message);
        mapMessage.put("type",type);
        mapMessage.put("sent",ServerValue.TIMESTAMP);
        messages.setValue(mapMessage, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Log.d("MESSAGE","MESSAGE SENT" + message);
              //  Log.d("DATABASEERROR","ERROR: "+databaseError.getDetails().toString());
            }
        });
    }

    public void getMessegesFromRoom(String roomid, ValueEventListener listener){

    Query   getMesseges = db.getReference("messeges").orderByChild("chat_room_id").equalTo(roomid);


        getMesseges.addValueEventListener(listener);


    }


//endregion


}

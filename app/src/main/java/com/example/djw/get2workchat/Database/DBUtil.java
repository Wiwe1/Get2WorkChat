package com.example.djw.get2workchat.Database;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.djw.get2workchat.Data_Models.Chat_room;
import com.example.djw.get2workchat.Data_Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DBUtil {




     private    FirebaseDatabase db = FirebaseDatabase.getInstance();
    private  FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference test = db.getReference();
    private  DatabaseReference user = db.getReference().child("users");
    private DatabaseReference currentUser = db.getReference("users").child(auth.getCurrentUser().getUid());
    private    DatabaseReference chatrooms = db.getReference("chatrooms").push();
    private  DatabaseReference getChatRooms = db.getReference("chatrooms");
    private DatabaseReference  UpdateChatRoom = db.getReference("chatrooms").child("UserIds").push();
    private    DatabaseReference userEngagedChats = db.getReference("users").child(auth.getCurrentUser().getUid()).child("engaged chats");
    private  DatabaseReference messages = db.getReference("messeges").push();
    private     List<Chat_room> chtrom;

//Callback for getting User chatrooms
public interface firebasCallback{

    void OnCallBack(List<Chat_room> list);

    }

    //Caallback for getting the message counter
    public interface getCounterCallbck{

    void getCounter(int counter);

    }
//region UserMethods

    public void intCurrentUser() {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){
                        String Auth_id= auth.getUid();
                        User user = new User(auth.getCurrentUser().getDisplayName().toString(),auth.getCurrentUser().getEmail().toString(),null,null,null,null);
                        currentUser.setValue(user);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        currentUser.addListenerForSingleValueEvent(valueEventListener);

    }


    public void getUserById(String Userid, ValueEventListener valueEventListener){

         DatabaseReference getUser = db.getReference().child("users").child(Userid);

         getUser.addValueEventListener(valueEventListener);


    }


    public void updateUser(final String name, final String email, final String phone_number, final String prof, final String ProfilePicturePath)
    {

            currentUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    HashMap userInfo = new HashMap();


                    User user = dataSnapshot.getValue(User.class);
                   if( name!=null)
                  userInfo.put("userName",name);
                   if ( email!=null)
                       userInfo.put("email",email);
                   if(  phone_number!=null)
                       userInfo.put("phone_number",phone_number);;
                   if(  prof !=null)
                       userInfo.put("profession",prof);
                   if(ProfilePicturePath!= null)
                       userInfo.put("profilePicturePath",ProfilePicturePath);
                    currentUser.updateChildren(userInfo);
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
                        userEngagedChats.push().setValue(chatrooms.getKey());

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
                    // userEngagedChats.push().setValue(roomid);
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

// Get's the chat rooms which a particular user is engaged in.
    public void getUserRooms(final firebasCallback firebasCallback){
        chtrom = new ArrayList<Chat_room>();


        userEngagedChats.addChildEventListener(new ChildEventListener() {
// Gets the engaged chats of the user
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    //    chtrom.clear();
                final String groupKey = dataSnapshot.getValue().toString();

                //Gets the rooms which corresponds to those ids.
                test.child("chatrooms/"+groupKey+"/name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        chtrom.add(new Chat_room(groupKey,dataSnapshot.getValue().toString(),null));
                            Log.d(dataSnapshot.getValue().toString(),"User is member of this group");
                            // Call to callback interface for useing the results outside of onDataChange
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

    public void sendMessageChatRoom(final String roomid, final String senderId, final String message, final String type, DatabaseReference.CompletionListener completionListener){

        final Map<String,Object> mapMessage = new HashMap<>();


        final DatabaseReference messagesTest = db.getReference("chatrooms").child(roomid).child("messeges").push();
     //   final DatabaseReference messagesTest2 = db.getReference("chatrooms").child(roomid).child("messeges");
        final DatabaseReference messagesNumberTest = db.getReference("chatrooms").child(roomid).child("count");
        final long[] count = new long[1];
       updateCount(messagesNumberTest, new getCounterCallbck() {
           @Override
           public void getCounter(int counter) {
               Log.d("updateCounter", "getCounter: "+counter);
               mapMessage.put("chat_room_id",roomid);
               mapMessage.put("sender_id",senderId);
               mapMessage.put("message",message);
               mapMessage.put("type",type);
               mapMessage.put("sent",ServerValue.TIMESTAMP);
                mapMessage.put("message_number",counter);
               messagesTest.setValue(mapMessage, new DatabaseReference.CompletionListener() {
                   @Override
                   public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                       Log.d("MESSAGE","MESSAGE SENT" + message);
                       //     Log.d("DATABASEERROR","ERROR: "+databaseError.getDetails().toString());


                   }
               });


           }
       });

/*
        messagesNumberTest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mapMessage.clear();
                 count[0] = (long)dataSnapshot.getValue();
                Log.d("Count", "onDataChange: "+ count[0]);

                mapMessage.put("chat_room_id",roomid);
                mapMessage.put("sender_id",senderId);getCounter
                mapMessage.put("message",message);getCounter
                mapMessage.put("type",type);
                mapMessage.put("sent",ServerValue.TIMESTAMP);
                mapMessage.put("message_number",count[0]);
                //     testcount   = testcount+1;
                messagesTest.setValue(mapMessage, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        Log.d("MESSAGE","MESSAGE SENT" + message);
                        //     Log.d("DATABASEERROR","ERROR: "+databaseError.getDetails().toString());


                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

*/
/*
        mapMessage.put("chat_room_id",roomid);
        mapMessage.put("sender_id",senderId);
        mapMessage.put("message",message);
        mapMessage.put("type",type);
        mapMessage.put("sent",ServerValue.TIMESTAMP);
     // mapMessage.put("message_number",count[0]);
   //     testcount   = testcount+1;
        messagesTest.setValue(mapMessage, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Log.d("MESSAGE","MESSAGE SENT" + message);
           //     Log.d("DATABASEERROR","ERROR: "+databaseError.getDetails().toString());


            }
        });
*/
    }

    // Increments the a counter used formessages
    public void updateCount(final DatabaseReference database, final getCounterCallbck counterCallbck){
        database.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {


// Checks if a value exsist and set's it to one if it does not. Returns th value in callback
                if(mutableData.getValue() == null){

                    mutableData.setValue(1);
                    int Counter = Integer.parseInt(mutableData.getValue().toString());
                    counterCallbck.getCounter(Counter);
                }
                else{

                    //Reads the counter and increments it.
                    mutableData.setValue(Integer.parseInt(mutableData.getValue().toString()) + 1);
                    int  Counter     = Integer.parseInt(mutableData.getValue().toString());

                    counterCallbck.getCounter(Counter);
                    //HashMap<String,Object> testMap = new HashMap<>();
                    //testMap.put("messagenumber",test);
                   // database.child(key).updateChildren(testMap);
                }
                return Transaction.success(mutableData);

            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {

            }
        });
    }

    public void deleteRoom(final String roomid){

/*
    DatabaseReference removeRoom = db.getReference("chatrooms").child(roomid);
removeRoom .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("RemovedRoom", "onComplete: Removed room with id " + roomid);
                        } else {

                        }
                    }
                });

*/
            final Query getAllUsers = db.getReference("users");
                // Get's the ids of all users and lopps though each the users engaged chats to see if it contains the room id
            getAllUsers.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    // The User Id
                    String userKey = dataSnapshot.getKey()    .toString();
                    Log.d("userKey", "onComplete: Removed room with id " + userKey);

                    final Query removeUser = db.getReference("users").child(userKey).child("engaged chats");

                    removeUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                                if (roomid.contentEquals(dsp.getValue().toString())) {

                                    // Removes the room id if equal to the roo id given as parameter

                                    ((DatabaseReference) removeUser).child(dsp.getKey()).removeValue();

                                    //  removeUser.

                                    Log.d("Roomkey", "onDataChange: " + dsp.getKey());


                                }


                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

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

                }
            });
/*
    final Query removeUser = db.getReference("users").child(auth.getCurrentUser().getUid()).child("engaged chats");

    removeUser.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            for(DataSnapshot dsp : dataSnapshot.getChildren()){

                if(roomid.contentEquals(dsp.getValue().toString())){

                        ((DatabaseReference) removeUser).child(dsp.getKey()).removeValue();

                  //  removeUser.

                    Log.d("Roomkey", "onDataChange: "+dsp.getKey());


                }

            }
            String key = dataSnapshot.getKey();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
*/

        /*
        removeUser .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("RemovedRoom", "onComplete: Removed room with id " + roomid);
                        }
                        else {



                        }
                    }
                });
*/
    }



    public void getMoreMessages(String roomId,String key, ChildEventListener listener){

        DatabaseReference messageRef = db.getReference("chatrooms").child(roomId).child("messeges");
        Query getMesseges = messageRef.orderByKey().endAt(key).limitToLast(10);
           getMesseges.addChildEventListener(listener);

    }

    // Gets the messges in a room and orders the by the time they are sent
    public void getMessegesFromRoom(String roomid,int total_messeges,int currentpage, ChildEventListener listener){
 DatabaseReference messageRef = db.getReference("chatrooms").child(roomid).child("messeges");
            Query getMesseges = messageRef.orderByChild("sent").limitToLast(currentpage*total_messeges);
//   Query   getMesseges = db.getReference("messeges").orderByChild("chat_room_id").equalTo(roomid);
      //  Query   getMesseges = db.getReference("messeges").startAt("chat_room_id",roomid );
        getMesseges.addChildEventListener(listener);

    }

    public void searchMessages(String roomId,ValueEventListener listener){





    }


//endregion


}

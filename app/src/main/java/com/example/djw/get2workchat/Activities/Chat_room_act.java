package com.example.djw.get2workchat.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.djw.get2workchat.Data_Models.Message;
import com.example.djw.get2workchat.Database.DBUtil;
import com.example.djw.get2workchat.MessageRecyclerAdapter;
import com.example.djw.get2workchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Chat_room_act extends AppCompatActivity {
   // private static  final String CURRENT_USER ="CURRENT_USR";
    private static final String room_id = "room_id";
    private static final String room_name = "room_name";
    private Uri resulturi;
    private String profilePicturePath;
    private String roomId;
    private String roomName;
    private DBUtil db;
private String userId;
private EditText txtMessage;
private ImageButton sendMessage;
private ImageView SendImageMessage;
private ImageView message_image;
private RecyclerView recyclerMesseges;
private MessageRecyclerAdapter messageRecyclerAdapter;
    private final List<Message> msgList = new ArrayList<>();
    private FirebaseDatabase dbtest = FirebaseDatabase.getInstance();

    private int testcount =0;

    private  FirebaseAuth auth = FirebaseAuth.getInstance();
  //  private DatabaseReference hej =   dbtest.getReference("chatrooms").child(roomId).child("messeges").push();;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_act);
      txtMessage = findViewById(R.id.send_message_text);
         sendMessage = findViewById(R.id.send_message);
        SendImageMessage= findViewById(R.id.send_message_image);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerMesseges = findViewById(R.id.chat_messages);
        LinearLayoutManager manager =new LinearLayoutManager(this);
        recyclerMesseges.setLayoutManager(manager);
        messageRecyclerAdapter = new MessageRecyclerAdapter(Glide.with(getApplicationContext()),msgList,userId);
           // manager.setReverseLayout(true);
            recyclerMesseges.setAdapter(messageRecyclerAdapter);
        db= new DBUtil();


        // Gets the RoomId and Name passed via an Intent from the fragment.
        Bundle extras = getIntent().getExtras();


        if(extras!= null){
         roomId = extras.getString(room_id,"");
         roomName = extras.getString(room_name, "");

            Log.d(roomId, "onCreate: EXTRAS");
            Log.d(roomName, "onCreate: EXTRAS");
        }

        if(getSupportActionBar()!=null){
            setTitle(roomName);
        }


        DatabaseReference hej =   dbtest.getReference("chatrooms").child(roomId).child("count");

       //db.updateCount(hej);

        SendImageMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Vælg et billede"), 1);

            }
        }) ;


        sendMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        if(txtMessage.getText().toString().isEmpty()){

            Toast.makeText(Chat_room_act.this,getString(R.string.send_message_error),Toast.LENGTH_LONG).show();


        }else
            sendMessageRoom();

            }
        });

       getTextMesseges();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_people_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            switch(item.getItemId()){

                case R.id.add_people:

                addPeopleDialog();

                default:
                    return super.onOptionsItemSelected(item);
            }
    }



  


    private void sendMessageRoom(){

        String senderName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString();
        String type = "text";
        String message = txtMessage.getText().toString();
        txtMessage.setText("");
        sendMessage.setEnabled(true);

        db.sendMessageChatRoom(roomId, userId, message,type, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
           //    sendMessage.setEnabled(true);
            }
        });

    }



    public void getTextMesseges(){

        // final List<Message> msgList = new ArrayList<>();


                db.getMessegesFromRoom(roomId, new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Message message = dataSnapshot.getValue(Message.class);
                        msgList.add(message);
                            messageRecyclerAdapter.notifyDataSetChanged();
                            recyclerMesseges.scrollToPosition(msgList.size()-1);
                        recyclerMesseges.setItemViewCacheSize(9);

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
        db.getMessegesFromRoom(roomId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               //   msgList.clear();
                        for(DataSnapshot dsp: dataSnapshot.getChildren()){



                            Log.d("SENDERID","onDataChange: MESSAGE"+ dsp.child("sender_id").getValue().toString());

                   //msgList.add(new Message(dsp.getKey().toString(),dsp.child("sender_id").getValue().toString(),dsp.child("chat_room_id").toString() ,dsp.child("message").getValue().toString(),dsp.child("type").getValue().toString(),null));

                        }

                       // messageRecyclerAdapter = new MessageRecyclerAdapter(Glide.with(getApplicationContext()),msgList,userId);

//                        messageRecyclerAdapter.setHasStableIds(true);


                    recyclerMesseges.setAdapter(messageRecyclerAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {


            final Uri imageuri = data.getData();
            resulturi = imageuri;
            String type = "image";
            UUID uuid = UUID.randomUUID();
            final StorageReference filepath =   FirebaseStorage.getInstance().getReference().child(roomId).child(uuid.toString());
            filepath.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //  Map newImage = new HashMap();
                            //newImage.put("profileImageUrl", uri.toString());
                            String type = "image";

                            db.sendMessageChatRoom(roomId, userId, uri.toString(),type, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                }
                            });




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

    public void addPeopleDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.addPeople_dialog_title);
        // Setups up a dialo window for adding new people.

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_people, null);
        builder.setView(dialogView);
        builder.setTitle(R.string.addPeople_dialog_title);
        EditText editText = (EditText) dialogView.findViewById(R.id.dialog_name);


            builder.setPositiveButton(R.string.addPeople_dialog_add, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setNegativeButton(R.string.addPeople_dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });




    }



}

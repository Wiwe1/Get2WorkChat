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

import com.example.djw.get2workchat.Data_Models.Message;
import com.example.djw.get2workchat.Database.DBUtil;
import com.example.djw.get2workchat.MessageRecyclerAdapter;
import com.example.djw.get2workchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Chat_room_act extends AppCompatActivity {
   // private static  final String CURRENT_USER ="CURRENT_USR";
    private static final String room_id = "room_id";
    private static final String room_name = "room_name";
    private String roomId;
    private String roomName;
    private DBUtil db;
private String userId;
private EditText txtMessage;
private ImageButton sendMessage;
private ImageView SendImageMessage;
private RecyclerView recyclerMesseges;
private MessageRecyclerAdapter messageRecyclerAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_act);
        txtMessage = findViewById(R.id.send_message_text);
        sendMessage = findViewById(R.id.send_message);
        SendImageMessage= findViewById(R.id.send_message_image);
        recyclerMesseges = findViewById(R.id.chat_messages);
        LinearLayoutManager manager =new LinearLayoutManager(this);
       // manager.setReverseLayout(true);
            recyclerMesseges.setLayoutManager(manager);
        db= new DBUtil();

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
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

        SendImageMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

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
        String message = txtMessage.getText().toString();
        txtMessage.setText("");
        sendMessage.setEnabled(false);

        db.sendMessageChatRoom(roomId, userId, message, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                sendMessage.setEnabled(true);
            }
        });

    }



    public void getTextMesseges(){


        db.getMessegesFromRoom(roomId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Message> msgList = new ArrayList<>();
                        for(DataSnapshot dsp: dataSnapshot.getChildren()){

String message = dsp.child("message").getValue().toString();

                            Log.d("SENDERID","onDataChange: MESSAGE"+ dsp.child("sender_id").getValue().toString());

                   msgList.add(new Message(dsp.getKey().toString(),dsp.child("sender_id").getValue().toString(),dsp.child("chat_room_id").toString() ,dsp.child("message").getValue().toString(),null));

                        }

                        messageRecyclerAdapter = new MessageRecyclerAdapter(msgList,userId);
                    recyclerMesseges.setAdapter(messageRecyclerAdapter);
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
            //resulturi = imageuri;
            //profile_image.setImageURI(resulturi);

        }
    }

    public void addPeopleDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.addPeople_dialog_title);
        LayoutInflater inflater = this.getLayoutInflater();

            // Setups up a dialo window for adding new people.
        builder.setView(inflater.inflate(R.layout.dialog_add_people,null))
                .setPositiveButton(R.string.addPeople_dialog_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView addEmail =  findViewById(R.id.dialog_name);
                        if(addEmail.getText() !=null){

                            db.addUserToRoom(addEmail.getText().toString(),roomId);


                        }
                    }
                }).setNegativeButton(R.string.addPeople_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();

    }



}

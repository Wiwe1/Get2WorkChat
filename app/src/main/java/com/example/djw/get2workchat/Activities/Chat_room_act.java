package com.example.djw.get2workchat.Activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

    private static final String room_id = "room_id";
    private static final String room_name = "room_name";

    private Uri resulturi;
    private String roomId;
    private String roomName;
    private DBUtil db;
    private String userId;


    private EditText txtMessage;
    private ImageButton sendMessage;
    private ImageView SendImageMessage;
    private ImageView message_image;
    private Toolbar tbar ;
    private SwipeRefreshLayout refreshLayout;
    private  SearchView searchView;


    private RecyclerView recyclerMesseges;
private MessageRecyclerAdapter messageRecyclerAdapter;
    private final ArrayList<Message> msgList = new ArrayList<>();





    private  FirebaseAuth auth = FirebaseAuth.getInstance();


   //Pagination
    private int itemPos=0;
    private String LastKey="";
    private  static int total_items_load = 10;
    private int currentPage =1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_act);


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
           getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple)));
        }

      txtMessage = findViewById(R.id.send_message_text);
         sendMessage = findViewById(R.id.send_message);
        SendImageMessage= findViewById(R.id.send_message_image);
        refreshLayout = findViewById(R.id.swipe_msg_list);

        recyclerMesseges = findViewById(R.id.chat_messages);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        LinearLayoutManager manager =new LinearLayoutManager(this);
        recyclerMesseges.setLayoutManager(manager);
        MessageRecyclerAdapter.profileImageClick listener = new MessageRecyclerAdapter.profileImageClick() {
            @Override
            public void profileclick(View v, int position) {

                if(v.getId() == R.id.profie_message){
              String userId =       msgList.get(position).getSender_id();


              Intent i = new Intent(getApplicationContext(),Profile_Act.class);
              i.putExtra("userId",userId);
                    startActivity(i);


                    Toast.makeText(Chat_room_act.this, "Clicked From ChatroomAct"+userId, Toast.LENGTH_SHORT).show();

                }

            }
        };
        messageRecyclerAdapter = new MessageRecyclerAdapter(Glide.with(getApplicationContext()),msgList,userId,getApplicationContext(),listener);
           // manager.setReverseLayout(true);,g
            recyclerMesseges.setAdapter(messageRecyclerAdapter);
        db= new DBUtil();




        initUi();

       getTextMesseges();
    }

    private void initUi() {

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage++;
                itemPos = 0;

              getMoreTextMessages();

            }
        });
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_people_menu,menu);

        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)menu.findItem(R.id.search_chat).getActionView();
        searchView.setOnQueryTextListener(onQueryTextListener());
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {


        if(!searchView.isIconified()){

            searchView.setIconified(true);
            return;
        }


        super.onBackPressed();
    }

    private SearchView.OnQueryTextListener onQueryTextListener() {
        return  new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                    messageRecyclerAdapter.getFilter().filter(s);
                return false;

            }

            @Override
            public boolean onQueryTextChange(String s) {
                    messageRecyclerAdapter.getFilter().filter(s);
                    return  false;

            }

        };


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


    public void getMoreTextMessages(){


            db.getMoreMessages(roomId, LastKey, new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Message message = dataSnapshot.getValue(Message.class);

                    msgList.add(itemPos++,message);
                    if(itemPos ==1){
                        String messageKey = dataSnapshot.getKey();
                        LastKey = messageKey;

                    }

                    messageRecyclerAdapter.notifyDataSetChanged();
                    recyclerMesseges.scrollToPosition(msgList.size()-1);
                    refreshLayout.setRefreshing(false);

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


    }


    public void getTextMesseges(){

        // final List<Message> msgList = new ArrayList<>();


                db.getMessegesFromRoom(roomId,total_items_load,currentPage, new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Message message = dataSnapshot.getValue(Message.class);
                      
                      itemPos++;

                      if(itemPos ==1){

                          String messageKey = dataSnapshot.getKey();
                          LastKey = messageKey;
                      }
                        
                        msgList.add(message);
                            messageRecyclerAdapter.notifyDataSetChanged();
                            recyclerMesseges.scrollToPosition(msgList.size()-1);
                    //    recyclerMesseges.setItemViewCacheSize(9);
                        refreshLayout.setRefreshing(false);

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
        final EditText editText = (EditText) dialogView.findViewById(R.id.dialog_name);


            builder.setPositiveButton(R.string.addPeople_dialog_add, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    db.addUserToRoom(editText.getText().toString(),roomId);

                }
            }).setNegativeButton(R.string.addPeople_dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();




    }



}

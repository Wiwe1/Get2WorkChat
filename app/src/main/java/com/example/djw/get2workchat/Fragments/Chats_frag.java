package com.example.djw.get2workchat.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.djw.get2workchat.Activities.Chat_room_act;
import com.example.djw.get2workchat.Activities.CreateRoom_act;
import com.example.djw.get2workchat.Data_Models.Chat_room;
import com.example.djw.get2workchat.Database.DBUtil;
import com.example.djw.get2workchat.Database.DBUtil.firebasCallback;
import com.example.djw.get2workchat.R;
import com.example.djw.get2workchat.chatAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Chats_frag extends Fragment {


    private View v;
    private DBUtil db;
    private RecyclerView chatRooms;
    private chatAdapter chatAdapter;
    private List <Chat_room> rooms = new ArrayList<>();
    private static final String room_id = "room_id";
    private static final String room_name = "room_name";

    public Chats_frag() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_chats, container, false);
             db = new DBUtil();
        FloatingActionButton fab =  v.findViewById(R.id.create_room);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(getActivity(), CreateRoom_act.class);
                startActivity(I);
                Log.d("floating click", "onClick: .");

            }
        });
        chatRooms = v.findViewById(R.id.rooms);
        chatRooms.setLayoutManager(new LinearLayoutManager(getActivity()));

        db.getUserRooms(new firebasCallback() {
            @Override
            public void OnCallBack(List<Chat_room> list) {
                Log.d("CALLBACK",list.toString());
                rooms= list;
                chatAdapter = new chatAdapter(getContext(),rooms,listener);
                chatRooms.setAdapter(chatAdapter);


            }
        });

        return v;
    }

    // Gets all Chatrooms(Names+ID's) from Firebase node Chatrooms and sets the adapter for the recycleView

            private void getChatrooms(){

                final List<Chat_room> rooms = new ArrayList<>();

            db.getRooms(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rooms.clear();


                for (DataSnapshot dsp : dataSnapshot.getChildren()) {



                    rooms.add(new Chat_room(dsp.getKey(),dsp.child("name").getValue().toString(),null));

                }


                chatAdapter = new chatAdapter(getContext(),rooms,listener);
                chatRooms.setAdapter(chatAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                throw  databaseError.toException();

            }
        });

    }



        chatAdapter.OnChatRoomClick listener = new chatAdapter.OnChatRoomClick() {
            @Override
            public void onClick(Chat_room chat_room,int position) {

                Intent i = new Intent(getActivity(), Chat_room_act.class);
                i.putExtra(Chats_frag.room_id, chat_room.getId());
                i.putExtra(Chats_frag.room_name, chat_room.getName());
                i.putExtra("position",position);
                startActivityForResult(i,1);
                Toast.makeText(getContext(), "Clicked"+position + chat_room.getName(), Toast.LENGTH_LONG).show();

            }



            @Override
            public void OnLongclick(Chat_room chat_room,int position) {

                deleteRoomDialog(chat_room.getId(),chat_room.getName(),position);


                Toast.makeText(getContext(), "OnLongClicked"+position+ "" + chat_room.getName(), Toast.LENGTH_LONG).show();
            }
        };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode == Activity.RESULT_OK){

                if(data.getBooleanExtra("delete",true)){


                    int position = data.getIntExtra("position",0);

                    rooms.remove(position);
                    chatAdapter.notifyItemRemoved(position);


                }

            }

        }
    }

    private void deleteRoomDialog(final String room_id, final String room_name, final int position){

        // Setups up a dialo window for deleting a room
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.deleteRoom_dialog_title  );




        builder.setPositiveButton(R.string.deleteRoom_dialog_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                db.deleteRoom(room_id);
                rooms.remove(position);
                chatAdapter.notifyItemRemoved(position);
                Toast.makeText(getContext(), "Deleted room: "+ room_name, Toast.LENGTH_SHORT).show();



            }
        }).setNegativeButton(R.string.deleteRoon_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();

    }

}


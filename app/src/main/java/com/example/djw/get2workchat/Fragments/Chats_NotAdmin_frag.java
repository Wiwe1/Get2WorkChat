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
import android.widget.Toast;

import com.example.djw.get2workchat.Activities.Chat_room_act;
import com.example.djw.get2workchat.Activities.CreateRoom_act;
import com.example.djw.get2workchat.Data_Models.Chat_room;
import com.example.djw.get2workchat.Database.DBUtil;
import com.example.djw.get2workchat.R;
import com.example.djw.get2workchat.chatAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Chats_NotAdmin_frag extends Fragment {


    private View v;
    private DBUtil db;
    private RecyclerView chatRooms;
    private com.example.djw.get2workchat.chatAdapter chatAdapter;
    private List<Chat_room> rooms = new ArrayList<>();
    private static final String room_id = "room_id";
    private static final String room_name = "room_name";

    public Chats_NotAdmin_frag() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_chats_notadmin, container, false);
        db = new DBUtil();

        chatRooms = v.findViewById(R.id.rooms);
        chatRooms.setLayoutManager(new LinearLayoutManager(getActivity()));

        db.getUserRooms(new DBUtil.firebasCallback() {
            @Override
            public void OnCallBack(List<Chat_room> list) {
                Log.d("CALLBACK", list.toString());
                rooms = list;
                chatAdapter = new chatAdapter(getContext(), rooms, listener);
                chatRooms.setAdapter(chatAdapter);


            }
        });

        return v;
    }

    // Gets all Chatrooms


    chatAdapter.OnChatRoomClick listener = new chatAdapter.OnChatRoomClick() {
        @Override
        public void onClick(Chat_room chat_room, int position) {

            Intent i = new Intent(getActivity(), Chat_room_act.class);
            i.putExtra(Chats_NotAdmin_frag.room_id, chat_room.getId());
            i.putExtra(Chats_NotAdmin_frag.room_name, chat_room.getName());
            i.putExtra("position", position);
            startActivityForResult(i, 1);
            Toast.makeText(getContext(), "Clicked" + position + chat_room.getName(), Toast.LENGTH_LONG).show();

        }

        @Override
        public void OnLongclick(Chat_room chat_room, int position) {

        }


    };
}
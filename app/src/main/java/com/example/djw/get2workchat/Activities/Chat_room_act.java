package com.example.djw.get2workchat.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.djw.get2workchat.R;

public class Chat_room_act extends AppCompatActivity {

    private static final String room_id = "room_id";
    private static final String room_name = "room_name";
    private String roomId;
    private String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_act);

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
    }
}

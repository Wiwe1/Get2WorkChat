package com.example.djw.get2workchat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.djw.get2workchat.Data_Models.Chat_room;

import java.util.List;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.chatViewHolder> {
    private List<Chat_room>  chatrooms;
    private Context mContext;
    private  OnChatRoomClick listener;
     public  interface OnChatRoomClick{

        void onClick(Chat_room chat_room);

    }

    public chatAdapter(Context context, List<Chat_room> chatroom,OnChatRoomClick listener) {
        this.mContext = context;
        this.chatrooms = chatroom;
        this.listener = listener;
    }





    @NonNull
    @Override
    public chatAdapter.chatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_room,viewGroup,false);
        return new chatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull chatViewHolder chatViewHolder, int position) {

        chatViewHolder.bind(chatrooms.get(position));

    }

    @Override
    public int getItemCount() {
        return chatrooms.size();
    }

    public class  chatViewHolder extends RecyclerView.ViewHolder{
            TextView name;
            Chat_room room;

        public chatViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_chat_room_name);
        }

        public void bind (final Chat_room room){

                this.room = room;
            name.setText(room.getName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(room);
                }
            });

        }

    }
}

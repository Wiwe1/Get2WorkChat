package com.example.djw.get2workchat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.djw.get2workchat.Data_Models.Chat_room;
import com.example.djw.get2workchat.Database.DBUtil;

import java.util.List;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.chatViewHolder> {
    private List<Chat_room>  chatrooms;
    private Context mContext;
    private  OnChatRoomClick listener;
    private DBUtil db;
     public  interface OnChatRoomClick{

        void onClick(Chat_room chat_room);

    }

    public chatAdapter(Context context, List<Chat_room> chatroom,OnChatRoomClick listener) {
        this.mContext = context;
        this.chatrooms = chatroom;
        this.listener = listener;
        setHasStableIds(true);
    }





    @NonNull
    @Override
    public chatAdapter.chatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       db= new DBUtil();
         View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_room,viewGroup,false);
        return new chatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final chatViewHolder chatViewHolder, int position) {

        chatViewHolder.bind(chatrooms.get(position));
        chatViewHolder.room_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu pop =  new PopupMenu(mContext,chatViewHolder.room_options);

                pop.inflate(R.menu.chat_room_menu);

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){

                            case R.id.delete_room:
                                Toast.makeText(mContext, "clicked"+chatrooms.get(chatViewHolder.getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                                db.deleteRoom(chatrooms.get(chatViewHolder.getAdapterPosition()).getId());
                                break;
                        }

                        return false;
                    }
                });

                pop.show();
            }

        });


    }

    @Override
    public int getItemCount() {
        return chatrooms.size();
    }

    public class  chatViewHolder extends RecyclerView.ViewHolder{
            public TextView name;
           public TextView room_options;
           public  Chat_room room;

        public chatViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_chat_room_name);
            room_options = itemView.findViewById(R.id.item_memu_options);
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

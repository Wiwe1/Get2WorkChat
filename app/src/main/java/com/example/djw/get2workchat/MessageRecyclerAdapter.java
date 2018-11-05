package com.example.djw.get2workchat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.djw.get2workchat.Data_Models.Message;
import com.example.djw.get2workchat.Database.DBUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessageRecyclerAdapter extends RecyclerView.Adapter {
    private DBUtil db;
    private static final int SENT = 0;
    private static final int RECEIVED = 1;


    private  String userId;
    private List<Message> messages;
    private Context mcontext;
    private RequestManager glide;

    public MessageRecyclerAdapter(RequestManager glide, List<Message> messages, String userId) {
        this.glide = glide;
        this.userId = userId;
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        db = new DBUtil();
        View view = null;
        SendMessageHolder holder = null;
        if(viewType == SENT){

            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message_sent,viewGroup,false);
            return new SendMessageHolder(view);
        }
        else if(viewType == RECEIVED) {

            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_messages_recived,viewGroup,false);

            return new ReceivedMessageHolder (view);

        }

        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {

        Message message = messages.get(i);

        db.getUserById(messages.get(i).getSender_id().toString(), new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("userName").getValue().toString();
                String image = dataSnapshot.child("profilePicturePath").getValue().toString();
                if(viewHolder.getItemViewType() == SENT){
                    glide.load(image).apply(new RequestOptions().placeholder(R.drawable.baseline_add_photo_alternate_24)).into(((SendMessageHolder)viewHolder).profileimage);
                //    glide.load(image).into(((SendMessageHolder)viewHolder).profileimage);

                }else{
                    glide.load(image).into(((ReceivedMessageHolder)viewHolder).profileimage);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        switch (viewHolder.getItemViewType()){

            case SENT:
                Log.d("TYPE","MESSAGETYPE"+messages.get(i).getMessage().toString());
                SendMessageHolder vh = (SendMessageHolder) viewHolder;
                if(messages.get(i).getType().equals("text")){
                    vh.imagemessage.setVisibility(View.GONE);
                    vh.textmessage.setText(messages.get(viewHolder.getAdapterPosition()).getMessage());
                   // vh.imagemessage.setVisibility(View.GONE);
                }
                else if(messages.get(i).getType().equals("image")){
                    vh.textmessage.setText("");
                    vh.imagemessage.setVisibility(View.VISIBLE);
                    vh.imagemessage.setImageResource(0);
                    glide.load(messages.get(viewHolder.getAdapterPosition()).getMessage().toString()).into(vh.imagemessage);

                    // String url = messages.get(i).getMessage().toString().trim();
                }
                break;


            case RECEIVED:
                ReceivedMessageHolder receivedVh = (ReceivedMessageHolder)viewHolder;
                if(messages.get(i).getType().equals("text")){
                    receivedVh.imagemessage.setVisibility(View.GONE);
                    receivedVh.textmessage.setText(messages.get(viewHolder.getAdapterPosition()).getMessage());
                  //  ((ReceivedMessageHolder)viewHolder).bind(messages.get(i));
                    break;
                }else if (messages.get(i).getType().equals("image")){

                    receivedVh.textmessage.setText("");
                    receivedVh.imagemessage.setVisibility(View.VISIBLE);
                    receivedVh.imagemessage.setImageResource(0);

                        glide.load(messages.get(i).getMessage().toString()).into(receivedVh.imagemessage);
                        break;

                    }

        }



    }


    @Override
    public int getItemCount() {

        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(messages.get(position).getSender_id().contentEquals(userId)){

            Log.d("SENDERID ", "getItemViewType: " + messages.get(position).getSender_id().toString());
            return SENT;

        }else{
            Log.d("SENDERID ", "getItemViewType: " + messages.get(position).getSender_id().toString());
            return  RECEIVED;

        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}

class SendMessageHolder extends RecyclerView.ViewHolder {

    public TextView textmessage;
    public   ImageView imagemessage;
    public CircleImageView profileimage;
    public SendMessageHolder(@NonNull View itemView) {
        super(itemView);
        profileimage = itemView.findViewById(R.id.profie_message);
        imagemessage = itemView.findViewById(R.id.send_message_image);
        textmessage = itemView.findViewById(R.id.text_message_send);

    }


}

class ReceivedMessageHolder extends  RecyclerView.ViewHolder {
 public    TextView textmessage ;
    public ImageView imagemessage;
    public ImageView profileimage;

    public ReceivedMessageHolder(@NonNull View itemView) {
        super(itemView);
        textmessage = itemView.findViewById(R.id.text_message_recieved);
        imagemessage = itemView.findViewById(R.id.send_message_image);
        profileimage = itemView.findViewById(R.id.profie_message);
    }

    void  bind(Message message){

        textmessage.setText(message.getMessage());

    }
}


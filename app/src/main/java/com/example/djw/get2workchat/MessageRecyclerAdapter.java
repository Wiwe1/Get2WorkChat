package com.example.djw.get2workchat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.djw.get2workchat.Data_Models.Message;

import java.util.List;


public class MessageRecyclerAdapter extends RecyclerView.Adapter {

    private static final int SENT = 0;
    private static final int RECEIVED = 1;


    private  String userId;
    private List<Message> messages;

    public MessageRecyclerAdapter(List<Message> messages, String userId) {
        this.userId = userId;
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

      View view = null;
      SendMessageHolder holder = null;
      if(viewType == SENT){

          view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message_sent,viewGroup,false);
          return new SendMessageHolder(view);
      }
      else if(viewType == RECEIVED) {

          view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_messages_recived,viewGroup,false);

          return new ReceivedMessageHolder(view);

      }

      return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

switch (viewHolder.getItemViewType()){


    case SENT:
        ((SendMessageHolder) viewHolder ).bind(messages.get(i));
        break;
    case RECEIVED:
        ((ReceivedMessageHolder)viewHolder).bind(messages.get(i));
}


    }
/*
    @Override
    public void onBindViewHolder(@NonNull SendMessageHolder Sendholder, int position) {

        if(Sendholder.getItemViewType() == RECEIVED){


        }



    }
*/
    @Override
    public int getItemCount() {

        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {

       if(messages.get(position).getSenderId().contentEquals(userId)){

           Log.d("SENDERID ", "getItemViewType: " + messages.get(position).getSenderId().toString());

               return SENT;

       }else{
           Log.d("SENDERID ", "getItemViewType: " + messages.get(position).getSenderId().toString());
           return  RECEIVED;

       }

    }

  public   class SendMessageHolder extends RecyclerView.ViewHolder {

        TextView textmessage;

        private SendMessageHolder(@NonNull View itemView) {
            super(itemView);
            textmessage = itemView.findViewById(R.id.text_message_send);

        }


        private void bind(Message message){

            textmessage.setText(message.getMessage());

        }



    }

    private class ReceivedMessageHolder extends  RecyclerView.ViewHolder {
      TextView textmessage ;

        private ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            textmessage = itemView.findViewById(R.id.text_message_recieved);

        }

        void  bind(Message message){

            textmessage.setText(message.getMessage());

        }
    }

}

package com.example.djw.get2workchat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.djw.get2workchat.Data_Models.Message;
import com.example.djw.get2workchat.Database.DBUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


    public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.ChatViewHolder> implements Filterable {
    private DBUtil db;
    private static final int SENT = 0;
    private static final int RECEIVED = 1;


    private String userId;

    private List<Message> messages;
    private List<Message> orig;
    private Context mcontext;
    private profileImageClick listener;
    private RequestManager glide;


    public interface profileImageClick {

        public void profileclick(View v, int position);

    }


    public MessageRecyclerAdapter(RequestManager glide, List<Message> messages, String userId, Context mcontext, profileImageClick listener) {
        this.glide = glide;
        this.userId = userId;
        this.messages = messages;
        this.mcontext = mcontext;
        this.listener = listener;
        this.orig = messages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        db = new DBUtil();
        View view;
        //  SendMessageHolder holder = null;
        if (viewType == SENT) {

            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message_sent, viewGroup, false);
            //      return new SendMessageHolder(view);
        } else {

            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_messages_recived, viewGroup, false);

            //return new ReceivedMessageHolder (view);

        }

        return new ChatViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ChatViewHolder chatViewHolder, int i) {


        Message message = messages.get(i);

/*
        ((ReceivedMessageHolder)viewHolder).profileimage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
*/
        db.getUserById(messages.get(i).getSender_id().toString(), new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("userName").getValue().toString();
                String image = dataSnapshot.child("profilePicturePath").getValue().toString();
                if (chatViewHolder.getItemViewType() == SENT) {
                    glide.load(image).apply(new RequestOptions().placeholder(R.drawable.baseline_add_photo_alternate_24)).into(chatViewHolder.profileimage);
                    //    glide.load(image).into(((SendMessageHolder)viewHolder).profileimage);

                } else {

                    glide.load(image).into(chatViewHolder.profileimage);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d("Messagenumber", "mesagenum " + messages.get(i).getMessage_number());


        if (messages.get(i).getType().equals("text")) {

            if (messages.get(chatViewHolder.getAdapterPosition()).getMessage_number() != null) {
                //  vh.textmessage_num.setText("");
                chatViewHolder.textmessage_num.setVisibility(View.VISIBLE);
                chatViewHolder.textmessage_num.setText(messages.get(chatViewHolder.getAdapterPosition()).getMessage_number().toString());
            } else {

                chatViewHolder.textmessage_num.setText("");

            }

            chatViewHolder.imagemessage.setVisibility(View.GONE);
            chatViewHolder.textmessage.setVisibility(View.VISIBLE);
            chatViewHolder.textmessage.setText(messages.get(chatViewHolder.getAdapterPosition()).getMessage());
            // vh.imagemessage.setVisibility(View.GONE);
        } else if (messages.get(i).getType().equals("image")) {

            if (messages.get(chatViewHolder.getAdapterPosition()).getMessage_number() != null) {

                chatViewHolder.textmessage.setVisibility(View.VISIBLE);
                String num = messages.get(chatViewHolder.getAdapterPosition()).getMessage_number().toString();
                chatViewHolder.textmessage_num.setVisibility(View.VISIBLE);
                chatViewHolder.textmessage_num.setText(num);
            } else {

                chatViewHolder.textmessage_num.setText("");

            }


            chatViewHolder.textmessage.setVisibility(View.GONE);
            // vh.textmessage.setText("");
            chatViewHolder.imagemessage.setVisibility(View.VISIBLE);
            chatViewHolder.imagemessage.setImageResource(0);
            //vh.textmessage_num.setText(messages.get(viewHolder.getAdapterPosition()).getMessage_number().toString());
            glide.load(messages.get(chatViewHolder.getAdapterPosition()).getMessage().toString()).into(chatViewHolder.imagemessage);


            // String url = messages.get(i).getMessage().toString().trim(); android:text="hej"
        }


    }


    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    @Override
    public int getItemViewType(int position) {

        if (messages.get(position).getSender_id().contentEquals(userId)) {

            Log.d("SENDERID ", "getItemViewType: " + messages.get(position).getSender_id().toString());
            return SENT;

        } else {
            Log.d("SENDERID ", "getItemViewType: " + messages.get(position).getSender_id().toString());
            return RECEIVED;

        }
    }

    @Override
    public long getItemId(int position) {

        return super.getItemId(position);
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                oReturn.values = messages;
                oReturn.count = messages.size();


                if (constraint.length() == 0) {

                    messages = orig;

                } else {
                    final ArrayList<Message> resultls = new ArrayList<Message>();
                    for (final Message msg : messages) {
                        if ((msg.getMessage_number() != null && msg.getMessage_number().toString().contains(constraint))) {
                            resultls.add(msg);
                        }

                    }

                    messages = resultls;

                }

                oReturn.values = messages;
                oReturn.count = messages.size();
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {


                messages = (ArrayList<Message>) results.values;
                notifyDataSetChanged();

            }
        };


    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        public TextView textmessage_num;
        public TextView textmessage;
        public ImageView imagemessage;
        public CircleImageView profileimage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            profileimage = itemView.findViewById(R.id.profie_message);
            imagemessage = itemView.findViewById(R.id.message_image);
            textmessage = itemView.findViewById(R.id.text_message);
            textmessage_num = itemView.findViewById(R.id.text_message_number);
        }

    }
}
/*

    class ReceivedMessageHolder extends  RecyclerView.ViewHolder {
        public    TextView textmessage ;
        public ImageView imagemessage;
        public CircleImageView profileimage;
        public TextView textmessage_num;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);

            textmessage = itemView.findViewById(R.id.text_message_recieved);
            imagemessage = itemView.findViewById(R.id.send_message_image);
            profileimage = itemView.findViewById(R.id.profie_message);
            textmessage_num= itemView.findViewById(R.id.text_message_number);

                    profileimage.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if(listener!=null)
                            {
                                listener.profileclick(v,getAdapterPosition());
                            }


                            return true;
                        }
                    });

           /* profileimage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });




        }

        void  bind(Message message){

            textmessage.setText(message.getMessage());

        }
    }
}


}
*/

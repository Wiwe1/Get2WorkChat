package com.example.djw.get2workchat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.djw.get2workchat.Activities.Profile_Act;
import com.example.djw.get2workchat.Data_Models.Message;
import com.example.djw.get2workchat.Database.DBUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessageRecyclerAdapter extends RecyclerView.Adapter implements Filterable {
    private DBUtil db;
    private static final int SENT = 0;
    private static final int RECEIVED = 1;


    private  String userId;
    private List<Message> messages;
    private List<Message> orig;
    private Context mcontext;
    private  profileImageClick listener;
    private RequestManager glide;




    public interface profileImageClick{

        public void profileclick(View v,int position);

    }


    public MessageRecyclerAdapter( RequestManager glide, List<Message> messages, String userId,Context mcontext,profileImageClick listener) {
        this.glide = glide;
        this.userId = userId;
        this.messages = messages;
        this.mcontext = mcontext;
        this.listener = listener;
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

        Log.d("Messagenumber", "mesagenum "+  messages.get(i).getMessage_number());


        switch (viewHolder.getItemViewType()){



            case SENT:
                Log.d("TYPE","MESSAGETYPE"+messages.get(i).getMessage().toString());
                SendMessageHolder vh = (SendMessageHolder) viewHolder;
                if(messages.get(i).getType().equals("text")){
                    vh.imagemessage.setVisibility(View.GONE);
                    vh.textmessage.setVisibility(View.VISIBLE);
                        if(messages.get(viewHolder.getAdapterPosition()).getMessage_number()!= null){
                        //  vh.textmessage_num.setText("");
                       vh.textmessage_num.setText(messages.get(viewHolder.getAdapterPosition()).getMessage_number().toString());
                    }

                    vh.textmessage.setText(messages.get(viewHolder.getAdapterPosition()).getMessage());
                   // vh.imagemessage.setVisibility(View.GONE);
                }
                else if(messages.get(i).getType().equals("image")){
                    vh.textmessage.setVisibility(View.GONE);
                  // vh.textmessage.setText("");
                    vh.imagemessage.setVisibility(View.VISIBLE);
                    vh.imagemessage.setImageResource(0);
                    //vh.textmessage_num.setText(messages.get(viewHolder.getAdapterPosition()).getMessage_number().toString());
                    glide.load(messages.get(viewHolder.getAdapterPosition()).getMessage().toString()).into(vh.imagemessage);


                    // String url = messages.get(i).getMessage().toString().trim(); android:text="hej"
                }
                break;


            case RECEIVED:
                final ReceivedMessageHolder receivedVh = (ReceivedMessageHolder)viewHolder;

//                profilePopup(viewHolder, receivedVh);


                if(messages.get(i).getType().equals("text")){
                    receivedVh.imagemessage.setVisibility(View.GONE);
                    if(messages.get(viewHolder.getAdapterPosition()).getMessage_number()!= null){
                        //receivedVh.textmessage_num.setText("");
                        //receivedVh.textmessage_num.setText(messages.get(viewHolder.getAdapterPosition()).getMessage_number().toString());
                    }
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

    private void profilePopup(@NonNull final RecyclerView.ViewHolder viewHolder, final ReceivedMessageHolder receivedVh) {
        receivedVh.profileimage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mcontext, "ClickdProfile "+messages.get(viewHolder.getAdapterPosition()).getSender_id(), Toast.LENGTH_SHORT).show();

                PopupMenu pop =  new PopupMenu(mcontext,receivedVh.profileimage);

                pop.inflate(R.menu.message_menu);

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){

                            case R.id.message_menu_profile:
                                Intent i = new Intent(mcontext,Profile_Act.class);
                                i.putExtra("UserId",messages.get(viewHolder.getAdapterPosition()).getSender_id());
                                mcontext.startActivity(i);
                                break;
                        }

                        return false;
                    }
                });

                pop.show();


                return  false;
            }
        });
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
    @Override
    public Filter getFilter() {

        return  new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                final  FilterResults oReturn = new FilterResults();
               final ArrayList<Message> resultls = new ArrayList<Message>();

                if(orig == null)
                    orig = messages;
                    if(constraint!=null){
                        if(orig!=null && orig.size()>0){
                            for(final Message msg: orig){

                                    msg.getMessage_number();
                                if(msg.getMessage_number().toString().contains(constraint)){

                                    resultls.add(msg);

                                }


                            }
                            oReturn.values  = resultls;

                        }



                    }


                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {


                messages = (ArrayList<Message>) results.values;
                notifyDataSetChanged();

            }
        };



    }

    class SendMessageHolder extends RecyclerView.ViewHolder {
        public  TextView textmessage_num;
        public TextView textmessage;
        public   ImageView imagemessage;
        public CircleImageView profileimage;
        public SendMessageHolder(@NonNull View itemView) {
            super(itemView);
            profileimage = itemView.findViewById(R.id.profie_message);
            imagemessage = itemView.findViewById(R.id.send_message_image);
            textmessage = itemView.findViewById(R.id.text_message_send);
            textmessage_num = itemView.findViewById(R.id.text_message_number);
        }


    }


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
*/
            textmessage_num= itemView.findViewById(R.id.text_message_number);


        }

        void  bind(Message message){

            textmessage.setText(message.getMessage());

        }
    }
}
/*
class SendMessageHolder extends RecyclerView.ViewHolder {
    public  TextView textmessage_num;
    public TextView textmessage;
    public   ImageView imagemessage;
    public CircleImageView profileimage;
    public SendMessageHolder(@NonNull View itemView) {
        super(itemView);
        profileimage = itemView.findViewById(R.id.profie_message);
        imagemessage = itemView.findViewById(R.id.send_message_image);
        textmessage = itemView.findViewById(R.id.text_message_send);
        textmessage_num = itemView.findViewById(R.id.text_message_number);
    }


}
*/
/*
class ReceivedMessageHolder extends  RecyclerView.ViewHolder {
 public    TextView textmessage ;
    public ImageView imagemessage;
    public ImageView profileimage;
    public TextView textmessage_num;

    public ReceivedMessageHolder(@NonNull View itemView) {
        super(itemView);
        textmessage = itemView.findViewById(R.id.text_message_recieved);
        imagemessage = itemView.findViewById(R.id.send_message_image);
        profileimage = itemView.findViewById(R.id.profie_message);
        textmessage_num= itemView.findViewById(R.id.text_message_number);
    }

    void  bind(Message message){

        textmessage.setText(message.getMessage());

    }
}
*/

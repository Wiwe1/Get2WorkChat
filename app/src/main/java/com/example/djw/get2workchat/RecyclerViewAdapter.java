package com.example.djw.get2workchat;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.djw.get2workchat.Data_Models.Contact;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context mContext ;
    List<Contact> mData;
    Dialog mydialog;

    public RecyclerViewAdapter(Context mContext ,List<Contact> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parrent, int i) {
        View v;
        v=LayoutInflater.from(mContext).inflate(R.layout.item_contact,parrent,false);
        final MyViewHolder vHolder = new MyViewHolder(v);
        vHolder.item_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText((mContext),"Test click"+ String.valueOf(vHolder.getAdapterPosition()),Toast.LENGTH_SHORT).show();

            }
        });
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tv_name.setText(mData.get(position).getName());
        holder.tv_phone.setText(mData.get(position).getPhone_num());
        holder.img.setImageResource(mData.get(position).getPhoto());
    }

    @Override
    public int getItemCount() {
      return mData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout item_contact;
        private TextView tv_name;
        private  TextView tv_phone;
        private ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_contact = (LinearLayout)itemView.findViewById(R.id.linear_contact_item);
            tv_name = (TextView)itemView.findViewById(R.id.name_contact);
            tv_phone = (TextView)itemView.findViewById(R.id.phone_contact);
            img = (ImageView)itemView.findViewById(R.id.img_contact);

        }
    }

}

package com.example.djw.get2workchat.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.djw.get2workchat.Data_Models.Contact;
import com.example.djw.get2workchat.R;
import com.example.djw.get2workchat.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class Contacts_frag extends Fragment {

    private RecyclerView vRecycler;
    List<Contact> lstContacs;
   private View v;
    public Contacts_frag() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

     v = inflater.inflate(R.layout.frag_contacs,container,false);
 vRecycler = (RecyclerView)v.findViewById(R.id.contact_recycler);
 RecyclerViewAdapter recycleAdapter = new RecyclerViewAdapter(getContext(),lstContacs);
        vRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        vRecycler.setAdapter(recycleAdapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lstContacs = new ArrayList<>();
        lstContacs.add(new Contact("Kristian Wiwe","28346876",R.drawable.wiwe));



    }
}

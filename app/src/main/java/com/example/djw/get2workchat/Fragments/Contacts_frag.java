package com.example.djw.get2workchat.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.djw.get2workchat.R;

public class Contacts_frag extends Fragment {

   private View v;
    public Contacts_frag() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

     v = inflater.inflate(R.layout.frag_contact,container,false);

        return v;
    }


}

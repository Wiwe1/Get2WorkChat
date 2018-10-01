package com.example.djw.get2workchat.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.djw.get2workchat.Fragments.Calls_frag;
import com.example.djw.get2workchat.Fragments.Chats_frag;
import com.example.djw.get2workchat.Fragments.Contacts_frag;
import com.example.djw.get2workchat.R;
import com.example.djw.get2workchat.ViewPagerAdapter;


public class Main_act extends AppCompatActivity {

    private TabLayout tab;
    private ViewPagerAdapter vPageAdapp;
    private ViewPager vPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tab = (TabLayout) findViewById(R.id.tab_layout);
        vPage = (ViewPager) findViewById(R.id.viewpage);
        vPageAdapp = new ViewPagerAdapter(getSupportFragmentManager());

        vPageAdapp.AddFragment(new Chats_frag(),"Chats ");
        vPageAdapp.AddFragment(new Contacts_frag(),"Contacts");
        vPageAdapp.AddFragment(new Calls_frag(),"Calls");




        vPage.setAdapter(vPageAdapp);
        tab.setupWithViewPager(vPage);
    }
}

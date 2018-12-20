package com.example.djw.get2workchat.Activities;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.djw.get2workchat.Data_Models.User;
import com.example.djw.get2workchat.Database.DBUtil;
import com.example.djw.get2workchat.Fragments.Chats_NotAdmin_frag;
import com.example.djw.get2workchat.Fragments.Profile_frag;
import com.example.djw.get2workchat.Fragments.Chats_frag;
import com.example.djw.get2workchat.R;
import com.example.djw.get2workchat.ViewPagerAdapter;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static android.app.PendingIntent.getActivity;


public class Main_act extends AppCompatActivity {

    private TextView txt;
    private TabLayout tab;
    private ViewPagerAdapter vPageAdapp;
    private ViewPager vPage;
    private User testUsr;
    private Toolbar tbar;
    private  String usrEmail;
    private String test;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  test   = getIntent().getStringExtra("UserAuthId");

 usrEmail= FirebaseAuth.getInstance().getCurrentUser().getEmail();


        tbar = findViewById(R.id.toolbar);
        tab =  findViewById(R.id.tab_layout);
        vPage =  findViewById(R.id.viewpage);
        tbar.setTitleTextColor(Color.WHITE);
        vPageAdapp = new ViewPagerAdapter(getSupportFragmentManager());

        if(usrEmail.endsWith("@get2work.dk")){

            vPageAdapp.AddFragment(new Chats_frag(),"Chats ");


        }else{
            vPageAdapp.AddFragment(new Chats_NotAdmin_frag(),"Chats");
            Toast.makeText(this, "not admin", Toast.LENGTH_LONG).show();

        }


        vPageAdapp.AddFragment(new Profile_frag(),"Profile");
//        vPageAdapp.AddFragment(new Contacts_frag(),"Contacts");

        vPage.setAdapter(vPageAdapp);
        tab.setupWithViewPager(vPage);
        setSupportActionBar(tbar);

     DBUtil db = new DBUtil();







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_logout:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                {
                                    startActivity(new Intent(Main_act.this,SignIn_act.class));
                                    finish();
                                }

                            }
                        });



                default:
                    return super.onOptionsItemSelected(item);



        }


    }

}

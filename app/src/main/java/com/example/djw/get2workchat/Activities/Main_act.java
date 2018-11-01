package com.example.djw.get2workchat.Activities;
import android.content.Intent;
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
import com.example.djw.get2workchat.Fragments.Calls_frag;
import com.example.djw.get2workchat.Fragments.Chats_frag;
import com.example.djw.get2workchat.Fragments.Contacts_frag;
import com.example.djw.get2workchat.R;
import com.example.djw.get2workchat.ViewPagerAdapter;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.app.PendingIntent.getActivity;


public class Main_act extends AppCompatActivity {

    private TextView txt;
    private TabLayout tab;
    private ViewPagerAdapter vPageAdapp;
    private ViewPager vPage;
    private User testUsr;
    private Toolbar tbar;
    private String test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  test   = getIntent().getStringExtra("UserAuthId");



        tbar = findViewById(R.id.toolbar);
        tab =  findViewById(R.id.tab_layout);
        vPage =  findViewById(R.id.viewpage);
        vPageAdapp = new ViewPagerAdapter(getSupportFragmentManager());

        vPageAdapp.AddFragment(new Chats_frag(),"Chats ");
        vPageAdapp.AddFragment(new Contacts_frag(),"Contacts");
        vPageAdapp.AddFragment(new Calls_frag(),"Calls");
        vPage.setAdapter(vPageAdapp);
        tab.setupWithViewPager(vPage);
        setSupportActionBar(tbar);

     DBUtil db = new DBUtil();


   //  db.getUserRooms();

        db.getUserById("test", new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

      //  testUsr = new User("bob",null);


//FirebaseDatabase database = FirebaseDatabase.getInstance();
     //DatabaseReference myRef = database.getReference("users");
   //     String userId = myRef.push().getKey();
//        myRef.    child(userId).setValue(testUsr);
 //       myRef.push();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);


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



            case R.id.action_profile:
                   startActivity(new Intent(Main_act.this,Profile_Act.class));

                default:
                    return super.onOptionsItemSelected(item);



        }


    }

}

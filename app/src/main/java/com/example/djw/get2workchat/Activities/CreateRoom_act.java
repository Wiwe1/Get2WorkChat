package com.example.djw.get2workchat.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.djw.get2workchat.Database.DBUtil;
import com.example.djw.get2workchat.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

public class CreateRoom_act extends AppCompatActivity {

   private EditText roomName;
    private DBUtil db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_create_room);

        roomName = (EditText) findViewById(R.id.create_room_name);
        setTitle("Create Room");

        if(getSupportActionBar()!= null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_clear_24);



        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_room_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        db = new DBUtil();
        switch (item.getItemId()){

            case android.R.id.home:
                finish();
                return  true;

            case R.id.create_room:
                if(roomName.getText().toString().isEmpty()){

                    Toast.makeText(this,"Please enter room name",Toast.LENGTH_LONG).show();

                }else {

                    try{
                        db.CreateChatroom(roomName.getText().toString());
                        finish();

                    }catch (Exception e){

                        e.printStackTrace();

                    }



                }
          default:
              return super.onOptionsItemSelected(item);
        }

    }
}

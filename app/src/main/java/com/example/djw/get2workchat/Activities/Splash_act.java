package com.example.djw.get2workchat.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
public class Splash_act extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            Intent i = new Intent(this,SignIn_act.class);
            startActivity(i);

        }else
        {
            Intent i = new Intent(this,Main_act.class);
            startActivity(i);

        }
        finish();

    }
}

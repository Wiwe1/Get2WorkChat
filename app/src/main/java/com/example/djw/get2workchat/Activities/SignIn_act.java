package com.example.djw.get2workchat.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.djw.get2workchat.Database.DBUtil;
import com.example.djw.get2workchat.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUserMetadata;


import java.util.Arrays;
import java.util.List;

public class SignIn_act extends AppCompatActivity {
    private int RC_SIGN_IN = 1;
    private DBUtil db;
    private List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder()
            .setAllowNewAccounts(true)
            .setRequireName(true)
            .build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_act);

        Button sign_in = (Button) findViewById(R.id.account_sign_in);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.common_full_open_on_phone)
                        .setIsSmartLockEnabled(false)
                        .build();


                startActivityForResult(i, RC_SIGN_IN);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        db = new DBUtil();

        if (requestCode == RC_SIGN_IN) {

            IdpResponse respons = IdpResponse.fromResultIntent(data);

            respons.getError();

            if (resultCode == Activity.RESULT_OK) {
                ProgressDialog pDialog = ProgressDialog.show(this, "Setting up",
                        "Setting up your account");
                if (isNewUser()) {


                    db.intCurrentUser();

                    Intent i = new Intent(this, Main_act.class);
                   // i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                 //   i.putExtra("UserAuthId",FirebaseAuth.getInstance().getUid().toString());
                    startActivity(i);

                    pDialog.dismiss();
                } else if (!isNewUser()) {

                    Intent i = new Intent(this, Main_act.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.putExtra("UserAuthId",FirebaseAuth.getInstance().getUid().toString());
                    startActivity(i);
                    pDialog.dismiss();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

                if (respons == null) return;

                //if(respons.getError() == ErrorCodes.NO_NETWORK)

            }

        }
    }

    public boolean isNewUser() {
        FirebaseUserMetadata metadata = FirebaseAuth.getInstance().getCurrentUser().getMetadata();

        return metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp();

    }

}

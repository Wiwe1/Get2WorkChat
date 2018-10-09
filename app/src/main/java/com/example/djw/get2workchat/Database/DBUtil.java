package com.example.djw.get2workchat.Database;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.djw.get2workchat.Data_Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DBUtil {




    FirebaseDatabase db = FirebaseDatabase.getInstance();


    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference myref = db.getReference("users").child(auth.getUid());
    public void intCurrentUser() {





        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){



                        String Auth_id= auth.getUid();

                        User user = new User(auth.getCurrentUser().getDisplayName().toString(),auth.getCurrentUser().getEmail().toString());
                        myref.setValue(user);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        myref.addListenerForSingleValueEvent(valueEventListener);

    }




}

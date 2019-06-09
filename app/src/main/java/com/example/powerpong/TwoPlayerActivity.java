package com.example.powerpong;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class TwoPlayerActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private String mGameCode;
    private String mOpp;

    private boolean mIsPlayer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();


        mRef = mDatabase.getReference("users/" + mUser.getUid());

        mOpp = "";
        mGameCode = "";

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()) {
                    if(child.getKey() == "opp") {
                        mOpp = (String) (child.getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef = mDatabase.getReference("games");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()) {
                    if (child.getKey().equals(mUser.getUid() + mOpp)){
                        mGameCode = mUser.getUid() + mOpp;
                        if(child.getValue().equals(mUser.getUid())){
                            mIsPlayer1 = true;
                        }
                        else{
                            mIsPlayer1 = false;
                        }
                    }
                    else if(child.getKey().equals(mOpp + mUser.getUid())){
                        mGameCode = mOpp + mUser.getUid();
                        if(child.getValue().equals(mUser.getUid())){
                            mIsPlayer1 = true;
                        }
                        else{
                            mIsPlayer1 = false;
                        }
                    }
                }
            }
            //
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if(mUser == null){
            Intent i = new Intent(this, Login2Activity.class);
            startActivity(i);
        }
//        setContentView(R.layout.activity_single_player);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        Log.d("tribe", "and you are the reason now");
        setContentView(new GameView2(this, mUser, mDatabase));
    }
}

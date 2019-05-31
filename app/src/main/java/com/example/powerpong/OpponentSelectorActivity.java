package com.example.powerpong;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class OpponentSelectorActivity extends AppCompatActivity {

    private Button mJoinGameButton;
    private Button mPrivateGameButton;
    private Button mPublicGameButton;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private Map<String, String> mMap;

    private boolean mGameCodePresent;


    public void deleteJaunt(String ref){
        DatabaseReference delref = mDatabase.getReference(ref);
        delref.removeValue();
    }
    public void joinPublicGame(){

        DatabaseReference userRef = mDatabase.getReference("users/" + mUser.getUid()
        + "gamecode");

        mRef = mDatabase.getReference("looking");
        mRef.addValueEventListener(new ValueEventListener() {

            // to future vinay:
            // what the code does is set up a game object which has the ball values
            // like where the ball is, and who's the main player to base calculuations
            // on if we find two ppl to match up

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("yall", dataSnapshot.toString());
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Log.d("llay", child.toString());
                    try {
                        Thread.sleep(500);
                    }
                    catch (InterruptedException e){
                        Log.d("error", e.toString());
                    }
                    if(child.getValue().equals("public") && !mUser.getUid().equals(child.getKey())
                    && !mGameCodePresent){
                        Log.d("hii", "im here");
//                        mDatabase.getReference("users/" + mUser.getUid() + "/opp")
//                                .setValue(child.getKey());
//                        mDatabase.getReference("users/" + child.getKey() + "/opp")
//                                .setValue(mUser.getUid());
                        String gameCode = mUser.getUid() + child.getKey();

                        mDatabase.getReference("users/" + mUser.getUid() + "/gamecode")
                                .setValue(gameCode);

                        mDatabase.getReference("users/" + child.getKey() + "/gamecode")
                                .setValue(gameCode);

                        DatabaseReference gameRef = mDatabase.getReference("games/"
                                + gameCode);

                        mMap.put("player1", mUser.getUid());
                        gameRef.setValue(mMap);


                        deleteJaunt("looking/"
                                + mUser.getUid());

                        deleteJaunt("looking/"
                                + child.getKey());

                        Intent i = new Intent(OpponentSelectorActivity.this,
                                TwoPlayerActivity.class);

                        startActivity(i);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        DatabaseReference ref2 = mDatabase.getReference("users/" + mUser.getUid() + "/gamecode");
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mGameCodePresent = true;
                Log.d("bruh", dataSnapshot.toString());
                if(dataSnapshot.getValue() != null){
                    Intent i = new Intent(OpponentSelectorActivity.this,
                            TwoPlayerActivity.class);

                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opponent_selector);

        mGameCodePresent = false;

        mMap = new HashMap<String, String>();
        mMap.put("bx", "50");
        mMap.put("by", "50");

        mAuth = FirebaseAuth.getInstance();

        mUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();



        mJoinGameButton = (Button)findViewById(R.id.join_game_button);
        mPrivateGameButton = (Button)findViewById(R.id.private_game_button);
        mPublicGameButton = (Button)findViewById(R.id.public_game_button);

        mJoinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                joinPublicGame();
            }
        });

        mPrivateGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef = mDatabase.getReference("looking/" + mUser.getUid());
                mRef.setValue("private");

                mRef = mDatabase.getReference("looking");

                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.getValue() == "private"){
//                            mDatabase.getReference("users/" + mUser.getUid() + "/opp")
//                                    .setValue(dataSnapshot.getKey());
//                        }
                    }
//
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        mPublicGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef = mDatabase.getReference("looking/" + mUser.getUid());
                mRef.setValue("public");
                joinPublicGame();
            }
        });
    }

}


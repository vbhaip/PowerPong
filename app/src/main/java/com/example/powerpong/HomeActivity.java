package com.example.powerpong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class HomeActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private TextView mPointsView;

    private Button mSinglePlayerButton;
    private Button mLogOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        mUser = mAuth.getCurrentUser();

        mSinglePlayerButton = (Button)findViewById(R.id.single_player_button);

        mSinglePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, SinglePlayerActivity.class);
                startActivity(i);
            }
        });

        mLogOutButton = (Button)findViewById(R.id.logout_button);

        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent i = new Intent(HomeActivity.this, Login2Activity.class);
                startActivity(i);
            }
        });


        mPointsView = findViewById(R.id.points_view);

        mDatabase = FirebaseDatabase.getInstance();

        mRef = mDatabase.getReference("users/" + mUser.getUid() + "/score");

        mRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                long score = (long)(dataSnapshot.getValue());

                mPointsView.setText("Total Score: " + score);
                // ...
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        });

//        Log.d("dfasfal", mUser);

    }
}

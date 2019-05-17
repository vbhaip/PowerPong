package com.example.powerpong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Button mSinglePlayerButton;
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
//        Log.d("dfasfal", mUser);

    }
}

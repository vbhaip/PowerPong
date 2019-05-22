package com.example.powerpong;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login2Activity extends AppCompatActivity {


    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mSignInButton;
    private Button mRegisterButton;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        mEmailView = (EditText)findViewById(R.id.email_field);
        mPasswordView = (EditText)findViewById(R.id.password_field);
        mSignInButton = (Button)findViewById(R.id.sign_in_button);
        mRegisterButton = (Button)findViewById(R.id.register_button);

        mAuth = FirebaseAuth.getInstance();

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        mDatabase = FirebaseDatabase.getInstance();




    }

    private void updateUI(FirebaseUser user){
        if(user != null){
            Log.d("user email", user.getEmail());
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        }
    }

    private void login(){
        String email = mEmailView.getText().toString();
        String pass = mPasswordView.getText().toString();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("loginemailsuccess", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user
                            Log.d("loginemailfail", "createUserWithEmail:fail");
                            Toast.makeText(Login2Activity.this, "Sign In failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void register(){
        String email = mEmailView.getText().toString();
        String pass = mPasswordView.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("registeremailsuccess", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            DatabaseReference ref = mDatabase.getReference("users/" + mAuth.getUid() + "/score");
                            ref.setValue(0);

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("registeremailfail", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Login2Activity.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

}

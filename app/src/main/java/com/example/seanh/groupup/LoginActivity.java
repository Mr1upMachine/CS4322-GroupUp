package com.example.seanh.groupup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private final String LOGTAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText editLoginEmail, editLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editLoginEmail = (EditText) findViewById(R.id.editRegisterEmail);
        editLoginPassword = (EditText) findViewById(R.id.editRegisterPasswordReenter);
        mAuth = FirebaseAuth.getInstance();


        //Auto sign in
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(LOGTAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    LoginActivity.this.startActivity( new Intent(LoginActivity.this, MainActivity.class) );
                    finish();
                } else {
                    // User is signed out
                    Log.d(LOGTAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editLoginEmail.getText().toString().isEmpty() && !editLoginPassword.getText().toString().isEmpty()) {
                    signIn(editLoginEmail.getText().toString(), editLoginPassword.getText().toString());
                }
                else{
                    Toast.makeText(getApplicationContext(),"Fields are empty", Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.textNeedAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                i.putExtra("email", editLoginEmail.getText().toString());
                i.putExtra("pass", editLoginPassword.getText().toString());
                LoginActivity.this.startActivity( i );
            }
        });

        findViewById(R.id.textLoginForgot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"That sucks doesn't it?", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //auto sign in
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        //auto sign in
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void signIn(final String email, final String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOGTAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            LoginActivity.this.startActivity( new Intent(LoginActivity.this, MainActivity.class) );
                            finish(); //ends current activity
                        }
                        else {
                            Log.w(LOGTAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}


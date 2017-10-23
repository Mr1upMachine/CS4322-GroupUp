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

public class RegisterActivity extends AppCompatActivity {

    private final String LOGTAG = "RegisterActivity";
    private FirebaseAuth mAuth;

    EditText editEmail;
    EditText editPassword;

    private String emailFromLogin = "", passFromLogin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Creates remaining objects from above
        editEmail = (EditText) findViewById(R.id.editRegisterEmail);
        editPassword = (EditText) findViewById(R.id.editRegisterPassword);

        mAuth = FirebaseAuth.getInstance();

        //gets information from LoginActivity and places it in respective fields
        Intent intentFromLogin = getIntent();
        emailFromLogin = intentFromLogin.getStringExtra("email");
        passFromLogin = intentFromLogin.getStringExtra("pass");
        editEmail.setText(emailFromLogin);
        editPassword.setText(passFromLogin);
        
        //Submit button OnClickListener
        findViewById(R.id.buttonRegisterSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(editEmail.getText().toString(), editPassword.getText().toString());
            }
        });
    }

    public void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOGTAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                            //TODO add user to database
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        }
                    }
                });
    }
}

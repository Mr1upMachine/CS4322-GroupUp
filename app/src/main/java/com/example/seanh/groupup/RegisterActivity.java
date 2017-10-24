package com.example.seanh.groupup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private final String LOGTAG = "RegisterActivity";
    private FirebaseAuth mAuth;

    private EditText editEmail, editPassword, editPasswordReenter, editFirstName, editLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupKeyboardHide(findViewById(R.id.layoutRegister)); //This auto-hides the keyboard

        //Creates remaining objects from above
        editEmail = (EditText) findViewById(R.id.editRegisterEmail);
        editPassword = (EditText) findViewById(R.id.editRegisterPassword);
        editPasswordReenter = (EditText) findViewById(R.id.editRegisterPasswordReenter);
        editFirstName = (EditText) findViewById(R.id.editRegisterFirstName);
        editLastName = (EditText) findViewById(R.id.editRegisterLastName);

        mAuth = FirebaseAuth.getInstance();

        //gets information from LoginActivity and places it in respective fields
        final Intent intentFromLogin = getIntent();
        final String emailFromLogin = intentFromLogin.getStringExtra("email");
        final String passFromLogin = intentFromLogin.getStringExtra("pass");
        editEmail.setText(emailFromLogin);
        editPassword.setText(passFromLogin);








        //Submit button OnClickListener
        findViewById(R.id.buttonRegisterSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editEmail.getText().toString().isEmpty()){
                    editEmail.setError("Field cannot be empty");
                }
                else if( !editPassword.getText().toString().equals( editPasswordReenter.getText().toString()) ) {
                    editPasswordReenter.setError("Passwords do not match");
                }
                else if(editFirstName.getText().toString().isEmpty()){
                    editFirstName.setError("Field cannot be empty");
                }
                else if(editLastName.getText().toString().isEmpty()){
                    editLastName.setError("Field cannot be empty");
                }
                else{
                    createAccount(editEmail.getText().toString(), editPassword.getText().toString());
                }

            }
        });
    }

    public void createAccount(final String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOGTAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();

                            FirebaseUser fbUser = mAuth.getCurrentUser();
                            User u = new User(fbUser.getUid(), fbUser.getEmail(), editFirstName.getText().toString(), editLastName.getText().toString());
                            Database.createNewUser(u);
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Account creation failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    //This auto-hides the keyboard
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
    public void setupKeyboardHide(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(RegisterActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupKeyboardHide(innerView);
            }
        }
    }
}

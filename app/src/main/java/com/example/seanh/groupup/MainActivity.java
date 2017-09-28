package com.example.seanh.groupup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textDisplayUser = (TextView) findViewById(R.id.textDisplayUser);
        textDisplayUser.setText(firebaseUser.getEmail());

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Event event = new Event("name","desc","URL",0.0,1.1);
                //Database.recordNewEvent(event);

                //User user = new User(firebaseUser.getUid(),firebaseUser.getEmail(),"fn","test");

                //Database.createNewUser(user);

                Log.d("OUTPUT", Database.getUserById(firebaseUser.getUid()).toString());
            }
        });
    }
}

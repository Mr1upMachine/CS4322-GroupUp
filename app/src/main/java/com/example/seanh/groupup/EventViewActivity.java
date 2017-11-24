package com.example.seanh.groupup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventViewActivity extends AppCompatActivity {
    private TextView textEventViewOwner, textEventViewStartTime, textEventViewEndTime, textEventViewDate,
            textEventViewDescription, textEventViewAddress, textEventViewAttendance, textEventViewCapacity;
    private User owner;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        overridePendingTransition(R.anim.slide_in, R.anim.nothing);

        //Gets Event object from Main Activity
        final Bundle b = getIntent().getExtras();
        final Event event = b.getParcelable("myEvent");
        final User user = b.getParcelable("myUser");
        fetchOwner(event.getOwnerId());

        android.support.v7.widget.Toolbar tb = findViewById(R.id.toolbarEventView);
        setSupportActionBar(tb);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle( event.getName() );
        tb.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.nothing, R.anim.slide_out);
            }
        });
        
        //textEventViewName =  findViewById(R.id.textEventViewName);
        textEventViewOwner =  findViewById(R.id.textEventViewOwner);
        textEventViewStartTime =  findViewById(R.id.textEventViewStartTime);
        textEventViewEndTime =  findViewById(R.id.textEventViewEndTime);
        textEventViewDate =  findViewById(R.id.textEventViewDate);
        textEventViewDescription = findViewById(R.id.textEventViewDescription);
        textEventViewAddress =  findViewById(R.id.textEventViewAddress);
        textEventViewAttendance =  findViewById(R.id.textEventViewAttendance);
        textEventViewCapacity =  findViewById(R.id.textEventViewCapacity);

        //textEventViewName.setText( event.getName() );
        textEventViewStartTime.setText( event.getStartTime() );
        textEventViewEndTime.setText( event.getEndTime() );
        textEventViewDate.setText( event.getDate() );
        textEventViewDescription.setText( event.getDescription() );
        textEventViewAddress.setText( event.getWhere() ); //TODO fix?
        textEventViewAttendance.setText( ""+event.getAttendance() );
        textEventViewCapacity.setText( ""+event.getCapacity() );
        //imageEventViewPicture.setImageBitmap(pictureBitMap);



        findViewById(R.id.buttonViewEventJoin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Some database call adding current user to event attendees
                //TODO increase current attendees +1
                //Toast.makeText(getApplicationContext(), "Joined the Group " + eventString + "!", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.buttonViewEventShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO open a view asking for user with whom to share with
                //TODO send some sort of invitation to specified user
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out);
    }

    //relevant database calls
    private final DatabaseReference dataRoot = FirebaseDatabase.getInstance().getReference();
    public void fetchOwner(String id){
        // Read from the database
        dataRoot.child("users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                owner = dataSnapshot.getValue(User.class);
                textEventViewOwner.setText("Host: " + owner.getfName() + " " + owner.getlName() ); //TODO change for owner name
            }

            @Override
            public void onCancelled(DatabaseError error) {  }
        });
    }
}

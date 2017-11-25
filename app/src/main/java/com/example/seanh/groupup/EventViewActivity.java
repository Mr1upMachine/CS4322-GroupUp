package com.example.seanh.groupup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventViewActivity extends AppCompatActivity {
    private TextView textEventViewOwner, textEventViewStartTime, textEventViewEndTime, textEventViewDate,
            textEventViewDescription, textEventViewAddress, textEventViewAttendance, textEventViewCapacity;
    private Event event;
    private User user, owner;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        overridePendingTransition(R.anim.slide_in, R.anim.nothing);

        //Gets Event object from Main Activity
        final Bundle b = getIntent().getExtras();
        event = b.getParcelable("myEvent");
        user = b.getParcelable("myUser");
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
        textEventViewAddress.setText( event.getAddress() ); //TODO fix?
        textEventViewAttendance.setText( ""+event.getAttendance() );
        textEventViewCapacity.setText( ""+event.getCapacity() );
        //imageEventViewPicture.setImageBitmap(pictureBitMap);

        findViewById(R.id.buttonViewEventShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO open a view asking for user with whom to share with
                //TODO send some sort of invitation to specified user
                Toast.makeText(EventViewActivity.this, "This does nothing yet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out);
    }

    public void setUpJoinButton(){
        final Button buttonViewEventJoin = findViewById(R.id.buttonViewEventJoin);
        if( !user.getId().equals(event.getOwnerId()) ) {
            if (user.getSubscribedEvents().contains(event.getId())) {
                buttonViewEventJoin.setText("Leave");
            }
            buttonViewEventJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!user.getSubscribedEvents().contains(event.getId())) {
                        event.addSubscribedToEvent(user.getId());
                        user.addSubscribedEvent(event.getId());
                        buttonViewEventJoin.setText("Leave");
                        Toast.makeText(EventViewActivity.this, "Event joined successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        event.removeSubscribedToEvent(user.getId());
                        user.removeSubscribedEvent(event.getId());
                        buttonViewEventJoin.setText("Join");
                        Toast.makeText(EventViewActivity.this, "Event left successfully", Toast.LENGTH_SHORT).show();
                    }
                    Database.updateEvent(event);
                    Database.updateUser(user);
                }
            });
        }
        else{
            buttonViewEventJoin.setText("Owner");
            buttonViewEventJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(EventViewActivity.this, "I'd hope you're already going", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    //relevant database calls
    private final DatabaseReference dataRoot = FirebaseDatabase.getInstance().getReference();
    public void fetchOwner(String id){
        // Read from the database
        dataRoot.child("users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                owner = dataSnapshot.getValue(User.class);
                textEventViewOwner.setText("Host: " + owner.getfName() + " " + owner.getlName() );
                setUpJoinButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {  }
        });
    }
}

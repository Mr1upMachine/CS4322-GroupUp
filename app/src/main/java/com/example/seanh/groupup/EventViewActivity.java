package com.example.seanh.groupup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EventViewActivity extends AppCompatActivity {
    //TODO Micah stuff here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        final TextView textEventViewName =  findViewById(R.id.textEventViewName);
        final TextView textEventViewOwner =  findViewById(R.id.textEventViewOwner);
        final TextView textEventViewStartTime =  findViewById(R.id.textEventViewStartTime);
        final TextView textEventViewEndTime =  findViewById(R.id.textEventViewEndTime);
        final TextView textEventViewDate =  findViewById(R.id.textEventViewDate);
        final TextView textEventViewDescription = findViewById(R.id.textEventViewDescription);
        final TextView textEventViewAddress =  findViewById(R.id.textEventViewAddress);
        final TextView textEventViewAttendance =  findViewById(R.id.textEventViewAttendance);
        final TextView textEventViewCapacity =  findViewById(R.id.textEventViewCapacity);
        final ImageView imageEventViewPicture = findViewById(R.id.imageViewEventPicture);

        //Gets Event object from Main Activity
        final Bundle b = getIntent().getExtras();
        final Event event = b.getParcelable("myEvent");


        textEventViewName.setText( event.getName() );
        textEventViewOwner.setText( event.getOwnerId() ); //TODO change for owner name
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
    }
}

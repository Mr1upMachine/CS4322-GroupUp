package com.example.seanh.groupup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.IOException;
import java.io.FileDescriptor;
import android.os.ParcelFileDescriptor;
import android.net.Uri;
import android.provider.MediaStore;
import android.database.Cursor;
import android.view.inputmethod.InputMethodManager;

public class EventViewActivity extends AppCompatActivity {
//TODO Micah stuff here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        final String eventString = getIntent().getStringExtra("Title");
        final String ownerString = getIntent().getStringExtra("Owner");
        final String startTimeString = getIntent().getStringExtra("Start Time");
        final String endTimeString = getIntent().getStringExtra("End Time");
        final String startDateString = getIntent().getStringExtra("Start Date");
        final String endDateString = getIntent().getStringExtra("End Date");
        final String descriptionString = getIntent().getStringExtra("Description");
        final String addressString = getIntent().getStringExtra("Address");
        String attendersString = getIntent().getStringExtra("Current Attenders");
        final String capacityString = getIntent().getStringExtra("Capacity");
//        final Uri pictureURI = getIntent().getData(data);
//        final Bitmap pictureBitMap = getBitmapFromUri(pictureURI);


        TextView textEventViewEvent = (TextView) findViewById(R.id.textEventViewName);
        TextView textEventViewOwner = (TextView) findViewById(R.id.textEventViewOwner);
        TextView textEventViewStartTime = (TextView) findViewById(R.id.textEventViewStartTime);
        TextView textEventViewEndTime = (TextView) findViewById(R.id.textEventViewEndTime);
        TextView textEventViewStartDate = (TextView) findViewById(R.id.textEventViewStartDate);
        TextView textEventViewEndDate = (TextView) findViewById(R.id.textEventViewEndDate);
        TextView textEventViewDescription = (TextView) findViewById(R.id.textEventViewDescription);
        TextView textEventViewAddress = (TextView) findViewById(R.id.textEventViewAddress);
        TextView textEventViewAttenders = (TextView) findViewById(R.id.textEventViewAttenders);
        TextView textEventViewCapacity = (TextView) findViewById(R.id.textEventViewCapacity);
        ImageView imageEventViewPicture = (ImageView) findViewById(R.id.imageViewEventPicture);



        textEventViewEvent.setText(eventString);
        textEventViewOwner.setText(ownerString);
        textEventViewStartTime.setText(startTimeString);
        textEventViewEndTime.setText(endTimeString);
        textEventViewStartDate.setText(startDateString);
        textEventViewEndDate.setText(endDateString);
        textEventViewDescription.setText(descriptionString);
        textEventViewAddress.setText(addressString);
        textEventViewAttenders.setText(attendersString);
        textEventViewCapacity.setText(capacityString);
//        imageEventViewPicture.setImageBitmap(pictureBitMap);

        findViewById(R.id.buttonViewEventJoin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Some database call adding current user to event attendees
                //TODO increase current attendees +1
                Toast.makeText(getApplicationContext(), "Joined the Group " + eventString + "!", Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.buttonViewEventShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO open a view asking for user with whom to share with
                //TODO send some sort of invitation to specified user
            }
        });

        findViewById(R.id.buttonViewEventMaps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO open maps view
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

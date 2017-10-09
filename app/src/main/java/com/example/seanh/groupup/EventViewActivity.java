package com.example.seanh.groupup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class EventViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        String eventString = getIntent().getStringExtra("Event");
        String ownerString = getIntent().getStringExtra("Owner");

        TextView textEventViewEvent = (TextView) findViewById(R.id.textEventViewEvent);
        TextView textEventViewOwner = (TextView) findViewById(R.id.textEventViewOwner);

        textEventViewEvent.setText(eventString);
        textEventViewOwner.setText(ownerString);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

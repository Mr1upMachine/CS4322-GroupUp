package com.example.seanh.groupup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String LOGTAG = "MainActivity";

    private List<Event> eventList = new ArrayList<>();
    private RecyclerView recyclerView;
    private EventsAdapter eAdapter;
    SwipeRefreshLayout swipeContainer;

    FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sets action bar to user's email (for testing)
        setTitle(""+fbuser.getEmail());


        //if Location permission is not granted, try granting Location permission TODO replace this with better way (ie. better location)
        requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        //Handles setup of RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewEventList);
        eAdapter = new EventsAdapter(eventList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(eAdapter);

        //loads events for the first time
        fetchAllEvents();


        //onClickListener for RecycleView elements
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            public void onClick(View view, final int position) {
                //Toast.makeText(MainActivity.this, "Single Click on position: " + position, Toast.LENGTH_SHORT).show();

                //passes through the information to the next activity about this event
                Event e = eventList.get(position);
                Intent i = new Intent(MainActivity.this, EventViewActivity.class);
                i.putExtra("Event", e.toString());
                i.putExtra("Owner", e.getOwnerId());
                startActivity(i);
            }
            @Override
            public void onLongClick(View view, final int position) {
                //Toast.makeText(MainActivity.this, "Long press on position: "+position, Toast.LENGTH_LONG).show();
            }
        }));

        //Makes refreshing the event list easy
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchAllEvents();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);






        //Makes FAB (plus button) go to EventCreateActivity
        findViewById(R.id.fabNewEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EventCreateActivity.class));
            }
        });
    }

    //Back button currently signs users out TODO change so pressing back 3 times instead
    @Override
    public void onBackPressed(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }





    private final DatabaseReference dataRoot = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference dataEvents = dataRoot.child("events");
    public void fetchAllEvents(){
        dataEvents.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parseAllEvents(dataSnapshot);
                Log.d(LOGTAG,"data parse complete");
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
    }
    private void parseAllEvents(DataSnapshot dataSnapshot){
        eventList.clear();//clears out old list
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            eventList.add(ds.getValue(Event.class));
        }
    }

    public void updateUI(){
        //Updates the RecycleView
        eAdapter.notifyDataSetChanged();
        //Hides loading bar
        findViewById(R.id.progressBarMainActivity).setVisibility(View.GONE);
        swipeContainer.setRefreshing(false);

    }

}

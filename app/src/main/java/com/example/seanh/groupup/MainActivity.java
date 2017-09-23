package com.example.seanh.groupup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private List<Event> eventList = new ArrayList<>();
    private RecyclerView recyclerView;
    private EventsAdapter eAdapter;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Handles setup of RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewEventList);
        eAdapter = new EventsAdapter(eventList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(eAdapter);

        //Test data
        User u = new User("id0","123fakeemail@rocketmail.com","John","Lebrowski");
        eventList.add(new Event("Basketball pick-up", "title says it all", "10/9/17 5:00PM", "https://imgur.com/gallery/JgWznk4", 1.0, 1.1, u));
        eventList.add(new Event("event2", "e2desc", "e2time", "e2pic", 2.0, 2.2, u));
        eventList.add(new Event("event3", "e3desc", "e3time", "e3pic", 3.0, 3.3, u));
        eventList.add(new Event("event4", "e4desc", "e4time", "e4pic", 4.0, 4.4, u));
        eventList.add(new Event("event5", "e5desc", "e5time", "e5pic", 5.0, 5.5, u));
        eventList.add(new Event("event6", "e6desc", "e6time", "e6pic", 6.0, 6.6, u));
        eventList.add(new Event("event7", "e7desc", "e7time", "e7pic", 7.0, 7.7, u));

        //Updates the RecycleView
        eAdapter.notifyDataSetChanged();


        //onClickListener for RecycleView elements
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                Toast.makeText(MainActivity.this, "Single Click on position: "+position, Toast.LENGTH_SHORT).show();

                //passes through the information to the next activity about this event
                Event e = eventList.get(position);
                Intent i = new Intent(MainActivity.this, EventViewActivity.class);
                i.putExtra("Event", e.toString());
                i.putExtra("Owner", e.getOwner().toString());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, final int position) {
                Toast.makeText(MainActivity.this, "Long press on position: "+position, Toast.LENGTH_LONG).show();
            }
        }));


        //Hides the progress bar
        findViewById(R.id.progressBarMainActivity).setVisibility(View.GONE);
    }
}

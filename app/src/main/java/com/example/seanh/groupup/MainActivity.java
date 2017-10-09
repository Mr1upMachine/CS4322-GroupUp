package com.example.seanh.groupup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Event> eventList = new ArrayList<>();
    private RecyclerView recyclerView;
    private EventsAdapter eAdapter;

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
        User u = new User("id0","email","fname","lname");
        eventList.add(new Event("event1", "e1desc", "e1time", "e1pic", 1.0, 1.1, u));
        eventList.add(new Event("event2", "e2desc", "e2time", "e2pic", 2.0, 2.2, u));
        eventList.add(new Event("event3", "e3desc", "e3time", "e3pic", 3.0, 3.3, u));
        eventList.add(new Event("event4", "e4desc", "e4time", "e4pic", 4.0, 4.4, u));
        eventList.add(new Event("event5", "e5desc", "e5time", "e5pic", 5.0, 5.5, u));
        eventList.add(new Event("event6", "e6desc", "e6time", "e6pic", 6.0, 6.6, u));
        eventList.add(new Event("event7", "e7desc", "e7time", "e7pic", 7.0, 7.7, u));

        //Updates the RecycleView
        eAdapter.notifyDataSetChanged();

        //Hides the progress bar
        findViewById(R.id.progressBarMainActivity).setVisibility(View.GONE);
    }
}

package com.example.seanh.groupup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
    private final FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

    private List<Event> eventList = new ArrayList<>();
    private List<Event> tempList;
    private RecyclerView recyclerView;
    private EventsAdapter eAdapter;
    private SwipeRefreshLayout swipeRefreshContainer;

    private Toolbar toolbar;
    private boolean menuViewListMap = false; //alternates which menu option is visible

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        toolbar.setTitle("GroupUp");
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        toolbar.inflateMenu(R.menu.main);


        //if Location permission is not granted, try granting Location permission TODO replace this with better way (ie. better location)
        requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        //Handles setup of RecyclerView
        recyclerView = findViewById(R.id.recycleViewEventList);
        eAdapter = new EventsAdapter(eventList);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(eAdapter);

        //loads events for the first time
        fetchAllEvents();


        //onClickListener for RecycleView elements
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            public void onClick(View view, final int position) {
                //passes through the information to the next activity about this event
                Event e = eventList.get(position);
                Intent i = new Intent(MainActivity.this, EventViewActivity.class);
                i.putExtra("Event", e.toString());
                i.putExtra("Owner", e.getOwnerId());
                startActivity(i);
            }
            @Override
            public void onLongClick(View view, final int position) { }
        }));

        //Makes refreshing the event list easy
        swipeRefreshContainer = findViewById(R.id.swipeRefreshContainer);
        swipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchAllEvents();
            }
        });
        // Configure the refreshing colors
        swipeRefreshContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        //All for SearchView (search button at top)
        final MenuItem searchMenu = menu.findItem(R.id.main_search);
        final SearchView searchView = (SearchView) searchMenu.getActionView();
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Saves original values as temp
                tempList = new ArrayList<>(eventList);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                eventList.clear();
                callSearch(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                eventList.clear();
                callSearch(newText);
                return true;
            }
            void callSearch(String query) {
                for (Event e : tempList) {
                    if (e.getName().toLowerCase().contains( query.toLowerCase() )) {
                        eventList.add(e);
                    }
                }
                eAdapter.notifyDataSetChanged();
            }
        });
        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventList.addAll(tempList);
                eAdapter.notifyDataSetChanged();

                EditText et = findViewById(R.id.search_src_text); //Find EditText view
                et.setText(""); //Clear the text from EditText view
                searchView.setQuery("", false); //Clear query
                searchView.onActionViewCollapsed(); //Collapse the action view
                searchMenu.collapseActionView(); //Collapse the search widget
            }
        });


        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //alternates which menu option is visible
        menu.findItem(R.id.main_view_map).setVisible(!menuViewListMap);
        menu.findItem(R.id.main_view_list).setVisible(menuViewListMap);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Toast.makeText(this,item.getTitle()+" selected", Toast.LENGTH_SHORT).show();

        if (id == R.id.main_share_test) {

        }
        else if (id == R.id.main_view_map) {
            recyclerView.setVisibility(View.GONE);
            findViewById(R.id.constraintLayoutEventMap).setVisibility(View.VISIBLE);
            menuViewListMap = !menuViewListMap; //alternates which menu option is visible
        }
        else if (id == R.id.main_view_list) {
            findViewById(R.id.constraintLayoutEventMap).setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            menuViewListMap = !menuViewListMap; //alternates which menu option is visible
        }
        return super.onOptionsItemSelected(item);
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
        swipeRefreshContainer.setRefreshing(false);

    }

}

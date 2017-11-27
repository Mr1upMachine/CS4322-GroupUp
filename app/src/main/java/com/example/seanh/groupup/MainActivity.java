package com.example.seanh.groupup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
    private User user;

    private List<Event> eventList = new ArrayList<>();
    private List<Event> tempList;
    private RecyclerView recyclerView;
    private EventsAdapter eAdapter;
    private SwipeRefreshLayout swipeRefreshContainer;

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    Menu menuBar;
    private boolean menuViewSwitch = true; //alternates which menu option is visible
    private FloatingActionButton fab;

    boolean doubleBackToExitPressedOnce = false; //sign out pressing back twice
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        toolbar.setTitle("GroupUp");
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });
        toolbar.inflateMenu(R.menu.menu_main);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationMain);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                final int id = item.getItemId();

                tempList = new ArrayList<>(eventList);

                //TODO figure out why recycleview doesn't clear cache

                if(id == R.id.main_drawer_subscribed){
                    if( !user.getSubscribedEventIds().isEmpty() ) {
                        filterEventList( user.getSubscribedEventIds() );
                    }
                    else{
                        Toast.makeText(MainActivity.this, "No subscribed events", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(id == R.id.main_drawer_hosted){
                    if( !user.getCreatedEventIds().isEmpty() ) {
                        filterEventList( user.getCreatedEventIds() );
                    }
                    else{
                        Toast.makeText(MainActivity.this, "No hosted events", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(id == R.id.main_drawer_item3){

                }
                else if(id == R.id.main_drawer_item4){

                }

                toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
                toolbar.setTitle(item.getTitle()+" Events");
                menuBar.getItem(0).setVisible(false);
                menuBar.getItem(1).setVisible(false);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetEventList();
                        toolbar.setNavigationIcon(R.drawable.ic_menu_white);
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDrawerLayout.openDrawer(Gravity.START);
                            }
                        });
                        toolbar.setTitle("GroupUp");
                        menuBar.getItem(0).setVisible(true);
                        menuBar.getItem(1).setVisible(true);
                    }
                });

                mDrawerLayout.closeDrawer(Gravity.START);
                return false;
            }
        });


        //if Location permission is not granted, try granting Location permission TODO replace this with better way (ie. better location)
        requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        //loads events for the first time
        fetchAllData();



        //Makes refreshing the event list easy
        swipeRefreshContainer = findViewById(R.id.swipeRefreshContainer);
        swipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchAllData();
            }
        });
        // Configure the refreshing colors
        swipeRefreshContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);





        //Makes FAB (plus button) go to EventCreateActivity
        fab = findViewById(R.id.fabNewEvent);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, EventCreateActivity.class);
                i.putExtra("myUser", user);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menuBar = menu;
        
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
                eAdapter.clear();
                filterEventList(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                eAdapter.clear();
                filterEventList(newText);
                return true;
            }
        });
        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetEventList();

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_view_switch) {
            if(menuViewSwitch) {
                recyclerView.setVisibility(View.GONE);
                fab.hide();
                findViewById(R.id.constraintLayoutEventMap).setVisibility(View.VISIBLE);
                item.setIcon(R.drawable.ic_format_list_bulleted_white_18dp);
            }
            else{
                findViewById(R.id.constraintLayoutEventMap).setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                fab.show();
                item.setIcon(R.drawable.ic_map_white_18dp);
            }
            menuViewSwitch = !menuViewSwitch; //alternates which menu option is visible
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
        } else {
            //must press back twice to sign out
            if (doubleBackToExitPressedOnce) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to sign out", Toast.LENGTH_SHORT).show();
          
            //after 2 seconds resets
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }



    //relevant database calls
    private final DatabaseReference dataRoot = FirebaseDatabase.getInstance().getReference();
    public void fetchAllData(){
        dataRoot.child("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parseAllEvents(dataSnapshot);
                fetchCurrentUser(fbUser.getUid());
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
    public void fetchCurrentUser(String id){
        // Read from the database
        dataRoot.child("users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                Log.d(LOGTAG,"data parse complete");

                final TextView tvName = findViewById(R.id.textMainDrawerName);
                final TextView tvEmail = findViewById(R.id.textMainDrawerEmail);
                tvName.setText(user.getfName()+" "+user.getlName());
                tvEmail.setText(user.getEmail());

                showEventList();
            }

            @Override
            public void onCancelled(DatabaseError error) {  }
        });
    }
    public void showEventList(){
        //Handles setup of RecyclerView
        recyclerView = findViewById(R.id.recycleViewEventList);
        eAdapter = new EventsAdapter(eventList, user);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(eAdapter);

        //Updates the RecycleView
        eAdapter.notifyDataSetChanged();

        //onClickListener for RecycleView elements
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            public void onClick(View view, final int position) {
                //passes through the information to the next activity about this event
                Event e = eventList.get(position);
                Intent i = new Intent(MainActivity.this, EventViewActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("myEvent", e);
                b.putParcelable("myUser", user);
                i.putExtras(b);
                startActivity(i);
            }
            @Override
            public void onLongClick(View view, final int position) { }
        }));

        showMapMarkers();
    }
    private void showMapMarkers(){

        for(Event e : eventList){

        }

        //Hides loading bar(s)
        findViewById(R.id.progressBarMainActivity).setVisibility(View.GONE);
        swipeRefreshContainer.setRefreshing(false);
    }


    //helper methods for altering the eventList
    private void filterEventList(String query) {
        for (Event e : tempList) {
            if (e.getName().toLowerCase().contains( query.toLowerCase() )) {
                eventList.add(e);
            }
        }

        //TODO Map filter update

        eAdapter.notifyDataSetChanged();
    }
    private void filterEventList(List<String> eventIdList){
        eAdapter.clear();
        for(String eventId : eventIdList) {
            for (Event e : tempList) {
                if (e.getId().equals(eventId)) {
                    eventList.add(e);
                    break;
                }
            }
        }
        eAdapter.notifyDataSetChanged();
    }
    private void resetEventList(){
        eAdapter.clear();
        eAdapter.addAll(tempList);
    }
}

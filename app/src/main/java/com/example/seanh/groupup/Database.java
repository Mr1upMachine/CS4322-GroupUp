package com.example.seanh.groupup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

//TODO All data retrieval lags by one iteration (ie. returns null first time called). Why?
public class Database {

    private static final DatabaseReference dataRoot = FirebaseDatabase.getInstance().getReference();
    private static final DatabaseReference dataEvents = dataRoot.child("events");
    private static final DatabaseReference dataUsers = dataRoot.child("users");
    //TODO add storage related stuff for user pics and event pics
    private static final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    private static Event tempEvent = new Event();
    private static User tempUser = new User();

    private static List<Event> eventList = new ArrayList<>()

    //Records new event into the database
    public static void createNewEvent(Event e) {
        DatabaseReference dr = dataEvents.push(); //generates unique id for event
        e.setId(dr.getKey()); //makes it easier to get generated key
        dr.setValue(e); //uploads data to database
    }
    public static void updateEvent(Event newEvent){
        DatabaseReference dr = dataEvents.child(newEvent.getId()); //gets event by id
        dr.setValue(newEvent); //uploads data to database
    }
    public static Event getEventById(String id){
        // Read from the database
        dataEvents.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tempEvent = dataSnapshot.getValue(Event.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        return tempEvent;
    }

    /*
    public static List<Event> getAllEvents(){ //TODO make better name
        dataEvents.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parseAllEvents(dataSnapshot);
                Log.d("OUTPUT","data parse complete");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
        return eventList;
    }
    private static void parseAllEvents(DataSnapshot dataSnapshot){
        eventList.clear();//clears out old list
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            eventList.add(ds.getValue(Event.class));
        }
    }


    public static void getEventIdCount(){  }
       */

    //Records new user into the database
    public static void createNewUser(User u) {
        dataUsers.child(u.getId()).setValue(u);
    }
    public static User getUserById(String id){
        // Read from the database
        dataUsers.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tempUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {  }
        });
        return tempUser;
    }
    public static void updateUser(User newUser){
        //TODO write method
    }




}

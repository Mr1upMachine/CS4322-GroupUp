package com.example.seanh.groupup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private final String LOGTAG = "Database";

    private static final DatabaseReference dataRoot = FirebaseDatabase.getInstance().getReference();
    private static final DatabaseReference dataEvents = dataRoot.child("events");
    private static final DatabaseReference dataUsers = dataRoot.child("users");

    private static Event tempEvent = new Event();
    private static User tempUser = new User();
    private static List<Event> tempEventList = new ArrayList<>();



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
    public static List<Event> getEventListByIds(List<String> ids){
        tempEventList.clear();
        for(String id : ids) {
            // Read from the database
            tempEventList.add( getEventById(id) );
        }
        return tempEventList;
    }


    //Records new user into the database
    public static void createNewUser(User u) {
        updateUser(u);
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
    public static void updateUser(User u){
        dataUsers.child(u.getId()).setValue(u);
    }




}

package com.example.seanh.groupup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

//TODO All data retrieval lags by one iteration (ie. returns null first time called). Why?
public class Database {

    private static final DatabaseReference dataRoot = FirebaseDatabase.getInstance().getReference();
    private static final DatabaseReference dataEvents = dataRoot.child("events");
    private static final DatabaseReference dataEventIDs = dataEvents.child("id:");
    private static final DatabaseReference dataUsers = dataRoot.child("users");
    //TODO add storage related stuff for user pics and event pics
    private static final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    //TODO find better way to do this
    private static int eventIdCount = getEventIdCount();
    private static Event tempEvent = new Event();
    private static User tempUser = new User();



    //Records new event into the database
    public static void createNewEvent(Event e) {
        e.setId(++eventIdCount);
        dataEventIDs.child("" + eventIdCount).setValue(e);
        setEventIdCount(eventIdCount);
    }
    public static Event getEventById(int id){
        // Read from the database
        dataEventIDs.child(""+id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                tempEvent = dataSnapshot.getValue(Event.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        return tempEvent;
    }
    public static void updateEvent(Event newEvent){
        //TODO write method
    }

    public static int getEventIdCount(){
        dataEvents.child("idCount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //TODO get rid of backassward code loop
                eventIdCount = snapshot.getValue(Integer.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return eventIdCount;
    }
    public static void setEventIdCount(int id){
        dataEvents.child("idCount").setValue(id);
    }


    //Records new user into the database
    public static void createNewUser(User u) {
        dataUsers.child(u.getId()).setValue(u);
    }
    public static User getUserById(String id){
        // Read from the database
        dataUsers.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                tempUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        return tempUser;
    }
    public static void updateUser(User newUser){
        //TODO write method
    }



}

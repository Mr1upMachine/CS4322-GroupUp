package com.example.seanh.groupup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Database {

    private static final DatabaseReference dataRoot = FirebaseDatabase.getInstance().getReference();
    private static final DatabaseReference dataEvents = dataRoot.child("events");
    private static final DatabaseReference dataEventIDs = dataEvents.child("id:");
    private static int idCount = getIdCount();

    //TODO find better way to do this
    private static Event tempEvent = new Event();

    public static void recordNewEvent(Event e) {
        e.setId(++idCount);
        dataEventIDs.child("" + idCount).setValue(e);
        setIdCount(idCount);
    }

    public static Event getEventById(int id){
        // Read from the database
        dataEventIDs.child(""+id).addValueEventListener(new ValueEventListener() {
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

    public static int getIdCount(){
        dataEvents.child("idCount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                idCount = snapshot.getValue(Integer.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return idCount;
    }
    public static void setIdCount(int id){
        dataEvents.child("idCount").setValue(id);
    }

}

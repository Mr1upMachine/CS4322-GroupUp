package com.example.seanh.groupup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database {

    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public static void writeNewUser(String email, String password) {
        User user = new User(email, password);

        mDatabase.child("Users").setValue(user);
    }
    public void setEmail(String email){

    }

}

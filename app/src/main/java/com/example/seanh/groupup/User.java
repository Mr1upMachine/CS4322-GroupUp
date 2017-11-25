package com.example.seanh.groupup;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable{
    private String id, email, fName, lName;
    private List<String> subscribedEventIds = new ArrayList<>();
    private List<String> createdEventIds = new ArrayList<>();

    //Constructors
    public User(){ }
    public User(String id, String email, String fName, String lName){
        this.id = id;
        this.email = email;
        this.fName = fName;
        this.lName = lName;
    }
    protected User(Parcel in) {
        id = in.readString();
        email = in.readString();
        fName = in.readString();
        lName = in.readString();
        subscribedEventIds = in.createStringArrayList();
        createdEventIds = in.createStringArrayList();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    //Getter & setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getfName() {
        return fName;
    }
    public void setfName(String fName) {
        this.fName = fName;
    }
    public String getlName() {
        return lName;
    }
    public void setlName(String lName) {
        this.lName = lName;
    }
    public List<String> getSubscribedEventIds(){
        return subscribedEventIds;
    }
    public void setSubscribedEventIds(List<String> subscribedEvents) {
        this.subscribedEventIds = subscribedEvents;
    }
    public List<String> getCreatedEventIds() {
        return createdEventIds;
    }
    public void setCreatedEventIds(List<String> createdEventIds) {
        this.createdEventIds = createdEventIds;
    }

    public void addSubscribedEvent(String eventId){ subscribedEventIds.add(eventId); }
    public boolean removeSubscribedEvent(String eventId){ return subscribedEventIds.remove(eventId); }
    public boolean containsSubscribedEvent(String eventId){ return subscribedEventIds.contains(eventId); }
    public void addCreatedEvent(String eventId){ createdEventIds.add(eventId); }
    public boolean removeCreatedEvent(String eventId){ return createdEventIds.remove(eventId); }
    public boolean containsCreatedEvent(String eventId){ return createdEventIds.contains(eventId); }





    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(fName);
        dest.writeString(lName);
        dest.writeStringList(subscribedEventIds);
        dest.writeStringList(createdEventIds);
    }

    @Override
    public String toString() {
        return "User{\nid="+id+"\nemail="+email+"\nfName="+fName+"\nlName="+lName+"\n}";
    }
}
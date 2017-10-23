package com.example.seanh.groupup;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id, email, fName, lName;
    private List<String> subscribedEvents = new ArrayList<>();

    //Constructors
    public User(){ }
    public User(String id, String email, String fName, String lName){
        this.id = id;
        this.email = email;
        this.fName = fName;
        this.lName = lName;
    }

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
    public void setSubscribedEvents(List<String> subscribedEvents) {
        this.subscribedEvents = subscribedEvents;
    }
    public List<String> getSubscribedEvents(){
        return subscribedEvents;
    }

    public void addSubscribedEvent(String eventId){
        subscribedEvents.add(eventId);
    }


    @Override
    public String toString() {
        return "User{\nid="+id+"\nemail="+email+"\nfName="+fName+"\nlName="+lName+"\n}";
    }
}
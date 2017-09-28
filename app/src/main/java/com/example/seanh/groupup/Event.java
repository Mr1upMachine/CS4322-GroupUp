package com.example.seanh.groupup;

public class Event {
    private int id;
    private String name, description, picURL;
    private double locX, locY;
    private User owner;

    //Constructors
    Event(){}
    Event(int id, String name){
        this.id = id;
        this.name = name;
    }
    Event(String name, String description, String picURL, double locX, double locY){
        //Used for the creation of new events
        this.name = name;
        this.description = description;
        this.picURL = picURL;
        this.locX = locX;
        this.locY = locY;
    }

    //Getter & setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPicURL() {
        return picURL;
    }
    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }
    public double getLocX() {
        return locX;
    }
    public double getLocY() {
        return locY;
    }
    public void setLoc(double locX, double locY) {
        this.locX = locX;
        this.locY = locY;
    }
    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String toString(){
        return "Event{id="+id+" name="+name+" desc="+description+" picURL="+picURL+" locX="+locX+" locY"+locY;
    }

}

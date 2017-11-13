package com.example.seanh.groupup;

import java.text.DecimalFormat;

public class Event {
    private String id, name, date, description, time, picURL, ownerId, where;
    private double locX, locY;
    //private User owner;

    //Constructors
    Event(){}
    Event(String name, String description, String time, String date, String picURL, String where, String ownerId){
        //Used for the creation of new events
        this.name = name;
        this.description = description;
        this.time = time;
        this.date = date;
        this.picURL = picURL;
        this.where = where;
        this.ownerId = ownerId;
    }

    //Getter & setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
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
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
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
    public String getWhere() {
        return where;
    }
    public void setWhere(String where) {
        this.where = where;
    }
    public String getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String generateLocString(){
        //TODO parse gps loc to city
        DecimalFormat df = new DecimalFormat("#.000");
        return df.format(locX)+"  "+df.format(locY);
    }

    public String toString(){
        return "Event{\nid="+id+"\nname="+name+"\ndesc="+description+"\ntime="+time+"\ndate="+date+"\naddress="+where+"\npicURL="+picURL+"}";
    }
}
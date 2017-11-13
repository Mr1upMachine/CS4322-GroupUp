package com.example.seanh.groupup;

import java.text.DecimalFormat;
import android.graphics.Bitmap;

public class Event {
    private String id, name, startDate, endDate, description, startTime, endTime, picURL, ownerId, where, type;
    private double locX, locY;
    private Bitmap bitMapEventImage;
    //private User owner;

    //Constructors
    Event(){}
    Event(String name, String description, String startTime, String endTime, String startDate, String endDate, Bitmap bitMapEventImage, String where, String ownerId, String type){
        //Used for the creation of new events
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bitMapEventImage = bitMapEventImage;
        this.where = where;
        this.ownerId = ownerId;
        this.type = type;
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
    public String getStartDate() {
        return startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setStartDate(String startDate) {this.startDate = startDate;}
    public void setEndDate(String endDate) {this.endDate = endDate;}
    public String getStartTime() {
        return startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
    public void setBitMapEventImage(Bitmap bitMapEventImage) {this.bitMapEventImage=bitMapEventImage;}
    public Bitmap getBitMapEventImage() {return bitMapEventImage;}
    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public String generateLocString(){
        //TODO parse gps loc to city
        DecimalFormat df = new DecimalFormat("#.000");
        return df.format(locX)+"  "+df.format(locY);
    }

    public String toString(){
        return "Event{\nid="+id+"\nname="+name+"\ndesc="+description+"\nstartTime="+startTime+"\nendTime="+endTime+"\nstartDate="+startDate+"\nendDate="+endDate+"\naddress="+where+"\npicURL="+picURL+"}";
    }
}
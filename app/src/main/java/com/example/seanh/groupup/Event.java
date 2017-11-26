package com.example.seanh.groupup;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Event implements Parcelable {
    private String id, name, description, date, startTime, endTime, picURL, ownerId, address;
    private int colorR, colorG, colorB, attendance, capacity;
    private double locX, locY;
    private List<String> subscribedUserIds = new ArrayList<>();

    //Constructors
    Event(){}
    Event(String name, String description, String startTime, String endTime, String date, String address,
          String ownerId, int colorR, int colorG, int colorB, int attendance, int capacity){

        //Used for the creation of new events
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.address = address;
        this.ownerId = ownerId;
        this.colorR = colorR;
        this.colorG = colorG;
        this.colorB = colorB;
        this.attendance = attendance;
        this.capacity = capacity;
    }

    //Parcelable stuff here
    protected Event(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        date = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        picURL = in.readString();
        ownerId = in.readString();
        address = in.readString();
        colorR = in.readInt();
        colorG = in.readInt();
        colorB = in.readInt();
        locX = in.readDouble();
        locY = in.readDouble();
        attendance = in.readInt();
        capacity = in.readInt();
        in.readStringList(subscribedUserIds);
        //bitMapEventImage = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

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
    public void setDate(String date) {this.date = date;}
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
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    public int getColorR() { return colorR; }
    public int getColorG() { return colorG; }
    public int getColorB() { return colorB; }
    public int getAttendance() { return attendance; }
    public void setAttendance(int attendance) { this.attendance = attendance; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public List<String> getSubscribedUserIds() {
        return subscribedUserIds;
    }
    public void setSubscribedUserIds(List<String> subscribedUserIds) {
        this.subscribedUserIds = subscribedUserIds;
    }

    public String getDateTime(){
        return date+" "+startTime;
    }
    public String generateLocString(){
        //TODO parse gps loc to city
        DecimalFormat df = new DecimalFormat("#.000");
        return df.format(locX)+"  "+df.format(locY);
    }
    public void addSubscribedToEvent(String userId){ subscribedUserIds.add(userId); }
    public boolean removeSubscribedToEvent(String userId){ return subscribedUserIds.remove(userId); }



    //Parcelable part
    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(picURL);
        dest.writeString(ownerId);
        dest.writeString(address);
        dest.writeInt(colorR);
        dest.writeInt(colorG);
        dest.writeInt(colorB);
        dest.writeDouble(locX);
        dest.writeDouble(locY);
        dest.writeInt(attendance);
        dest.writeInt(capacity);
        dest.writeStringList(subscribedUserIds);
    }

    @Override
    public String toString(){
        return "Event{\nid="+id
                +"\nname="+name
                +"\ndesc="+description
                +"\nstartTime="+startTime
                +"\nendTime="+endTime
                +"\nstartDate="+date
                +"\naddress="+address
                +"\npicURL="+picURL+"}";
    }



}
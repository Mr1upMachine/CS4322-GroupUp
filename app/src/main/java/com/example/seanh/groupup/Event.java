package com.example.seanh.groupup;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;

public class Event implements Parcelable {
    private String id, name, description, date, startTime, endTime, picURL, ownerId, where, type;
    private int attendance, capacity;
    private double locX, locY;
    private Bitmap bitMapEventImage;
    //private User owner;

    //Constructors
    Event(){} 
    Event(String name, String description, String startTime, String endTime, String date, String where, String type, Bitmap bitMapEventImage, String ownerId, int attendance, int capacity){
        //Used for the creation of new events
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.bitMapEventImage = bitMapEventImage;
        this.where = where;
        this.ownerId = ownerId;
        this.type = type;
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
        where = in.readString();
        type = in.readString();
        locX = in.readDouble();
        locY = in.readDouble();
        attendance = in.readInt();
        capacity = in.readInt();
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
    public int getAttendance() { return attendance; }
    public void setAttendance(int attendance) { this.attendance = attendance; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getTime(){
        return date+" "+startTime;
    }

    public String generateLocString(){
        //TODO parse gps loc to city
        DecimalFormat df = new DecimalFormat("#.000");
        return df.format(locX)+"  "+df.format(locY);
    }




    //Parcelable part
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.date);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.picURL);
        dest.writeString(this.ownerId);
        dest.writeString(this.where);
        dest.writeString(this.type);
        dest.writeDouble(this.locX);
        dest.writeDouble(this.locY);
        dest.writeInt(this.attendance);
        dest.writeInt(this.capacity);
    }

    @Override
    public String toString(){
        return "Event{\nid="+id
                +"\nname="+name
                +"\ndesc="+description
                +"\nstartTime="+startTime
                +"\nendTime="+endTime
                +"\nstartDate="+date
                +"\naddress="+where
                +"\npicURL="+picURL+"}";
    }
}
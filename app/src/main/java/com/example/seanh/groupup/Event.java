package com.example.seanh.groupup;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Event implements Parcelable {
    private String id, name, description, date, startTime, endTime, picURL, ownerId,
            address, addressStreet, addressCityState, addressZip;
    private int colorR, colorG, colorB, attendance, capacity;
    private double locX, locY;
    private List<String> subscribedUserIds = new ArrayList<>();

    //Constructors
    Event(){}
    Event(String name, String description, String ownerId,
          String date, String startTime, String endTime,
          String address, String addressStreet, String addressCityState, String addressZip,
          double locX, double locY,
          String picURL, int attendance, int capacity,
          int colorR, int colorG, int colorB){

        //Used for the creation of new events
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.address = address;
        this.addressStreet = addressStreet;
        this.addressCityState = addressCityState;
        this.addressZip = addressZip;
        this.locX = locX;
        this.locY = locY;
        this.picURL = picURL;
        this.attendance = attendance;
        this.capacity = capacity;
        this.colorR = colorR;
        this.colorG = colorG;
        this.colorB = colorB;
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
        addressStreet = in.readString();
        addressCityState = in.readString();
        addressZip = in.readString();
        colorR = in.readInt();
        colorG = in.readInt();
        colorB = in.readInt();
        locX = in.readDouble();
        locY = in.readDouble();
        attendance = in.readInt();
        capacity = in.readInt();
        in.readStringList(subscribedUserIds);
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

    //Getter & setters galore
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDate() { return date; }
    public void setDate(String date) {this.date = date;}
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public String getPicURL() { return picURL; }
    public void setPicURL(String picURL) { this.picURL = picURL; }
    public double getLocX() { return locX; }
    public double getLocY() { return locY; }
    public void setLoc(double locX, double locY) { this.locX = locX; this.locY = locY; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getAddressStreet() { return addressStreet; }
    public void setAddressStreet(String addressStreet) { this.addressStreet = addressStreet; }
    public String getAddressCityState() { return addressCityState; }
    public void setAddressCityState(String addressCityState) { this.addressCityState = addressCityState; }
    public String getAddressZip() { return addressZip; }
    public void setAddressZip(String addressZip) { this.addressZip = addressZip; }
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    public int getColorR() { return colorR; }
    public int getColorG() { return colorG; }
    public int getColorB() { return colorB; }
    public int getAttendance() { return attendance; }
    public void setAttendance(int attendance) { this.attendance = attendance; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public List<String> getSubscribedUserIds() { return subscribedUserIds; }
    public void setSubscribedUserIds(List<String> subscribedUserIds) { this.subscribedUserIds = subscribedUserIds; }

    public String getDateTime(){
        return date+" "+startTime;
    }
    public String generateLocString(){
        DecimalFormat df = new DecimalFormat("#.000");
        return df.format(locX)+"  "+df.format(locY);
    }
    public String generateAddressPretty(){
        return addressStreet+" | "+addressCityState+" "+addressZip;
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
        dest.writeString(addressStreet);
        dest.writeString(addressCityState);
        dest.writeString(addressZip);
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
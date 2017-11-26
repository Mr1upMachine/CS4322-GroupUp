package com.example.seanh.groupup;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Event implements Parcelable {
    private String id, name, description, picURL, ownerId,
            address, addressStreet, addressCityState, addressZip;
    private long startDateTime, endDateTime;
    private int attendance, capacity, color;
    private double locX, locY;
    private List<String> subscribedUserIds = new ArrayList<>();

    //Constructors
    Event() { }

    Event(String name, String description, String ownerId,
          long startDateTime, long endDateTime,
          String address, String addressStreet, String addressCityState, String addressZip,
          double locX, double locY, String picURL,
          int attendance, int capacity, int color) {

        //Used for the creation of new events
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.address = address;
        this.addressStreet = addressStreet;
        this.addressCityState = addressCityState;
        this.addressZip = addressZip;
        this.locX = locX;
        this.locY = locY;
        this.picURL = picURL;
        this.attendance = attendance;
        this.capacity = capacity;
        this.color = color;
    }

    //Parcelable stuff here
    protected Event(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        startDateTime = in.readLong();
        endDateTime = in.readLong();
        picURL = in.readString();
        ownerId = in.readString();
        address = in.readString();
        addressStreet = in.readString();
        addressCityState = in.readString();
        addressZip = in.readString();
        color = in.readInt();
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

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getAddressCityState() {
        return addressCityState;
    }

    public void setAddressCityState(String addressCityState) {
        this.addressCityState = addressCityState;
    }

    public String getAddressZip() {
        return addressZip;
    }

    public void setAddressZip(String addressZip) {
        this.addressZip = addressZip;
    }

    public long getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(long startDateTime) {
        this.startDateTime = startDateTime;
    }

    public long getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(long endDateTime) {
        this.endDateTime = endDateTime;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public double getLocX() {
        return locX;
    }

    public void setLocX(double locX) {
        this.locX = locX;
    }

    public double getLocY() {
        return locY;
    }

    public void setLocY(double locY) {
        this.locY = locY;
    }

    public int genColorR() {
        return Color.red(color);
    }
    public int genColorG() {
        return Color.green(color);
    }
    public int genColorB() {
        return Color.blue(color);
    }

    public String genStartDateSimple() {
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
        return fmtOut.format(startDateTime);
    }
    public String genStartTimeSimple() {
        final DecimalFormat df = new DecimalFormat("00");
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startDateTime);
        return cal.get(Calendar.HOUR_OF_DAY) + ":" + df.format(cal.get(Calendar.MINUTE));
    }
    public String genStartDateTimeSimple() {
        return genStartDateSimple() + " " + genStartTimeSimple();
    }
    public String genEndTimeSimple() {
        final DecimalFormat df = new DecimalFormat("00");
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(endDateTime);
        return cal.get(Calendar.HOUR_OF_DAY) + ":" + df.format(cal.get(Calendar.MINUTE));
    }
    public String genLocString() {
        DecimalFormat df = new DecimalFormat("#.000");
        return df.format(locX) + "  " + df.format(locY);
    }
    public String genAddressPretty() {
        return addressStreet + " | " + addressCityState + " " + addressZip;
    }

    public void addSubscribedToEvent(String userId) {
        subscribedUserIds.add(userId);
    }
    public boolean removeSubscribedToEvent(String userId) {
        return subscribedUserIds.remove(userId);
    }


    //Parcelable part
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeLong(startDateTime);
        dest.writeLong(endDateTime);
        dest.writeString(picURL);
        dest.writeString(ownerId);
        dest.writeString(address);
        dest.writeString(addressStreet);
        dest.writeString(addressCityState);
        dest.writeString(addressZip);
        dest.writeInt(color);
        dest.writeDouble(locX);
        dest.writeDouble(locY);
        dest.writeInt(attendance);
        dest.writeInt(capacity);
        dest.writeStringList(subscribedUserIds);
    }

    @Override
    public String toString() {
        return "Event{\nid=" + id
                + "\nname=" + name
                + "\ndesc=" + description
                + "\nstartDateTime=" + startDateTime
                + "\nendDateTime=" + endDateTime
                + "\naddress=" + address
                + "\npicURL=" + picURL + "}";
    }
}
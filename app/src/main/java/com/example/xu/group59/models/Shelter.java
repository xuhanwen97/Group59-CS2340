package com.example.xu.group59.models;

import java.util.HashMap;

/**
 * Created by xu on 2/27/18
 */

public class Shelter {

    public static final String shelterListKey = "Shelters";

    public static final String addressKey = "Address";
    public static final String capacityKey= "Capacity";
    public static final String latitudeKey = "Latitude";
    public static final String longitudeKey = "Longitude";
    public static final String phoneNumberKey = "Phone Number";
    public static final String restrictionsKey = "Restrictions";
    public static final String shelterNameKey = "Shelter Name";
    public static final String specialNotesKey = "Special Notes";
    public static final String uniqueKeyKey = "Unique Key";

    private String address;
    private String capacity;
    private double latitude;
    private double longitude;
    private String phoneNumber;
    private String restrictions;
    private String shelterName;
    private String specialNotes;
    private int uniqueKey;

    public Shelter(HashMap<String, Object> shelterData) {
        if (shelterData != null) {
            address = (String) shelterData.get(addressKey);
            capacity = shelterData.get(capacityKey).toString();
            latitude = (double) shelterData.get(latitudeKey);
            longitude = (double) shelterData.get(longitudeKey);
            phoneNumber = (String) shelterData.get(phoneNumberKey);
            restrictions = (String) shelterData.get(restrictionsKey);
            shelterName = (String) shelterData.get(shelterNameKey);
            specialNotes = (String) shelterData.get(specialNotesKey);
            uniqueKey = ((Long) shelterData.get(uniqueKeyKey)).intValue();
        }
    }

    public int parseToInt(Object input) {
        if (input == null) {
            return 0;
        }

        if (input.getClass().equals(String.class)) {
            return Integer.valueOf((String) input);
        }

        if (input.getClass().equals(Long.class)) {
            return ((Long) input).intValue();
        }

        return 0;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    public String getSpecialNotes() {
        return specialNotes;
    }

    public void setSpecialNotes(String specialNotes) {
        this.specialNotes = specialNotes;
    }

    public int getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(int uniqueKey) {
        this.uniqueKey = uniqueKey;
    }
}

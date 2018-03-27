package com.example.xu.group59.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xu on 2/27/18
 */

public class Shelter {

    public enum Gender {
        men(Restrictions.men.getName()), women(Restrictions.women.getName());

        String name;

        Gender(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum Restrictions {
        men("Men"), women("Women"), children("Children"), youngAdults("Young Adults"), families("Families"),
        familiesNewborns("Families With Newborns"), familiesChildrenUnder7("Families With Children Under 7"),
        veterans("Veterans");

        private String name;

        Restrictions(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static final String shelterListKey = "shelters";

    public static final String addressKey = "address";
    public static final String capacityKey= "capacity";
    public static final String latitudeKey = "latitude";
    public static final String longitudeKey = "longitude";
    public static final String phoneNumberKey = "phone_number";
    public static final String restrictionsKey = "restrictions";
    public static final String shelterNameKey = "name";
    public static final String specialNotesKey = "special_notes";
    public static final String uniqueKeyKey = "Unique Key";

    private String address;
    private String capacity;
    private double latitude;
    private double longitude;
    private String phoneNumber;
    private List<Restrictions> restrictions;
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
            shelterName = (String) shelterData.get(shelterNameKey);
            specialNotes = (String) shelterData.get(specialNotesKey);

            restrictions = new ArrayList<>(10);
            parseRestrictions((HashMap) shelterData.get(restrictionsKey));
        }
    }

    private void parseRestrictions(HashMap restrictionData) {
        if (restrictionData != null) {
            for (Object statusString : restrictionData.keySet()) {
                for (Restrictions r : Restrictions.values()) {
                    if (statusString.equals(r.getName())) {
                        restrictions.add(r);
                    }
                }
            }
        }
    }

    public String getRestrictionsString() {
        if (restrictions == null || restrictions.size() == 0) {
            return "No Restrictions";
        }

        String restrictionsString = "";

        for (Restrictions r: restrictions) {
            restrictionsString = restrictionsString + r.getName() + ", ";
        }

        return restrictionsString.substring(0, restrictionsString.length() - 3);
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

    public List<Restrictions> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(List<Restrictions> restrictions) {
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

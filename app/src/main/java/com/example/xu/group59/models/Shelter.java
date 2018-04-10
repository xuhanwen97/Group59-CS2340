package com.example.xu.group59.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xu on 2/27/18
 */

public class Shelter implements Parcelable {

    public enum Gender {
        men(Restrictions.men.getName()), women(Restrictions.women.getName());

        final String name;

        Gender(String name) {
            this.name = name;
        }

        public CharSequence getName() {
            return name;
        }
    }

    public enum Restrictions {
        men("Men"), women("Women"), children("Children"), youngAdults("Young Adults"), families("Families"),
        familiesNewborns("Families With Newborns"), familiesChildrenUnder7("Families With Children Under 7"),
        veterans("Veterans");

        private final String name;

        Restrictions(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static final int singleUserMaxReservation = 10;

    public static final String shelterListKey = "shelters";
    public static final String shelterOccupancyKey = "shelter_occupancy";
    public static final String shelterOccupancyTotalReservedKey = "total_reserved";

    private static final String addressKey = "address";
    private static final String capacityKey= "capacity";
    private static final String latitudeKey = "latitude";
    private static final String longitudeKey = "longitude";
    private static final String phoneNumberKey = "phone_number";
    private static final String restrictionsKey = "restrictions";
    public static final String shelterNameKey = "name";
    private static final String specialNotesKey = "special_notes";
    public static final String shelterKeyKey = "Unique Key";

    private String address;
    private int capacity;
    private double latitude;
    private double longitude;
    private String phoneNumber;
    private List<Restrictions> restrictions;
    private String shelterName;
    private String specialNotes;
    private String shelterKey;

    public Shelter(DataSnapshot dataSnapshot) {
        this((HashMap) dataSnapshot.getValue());
        this.shelterKey = dataSnapshot.getKey();
    }

    private Shelter(HashMap<String, Object> shelterData) {
        if (shelterData != null) {
            address = (String) shelterData.get(addressKey);
            capacity = ((Long)shelterData.get(capacityKey)).intValue();
            latitude = (double) shelterData.get(latitudeKey);
            longitude = (double) shelterData.get(longitudeKey);
            phoneNumber = (String) shelterData.get(phoneNumberKey);
            shelterName = (String) shelterData.get(shelterNameKey);
            specialNotes = (String) shelterData.get(specialNotesKey);

            restrictions = new ArrayList<>(10);
            parseRestrictions((HashMap) shelterData.get(restrictionsKey));
        }
    }

    private void parseRestrictions(Map restrictionData) {
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

    public CharSequence getRestrictionsString() {
        if ((restrictions == null) || (restrictions.isEmpty())) {
            return "No Restrictions";
        }

        StringBuilder restrictionsString = new StringBuilder();

        for (Restrictions r: restrictions) {
            restrictionsString.append(r.getName()).append(", ");
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

    public CharSequence getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
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

    public Collection<Restrictions> getRestrictions() {
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

    public CharSequence getSpecialNotes() {
        return specialNotes;
    }

    public void setSpecialNotes(String specialNotes) {
        this.specialNotes = specialNotes;
    }

    public String getShelterKey() {
        return shelterKey;
    }

    public void setShelterKey(String shelterKey) {
        this.shelterKey = shelterKey;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeInt(this.capacity);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.phoneNumber);
        dest.writeList(this.restrictions);
        dest.writeString(this.shelterName);
        dest.writeString(this.specialNotes);
        dest.writeString(this.shelterKey);
    }

    private Shelter(Parcel in) {
        this.address = in.readString();
        this.capacity = in.readInt();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.phoneNumber = in.readString();
        this.restrictions = new ArrayList<Restrictions>();
        in.readList(this.restrictions, Restrictions.class.getClassLoader());
        this.shelterName = in.readString();
        this.specialNotes = in.readString();
        this.shelterKey = in.readString();
    }

    public static final Parcelable.Creator<Shelter> CREATOR = new Parcelable.Creator<Shelter>() {
        @Override
        public Shelter createFromParcel(Parcel source) {
            return new Shelter(source);
        }

        @Override
        public Shelter[] newArray(int size) {
            return new Shelter[size];
        }
    };
}

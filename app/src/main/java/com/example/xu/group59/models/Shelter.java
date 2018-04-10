package com.example.xu.group59.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shelter Model class
 */

public class Shelter implements Parcelable {

    public enum Gender {
        men(Restrictions.men.getName()), women(Restrictions.women.getName());

        final String name;

        Gender(String name) {
            this.name = name;
        }

        /**
         * This method gets the name of the homeless person
         * @return a CharSequence of the name of the homeless person
         */
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

        /**
         * Retrieves the name of the Shelter
         * @return a string of the name of the shelter
         */
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

    /**
     * A constructor for the shelter
     * @param dataSnapshot takes in a packet of the shelters data
     */
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

    /**
     * Gives the restrictions of the shelter
     * @return a string detailing the restrictions or no restrictions
     */
    public String getRestrictionsString() {
        if ((restrictions == null) || restrictions.isEmpty()) {
            return "No Restrictions";
        }

        StringBuilder restrictionsString = new StringBuilder();

        for (Restrictions r: restrictions) {
            restrictionsString.append(r.getName()).append(", ");
        }

        return restrictionsString.substring(0, restrictionsString.length() - 3);
    }

    /**
     * Converting an object to an integer
     * @param input any object
     * @return integer value associated with that object
     */
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

    /**
     * Retrieves the address
     * @return a string of the address of the shelter
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the shelter
     * @param address a string of the address of the shelter
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Retrieves the capacity of the shelter
     * @return an integer of the capacity of the shelter
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the capacity of the shelter
     * @param capacity an integer of the capacity of the shelter
     */

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Retrieves the latitude of the shelter
     * @return a double of the latitude of the shelter
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the shelter
     * @param latitude a double of the lattitude of the shelter
     */

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Retrieves the longitude of the shelter
     * @return  the longitude of the shelter
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the shelter
     * @param longitude a double of the longitude of the shelter
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Retrieves the phone number of the shelter
     * @return a string of the phone number of the shelter
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the shelter
     * @param phoneNumber a string of the phone number of the shelter
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Retrieves the restrictions of the shelter
     * @return a list of the restrictions of the shelter
     */
    public List<Restrictions> getRestrictions() {
        return restrictions;
    }

    /**
     * Sets the restrictions of the shelter
     * @param restrictions a list of the restriction of the shelter
     */
    public void setRestrictions(List<Restrictions> restrictions) {
        this.restrictions = restrictions;
    }

    /**
     * Retrieves the shelter name
     * @return a string of the shelter name
     */
    public String getShelterName() {
        return shelterName;
    }

    /**
     * Sets the shelter name of the shelter
     * @param shelterName a String of the shelter name
     */
    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    /**
     * Retrieves the special notes of the shelter
     * @return a string of the special notes of the shelter
     */
    public String getSpecialNotes() {
        return specialNotes;
    }

    /**
     * Sets the special notes of the shelter
     * @param specialNotes a string of the special notes of the shelter
     */
    public void setSpecialNotes(String specialNotes) {
        this.specialNotes = specialNotes;
    }

    /**
     * Retrieves the shelter key of the shelter
     * @return a string the shelter key of the shelter
     */
    public String getShelterKey() {
        return shelterKey;
    }

    /**
     * Sets the shelter key of the shelter
     * @param shelterKey a string of the shelter key of the shelter
     */
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
        this.restrictions = new ArrayList<>();
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

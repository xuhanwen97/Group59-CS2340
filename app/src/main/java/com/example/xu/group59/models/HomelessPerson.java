package com.example.xu.group59.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.xu.group59.Utils.StringUtils;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomelessPerson implements Parcelable {
    //region [ Declarations ] ================================= //

    public static String homelessPersonKey = "homeless_person";

    public static final String nameKey = "name";
    public static final String passwordKey = "password";
    public static final String statusKey = "status";
    public static final String currentShelterKey = "current_shelter";

    //Enum class to describe if user is a normal, active user, or if it's blocked for some reason
    enum UserStatus {
        Active,
        Blocked,
        Admin,
    }

    //Homeless person's login name is the key in the database
    String login;
    String password;
    String name;
    List<UserStatus> homelessStatus;
    String currentShelter;


    //endregion

    public static Map<String, Boolean> defaultHomelessStatus() {
        HashMap<String, Boolean> homelessStatuses = new HashMap<>();

        homelessStatuses.put("Active", true);

        return homelessStatuses;
    }

    public static Map<String, Boolean> defaultAdminStatus() {
        HashMap<String, Boolean> adminStatuses = new HashMap<>();

        adminStatuses.put("Active", true);
        adminStatuses.put("Admin", true);

        return adminStatuses;
    }

    //region [ Constructors ] ================================= //

    //Public constructor, takes in email, password, and name, and sets status to Active by default
    public HomelessPerson(String email, String password, String name) {
        this(email, password, name, UserStatus.Active);
    }

    private HomelessPerson(String email, String password, String name, UserStatus homelessStatus) {
        if (StringUtils.isNullOrEmpty(email)) {
            throw new IllegalArgumentException("email cannot be null");
        }

        if (StringUtils.isNullOrEmpty(password)) {
            throw new IllegalArgumentException("password cannot be null");
        }

        if (StringUtils.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("name cannot be null");
        }

        if (homelessStatus == null) {
            throw new IllegalArgumentException("homelessStatus cannot be null");
        }

        this.login = email;
        this.password = password;
        this.name = name;

        this.homelessStatus = new ArrayList<>(10);
        this.homelessStatus.add(homelessStatus);
    }

    public HomelessPerson(DataSnapshot homelessSnapshot) {
        HashMap homelessData = (HashMap) homelessSnapshot.getValue();
        String homelessKey = homelessSnapshot.getKey();
        if (homelessKey != null && homelessData != null) {
            login = homelessKey;
            password = (String) homelessData.get(passwordKey);
            name = (String) homelessData.get(nameKey);
            currentShelter = (String) homelessData.get(currentShelterKey);

            Map statusData = (HashMap) homelessData.get(statusKey);
            homelessStatus = new ArrayList<>(10);
            if (statusData != null) {
                for (Object statusString : statusData.keySet()) {
                    switch ((String) statusString) {
                        case "Active":
                            homelessStatus.add(UserStatus.Active);
                            break;
                        case "Blocked":
                            homelessStatus.add(UserStatus.Blocked);
                            break;
                        case "Admin":
                            homelessStatus.add(UserStatus.Admin);
                            break;
                    }
                }
            }
        }
    }

    //endregion

    //region [ Java-Methods ] ================================= //

    //Android Studio generated Equals method
    //Checks login and name
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HomelessPerson homelessPerson = (HomelessPerson) o;

        return login.equals(homelessPerson.login);
    }

    //Android Studio generated hashCode
    @Override
    public int hashCode() {
        return login.hashCode();
    }

    //region [ Parcelable ] ================================= //


    //endregion

    //endregion

    //region [ Getters/Setters ] ================================= //

    public String getUserLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserStatus> getUserStatus() {
        return homelessStatus;
    }

    public void setUserStatus(List<UserStatus> homelessStatus) {
        this.homelessStatus = homelessStatus;
    }
    //endregion

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.login);
        dest.writeString(this.password);
        dest.writeString(this.name);
        dest.writeList(this.homelessStatus);
        dest.writeString(this.currentShelter);
    }

    protected HomelessPerson(Parcel in) {
        this.login = in.readString();
        this.password = in.readString();
        this.name = in.readString();
        this.homelessStatus = new ArrayList<UserStatus>();
        in.readList(this.homelessStatus, UserStatus.class.getClassLoader());
        this.currentShelter = in.readString();
    }

    public static final Creator<HomelessPerson> CREATOR = new Creator<HomelessPerson>() {
        @Override
        public HomelessPerson createFromParcel(Parcel source) {
            return new HomelessPerson(source);
        }

        @Override
        public HomelessPerson[] newArray(int size) {
            return new HomelessPerson[size];
        }
    };
}


package com.example.xu.group59.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.xu.group59.Utils.StringUtils;

public class User implements Parcelable {
    //region [ Declarations ] ================================= //

    //Enum class to describe if user is a normal, active user, or if it's blocked for some reason
    enum UserStatus {
        Active,
        Blocked,
    }

    //For now userID is the email
    String userID;
    String password;
    String name;
    UserStatus userStatus;

    //endregion

    //region [ Constructors ] ================================= //

    //Public constructor, takes in email, password, and name, and sets status to Active by default
    public User(String email, String password, String name) {
        this(email, password, name, UserStatus.Active);
    }

    private User(String email, String password, String name, UserStatus userStatus) {
        if (StringUtils.isNullOrEmpty(email)) {
            throw new IllegalArgumentException("email cannot be null");
        }

        if (StringUtils.isNullOrEmpty(password)) {
            throw new IllegalArgumentException("password cannot be null");
        }

        if (StringUtils.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("name cannot be null");
        }

        if (userStatus == null) {
            throw new IllegalArgumentException("userStatus cannot be null");
        }

        this.userID = email;
        this.password = password;
        this.name = name;
        this.userStatus = userStatus;
    }

    //endregion

    //region [ Java-Methods ] ================================= //

    //Android Studio generated Equals method
    //Checks userID and name
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return userID.equals(user.userID);
    }

    //Android Studio generated hashCode
    @Override
    public int hashCode() {
        return userID.hashCode();
    }

    //region [ Parcelable ] ================================= //

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userID);
        dest.writeString(this.password);
        dest.writeString(this.name);
        dest.writeInt(this.userStatus == null ? -1 : this.userStatus.ordinal());
    }

    protected User(Parcel in) {
        this.userID = in.readString();
        this.password = in.readString();
        this.name = in.readString();
        int tmpUserStatus = in.readInt();
        this.userStatus = tmpUserStatus == -1 ? null : UserStatus.values()[tmpUserStatus];
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    //endregion

    //endregion

    //region [ Getters/Setters ] ================================= //

    public String getUserID() {
        return userID;
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

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
    //endregion
}


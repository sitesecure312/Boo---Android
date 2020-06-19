package com.boo.app.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by razir on 5/26/2016.
 */
public class SignUpData {
    @SerializedName("user_name")
    String userName;
    @SerializedName("user_email")
    String email;
    @SerializedName("user_first_name")
    String firstName;
    @SerializedName("user_last_name")
    String lastName;
    @SerializedName("user_password")
    String password;
    @SerializedName("user_location_latitude")
    double latitude;
    @SerializedName("user_location_longitude")
    double longitude;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}

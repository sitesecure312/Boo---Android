package com.boo.app.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by razir on 5/26/2016.
 */
public class SocialLoginData {
    public static final int MODE_FACEBOOK=1;
    public static final int MODE_TWITTER=2;

    @SerializedName("user_email")
    String email;
    @SerializedName("user_first_name")
    String firstName;
    @SerializedName("user_last_name")
    String lastName;
    @SerializedName("user_photo_url")
    String photoUrl;
    @SerializedName("user_location_latitude")
    double latitude;
    @SerializedName("user_location_longitude")
    double longitude;
    @SerializedName("user_facebook_id")
    String facebookId;
    @SerializedName("user_twitter_id")
    String twitterId;
    @SerializedName("signup_mode")
    int mode;

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public void setMode(int mode) {
        this.mode = mode;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
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

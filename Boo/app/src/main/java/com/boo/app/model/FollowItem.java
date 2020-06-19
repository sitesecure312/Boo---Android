package com.boo.app.model;

import com.google.gson.annotations.SerializedName;

public class FollowItem {

    @SerializedName("user_photo_url")
    String profileUrl;
    @SerializedName("user_full_name")
    String userName;

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

package com.boo.app.model;

import com.google.gson.annotations.SerializedName;

public class RecentActivityItem {


    @SerializedName("activity_user_photo_url")
    String profileImgUrl;
    @SerializedName("activity_user_full_name")
    String userName;
    @SerializedName("activity_type")
    int recentActivityType;
    @SerializedName("activity_date")
    String recentActivityTime;

    public String getProfileImgId() {
        return profileImgUrl;
    }

    public String getUserName() {
        return userName;
    }

    public int getRecentActivityType() {
        return recentActivityType;
    }


    public String getRecentActivityTime() {
        return recentActivityTime;
    }

    public void setProfileImgId(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRecentActivityName(int recentActivityType) {
        this.recentActivityType = recentActivityType;
    }


    public void setRecentActivityTime(String recentActivityTime) {
        this.recentActivityTime = recentActivityTime;
    }
}

package com.boo.app.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by razir on 5/27/2016.
 */
public class GetFollowersData extends PageData {
    @SerializedName("user_id")
    Long userID;

    public void setUserID(Long userID) {
        this.userID = userID;
    }


}

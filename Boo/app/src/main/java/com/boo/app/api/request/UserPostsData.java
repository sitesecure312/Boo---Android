package com.boo.app.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by razir on 5/27/2016.
 */
public class UserPostsData extends PageData {
    @SerializedName("user_id")
    Long userID;

    @SerializedName("ref_id")
    Long refID;

    public void setRefID(Long refID) {
        this.refID = refID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }


}

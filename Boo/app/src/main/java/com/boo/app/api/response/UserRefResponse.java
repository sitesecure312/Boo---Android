package com.boo.app.api.response;

import com.boo.app.model.User;
import com.google.gson.annotations.SerializedName;

/**
 * Created by razir on 5/27/2016.
 */
public class UserRefResponse extends ServerResponse{

    @SerializedName("ref_user")
    User user;

    public User getUser() {
        return user;
    }
}

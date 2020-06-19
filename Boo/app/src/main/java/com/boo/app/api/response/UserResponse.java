package com.boo.app.api.response;

import com.boo.app.model.User;
import com.google.gson.annotations.SerializedName;

/**
 * Created by razir on 5/26/2016.
 */
public class UserResponse extends ServerResponse {

    @SerializedName("current_user")
    User user;

    public User getUser() {
        return user;
    }
}

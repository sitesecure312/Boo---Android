package com.boo.app.api.response;

import com.boo.app.model.FollowItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by razir on 5/26/2016.
 */
public class FollowersResponse extends ServerResponse {
    @SerializedName("users")
    List<FollowItem> data;

    public List<FollowItem> getData() {
        return data;
    }
}

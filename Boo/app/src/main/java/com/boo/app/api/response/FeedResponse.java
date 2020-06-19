package com.boo.app.api.response;

import com.boo.app.model.FeedItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by razir on 5/26/2016.
 */
public class FeedResponse extends ServerResponse {
    @SerializedName("posts")
    List<FeedItem> data;

    public List<FeedItem> getData() {
        return data;
    }
}

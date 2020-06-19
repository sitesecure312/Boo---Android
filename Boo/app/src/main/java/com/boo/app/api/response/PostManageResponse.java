package com.boo.app.api.response;

import com.boo.app.model.FeedItem;
import com.google.gson.annotations.SerializedName;

/**
 * Created by razir on 5/26/2016.
 */
public class PostManageResponse extends ServerResponse{
    @SerializedName("ref_post")
    FeedItem post;

    public FeedItem getPost() {
        return post;
    }
}

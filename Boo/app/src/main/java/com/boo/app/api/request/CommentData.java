package com.boo.app.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by razir on 5/27/2016.
 */
public class CommentData {
    public static final String MODE_ADD="add";
    public static final String MODE_DELETE="delete";

    @SerializedName("mode")
    String mode;
    @SerializedName("user_id")
    Long userID;
    @SerializedName("ref_id")
    Long refID;
    @SerializedName("comment_content")
    String content;

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public void setRefID(Long refID) {
        this.refID = refID;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

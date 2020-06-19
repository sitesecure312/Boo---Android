package com.boo.app.model;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.io.Serializable;

public class Comment implements Serializable{
    @SerializedName("comment_id")
    Long commentID;
    @SerializedName("comment_user_name")
    String userName;
    @SerializedName("comment_user_full_name")
    String userFullName;
    @SerializedName("comment_time_diff")
    int commentDate;
    @SerializedName("comment_content")
    String commentContent;
    @SerializedName("comment_user_id")
    Long userID;
    @SerializedName("comment_user_photo_url")
    String userPhoto;

    public Long getCommentID() {
        return commentID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public int getCommentDate() {
        return commentDate;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public Long getUserID() {
        return userID;
    }

    public String getUserPhoto() {
        return userPhoto;
    }
}

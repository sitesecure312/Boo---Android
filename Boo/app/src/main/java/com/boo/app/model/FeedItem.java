package com.boo.app.model;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

public class FeedItem implements Serializable{
    @SerializedName("post_user_id")
    Long userID;
    @SerializedName("post_id")
    Long postID;
    @SerializedName("post_photo_url")
    String photoUrl;
    @SerializedName("post_video_url")
    String videoUrl;
    @SerializedName("post_location_address")
    String locationAddress;
    @SerializedName("post_location_lat")
    Double lat;
    @SerializedName("post_location_lon")
    Double lon;
    @SerializedName("post_user_full_name")
    String fullName;
    @SerializedName("post_user_name")
    String userName;
    @SerializedName("post_date")
    DateTime postdate;
    @SerializedName("post_time_diff")
    int timeDiff;

    @SerializedName("post_text")
    String postContent;
    @SerializedName("post_user_photo_url")
    String userPhoto;
    @SerializedName("post_booed_count")
    int booedCount;
    @SerializedName("post_comments_count")
    int commentsCount;
    @SerializedName("is_booed_by_me")
    int isBooedByMe;
    @SerializedName("post_comments")
    List<Comment> comments;

    public List<Comment> getComments() {
        return comments;
    }

    public boolean isBooedByMe(){
        return isBooedByMe==1;
    }

    public void setIsBooedByMe(int isBooedByMe) {
        this.isBooedByMe = isBooedByMe;
    }

    public void setBooedCount(int booedCount) {
        this.booedCount = booedCount;
    }

    public int getIsBooedByMe() {
        return isBooedByMe;
    }

    public int getBooedCount() {
        return booedCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getPostID() {
        return postID;
    }

    public void setPostID(Long postID) {
        this.postID = postID;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public DateTime getPostdate() {
        return postdate;
    }

    public void setPostdate(DateTime postdate) {
        this.postdate = postdate;
    }

    public int getTimeDiff() {
        return timeDiff;
    }

    public void setPostdate(int timeDiff) {
        this.timeDiff = timeDiff;
    }



    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
}

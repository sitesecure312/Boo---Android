package com.boo.app.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by razir on 5/26/2016.
 */
public class PostManage {
    public static final String TYPE_ADD = "add";
    public static final String TYPE_DELETE = "delete";

    @SerializedName("user_id")
    Long userId;
    @SerializedName("post_id")
    Long postId;
    @SerializedName("manage_type")
    String manageType;
    @SerializedName("post_text")
    String text;
    @SerializedName("post_photo")
    String photo;
    @SerializedName("post_video")
    String video;
    @SerializedName("post_location_latitude")
    double lat;
    @SerializedName("post_location_longitude")
    double lon;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getManageType() {
        return manageType;
    }

    public void setManageType(String manageType) {
        this.manageType = manageType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}

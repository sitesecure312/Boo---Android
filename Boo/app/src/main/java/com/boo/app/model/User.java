package com.boo.app.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.boo.app.App;
import com.boo.app.api.GsonConfig;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by razir on 5/26/2016.
 */
public class User implements Serializable{

    @SerializedName("user_id")
    Long id;
    @SerializedName("user_name")
    String name;
    @SerializedName("user_full_name")
    String fullName;
    @SerializedName("user_email")
    String email;
    @SerializedName("user_bio")
    String bio;
    @SerializedName("user_photo_url")
    String userPhoto;
    @SerializedName("user_location_latitude")
    double latitude;
    @SerializedName("user_location_longitude")
    double longitude;
    @SerializedName("user_booed_count")
    Integer booedCount;
    @SerializedName("user_booing_count")
    Integer booingCount;
    @SerializedName("user_followers_count")
    int followersCount;
    @SerializedName("user_following_count")
    int followingCount;
    @SerializedName("user_posts_count")
    int postsCount;
    @SerializedName("user_location_address")
    String locationAddress;

    public User(FeedItem post){
        id=post.getUserID();
        name=post.getUserName();
        fullName=post.getFullName();
        userPhoto=post.getUserPhoto();
    }

    public User() {
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void save() {
        save(this);
    }

    public static void deleteCurrent(){
        save(null);
    }

    public static void save(User user) {
        String json = null;
        if (user != null) {
            json = GsonConfig.buildDefaultJson().toJson(user);
        }
        SharedPreferences preferences = App.getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        preferences.edit().putString("user", json).commit();
    }

    public static User getCurrent() {
        SharedPreferences preferences = App.getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        String json = preferences.getString("user", null);
        if (json != null) {
            return GsonConfig.buildDefaultJson().fromJson(json, User.class);
        }
        return null;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getBio() {
        return bio;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Integer getBooedCount() {
        return booedCount;
    }

    public Integer getBooingCount() {
        return booingCount;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public Integer getFollowingCount() {
        return followingCount;
    }

    public Integer getPostsCount() {
        return postsCount;
    }
}

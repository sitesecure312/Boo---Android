package com.boo.app.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by razir on 5/26/2016.
 */
public class LoginData {
    @SerializedName("user_email")
    String email;
    @SerializedName("user_password")
    String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

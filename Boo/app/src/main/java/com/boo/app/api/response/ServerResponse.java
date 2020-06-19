package com.boo.app.api.response;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.User;

/**
 * Created by razir on 5/26/2016.
 */
public class ServerResponse {
    @SerializedName("status")
    int status;
    @SerializedName("msg")
    String msg;

    public boolean isSuccess(){
        return status==1;
    }

    public String getMsg() {
        return msg;
    }
}

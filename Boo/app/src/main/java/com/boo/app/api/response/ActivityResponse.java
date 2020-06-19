package com.boo.app.api.response;


import com.boo.app.model.RecentActivityItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by razir on 5/26/2016.
 */
public class ActivityResponse extends ServerResponse {
    @SerializedName("activities")
    List<RecentActivityItem> data;

    public List<RecentActivityItem> getData() {
        return data;
    }
}
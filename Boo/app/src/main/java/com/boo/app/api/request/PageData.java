package com.boo.app.api.request;

import com.boo.app.api.response.ServerResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by razir on 5/27/2016.
 */
public class PageData {
    @SerializedName("search_offset")
    int searchOffset;
    @SerializedName("search_limit")
    int searchLimit;

    public void setSearchOffset(int searchOffset) {
        this.searchOffset = searchOffset;
    }

    public void setSearchLimit(int searchLimit) {
        this.searchLimit = searchLimit;
    }
}

package com.boo.app.ui.utils;

import com.boo.app.api.request.PageData;

/**
 * Created by razir on 5/27/2016.
 */
public class PageUtils {

    public static final int ITEMS_PER_PAGE = 10;

    public static void setPageInfo(PageData data, int page) {
        data.setSearchLimit(ITEMS_PER_PAGE);
        data.setSearchOffset(ITEMS_PER_PAGE * page);
    }
}

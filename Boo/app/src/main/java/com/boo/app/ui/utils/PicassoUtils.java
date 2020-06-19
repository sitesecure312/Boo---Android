package com.boo.app.ui.utils;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicassoUtils {

    static public void loadBackgroundImg(Context ctx, ImageView imageView, int bgImageId) {
        Picasso.with(ctx)
                .load(bgImageId)
                .fit()
                .centerCrop()
                .into(imageView);
    }

    static public void loadPostImg(Context ctx, int postImage, ImageView imgView){
        Picasso picassoInstance = Picasso.with(ctx);
        picassoInstance.load(postImage)
                .fit()
                .centerCrop()
                .into(imgView);
    }
}

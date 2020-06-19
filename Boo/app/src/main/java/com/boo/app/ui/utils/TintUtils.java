package com.boo.app.ui.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

public class TintUtils {

    static public void setSelectedTint(Context ctx, ImageView imageView, int colorNormal, int colorPressed) {
        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_selected},
                new int[]{android.R.attr.state_selected}
        };
        int[] colors = new int[]{
                ContextCompat.getColor(ctx, colorNormal),
                ContextCompat.getColor(ctx, colorPressed)
        };
        ColorStateList list = new ColorStateList(states, colors);
        Drawable drawable = DrawableCompat.wrap(imageView.getDrawable());
        DrawableCompat.setTintList(drawable, list);
        imageView.setImageDrawable(drawable);
    }

    static public void setPressedTint(Context ctx, ImageView imageView, int colorNormal, int colorPressed) {
        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_pressed},
                new int[]{android.R.attr.state_pressed}
        };
        int[] colors = new int[]{
                ContextCompat.getColor(ctx, colorNormal),
                ContextCompat.getColor(ctx, colorPressed)
        };
        ColorStateList list = new ColorStateList(states, colors);
        Drawable drawable = DrawableCompat.wrap(imageView.getDrawable());
        DrawableCompat.setTintList(drawable, list);
        imageView.setImageDrawable(drawable);
    }
}

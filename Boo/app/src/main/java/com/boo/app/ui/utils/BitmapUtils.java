package com.boo.app.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

/**
 * Created by razir on 4/11/2016.
 */
public class BitmapUtils {

    public static byte[] getBytes(Context context, Uri uri) {
        Bitmap image = null;
        try {
            image = Picasso.with(context).load(uri).resize(800,800).centerInside().get();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        if (image == null) {
            return null;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] imageData = stream.toByteArray();
        return imageData;
    }
}

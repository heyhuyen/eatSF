package com.heyhuyen.eatsf.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Utils for image loading.
 */
public class ImageUtils {

    public static void loadImage(ImageView imageView, String imageUrl, int placeHolder) {
        imageView.setImageResource(0);
        Glide.with(imageView.getContext()).load(imageUrl)
                .placeholder(placeHolder)
                .centerCrop()
                .into(imageView);
    }
}

package com.vbes.util.engine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;

public class ImageEngine {
    public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        /*Glide.with(context)
                .load(uri)
                .asBitmap() // some .jpeg files are actually gif
                .override(resize, resize)
                .placeholder(placeholder)
                .centerCrop()
                .into(imageView);*/
        Glide.with(context)
                .asBitmap() // some .jpeg files are actually gif
                .load(uri)
                .apply(new RequestOptions()
                        .override(resize, resize)
                        .placeholder(placeholder)
                        .centerCrop())
                .into(imageView);
    }

    public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        /*Glide.with(context)
                .load(uri)
                .asBitmap() // some .jpeg files are actually gif
                .override(resize, resize)
                .placeholder(placeholder)
                 .centerCrop()
                .into(imageView);*/
        Glide.with(context)
                .asBitmap() // some .jpeg files are actually gif
                .load(uri)
                .apply(new RequestOptions()
                        .override(resize, resize)
                        .placeholder(placeholder)
                        .centerCrop())
                .into(imageView);
    }

    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        /*Glide.with(context)
                .load(uri)
                .override(resizeX, resizeY)
                .priority(Priority.HIGH)
                .fitCenter()
                .into(imageView);*/
        Glide.with(context)
                .load(uri)
                .apply(new RequestOptions()
                        .override(resizeX, resizeY)
                        .priority(Priority.HIGH)
                        .fitCenter())
                .into(imageView);
    }

    public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        /*Glide.with(context)
                .load(uri)
                .asGif()
                .override(resizeX, resizeY)
                .priority(Priority.HIGH)
                .fitCenter()
                .into(imageView);*/
        Glide.with(context)
                .asGif()
                .load(uri)
                .apply(new RequestOptions()
                        .override(resizeX, resizeY)
                        .priority(Priority.HIGH)
                        .fitCenter())
                .into(imageView);
    }

    public boolean supportAnimatedGif() {
        return true;
    }
}

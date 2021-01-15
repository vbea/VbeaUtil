package com.vbes.util.sticky;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;

import com.vbes.util.sticky.lis.IScreenInfoProvider;

/**
 * Created by Vbe on 2020/12/17.
 */

public class ScreenInfoProvider implements IScreenInfoProvider {

    private final Context mContext;

    public ScreenInfoProvider(Context context) {
        mContext = context;
    }

    @Override
    public int getScreenHeight() {
        return getDeviceDimension().y;
    }

    @Override
    public int getScreenWidth() {
        return getDeviceDimension().x;
    }

    Point getDeviceDimension() {
        Point lPoint = new Point();
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        lPoint.x = metrics.widthPixels;
        lPoint.y = metrics.heightPixels;
        return lPoint;
    }
}

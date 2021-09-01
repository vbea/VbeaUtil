package com.vbes.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * 安卓单位换算工具
 */
public class DensityUtil {

    private DensityUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * dp转px
     * @param context context
     * @param dpVal dp
     * @return px
     */
    public static int dp2px(Context context, float dpVal) {
        return dip2px(context, dpVal);
    }

    /**
     * dp转px
     * @param context context
     * @param dpVal dp
     * @return px
     */
    public static int dip2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     * @param context context
     * @param spVal sp
     * @return px
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     * @param context context
     * @param pxVal px
     * @return dp value
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     * @param context context
     * @param pxVal px
     * @return sp value
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 获取屏幕宽度像素值
     * @param context context
     * @return 屏幕宽度
     */
    public static int getWidthPixel(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度像素值
     * @param context context
     * @return 屏幕高度
     */
    public static int getHeightPixel(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}

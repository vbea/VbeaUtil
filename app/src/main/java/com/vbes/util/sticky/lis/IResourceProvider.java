package com.vbes.util.sticky.lis;

import android.support.annotation.StyleableRes;

/**
 * Created by Vbe on 2020/12/17.
 */
public interface IResourceProvider {
    int getResourceId(@StyleableRes final int styleResId);
    void recycle();
}

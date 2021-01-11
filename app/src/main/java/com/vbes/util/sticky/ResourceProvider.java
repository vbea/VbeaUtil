package com.vbes.util.sticky;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StyleableRes;
import android.util.AttributeSet;

import com.vbes.util.sticky.lis.IResourceProvider;

/**
 * Created by Vbe on 2020/12/17.
 */
public class ResourceProvider implements IResourceProvider {
    private final TypedArray mTypeArray;

    public ResourceProvider(Context context, AttributeSet attrs, @StyleableRes int[] styleRes) {
        mTypeArray = context.obtainStyledAttributes(attrs, styleRes);
    }

    @Override
    public int getResourceId(@StyleableRes int styleResId) {
        return mTypeArray.getResourceId(styleResId, 0);
    }

    @Override
    public void recycle() {
        mTypeArray.recycle();
    }
}

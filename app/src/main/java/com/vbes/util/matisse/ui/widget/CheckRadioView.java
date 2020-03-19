package com.vbes.util.matisse.ui.widget;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.vbes.util.R;

public class CheckRadioView extends AppCompatImageView {

    private Drawable mDrawable;

    private int mSelectedColor;
    private int mUnSelectUdColor;

    public CheckRadioView(Context context) {
        super(context);
        init();
    }



    public CheckRadioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        mSelectedColor =  ResourcesCompat.getColor(getResources(), typedValue.resourceId, getContext().getTheme());
        mUnSelectUdColor = ResourcesCompat.getColor(getResources(), R.color.grey, getContext().getTheme());
        setChecked(false);
    }

    public void setChecked(boolean enable) {
        if (enable) {
            setImageResource(R.drawable.ic_preview_radio_on);
            mDrawable = getDrawable();
            mDrawable.setColorFilter(mSelectedColor, PorterDuff.Mode.SRC_IN);
        } else {
            setImageResource(R.drawable.ic_preview_radio_off);
            mDrawable = getDrawable();
            mDrawable.setColorFilter(mUnSelectUdColor, PorterDuff.Mode.SRC_IN);
        }
    }


    public void setColor(int color) {
        if (mDrawable == null) {
            mDrawable = getDrawable();
        }
        mDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }
}

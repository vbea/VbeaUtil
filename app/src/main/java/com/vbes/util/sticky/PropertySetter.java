package com.vbes.util.sticky;

import android.support.v4.view.ViewCompat;
import android.view.View;

import com.vbes.util.VbeUtil;

/**
 * Created by Vbe on 2020/12/17.
 */
public class PropertySetter {
    public static void setTranslationZ(View view, float translationZ) {
        if (VbeUtil.isAndroidL()) {
            ViewCompat.setTranslationZ(view, translationZ);
        } else if (translationZ != 0) {
            view.bringToFront();
            if (view.getParent() != null) {
                ((View) view.getParent()).invalidate();
            }
        }
    }
}

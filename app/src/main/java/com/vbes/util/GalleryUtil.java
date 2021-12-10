package com.vbes.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vbes.util.media.MimeType;
import com.vbes.util.media.SelectionCreator;
import com.vbes.util.ui.GalleryActivity;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Set;

/**
 * 图片选择器
 */
public final class GalleryUtil {

    private final WeakReference<Activity> mContext;
    private final WeakReference<Fragment> mFragment;

    private GalleryUtil(Activity activity) {
        this(activity, null);
    }

    private GalleryUtil(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    private GalleryUtil(Activity activity, Fragment fragment) {
        mContext = new WeakReference<>(activity);
        mFragment = new WeakReference<>(fragment);
    }

    public static GalleryUtil from(Activity activity) {
        return new GalleryUtil(activity);
    }

    public static GalleryUtil from(Fragment fragment) {
        return new GalleryUtil(fragment);
    }

    public static List<Uri> obtainResult(Intent data) {
        return data.getParcelableArrayListExtra(GalleryActivity.EXTRA_RESULT_SELECTION);
    }

    public static List<String> obtainPathResult(Intent data) {
        return data.getStringArrayListExtra(GalleryActivity.EXTRA_RESULT_SELECTION_PATH);
    }

    public static boolean obtainOriginalState(Intent data) {
        return data.getBooleanExtra(GalleryActivity.EXTRA_RESULT_ORIGINAL_ENABLE, false);
    }

    public SelectionCreator choose(Set<MimeType> mimeTypes) {
        return this.choose(mimeTypes, true);
    }

    public SelectionCreator choose(Set<MimeType> mimeTypes, boolean mediaTypeExclusive) {
        return new SelectionCreator(this, mimeTypes, mediaTypeExclusive);
    }

    @Nullable
    public Activity getActivity() {
        return mContext.get();
    }

    @Nullable
    public Fragment getFragment() {
        return mFragment != null ? mFragment.get() : null;
    }

}

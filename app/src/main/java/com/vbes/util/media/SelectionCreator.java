package com.vbes.util.media;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StyleRes;
import androidx.fragment.app.Fragment;

import com.vbes.util.GalleryUtil;
import com.vbes.util.engine.ImageEngine;
import com.vbes.util.matisse.Filter;
import com.vbes.util.matisse.entity.CaptureStrategy;
import com.vbes.util.matisse.entity.SelectionSpec;
import com.vbes.util.interfaces.OnCheckedListener;
import com.vbes.util.interfaces.OnSelectedListener;
import com.vbes.util.ui.GalleryActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Set;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_BEHIND;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_FULL_USER;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LOCKED;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_NOSENSOR;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT;

@SuppressWarnings("unused")
public final class SelectionCreator {

    private final GalleryUtil mGallery;
    private final SelectionSpec mSelectionSpec;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @IntDef({
            SCREEN_ORIENTATION_UNSPECIFIED,
            SCREEN_ORIENTATION_LANDSCAPE,
            SCREEN_ORIENTATION_PORTRAIT,
            SCREEN_ORIENTATION_USER,
            SCREEN_ORIENTATION_BEHIND,
            SCREEN_ORIENTATION_SENSOR,
            SCREEN_ORIENTATION_NOSENSOR,
            SCREEN_ORIENTATION_SENSOR_LANDSCAPE,
            SCREEN_ORIENTATION_SENSOR_PORTRAIT,
            SCREEN_ORIENTATION_REVERSE_LANDSCAPE,
            SCREEN_ORIENTATION_REVERSE_PORTRAIT,
            SCREEN_ORIENTATION_FULL_SENSOR,
            SCREEN_ORIENTATION_USER_LANDSCAPE,
            SCREEN_ORIENTATION_USER_PORTRAIT,
            SCREEN_ORIENTATION_FULL_USER,
            SCREEN_ORIENTATION_LOCKED
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface ScreenOrientation {
    }

    /**
     * Constructs a new specification builder on the context.
     *
     * @param matisse   a requester context wrapper.
     * @param mimeTypes MIME type set to select.
     */
    public SelectionCreator(GalleryUtil matisse, @NonNull Set<MimeType> mimeTypes, boolean mediaTypeExclusive) {
        mGallery = matisse;
        mSelectionSpec = SelectionSpec.getCleanInstance();
        mSelectionSpec.mimeTypeSet = mimeTypes;
        mSelectionSpec.mediaTypeExclusive = mediaTypeExclusive;
        mSelectionSpec.orientation = SCREEN_ORIENTATION_UNSPECIFIED;
    }

    /**
     * Whether to show only one media type if choosing medias are only images or videos.
     *
     * @param showSingleMediaType whether to show only one media type, either images or videos.
     * @return {@link SelectionCreator} for fluent API.
     * @see SelectionSpec#onlyShowImages()
     * @see SelectionSpec#onlyShowVideos()
     */
    public SelectionCreator showSingleMediaType(boolean showSingleMediaType) {
        mSelectionSpec.showSingleMediaType = showSingleMediaType;
        return this;
    }

    public SelectionCreator theme(@StyleRes int themeId) {
        mSelectionSpec.themeId = themeId;
        return this;
    }

    public SelectionCreator countable(boolean countable) {
        mSelectionSpec.countable = countable;
        return this;
    }

    public SelectionCreator maxSelectable(int maxSelectable) {
        if (maxSelectable < 1)
            throw new IllegalArgumentException("maxSelectable must be greater than or equal to one");
        if (mSelectionSpec.maxImageSelectable > 0 || mSelectionSpec.maxVideoSelectable > 0)
            throw new IllegalStateException("already set maxImageSelectable and maxVideoSelectable");
        mSelectionSpec.maxSelectable = maxSelectable;
        return this;
    }

    public SelectionCreator maxSelectablePerMediaType(int maxImageSelectable, int maxVideoSelectable) {
        if (maxImageSelectable < 1 || maxVideoSelectable < 1)
            throw new IllegalArgumentException(("max selectable must be greater than or equal to one"));
        mSelectionSpec.maxSelectable = -1;
        mSelectionSpec.maxImageSelectable = maxImageSelectable;
        mSelectionSpec.maxVideoSelectable = maxVideoSelectable;
        return this;
    }

    public SelectionCreator addFilter(@NonNull Filter filter) {
        if (mSelectionSpec.filters == null) {
            mSelectionSpec.filters = new ArrayList<>();
        }
        if (filter == null) throw new IllegalArgumentException("filter cannot be null");
        mSelectionSpec.filters.add(filter);
        return this;
    }

    public SelectionCreator capture(boolean enable) {
        mSelectionSpec.capture = enable;
        return this;
    }

    public SelectionCreator originalEnable(boolean enable) {
        mSelectionSpec.originalable = enable;
        return this;
    }

    public SelectionCreator autoHideToolbarOnSingleTap(boolean enable) {
        mSelectionSpec.autoHideToobar = enable;
        return this;
    }

    public SelectionCreator maxOriginalSize(int size) {
        mSelectionSpec.originalMaxSize = size;
        return this;
    }

    public SelectionCreator captureStrategy(CaptureStrategy captureStrategy) {
        mSelectionSpec.captureStrategy = captureStrategy;
        return this;
    }

    public SelectionCreator restrictOrientation(@ScreenOrientation int orientation) {
        mSelectionSpec.orientation = orientation;
        return this;
    }

    public SelectionCreator spanCount(int spanCount) {
        if (spanCount < 1) throw new IllegalArgumentException("spanCount cannot be less than 1");
        mSelectionSpec.spanCount = spanCount;
        return this;
    }

    public SelectionCreator gridExpectedSize(int size) {
        mSelectionSpec.gridExpectedSize = size;
        return this;
    }

    public SelectionCreator thumbnailScale(float scale) {
        if (scale <= 0f || scale > 1f)
            throw new IllegalArgumentException("Thumbnail scale must be between (0.0, 1.0]");
        mSelectionSpec.thumbnailScale = scale;
        return this;
    }

    @Deprecated
    public SelectionCreator imageEngine(ImageEngine imageEngine) {
        mSelectionSpec.imageEngine = imageEngine;
        return this;
    }

    @NonNull
    public SelectionCreator setOnSelectedListener(@Nullable OnSelectedListener listener) {
        mSelectionSpec.onSelectedListener = listener;
        return this;
    }

    /**
     * Set listener for callback immediately when user check or uncheck original.
     *
     * @param listener {@link OnSelectedListener}
     * @return {@link SelectionCreator} for fluent API.
     */
    public SelectionCreator setOnCheckedListener(@Nullable OnCheckedListener listener) {
        mSelectionSpec.onCheckedListener = listener;
        return this;
    }

    /**
     * Start to select media and wait for result.
     *
     * @param requestCode Identity of the request Activity or Fragment.
     */
    public void forResult(int requestCode) {
        Activity activity = mGallery.getActivity();
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, GalleryActivity.class);

        Fragment fragment = mGallery.getFragment();
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public SelectionCreator showPreview(boolean showPreview) {
        mSelectionSpec.showPreview = showPreview;
        return this;
    }
}

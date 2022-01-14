package com.vbes.util.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vbes.util.VbeUtil;

import androidx.annotation.AnyRes;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public abstract class BaseFragment extends Fragment {
    private ProgressDialog dialog;
    private int layoutRes;
    protected View mView;
    protected FragmentActivity mActivity;
    private final SparseArray<View> views = new SparseArray();

    public BaseFragment(@LayoutRes int id) {
        this.layoutRes = id;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mView = inflater.inflate(this.layoutRes, container, false);
        return this.mView;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onInitView(view);
    }

    public abstract void onInitView(View paramView);

    public void showLoading(@StringRes int str) {
        showLoading(getString(str));
    }

    public void showLoading(String msg) {
        if ((this.dialog != null) && (this.dialog.isShowing())) {
            return;
        }
        this.dialog = new ProgressDialog(getActivity());
        this.dialog.requestWindowFeature(1);
        this.dialog.setCanceledOnTouchOutside(false);
        this.dialog.setProgressStyle(0);
        this.dialog.setMessage(msg);
        this.dialog.show();
    }

    public void hideLoading() {
        if ((this.dialog != null) && (this.dialog.isShowing())) {
            this.dialog.dismiss();
        }
    }

    public void showToast(int id) {
        showToast(getString(id));
    }

    public void showToast(String msg) {
        if (!VbeUtil.isNullOrEmpty(msg)) {
            VbeUtil.toastShortMessage(getActivity(), msg);
        }
    }

    public void displayImage(String url, ImageView imageView) {
        Glide.with(getActivity()).load(url).into(imageView);
    }

    public <T extends View> T getView(@IdRes int id) {
        View view = (View) this.views.get(id);
        if (view == null) {
            view = this.mView.findViewById(id);
            this.views.put(id, view);
        }
        return (T)view;
    }

    public void setText(@IdRes int viewId, CharSequence value) {
        TextView view = (TextView) getView(viewId);
        if (view != null) {
            view.setText(value);
        }
    }

    public void setText(@IdRes int viewId, @StringRes int value) {
        TextView view = (TextView) getView(viewId);
        if (view != null) {
            view.setText(value);
        }
    }

    public void setTextColor(@IdRes int viewId, @ColorInt int textColor) {
        TextView view = (TextView) getView(viewId);
        if (view != null) {
            view.setTextColor(textColor);
        }
    }

    public void setTextColor(@IdRes int viewId, ColorStateList textColor) {
        TextView view = (TextView) getView(viewId);
        if (view != null) {
            view.setTextColor(textColor);
        }
    }

    public void setTextColorResource(@IdRes int viewId, @ColorRes int colorId) {
        TextView view = (TextView) getView(viewId);
        if (view != null) {
            view.setTextColor(getRes2Color(colorId));
        }
    }

    public void setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        View view = getView(viewId);
        if ((view instanceof ImageView)) {
            ((ImageView) view).setImageBitmap(bitmap);
        }
    }

    public void setImageDrawable(@IdRes int viewId, Drawable drawable) {
        View view = getView(viewId);
        if ((view instanceof ImageView)) {
            ((ImageView) view).setImageDrawable(drawable);
        }
    }

    public void setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        View view = getView(viewId);
        if ((view instanceof ImageView)) {
            ((ImageView) view).setImageResource(imageResId);
        }
    }

    public void setBackgroundResource(@IdRes int viewId, @DrawableRes int value) {
        View view = getView(viewId);
        if (view != null) {
            view.setBackgroundResource(value);
        }
    }

    public void setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
        View view = getView(viewId);
        if (view != null) {
            view.setBackgroundColor(color);
        }
    }

    @SuppressLint({"NewApi"})
    public void setBackground(@IdRes int viewId, Drawable background) {
        View view = getView(viewId);
        if (view != null) {
            view.setBackground(background);
        }
    }

    public void setVisible(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        if (view != null) {
            view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void setGone(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        if (view != null) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    public void setEnabled(@IdRes int viewId, boolean enable) {
        View view = getView(viewId);
        if (view != null) {
            view.setEnabled(enable);
        }
    }

    public void setClickable(@IdRes int viewId, boolean able) {
        View view = getView(viewId);
        if (view != null) {
            view.setClickable(able);
        }
    }

    public void setChecked(@IdRes int viewId, boolean checked) {
        View view = getView(viewId);
        if ((view instanceof Checkable)) {
            ((Checkable) view).setChecked(checked);
        }
    }

    public void setOnClickListener(@IdRes int id, View.OnClickListener listener) {
        View view = getView(id);
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }

    public void setOnLongClickListener(@IdRes int viewId, View.OnLongClickListener onClickListener) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnLongClickListener(onClickListener);
        }
    }

    @ColorInt
    public int getRes2Color(@ColorRes int id) {
        if (VbeUtil.isAndroidM()) {
            return getContext().getColor(id);
        }
        return getContext().getResources().getColor(id);
    }

    @AnyRes
    public int getThemeAttr(@AttrRes int id) {
        TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(id, typedValue, true);
        return typedValue.resourceId;
    }

    @ColorInt
    public int getThemeColor(@AttrRes int id) {
        return ResourcesCompat.getColor(getResources(), getThemeAttr(id), getActivity().getTheme());
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void onDestroyView() {
        this.views.clear();
        hideLoading();
        super.onDestroyView();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}

package com.vbes.util.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.AnyRes;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vbes.util.R;
import com.vbes.util.VbeUtil;
import com.vbes.util.interfaces.DialogResult;

/**
 * Created by Vbe on 2021/2/3.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    private ProgressDialog progressDialog;
    private final SparseArray<View> views = new SparseArray<>();

    public abstract void setTheme();

    public abstract void renderView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
        renderView();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    /**
     * 启用Toolbar
     * @param id Toolbar view id
     */
    public void enableToolBar(@IdRes int id) {
        if (toolbar == null) {
            toolbar = getView(id);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
            }
        }
    }

    /**
     * 设置Toolbar标题
     * @param t 标题
     */
    public void setToolbarTitle(String t) {
        if (toolbar != null) {
            toolbar.setTitle(t);
        }
    }

    /**
     * 启用Toolbar返回键
     * @param id Toolbar view id
     */
    public void enableBackButton(@IdRes int id) {
        enableToolBar(id);
        enableBackButton(id, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });
    }

    public void enableBackButton(@IdRes int id, View.OnClickListener onClickListener) {
        enableToolBar(id);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(onClickListener);
        }
    }

    /**
     * 启用Toolbar返回键
     * @param id Toolbar view id
     * @param backRes 返回键图标资源id
     */
    public void enableBackButton(@IdRes int id, @DrawableRes int backRes) {
        enableBackButton(id, backRes, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });
    }

    /**
     * 启用Toolbar返回键
     * @param id Toolbar view id
     * @param backRes 返回键图标资源id
     * @param onClickListener 返回键点击监听器
     */
    public void enableBackButton(@IdRes int id, @DrawableRes int backRes, View.OnClickListener onClickListener) {
        enableToolBar(id);
        if (toolbar != null) {
            toolbar.setNavigationIcon(backRes);
            toolbar.setNavigationOnClickListener(onClickListener);
        }
    }

    protected void toastShortMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void toastLongMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    protected void toastShortMessage(@StringRes int message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void toastLongMessage(@StringRes int message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void showLoading(@StringRes int id) {
        showLoading(getString(id));
    }

    public void showLoading(String msg) {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }
        progressDialog = ProgressDialog.show(this, "", msg, false, false);
    }

    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void showDialog(@NonNull String title, @NonNull String message) {
        VbeUtil.showConfirmCancelDialog(this, title, message, new DialogResult() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    public void showDialog(@NonNull String title, @NonNull String message, DialogResult result) {
        VbeUtil.showConfirmCancelDialog(this, title, message, result);
    }

    public void showDialog(@NonNull String title, @NonNull String message, @NonNull String ok, @Nullable String cancel, final DialogResult result) {
        if (VbeUtil.isNullOrEmpty(cancel)) {
            VbeUtil.showResultDialog(this, message, title, ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    result.onConfirm();
                }
            });
        } else {
            VbeUtil.showConfirmCancelDialog(this, title, message, ok, cancel, result);
        }
    }

    public void displayImage(String url, ImageView imageView) {
        Glide.with(this).load(url).into(imageView);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int id) {
        View view = this.views.get(id);
        if (view == null) {
            view = findViewById(id);
            this.views.put(id, view);
        }
        return (T) view;
    }

    public void setText(View view, @IdRes int id, String str) {
        TextView textView = view.findViewById(id);
        if (textView != null)
            textView.setText(str);
    }

    public void setVisible(View view, @IdRes int id, boolean visible) {
        View view1 = view.findViewById(id);
        if (view1 != null)
            view1.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    public void setGone(View view, @IdRes int id, boolean visible) {
        View view1 = view.findViewById(id);
        if (view1 != null)
            view1.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setText(@IdRes int viewId, CharSequence value) {
        TextView view = (TextView) this.getView(viewId);
        if (view != null)
            view.setText(value);
    }

    public void setText(@IdRes int viewId, @StringRes int value) {
        TextView view = (TextView) this.getView(viewId);
        if (view != null)
            view.setText(value);
    }

    public void setTextColor(@IdRes int viewId, @ColorInt int textColor) {
        TextView view = getView(viewId);
        if (view != null)
            view.setTextColor(textColor);
    }

    public void setTextColor(@IdRes int viewId, ColorStateList textColor) {
        TextView view = getView(viewId);
        if (view != null)
            view.setTextColor(textColor);
    }

    public void setTextColorResource(@IdRes int viewId, @ColorRes int colorId) {
        TextView view = getView(viewId);
        if (view != null)
            view.setTextColor(getRes2Color(colorId));
    }

    public void setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        View view = getView(viewId);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageBitmap(bitmap);
        }
    }

    public void setImageDrawable(@IdRes int viewId, Drawable drawable) {
        View view = getView(viewId);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(drawable);
        }
    }

    public void setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        View view = getView(viewId);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(imageResId);
        }
    }

    public void setBackgroundResource(@IdRes int viewId, @DrawableRes int value) {
        View view = this.getView(viewId);
        if (view != null)
            view.setBackgroundResource(value);
    }

    public void setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
        View view = getView(viewId);
        if (view != null)
            view.setBackgroundColor(color);
    }

    @SuppressLint("NewApi")
    public void setBackground(@IdRes int viewId, Drawable background) {
        View view = getView(viewId);
        if (view != null)
            view.setBackground(background);
    }

    public void setVisible(@IdRes int viewId, boolean visible) {
        View view = this.getView(viewId);
        if (view != null)
            view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    public void setGone(@IdRes int viewId, boolean visible) {
        View view = this.getView(viewId);
        if (view != null)
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setEnabled(@IdRes int viewId, boolean enable) {
        View view = this.getView(viewId);
        if (view != null)
            view.setEnabled(enable);
    }

    public void setClickable(@IdRes int viewId, boolean able) {
        View view = this.getView(viewId);
        if (view != null)
            view.setClickable(able);
    }

    public void setChecked(@IdRes int viewId, boolean checked) {
        View view = getView(viewId);
        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(checked);
        }
    }

    public void setOnClickListener(@IdRes int id, View.OnClickListener listener) {
        View view = this.getView(id);
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }

    public void setOnLongClickListener(@IdRes int viewId, View.OnLongClickListener onClickListener) {
        View view = this.getView(viewId);
        if (view != null) {
            view.setOnLongClickListener(onClickListener);
        }
    }

    @ColorInt
    public int getRes2Color(@ColorRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getColor(id);
        } else {
            return getResources().getColor(id);
        }
    }

    @AnyRes
    public int getThemeAttr(@AttrRes int id) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(id, typedValue, true);
        return typedValue.resourceId;
    }

    @ColorInt
    public int getThemeColor(@AttrRes int id) {
        return ResourcesCompat.getColor(getResources(), getThemeAttr(id), getTheme());
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        views.clear();
        super.onDestroy();
    }
}

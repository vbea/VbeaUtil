package com.vbes.util.list;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Vbe on 2018/09/28.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> views = new SparseArray<>();
    private BaseListAdapter.OnItemClickListener mListener;
    private BaseListAdapter.OnItemLongClickListener longClickListener;
    private View mView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    @Deprecated
    public <T extends View> T bind(@IdRes int id) {
        return mView.findViewById(id);
    }

    @Deprecated
    public void setViews(View view) {
        mView = view;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int id) {
        View view = this.views.get(id);
        if (view == null) {
            view = this.mView.findViewById(id);
            this.views.put(id, view);
        }
        return (T) view;
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
        view.setTextColor(textColor);
    }

    public void setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
    }

    public void setImageDrawable(@IdRes int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
    }

    public void setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
    }

    public void setBackgroundResource(@IdRes int viewId, @DrawableRes int value) {
        View view = this.getView(viewId);
        if (view != null)
            view.setBackgroundResource(value);
    }

    public void setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
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

    public void setChecked(@IdRes int viewId, boolean checked) {
        View view = getView(viewId);
        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(checked);
        }
    }

    public void addClick(@IdRes int viewId) {
        View view = this.getView(viewId);
        if (view != null && mListener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(getAdapterPosition(), view);
                }
            });
        }
    }

    public void addLongClick(@IdRes int viewId) {
        View view = this.getView(viewId);
        if (view != null && longClickListener != null) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longClickListener.onItemLongClick(getAdapterPosition(), view);
                    return true;
                }
            });
        }
    }

    public void setOnItemClickListener(BaseListAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void setOnItemLongClickListener(BaseListAdapter.OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }
}

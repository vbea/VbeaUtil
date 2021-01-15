package com.vbes.util.list;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
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
            view.setTextColor(getRes2Color(view.getContext(), colorId));
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

    public void addClick(@IdRes int viewId) {
        View view = this.getView(viewId);
        addClick(view);
    }

    public void addLongClick(@IdRes int viewId) {
        View view = this.getView(viewId);
        addLongClick(view);
    }

    public void addClick(View view) {
        if (view != null && mListener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(getAdapterPosition(), view);
                }
            });
        }
    }

    public void addLongClick(View view) {
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

    public void setOnClickListener(@IdRes int viewId, View.OnClickListener onClickListener) {
        View view = this.getView(viewId);
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
    }

    public void setOnLongClickListener(@IdRes int viewId, View.OnLongClickListener onClickListener) {
        View view = this.getView(viewId);
        if (view != null) {
            view.setOnLongClickListener(onClickListener);
        }
    }

    public void setOnItemClickListener(BaseListAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void setOnItemLongClickListener(BaseListAdapter.OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public String getString(@StringRes int id) {
        return mView.getContext().getString(id);
    }

    @ColorInt
    public int getRes2Color(@ColorRes int id) {
        return getRes2Color(mView.getContext(), id);
    }

    @ColorInt
    public int getRes2Color(Context context, @ColorRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(id, context.getTheme());
        } else {
            return context.getResources().getColor(id);
        }
    }
}

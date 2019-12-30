package com.vbes.util.list;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Vbe on 2018/09/28.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> views = new SparseArray<>();
    private View mView;

    BaseViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public <T extends View> T bind(@IdRes int id) {
        return mView.findViewById(id);
    }

    public View getView(@IdRes int id) {
        View view = this.views.get(id);
        if (view == null) {
            view = this.bind(id);
            this.views.put(id, view);
        }
        return mView.findViewById(id);
    }

    public void setText(@IdRes int viewId, CharSequence value) {
        TextView view = (TextView) this.getView(viewId);
        if (view != null)
            view.setText(value);
    }

    public void setBackgroundResource(@IdRes int viewId, @DrawableRes int value) {
        View view = this.getView(viewId);
        if (view != null)
            view.setBackgroundResource(value);
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
}

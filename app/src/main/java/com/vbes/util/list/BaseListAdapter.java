package com.vbes.util.list;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vbe on 2018/9/28.
 */
public abstract class BaseListAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int EMPTY_VIEW = 1000;
    private static final int NORMAL_VIEW = 1001;
    private int layoutRes;
    protected Context mContext;
    protected List<T> mList;
    private BaseListAdapter.OnItemClickListener mListener;
    private BaseListAdapter.OnItemLongClickListener longClickListener;
    //protected BaseViewHolder holder;
    private FrameLayout mEmptyLayout;
    public BaseListAdapter(@LayoutRes int resId) {
        layoutRes = resId;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup p1, int type) {
        mContext = p1.getContext();
        if (type == EMPTY_VIEW) {
            return new BaseViewHolder(mEmptyLayout);
        } else {
            View mView = LayoutInflater.from(p1.getContext()).inflate(layoutRes, p1, false);
            BaseViewHolder holder = new BaseViewHolder(mView);
            if (mListener != null)
                holder.setOnItemClickListener(mListener);
            if (longClickListener != null)
                holder.setOnItemLongClickListener(longClickListener);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if (viewType == EMPTY_VIEW) {
            //empty view
        } else {
            if (mList == null || position >= mList.size()) {
                onRender(holder, null, position);
            } else {
                onRender(holder, mList.get(position), position);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getEmptyViewCount() == 1) {
            if (position == 0) {
                return EMPTY_VIEW;
            }
            return NORMAL_VIEW;
        }
        return super.getItemViewType(position);
    }

    private int getEmptyViewCount() {
        if (mList == null && mEmptyLayout != null)
            return 1;
        if (mEmptyLayout == null || mEmptyLayout.getChildCount() == 0) {
            return 0;
        }
        if (mList.size() != 0) {
            return 0;
        }
        return 1;
    }

    public void setEmptyView(int layoutResId, ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutResId, viewGroup, false);
        setEmptyView(view);
    }

    public void setEmptyView(View emptyView) {
        boolean insert = false;
        if (mEmptyLayout == null) {
            mEmptyLayout = new FrameLayout(emptyView.getContext());
            final RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
            final ViewGroup.LayoutParams lp = emptyView.getLayoutParams();
            if (lp != null) {
                layoutParams.width = lp.width;
                layoutParams.height = lp.height;
            }
            mEmptyLayout.setLayoutParams(layoutParams);
            insert = true;
        }
        mEmptyLayout.removeAllViews();
        mEmptyLayout.addView(emptyView);
        if (insert) {
            notifyItemInserted(0);
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (getEmptyViewCount() == 1) {
            count = 1;
        } else if (mList != null){
            count = mList.size();
        }
        return count;
    }

    public int getDataCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }

    protected abstract void onRender(BaseViewHolder holder, T item, int position);

    public void setNewData(List<T> data) {
        if (data != null)
            mList = data;
        else
            mList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addData(T data) {
        if (mList == null)
            mList = new ArrayList<>();
        mList.add(data);
        notifyItemInserted(mList.size() - 1);
    }

    public void addData(List<T> data) {
        if (mList == null)
            mList = new ArrayList<>();
        int old = mList.size();
        mList.addAll(data);
        notifyItemRangeInserted(old, data.size());
    }

    public List<T> getData() {
        return mList;
    }

    public void removeData(int position) {
        if (mList != null) {
            mList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeAll() {
        if (mList != null) {
            int count = mList.size();
            mList.clear();
            notifyItemRangeRemoved(0, count);
        }
    }

    public void clear() {
        if (mList != null) {
            mList.clear();
        }
        notifyDataSetChanged();
    }

    public T getItemData(int position) {
        if (mList != null) {
            return mList.get(position);
        } else {
            return null;
        }
    }

    public void setOnItemClickListener(BaseListAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void setOnItemLongClickListener(BaseListAdapter.OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position, View view);
    }
}

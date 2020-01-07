package com.vbes.util.list;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vbe on 2018/9/28.
 */
public abstract class BaseListAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private int layoutRes;
    private List<T> mList;
    private OnItemClickListener mListener;
    private OnItemLongClickListener longClickListener;

    public BaseListAdapter(@LayoutRes int resId) {
        layoutRes = resId;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup p1, int p2)
    {
        View mView = LayoutInflater.from(p1.getContext()).inflate(layoutRes, p1, false);
        return new BaseViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        onRender(holder, mList.get(position), position);
    }

    public void addClick(final int position, View v) {
        if (mListener != null) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(position, view);
                }
            });
        }
    }

    public void addLongClick(final int position, View v) {
        if (longClickListener != null) {
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longClickListener.onItemLongClick(position, view);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
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

    public void removeData(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public T getItemData(int position) {
        return mList.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position, View view);
    }
}

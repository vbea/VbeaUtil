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
    private BaseListAdapter.OnItemClickListener mListener;
    private BaseListAdapter.OnItemLongClickListener longClickListener;
    protected BaseViewHolder holder;
    public BaseListAdapter(@LayoutRes int resId) {
        layoutRes = resId;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup p1, int p2)
    {
        View mView = LayoutInflater.from(p1.getContext()).inflate(layoutRes, p1, false);
        holder = new BaseViewHolder(mView);
        if (mListener != null)
            holder.setOnItemClickListener(mListener);
        if (longClickListener != null)
            holder.setOnItemLongClickListener(longClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        onRender(holder, mList.get(position), position);
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

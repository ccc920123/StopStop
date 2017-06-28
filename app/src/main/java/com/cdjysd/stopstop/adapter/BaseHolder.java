package com.cdjysd.stopstop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cdjysd.stopstop.base.ItemListener;

import butterknife.ButterKnife;

public abstract class BaseHolder<T> extends RecyclerView.ViewHolder {
    public View mView;
    public T mData;
    public Context mContext;
    public ItemListener listener;

    public View getView() {
        return mView;
    }

    public BaseHolder(View view) {
        super(view);
        this.mView = view;
        mContext = this.mView.getContext();
        ButterKnife.bind(this, mView);
        init();
    }


    public void init() {

    }

    public void setData(T mData) {
        this.mData = mData;
    }

    public void setListener(ItemListener listener) {
        this.listener = listener;
    }
}
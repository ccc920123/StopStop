package com.cdjysd.stopstop.mvp.presenter;


import com.cdjysd.stopstop.mvp.view.BaseView;

public abstract class BasePresenter<T extends BaseView> {
    public T mView;

    public void attach(T mView) {
        this.mView = mView;
    }

    public void detachView() {
        if (mView != null) {
            mView = null;
        }
    }



}
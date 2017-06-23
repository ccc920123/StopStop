package com.cdjysd.stopstop.mvp.view;

/**
 * MVP基础view
 */
public interface BaseView {

    void showLoadProgressDialog(String str);

    void disDialog();

    void showToast(String message);

}
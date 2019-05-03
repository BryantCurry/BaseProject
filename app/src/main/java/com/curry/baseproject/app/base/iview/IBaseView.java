package com.curry.baseproject.app.base.iview;

public interface IBaseView {

    void showLoading();

    void hideLoading();

    void showException(int code, String msg);
}

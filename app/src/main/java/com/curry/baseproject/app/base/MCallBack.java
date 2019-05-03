package com.curry.baseproject.app.base;

/**
 * Created by Bryant on 2018/3/23.
 */

public interface MCallBack<T> {

    void onSuccess(T data);

    void onFailure(int code, String msg);

}

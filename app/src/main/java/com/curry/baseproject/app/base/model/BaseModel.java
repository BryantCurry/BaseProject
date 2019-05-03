package com.curry.baseproject.app.base.model;


import com.curry.baseproject.app.base.MCallBack;

/**
 * Created by Bryant on 2018/3/23.
 */

public abstract class BaseModel<T> {

    //数据请求参数
    protected String[] mParams;

    /**
     * 设置数据请求参数
     */
    public BaseModel<T> setParams(String... args){
        this.mParams = args;
        return this;
    }

    // 添加Callback并执行数据请求
    public abstract void execute(MCallBack<T> mCallBack);

}

package com.curry.baseproject.app.base.model;

/**
 * Created by Bryant on 2018/3/23.
 */

public class ModelManager {
    public static <T> T newInstance (Class<T> tClass) {
        T model = null;
        try {
            model = tClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return model;
    }
}

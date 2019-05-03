package com.curry.baseproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by LH-Android-00 on 2017/4/17.
 */

public class SharedPreferencesUtil {

    private static final String TAG = "PZTuan.SharePreferencesUtil";
    private static final String SHAREDPREFERENCE_NAME = "sharedpreferences";

    private static SharedPreferencesUtil mInstance;

    private static SharedPreferences mSharedPreferences;

    private static SharedPreferences.Editor mEditor;

    public static SharedPreferencesUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SharedPreferencesUtil.class) {
                if (mInstance == null) {
                    synchronized (SharedPreferencesUtil.class) {
                        mInstance = new SharedPreferencesUtil(context);
                    }
                }
            }
        }
        return mInstance;
    }

    private SharedPreferencesUtil(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public synchronized boolean putString(String key, String value) {
        mEditor.putString(key, value);
        return mEditor.commit();
    }

    public synchronized boolean putStringArrayList(String key, ArrayList<String> value) {
        /*for (int j = 0; j < value.size() - 1; j++) {
            if (value.get(value.size() - 1).equals(value.get(j))) {
                value.remove(j);
            }
        }*/
        mEditor.putInt(key, value.size());

        for(int i = 0; i < value.size(); i++) {
            mEditor.remove(key + i);
            mEditor.putString(key + i, value.get(i));
        }
        return mEditor.commit();
    }

    /**
     * 保存List
     */
    public <T> void putDataList(String tag, List<T> datalist) {
        String strJson = "";
        if (null == datalist || datalist.size() <= 0) {
            strJson = "";
        } else {
            Gson gson = new Gson();
            //转换成json数据，再保存
            strJson = gson.toJson(datalist);
        }

        mEditor.clear();
        mEditor.putString(tag, strJson);
        mEditor.commit();

    }

    /**
     * 获取List
     */
    public <T> List<T> getDataList(String tag) {
        List<T> datalist = new ArrayList<T>();
        String strJson = mSharedPreferences.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());
        return datalist;
    }

    public synchronized boolean putMap(String key, Map<String, String> map) {
        Gson mGson = new Gson();
        String mJsonMap = mGson.toJson(map);
        mEditor.putString(key, mJsonMap);
        return mEditor.commit();
    }

    public synchronized boolean putInt(String key, int value) {
        mEditor.putInt(key, value);
        return mEditor.commit();
    }

    public synchronized boolean putLong(String key, long value) {
        mEditor.putLong(key, value);
        return mEditor.commit();
    }

    public synchronized boolean putFloat(String key, float value) {
        mEditor.putFloat(key, value);
        return mEditor.commit();
    }

    public synchronized boolean putBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        return mEditor.commit();
    }

    public synchronized boolean putStringSet(String key, Set<String> value) {
        mEditor.putStringSet(key, value);
        return mEditor.commit();
    }

    public String getString(String key, String value) {
        return mSharedPreferences.getString(key, value);
    }

    public ArrayList<String> getStringArrayList(String key) {
        ArrayList<String> al = new ArrayList<String>();
        if (!al.isEmpty()) {
            al.clear();
        }
        int size = mSharedPreferences.getInt(key, 0);

        for(int i = 0; i < size; i++) {
            al.add(mSharedPreferences.getString(key + i, null));

        }
        return al;
    }

    public int getInt(String key, int value) {
        return mSharedPreferences.getInt(key, value);
    }

    public long getLong(String key, long value) {
        return mSharedPreferences.getLong(key, value);
    }

    public float getFloat(String key, float value) {
        return mSharedPreferences.getFloat(key, value);
    }

    public boolean getBoolean(String key, boolean value) {
        return mSharedPreferences.getBoolean(key, value);
    }

    public Set<String> getStringSet(String key, Set<String> value) {
        return mSharedPreferences.getStringSet(key, value);
    }

    public boolean remove(String key) {
        mEditor.remove(key);
        return mEditor.commit();
    }

    public boolean clear() {
        mEditor.clear();
        return mEditor.commit();
    }

}

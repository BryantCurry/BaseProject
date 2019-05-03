package com.curry.baseproject;

import android.app.Application;
import android.content.Context;

import com.curry.baseproject.app.appmanager.CrashHandlerUtil;
import com.curry.baseproject.app.appmanager.JConfig;
import com.vise.log.ViseLog;
import com.vise.log.inner.LogcatTree;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.interceptor.HttpLogInterceptor;

import java.util.HashMap;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Bryant on 2018/4/11.
 */

public class MyApp extends Application {

    private static MyApp myApp;
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        mContext = myApp.getApplicationContext();
        initCrash();
        initLog();
        initNet();
    }

    public static MyApp getApp() {
        return myApp;
    }

    private void initCrash() {
        CrashHandlerUtil crashHandlerUtil = CrashHandlerUtil.getInstance();
        crashHandlerUtil.init(this);
        crashHandlerUtil.setCrashTip("很抱歉，程序出现异常，即将退出！");
    }

    private void initLog() {
        ViseLog.getLogConfig()
                .configAllowLog(JConfig.SHOW_LOG)//是否输出日志
                .configShowBorders(false);//是否排版显示
        ViseLog.plant(new LogcatTree());//添加打印日志信息到Logcat的树
    }

    private void initNet() {
        HashMap<String, String> mHeaders = new HashMap<>();
        mHeaders.put("app", "Android");
        mHeaders.put("version", BuildConfig.VERSION_NAME);
        mHeaders.put("versionCode", String.valueOf(BuildConfig.VERSION_CODE));
        mHeaders.put("Accept", "application/json");

        ViseHttp.init(this);
        ViseHttp.CONFIG()
                //配置请求主机地址
                .baseUrl(JConfig.API_HOST)
                //配置全局请求头
                .globalHeaders(mHeaders)
                //配置读取超时时间，单位秒
                .readTimeout(30)
                //配置写入超时时间，单位秒
                .writeTimeout(30)
                //配置连接超时时间，单位秒
                .connectTimeout(30)
                //配置请求失败重试次数
                .retryCount(2)
                //配置请求失败重试间隔时间，单位毫秒
                .retryDelayMillis(1000)
                //配置是否使用cookie
                .setCookie(true)
                //配置是否使用OkHttp的默认缓存
                .setHttpCache(true)
                //配置应用级拦截器
                .interceptor(new HttpLogInterceptor()
                        .setLevel(HttpLogInterceptor.Level.BODY))
                //配置转换工厂
                .converterFactory(GsonConverterFactory.create())
                //配置适配器工厂
                .callAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    public Context getAppContext() {
        return this.mContext;
    }

}

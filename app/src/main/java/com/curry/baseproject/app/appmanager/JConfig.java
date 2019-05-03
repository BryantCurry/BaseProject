package com.curry.baseproject.app.appmanager;

public class JConfig {
    public static final boolean IS_DEBUG = true;

    public static final boolean SHOW_LOG = true;

    private static String DEBUG_API_HOST = "https://betashy.guigug.com/gateway-app/";   //默认测试API主机地址

    private static String PRODUCT_API_HOST = "https://renshegc.com/gateway-app/";   //默认生产API主机地址

    private static String DEBUG_WEB_HOST = "https://betashy.guigug.com/";   //默认测试API主机地址

    private static String PRODUCT_WEB_HOST = "https://renshegc.com/";   //默认生产API主机地址

    public static String API_HOST = IS_DEBUG ? DEBUG_API_HOST : PRODUCT_API_HOST;

    public static String WEB_HOST = IS_DEBUG ? DEBUG_WEB_HOST : PRODUCT_WEB_HOST;

}

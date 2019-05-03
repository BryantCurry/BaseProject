package com.curry.baseproject.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.curry.baseproject.widget.GlideCircleTransform;
import com.curry.baseproject.widget.GlideRoundTransform;

public class GlideUtils {

    /**
     * 加载网络圆角图片
     * @param context
     * @param url           图片地址
     * @param errorId       加载错误时显示的图片
     * @param resourceId    未加载出来时占位图片
     * @param corner        角度（dp）
     * @param imageView           加载的ImageView对象
     */
    public static void loadNetCornerImg(Context context, String url, int errorId, int resourceId, int corner, final ImageView imageView) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .placeholder(resourceId)
                .error(errorId)
                .transform(new CenterCrop(context), new GlideRoundTransform(context, corner))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new BitmapImageViewTarget(imageView){
                    @Override
                    protected void setResource(Bitmap resource) {
                        imageView.setImageBitmap(resource);
                    }
                });

    }

    /**
     * 加载网络圆角图片
     * @param context
     * @param url           图片地址
     * @param errorId       加载错误时显示的图片
     * @param resourceId    未加载出来时占位图片
     * @param corner        角度（dp）
     * @param imageView           加载的ImageView对象
     */
    public static void loadNetCornerNoCropImg(Context context, String url, int errorId, int resourceId, int corner, final ImageView imageView) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .placeholder(resourceId)
                .error(errorId)
                .transform(new GlideRoundTransform(context, corner))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new BitmapImageViewTarget(imageView){
                    @Override
                    protected void setResource(Bitmap resource) {
                        imageView.setImageBitmap(resource);
                    }
                });

    }


    /**
     * 加载网络图片
     * @param context
     * @param url           图片地址
     * @param errorId       加载错误时显示的图片
     * @param resourceId    未加载出来时占位图片
     * @param img           加载的ImageView对象
     */
    public static void loadNetNormalImg(Context context, String url, int errorId, int resourceId, ImageView img) {
        Glide.with(context)
                .load(url)
                .placeholder(resourceId)
                .error(errorId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(img);

    }

    /**
     * 加载圆形图片
     * @param context
     * @param url
     * @param errorId
     * @param resourceId
     * @param img
     */
    public static void loadNetCircleImg(Context context, String url, int errorId, int resourceId, ImageView img) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .error(errorId)
                .placeholder(resourceId)
                .transform(new GlideCircleTransform(context))
                .into(img);

    }

}

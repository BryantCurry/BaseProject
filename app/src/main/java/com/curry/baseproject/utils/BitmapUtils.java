package com.curry.baseproject.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;

import com.curry.baseproject.app.appmanager.JLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by Bryant on 2017/4/19.
 */

public final class BitmapUtils {

    private static final float BITMAP_HEIGHT = 800f;
    private static final float BITMAP_WIDTH = 480f;
    private static final int QUALITY_INITIAL = 100;
    private static final int COMPRESS_SIZE_LIMIT = 100;
    private static final int KILO = 1024;
    private static final int COMPRESS_STEP = 30;
    private static final int REQUEST_CODE_CROP = 3;
    private static final int BMP_TO_STR_QUALITY = 40;
    public static final int PICTURE_COMPRESS_LIMIT = 150;
    public static final int MIN_QUALITY = 10;
    public static final int ROTATE_90 = 90;
    public static final int ROTATE_180 = 180;
    public static final int ROTATE_270 = 270;

    private BitmapUtils() {
    }

    /**
     * 通过uri获取图片并进行压缩
     */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri)
            throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true; // optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888; // optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1)) {
            return null;
        }
        //图片分辨率以480x800为标准
        float hh = BITMAP_HEIGHT; //这里设置高度为800f
        float ww = BITMAP_WIDTH; //这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1; //be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) { //如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) { //如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be; //设置缩放比例
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888; //optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap); //再进行质量压缩
    }

    /**
     * 质量压缩方法
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, QUALITY_INITIAL,
                baos); //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = QUALITY_INITIAL;
        while (baos.toByteArray().length / KILO
                > COMPRESS_SIZE_LIMIT) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); //重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options,
                    baos); //这里压缩options%，把压缩后的数据存放到baos中
            options -= COMPRESS_STEP; //每次都减少30
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(
                baos.toByteArray()); //把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null); //把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static void onPhoto(Activity activity, Uri uri, int outputX, int outputY) {
        Intent intent = null;

        intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", true);
        intent.putExtra("circleCrop", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        activity.startActivityForResult(intent, REQUEST_CODE_CROP);
    }


    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                            int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            //计算图片高度和我们需要高度的最接近比例值
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            //宽度比例值
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            //取比例值中的较大值作为inSampleSize
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, (int) BITMAP_WIDTH,
                (int) BITMAP_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    //把bitmap转换成String
    public static String bitmapToString(String filePath) {

        Bitmap bm = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, BMP_TO_STR_QUALITY, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * 存储图片
     *
     * @param bitmap 位图
     */
    public static String cutPictureQuality(Bitmap bitmap) {

        String fileName = UUID.randomUUID().toString().replace("-", "") + ".png";
        String filePath = Environment.getExternalStorageDirectory() + File.separator
                + System.currentTimeMillis();

        // 判断文件夹存在
        File file = new File(filePath);
        if (file != null && !file.exists()) {
            file.mkdirs();
        }

        try {

            // 第一次压缩
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY_INITIAL, baos);

            FileOutputStream fos = new FileOutputStream(new File(filePath, fileName));

            int options = QUALITY_INITIAL;
            // 如果大于150kb则再次压缩,最多压缩三次
            while (baos.toByteArray().length / KILO > PICTURE_COMPRESS_LIMIT
                    && options != MIN_QUALITY) {
                // 清空baos
                baos.reset();
                // 这里压缩options%，把压缩后的数据存放到baos中
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
                options -= COMPRESS_STEP;
            }
            fos.write(baos.toByteArray());
            fos.close();
            baos.close();

        } catch (Exception e) {
        }
        return filePath + File.separator + fileName;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        JLog.i("======================path: " + path);
        if (TextUtils.isEmpty(path)) {
            return 0;
        }
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = ROTATE_90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = ROTATE_180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = ROTATE_270;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }
}

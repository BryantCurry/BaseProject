package com.curry.baseproject.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;

import com.curry.baseproject.R;

/**
 * Created by Bryant on 2017/4/18.
 */

public class CountDownTimerUtils extends CountDownTimer {
    public static final int MILLISECOND = 1000;
    public static final int START = 0;
    public static final int END = 2;
    private Button mButton;
    private boolean mIsLogin = true;

    /**
     * @param mButton          mButton
     *
     *
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receiver
     *                          {@link #onTick(long)} callbacks.
     */
    public CountDownTimerUtils(Button mButton, long millisInFuture, long countDownInterval, boolean isLogin) {
        super(millisInFuture, countDownInterval);
        this.mButton = mButton;
        this.mIsLogin = isLogin;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (mButton == null) {
            return;
        }
        mButton.setClickable(false); //设置不可点击
//        mButton.setText(millisUntilFinished / 1000 + "秒后可重新发送");  //设置倒计时时间
        mButton.setText(millisUntilFinished / MILLISECOND + "s");  //设置倒计时时间
        if (mIsLogin) {
            mButton.setBackgroundResource(R.drawable.sms_gray_corner_shape); //设置按钮为灰色，这时是不能点击的
        } else {
            mButton.setBackgroundResource(R.drawable.sms_gray_right_corner_shape);
        }

        /**
         * 超链接 URLSpan
         * 文字背景颜色 BackgroundColorSpan
         * 文字颜色 ForegroundColorSpan
         * 字体大小 AbsoluteSizeSpan
         * 粗体、斜体 StyleSpan
         * 删除线 StrikethroughSpan
         * 下划线 UnderlineSpan
         * 图片 ImageSpan
         * http://blog.csdn.net/ah200614435/article/details/7914459
         */
        SpannableString spannableString = new SpannableString(mButton.getText().toString());  //获取按钮上的文字
        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
        /**
         * public void setSpan(Object what, int start, int end, int flags) {
         * 主要是start跟end，start是起始位置,无论中英文，都算一个。
         * 从0开始计算起。end是结束位置，所以处理的文字，包含开始位置，但不包含结束位置。
         */
        spannableString.setSpan(span, START, END, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);   //将倒计时的时间设置为红色
        mButton.setText(spannableString);
    }

    @Override
    public void onFinish() {
        if (mButton != null) {
//            mButton.setText("重新获取验证码");
            mButton.setText("重新获取");
            mButton.setClickable(true);   //重新获得点击
        }
        if (mIsLogin) {
            mButton.setBackgroundResource(R.drawable.blue_solid_corner_4dp_shape);  //还原背景色
        } else {
            mButton.setBackgroundResource(R.drawable.blue_solid_right_corner_2dp_shape);
        }
    }
}

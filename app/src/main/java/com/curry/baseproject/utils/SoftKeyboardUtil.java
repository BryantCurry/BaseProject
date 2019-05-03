package com.curry.baseproject.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.List;

public class SoftKeyboardUtil {

    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    public static void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (null != inputMethodManager) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 隐藏软键盘(可用于Activity，Fragment)
     * viewList 中需要放的是当前界面所有触发软键盘弹出的控件。
     * 比如一个登陆界面， 有一个账号输入框和一个密码输入框，
     * 需要隐藏键盘的时候， 就将两个输入框对象放在 viewList 中，
     * 作为参数传到 hideSoftKeyboard 方法中即可
     */
    public static void hideSoftKeyboard(Context context, List<View> viewList) {
        if (viewList == null) return;

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (null != inputMethodManager) {
            for (View v : viewList) {
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 切换软键盘的弹出和隐藏
     */
    public static void popupInputMethodWindow(final Activity activity) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManage = (InputMethodManager) activity.getSystemService(Service.INPUT_METHOD_SERVICE);
                if (null != inputMethodManage) {
                    inputMethodManage.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }, 0);
    }

    /**
     * 弹出软键盘
     * @param context
     * @param editText
     */
    public static void showKeyboard(final Context context, final EditText editText) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManage = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
                if (null != inputMethodManage) {
                    editText.requestFocus();
                    inputMethodManage.showSoftInput(editText, 0);
                }
            }
        }, 500);

    }

    /**
     * 隐藏软键盘
     * @param context
     * @param v
     * @return
     */
    public static Boolean hideInputMethod(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return false;
    }

    /**
     * 判断软键盘是否打开
     * @param v
     * @param event
     * @return
     */
    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            v.getLocationInWindow(leftTop);
            int left = leftTop[0], top = leftTop[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}

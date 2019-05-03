package com.curry.baseproject.app.base.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.curry.baseproject.app.appmanager.ActivityManagerUtil;
import com.curry.baseproject.app.appmanager.JLog;
import com.curry.baseproject.app.base.iview.IBaseView;
import com.curry.baseproject.app.base.presenter.BasePresenter;
import com.curry.baseproject.utils.SoftKeyboardUtil;
import com.curry.baseproject.widget.LoadingDialog;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.ActivityLifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;

import butterknife.ButterKnife;
import rx.Observable;
import rx.subjects.BehaviorSubject;


public abstract class BaseActivity<V extends IBaseView, P extends BasePresenter<V>>
        extends AppCompatActivity implements ActivityLifecycleProvider, IBaseView {
    public ActivityManagerUtil activityManagerUtil;
    public Activity mActivity;
    protected LoadingDialog mLoadingDialog;
    protected LoadingDialog.Builder mBuilder;
    private V mView;
    private P mPresenter;

    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        activityManagerUtil = ActivityManagerUtil.getInstance();
        activityManagerUtil.pushOneActivity(this);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        setContentView(setLayoutResourceId());
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        createViewAndPresenter();

        mBuilder = new LoadingDialog.Builder(this)
                .setMessage("加载中...")
                .setCancelable(false);
        mLoadingDialog = mBuilder.create();
    }

    private void createViewAndPresenter() {
        if (this.mPresenter == null) {
            this.mPresenter = createPresenter();
        }

        if (this.mView == null) {
            this.mView = createView();
        }

        if (this.mPresenter != null && this.mView != null) {
            mPresenter.attachView(this.mView);
        }
    }

    public abstract V createView();
    public abstract P createPresenter();

    public P getPresenter() {
        return this.mPresenter;
    }

    @LayoutRes
    protected abstract int setLayoutResourceId();

    public void setToolBar(Toolbar toolBar, TextView toolbarTitle, int titleId) {
        toolBar.setTitle("");
        setSupportActionBar(toolBar);
        toolbarTitle.setText(titleId);
    }

    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycle.bindActivity(lifecycleSubject);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            JLog.d("   现在是横屏");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            JLog.d("   现在是竖屏");
        }
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    @CallSuper
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        SoftKeyboardUtil.hideSoftKeyboard(this);
        if (this.mPresenter != null && this.mView != null) {
            this.mPresenter.detachView();        }
        //结束Activity&从栈中移除该Activity
        activityManagerUtil.popOneActivity(this);
    }

    @Override
    public void showLoading() {
        if (mBuilder == null || mLoadingDialog == null) {
            mBuilder = new LoadingDialog.Builder(this)
                    .setMessage("加载中...")
                    .setCancelable(false);
            mLoadingDialog = mBuilder.create();
        }
        mLoadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void showException(int code, String msg) {
        hideLoading();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (SoftKeyboardUtil.isShouldHideInput(v, ev)) {
                if(SoftKeyboardUtil.hideInputMethod(this, v)) {
                    return true; //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

}

package com.curry.baseproject.app.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.curry.baseproject.app.appmanager.JLog;
import com.curry.baseproject.app.base.iview.IBaseView;
import com.curry.baseproject.app.base.presenter.BasePresenter;
import com.curry.baseproject.widget.LoadingDialog;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.FragmentLifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;

import butterknife.ButterKnife;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by LH-Android-00 on 2017/5/17.
 */

public abstract class BaseFragment<V extends IBaseView, P extends BasePresenter<V>> extends Fragment implements FragmentLifecycleProvider, IBaseView {
    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();
    protected View mRootView;
    protected boolean isInit = false;
    protected boolean isLoad = false;
    protected Context mContext;
    protected Activity mActivity;
    protected LoadingDialog mLoadingDialog;
    protected LoadingDialog.Builder mBuilder;
    private V mView;
    private P mPresenter;

    @LayoutRes
    protected abstract int getLayoutId();

    @NonNull
    @Override
    public Observable<FragmentEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @NonNull
    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @NonNull
    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycle.bindFragment(lifecycleSubject);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
        onAttachToContext(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
        onAttachToContext(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
        mRootView = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, mRootView);
        isInit = true;
        prepareLoadData();
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createViewAndPresenter();
        if (mBuilder == null || mLoadingDialog == null) {
            mBuilder = new LoadingDialog.Builder(mContext)
                    .setMessage("加载中...")
                    .setCancelable(false);
            mLoadingDialog = mBuilder.create();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        prepareLoadData();
    }

    private void prepareLoadData() {
        if (!isInit) {
            return;
        }

        if (getUserVisibleHint()) {
            lazyLoad();
            isLoad = true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(FragmentEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(FragmentEvent.RESUME);

    }

    @Override
    public void onPause() {
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        lifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
        ButterKnife.unbind(this);
        isInit = false;
        if (this.mPresenter != null && this.mView != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void onDestroy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        lifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
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

    public P getPresenter() {
        return this.mPresenter;
    }

    public abstract P createPresenter();

    public abstract V createView();

    @Override
    public void showException(int code, String msg) {
        if (code > 0) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        } else {
            //            toke error: code = -3 or -2
            JLog.e("errCode = " + code + ", msg = " + msg);
        }
    }

    protected void onAttachToContext(Context context) {
        mContext = context;
    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract void lazyLoad();

    @Override
    public void showLoading() {
        if (mBuilder == null || mLoadingDialog == null) {
            mBuilder = new LoadingDialog.Builder(mContext)
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

}

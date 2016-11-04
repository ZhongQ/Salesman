package com.salesman.presenter;

import com.salesman.listener.RequestCallBack;
import com.salesman.model.DataModel;
import com.salesman.presenter.base.BaseListPresenter;
import com.salesman.view.OnCommonListener;

/**
 * Persenter
 * Created by LiHuai on 2016/8/30 0030.
 */
public class RequestDataPresenter implements BaseListPresenter {
    private DataModel dataModel;
    private OnCommonListener onCommonListener;

    private boolean mIsRequesting = false;
    private boolean mEnableLoadMore = true;

    public RequestDataPresenter(OnCommonListener onCommonListener) {
        this.onCommonListener = onCommonListener;
        dataModel = new DataModel();
    }

    @Override
    public void refreshData() {
        mEnableLoadMore = false;
        getData();
    }

    @Override
    public void loadMore() {
        if (mEnableLoadMore) {
            getData();
        } else {
            onCommonListener.hideLoading();
        }
    }

    public void setmEnableLoadMore(boolean mEnableLoadMore) {
        this.mEnableLoadMore = mEnableLoadMore;
    }

    public void getData() {
        if (mIsRequesting) {
            return;
        }
        mIsRequesting = true;
        onCommonListener.showLoading();
        dataModel.onGetData(onCommonListener.getRequestContext(), onCommonListener.getRequestUrl(), onCommonListener.getRequestParam(), new RequestCallBack() {
            @Override
            public void onSuccess(String response) {
                mIsRequesting = false;
                onCommonListener.hideLoading();
                onCommonListener.requestSuccess(response);
            }

            @Override
            public void onError() {
                mIsRequesting = false;
                onCommonListener.hideLoading();
                onCommonListener.requestFail();
            }
        });
    }
}

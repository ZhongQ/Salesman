package com.salesman.view;

import android.content.Context;

import java.util.Map;

/**
 * View层回调接口
 * Created by LiHuai on 2016/8/30 0030.
 */
public interface OnCommonListener {

    Context getRequestContext();

    String getRequestUrl();

    Map<String, String> getRequestParam();

    void showLoading();

    void hideLoading();

    void requestSuccess(String response);

    void requestFail();
}

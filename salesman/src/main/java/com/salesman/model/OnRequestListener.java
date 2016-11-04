package com.salesman.model;

/**
 * 数据请求回调接口
 * Created by LiHuai on 2016/8/30 0030.
 */
public interface OnRequestListener {
    void onSuccess(String response);

    void onFail();
}

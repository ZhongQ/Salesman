package com.salesman.listener;

/**
 * 数据请求回调接口
 * Created by LiHuai on 2016/9/20 0020.
 */

public interface RequestCallBack {

    void onSuccess(String response);

    void onError();
}

package com.salesman.model;

import android.content.Context;

import com.salesman.listener.RequestCallBack;

import java.util.Map;

/**
 * 数据获取回调接口
 * Created by LiHuai on 2016/8/30 0030.
 */
public interface OnGetDataListener {
    void onGetData(Context context, String url, Map<String, String> map, RequestCallBack requestCallBack);
}

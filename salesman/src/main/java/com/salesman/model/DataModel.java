package com.salesman.model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.listener.RequestCallBack;
import com.salesman.network.BaseHelper;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;

import java.util.Map;

/**
 * 获取数据Model
 * Created by LiHuai on 2016/8/30 0030.
 */
public class DataModel implements OnGetDataListener {
    private final String TAG = DataModel.class.getSimpleName();

    @Override
    public void onGetData(Context context, String url, Map<String, String> map, final RequestCallBack requestCallBack) {
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                requestCallBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestCallBack.onError();
            }
        });
        VolleyController.getInstance(context).addToQueue(post);
    }
}

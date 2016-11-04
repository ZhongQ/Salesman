package com.salesman.utils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.application.SalesManApplication;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.BaseBean;
import com.salesman.network.BaseHelper;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;

import java.util.Map;

/**
 * 日志工具类
 * Created by LiHuai on 2016/8/5 0005.
 */
public class DailyUtil {
    private final String TAG = DailyUtil.class.getSimpleName();
    private OnDeleteListener mListener;

    public void delLogComment(String commentId) {
        String url = Constant.moduleDelLogComment;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("commentId", commentId);
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean && baseBean.success) {
                    if (null != mListener) {
                        mListener.onDeleteSuccess();
                    }
                } else {
                    if (null != mListener) {
                        mListener.onDeleteFail();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (null != mListener) {
                    mListener.onDeleteFail();
                }
            }
        });
        VolleyController.getInstance(SalesManApplication.getInstance()).addToQueue(post);
    }

    public void setmListener(OnDeleteListener mListener) {
        this.mListener = mListener;
    }

    public interface OnDeleteListener {
        void onDeleteSuccess();

        void onDeleteFail();
    }
}

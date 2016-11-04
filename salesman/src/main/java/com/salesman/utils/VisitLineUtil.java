package com.salesman.utils;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.application.SalesManApplication;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.LinesListBean;
import com.salesman.entity.SingleSelectionBean;
import com.salesman.network.BaseHelper;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 拜访线路工具类
 * Created by LiHuai on 2016/8/9 0009.
 */
public class VisitLineUtil {
    private final String TAG = VisitLineUtil.class.getSimpleName();
    private VisitLineCallBack mListener;

    public interface VisitLineCallBack {
        void onSuccess(ArrayList<SingleSelectionBean> data);

        void onError();
    }

    /**
     * 获取拜访线路
     *
     * @param salesmanId
     */
    public void getVisitLineData(String salesmanId) {
        String url = Constant.moduleSalesLinesList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("salesmanId", salesmanId);
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                LinesListBean bean = GsonUtils.json2Bean(response, LinesListBean.class);
                if (null != bean && bean.success) {
                    if (null != mListener) {
                        mListener.onSuccess(changeToSingleBean(bean.data.list));
                    }
                } else {
                    if (null != mListener) {
                        mListener.onError();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (null != mListener) {
                    mListener.onError();
                }
            }
        });
        VolleyController.getInstance(SalesManApplication.getInstance()).addToQueue(post);
    }

    public static ArrayList<SingleSelectionBean> changeToSingleBean(List<LinesListBean.LineBean> list) {
        ArrayList<SingleSelectionBean> data = new ArrayList<>();
        for (LinesListBean.LineBean lineBean : list) {
            data.add(new SingleSelectionBean(lineBean.lineId, lineBean.lineName, String.valueOf(lineBean.shopTotal)));
        }
        return data;
    }

    public static void setSelectItem(ArrayList<SingleSelectionBean> data, String lineId) {
        for (SingleSelectionBean bean : data) {
            if (TextUtils.isEmpty(lineId)) {
                if ("ALL".equals(bean.id)) {
                    bean.setIsSelect(true);
                } else {
                    bean.setIsSelect(false);
                }
            } else {
                if (lineId.equals(bean.id)) {
                    bean.setIsSelect(true);
                } else {
                    bean.setIsSelect(false);
                }
            }
        }
    }

    public void setVisitLineListener(VisitLineCallBack mListener) {
        this.mListener = mListener;
    }
}

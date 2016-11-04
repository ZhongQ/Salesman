package com.salesman.utils;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.application.SalesManApplication;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.BaseBean;
import com.salesman.entity.SingleSelectionBean;
import com.salesman.entity.ZhanJiFilterListBean;
import com.salesman.network.BaseHelper;
import com.salesman.views.popupwindow.FilterItem;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 战绩筛选条件工具类
 * Created by LiHuai on 2016/7/8 0008.
 */
public class ZhanJiFilterUtil {
    private static final String TAG = ZhanJiFilterUtil.class.getSimpleName();
    private OnGetZhanJiFilterListener mListener;

    /**
     * 获取客户类型数据
     */
    public void getZhanJiFilterData() {
        String url = Constant.moduleZhanJiFilter;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("deptId", SalesManApplication.g_GlobalObject.getmUserInfo().getDeptId());
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean && baseBean.success) {
                    UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
                    mUserConfig.saveZhanJiFilter(response).apply();
                    if (null != mListener) {
                        mListener.onGetZhanJiFilterSuccess();
                    }
                } else {
                    if (null != mListener) {
                        mListener.onGetZhanJiFilterFail();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (null != mListener) {
                    mListener.onGetZhanJiFilterFail();
                }
            }
        });
        VolleyController.getInstance(SalesManApplication.getInstance()).addToQueue(post);
    }

    /**
     * 获取战绩第一级筛选
     *
     * @return
     */
    public static List<ZhanJiFilterListBean.ZhanJiFilterBean> getZhanJiFilterList() {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        ZhanJiFilterListBean zhanJiFilterListBean = GsonUtils.json2Bean(mUserConfig.getZhanJiFilter(), ZhanJiFilterListBean.class);
        List<ZhanJiFilterListBean.ZhanJiFilterBean> data = new ArrayList<>();
        if (null != zhanJiFilterListBean && null != zhanJiFilterListBean.list) {
            List<ZhanJiFilterListBean.ZhanJiFilterBean> list = zhanJiFilterListBean.list;
            if (!list.isEmpty()) {
                for (ZhanJiFilterListBean.ZhanJiFilterBean filterBean : list) {
                    if ("1".equals(filterBean.level)) {
                        data.add(filterBean);
                    }
                }
            }
        }
        return data;
    }

    /**
     * 获取战绩第二级筛选
     *
     * @return
     */
    public static ArrayList<SingleSelectionBean> getSecondZhanJiFilterList() {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        ZhanJiFilterListBean zhanJiFilterListBean = GsonUtils.json2Bean(mUserConfig.getZhanJiFilter(), ZhanJiFilterListBean.class);
        ArrayList<SingleSelectionBean> data = new ArrayList<>();
        if (null != zhanJiFilterListBean && null != zhanJiFilterListBean.list) {
            List<ZhanJiFilterListBean.ZhanJiFilterBean> list = zhanJiFilterListBean.list;
            if (!list.isEmpty()) {
                for (ZhanJiFilterListBean.ZhanJiFilterBean filterBean : list) {
                    if ("2".equals(filterBean.level)) {
                        data.add(new SingleSelectionBean(filterBean.deptId, filterBean.areaId, filterBean.salesmanId, filterBean.deptName));
                    }
                }
            }
        }
        return data;
    }

    /**
     * 获取趋势筛选数据
     *
     * @return
     */
    public static List<FilterItem> getFilterList() {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        ZhanJiFilterListBean zhanJiFilterListBean = GsonUtils.json2Bean(mUserConfig.getZhanJiFilter(), ZhanJiFilterListBean.class);
        ArrayList<FilterItem> data = new ArrayList<>();
        if (null != zhanJiFilterListBean && null != zhanJiFilterListBean.list) {
            List<ZhanJiFilterListBean.ZhanJiFilterBean> list = zhanJiFilterListBean.list;
            if (!list.isEmpty()) {
                for (ZhanJiFilterListBean.ZhanJiFilterBean filterBean : list) {
                    data.add(new FilterItem(filterBean.deptId, filterBean.deptName, filterBean.salesmanId, filterBean.userType));
                }
                data.get(0).setSelect(true);// 默认第一个选中
            }
        }
        return data;
    }

    /**
     * 是否二次请求数据
     *
     * @return
     */
    public static boolean isSecondRequest() {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        String zhanJiFilter = mUserConfig.getZhanJiFilter();
        if (TextUtils.isEmpty(zhanJiFilter)) {
            return true;
        }
        return false;
    }

    public void setOnGetZhanJiFilterListener(OnGetZhanJiFilterListener mListener) {
        this.mListener = mListener;
    }

    public interface OnGetZhanJiFilterListener {
        void onGetZhanJiFilterSuccess();

        void onGetZhanJiFilterFail();
    }
}

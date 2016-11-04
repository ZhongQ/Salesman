package com.salesman.utils;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.application.SalesManApplication;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.BaseBean;
import com.salesman.entity.ClientTypeListBean;
import com.salesman.entity.ShopTypeBean;
import com.salesman.network.BaseHelper;
import com.salesman.views.popupwindow.FilterItem;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 客户(商铺)类型工具类
 * Created by LiHuai on 2016/6/27 0027.
 */
public class ClientTypeUtil {
    private static final String TAG = ClientTypeUtil.class.getSimpleName();
    private GetClientTypeListener mListener;

    public ClientTypeUtil() {}

    /**
     * 获取客户类型数据
     */
    public void getClientTypeData() {
        String url = Constant.moduleClientType;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean && baseBean.success) {
                    UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
                    mUserConfig.saveClientType(response).apply();
                    if (null != mListener) {
                        mListener.onGetClientTypeSuccess();
                    }
                } else {
                    if (null != mListener) {
                        mListener.onGetClientTypeFail();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (null != mListener) {
                    mListener.onGetClientTypeFail();
                }
            }
        });
        VolleyController.getInstance(SalesManApplication.getInstance()).addToQueue(post);
    }

    /**
     * 获取客户类型筛选条件
     *
     * @return
     */
    public static List<FilterItem> getTypeFilterList() {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        ClientTypeListBean clientTypeListBean = GsonUtils.json2Bean(mUserConfig.getClientType(), ClientTypeListBean.class);
        List<FilterItem> list = new ArrayList<>();
        if (null != clientTypeListBean && null != clientTypeListBean.data && null != clientTypeListBean.data.list) {
            List<ShopTypeBean> shopTypeBeans = clientTypeListBean.data.list;
            for (ShopTypeBean shopTypeBean : shopTypeBeans) {
                list.add(new FilterItem(shopTypeBean.value, shopTypeBean.label));
            }
        }
        return list;
    }

    public static List<ShopTypeBean> getShopTypeList() {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        ClientTypeListBean clientTypeListBean = GsonUtils.json2Bean(mUserConfig.getClientType(), ClientTypeListBean.class);
        List<ShopTypeBean> list = new ArrayList<>();
        if (null != clientTypeListBean && null != clientTypeListBean.data && null != clientTypeListBean.data.list) {
            list = clientTypeListBean.data.list;
            if (!list.isEmpty()){
                list.remove(0);// 删除第一条数据（全部类型）
            }
        }
        return list;
    }

    /**
     * 是否二次请求数据
     *
     * @return
     */
    public static boolean isSecondRequest() {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        String typeJsonStr = mUserConfig.getClientType();
        if (TextUtils.isEmpty(typeJsonStr)) {
            return true;
        }
        return false;
    }

    public void setClientTypeListener(GetClientTypeListener mListener) {
        this.mListener = mListener;
    }

    public interface GetClientTypeListener {
        void onGetClientTypeSuccess();

        void onGetClientTypeFail();
    }
}

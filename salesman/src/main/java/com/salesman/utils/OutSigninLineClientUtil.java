package com.salesman.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.application.SalesManApplication;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.BaseBean;
import com.salesman.entity.OutLineClientListBean;
import com.salesman.entity.SingleSelectionBean;
import com.salesman.listener.OnCommonPostListener;
import com.salesman.network.BaseHelper;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 外勤签到线路和客户工具类
 * Created by LiHuai on 2016/8/10 0010.
 */
public class OutSigninLineClientUtil {
    private final String TAG = OutSigninLineClientUtil.class.getSimpleName();
    private OnCommonPostListener mListener;

    /**
     * 获取外勤签到线路与客户
     */
    public void getOutLineAndClientData() {
        final UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        mUserConfig.saveOutLineClient("").apply();// 清空
        String url = Constant.moduleOutLineAndClient;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean && baseBean.success) {
                    mUserConfig.saveOutLineClient(response).apply();
                    if (null != mListener) {
                        mListener.onSuccessListener();
                    }
                } else {
                    if (null != mListener) {
                        mListener.onFailListener();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (null != mListener) {
                    mListener.onFailListener();
                }
            }
        });
        VolleyController.getInstance(SalesManApplication.getInstance()).addToQueue(post);
    }

    /**
     * 获取外勤全部线路
     *
     * @return
     */
    public static ArrayList<SingleSelectionBean> getAllOutLineSingSelectionData() {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        OutLineClientListBean linesClientBean = GsonUtils.json2Bean(mUserConfig.getOutLineClient(), OutLineClientListBean.class);
        ArrayList<SingleSelectionBean> list = new ArrayList<>();
        if (null != linesClientBean && null != linesClientBean.data && null != linesClientBean.data.list) {
            List<OutLineClientListBean.LineBean> linesBean = linesClientBean.data.list;
            for (OutLineClientListBean.LineBean lineBean : linesBean) {
                list.add(new SingleSelectionBean(lineBean.lineId, lineBean.lineName));
            }
        }
        return list;
    }

    /**
     * 根据线路id获取相对应的客户
     *
     * @param lineId
     * @return
     */
    public static ArrayList<SingleSelectionBean> getClientByLineId(@NonNull String lineId) {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        OutLineClientListBean linesClientBean = GsonUtils.json2Bean(mUserConfig.getOutLineClient(), OutLineClientListBean.class);
        ArrayList<SingleSelectionBean> list = new ArrayList<>();
        if (null != linesClientBean && null != linesClientBean.data && null != linesClientBean.data.list) {
            List<OutLineClientListBean.LineBean> linesBean = linesClientBean.data.list;
            for (OutLineClientListBean.LineBean lineBean : linesBean) {
                if (!TextUtils.isEmpty(lineId) && lineId.equals(lineBean.lineId)) {
                    List<OutLineClientListBean.ShopBean> shopBeanList = lineBean.shopList;
                    if (null != shopBeanList) {
                        for (OutLineClientListBean.ShopBean shopBean : shopBeanList) {
                            list.add(new SingleSelectionBean(shopBean.shopNo, shopBean.shopName));
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * 是否存在客户
     *
     * @return
     */
    public static boolean isHaveClient() {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        OutLineClientListBean linesClientBean = GsonUtils.json2Bean(mUserConfig.getOutLineClient(), OutLineClientListBean.class);
        if (null != linesClientBean && null != linesClientBean.data && null != linesClientBean.data.list) {
            List<OutLineClientListBean.LineBean> linesBean = linesClientBean.data.list;
            for (OutLineClientListBean.LineBean lineBean : linesBean) {
                if (!lineBean.shopList.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否仅有一条线路
     *
     * @return
     */
    public static boolean isOnlyLine() {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        OutLineClientListBean linesClientBean = GsonUtils.json2Bean(mUserConfig.getOutLineClient(), OutLineClientListBean.class);
        if (null != linesClientBean && null != linesClientBean.data && null != linesClientBean.data.list) {
            if (linesClientBean.data.list.size() == 1) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSecondRequest() {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        String LCJsonStr = mUserConfig.getOutLineClient();
        if (TextUtils.isEmpty(LCJsonStr)) {
            return true;
        }
        return false;
    }

    public void setOnCommonPostListener(OnCommonPostListener mListener) {
        this.mListener = mListener;
    }
}

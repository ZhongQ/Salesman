package com.salesman.utils;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.application.SalesManApplication;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.BaseBean;
import com.salesman.entity.SalesmanLineListBean;
import com.salesman.entity.SingleSelectionBean;
import com.salesman.listener.OnCommonPostListener;
import com.salesman.network.BaseHelper;
import com.salesman.views.popupwindow.FilterItem;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 业务员线路工具类
 * Created by LiHuai on 2016/8/9 0009.
 */
public class SalesmanLineUtil {
    private final String TAG = SalesmanLineUtil.class.getSimpleName();
    private OnCommonPostListener mListener;

    public void getSalesmanAndLineData(boolean bool) {
        final UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        if (bool) {
            // 重新获取，先置空
            mUserConfig.saveSalesmanLine("").apply();
        }
        UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
        String url = Constant.moduleSalesmanLineList;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("deptId", mUserInfo.getDeptId());
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean && baseBean.success) {
                    mUserConfig.saveSalesmanLine(response).apply();
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
     * 获取业务员全部数据
     *
     * @param isAll 是否包含全部业务员
     * @return
     */
    public static List<FilterItem> getSalesmanFilterItemList(boolean isAll) {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        SalesmanLineListBean salesLineBean = GsonUtils.json2Bean(mUserConfig.getSalesmanLine(), SalesmanLineListBean.class);
        List<FilterItem> list = new ArrayList<>();
        if (null != salesLineBean && null != salesLineBean.data && null != salesLineBean.data.list) {
            List<SalesmanLineListBean.SalesBean> salesBeans = salesLineBean.data.list;
            for (SalesmanLineListBean.SalesBean salesBean : salesBeans) {
                FilterItem filterItem = new FilterItem(salesBean.userId, salesBean.userName);
                list.add(filterItem);
            }
        }
        if (!isAll && !list.isEmpty()) {
            list.remove(0);
        }
        return list;
    }

    /**
     * 获取指定业务员下的线路
     *
     * @param salesmanId
     * @return
     */
    public static List<FilterItem> getLineFilterItemList(String salesmanId) {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        SalesmanLineListBean salesLineBean = GsonUtils.json2Bean(mUserConfig.getSalesmanLine(), SalesmanLineListBean.class);
        List<FilterItem> list = new ArrayList<>();
        if (null != salesLineBean && null != salesLineBean.data && null != salesLineBean.data.list) {
            List<SalesmanLineListBean.SalesBean> salesBeans = salesLineBean.data.list;
            if (TextUtils.isEmpty(salesmanId) && !salesLineBean.data.list.isEmpty()) {// 默认全部
                List<SalesmanLineListBean.LineBean> lineBeans = salesLineBean.data.list.get(0).lineList;
                for (SalesmanLineListBean.LineBean lineBean : lineBeans) {
                    FilterItem filterItem = new FilterItem(lineBean.lineId, lineBean.lineName);
                    list.add(filterItem);
                }
            } else {
                for (SalesmanLineListBean.SalesBean salesBean : salesBeans) {
                    if (salesmanId.equals(salesBean.userId) && null != salesBean.lineList) {
                        List<SalesmanLineListBean.LineBean> lineBeans = salesBean.lineList;
                        for (SalesmanLineListBean.LineBean lineBean : lineBeans) {
                            FilterItem filterItem = new FilterItem(lineBean.lineId, lineBean.lineName);
                            list.add(filterItem);
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * 获取所有线路
     *
     * @param isAll 是否包含全部路线
     * @return
     */
    public static ArrayList<FilterItem> getAllLineFilterItemList(boolean isAll) {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        SalesmanLineListBean salesLineBean = GsonUtils.json2Bean(mUserConfig.getSalesmanLine(), SalesmanLineListBean.class);
        ArrayList<FilterItem> list = new ArrayList<>();
        if (null != salesLineBean && null != salesLineBean.data && null != salesLineBean.data.list) {
            List<SalesmanLineListBean.SalesBean> salesBeans = salesLineBean.data.list;
            for (SalesmanLineListBean.SalesBean salesBean : salesBeans) {
                if (null != salesBean.lineList) {
                    List<SalesmanLineListBean.LineBean> lineBeans = salesBean.lineList;
                    for (SalesmanLineListBean.LineBean lineBean : lineBeans) {
                        FilterItem bean = new FilterItem(lineBean.lineId, lineBean.lineName);
                        list.add(bean);
                    }
                }
            }
        }
        if (!isAll && !list.isEmpty()) {
            list.remove(0);
        }
        return list;
    }

    /**
     * 获取所有业务员,不包含全部业务员
     *
     * @return
     */
    public static ArrayList<SingleSelectionBean> getAllLineSingSelectionData() {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        SalesmanLineListBean salesLineBean = GsonUtils.json2Bean(mUserConfig.getSalesmanLine(), SalesmanLineListBean.class);
        ArrayList<SingleSelectionBean> list = new ArrayList<>();
        if (null != salesLineBean && null != salesLineBean.data && null != salesLineBean.data.list) {
            List<SalesmanLineListBean.SalesBean> salesBeans = salesLineBean.data.list;
            for (SalesmanLineListBean.SalesBean salesBean : salesBeans) {
                SingleSelectionBean bean = new SingleSelectionBean(salesBean.userId, salesBean.userName);
                list.add(bean);
            }
        }
        if (!list.isEmpty()) {
            list.remove(0);
        }
        return list;
    }

    public static boolean isSecondRequest() {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        String sLJsonStr = mUserConfig.getSalesmanLine();
        if (TextUtils.isEmpty(sLJsonStr)) {
            return true;
        }
        return false;
    }

    public void setOnCommonPostListener(OnCommonPostListener mListener) {
        this.mListener = mListener;
    }
}

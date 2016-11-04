package com.salesman.utils;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.application.SalesManApplication;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.BaseBean;
import com.salesman.entity.DingGeListBean;
import com.salesman.entity.SingleSelectionBean;
import com.salesman.network.BaseHelper;
import com.salesman.views.popupwindow.FilterItem;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 定格线路工具类
 * Created by LiHuai on 2016/6/24 0024.
 */
public class DingGeLineUtil {
    private static final String TAG = DingGeLineUtil.class.getSimpleName();
    private GetClientDingGeLineListener mListener;

    /**
     * 获取定格和线路数据
     *
     * @param bool
     */
    public void getDingGeAndLineData(boolean bool) {
        final UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        if (bool) {
            // 重新获取，先置空
            mUserConfig.saveDingGeLine("").apply();
        }
        String url = Constant.moduleDingGeLine;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean && baseBean.success) {
                    mUserConfig.saveDingGeLine(response).apply();
                    if (null != mListener) {
                        mListener.onGetClientDingGeLineSuccess();
                    }
                } else {
                    if (null != mListener) {
                        mListener.onGetClientDingGeLineFail();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (null != mListener) {
                    mListener.onGetClientDingGeLineFail();
                }
            }
        });
        VolleyController.getInstance(SalesManApplication.getInstance()).addToQueue(post);
    }

    /**
     * 获取定格全部数据
     *
     * @return
     */
    public static List<FilterItem> getDingGeFilterItemList() {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        DingGeListBean dingGeListBean = GsonUtils.json2Bean(mUserConfig.getDingGeLine(), DingGeListBean.class);
        List<FilterItem> list = new ArrayList<>();
        if (null != dingGeListBean && null != dingGeListBean.data) {
            List<DingGeListBean.DingGeBean> dingGeBeans = dingGeListBean.data.spgList;
            for (DingGeListBean.DingGeBean dingGeBean : dingGeBeans) {
                FilterItem filterItem = new FilterItem(dingGeBean.spGroupId, dingGeBean.spGroupName);
                list.add(filterItem);
            }
        }
        return list;
    }

    /**
     * 获取指定定格下的线路
     *
     * @param dingGeId
     * @return
     */
    public static List<FilterItem> getLineFilterItemList(String dingGeId) {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        DingGeListBean dingGeListBean = GsonUtils.json2Bean(mUserConfig.getDingGeLine(), DingGeListBean.class);
        List<FilterItem> list = new ArrayList<>();
        if (null != dingGeListBean && null != dingGeListBean.data && null != dingGeListBean.data.spgList) {
            List<DingGeListBean.DingGeBean> dingGeBeans = dingGeListBean.data.spgList;
            if (TextUtils.isEmpty(dingGeId) && !dingGeListBean.data.spgList.isEmpty()) {// 默认全部
                List<DingGeListBean.LineBean> lineBeans = dingGeListBean.data.spgList.get(0).lineList;
                for (DingGeListBean.LineBean lineBean : lineBeans) {
                    FilterItem filterItem = new FilterItem(lineBean.lineId, lineBean.lineName);
                    list.add(filterItem);
                }
            } else {
                for (DingGeListBean.DingGeBean dingGeBean : dingGeBeans) {
                    if (dingGeId.equals(dingGeBean.spGroupId) && null != dingGeBean.lineList) {
                        List<DingGeListBean.LineBean> lineBeans = dingGeBean.lineList;
                        for (DingGeListBean.LineBean lineBean : lineBeans) {
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
     * @return
     */
    public static ArrayList<FilterItem> getAllLineFilterItemList() {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        DingGeListBean dingGeListBean = GsonUtils.json2Bean(mUserConfig.getDingGeLine(), DingGeListBean.class);
        ArrayList<FilterItem> list = new ArrayList<>();
        if (null != dingGeListBean && null != dingGeListBean.data && null != dingGeListBean.data.spgList) {
            List<DingGeListBean.DingGeBean> dingGeBeans = dingGeListBean.data.spgList;
            for (DingGeListBean.DingGeBean dingGeBean : dingGeBeans) {
                if (null != dingGeBean.lineList) {
                    List<DingGeListBean.LineBean> lineBeans = dingGeBean.lineList;
                    for (DingGeListBean.LineBean lineBean : lineBeans) {
                        FilterItem bean = new FilterItem(lineBean.lineId, lineBean.lineName);
                        list.add(bean);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 获取所有线路(外勤签到)
     *
     * @return
     */
    public static ArrayList<SingleSelectionBean> getAllLineSingSelectionData() {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        DingGeListBean dingGeListBean = GsonUtils.json2Bean(mUserConfig.getDingGeLine(), DingGeListBean.class);
        ArrayList<SingleSelectionBean> list = new ArrayList<>();
        if (null != dingGeListBean && null != dingGeListBean.data && null != dingGeListBean.data.spgList) {
            List<DingGeListBean.DingGeBean> dingGeBeans = dingGeListBean.data.spgList;
            for (DingGeListBean.DingGeBean dingGeBean : dingGeBeans) {
                if (null != dingGeBean.lineList) {
                    List<DingGeListBean.LineBean> lineBeans = dingGeBean.lineList;
                    for (DingGeListBean.LineBean lineBean : lineBeans) {
                        SingleSelectionBean bean = new SingleSelectionBean(lineBean.lineId, lineBean.lineName);
                        list.add(bean);
                    }
                }
            }
            if (!list.isEmpty()) {
                list.remove(0);
            }
        }
        return list;
    }

    public static boolean isSecondRequest() {
        UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
        String dGLJsonStr = mUserConfig.getDingGeLine();
        if (TextUtils.isEmpty(dGLJsonStr)) {
            return true;
        }
        return false;
    }

    public void setGetClientDingGeLineListener(GetClientDingGeLineListener mListener) {
        this.mListener = mListener;
    }

    public interface GetClientDingGeLineListener {
        void onGetClientDingGeLineSuccess();

        void onGetClientDingGeLineFail();
    }
}

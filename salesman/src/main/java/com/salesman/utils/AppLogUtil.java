package com.salesman.utils;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.application.SalesManApplication;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.db.DBHelper;
import com.salesman.entity.AppLogDBBean;
import com.salesman.entity.BaseBean;
import com.salesman.network.BaseHelper;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * APP日志工具类
 * Created by LiHuai on 2016/5/6 0006.
 */
public class AppLogUtil {
    private static final String TAG = AppLogUtil.class.getSimpleName();
    public static final String state1 = "1"; //"上传成功";
    public static final String state2 = "2"; //"上传失败";
    public static final String state3 = "3"; //"批量上传成功";
    public static final String state4 = "4"; //"批量上传失败";
    public static final String state5 = "5"; //"不上传";
    public static final String alive = "1"; // 活着
    public static final String die = "2"; // 杀死

    public static void addLogToDB(String lng, String lat, String address, String uploadState) {
        DBHelper dbHelper = DBHelper.getInstance(SalesManApplication.getInstance());
        AppLogDBBean appLogDBBean = new AppLogDBBean(lng, lat, address, uploadState);
        dbHelper.insertToAppLogDB(appLogDBBean);
    }

    public static void addLogToDB(String appAlive) {
        DBHelper dbHelper = DBHelper.getInstance(SalesManApplication.getInstance());
        AppLogDBBean appLogDBBean = new AppLogDBBean(appAlive);
        dbHelper.insertToAppLogDB(appLogDBBean);
    }

    public static List<AppLogDBBean> getAppLogList() {
        DBHelper dbHelper = DBHelper.getInstance(SalesManApplication.getInstance());
        List<AppLogDBBean> list = dbHelper.queryAppLogRecord();
        return list;
    }

    public static List<AppLogDBBean> getAppLogList(String date) {
        DBHelper dbHelper = DBHelper.getInstance(SalesManApplication.getInstance());
        List<AppLogDBBean> list = dbHelper.queryAppLogByDate(date);
        return list;
    }

    public static List<String> getAppLogStrList(String date) {
        List<String> strList = new ArrayList<>();
        List<AppLogDBBean> list = getAppLogList(date);
        for (AppLogDBBean appLogDBBean : list) {
            strList.add(appLogDBBean.getLogString(appLogDBBean));
        }
        return strList;
    }

    /**
     * 上传App日志
     */
    public static void uploadAppLog() {
        StringBuffer sb = new StringBuffer();
        final List<AppLogDBBean> list = getAppLogList();
        for (AppLogDBBean bean : list) {
            sb.append(bean.toString());
        }
        if (TextUtils.isEmpty(sb.toString())) {
            return;
        }
        UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
        String url = Constant.moduleUploadAppLog;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("userId", mUserInfo.getUserId());
        map.put("deviceType", String.valueOf(1));
        map.put("dataInfo", sb.toString());
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                BaseBean bean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != bean) {
                    if (bean.success) {
                        LogUtils.d(TAG, "日志批量上传成功");
                        // 上传成功后，删除
                        DBHelper.getInstance(SalesManApplication.getInstance()).deleteLogByTime(list);
                    } else {
                        LogUtils.d(TAG, "日志批量上传失败");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d(TAG, "日志批量上传失败");
            }
        });
        VolleyController.getInstance(SalesManApplication.getInstance()).addToQueue(post);
    }
}

package com.salesman.utils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.application.SalesManApplication;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.db.DBHelper;
import com.salesman.entity.BaseBean;
import com.salesman.entity.TrackDBBean;
import com.salesman.network.BaseHelper;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;

import java.util.List;
import java.util.Map;

/**
 * 足迹上传工具类
 * Created by LiHuai on 2016/3/22 0022.
 */
public class TrackUtil {
    private static final String TAG = TrackUtil.class.getSimpleName();

    /**
     * 将未上传的足迹保存本地
     *
     * @param longitude
     * @param latitude
     * @param address
     * @param time
     */
    public static void addTrackToDB(double longitude, double latitude, String address, long time) {
        TrackDBBean bean = new TrackDBBean(longitude, latitude, address, String.valueOf(time));
        DBHelper dbHelper = DBHelper.getInstance(SalesManApplication.getInstance());
        dbHelper.insertTrackToDB(bean);
    }

//    /**
//     * 将足迹保存在本地
//     *
//     * @param longitude
//     * @param latitude
//     * @param address
//     * @param time
//     */
//    public static void addToTrackRecordDB(double longitude, double latitude, String address, long time) {
//        TrackDBBean bean = new TrackDBBean(longitude, latitude, address, String.valueOf(time));
//        DBHelper dbHelper = DBHelper.getInstance(SalesManApplication.getInstance());
//        dbHelper.insertToTrackRecordDB(bean);
//    }
//
//    /**
//     * 将app日志保存在本地
//     *
//     * @param appAlive
//     * @param uploadState
//     */
//    public static void addToLogDB(String appAlive, String uploadState) {
//        LogDBBean logBean = new LogDBBean(appAlive, uploadState, String.valueOf(System.currentTimeMillis()));
//        DBHelper dbHelper = DBHelper.getInstance(SalesManApplication.getInstance());
//        dbHelper.insertToLogDB(logBean);
//    }

//    public static void logString() {
//        DBHelper dbHelper = DBHelper.getInstance(SalesManApplication.getInstance());
//        List<TrackDBBean> list = dbHelper.queryTrackRecord();
//        LogUtils.d(TAG, "============");
//        for (TrackDBBean bean : list) {
//            LogUtils.d(TAG, bean.toString() + "\n");
//        }
//    }
//
//    public static List<String> getLogList() {
//        List<String> strList = new ArrayList<>();
//        DBHelper dbHelper = DBHelper.getInstance(SalesManApplication.getInstance());
//        List<TrackDBBean> list = dbHelper.queryTrackRecord();
//        for (TrackDBBean bean : list) {
//            strList.add(bean.toString());
//        }
//        return strList;
//    }
//
//    public static List<String> getAppLogList() {
//        List<String> strList = new ArrayList<>();
//        DBHelper dbHelper = DBHelper.getInstance(SalesManApplication.getInstance());
//        List<LogDBBean> list = dbHelper.queryLogRecord();
//        for (LogDBBean bean : list) {
//            strList.add(bean.toString());
//        }
//        return strList;
//    }

    /**
     * 批量上传足迹
     */
    public synchronized static void uploadTrack() {
        List<TrackDBBean> list = DBHelper.getInstance(SalesManApplication.getInstance()).queryTrack();
        if (list.size() > 0) {
            upload(list);
        }
    }

    /**
     * 上传足迹
     */
    public static void upload(final List<TrackDBBean> list) {
        StringBuffer sb = new StringBuffer();
        for (TrackDBBean bean : list) {
            sb.append(bean.toString(bean));
        }
        UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
        String url = Constant.moduleBatchUploadTrack;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("deptId", mUserInfo.getDeptId());
        map.put("deviceType", String.valueOf(1));
        map.put("batchTrackStr", sb.toString());
        map.put("type", String.valueOf(0));//0:百度默认抓取；1：手动抓取
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                BaseBean bean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != bean) {
                    if (bean.success) {
                        LogUtils.d(TAG, "足迹批量上传成功！");
                        AppLogUtil.addLogToDB("0", "0", "", AppLogUtil.state3);
                        DBHelper.getInstance(SalesManApplication.getInstance()).deleteTrackByTime(list);
                    } else {
                        AppLogUtil.addLogToDB("0", "0", "", AppLogUtil.state4);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLogUtil.addLogToDB("0", "0", "", AppLogUtil.state4);
            }
        });
        VolleyController.getInstance(SalesManApplication.getInstance()).addToQueue(post);
    }
}

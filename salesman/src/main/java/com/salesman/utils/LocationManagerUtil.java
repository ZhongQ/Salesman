package com.salesman.utils;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.salesman.application.SalesManApplication;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.TrackUploadResultBean;
import com.salesman.network.BaseHelper;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.utils.NetworkUtils;

import java.util.Map;

/**
 * 百度定位工具类
 * Created by LiHuai on 2016/1/25 0025.
 */
public class LocationManagerUtil {
    public static final String TAG = LocationManagerUtil.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
    private UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
    private final String PRODNAME = "SalesmanLocation";
    private Context context;
    private static LocationManagerUtil mInstance;
    private static LocationClient mLocationClient;
    private static MyLocationListener mLocationListener;
    public static boolean isSave = false;// 保存开启定位时间标识

    //    public static final long UPLOAD_TIME = 1000 * 50;//上传时间（单位ms）
    //    public static final int ALARM_TIME = 1000 * 60;//单位ms（闹钟）
    //    public static final int TIME = 1000 * 60;//单位ms（百度定位循环）
    public static final long THREAD_TIME = 1000 * 10;//单位ms（线程）
    private int location_fail = 1;// 定位失败次数

    public static final long UPLOAD_TIME = SalesManApplication.g_GlobalObject.getmUserInfo().getUploadTrackTime();//上传时间（单位ms）
    public static final long ALARM_TIME = SalesManApplication.g_GlobalObject.getmUserInfo().getUploadTrackTime();//单位ms（闹钟）
    public static final int TIME = Integer.parseInt(String.valueOf(SalesManApplication.g_GlobalObject.getmUserInfo().getUploadTrackTime())) / 5;//单位ms（百度定位循环, 时间为后台上传足迹频率的五分之一）

    private double distanceTmp = 0d;
    private float speedTmp = 0f;

    public LocationManagerUtil(Context context) {
        super();
        this.context = context;
    }

    public static LocationManagerUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (LocationManagerUtil.class) {
                if (mInstance == null) {
                    mInstance = new LocationManagerUtil(context);
                }
            }
        }
        return mInstance;
    }

    public void initLocation(int time) {
        if (null == mLocationClient) {
            mLocationClient = new LocationClient(context);
        }
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式,默认高精度
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(time);// 默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
//        option.disableCache(true);// true表示禁用缓存定位，false表示启用缓存定位
        option.setTimeOut(10 * 1000);
        option.setProdName(PRODNAME);
//        option.setOpenAutoNotifyMode(10000, 5, LocationClientOption.LOC_SENSITIVITY_HIGHT);//分别为频率，距离，敏感程度设置后，SDK会按照“或”的关系，达到标准才会发起定位
        mLocationClient.setLocOption(option);
    }

    public LocationClient initLocation(Context context, int time) {
        LocationClient mClient = new LocationClient(context);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式,默认高精度
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(time);// 默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
//        option.disableCache(true);// true表示禁用缓存定位，false表示启用缓存定位
        option.setTimeOut(10 * 1000);
        option.setProdName(PRODNAME);
//        option.setOpenAutoNotifyMode(10000, 5, LocationClientOption.LOC_SENSITIVITY_HIGHT);//分别为频率，距离，敏感程度设置后，SDK会按照“或”的关系，达到标准才会发起定位
        mClient.setLocOption(option);
        return mClient;
    }

    public void initLocationListener() {
        if (null == mLocationListener) {
            mLocationListener = new MyLocationListener();
        }
    }

    public void startLocationListener() {
        if (null != mLocationClient && null != mLocationListener) {
            if (isSave) {
                isSave = false;
                // 保存开启定位时间
                mUserConfig.saveLocationDate(DateUtils.getYMD(System.currentTimeMillis())).apply();
                LogUtils.d(TAG, "保存开启定位时间");
            }
            LogUtils.e(TAG, "开启定位" + DateUtils.getYMD(System.currentTimeMillis()));
            mLocationClient.registerLocationListener(mLocationListener);
            mLocationClient.start();
        }
    }

    public void startLocationListener(LocationClient locationClient, BDLocationListener bdLocationListener) {
        LogUtils.e(TAG, "开启定位");
        locationClient.registerLocationListener(bdLocationListener);
        locationClient.start();
    }

    public void unRegisterLocationListener() {
        if (null != mLocationClient && null != mLocationListener) {
            LogUtils.d(TAG, "注销定位");
            mLocationClient.unRegisterLocationListener(mLocationListener);
        }
    }

    public void unRegisterLocationListener(BDLocationListener listener) {
        LogUtils.d(TAG, "注销定位");
        mLocationClient.unRegisterLocationListener(listener);
    }

    public void unRegisterLocationListener(LocationClient locationClient, BDLocationListener listener) {
        LogUtils.d(TAG, "注销定位");
        locationClient.unRegisterLocationListener(listener);
    }

    public void stopLocation() {
        if (mLocationClient != null) {
            LogUtils.e(TAG, "停止定位" + DateUtils.getYMD(System.currentTimeMillis()));
            mLocationClient.stop();
        }
    }

    public static LocationClient getmLocationClient() {
        return mLocationClient;
    }

    public static MyLocationListener getmLocationListener() {
        return mLocationListener;
    }

    /**
     * 百度地图定位回调
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // 下班签到后，注销登录，足迹抓取开关关闭，停止定位
            if (mUserConfig.getGetOffWork() || mUserConfig.getHandExit() || !mUserConfig.getTrackSet()) {
                unRegisterLocationListener();
                stopLocation();
                return;
            }
            // 上传离线足迹
            if (NetworkUtils.isNetworkAvailable(context)) {
                TrackUtil.uploadTrack();
            }
            // 是否同一天签到
            if (!DateUtils.isSameDate()) {
                LogUtils.d(TAG, "签到不是同一天");
                mUserConfig.saveGoToWork(false).saveGetOffWork(false).apply();
            }
            // 开启定位时间是否是同一天，不是则停止定位
            if (!DateUtils.isSameLocationDate()) {
                LogUtils.d(TAG, "定位不是同一天");
                unRegisterLocationListener();
                stopLocation();
                isSave = true;
                AlarmUtil.cancelServiceAlarm();
                return;
            }
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            String address = LocationCoordinateUtil.getLocationAddress(location);
            LogUtils.d("123456", String.valueOf(location.getSpeed()));
            LogUtils.d(TAG, LocationCoordinateUtil.getLocationDetail(location));
//            AppLogUtil.addLogToDB(String.valueOf(longitude), String.valueOf(latitude), address, AppLogUtil.state5);// 保存日志
            if (longitude != 4.9E-324 && latitude != 4.9E-324) {
                location_fail = 1;
                if (mUserConfig.getGotoWork() && !mUserConfig.getGetOffWork()) {
                    disposeTrack(longitude, latitude, address, location.getSpeed());
                }
            } else {
                location_fail++;
                if (location_fail > 5) {
                    location_fail = 1;
                    NotificationUtil.showNotification();
                }
                LogUtils.d(TAG, "定位失败");
                AppLogUtil.addLogToDB("0", "0", address, AppLogUtil.state5);// 保存日志
            }
        }
    }


    /**
     * 处理经纬度
     *
     * @param longitude
     * @param latitude
     * @param address
     * @param speed
     */
    private void disposeTrack(final double longitude, final double latitude, final String address, float speed) {
        long past = mUserConfig.getUploadTime();
        final long now = System.currentTimeMillis();
        LogUtils.d(TAG, "定位时间间隔" + (now - past));
        if (now - past < UPLOAD_TIME) {
            return;
        }
        mUserConfig.saveUploadTime(now).apply();// 保存上传时间,即使上传失败也需要记录保存时间，因为坐标点会写入离线足迹表中
        // 此处解决原地经纬度漂移问题，如果速度为0，计算上个点和此时点的距离，如果满足10~25米范围则上传上个点坐标，否则上传此时坐标
        double lng = longitude;
        double lat = latitude;
        double distance = LocationCoordinateUtil.getDistance(longitude, latitude, LocationCoordinateUtil.getLongitude(), LocationCoordinateUtil.getLatitude());
        distanceTmp = distance;
        speedTmp = speed;
//        if (speed <= 0f) {
//            LogUtils.d("1234", String.valueOf(distance));
//            if (distance < 10d || distance > 150d) {// 满足条件使用新坐标，反之使用上个点坐标
//                lng = longitude;
//                lat = latitude;
//            }
//        } else {
//            lng = longitude;
//            lat = latitude;
//        }
        if (!TextUtils.isEmpty(address)) {
            mUserConfig.saveLocationAddress(address).apply();
        }
        mUserConfig.saveLongitude(String.valueOf(lng))
                .saveLatitude(String.valueOf(lat))
                .apply();

        // 没网络时，将足迹写入数据库
        if (!NetworkUtils.isNetworkAvailable(context)) {
            TrackUtil.addTrackToDB(lng, lat, address, now);// 将足迹保存至离线表
            AppLogUtil.addLogToDB(String.valueOf(lng), String.valueOf(lat), address, AppLogUtil.state2);// 保存日志
            return;
        }
        uploadTrack(lng, lat, address, now);
    }

    /**
     * 上传足迹
     */
    private void uploadTrack(final double longitude, final double latitude, final String address, final long time) {
        String url = Constant.moduleUploadTrack;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("deptId", mUserInfo.getDeptId());
        map.put("longitude", String.valueOf(longitude));
        map.put("latitude", String.valueOf(latitude));
        map.put("localtimes", String.valueOf(System.currentTimeMillis()));
        map.put("type", "0");//0:百度默认抓取；1：手动抓取
        if (!TextUtils.isEmpty(address)) {
            map.put("positionName", ReplaceSymbolUtil.transcodeToUTF8(address));
        } else {
            map.put("positionName", "");
        }
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                TrackUploadResultBean bean = GsonUtils.json2Bean(response, TrackUploadResultBean.class);
                if (null != bean && bean.success) {
                    LogUtils.d(TAG, "足迹上传成功！");
                    AppLogUtil.addLogToDB(String.valueOf(longitude), String.valueOf(latitude), address + ",速度" + speedTmp + ",距离" + distanceTmp, AppLogUtil.state1);// 保存日志
                    if (null != bean.data && !TextUtils.isEmpty(bean.data.positionName) && TextUtils.isEmpty(address)) {
                        mUserConfig.saveLocationAddress(bean.data.positionName).apply();
                    }
                } else {
                    TrackUtil.addTrackToDB(longitude, latitude, address, time);
                    AppLogUtil.addLogToDB(String.valueOf(longitude), String.valueOf(latitude), address + ",速度" + speedTmp + ",距离" + distanceTmp, AppLogUtil.state2);// 保存日志
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TrackUtil.addTrackToDB(longitude, latitude, address, time);
                AppLogUtil.addLogToDB(String.valueOf(longitude), String.valueOf(latitude), address + ",速度" + speedTmp + ",距离" + distanceTmp, AppLogUtil.state2);// 保存日志
            }
        });
        VolleyController.getInstance(context).addToQueue(post);
    }
}

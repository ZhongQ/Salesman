package com.salesman.entity;

import android.text.TextUtils;

import com.salesman.application.SalesManApplication;
import com.salesman.utils.DateUtils;
import com.studio.jframework.utils.NetworkUtils;

/**
 * 日志数据库字段
 * Created by LiHuai on 2016/5/4 0004.
 */
public class LogDBBean {
    public static final String ID = "_id";
    public static final String LOG_DATE = "log_date";
    public static final String NETWORK = "network";
    public static final String GPS = "gps";
    public static final String APP_ALIVE = "app_alive";
    public static final String UPLOAD_STATE = "upload_state";
    public static final String CREATE_TIME = "create_time";

    private String _id;
    private String log_date;
    private String network;
    private String gps;
    private String app_alive;
    private String upload_state;
    private String create_time;

    public LogDBBean() {
    }

    public LogDBBean(String app_alive, String upload_state, String create_time) {
        this.log_date = DateUtils.getYMD(System.currentTimeMillis());
        this.network = NetworkUtils.getNetworkTypeString(SalesManApplication.getInstance());
        this.gps = String.valueOf(NetworkUtils.isGpsEnable(SalesManApplication.getInstance()));
        this.app_alive = app_alive;
        this.upload_state = upload_state;
        this.create_time = create_time;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getLog_date() {
        return log_date;
    }

    public void setLog_date(String log_date) {
        this.log_date = log_date;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getApp_alive() {
        return app_alive;
    }

    public void setApp_alive(String app_alive) {
        this.app_alive = app_alive;
    }

    public String getUpload_state() {
        return upload_state;
    }

    public void setUpload_state(String upload_state) {
        this.upload_state = upload_state;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        if (!TextUtils.isEmpty(this.create_time)) {
            return DateUtils.getYMDHM(Long.parseLong(this.create_time)) + "," + this.network + "," + this.gps + "," + this.app_alive + "," + this.upload_state;
        } else {
            return "时间空," + this.network + "," + this.gps + "," + this.app_alive + "," + this.upload_state;
        }
    }
}

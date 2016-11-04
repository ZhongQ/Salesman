package com.salesman.entity;

import android.text.TextUtils;

import com.salesman.application.SalesManApplication;
import com.salesman.utils.AppLogUtil;
import com.salesman.utils.DateUtils;
import com.salesman.utils.ServiceUtil;
import com.studio.jframework.utils.NetworkUtils;

/**
 * 日志数据库字段
 * Created by LiHuai on 2016/5/4 0004.
 */
public class AppLogDBBean {
    public static final String ID = "_id";
    public static final String KEEPALIVE = "keepAlive";
    public static final String NETWORK = "network";
    public static final String GPS = "gps";
    public static final String LNG = "lng";
    public static final String LAT = "lat";
    public static final String ADDRESS = "address";
    public static final String UPLOADSTATE = "uploadState";
    public static final String CREATETIME = "createTime";
    public static final String LOGDATE = "logDate";

    private String _id;
    private String keepAlive;
    private String network;
    private String gps;
    private String lng;
    private String lat;
    private String address;
    private String uploadState;
    private String createTime;
    private String logDate;

    public AppLogDBBean() {
    }

    public AppLogDBBean(String lng, String lat, String address, String uploadState) {
        this.lng = lng;
        this.lat = lat;
        this.address = address;
        this.uploadState = uploadState;
        this.keepAlive = ServiceUtil.isAppRunning();
        this.network = getNetworStr();
        this.gps = NetworkUtils.isGpsEnable(SalesManApplication.getInstance()) ? "1" : "2";
        this.createTime = String.valueOf(System.currentTimeMillis());
        this.logDate = DateUtils.getYMD(System.currentTimeMillis());
    }

    public AppLogDBBean(String keepAlive) {
        this.keepAlive = keepAlive;
        this.lng = "0";
        this.lat = "0";
        this.address = "";
        this.uploadState = AppLogUtil.state5;
        this.network = getNetworStr();
        this.gps = NetworkUtils.isGpsEnable(SalesManApplication.getInstance()) ? "1" : "2";
        this.createTime = String.valueOf(System.currentTimeMillis());
        this.logDate = DateUtils.getYMD(System.currentTimeMillis());
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(String keepAlive) {
        this.keepAlive = keepAlive;
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

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUploadState() {
        return uploadState;
    }

    public void setUploadState(String uploadState) {
        this.uploadState = uploadState;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    private String getNetworStr() {
        String str = NetworkUtils.getNetworkTypeString(SalesManApplication.getInstance());
        if ("none".equals(str)) {
            return "1";
        } else if ("3g".equals(str) || "4g".equals(str)) {
            return "2";
        } else if ("wifi".equals(str)) {
            return "3";
        }
        return "2";
    }

    @Override
    public String toString() {
        return this.keepAlive + "," + this.uploadState + "," + this.network + "," + this.gps + "," + this.lng + "," + this.lat + "," + this.createTime + "," + this.address + "]";
    }

    public String getLogString(AppLogDBBean bean) {
        StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(bean.keepAlive)) {
            switch (bean.keepAlive) {
                case "1":
                    sb.append("运行");
                    break;
                case "2":
                    sb.append("杀死");
                    break;
            }
            sb.append(",");
        }
        if (!TextUtils.isEmpty(bean.uploadState)) {
            switch (bean.uploadState) {
                case "1":
                    sb.append("上传成功");
                    break;
                case "2":
                    sb.append("上传失败");
                    break;
                case "3":
                    sb.append("批量上传成功");
                    break;
                case "4":
                    sb.append("批量上传失败");
                    break;
                case "5":
                    sb.append("不上传");
                    break;
            }
            sb.append(",");
        }
        if (!TextUtils.isEmpty(bean.network)) {
            switch (bean.network) {
                case "1":
                    sb.append("无网络");
                    break;
                case "2":
                    sb.append("手机网络");
                    break;
                case "3":
                    sb.append("wifi");
                    break;
            }
            sb.append(",");
        }
        if (!TextUtils.isEmpty(bean.gps)) {
            switch (bean.gps) {
                case "1":
                    sb.append("GPS开");
                    break;
                case "2":
                    sb.append("GPS关");
                    break;
            }
            sb.append(",");
        }
        if (!TextUtils.isEmpty(bean.createTime)) {
            sb.append(DateUtils.getYMDHM(Long.parseLong(bean.createTime)));
        }
        sb.append(",\n");
        sb.append(bean.lng);
        sb.append(",");
        sb.append(bean.lat);
        sb.append(",");
        sb.append(bean.address);
        return sb.toString();
    }
}

package com.salesman.entity;

import android.text.TextUtils;

import com.salesman.utils.DateUtils;
import com.salesman.utils.ReplaceSymbolUtil;

/**
 * 本地足迹数据库实体
 * Created by LiHuai on 2016/3/17 0017.
 */
public class TrackDBBean {
    public static final String ID = "_id";
    public static final String TRACK_DATE = "track_date";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String ADDRESS = "address";
    public static final String CREATE_TIME = "create_time";

    private String _id;
    private String track_date;
    private double longitude;
    private double latitude;
    private String address;
    private String create_time;

    public TrackDBBean() {
    }

    public TrackDBBean(double longitude, double latitude, String address, String create_time) {
        this.track_date = DateUtils.getYMD(System.currentTimeMillis());
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.create_time = create_time;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTrack_date() {
        return track_date;
    }

    public void setTrack_date(String track_date) {
        this.track_date = track_date;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String toString(TrackDBBean bean) {
        if (!TextUtils.isEmpty(bean.getAddress())) {
            return bean.getLongitude() + "," + bean.getLatitude() + "," + bean.getCreate_time() + "," + ReplaceSymbolUtil.transcodeToUTF8(bean.getAddress()) + "]";
        }
        return bean.getLongitude() + "," + bean.getLatitude() + "," + bean.getCreate_time() + "]";
    }

    public String toString() {
        if (!TextUtils.isEmpty(this.address)) {
            if (!TextUtils.isEmpty(this.create_time)) {
                return DateUtils.getYMDHM(Long.parseLong(this.create_time)) + "," + this.longitude + "," + this.latitude + "," + this.address;
            } else {
                return "时间空," + this.longitude + "," + this.latitude + "," + this.address;
            }
        }
        if (!TextUtils.isEmpty(this.create_time)) {
            return DateUtils.getYMDHM(Long.parseLong(this.create_time)) + "," + this.longitude + "," + this.latitude;
        } else {
            return "时间空" + "," + this.longitude + "," + this.latitude;
        }
    }
}

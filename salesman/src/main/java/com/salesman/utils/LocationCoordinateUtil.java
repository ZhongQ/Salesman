package com.salesman.utils;

import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.salesman.application.SalesManApplication;

import java.util.List;

/**
 * 定位信息工具类
 * Created by LiHuai on 2016/2/23 0023.
 */
public class LocationCoordinateUtil {

    // 深圳市南山区政府 22.5390170000,113.9369500000
    // 深圳市政府经纬度22.5485150000,114.0661120000
    public final static double LAT = 22.5485150000;// 默认纬度
    public final static double LNG = 114.0661120000;// 默认经度
    private static final double EARTH_RADIUS = 6378137.0;// 地球半径

    /**
     * 获取经度
     *
     * @return
     */
    public synchronized static double getLongitude() {
        if (!TextUtils.isEmpty(getLongitudeString())) {
            return Double.parseDouble(getLongitudeString());
        } else {
            return LNG;
        }
    }

    /**
     * 获取纬度
     *
     * @return
     */
    public synchronized static double getLatitude() {
        if (!TextUtils.isEmpty(getLatitudeString())) {
            return Double.parseDouble(getLatitudeString());
        } else {
            return LAT;
        }
    }

    /**
     * 获取自动定位保存的经度字符串
     */
    public static String getLongitudeString() {
        String lng = SalesManApplication.g_GlobalObject.getmUserConfig().getLongitude();
        if (TextUtils.isEmpty(lng) || Double.parseDouble(lng) == 0
                || Double.parseDouble(lng) == 4.9E-324) {
            return "";
        } else {
            return lng;
        }
    }

    /**
     * 获取自动定位保存的纬度字符串
     */
    public static String getLatitudeString() {
        String lat = SalesManApplication.g_GlobalObject.getmUserConfig().getLatitude();
        if (TextUtils.isEmpty(lat) || Double.parseDouble(lat) == 0
                || Double.parseDouble(lat) == 4.9E-324) {
            return "";
        } else {
            return lat;
        }
    }


    /**
     * 根据定位选择最合适位置
     */
    public static String getLocationAddress(BDLocation bdLocation) {
        if (null == bdLocation) {
            return "";
        }
        String address = "";
        List<Poi> list = bdLocation.getPoiList();// POI数据
        if (list != null && list.size() > 0) {
            if (!TextUtils.isEmpty(bdLocation.getCity()) && !"null".equals(bdLocation.getCity())) {
                address = bdLocation.getCity();
            }
            if (!TextUtils.isEmpty(bdLocation.getDistrict()) && !"null".equals(bdLocation.getDistrict())) {
                address = address + bdLocation.getDistrict() + list.get(0).getName();
            }
        } else {
            address = bdLocation.getAddrStr();
        }
        if (!TextUtils.isEmpty(address)) {
            address = address.replace("中国", "");
            address = address.replace("广东省", "");
        }
        return address;
    }

    /**
     * 获取定位的详细信息
     */
    public static String getLocationDetail(BDLocation location) {
        StringBuffer sb = new StringBuffer(256);
        sb.append("time : ");
        sb.append(location.getTime());
        sb.append("\nerror code : ");
        sb.append(location.getLocType());
        sb.append("\nlatitude : ");
        sb.append(location.getLatitude());
        sb.append("\nlontitude : ");
        sb.append(location.getLongitude());
        sb.append("\nradius : ");
        sb.append(location.getRadius());
        if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
            sb.append("\nspeed : ");
            sb.append(location.getSpeed());// 单位：公里每小时
            sb.append("\nsatellite : ");
            sb.append(location.getSatelliteNumber());
            sb.append("\nheight : ");
            sb.append(location.getAltitude());// 单位：米
            sb.append("\ndirection : ");
            sb.append(location.getDirection());// 单位度
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
            sb.append("\ndescribe : ");
            sb.append("gps定位成功");

        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
            //运营商信息
            sb.append("\noperationers : ");
            sb.append(location.getOperators());
            sb.append("\ndescribe : ");
            sb.append("网络定位成功");
        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
            sb.append("\ndescribe : ");
            sb.append("离线定位成功，离线定位结果也是有效的");
        } else if (location.getLocType() == BDLocation.TypeServerError) {
            sb.append("\ndescribe : ");
            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
            sb.append("\ndescribe : ");
            sb.append("网络不同导致定位失败，请检查网络是否通畅");
        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
            sb.append("\ndescribe : ");
            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
        }
        sb.append("\nlocationdescribe : ");
        sb.append(location.getLocationDescribe());// 位置语义化信息
        List<Poi> list = location.getPoiList();// POI数据
        if (list != null) {
            sb.append("\npoilist size = : ");
            sb.append(list.size());
            for (Poi p : list) {
                sb.append("\npoi= : ");
                sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
            }
        }

        return sb.toString();
    }

    /**
     * 获取定位的类型
     */
    public static String getLocationType(BDLocation location) {
        StringBuffer sb = new StringBuffer(256);
        if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
            sb.append("gps定位成功");
        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
            sb.append("网络定位成功");
        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
            sb.append("离线定位成功");
        } else if (location.getLocType() == BDLocation.TypeServerError) {
            sb.append("定位失败");
//            sb.append("定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
            sb.append("定位失败");
//            sb.append("网络不同导致定位失败，请检查网络是否通畅");
        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
            sb.append("定位失败");
//            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
        }
        return sb.toString();
    }

    /**
     * 计算经纬度之间直线距离，单位米
     *
     * @param longitude1
     * @param latitude1
     * @param longitude2
     * @param latitude2
     * @return
     */
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        if (longitude1 == longitude2 && latitude1 == latitude2) {
            return 0d;
        }
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(Lat1) * Math.cos(Lat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }
}

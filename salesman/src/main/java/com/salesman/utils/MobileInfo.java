package com.salesman.utils;

/**
 * Created by andi on 2015/10/20 0020.
 */
public class MobileInfo {
    //手机型号
    public static String getMobileModel() {
        return android.os.Build.MODEL;
    }

    //手机品牌
    public static String getMobileVendor() {
        return android.os.Build.BRAND;
    }

    //系统固件版本
    public static String getSysVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    //VERSION.SDK SDK版本
    public static String getVersionSDK() {
        return android.os.Build.VERSION.SDK;
    }


}

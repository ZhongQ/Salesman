package com.salesman.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.salesman.application.SalesManApplication;
import com.salesman.service.ForegroundService;
import com.salesman.service.WatchService;
import com.studio.jframework.utils.LogUtils;

import java.util.List;

/**
 * 服务工具类
 * Created by LiHuai on 2016/3/7 0007.
 */
public class ServiceUtil {
    private static final String TAG = ServiceUtil.class.getSimpleName();
    private static final String MY_PKG_NAME = "com.salesman";

    /**
     * 开启服务
     */
    public static void startService() {
        LocationManagerUtil.isSave = true;
        Intent startIntent = new Intent(SalesManApplication.getInstance(), ForegroundService.class);
        SalesManApplication.getInstance().startService(startIntent);
    }

    /**
     * 停止服务
     */
    public static void stopService() {
        stopWatchService();// 停止守护进程
        Intent stopIntent = new Intent(SalesManApplication.getInstance(), ForegroundService.class);
        SalesManApplication.getInstance().stopService(stopIntent);
    }

    /**
     * 开启守护进程
     */
    public static void startWatchService() {
        Intent startIntent = new Intent(SalesManApplication.getInstance(), WatchService.class);
        SalesManApplication.getInstance().startService(startIntent);
    }

    /**
     * 停止守护进程
     */
    public static void stopWatchService() {
        Intent stopIntent = new Intent(SalesManApplication.getInstance(), WatchService.class);
        SalesManApplication.getInstance().stopService(stopIntent);
    }

    /**
     * 判断服务是否存在
     *
     * @param serviceName
     * @return
     */
    public static boolean judgeServiceRunning(String serviceName) {
        ActivityManager manager = (ActivityManager) SalesManApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                LogUtils.d(TAG, service.service.getClassName());
                return true;
            }
        }
        return false;
    }

    /**
     * 判断百度定位服务是否存在
     *
     * @return
     */
    public static boolean judgeServiceRunning() {
        ActivityManager manager = (ActivityManager) SalesManApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(100)) {
            if ("com.baidu.location.f".equals(service.service.getClassName())) {
                LogUtils.d(TAG, service.service.getClassName());
                return true;
            }
        }
        return false;
    }

    public static String judgeAppRunning() {
        ActivityManager am = (ActivityManager) SalesManApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME) && info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                return "运行";
            }
        }
        return "杀死";
    }

    public static String isAppRunning() {
        ActivityManager am = (ActivityManager) SalesManApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME) && info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                return "1";
            }
        }
        return "2";
    }
}

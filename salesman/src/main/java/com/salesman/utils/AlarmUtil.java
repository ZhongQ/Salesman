package com.salesman.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.salesman.application.SalesManApplication;
import com.salesman.service.ForegroundService;
import com.studio.jframework.utils.LogUtils;

/**
 * 闹钟工具类
 * Created by LiHuai on 2016/3/4 0004.
 */
public class AlarmUtil {
    private static final String TAG = AlarmUtil.class.getSimpleName();

    /**
     * 开启闹钟（与服务绑定）
     */
    public static void startServiceAlarm() {
        LogUtils.d(TAG, "开启闹钟");
        Context context = SalesManApplication.getInstance();
        Intent intent = new Intent(context, ForegroundService.class);
        context.startService(intent);// 马上启动服务
        PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(SalesManApplication.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), LocationManagerUtil.ALARM_TIME, pi);
    }

    /**
     * 关闭闹钟（与服务绑定）
     */
    public static void cancelServiceAlarm() {
        LogUtils.d(TAG, "关闭闹钟");
        Context context = SalesManApplication.getInstance();
        Intent intent = new Intent(context, ForegroundService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(SalesManApplication.ALARM_SERVICE);
        am.cancel(pi);
        ServiceUtil.stopService();// 停止所有服务
    }
}

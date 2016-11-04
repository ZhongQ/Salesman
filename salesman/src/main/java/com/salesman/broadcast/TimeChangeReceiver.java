package com.salesman.broadcast;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.salesman.application.SalesManApplication;
import com.salesman.service.ForegroundService;
import com.salesman.utils.AlarmUtil;
import com.salesman.utils.UserConfigPreference;
import com.studio.jframework.utils.LogUtils;

/**
 * 手机时间改变监听广播
 * Created by LiHuai on 2016/5/13 0013.
 */
public class TimeChangeReceiver extends BroadcastReceiver {
    private static final String TAG = TimeChangeReceiver.class.getSimpleName();
    private UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();

    @Override
    public void onReceive(Context context, Intent intent) {
        //启动service
        boolean isServiceRunning = false;
        LogUtils.d(TAG, "TimeChangeReceiver");
        if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
            //检查Service状态
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (ForegroundService.SERVICENAME.equals(service.service.getClassName())) {
                    isServiceRunning = true;
                }
            }
            if (!isServiceRunning) {
                if (null != mUserConfig && mUserConfig.getGotoWork() && !mUserConfig.getGetOffWork() && !mUserConfig.getHandExit()) {
                    AlarmUtil.startServiceAlarm();
                }
            }
        }
    }
}

package com.salesman.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.salesman.application.SalesManApplication;
import com.salesman.utils.AlarmUtil;
import com.salesman.utils.AppLogUtil;
import com.salesman.utils.LocationManagerUtil;
import com.salesman.utils.ServiceUtil;
import com.salesman.utils.UserConfigPreference;
import com.studio.jframework.utils.LogUtils;

/**
 * 守护进程
 * Created by LiHuai on 2016/3/10 0010.
 */
public class WatchService extends Service {
    private static final String TAG = WatchService.class.getSimpleName();
    private UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
    public static final String SERVICENAME = "com.salesman.service.WatchService";
    private MyThread mThread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == mThread) {
            mThread = new MyThread();
            mThread.start();
        } else {
            if (mThread.getState() == Thread.State.TERMINATED) {
                mThread = new MyThread();
                mThread.start();
            } else {
                if (!mThread.isAlive()) {
                    mThread.start();
                }
            }
        }
        return START_STICKY;
    }

    private class MyThread extends Thread {
        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(LocationManagerUtil.THREAD_TIME);
                    if (!ServiceUtil.judgeServiceRunning(ForegroundService.SERVICENAME)) {
                        LogUtils.d(TAG, "重启" + ForegroundService.SERVICENAME);
                        AlarmUtil.startServiceAlarm();// 重启闹钟
                        AppLogUtil.addLogToDB(AppLogUtil.die);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                //由于阻塞库函数，如：Object.wait,Thread.sleep除了抛出异常外，还会清除线程中断状态，因此可能在这里要保留线程的中断状态
                Thread.currentThread().interrupt();
            }
        }

        public void cancel() {
            interrupt();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "onDestroy");
        if (null != mThread) {
            mThread.cancel();
        }
    }
}

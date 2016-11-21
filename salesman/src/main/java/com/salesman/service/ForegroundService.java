package com.salesman.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.LocationClient;
import com.salesman.R;
import com.salesman.activity.MainActivity;
import com.salesman.application.SalesManApplication;
import com.salesman.broadcast.TimeChangeReceiver;
import com.salesman.utils.AlarmUtil;
import com.salesman.utils.AppLogUtil;
import com.salesman.utils.LocationManagerUtil;
import com.salesman.utils.ScreenListener;
import com.salesman.utils.ServiceUtil;
import com.salesman.utils.UserConfigPreference;
import com.salesman.utils.UserInfoPreference;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.utils.NetworkUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 前台服务
 * Created by LiHuai on 2016/17/02 0025.
 */
public class ForegroundService extends Service {
    private static final String TAG = ForegroundService.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
    private UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();

    public static final String SERVICENAME = "com.salesman.service.ForegroundService";
    private boolean mReflectFlg = false;

    private static final int NOTIFICATION_ID = 1; // 如果id设置为0,会导致不能设置为前台service  
    private static final Class<?>[] mSetForegroundSignature = new Class[]{boolean.class};
    private static final Class<?>[] mStartForegroundSignature = new Class[]{int.class, Notification.class};
    private static final Class<?>[] mStopForegroundSignature = new Class[]{boolean.class};

    private NotificationManager mNM;
    private Method mSetForeground;
    private Method mStartForeground;
    private Method mStopForeground;
    private Object[] mSetForegroundArgs = new Object[1];
    private Object[] mStartForegroundArgs = new Object[2];
    private Object[] mStopForegroundArgs = new Object[1];

    // 通知
    private MyThread mThread;
    private Notification.Builder builder;
    private PendingIntent contentIntent;
    // 屏幕锁屏监听
    private ScreenListener screenListener;
    private static final String KYEGUARD_LOCK_FLAG = "Salesman";
    //
    private TimeChangeReceiver receiver;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        mNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            mStartForeground = ForegroundService.class.getMethod("startForeground", mStartForegroundSignature);
            mStopForeground = ForegroundService.class.getMethod("stopForeground", mStopForegroundSignature);
        } catch (NoSuchMethodException e) {
            mStartForeground = mStopForeground = null;
        }

        try {
            mSetForeground = getClass().getMethod("setForeground", mSetForegroundSignature);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("OS doesn't have Service.startForeground OR Service.setForeground!");
        }
        startForegroundCompat(NOTIFICATION_ID, getNotification());
        LocationManagerUtil.isSave = true;// 保存定位时间
        //服务启动广播接收器，使得广播接收器可以在程序退出后在后台继续执行，接收系统时间变更广播事件
        receiver = new TimeChangeReceiver();
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_TIME_TICK));

        screenListener = new ScreenListener(SalesManApplication.getInstance());
        screenListener.begin(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                LogUtils.d(TAG, "ScreenOn");
//                if (null != mUserConfig && mUserConfig.getGotoWork() && !mUserConfig.getGetOffWork() && !mUserConfig.getHandExit() && !ServiceUtil.judgeServiceRunning(SERVICENAME)) {
//                    AlarmUtil.startServiceAlarm();
//                }
//                KeyguardManager mKeyguardManager = (KeyguardManager) ForegroundService.this.getSystemService(Context.KEYGUARD_SERVICE);
//                KeyguardManager.KeyguardLock mKeyguardLock = mKeyguardManager.newKeyguardLock(KYEGUARD_LOCK_FLAG);
//                mKeyguardLock.disableKeyguard(); // 解锁系统锁屏
//                Intent screenIntent = new Intent(ForegroundService.this, ScreenSaverShowActivity.class);
//                screenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(screenIntent);
            }

            @Override
            public void onScreenOff() {
                LogUtils.d(TAG, "ScreenOff");
//                if (!TextUtils.isEmpty(mUserInfo.getUserId()) && !TextUtils.isEmpty(mUserInfo.getToken())) {
//                    Intent mainIntent = new Intent(SalesManApplication.getInstance(), MainActivity.class);
//                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(mainIntent);
//                }
            }

            @Override
            public void onUserPresent() {
                LogUtils.d(TAG, "UserPresent");
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        LogUtils.d(TAG, "onStartCommand");

//        mHandler.sendMessage(mHandler.obtainMessage(0));
        startLocation();

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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (null != mThread) {
            mThread.cancel();
        }
        unregisterReceiver(receiver);
        screenListener.unregisterListener();
        stopForegroundCompat(NOTIFICATION_ID);
        AppLogUtil.addLogToDB(AppLogUtil.die);
        if (null != mUserConfig && mUserConfig.getGotoWork() && !mUserConfig.getGetOffWork() && !mUserConfig.getHandExit() && mUserConfig.getTrackSet()) {
            AlarmUtil.startServiceAlarm();
        }
    }

    void invokeMethod(Method method, Object[] args) {
        try {
            method.invoke(this, args);
        } catch (InvocationTargetException e) {
            // Should not happen.  
            Log.w("ApiDemos", "Unable to invoke method", e);
        } catch (IllegalAccessException e) {
            // Should not happen.  
            Log.w("ApiDemos", "Unable to invoke method", e);
        }
    }

    /**
     * This is a wrapper around the new startForeground method, using the older
     * APIs if it is not available.
     */
    void startForegroundCompat(int id, Notification notification) {
        if (mReflectFlg) {
            // If we have the new startForeground API, then use it.  
            if (mStartForeground != null) {
                mStartForegroundArgs[0] = Integer.valueOf(id);
                mStartForegroundArgs[1] = notification;
                invokeMethod(mStartForeground, mStartForegroundArgs);
                return;
            }

            // Fall back on the old API.  
            mSetForegroundArgs[0] = Boolean.TRUE;
            invokeMethod(mSetForeground, mSetForegroundArgs);
            mNM.notify(id, notification);
        } else {  
            /* 还可以使用以下方法，当sdk大于等于5时，调用sdk现有的方法startForeground设置前台运行， 
             * 否则调用反射取得的sdk level 5（对应Android 2.0）以下才有的旧方法setForeground设置前台运行 */

            if (VERSION.SDK_INT >= 5) {
                startForeground(id, notification);
            } else {
                // Fall back on the old API.  
                mSetForegroundArgs[0] = Boolean.TRUE;
                invokeMethod(mSetForeground, mSetForegroundArgs);
                mNM.notify(id, notification);
            }
        }
    }

    /**
     * This is a wrapper around the new stopForeground method, using the older
     * APIs if it is not available.
     */
    void stopForegroundCompat(int id) {
        if (mReflectFlg) {
            // If we have the new stopForeground API, then use it.  
            if (mStopForeground != null) {
                mStopForegroundArgs[0] = Boolean.TRUE;
                invokeMethod(mStopForeground, mStopForegroundArgs);
                return;
            }

            // Fall back on the old API.  Note to cancel BEFORE changing the  
            // foreground state, since we could be killed at that point.  
            mNM.cancel(id);
            mSetForegroundArgs[0] = Boolean.FALSE;
            invokeMethod(mSetForeground, mSetForegroundArgs);
        } else {  
            /* 还可以使用以下方法，当sdk大于等于5时，调用sdk现有的方法stopForeground停止前台运行， 
             * 否则调用反射取得的sdk level 5（对应Android 2.0）以下才有的旧方法setForeground停止前台运行 */

            if (VERSION.SDK_INT >= 5) {
                stopForeground(true);
            } else {
                // Fall back on the old API.  Note to cancel BEFORE changing the  
                // foreground state, since we could be killed at that point.  
                mNM.cancel(id);
                mSetForegroundArgs[0] = Boolean.FALSE;
                invokeMethod(mSetForeground, mSetForegroundArgs);
            }
        }
    }

    /**
     * 获取通知对象
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private Notification getNotification() {
        if (null == builder) {
            builder = new Notification.Builder(this);
        }
        if (!TextUtils.isEmpty(mUserInfo.getToken()) && !TextUtils.isEmpty(mUserInfo.getUserId())) {
            if (null == contentIntent) {
                contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
            }
            builder.setContentIntent(contentIntent);
        }
        builder.setSmallIcon(R.drawable.applogo);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.applogo));
        builder.setTicker(getText(R.string.app_name));
        builder.setContentTitle(getText(R.string.app_name));
        builder.setContentText(mUserConfig.getLocationAddress());
        return builder.build();
    }

    /**
     * 当达到队列容量时，在这里会阻塞
     * put的内部会调用LockSupport.park()这个是用来阻塞线程的方法
     * 当其他线程，调用此线程的interrupt()方法时，会设置一个中断标志
     * LockSupport.part()中检测到这个中断标志，会抛出InterruptedException，并清除线程的中断标志
     * 因此在异常段调用Thread.currentThread().isInterrupted()返回为false
     */
    private class MyThread extends Thread {
        @Override
        public void run() {
            try {
                while ((!Thread.currentThread().isInterrupted() && mUserConfig.getGotoWork() && !mUserConfig.getGetOffWork())) {
                    if (!ServiceUtil.judgeServiceRunning(WatchService.SERVICENAME)) {
                        LogUtils.d(TAG, "重启" + WatchService.SERVICENAME);
                        ServiceUtil.startWatchService();// 重启守护进程
                    }
                    Thread.sleep(LocationManagerUtil.THREAD_TIME);
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

    /**
     * 开启定位
     */
    private void startLocation() {
        startForegroundCompat(NOTIFICATION_ID, getNotification());
        LocationManagerUtil locationManagerUtil = LocationManagerUtil.getInstance(SalesManApplication.getInstance());
        LocationClient mlc = LocationManagerUtil.getmLocationClient();
        LocationManagerUtil.MyLocationListener mll = LocationManagerUtil.getmLocationListener();
        if (null == mlc) {
            locationManagerUtil.initLocation(LocationManagerUtil.TIME);
            if (null == mll) {
                locationManagerUtil.initLocationListener();
            }
        } else if (null == mll) {
            locationManagerUtil.initLocationListener();
        }
        locationManagerUtil.startLocationListener();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            startForegroundCompat(NOTIFICATION_ID, getNotification());
            LogUtils.d(TAG, "定位--网络状态" + NetworkUtils.isNetworkAvailable(SalesManApplication.getInstance()));
            LocationManagerUtil locationManagerUtil = LocationManagerUtil.getInstance(SalesManApplication.getInstance());
            LocationClient mlc = LocationManagerUtil.getmLocationClient();
            LocationManagerUtil.MyLocationListener mll = LocationManagerUtil.getmLocationListener();
            if (null == mlc) {
                locationManagerUtil.initLocation(LocationManagerUtil.TIME);
                if (null == mll) {
                    locationManagerUtil.initLocationListener();
                }
            } else if (null == mll) {
                locationManagerUtil.initLocationListener();
            }
            locationManagerUtil.startLocationListener();
        }
    };

}
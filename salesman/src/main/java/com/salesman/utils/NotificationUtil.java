package com.salesman.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

import com.salesman.R;
import com.salesman.activity.MainActivity;
import com.salesman.application.SalesManApplication;

/**
 * 通知栏工具类
 * Created by LiHuai on 2016/5/9 0009.
 */
public class NotificationUtil {
    private static Context mContext = SalesManApplication.getInstance();
    public static final String SHOW_NOTI = "show_noti";
    public static final String CLEAR_NOTI_ACTION = "clear_noti_action";
    public static final int NOTIFICATION_ID = 1001;

    public static void showNotification() {
        // 此方法不能在onCreate方法之前
        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        //此Builder为android.support.v4.app.NotificationCompat.Builder中的，下同。
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
        //系统收到通知时，通知栏上面显示的文字。
        mBuilder.setTicker("定位失败，请检查您的网络和GPS是否正常");
        //显示在通知栏上的小图标
        mBuilder.setSmallIcon(R.drawable.applogo);
        //通知标题
        mBuilder.setContentTitle("定位失败");
        //通知内容
        mBuilder.setContentText("定位失败，请检查您的网络和GPS是否正常");
        //设置大图标，即通知条上左侧的图片（如果只设置了小图标，则此处会显示小图标）
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.applogo));
        //显示在小图标左侧的数字
//        mBuilder.setNumber(6);
        //设置为不可清除模式
        mBuilder.setOngoing(true);
        // 设置通知栏点击跳转后的界面
        Intent mainIntent = new Intent(mContext, MainActivity.class);
        mainIntent.setAction(CLEAR_NOTI_ACTION);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, mainIntent, 0);
        mBuilder.setContentIntent(contentIntent);
        //设置这个标志当用户单击面板就可以让通知将自动取消
        mBuilder.setAutoCancel(true);
        //向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);

        //显示通知，id必须不重复，否则新的通知会覆盖旧的通知（利用这一特性，可以对通知进行更新）
        nm.notify(NOTIFICATION_ID, mBuilder.build());
    }
}

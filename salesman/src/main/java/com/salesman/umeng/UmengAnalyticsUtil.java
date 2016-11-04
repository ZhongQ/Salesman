package com.salesman.umeng;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * 友盟统计工具类
 * Created by LiHuai on 2016/8/12 0012.
 */
public class UmengAnalyticsUtil {

    public static void umengOnResume(Context context) {
        MobclickAgent.onResume(context);
    }

    public static void umengOnPause(Context context) {
        MobclickAgent.onPause(context);
    }

    public static void umengOnPageStart(String pageName) {
        MobclickAgent.onPageStart(pageName);
    }

    public static void umengOnPageEnd(String pageName) {
        MobclickAgent.onPageEnd(pageName);
    }

    public static void umengOnCustomResume(Context context, String pageName) {
        umengOnPageStart(pageName);
        umengOnResume(context);
    }

    public static void umengOnCustomPause(Context context, String pageName) {
        umengOnPageEnd(pageName);
        umengOnPause(context);
    }

    /**
     * 友盟计数统计
     *
     * @param context
     * @param eventId
     */
    public static void onEvent(Context context, String eventId) {
        MobclickAgent.onEvent(context, eventId);
    }

}

package com.salesman.utils;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时器工具类
 * Created by LiHuai on 2016/4/6 0006.
 */
public class TimerUtil {
    private static Timer timer;
    private static TimerTask timerTask;
    private static final long delay = 1000 * 10;
    private static final long period = 1000 * 30;

    /**
     * 开始计时
     */
    public static void startTimer(final Handler handler) {
        if (null == timer) {
            timer = new Timer();
        }
        if (null == timerTask) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(1);
                }
            };
        }
        timer.schedule(timerTask, delay, period);
    }

    /**
     * 开始计时
     */
    public static void startTimer(final Handler handler, long delay, long period) {
        if (null == timer) {
            timer = new Timer();
        }
        if (null == timerTask) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(1);
                }
            };
        }
        timer.schedule(timerTask, delay, period);
    }

    /**
     * 停止计时
     */
    public static void clearTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
    }

    /**
     * 重启定时器
     *
     * @param handler
     */
    public static void restartTimer(Handler handler) {
        clearTimer();
        startTimer(handler);
    }

    /**
     * 重启定时器
     *
     * @param handler
     */
    public static void restartTimer(Handler handler, long delay, long period) {
        clearTimer();
        startTimer(handler, delay, period);
    }
}

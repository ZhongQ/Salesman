package com.salesman.application;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.salesman.BuildConfig;
import com.salesman.common.Constant;
import com.salesman.common.EventBusConfig;
import com.salesman.common.GsonUtils;
import com.salesman.entity.BaseBean;
import com.salesman.global.GlobalObject;
import com.salesman.network.BaseHelper;
import com.salesman.utils.ImgCacheUtil;
import com.salesman.utils.LocalImageHelper;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyErrorHelper;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.CrashHandler;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.utils.PackageUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import okhttp3.OkHttpClient;

/**
 * Created by LiHuai on 2016/1/25 0025.
 */
public class SalesManApplication extends Application {
    public static final String TAG = SalesManApplication.class.getSimpleName();

    public static GlobalObject g_GlobalObject = null;
    private static SalesManApplication mInstance = null;
    private PushAgent mPushAgent;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        g_GlobalObject = GlobalObject.getInstance(this);
        //允许log，调试下打开，正式版自动关闭log
        LogUtils.setEnable(BuildConfig.DEBUG);
        // 百度地图SDK初始化
        SDKInitializer.initialize(getApplicationContext());

        // 初始化ImageLoader和本地图片工具类
        initImage();
        // 创建APP文件夹
        CreateAppLoadDir();

        // 日志
        CrashHandler.getInstance().init(this, "Crash", null, BuildConfig.DEBUG, new CrashHandler.ExceptionOperator() {
            @Override
            public void onExceptionThrows() {
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
                System.exit(1);
            }
        });
        String crashInfo = CrashHandler.getCrashInfo();
        if (!TextUtils.isEmpty(crashInfo)) {
            uploadCrashLog(crashInfo);
        }
        // 初始化OKHttp
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                .writeTimeout(20000L, TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(okHttpClient);

        initUmengPush();
        initUmengAnalytics();
    }

    public static SalesManApplication getInstance() {
        return mInstance;
    }

    public static void initImage() {
        //初始化多图选择
        ImageLoader.getInstance().init(ImgCacheUtil.getInstance(getInstance()).getImgLoaderConfig());
        LocalImageHelper.init(getInstance());
    }

    public void CreateAppLoadDir() {
        if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())) {
            File appLoadDir = new File(Environment.getExternalStorageDirectory(), Constant.DOWNLOADDIR);
            if (!appLoadDir.exists()) {
                appLoadDir.mkdirs();
            }
        }
    }

    public String getCachePath() {
        File cacheDir;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            cacheDir = getExternalCacheDir();
        } else {
            cacheDir = getCacheDir();
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir.getAbsolutePath();
    }

    /**
     * 上传AppCrashLog
     *
     * @param crashText crash 信息
     */
    public void uploadCrashLog(String crashText) {
        if (TextUtils.isEmpty(g_GlobalObject.getmUserInfo().getUserId())) {
            return;
        }
        String url = Constant.moduleUploadCrash;
        Map<String, String> params = g_GlobalObject.getCommomParams();
        params.put("userId", g_GlobalObject.getmUserInfo().getUserId());
        params.put("errorMsg", crashText);
        params.put("errorTime", String.valueOf(System.currentTimeMillis()));
        params.put("version", String.valueOf(PackageUtils.getVersionName(this)));
        params.put("deviceType", "1");
        LogUtils.d(TAG, url + BaseHelper.getParams(params));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d(TAG, response);
                BaseBean baseBean = GsonUtils.json2Bean(response, BaseBean.class);
                if (null != baseBean && baseBean.success) {
                    CrashHandler.deleteLogFile();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.e(TAG, VolleyErrorHelper.getMessage(error));
            }
        });
        VolleyController.getInstance(this).addToQueue(post);
    }

    /**
     * 初始化友盟推送
     */
    private void initUmengPush() {
        // 友盟推送初始化
        mPushAgent = PushAgent.getInstance(mInstance);
        mPushAgent.setDebugMode(false);
        /**
         * 自定义消息处理
         */
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage uMessage) {
                super.dealWithCustomMessage(context, uMessage);
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        //自定义消息的点击统计
                        UTrack.getInstance(mInstance).trackMsgClick(uMessage);
//                        ToastUtil.show(mInstance, "自定义消息");
                        EventBus.getDefault().post(EventBusConfig.HOME_REFRESH);
                    }
                });
            }
        };
        mPushAgent.setMessageHandler(messageHandler);
        // 设置通知栏最多显示两条通知（当通知栏已经有两条通知，此时若第三条通知到达，则会把第一条通知隐藏）
        // 参数number可以设置为0~10之间任意整数。当参数为0时，表示不合并通知。
        // 该方法可以多次调用，以最后一次调用时的设置为准。
        mPushAgent.setDisplayNotificationNumber(10);
        // 同一台设备在某段时间内（默认是60秒）收到多条消息时，不会重复提醒，可以通过下面的函数来设置冷却时间。
        mPushAgent.setMuteDurationSeconds(10);
    }

    /**
     * 初始化友盟统计
     */
    private void initUmengAnalytics() {
        MobclickAgent.setScenarioType(mInstance, MobclickAgent.EScenarioType.E_UM_NORMAL);// 普通统计场景类型
        MobclickAgent.setDebugMode(false);// 打开调试模式
        MobclickAgent.openActivityDurationTrack(false);// 禁止默认的页面统计方式
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行,函数是模拟一个过程环境，在真机中永远也不会被调用
        LogUtils.d(TAG, "onTerminate");
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        LogUtils.d(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        LogUtils.d(TAG, "onTrimMemory");
        super.onTrimMemory(level);
    }
}

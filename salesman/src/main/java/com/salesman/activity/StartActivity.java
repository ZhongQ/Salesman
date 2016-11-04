package com.salesman.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.salesman.R;
import com.salesman.activity.login.LoginActivity;
import com.salesman.application.SalesManApplication;
import com.salesman.common.Constant;
import com.salesman.common.GsonUtils;
import com.salesman.entity.UserBean;
import com.salesman.network.BaseHelper;
import com.salesman.umeng.UmengAnalyticsUtil;
import com.salesman.utils.CityUtil;
import com.salesman.utils.ClientTypeUtil;
import com.salesman.utils.DeviceUtil;
import com.salesman.utils.MobileInfo;
import com.salesman.utils.ToastUtil;
import com.salesman.utils.UserConfigPreference;
import com.salesman.utils.UserInfoPreference;
import com.studio.jframework.network.volley.VolleyController;
import com.studio.jframework.network.volley.VolleyStringRequest;
import com.studio.jframework.utils.LogUtils;
import com.studio.jframework.utils.PackageUtils;

import java.util.Map;

/**
 * 启动界面
 * Created by LiHuai on 2016/1/21.
 */
public class StartActivity extends Activity {
    public static final String TAG = StartActivity.class.getSimpleName();
    private UserInfoPreference mUserInfo = SalesManApplication.g_GlobalObject.getmUserInfo();
    private UserConfigPreference mUserConfig = SalesManApplication.g_GlobalObject.getmUserConfig();
    private boolean isBackClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (mUserConfig.getIsFirst()) {
            Intent guide = new Intent(this, GuideActivity.class);
            startActivity(guide);
            finish();
        } else {
            // 获取客户类型
            new ClientTypeUtil().getClientTypeData();
            // 获取省市区
            new CityUtil().getAllCityUtil();
            judgeLogIn();
        }
    }

    /**
     * 登录判断
     */
    private void judgeLogIn() {
        if (TextUtils.isEmpty(mUserInfo.getToken())) {
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
            StartActivity.this.finish();
        } else {
            logIn();
        }
    }

    /**
     * 登录
     */
    private void logIn() {
        String url = Constant.moduleLogIn;
        Map<String, String> map = SalesManApplication.g_GlobalObject.getCommomParams();
        map.put("deviceUUID", DeviceUtil.getImei(this));
        map.put("deviceName", MobileInfo.getMobileModel());
        map.put("version", PackageUtils.getVersionName(this));
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        LogUtils.d(TAG, url + BaseHelper.getParams(map));
        VolleyStringRequest post = new VolleyStringRequest(Request.Method.POST, url, map, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!isBackClick) {
                    return;
                }
                LogUtils.d(TAG, response);
                UserBean userBean = GsonUtils.json2Bean(response, UserBean.class);
                if (null != userBean) {
                    if (userBean.success && null != userBean.data) {
                        mUserInfo.saveUserId(userBean.data.userId)
                                .saveUserName(userBean.data.userName)
                                .saveNickName(userBean.data.nickName)
                                .saveMobile(userBean.data.mobile)
                                .saveDeviceUUID(userBean.data.deviceUUID)
                                .saveSessionid(userBean.data.sessionid)
                                .saveToken(userBean.data.token)
                                .saveSex(userBean.data.gender)
                                .saveUserType(userBean.data.userType)
                                .savePostName(userBean.data.postName)
                                .saveDeptId(userBean.data.deptId)
                                .saveDeptName(userBean.data.deptName)
                                .saveAreaId(userBean.data.areaId)
                                .saveBDType(userBean.data.bdType);
                        mUserInfo.apply();
                        mUserConfig.saveGoToWork(1 == userBean.data.signStart)
                                .saveGetOffWork(1 == userBean.data.signEnd);
                        mUserConfig.apply();
                        if (!TextUtils.isEmpty(userBean.data.timeHz)) {
                            mUserInfo.saveUploadTrackTime(Long.parseLong(userBean.data.timeHz) * 1000).apply();
                        } else {
                            mUserInfo.saveUploadTrackTime(60000).apply();
                        }
                        startActivity(new Intent(StartActivity.this, MainActivity.class));
                    } else {
                        ToastUtil.show(StartActivity.this, userBean.msg);
                        startActivity(new Intent(StartActivity.this, LoginActivity.class));
                    }
                } else {
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                }
                StartActivity.this.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!isBackClick) {
                    return;
                }
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
                StartActivity.this.finish();
            }
        });
        VolleyController.getInstance(this).addToQueue(post);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isBackClick = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengAnalyticsUtil.umengOnCustomResume(this, this.getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengAnalyticsUtil.umengOnCustomPause(this, this.getClass().getSimpleName());
    }
}

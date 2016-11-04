package com.salesman.global;

import android.content.Context;
import android.text.TextUtils;

import com.salesman.db.DBHelper;
import com.salesman.utils.UserConfigPreference;
import com.salesman.utils.UserInfoPreference;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局类
 * Created by LiHuai on 2016/1/27 0027.
 */
public class GlobalObject {
    public static final String TAG = GlobalObject.class.getSimpleName();

    private static GlobalObject INSTANCE;
    public UserInfoPreference mUserInfo;
    public UserConfigPreference mUserConfig;
    public DBHelper mDBHelper = null;

    public static GlobalObject getInstance(Context context) {
        if (null == INSTANCE) {
            synchronized (GlobalObject.class) {
                INSTANCE = new GlobalObject(context);
            }
        }
        return INSTANCE;
    }

    private GlobalObject(Context context) {
        mUserInfo = new UserInfoPreference(context);
        mUserConfig = new UserConfigPreference(context);
        if (null == mDBHelper) {
            mDBHelper = new DBHelper(context);
        }
    }

    public Map<String, String> getCommomParams() {
        Map<String, String> params = new HashMap<>();
        String tokenStr = mUserInfo.getToken();
        if (TextUtils.isEmpty(tokenStr)) {
            params.put("token", "0");
        } else {
            params.put("token", tokenStr);
        }
        params.put("deviceType", "1");
        params.put("userId", mUserInfo.getUserId());// 版本V1.3.0添加
        params.put("deptId", mUserInfo.getDeptId());// 版本V2.0.0添加
        params.put("userType", mUserInfo.getUserType());// 版本V2.0.0添加
        return params;
    }

    /**
     * 店宝接口公共参数
     *
     * @return
     */
    public Map<String, String> getDianBaoParams() {
        Map<String, String> params = new HashMap<>();
        params.put("securityKey", "97237a67-38e9-11e6-867a-40a8f069c857");
        return params;
    }

    public UserInfoPreference getmUserInfo() {
        return mUserInfo;
    }

    public UserConfigPreference getmUserConfig() {
        return mUserConfig;
    }
}
